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

    public int field_76201_a;
    public int field_76199_b;
    public byte field_76200_c;
    public boolean field_186210_e;
    public boolean field_191096_f;
    public byte field_76197_d;
    public byte[] field_76198_e = new byte[16384];
    public List<MapData.MapInfo> field_76196_g = Lists.newArrayList();
    public final Map<EntityPlayer, MapData.MapInfo> field_76202_j = Maps.newHashMap(); // Spigot private -> public
    public Map<UUID, MapDecoration> field_76203_h = Maps.newLinkedHashMap(); // Spigot
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
        vanillaRender.buffer = field_76198_e; // Paper
        // CraftBukkit end
    }

    public void func_176054_a(double d0, double d1, int i) {
        int j = 128 * (1 << i);
        int k = MathHelper.func_76128_c((d0 + 64.0D) / (double) j);
        int l = MathHelper.func_76128_c((d1 + 64.0D) / (double) j);

        this.field_76201_a = k * j + j / 2 - 64;
        this.field_76199_b = l * j + j / 2 - 64;
    }

    public void func_76184_a(NBTTagCompound nbttagcompound) {
        // CraftBukkit start
        byte dimension = nbttagcompound.func_74771_c("dimension");

        if (dimension >= 10) {
            long least = nbttagcompound.func_74763_f("UUIDLeast");
            long most = nbttagcompound.func_74763_f("UUIDMost");

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

        this.field_76200_c = dimension;
        // CraftBukkit end
        this.field_76201_a = nbttagcompound.func_74762_e("xCenter");
        this.field_76199_b = nbttagcompound.func_74762_e("zCenter");
        this.field_76197_d = nbttagcompound.func_74771_c("scale");
        this.field_76197_d = (byte) MathHelper.func_76125_a(this.field_76197_d, 0, 4);
        if (nbttagcompound.func_150297_b("trackingPosition", 1)) {
            this.field_186210_e = nbttagcompound.func_74767_n("trackingPosition");
        } else {
            this.field_186210_e = true;
        }

        this.field_191096_f = nbttagcompound.func_74767_n("unlimitedTracking");
        short short0 = nbttagcompound.func_74765_d("width");
        short short1 = nbttagcompound.func_74765_d("height");

        if (short0 == 128 && short1 == 128) {
            this.field_76198_e = nbttagcompound.func_74770_j("colors");
        } else {
            byte[] abyte = nbttagcompound.func_74770_j("colors");

            this.field_76198_e = new byte[16384];
            int i = (128 - short0) / 2;
            int j = (128 - short1) / 2;

            for (int k = 0; k < short1; ++k) {
                int l = k + j;

                if (l >= 0 || l < 128) {
                    for (int i1 = 0; i1 < short0; ++i1) {
                        int j1 = i1 + i;

                        if (j1 >= 0 || j1 < 128) {
                            this.field_76198_e[j1 + l * 128] = abyte[i1 + k * short0];
                        }
                    }
                }
            }
        }
        vanillaRender.buffer = field_76198_e; // Paper

    }

    public NBTTagCompound func_189551_b(NBTTagCompound nbttagcompound) {
        // CraftBukkit start
        if (this.field_76200_c >= 10) {
            if (this.uniqueId == null) {
                for (org.bukkit.World world : server.getWorlds()) {
                    CraftWorld cWorld = (CraftWorld) world;
                    if (cWorld.getHandle().dimension == this.field_76200_c) {
                        this.uniqueId = cWorld.getUID();
                        break;
                    }
                }
            }
            /* Perform a second check to see if a matching world was found, this is a necessary
               change incase Maps are forcefully unlinked from a World and lack a UID.*/
            if (this.uniqueId != null) {
                nbttagcompound.func_74772_a("UUIDLeast", this.uniqueId.getLeastSignificantBits());
                nbttagcompound.func_74772_a("UUIDMost", this.uniqueId.getMostSignificantBits());
            }
        }
        // CraftBukkit end
        nbttagcompound.func_74774_a("dimension", this.field_76200_c);
        nbttagcompound.func_74768_a("xCenter", this.field_76201_a);
        nbttagcompound.func_74768_a("zCenter", this.field_76199_b);
        nbttagcompound.func_74774_a("scale", this.field_76197_d);
        nbttagcompound.func_74777_a("width", (short) 128);
        nbttagcompound.func_74777_a("height", (short) 128);
        nbttagcompound.func_74773_a("colors", this.field_76198_e);
        nbttagcompound.func_74757_a("trackingPosition", this.field_186210_e);
        nbttagcompound.func_74757_a("unlimitedTracking", this.field_191096_f);
        return nbttagcompound;
    }

    public void updateSeenPlayers(EntityPlayer entityhuman, ItemStack itemstack) { func_76191_a(entityhuman, itemstack); } // Paper - OBFHELPER
    public void func_76191_a(EntityPlayer entityhuman, ItemStack itemstack) {
        if (!this.field_76202_j.containsKey(entityhuman)) {
            MapData.MapInfo worldmap_worldmaphumantracker = new MapData.MapInfo(entityhuman);

            this.field_76202_j.put(entityhuman, worldmap_worldmaphumantracker);
            this.field_76196_g.add(worldmap_worldmaphumantracker);
        }

        if (!entityhuman.field_71071_by.func_70431_c(itemstack)) {
            this.field_76203_h.remove(entityhuman.func_110124_au()); // Spigot
        }

        for (int i = 0; i < this.field_76196_g.size(); ++i) {
            MapData.MapInfo worldmap_worldmaphumantracker1 = (MapData.MapInfo) this.field_76196_g.get(i);

            if (!worldmap_worldmaphumantracker1.field_76211_a.field_70128_L && (worldmap_worldmaphumantracker1.field_76211_a.field_71071_by.func_70431_c(itemstack) || itemstack.func_82839_y())) {
                if (!itemstack.func_82839_y() && worldmap_worldmaphumantracker1.field_76211_a.field_71093_bK == this.field_76200_c && this.field_186210_e) {
                    this.a(MapDecoration.Type.PLAYER, worldmap_worldmaphumantracker1.field_76211_a.field_70170_p, worldmap_worldmaphumantracker1.field_76211_a.func_110124_au(), worldmap_worldmaphumantracker1.field_76211_a.field_70165_t, worldmap_worldmaphumantracker1.field_76211_a.field_70161_v, (double) worldmap_worldmaphumantracker1.field_76211_a.field_70177_z); // Spigot
                }
            } else {
                this.field_76202_j.remove(worldmap_worldmaphumantracker1.field_76211_a);
                this.field_76196_g.remove(worldmap_worldmaphumantracker1);
            }
        }

        if (itemstack.func_82839_y() && this.field_186210_e) {
            EntityItemFrame entityitemframe = itemstack.func_82836_z();
            BlockPos blockposition = entityitemframe.func_174857_n();

            this.a(MapDecoration.Type.FRAME, entityhuman.field_70170_p, UUID.nameUUIDFromBytes(("frame-" + entityitemframe.func_145782_y()).getBytes(Charsets.US_ASCII)), (double) blockposition.func_177958_n(), (double) blockposition.func_177952_p(), (double) (entityitemframe.field_174860_b.func_176736_b() * 90)); // Spigot
        }

        if (itemstack.func_77942_o() && itemstack.func_77978_p().func_150297_b("Decorations", 9)) {
            NBTTagList nbttaglist = itemstack.func_77978_p().func_150295_c("Decorations", 10);

            for (int j = 0; j < nbttaglist.func_74745_c(); ++j) {
                NBTTagCompound nbttagcompound = nbttaglist.func_150305_b(j);

                // Spigot - start
                UUID uuid = UUID.nameUUIDFromBytes(nbttagcompound.func_74779_i("id").getBytes(Charsets.US_ASCII));
                if (!this.field_76203_h.containsKey(uuid)) {
                    this.a(MapDecoration.Type.func_191159_a(nbttagcompound.func_74771_c("type")), entityhuman.field_70170_p, uuid, nbttagcompound.func_74769_h("x"), nbttagcompound.func_74769_h("z"), nbttagcompound.func_74769_h("rot"));
                    // Spigot - end
                }
            }
        }

    }

    public static void func_191094_a(ItemStack itemstack, BlockPos blockposition, String s, MapDecoration.Type mapicon_type) {
        NBTTagList nbttaglist;

        if (itemstack.func_77942_o() && itemstack.func_77978_p().func_150297_b("Decorations", 9)) {
            nbttaglist = itemstack.func_77978_p().func_150295_c("Decorations", 10);
        } else {
            nbttaglist = new NBTTagList();
            itemstack.func_77983_a("Decorations", (NBTBase) nbttaglist);
        }

        NBTTagCompound nbttagcompound = new NBTTagCompound();

        nbttagcompound.func_74774_a("type", mapicon_type.func_191163_a());
        nbttagcompound.func_74778_a("id", s);
        nbttagcompound.func_74780_a("x", (double) blockposition.func_177958_n());
        nbttagcompound.func_74780_a("z", (double) blockposition.func_177952_p());
        nbttagcompound.func_74780_a("rot", 180.0D);
        nbttaglist.func_74742_a(nbttagcompound);
        if (mapicon_type.func_191162_c()) {
            NBTTagCompound nbttagcompound1 = itemstack.func_190925_c("display");

            nbttagcompound1.func_74768_a("MapColor", mapicon_type.func_191161_d());
        }

    }

    private void a(MapDecoration.Type mapicon_type, World world, UUID s, double d0, double d1, double d2) { // Spigot; string->uuid
        int i = 1 << this.field_76197_d;
        float f = (float) (d0 - (double) this.field_76201_a) / (float) i;
        float f1 = (float) (d1 - (double) this.field_76199_b) / (float) i;
        byte b0 = (byte) ((int) ((double) (f * 2.0F) + 0.5D));
        byte b1 = (byte) ((int) ((double) (f1 * 2.0F) + 0.5D));
        boolean flag = true;
        byte b2;

        if (f >= -63.0F && f1 >= -63.0F && f <= 63.0F && f1 <= 63.0F) {
            d2 += d2 < 0.0D ? -8.0D : 8.0D;
            b2 = (byte) ((int) (d2 * 16.0D / 360.0D));
            if (this.field_76200_c < 0) {
                int j = (int) (world.func_72912_H().func_76073_f() / 10L);

                b2 = (byte) (j * j * 34187121 + j * 121 >> 15 & 15);
            }
        } else {
            if (mapicon_type != MapDecoration.Type.PLAYER) {
                this.field_76203_h.remove(s);
                return;
            }

            boolean flag1 = true;

            if (Math.abs(f) < 320.0F && Math.abs(f1) < 320.0F) {
                mapicon_type = MapDecoration.Type.PLAYER_OFF_MAP;
            } else {
                if (!this.field_191096_f) {
                    this.field_76203_h.remove(s);
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

        this.field_76203_h.put(s, new MapDecoration(mapicon_type, b0, b1, b2));
    }

    @Nullable
    public Packet<?> func_176052_a(ItemStack itemstack, World world, EntityPlayer entityhuman) {
        MapData.MapInfo worldmap_worldmaphumantracker = (MapData.MapInfo) this.field_76202_j.get(entityhuman);

        return worldmap_worldmaphumantracker == null ? null : worldmap_worldmaphumantracker.func_176101_a(itemstack);
    }

    public void func_176053_a(int i, int j) {
        super.func_76185_a();
        Iterator iterator = this.field_76196_g.iterator();

        while (iterator.hasNext()) {
            MapData.MapInfo worldmap_worldmaphumantracker = (MapData.MapInfo) iterator.next();

            worldmap_worldmaphumantracker.func_176102_a(i, j);
        }

    }

    public MapData.MapInfo func_82568_a(EntityPlayer entityhuman) {
        MapData.MapInfo worldmap_worldmaphumantracker = (MapData.MapInfo) this.field_76202_j.get(entityhuman);

        if (worldmap_worldmaphumantracker == null) {
            worldmap_worldmaphumantracker = new MapData.MapInfo(entityhuman);
            this.field_76202_j.put(entityhuman, worldmap_worldmaphumantracker);
            this.field_76196_g.add(worldmap_worldmaphumantracker);
        }

        return worldmap_worldmaphumantracker;
    }

    public class MapInfo {

        // Paper start
        private void addSeenPlayers(java.util.Collection<MapDecoration> icons) {
            org.bukkit.entity.Player player = (org.bukkit.entity.Player) field_76211_a.getBukkitEntity();
            MapData.this.field_76203_h.forEach((uuid, mapIcon) -> {
                // If this cursor is for a player check visibility with vanish system
                org.bukkit.entity.Player other = org.bukkit.Bukkit.getPlayer(uuid); // Spigot
                if (other == null || player.canSee(other)) {
                    icons.add(mapIcon);
                }
            });
        }
        private boolean shouldUseVanillaMap() {
            return mapView.getRenderers().size() == 1 && mapView.getRenderers().get(0).getClass() == org.bukkit.craftbukkit.map.CraftMapRenderer.class;
        }
        // Paper stop
        public final EntityPlayer field_76211_a;
        private boolean field_176105_d = true;
        private int field_176106_e;
        private int field_176103_f;
        private int field_176104_g = 127;
        private int field_176108_h = 127;
        private int field_176109_i;
        public int field_82569_d;

        public MapInfo(EntityPlayer entityhuman) {
            this.field_76211_a = entityhuman;
        }

        @Nullable
        public Packet<?> func_176101_a(ItemStack itemstack) {
            // CraftBukkit start
            if (!this.field_176105_d && this.field_176109_i % 5 != 0) { this.field_176109_i++; return null; } // Paper - this won't end up sending, so don't render it!
            boolean vanillaMaps = shouldUseVanillaMap(); // Paper
            org.bukkit.craftbukkit.map.RenderData render = !vanillaMaps ? MapData.this.mapView.render((org.bukkit.craftbukkit.entity.CraftPlayer) this.field_76211_a.getBukkitEntity()) : MapData.this.vanillaRender; // CraftBukkit // Paper

            java.util.Collection<MapDecoration> icons = new java.util.ArrayList<MapDecoration>();
            if (vanillaMaps) addSeenPlayers(icons); // Paper

            for ( org.bukkit.map.MapCursor cursor : render.cursors) {

                if (cursor.isVisible()) {
                    icons.add(new MapDecoration(MapDecoration.Type.func_191159_a(cursor.getRawType()), cursor.getX(), cursor.getY(), cursor.getDirection()));
                }
            }

            if (this.field_176105_d) {
                this.field_176105_d = false;
                // PAIL: this.e
                return new SPacketMaps(itemstack.func_77960_j(), MapData.this.field_76197_d, MapData.this.field_186210_e, icons, render.buffer, this.field_176106_e, this.field_176103_f, this.field_176104_g + 1 - this.field_176106_e, this.field_176108_h + 1 - this.field_176103_f);
            } else {
                return this.field_176109_i++ % 5 == 0 ? new SPacketMaps(itemstack.func_77960_j(), MapData.this.field_76197_d, MapData.this.field_186210_e, icons, render.buffer, 0, 0, 0, 0) : null;
            }
            // CraftBukkit end
        }

        public void func_176102_a(int i, int j) {
            if (this.field_176105_d) {
                this.field_176106_e = Math.min(this.field_176106_e, i);
                this.field_176103_f = Math.min(this.field_176103_f, j);
                this.field_176104_g = Math.max(this.field_176104_g, i);
                this.field_176108_h = Math.max(this.field_176108_h, j);
            } else {
                this.field_176105_d = true;
                this.field_176106_e = i;
                this.field_176103_f = j;
                this.field_176104_g = i;
                this.field_176108_h = j;
            }

        }
    }
}
