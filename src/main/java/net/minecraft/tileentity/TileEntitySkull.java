package net.minecraft.tileentity;

import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.properties.Property;
import java.util.UUID;
import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mojang.authlib.Agent;
import com.mojang.authlib.ProfileLookupCallback;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import net.minecraft.block.BlockSkull;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.StringUtils;

// Spigot start
import com.google.common.base.Predicate;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.util.concurrent.Futures;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mojang.authlib.Agent;
import com.mojang.authlib.ProfileLookupCallback;
import java.util.concurrent.Callable;
// Spigot end

public class TileEntitySkull extends TileEntity /*implements ITickable*/ { // Paper - remove tickable

    private int skullType;
    public int skullRotation;
    private GameProfile playerProfile;
    private int dragonAnimatedTicks;
    private boolean dragonAnimated;
    private static PlayerProfileCache profileCache;
    private static MinecraftSessionService sessionService;
    // Spigot start
    public static final ExecutorService executor = Executors.newFixedThreadPool(3,
            new ThreadFactoryBuilder()
                    .setNameFormat("Head Conversion Thread - %1$d")
                    .build()
    );
    public static final LoadingCache<String, GameProfile> skinCache = CacheBuilder.newBuilder()
            .maximumSize( 5000 )
            .expireAfterAccess( 60, TimeUnit.MINUTES )
            .build( new CacheLoader<String, GameProfile>()
            {
                @Override
                public GameProfile load(String key) throws Exception
                {
                    final GameProfile[] profiles = new GameProfile[1];
                    ProfileLookupCallback gameProfileLookup = new ProfileLookupCallback() {

                        @Override
                        public void onProfileLookupSucceeded(GameProfile gp) {
                            profiles[0] = gp;
                        }

                        @Override
                        public void onProfileLookupFailed(GameProfile gp, Exception excptn) {
                            profiles[0] = gp;
                        }
                    };

                    MinecraftServer.getServer().getGameProfileRepository().findProfilesByNames(new String[] { key }, Agent.MINECRAFT, gameProfileLookup);

                    GameProfile profile = profiles[ 0 ];
                    if (profile == null) {
                        UUID uuid = EntityPlayer.getUUID(new GameProfile(null, key));
                        profile = new GameProfile(uuid, key);

                        gameProfileLookup.onProfileLookupSucceeded(profile);
                    } else
                    {

                        Property property = Iterables.getFirst( profile.getProperties().get( "textures" ), null );

                        if ( property == null )
                        {
                            profile = MinecraftServer.getServer().getMinecraftSessionService().fillProfileProperties( profile, true );
                        }
                    }


                    return profile;
                }
            } );
    // Spigot end

    public TileEntitySkull() {}

    public static void setProfileCache(PlayerProfileCache usercache) {
        TileEntitySkull.profileCache = usercache;
    }

    public static void setSessionService(MinecraftSessionService minecraftsessionservice) {
        TileEntitySkull.sessionService = minecraftsessionservice;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setByte("SkullType", (byte) (this.skullType & 255));
        nbttagcompound.setByte("Rot", (byte) (this.skullRotation & 255));
        if (this.playerProfile != null) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();

            NBTUtil.writeGameProfile(nbttagcompound1, this.playerProfile);
            nbttagcompound.setTag("Owner", nbttagcompound1);
        }

