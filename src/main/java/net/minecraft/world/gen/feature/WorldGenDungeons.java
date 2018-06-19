package net.minecraft.world.gen.feature;

import java.util.Iterator;
import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class WorldGenDungeons extends WorldGenerator {

    private static final Logger field_175918_a = LogManager.getLogger();
    private static final ResourceLocation[] field_175916_b = new ResourceLocation[] { EntityList.func_191306_a(EntitySkeleton.class), EntityList.func_191306_a(EntityZombie.class), EntityList.func_191306_a(EntityZombie.class), EntityList.func_191306_a(EntitySpider.class)};

    public WorldGenDungeons() {}

    public boolean func_180709_b(World world, Random random, BlockPos blockposition) {
        boolean flag = true;
        int i = random.nextInt(2) + 2;
        int j = -i - 1;
        int k = i + 1;
        boolean flag1 = true;
        boolean flag2 = true;
        int l = random.nextInt(2) + 2;
        int i1 = -l - 1;
        int j1 = l + 1;
        int k1 = 0;

        int l1;
        int i2;
        int j2;
        BlockPos blockposition1;

        for (l1 = j; l1 <= k; ++l1) {
            for (i2 = -1; i2 <= 4; ++i2) {
                for (j2 = i1; j2 <= j1; ++j2) {
                    blockposition1 = blockposition.func_177982_a(l1, i2, j2);
                    Material material = world.func_180495_p(blockposition1).func_185904_a();
                    boolean flag3 = material.func_76220_a();

                    if (i2 == -1 && !flag3) {
                        return false;
                    }

                    if (i2 == 4 && !flag3) {
                        return false;
                    }

                    if ((l1 == j || l1 == k || j2 == i1 || j2 == j1) && i2 == 0 && world.func_175623_d(blockposition1) && world.func_175623_d(blockposition1.func_177984_a())) {
                        ++k1;
                    }
                }
            }
        }

        if (k1 >= 1 && k1 <= 5) {
            for (l1 = j; l1 <= k; ++l1) {
                for (i2 = 3; i2 >= -1; --i2) {
                    for (j2 = i1; j2 <= j1; ++j2) {
                        blockposition1 = blockposition.func_177982_a(l1, i2, j2);
                        if (l1 != j && i2 != -1 && j2 != i1 && l1 != k && i2 != 4 && j2 != j1) {
                            if (world.func_180495_p(blockposition1).func_177230_c() != Blocks.field_150486_ae) {
                                world.func_175698_g(blockposition1);
                            }
                        } else if (blockposition1.func_177956_o() >= 0 && !world.func_180495_p(blockposition1.func_177977_b()).func_185904_a().func_76220_a()) {
                            world.func_175698_g(blockposition1);
                        } else if (world.func_180495_p(blockposition1).func_185904_a().func_76220_a() && world.func_180495_p(blockposition1).func_177230_c() != Blocks.field_150486_ae) {
                            if (i2 == -1 && random.nextInt(4) != 0) {
                                world.func_180501_a(blockposition1, Blocks.field_150341_Y.func_176223_P(), 2);
                            } else {
                                world.func_180501_a(blockposition1, Blocks.field_150347_e.func_176223_P(), 2);
                            }
                        }
                    }
                }
            }

            l1 = 0;

            while (l1 < 2) {
                i2 = 0;

                while (true) {
                    if (i2 < 3) {
                        label197: {
                            j2 = blockposition.func_177958_n() + random.nextInt(i * 2 + 1) - i;
                            int k2 = blockposition.func_177956_o();
                            int l2 = blockposition.func_177952_p() + random.nextInt(l * 2 + 1) - l;
                            BlockPos blockposition2 = new BlockPos(j2, k2, l2);

                            if (world.func_175623_d(blockposition2)) {
                                int i3 = 0;
                                Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

                                while (iterator.hasNext()) {
                                    EnumFacing enumdirection = (EnumFacing) iterator.next();

                                    if (world.func_180495_p(blockposition2.func_177972_a(enumdirection)).func_185904_a().func_76220_a()) {
                                        ++i3;
                                    }
                                }

                                if (i3 == 1) {
                                    world.func_180501_a(blockposition2, Blocks.field_150486_ae.func_176458_f(world, blockposition2, Blocks.field_150486_ae.func_176223_P()), 2);
                                    TileEntity tileentity = world.func_175625_s(blockposition2);

                                    if (tileentity instanceof TileEntityChest) {
                                        ((TileEntityChest) tileentity).func_189404_a(LootTableList.field_186422_d, random.nextLong());
                                    }
                                    break label197;
                                }
                            }

                            ++i2;
                            continue;
                        }
                    }

                    ++l1;
                    break;
                }
            }

            world.func_180501_a(blockposition, Blocks.field_150474_ac.func_176223_P(), 2);
            TileEntity tileentity1 = world.func_175625_s(blockposition);

            if (tileentity1 instanceof TileEntityMobSpawner) {
                ((TileEntityMobSpawner) tileentity1).func_145881_a().func_190894_a(this.func_76543_b(random));
            } else {
                WorldGenDungeons.field_175918_a.error("Failed to fetch mob spawner entity at ({}, {}, {})", Integer.valueOf(blockposition.func_177958_n()), Integer.valueOf(blockposition.func_177956_o()), Integer.valueOf(blockposition.func_177952_p()));
            }

            return true;
        } else {
            return false;
        }
    }

    private ResourceLocation func_76543_b(Random random) {
        return WorldGenDungeons.field_175916_b[random.nextInt(WorldGenDungeons.field_175916_b.length)];
    }
}
