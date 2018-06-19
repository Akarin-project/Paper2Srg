package net.minecraft.world.storage;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

import java.util.UUID;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketMaps;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.map.CraftMapView;

// CraftBukkit start
import java.util.UUID;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.map.CraftMapView;
// CraftBukkit end

public class MapData extends WorldSavedData {

    public int xCenter;
    public int zCenter;
    public byte dimension;
    public boolean trackingPosition;
    public boolean unlimitedTracking;
    public byte scale;
    public byte[] colors = new byte[16384];
    public List<MapData.MapInfo> playersArrayList = Lists.newArrayList();
    public final Map<EntityPlayer, MapData.MapInfo> playersHashMap = Maps.newHashMap(); // Spigot private -> public
    public Map<UUID, MapDecoration> mapDecorations = Maps.newLinkedHashMap(); // Spigot
    private org.bukkit.craftbukkit.map.RenderData vanillaRender = new org.bukkit.craftbukkit.map.RenderData(); // Paper

    // CraftBukkit start
    public final CraftMapView mapView;
    private CraftServer server;
    private UUID uniqueId = null;
    // CraftBukkit end

    public MapData(String s) {
        super(s);
        // CraftBukkit start
        mapView = new CraftMapView(this);
        server = (CraftServer) org.bukkit.Bukkit.getServer();
        vanillaRender.buffer = colors; // Paper
        // CraftBukkit end
    }

    public void calculateMapCenter(double d0, double d1, int i) {
        int j = 128 * (1 << i);
        int k = MathHelper.floor((d0 + 64.0D) / j);
        int l = MathHelper.floor((d1 + 64.0D) / j);

        this.xCenter = k * j + j / 2 - 64;
        this.zCenter = l * j + j / 2 - 64;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        // CraftBukkit start
        byte dimension = nbttagcompound.getByte("dimension");

        if (dimension >= 10) {
            long least = nbttagcompound.getLong("UUIDLeast");
            long most = nbttagcompound.getLong("UUIDMost");

            if (least != 0L && most != 0L) {
                this.uniqueId = new UUID(most, least);

                CraftWorld world = (CraftWorld) server.getWorld(this.uniqueId);
                // Check if the stored world details are correct.
                if (world == null) {
                    /* All Maps which do not have their valid world loaded are set to a dimension which hopefully won't be reached.
                       This is to prevent them being corrupted with the wrong map data. */
                    dimension = 127;
                } else {
                    dimension = (byte) world.getHandle().dimension;
                }
            }
        }

        this.dimension = dimension;
        // CraftBukkit end
        this.xCenter = nbttagcompound.getInteger("xCenter");
        this.zCenter = nbttagcompound.getInteger("zCenter");
        this.scale = nbttagcompound.getByte("scale");
        this.scale = (byte) MathHelper.clamp(this.scale, 0, 4);
        if (nbttagcompound.hasKey("trackingPosition", 1)) {
            this.trackingPosition = nbttagcompound.getBoolean("trackingPosition");
        } else {
            this.trackingPosition = true;
        }

        this.unlimitedTracking = nbttagcompound.getBoolean("unlimitedTracking");
        short short0 = nbttagcompound.getShort("width");
        short short1 = nbttagcompound.getShort("height");

        if (short0 == 128 && short1 == 128) {
            this.colors = nbttagcompound.getByteArray("colors");
        } else {
            byte[] abyte = nbttagcompound.getByteArray("colors");

            this.colors = new byte[16384];
            int i = (128 - short0) / 2;
            int j = (128 - short1) / 2;

            for (int k = 0; k < short1; ++k) {
                int l = k + j;

                if (l >= 0 || l < 128) {
                    for (int i1 = 0; i1 < short0; ++i1) {
                        int j1 = i1 + i;

                        if (j1 >= 0 || j1 < 128) {
                            this.colors[j1 + l * 128] = abyte[i1 + k * short0];
                        }
                    }
                }
            }
        }
        vanillaRender.buffer = colors; // Paper

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        // CraftBukkit start
        if (this.dimension >= 10) {
            if (this.uniqueId == null) {
                for (org.bukkit.World world : server.getWorlds()) {
                    CraftWorld cWorld = (CraftWorld) world;
                    if (cWorld.getHandle().dimension == this.dimension) {
                        this.uniqueId = cWorld.getUID();
                        break;
                    }
                }
            }
            /* Perform a second check to see if a matching world was found, this is a necessary
               change incase Maps are forcefully unlinked from a World and lack a UID.*/
            if (this.uniqueId != null) {
                nbttagcompound.setLong("UUIDLeast", this.uniqueId.getLeastSignificantBits());
                nbttagcompound.setLong("UUIDMost", this.uniqueId.getMostSignificantBits());
            }
        }
        // CraftBukkit end
        nbttagcompound.setByte("dimension", this.dimension);
        nbttagcompound.setInteger("xCenter", this.xCenter);
        nbttagcompound.setInteger("zCenter", this.zCenter);
        nbttagcompound.setByte("scale", this.scale);
        nbttagcompound.setShort("width", (short) 128);
        nbttagcompound.setShort("height", (short) 128);
        nbttagcompound.setByteArray("colors", this.colors);
        nbttagcompound.setBoolean("trackingPosition", this.trackingPosition);
        nbttagcompound.setBoolean("unlimitedTracking", this.unlimitedTracking);
        return nbttagcompound;
    }

