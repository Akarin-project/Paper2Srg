package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class WorldGenMegaPineTree extends WorldGenHugeTrees {

    private static final IBlockState TRUNK = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE);
    private static final IBlockState LEAF = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE).withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
    private static final IBlockState PODZOL = Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.PODZOL);
    private final boolean useBaseHeight;

    public WorldGenMegaPineTree(boolean flag, boolean flag1) {
        super(flag, 13, 15, WorldGenMegaPineTree.TRUNK, WorldGenMegaPineTree.LEAF);
        this.useBaseHeight = flag1;
    }

    public boolean generate(World world, Random random, BlockPos blockposition) {
        int i = this.getHeight(random);

        if (!this.ensureGrowable(world, random, blockposition, i)) {
            return false;
        } else {
            this.createCrown(world, blockposition.getX(), blockposition.getZ(), blockposition.getY() + i, 0, random);

            for (int j = 0; j < i; ++j) {
                IBlockState iblockdata = world.getBlockState(blockposition.up(j));

                if (iblockdata.getMaterial() == Material.AIR || iblockdata.getMaterial() == Material.LEAVES) {
                    this.setBlockAndNotifyAdequately(world, blockposition.up(j), this.woodMetadata);
                }

                if (j < i - 1) {
                    iblockdata = world.getBlockState(blockposition.add(1, j, 0));
                    if (iblockdata.getMaterial() == Material.AIR || iblockdata.getMaterial() == Material.LEAVES) {
                        this.setBlockAndNotifyAdequately(world, blockposition.add(1, j, 0), this.woodMetadata);
                    }

                    iblockdata = world.getBlockState(blockposition.add(1, j, 1));
                    if (iblockdata.getMaterial() == Material.AIR || iblockdata.getMaterial() == Material.LEAVES) {
                        this.setBlockAndNotifyAdequately(world, blockposition.add(1, j, 1), this.woodMetadata);
                    }

                    iblockdata = world.getBlockState(blockposition.add(0, j, 1));
                    if (iblockdata.getMaterial() == Material.AIR || iblockdata.getMaterial() == Material.LEAVES) {
                        this.setBlockAndNotifyAdequately(world, blockposition.add(0, j, 1), this.woodMetadata);
                    }
                }
            }

            return true;
        }
    }

    private void createCrown(World world, int i, int j, int k, int l, Random random) {
        int i1 = random.nextInt(5) + (this.useBaseHeight ? this.baseHeight : 3);
        int j1 = 0;

        for (int k1 = k - i1; k1 <= k; ++k1) {
            int l1 = k - k1;
            int i2 = l + MathHelper.floor((float) l1 / (float) i1 * 3.5F);

            this.growLeavesLayerStrict(world, new BlockPos(i, k1, j), i2 + (l1 > 0 && i2 == j1 && (k1 & 1) == 0 ? 1 : 0));
            j1 = i2;
        }

    }

    public void generateSaplings(World world, Random random, BlockPos blockposition) {
        this.placePodzolCircle(world, blockposition.west().north());
        this.placePodzolCircle(world, blockposition.east(2).north());
        this.placePodzolCircle(world, blockposition.west().south(2));
        this.placePodzolCircle(world, blockposition.east(2).south(2));

        for (int i = 0; i < 5; ++i) {
            int j = random.nextInt(64);
            int k = j % 8;
            int l = j / 8;

            if (k == 0 || k == 7 || l == 0 || l == 7) {
                this.placePodzolCircle(world, blockposition.add(-3 + k, 0, -3 + l));
            }
        }

    }

    private void placePodzolCircle(World world, BlockPos blockposition) {
        for (int i = -2; i <= 2; ++i) {
            for (int j = -2; j <= 2; ++j) {
                if (Math.abs(i) != 2 || Math.abs(j) != 2) {
                    this.placePodzolAt(world, blockposition.add(i, 0, j));
                }
            }
        }

    }

    private void placePodzolAt(World world, BlockPos blockposition) {
        for (int i = 2; i >= -3; --i) {
            BlockPos blockposition1 = blockposition.up(i);
            IBlockState iblockdata = world.getBlockState(blockposition1);
            Block block = iblockdata.getBlock();

            if (block == Blocks.GRASS || block == Blocks.DIRT) {
                this.setBlockAndNotifyAdequately(world, blockposition1, WorldGenMegaPineTree.PODZOL);
                break;
            }

            if (iblockdata.getMaterial() != Material.AIR && i < 0) {
                break;
            }
        }

    }
}
