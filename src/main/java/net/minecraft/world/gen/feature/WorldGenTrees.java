package net.minecraft.world.gen.feature;

import java.util.Iterator;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCocoa;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockVine;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenTrees extends WorldGenAbstractTree {

    private static final IBlockState DEFAULT_TRUNK = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.OAK);
    private static final IBlockState DEFAULT_LEAF = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK).withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
    private final int minTreeHeight;
    private final boolean vinesGrow;
    private final IBlockState metaWood;
    private final IBlockState metaLeaves;

    public WorldGenTrees(boolean flag) {
        this(flag, 4, WorldGenTrees.DEFAULT_TRUNK, WorldGenTrees.DEFAULT_LEAF, false);
    }

    public WorldGenTrees(boolean flag, int i, IBlockState iblockdata, IBlockState iblockdata1, boolean flag1) {
        super(flag);
        this.minTreeHeight = i;
        this.metaWood = iblockdata;
        this.metaLeaves = iblockdata1;
        this.vinesGrow = flag1;
    }

    public boolean generate(World world, Random random, BlockPos blockposition) {
        int i = random.nextInt(3) + this.minTreeHeight;
        boolean flag = true;

        if (blockposition.getY() >= 1 && blockposition.getY() + i + 1 <= 256) {
            int j;
            int k;

            for (int l = blockposition.getY(); l <= blockposition.getY() + 1 + i; ++l) {
                byte b0 = 1;

                if (l == blockposition.getY()) {
                    b0 = 0;
                }

                if (l >= blockposition.getY() + 1 + i - 2) {
                    b0 = 2;
                }

                BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos();

                for (j = blockposition.getX() - b0; j <= blockposition.getX() + b0 && flag; ++j) {
                    for (k = blockposition.getZ() - b0; k <= blockposition.getZ() + b0 && flag; ++k) {
                        if (l >= 0 && l < 256) {
                            if (!this.canGrowInto(world.getBlockState(blockposition_mutableblockposition.setPos(j, l, k)).getBlock())) {
                                flag = false;
                            }
                        } else {
                            flag = false;
                        }
                    }
                }
            }

            if (!flag) {
                return false;
            } else {
                Block block = world.getBlockState(blockposition.down()).getBlock();

                if ((block == Blocks.GRASS || block == Blocks.DIRT || block == Blocks.FARMLAND) && blockposition.getY() < 256 - i - 1) {
                    this.setDirtAt(world, blockposition.down());
                    boolean flag1 = true;
                    boolean flag2 = false;

                    int i1;
                    int j1;
                    int k1;
                    BlockPos blockposition1;

                    for (j = blockposition.getY() - 3 + i; j <= blockposition.getY() + i; ++j) {
                        k = j - (blockposition.getY() + i);
                        i1 = 1 - k / 2;

                        for (int l1 = blockposition.getX() - i1; l1 <= blockposition.getX() + i1; ++l1) {
                            j1 = l1 - blockposition.getX();

                            for (k1 = blockposition.getZ() - i1; k1 <= blockposition.getZ() + i1; ++k1) {
                                int i2 = k1 - blockposition.getZ();

                                if (Math.abs(j1) != i1 || Math.abs(i2) != i1 || random.nextInt(2) != 0 && k != 0) {
                                    blockposition1 = new BlockPos(l1, j, k1);
                                    Material material = world.getBlockState(blockposition1).getMaterial();

                                    if (material == Material.AIR || material == Material.LEAVES || material == Material.VINE) {
                                        this.setBlockAndNotifyAdequately(world, blockposition1, this.metaLeaves);
                                    }
                                }
                            }
                        }
                    }

                    for (j = 0; j < i; ++j) {
                        Material material1 = world.getBlockState(blockposition.up(j)).getMaterial();

                        if (material1 == Material.AIR || material1 == Material.LEAVES || material1 == Material.VINE) {
                            this.setBlockAndNotifyAdequately(world, blockposition.up(j), this.metaWood);
                            if (this.vinesGrow && j > 0) {
                                if (random.nextInt(3) > 0 && world.isAirBlock(blockposition.add(-1, j, 0))) {
                                    this.addVine(world, blockposition.add(-1, j, 0), BlockVine.EAST);
                                }

                                if (random.nextInt(3) > 0 && world.isAirBlock(blockposition.add(1, j, 0))) {
                                    this.addVine(world, blockposition.add(1, j, 0), BlockVine.WEST);
                                }

                                if (random.nextInt(3) > 0 && world.isAirBlock(blockposition.add(0, j, -1))) {
                                    this.addVine(world, blockposition.add(0, j, -1), BlockVine.SOUTH);
                                }

                                if (random.nextInt(3) > 0 && world.isAirBlock(blockposition.add(0, j, 1))) {
                                    this.addVine(world, blockposition.add(0, j, 1), BlockVine.NORTH);
                                }
                            }
                        }
                    }

                    if (this.vinesGrow) {
                        for (j = blockposition.getY() - 3 + i; j <= blockposition.getY() + i; ++j) {
                            k = j - (blockposition.getY() + i);
                            i1 = 2 - k / 2;
                            BlockPos.MutableBlockPos blockposition_mutableblockposition1 = new BlockPos.MutableBlockPos();

                            for (j1 = blockposition.getX() - i1; j1 <= blockposition.getX() + i1; ++j1) {
                                for (k1 = blockposition.getZ() - i1; k1 <= blockposition.getZ() + i1; ++k1) {
                                    blockposition_mutableblockposition1.setPos(j1, j, k1);
                                    if (world.getBlockState(blockposition_mutableblockposition1).getMaterial() == Material.LEAVES) {
                                        BlockPos blockposition2 = blockposition_mutableblockposition1.west();

                                        blockposition1 = blockposition_mutableblockposition1.east();
                                        BlockPos blockposition3 = blockposition_mutableblockposition1.north();
                                        BlockPos blockposition4 = blockposition_mutableblockposition1.south();

                                        if (random.nextInt(4) == 0 && world.getBlockState(blockposition2).getMaterial() == Material.AIR) {
                                            this.addHangingVine(world, blockposition2, BlockVine.EAST);
                                        }

                                        if (random.nextInt(4) == 0 && world.getBlockState(blockposition1).getMaterial() == Material.AIR) {
                                            this.addHangingVine(world, blockposition1, BlockVine.WEST);
                                        }

                                        if (random.nextInt(4) == 0 && world.getBlockState(blockposition3).getMaterial() == Material.AIR) {
                                            this.addHangingVine(world, blockposition3, BlockVine.SOUTH);
                                        }

                                        if (random.nextInt(4) == 0 && world.getBlockState(blockposition4).getMaterial() == Material.AIR) {
                                            this.addHangingVine(world, blockposition4, BlockVine.NORTH);
                                        }
                                    }
                                }
                            }
                        }

                        if (random.nextInt(5) == 0 && i > 5) {
                            for (j = 0; j < 2; ++j) {
                                Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

                                while (iterator.hasNext()) {
                                    EnumFacing enumdirection = (EnumFacing) iterator.next();

                                    if (random.nextInt(4 - j) == 0) {
                                        EnumFacing enumdirection1 = enumdirection.getOpposite();

                                        this.placeCocoa(world, random.nextInt(3), blockposition.add(enumdirection1.getFrontOffsetX(), i - 5 + j, enumdirection1.getFrontOffsetZ()), enumdirection);
                                    }
                                }
                            }
                        }
                    }

                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    private void placeCocoa(World world, int i, BlockPos blockposition, EnumFacing enumdirection) {
        this.setBlockAndNotifyAdequately(world, blockposition, Blocks.COCOA.getDefaultState().withProperty(BlockCocoa.AGE, Integer.valueOf(i)).withProperty(BlockCocoa.FACING, enumdirection));
    }

    private void addVine(World world, BlockPos blockposition, PropertyBool blockstateboolean) {
        this.setBlockAndNotifyAdequately(world, blockposition, Blocks.VINE.getDefaultState().withProperty(blockstateboolean, Boolean.valueOf(true)));
    }

    private void addHangingVine(World world, BlockPos blockposition, PropertyBool blockstateboolean) {
        this.addVine(world, blockposition, blockstateboolean);
        int i = 4;

        for (blockposition = blockposition.down(); world.getBlockState(blockposition).getMaterial() == Material.AIR && i > 0; --i) {
            this.addVine(world, blockposition, blockstateboolean);
            blockposition = blockposition.down();
        }

    }
}