    public void updateSeenPlayers(EntityPlayer entityhuman, ItemStack itemstack) { updateVisiblePlayers(entityhuman, itemstack); } // Paper - OBFHELPER
    public void updateVisiblePlayers(EntityPlayer entityhuman, ItemStack itemstack) {
        if (!this.playersHashMap.containsKey(entityhuman)) {
            MapData.MapInfo worldmap_worldmaphumantracker = new MapData.MapInfo(entityhuman);

            this.playersHashMap.put(entityhuman, worldmap_worldmaphumantracker);
            this.playersArrayList.add(worldmap_worldmaphumantracker);
        }

        if (!entityhuman.inventory.hasItemStack(itemstack)) {
            this.mapDecorations.remove(entityhuman.getUniqueID()); // Spigot
        }

        for (int i = 0; i < this.playersArrayList.size(); ++i) {
            MapData.MapInfo worldmap_worldmaphumantracker1 = this.playersArrayList.get(i);

            if (!worldmap_worldmaphumantracker1.player.isDead && (worldmap_worldmaphumantracker1.player.inventory.hasItemStack(itemstack) || itemstack.isOnItemFrame())) {
                if (!itemstack.isOnItemFrame() && worldmap_worldmaphumantracker1.player.dimension == this.dimension && this.trackingPosition) {
                    this.a(MapDecoration.Type.PLAYER, worldmap_worldmaphumantracker1.player.world, worldmap_worldmaphumantracker1.player.getUniqueID(), worldmap_worldmaphumantracker1.player.posX, worldmap_worldmaphumantracker1.player.posZ, worldmap_worldmaphumantracker1.player.rotationYaw); // Spigot
                }
            } else {
                this.playersHashMap.remove(worldmap_worldmaphumantracker1.player);
                this.playersArrayList.remove(worldmap_worldmaphumantracker1);
            }
        }

        if (itemstack.isOnItemFrame() && this.trackingPosition) {
            EntityItemFrame entityitemframe = itemstack.getItemFrame();
            BlockPos blockposition = entityitemframe.getHangingPosition();

            this.a(MapDecoration.Type.FRAME, entityhuman.world, UUID.nameUUIDFromBytes(("frame-" + entityitemframe.getEntityId()).getBytes(Charsets.US_ASCII)), blockposition.getX(), blockposition.getZ(), entityitemframe.facingDirection.getHorizontalIndex() * 90); // Spigot
        }

        if (itemstack.hasTagCompound() && itemstack.getTagCompound().hasKey("Decorations", 9)) {
            NBTTagList nbttaglist = itemstack.getTagCompound().getTagList("Decorations", 10);

            for (int j = 0; j < nbttaglist.tagCount(); ++j) {
                NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(j);

                // Spigot - start
                UUID uuid = UUID.nameUUIDFromBytes(nbttagcompound.getString("id").getBytes(Charsets.US_ASCII));
                if (!this.mapDecorations.containsKey(uuid)) {
                    this.a(MapDecoration.Type.byIcon(nbttagcompound.getByte("type")), entityhuman.world, uuid, nbttagcompound.getDouble("x"), nbttagcompound.getDouble("z"), nbttagcompound.getDouble("rot"));
                    // Spigot - end
                }
            }
        }

    }

