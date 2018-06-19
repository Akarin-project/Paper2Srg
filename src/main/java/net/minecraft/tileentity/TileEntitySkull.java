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

    private int field_145908_a;
    public int field_145910_i;
    private GameProfile field_152110_j;
    private int field_184296_h;
    private boolean field_184297_i;
    private static PlayerProfileCache field_184298_j;
    private static MinecraftSessionService field_184299_k;
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

                    MinecraftServer.getServer().func_152359_aw().findProfilesByNames(new String[] { key }, Agent.MINECRAFT, gameProfileLookup);

                    GameProfile profile = profiles[ 0 ];
                    if (profile == null) {
                        UUID uuid = EntityPlayer.func_146094_a(new GameProfile(null, key));
                        profile = new GameProfile(uuid, key);

                        gameProfileLookup.onProfileLookupSucceeded(profile);
                    } else
                    {

                        Property property = Iterables.getFirst( profile.getProperties().get( "textures" ), null );

                        if ( property == null )
                        {
                            profile = MinecraftServer.getServer().func_147130_as().fillProfileProperties( profile, true );
                        }
                    }


                    return profile;
                }
            } );
    // Spigot end

    public TileEntitySkull() {}

    public static void func_184293_a(PlayerProfileCache usercache) {
        TileEntitySkull.field_184298_j = usercache;
    }

    public static void func_184294_a(MinecraftSessionService minecraftsessionservice) {
        TileEntitySkull.field_184299_k = minecraftsessionservice;
    }

    public NBTTagCompound func_189515_b(NBTTagCompound nbttagcompound) {
        super.func_189515_b(nbttagcompound);
        nbttagcompound.func_74774_a("SkullType", (byte) (this.field_145908_a & 255));
        nbttagcompound.func_74774_a("Rot", (byte) (this.field_145910_i & 255));
        if (this.field_152110_j != null) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();

            NBTUtil.func_180708_a(nbttagcompound1, this.field_152110_j);
            nbttagcompound.func_74782_a("Owner", nbttagcompound1);
        }

        return nbttagcompound;
    }

    public void func_145839_a(NBTTagCompound nbttagcompound) {
        super.func_145839_a(nbttagcompound);
        this.field_145908_a = nbttagcompound.func_74771_c("SkullType");
        this.field_145910_i = nbttagcompound.func_74771_c("Rot");
        if (this.field_145908_a == 3) {
            if (nbttagcompound.func_150297_b("Owner", 10)) {
                this.field_152110_j = NBTUtil.func_152459_a(nbttagcompound.func_74775_l("Owner"));
            } else if (nbttagcompound.func_150297_b("ExtraType", 8)) {
                String s = nbttagcompound.func_74779_i("ExtraType");

                if (!StringUtils.func_151246_b(s)) {
                    this.field_152110_j = new GameProfile((UUID) null, s);
                    this.func_152109_d();
                }
            }
        }

    }

    public void func_73660_a() {
        if (this.field_145908_a == 5) {
            if (this.field_145850_b.func_175640_z(this.field_174879_c)) {
                this.field_184297_i = true;
                ++this.field_184296_h;
            } else {
                this.field_184297_i = false;
            }
        }

    }

    @Nullable
    public GameProfile func_152108_a() {
        return this.field_152110_j;
    }

    @Nullable
    public SPacketUpdateTileEntity func_189518_D_() {
        return new SPacketUpdateTileEntity(this.field_174879_c, 4, this.func_189517_E_());
    }

    public NBTTagCompound func_189517_E_() {
        return this.func_189515_b(new NBTTagCompound());
    }

    public void func_152107_a(int i) {
        this.field_145908_a = i;
        this.field_152110_j = null;
    }

    public void func_152106_a(@Nullable GameProfile gameprofile) {
        this.field_145908_a = 3;
        this.field_152110_j = gameprofile;
        this.func_152109_d();
    }

    private void func_152109_d() {
        // Spigot start
        GameProfile profile = this.field_152110_j;
        func_152107_a( 0 ); // Work around client bug
        b(profile, new Predicate<GameProfile>() {

            @Override
            public boolean apply(GameProfile input) {
                func_152107_a(3); // Work around client bug
                field_152110_j = input;
                func_70296_d();
                if (field_145850_b != null) {
                    field_145850_b.func_175679_n(field_174879_c); // PAIL: notify
                }
                return false;
            }
        }, false); 
        // Spigot end
    }

    // Spigot start - Support async lookups
    public static Future<GameProfile> b(final GameProfile gameprofile, final Predicate<GameProfile> callback, boolean sync) {
        if (gameprofile != null && !StringUtils.func_151246_b(gameprofile.getName())) {
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

    public int func_145904_a() {
        return this.field_145908_a;
    }

    public void func_145903_a(int i) {
        this.field_145910_i = i;
    }

    public void func_189668_a(Mirror enumblockmirror) {
        if (this.field_145850_b != null && this.field_145850_b.func_180495_p(this.func_174877_v()).func_177229_b(BlockSkull.field_176418_a) == EnumFacing.UP) {
            this.field_145910_i = enumblockmirror.func_185802_a(this.field_145910_i, 16);
        }

    }

    public void func_189667_a(Rotation enumblockrotation) {
        if (this.field_145850_b != null && this.field_145850_b.func_180495_p(this.func_174877_v()).func_177229_b(BlockSkull.field_176418_a) == EnumFacing.UP) {
            this.field_145910_i = enumblockrotation.func_185833_a(this.field_145910_i, 16);
        }

    }
}
