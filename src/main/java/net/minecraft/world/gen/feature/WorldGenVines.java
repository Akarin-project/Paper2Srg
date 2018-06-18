package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.BlockVine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenVines extends WorldGenerator {

    public WorldGenVines() {}

    public boolean generate(World world, Random random, BlockPos blockposition) {
        for (; blockposition.getY() < 128; blockposition = blockposition.up()) {
            if (world.isAirBlock(blockposition)) {
                EnumFacing[] aenumdirection = EnumFacing.Plane.HORIZONTAL.facings();
                int i = aenumdirection.length;

                for (int j = 0; j < i; ++j) {
                    EnumFacing enumdirection = aenumdirection[j];

                    if (Blocks.VINE.canPlaceBlockOnSide(world, blockposition, enumdirection)) {
                        IBlockState iblockdata = Blocks.VINE.getDefaultState().withProperty(BlockVine.NORTH, Boolean.valueOf(enumdirection == EnumFacing.NORTH)).withProperty(BlockVine.EAST, Boolean.valueOf(enumdirection == EnumFacing.EAST)).withProperty(BlockVine.SOUTH, Boolean.valueOf(enumdirection == EnumFacing.SOUTH)).withProperty(BlockVine.WEST, Boolean.valueOf(enumdirection == EnumFacing.WEST));

                        world.setBlockState(blockposition, iblockdata, 2);
                        break;
                    }
                }
            } else {
                blockposition = blockposition.add(random.nextInt(4) - random.nextInt(4), 0, random.nextInt(4) - random.nextInt(4));
            }
        }

        return true;
    }
}