    public static void addTargetDecoration(ItemStack itemstack, BlockPos blockposition, String s, MapDecoration.Type mapicon_type) {
        NBTTagList nbttaglist;

        if (itemstack.hasTagCompound() && itemstack.getTagCompound().hasKey("Decorations", 9)) {
            nbttaglist = itemstack.getTagCompound().getTagList("Decorations", 10);
        } else {
            nbttaglist = new NBTTagList();
            itemstack.setTagInfo("Decorations", nbttaglist);
        }

        NBTTagCompound nbttagcompound = new NBTTagCompound();

        nbttagcompound.setByte("type", mapicon_type.getIcon());
        nbttagcompound.setString("id", s);
        nbttagcompound.setDouble("x", blockposition.getX());
        nbttagcompound.setDouble("z", blockposition.getZ());
        nbttagcompound.setDouble("rot", 180.0D);
        nbttaglist.appendTag(nbttagcompound);
        if (mapicon_type.hasMapColor()) {
            NBTTagCompound nbttagcompound1 = itemstack.getOrCreateSubCompound("display");

            nbttagcompound1.setInteger("MapColor", mapicon_type.getMapColor());
        }

    }

    private void a(MapDecoration.Type mapicon_type, World world, UUID s, double d0, double d1, double d2) { // Spigot; string->uuid
        int i = 1 << this.scale;
        float f = (float) (d0 - this.xCenter) / i;
        float f1 = (float) (d1 - this.zCenter) / i;
        byte b0 = (byte) ((int) (f * 2.0F + 0.5D));
        byte b1 = (byte) ((int) (f1 * 2.0F + 0.5D));
        boolean flag = true;
        byte b2;

        if (f >= -63.0F && f1 >= -63.0F && f <= 63.0F && f1 <= 63.0F) {
            d2 += d2 < 0.0D ? -8.0D : 8.0D;
            b2 = (byte) ((int) (d2 * 16.0D / 360.0D));
            if (this.dimension < 0) {
                int j = (int) (world.getWorldInfo().getWorldTime() / 10L);

                b2 = (byte) (j * j * 34187121 + j * 121 >> 15 & 15);
            }
        } else {
            if (mapicon_type != MapDecoration.Type.PLAYER) {
                this.mapDecorations.remove(s);
                return;
            }

            boolean flag1 = true;

            if (Math.abs(f) < 320.0F && Math.abs(f1) < 320.0F) {
                mapicon_type = MapDecoration.Type.PLAYER_OFF_MAP;
            } else {
                if (!this.unlimitedTracking) {
                    this.mapDecorations.remove(s);
                    return;
                }

                mapicon_type = MapDecoration.Type.PLAYER_OFF_LIMITS;
            }

            b2 = 0;
            if (f <= -63.0F) {
                b0 = -128;
            }

            if (f1 <= -63.0F) {
                b1 = -128;
            }

            if (f >= 63.0F) {
                b0 = 127;
            }

            if (f1 >= 63.0F) {
                b1 = 127;
            }
        }

        this.mapDecorations.put(s, new MapDecoration(mapicon_type, b0, b1, b2));
    }

    @Nullable
    public Packet<?> getMapPacket(ItemStack itemstack, World world, EntityPlayer entityhuman) {
        MapData.MapInfo worldmap_worldmaphumantracker = this.playersHashMap.get(entityhuman);

        return worldmap_worldmaphumantracker == null ? null : worldmap_worldmaphumantracker.getPacket(itemstack);
    }