        return nbttagcompound;
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.skullType = nbttagcompound.getByte("SkullType");
        this.skullRotation = nbttagcompound.getByte("Rot");
        if (this.skullType == 3) {
            if (nbttagcompound.hasKey("Owner", 10)) {
                this.playerProfile = NBTUtil.readGameProfileFromNBT(nbttagcompound.getCompoundTag("Owner"));
            } else if (nbttagcompound.hasKey("ExtraType", 8)) {
                String s = nbttagcompound.getString("ExtraType");

                if (!StringUtils.isNullOrEmpty(s)) {
                    this.playerProfile = new GameProfile((UUID) null, s);
                    this.updatePlayerProfile();
                }
            }
        }

    }

    public void update() {
        if (this.skullType == 5) {
            if (this.world.isBlockPowered(this.pos)) {
                this.dragonAnimated = true;
                ++this.dragonAnimatedTicks;
            } else {
                this.dragonAnimated = false;
            }
        }

    }

    @Nullable
    public GameProfile getPlayerProfile() {
        return this.playerProfile;
    }

    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 4, this.getUpdateTag());
    }

    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    public void setType(int i) {
        this.skullType = i;
        this.playerProfile = null;
    }

    public void setPlayerProfile(@Nullable GameProfile gameprofile) {
        this.skullType = 3;
        this.playerProfile = gameprofile;
        this.updatePlayerProfile();
    }

    private void updatePlayerProfile() {
        // Spigot start
        GameProfile profile = this.playerProfile;
        setType( 0 ); // Work around client bug
        b(profile, new Predicate<GameProfile>() {

            @Override
            public boolean apply(GameProfile input) {
                setType(3); // Work around client bug
                playerProfile = input;
                markDirty();
                if (world != null) {
                    world.notifyLightSet(pos); // PAIL: notify
                }
                return false;
            }
        }, false); 
        // Spigot end
    }

    // Spigot start - Support async lookups
    public static Future<GameProfile> b(final GameProfile gameprofile, final Predicate<GameProfile> callback, boolean sync) {
        if (gameprofile != null && !StringUtils.isNullOrEmpty(gameprofile.getName())) {
            if (gameprofile.isComplete() && gameprofile.getProperties().containsKey("textures")) {
                callback.apply(gameprofile);
            } else if (MinecraftServer.getServer() == null) {
                callback.apply(gameprofile);
            } else {
                GameProfile profile = skinCache.getIfPresent(gameprofile.getName().toLowerCase(java.util.Locale.ROOT));
                if (profile != null && Iterables.getFirst(profile.getProperties().get("textures"), (Object) null) != null) {
                    callback.apply(profile);

                    return Futures.immediateFuture(profile);
                } else {
                    Callable<GameProfile> callable = new Callable<GameProfile>() {
                        @Override
                        public GameProfile call() {
                            final GameProfile profile = skinCache.getUnchecked(gameprofile.getName().toLowerCase(java.util.Locale.ROOT));
                            MinecraftServer.getServer().processQueue.add(new Runnable() {
                                @Override
                                public void run() {
                                    if (profile == null) {
                                        callback.apply(gameprofile);
                                    } else {
                                        callback.apply(profile);
                                    }
                                }
                            });
                            return profile;
                        }
                    };
                    if (sync) {
                        try {
                            return Futures.immediateFuture(callable.call());
                        } catch (Exception ex) {
                            com.google.common.base.Throwables.throwIfUnchecked(ex);
                            throw new RuntimeException(ex); // Not possible
                        }
                    } else {
                        return executor.submit(callable);
                    }
                }
            }
        } else {
            callback.apply(gameprofile);
        }

        return Futures.immediateFuture(gameprofile);
    }
    // Spigot end

    public int getSkullType() {
        return this.skullType;
    }

    public void setSkullRotation(int i) {
        this.skullRotation = i;
    }

    public void mirror(Mirror enumblockmirror) {
        if (this.world != null && this.world.getBlockState(this.getPos()).getValue(BlockSkull.FACING) == EnumFacing.UP) {
            this.skullRotation = enumblockmirror.mirrorRotation(this.skullRotation, 16);
        }

    }

    public void rotate(Rotation enumblockrotation) {
        if (this.world != null && this.world.getBlockState(this.getPos()).getValue(BlockSkull.FACING) == EnumFacing.UP) {
            this.skullRotation = enumblockrotation.rotate(this.skullRotation, 16);
        }

    }
}
