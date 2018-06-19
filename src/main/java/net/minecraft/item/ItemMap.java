package net.minecraft.item;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multisets;
import javax.annotation.Nullable;

import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockStone;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.WorldSavedData;
import org.bukkit.Bukkit;
import org.bukkit.event.server.MapInitializeEvent;

// CraftBukkit start
import org.bukkit.Bukkit;
import org.bukkit.event.server.MapInitializeEvent;
// CraftBukkit end

public class ItemMap extends ItemMapBase {

    protected ItemMap() {
        this.func_77627_a(true);
    }

    public static ItemStack func_190906_a(World world, double d0, double d1, byte b0, boolean flag, boolean flag1) {
        World worldMain = world.getServer().getServer().worlds.get(0); // CraftBukkit - store reference to primary world
        ItemStack itemstack = new ItemStack(Items.field_151098_aY, 1, worldMain.func_72841_b("map")); // CraftBukkit - use primary world for maps
        String s = "map_" + itemstack.func_77960_j();
        MapData worldmap = new MapData(s);

        worldMain.func_72823_a(s, (WorldSavedData) worldmap); // CraftBukkit
        worldmap.field_76197_d = b0;
        worldmap.func_176054_a(d0, d1, worldmap.field_76197_d);
        worldmap.field_76200_c = (byte) ((WorldServer) world).dimension; // CraftBukkit - use bukkit dimension
        worldmap.field_186210_e = flag;
        worldmap.field_191096_f = flag1;
        worldmap.func_76185_a();
        org.bukkit.craftbukkit.event.CraftEventFactory.callEvent(new org.bukkit.event.server.MapInitializeEvent(worldmap.mapView)); // CraftBukkit
        return itemstack;
    }

    @Nullable
    public MapData func_77873_a(ItemStack itemstack, World world) {
        World worldMain = world.getServer().getServer().worlds.get(0); // CraftBukkit - store reference to primary world
        String s = "map_" + itemstack.func_77960_j();
        MapData worldmap = (MapData) worldMain.func_72943_a(MapData.class, s); // CraftBukkit - use primary world for maps

        if (worldmap == null && !world.field_72995_K) {
            itemstack.func_77964_b(worldMain.func_72841_b("map")); // CraftBukkit - use primary world for maps
            s = "map_" + itemstack.func_77960_j();
            worldmap = new MapData(s);
            worldmap.field_76197_d = 3;
            worldmap.func_176054_a((double) world.func_72912_H().func_76079_c(), (double) world.func_72912_H().func_76074_e(), worldmap.field_76197_d);
            worldmap.field_76200_c = (byte) ((WorldServer) world).dimension; // CraftBukkit - fixes Bukkit multiworld maps
            worldmap.func_76185_a();
            worldMain.func_72823_a(s, (WorldSavedData) worldmap); // CraftBukkit - use primary world for maps

            // CraftBukkit start
            MapInitializeEvent event = new MapInitializeEvent(worldmap.mapView);
            Bukkit.getServer().getPluginManager().callEvent(event);
            // CraftBukkit end
        }

        return worldmap;
    }