    public void updateMapData(int i, int j) {
        super.markDirty();
        Iterator iterator = this.playersArrayList.iterator();

        while (iterator.hasNext()) {
            MapData.MapInfo worldmap_worldmaphumantracker = (MapData.MapInfo) iterator.next();

            worldmap_worldmaphumantracker.update(i, j);
        }

    }

    public MapData.MapInfo getMapInfo(EntityPlayer entityhuman) {
        MapData.MapInfo worldmap_worldmaphumantracker = this.playersHashMap.get(entityhuman);

        if (worldmap_worldmaphumantracker == null) {
            worldmap_worldmaphumantracker = new MapData.MapInfo(entityhuman);
            this.playersHashMap.put(entityhuman, worldmap_worldmaphumantracker);
            this.playersArrayList.add(worldmap_worldmaphumantracker);
        }

        return worldmap_worldmaphumantracker;
    }

    public class MapInfo {

        // Paper start
        private void addSeenPlayers(java.util.Collection<MapDecoration> icons) {
            org.bukkit.entity.Player bukkitPlayer = (org.bukkit.entity.Player) player.getBukkitEntity();
            MapData.this.mapDecorations.forEach((uuid, mapIcon) -> {
                // If this cursor is for a player check visibility with vanish system
                org.bukkit.entity.Player other = org.bukkit.Bukkit.getPlayer(uuid); // Spigot
                if (other == null || bukkitPlayer.canSee(other)) {
                    icons.add(mapIcon);
                }
            });
        }
        private boolean shouldUseVanillaMap() {
            return mapView.getRenderers().size() == 1 && mapView.getRenderers().get(0).getClass() == org.bukkit.craftbukkit.map.CraftMapRenderer.class;
        }
        // Paper stop
        public final EntityPlayer player;
        private boolean isDirty = true;
        private int minX;
        private int minY;
        private int maxX = 127;
        private int maxY = 127;
        private int tick;
        public int step;

        public MapInfo(EntityPlayer entityhuman) {
            this.player = entityhuman;
        }

        @Nullable
        public Packet<?> getPacket(ItemStack itemstack) {
            // CraftBukkit start
            if (!this.isDirty && this.tick % 5 != 0) { this.tick++; return null; } // Paper - this won't end up sending, so don't render it!
            boolean vanillaMaps = shouldUseVanillaMap(); // Paper
            org.bukkit.craftbukkit.map.RenderData render = !vanillaMaps ? MapData.this.mapView.render((org.bukkit.craftbukkit.entity.CraftPlayer) this.player.getBukkitEntity()) : MapData.this.vanillaRender; // CraftBukkit // Paper

            java.util.Collection<MapDecoration> icons = new java.util.ArrayList<MapDecoration>();
            if (vanillaMaps) addSeenPlayers(icons); // Paper

            for ( org.bukkit.map.MapCursor cursor : render.cursors) {

                if (cursor.isVisible()) {
                    icons.add(new MapDecoration(MapDecoration.Type.byIcon(cursor.getRawType()), cursor.getX(), cursor.getY(), cursor.getDirection()));
                }
            }

            if (this.isDirty) {
                this.isDirty = false;
                // PAIL: this.e
                return new SPacketMaps(itemstack.getMetadata(), MapData.this.scale, MapData.this.trackingPosition, icons, render.buffer, this.minX, this.minY, this.maxX + 1 - this.minX, this.maxY + 1 - this.minY);
            } else {
                return this.tick++ % 5 == 0 ? new SPacketMaps(itemstack.getMetadata(), MapData.this.scale, MapData.this.trackingPosition, icons, render.buffer, 0, 0, 0, 0) : null;
            }
            // CraftBukkit end
        }

        public void update(int i, int j) {
            if (this.isDirty) {
                this.minX = Math.min(this.minX, i);
                this.minY = Math.min(this.minY, j);
                this.maxX = Math.max(this.maxX, i);
                this.maxY = Math.max(this.maxY, j);
            } else {
                this.isDirty = true;
                this.minX = i;
                this.minY = j;
                this.maxX = i;
                this.maxY = j;
            }

        }
    }
}
