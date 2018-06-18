package net.minecraft.world.gen.feature;

import com.google.common.base.Predicates;
import java.util.Iterator;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenDesertWells extends WorldGenerator {

    private static final BlockStateMatcher IS_SAND = BlockStateMatcher.forBlock((Block) Blocks.SAND).where(BlockSand.VARIANT, Predicates.equalTo(BlockSand.EnumType.SAND));
    private final IBlockState sandSlab;
    private final IBlockState sandstone;
    private final IBlockState water;

    public WorldGenDesertWells() {
        this.sandSlab = Blocks.STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.SAND).withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM);
        this.sandstone = Blocks.SANDSTONE.getDefaultState();
        this.water = Blocks.FLOWING_WATER.getDefaultState();
    }

    public boolean generate(World world, Random random, BlockPos blockposition) {
        while (world.isAirBlock(blockposition) && blockposition.getY() > 2) {
            blockposition = blockposition.down();
        }

        if (!WorldGenDesertWells.IS_SAND.apply(world.getBlockState(blockposition))) {
            return false;
        } else {
            int i;
            int j;

            for (i = -2; i <= 2; ++i) {
                for (j = -2; j <= 2; ++j) {
                    if (world.isAirBlock(blockposition.add(i, -1, j)) && world.isAirBlock(blockposition.add(i, -2, j))) {
                        return false;
                    }
                }
            }

            for (i = -1; i <= 0; ++i) {
                for (j = -2; j <= 2; ++j) {
                    for (int k = -2; k <= 2; ++k) {
                        world.setBlockState(blockposition.add(j, i, k), this.sandstone, 2);
                    }
                }
            }

            world.setBlockState(blockposition, this.water, 2);
            Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

            while (iterator.hasNext()) {
                EnumFacing enumdirection = (EnumFacing) iterator.next();

                world.setBlockState(blockposition.offset(enumdirection), this.water, 2);
            }

            for (i = -2; i <= 2; ++i) {
                for (j = -2; j <= 2; ++j) {
                    if (i == -2 || i == 2 || j == -2 || j == 2) {
                        world.setBlockState(blockposition.add(i, 1, j), this.sandstone, 2);
                    }
                }
            }

            world.setBlockState(blockposition.add(2, 1, 0), this.sandSlab, 2);
            world.setBlockState(blockposition.add(-2, 1, 0), this.sandSlab, 2);
            world.setBlockState(blockposition.add(0, 1, 2), this.sandSlab, 2);
            world.setBlockState(blockposition.add(0, 1, -2), this.sandSlab, 2);

            for (i = -1; i <= 1; ++i) {
                for (j = -1; j <= 1; ++j) {
                    if (i == 0 && j == 0) {
                        world.setBlockState(blockposition.add(i, 4, j), this.sandstone, 2);
                    } else {
                        world.setBlockState(blockposition.add(i, 4, j), this.sandSlab, 2);
                    }
                }
            }

            for (i = 1; i <= 3; ++i) {
                world.setBlockState(blockposition.add(-1, i, -1), this.sandstone, 2);
                world.setBlockState(blockposition.add(-1, i, 1), this.sandstone, 2);
                world.setBlockState(blockposition.add(1, i, -1), this.sandstone, 2);
                world.setBlockState(blockposition.add(1, i, 1), this.sandstone, 2);
            }

            return true;
        }
    }
}