    public void func_77872_a(World world, Entity entity, MapData worldmap) {
        // CraftBukkit - world.worldProvider -> ((WorldServer) world)
        if (((WorldServer) world).dimension == worldmap.field_76200_c && entity instanceof EntityPlayer) {
            int i = 1 << worldmap.field_76197_d;
            int j = worldmap.field_76201_a;
            int k = worldmap.field_76199_b;
            int l = MathHelper.func_76128_c(entity.field_70165_t - (double) j) / i + 64;
            int i1 = MathHelper.func_76128_c(entity.field_70161_v - (double) k) / i + 64;
            int j1 = 128 / i;

            if (world.field_73011_w.func_177495_o()) {
                j1 /= 2;
            }

            MapData.MapInfo worldmap_worldmaphumantracker = worldmap.func_82568_a((EntityPlayer) entity);

            ++worldmap_worldmaphumantracker.field_82569_d;
            boolean flag = false;

            for (int k1 = l - j1 + 1; k1 < l + j1; ++k1) {
                if ((k1 & 15) == (worldmap_worldmaphumantracker.field_82569_d & 15) || flag) {
                    flag = false;
                    double d0 = 0.0D;

                    for (int l1 = i1 - j1 - 1; l1 < i1 + j1; ++l1) {
                        if (k1 >= 0 && l1 >= -1 && k1 < 128 && l1 < 128) {
                            int i2 = k1 - l;
                            int j2 = l1 - i1;
                            boolean flag1 = i2 * i2 + j2 * j2 > (j1 - 2) * (j1 - 2);
                            int k2 = (j / i + k1 - 64) * i;
                            int l2 = (k / i + l1 - 64) * i;
                            HashMultiset hashmultiset = HashMultiset.create();
                            Chunk chunk = world.func_175726_f(new BlockPos(k2, 0, l2));

                            if (!chunk.func_76621_g()) {
                                int i3 = k2 & 15;
                                int j3 = l2 & 15;
                                int k3 = 0;
                                double d1 = 0.0D;

                                if (world.field_73011_w.func_177495_o()) {
                                    int l3 = k2 + l2 * 231871;

                                    l3 = l3 * l3 * 31287121 + l3 * 11;
                                    if ((l3 >> 20 & 1) == 0) {
                                        hashmultiset.add(Blocks.field_150346_d.func_176223_P().func_177226_a(BlockDirt.field_176386_a, BlockDirt.DirtType.DIRT).func_185909_g((IBlockAccess) world, BlockPos.field_177992_a), 10);
                                    } else {
                                        hashmultiset.add(Blocks.field_150348_b.func_176223_P().func_177226_a(BlockStone.field_176247_a, BlockStone.EnumType.STONE).func_185909_g((IBlockAccess) world, BlockPos.field_177992_a), 100);
                                    }

                                    d1 = 100.0D;
                                } else {
                                    BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos();

                                    for (int i4 = 0; i4 < i; ++i4) {
                                        for (int j4 = 0; j4 < i; ++j4) {
                                            int k4 = chunk.func_76611_b(i4 + i3, j4 + j3) + 1;
                                            IBlockState iblockdata = Blocks.field_150350_a.func_176223_P();

                                            if (k4 > 1) {
                                                do {
                                                    --k4;
                                                    iblockdata = chunk.func_186032_a(i4 + i3, k4, j4 + j3);
                                                    blockposition_mutableblockposition.func_181079_c((chunk.field_76635_g << 4) + i4 + i3, k4, (chunk.field_76647_h << 4) + j4 + j3);
                                                } while (iblockdata.func_185909_g((IBlockAccess) world, blockposition_mutableblockposition) == MapColor.field_151660_b && k4 > 0);

                                                if (k4 > 0 && iblockdata.func_185904_a().func_76224_d()) {
                                                    int l4 = k4 - 1;

                                                    IBlockState iblockdata1;

                                                    do {
                                                        iblockdata1 = chunk.func_186032_a(i4 + i3, l4--, j4 + j3);
                                                        ++k3;
                                                    } while (l4 > 0 && iblockdata1.func_185904_a().func_76224_d());
                                                }
                                            } else {
                                                iblockdata = Blocks.field_150357_h.func_176223_P();
                                            }

                                            d1 += (double) k4 / (double) (i * i);
                                            hashmultiset.add(iblockdata.func_185909_g((IBlockAccess) world, blockposition_mutableblockposition));
                                        }
                                    }
                                }

                                k3 /= i * i;
                                double d2 = (d1 - d0) * 4.0D / (double) (i + 4) + ((double) (k1 + l1 & 1) - 0.5D) * 0.4D;
                                byte b0 = 1;

                                if (d2 > 0.6D) {
                                    b0 = 2;
                                }

                                if (d2 < -0.6D) {
                                    b0 = 0;
                                }

                                MapColor materialmapcolor = (MapColor) Iterables.getFirst(Multisets.copyHighestCountFirst(hashmultiset), MapColor.field_151660_b);

                                if (materialmapcolor == MapColor.field_151662_n) {
                                    d2 = (double) k3 * 0.1D + (double) (k1 + l1 & 1) * 0.2D;
                                    b0 = 1;
                                    if (d2 < 0.5D) {
                                        b0 = 2;
                                    }

                                    if (d2 > 0.9D) {
                                        b0 = 0;
                                    }
                                }

                                d0 = d1;
                                if (l1 >= 0 && i2 * i2 + j2 * j2 < j1 * j1 && (!flag1 || (k1 + l1 & 1) != 0)) {
                                    byte b1 = worldmap.field_76198_e[k1 + l1 * 128];
                                    byte b2 = (byte) (materialmapcolor.field_76290_q * 4 + b0);

                                    if (b1 != b2) {
                                        worldmap.field_76198_e[k1 + l1 * 128] = b2;
                                        worldmap.func_176053_a(k1, l1);
                                        flag = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    public static void func_190905_a(World world, ItemStack itemstack) {
        if (itemstack.func_77973_b() == Items.field_151098_aY) {
            MapData worldmap = Items.field_151098_aY.func_77873_a(itemstack, world);

            if (worldmap != null) {
                if (world.field_73011_w.func_186058_p().func_186068_a() == worldmap.field_76200_c) {
                    int i = 1 << worldmap.field_76197_d;
                    int j = worldmap.field_76201_a;
                    int k = worldmap.field_76199_b;
                    Biome[] abiomebase = world.func_72959_q().func_76931_a((Biome[]) null, (j / i - 64) * i, (k / i - 64) * i, 128 * i, 128 * i, false);

                    for (int l = 0; l < 128; ++l) {
                        for (int i1 = 0; i1 < 128; ++i1) {
                            int j1 = l * i;
                            int k1 = i1 * i;
                            Biome biomebase = abiomebase[j1 + k1 * 128 * i];
                            MapColor materialmapcolor = MapColor.field_151660_b;
                            int l1 = 3;
                            int i2 = 8;

                            if (l > 0 && i1 > 0 && l < 127 && i1 < 127) {
                                if (abiomebase[(l - 1) * i + (i1 - 1) * i * 128 * i].func_185355_j() >= 0.0F) {
                                    --i2;
                                }

                                if (abiomebase[(l - 1) * i + (i1 + 1) * i * 128 * i].func_185355_j() >= 0.0F) {
                                    --i2;
                                }

                                if (abiomebase[(l - 1) * i + i1 * i * 128 * i].func_185355_j() >= 0.0F) {
                                    --i2;
                                }

                                if (abiomebase[(l + 1) * i + (i1 - 1) * i * 128 * i].func_185355_j() >= 0.0F) {
                                    --i2;
                                }

                                if (abiomebase[(l + 1) * i + (i1 + 1) * i * 128 * i].func_185355_j() >= 0.0F) {
                                    --i2;
                                }

                                if (abiomebase[(l + 1) * i + i1 * i * 128 * i].func_185355_j() >= 0.0F) {
                                    --i2;
                                }

                                if (abiomebase[l * i + (i1 - 1) * i * 128 * i].func_185355_j() >= 0.0F) {
                                    --i2;
                                }

                                if (abiomebase[l * i + (i1 + 1) * i * 128 * i].func_185355_j() >= 0.0F) {
                                    --i2;
                                }

                                if (biomebase.func_185355_j() < 0.0F) {
                                    materialmapcolor = MapColor.field_151676_q;
                                    if (i2 > 7 && i1 % 2 == 0) {
                                        l1 = (l + (int) (MathHelper.func_76126_a((float) i1 + 0.0F) * 7.0F)) / 8 % 5;
                                        if (l1 == 3) {
                                            l1 = 1;
                                        } else if (l1 == 4) {
                                            l1 = 0;
                                        }
                                    } else if (i2 > 7) {
                                        materialmapcolor = MapColor.field_151660_b;
                                    } else if (i2 > 5) {
                                        l1 = 1;
                                    } else if (i2 > 3) {
                                        l1 = 0;
                                    } else if (i2 > 1) {
                                        l1 = 0;
                                    }
                                } else if (i2 > 0) {
                                    materialmapcolor = MapColor.field_151650_B;
                                    if (i2 > 3) {
                                        l1 = 1;
                                    } else {
                                        l1 = 3;
                                    }
                                }
                            }

                            if (materialmapcolor != MapColor.field_151660_b) {
                                worldmap.field_76198_e[l + i1 * 128] = (byte) (materialmapcolor.field_76290_q * 4 + l1);
                                worldmap.func_176053_a(l, i1);
                            }
                        }
                    }

                }
            }
        }
    }

    public void func_77663_a(ItemStack itemstack, World world, Entity entity, int i, boolean flag) {
        if (!world.field_72995_K) {
            MapData worldmap = this.func_77873_a(itemstack, world);

            if (entity instanceof EntityPlayer) {
                EntityPlayer entityhuman = (EntityPlayer) entity;

                worldmap.func_76191_a(entityhuman, itemstack);
            }

            if (flag || entity instanceof EntityPlayer && ((EntityPlayer) entity).func_184592_cb() == itemstack) {
                this.func_77872_a(world, entity, worldmap);
            }

        }
    }

    @Nullable
    public Packet<?> func_150911_c(ItemStack itemstack, World world, EntityPlayer entityhuman) {
        return this.func_77873_a(itemstack, world).func_176052_a(itemstack, world, entityhuman);
    }

    public void func_77622_d(ItemStack itemstack, World world, EntityPlayer entityhuman) {
        NBTTagCompound nbttagcompound = itemstack.func_77978_p();

        if (nbttagcompound != null) {
            if (nbttagcompound.func_150297_b("map_scale_direction", 99)) {
                func_185063_a(itemstack, world, nbttagcompound.func_74762_e("map_scale_direction"));
                nbttagcompound.func_82580_o("map_scale_direction");
            } else if (nbttagcompound.func_74767_n("map_tracking_position")) {
                func_185064_b(itemstack, world);
                nbttagcompound.func_82580_o("map_tracking_position");
            }
        }

    }

    protected static void func_185063_a(ItemStack itemstack, World world, int i) {
        MapData worldmap = Items.field_151098_aY.func_77873_a(itemstack, world);

        world = world.getServer().getServer().worlds.get(0); // CraftBukkit - use primary world for maps
        itemstack.func_77964_b(world.func_72841_b("map"));
        MapData worldmap1 = new MapData("map_" + itemstack.func_77960_j());

        if (worldmap != null) {
            worldmap1.field_76197_d = (byte) MathHelper.func_76125_a(worldmap.field_76197_d + i, 0, 4);
            worldmap1.field_186210_e = worldmap.field_186210_e;
            worldmap1.func_176054_a((double) worldmap.field_76201_a, (double) worldmap.field_76199_b, worldmap1.field_76197_d);
            worldmap1.field_76200_c = worldmap.field_76200_c;
            worldmap1.func_76185_a();
            world.func_72823_a("map_" + itemstack.func_77960_j(), (WorldSavedData) worldmap1);
            // CraftBukkit start
            MapInitializeEvent event = new MapInitializeEvent(worldmap1.mapView);
            Bukkit.getServer().getPluginManager().callEvent(event);
            // CraftBukkit end
        }

    }

    protected static void func_185064_b(ItemStack itemstack, World world) {
        MapData worldmap = Items.field_151098_aY.func_77873_a(itemstack, world);

        world = world.getServer().getServer().worlds.get(0); // CraftBukkit - use primary world for maps
        itemstack.func_77964_b(world.func_72841_b("map"));
        MapData worldmap1 = new MapData("map_" + itemstack.func_77960_j());

        worldmap1.field_186210_e = true;
        if (worldmap != null) {
            worldmap1.field_76201_a = worldmap.field_76201_a;
            worldmap1.field_76199_b = worldmap.field_76199_b;
            worldmap1.field_76197_d = worldmap.field_76197_d;
            worldmap1.field_76200_c = worldmap.field_76200_c;
            worldmap1.func_76185_a();
            world.func_72823_a("map_" + itemstack.func_77960_j(), (WorldSavedData) worldmap1);
            // CraftBukkit start
            MapInitializeEvent event = new MapInitializeEvent(worldmap1.mapView);
            Bukkit.getServer().getPluginManager().callEvent(event);
            // CraftBukkit end
        }

    }
}
