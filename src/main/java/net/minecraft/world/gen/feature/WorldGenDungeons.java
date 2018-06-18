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

    private static final Logger LOGGER = LogManager.getLogger();
    private static final ResourceLocation[] SPAWNERTYPES = new ResourceLocation[] { EntityList.getKey(EntitySkeleton.class), EntityList.getKey(EntityZombie.class), EntityList.getKey(EntityZombie.class), EntityList.getKey(EntitySpider.class)};

    public WorldGenDungeons() {}

    public boolean generate(World world, Random random, BlockPos blockposition) {
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
                    blockposition1 = blockposition.add(l1, i2, j2);
                    Material material = world.getBlockState(blockposition1).getMaterial();
                    boolean flag3 = material.isSolid();

                    if (i2 == -1 && !flag3) {
                        return false;
                    }

                    if (i2 == 4 && !flag3) {
                        return false;
                    }

                    if ((l1 == j || l1 == k || j2 == i1 || j2 == j1) && i2 == 0 && world.isAirBlock(blockposition1) && world.isAirBlock(blockposition1.up())) {
                        ++k1;
                    }
                }
            }
        }

        if (k1 >= 1 && k1 <= 5) {
            for (l1 = j; l1 <= k; ++l1) {
                for (i2 = 3; i2 >= -1; --i2) {
                    for (j2 = i1; j2 <= j1; ++j2) {
                        blockposition1 = blockposition.add(l1, i2, j2);
                        if (l1 != j && i2 != -1 && j2 != i1 && l1 != k && i2 != 4 && j2 != j1) {
                            if (world.getBlockState(blockposition1).getBlock() != Blocks.CHEST) {
                                world.setBlockToAir(blockposition1);
                            }
                        } else if (blockposition1.getY() >= 0 && !world.getBlockState(blockposition1.down()).getMaterial().isSolid()) {
                            world.setBlockToAir(blockposition1);
                        } else if (world.getBlockState(blockposition1).getMaterial().isSolid() && world.getBlockState(blockposition1).getBlock() != Blocks.CHEST) {
                            if (i2 == -1 && random.nextInt(4) != 0) {
                                world.setBlockState(blockposition1, Blocks.MOSSY_COBBLESTONE.getDefaultState(), 2);
                            } else {
                                world.setBlockState(blockposition1, Blocks.COBBLESTONE.getDefaultState(), 2);
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
                            j2 = blockposition.getX() + random.nextInt(i * 2 + 1) - i;
                            int k2 = blockposition.getY();
                            int l2 = blockposition.getZ() + random.nextInt(l * 2 + 1) - l;
                            BlockPos blockposition2 = new BlockPos(j2, k2, l2);

                            if (world.isAirBlock(blockposition2)) {
                                int i3 = 0;
                                Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

                                while (iterator.hasNext()) {
                                    EnumFacing enumdirection = (EnumFacing) iterator.next();

                                    if (world.getBlockState(blockposition2.offset(enumdirection)).getMaterial().isSolid()) {
                                        ++i3;
                                    }
                                }

                                if (i3 == 1) {
                                    world.setBlockState(blockposition2, Blocks.CHEST.correctFacing(world, blockposition2, Blocks.CHEST.getDefaultState()), 2);
                                    TileEntity tileentity = world.getTileEntity(blockposition2);

                                    if (tileentity instanceof TileEntityChest) {
                                        ((TileEntityChest) tileentity).setLootTable(LootTableList.CHESTS_SIMPLE_DUNGEON, random.nextLong());
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

            world.setBlockState(blockposition, Blocks.MOB_SPAWNER.getDefaultState(), 2);
            TileEntity tileentity1 = world.getTileEntity(blockposition);

            if (tileentity1 instanceof TileEntityMobSpawner) {
                ((TileEntityMobSpawner) tileentity1).getSpawnerBaseLogic().setEntityId(this.pickMobSpawner(random));
            } else {
                WorldGenDungeons.LOGGER.error("Failed to fetch mob spawner entity at ({}, {}, {})", Integer.valueOf(blockposition.getX()), Integer.valueOf(blockposition.getY()), Integer.valueOf(blockposition.getZ()));
            }

            return true;
        } else {
            return false;
        }
    }

    private ResourceLocation pickMobSpawner(Random random) {
        return WorldGenDungeons.SPAWNERTYPES[random.nextInt(WorldGenDungeons.SPAWNERTYPES.length)];
    }
}
