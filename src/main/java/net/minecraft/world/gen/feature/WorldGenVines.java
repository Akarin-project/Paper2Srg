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

    public boolean func_180709_b(World world, Random random, BlockPos blockposition) {
        for (; blockposition.func_177956_o() < 128; blockposition = blockposition.func_177984_a()) {
            if (world.func_175623_d(blockposition)) {
                EnumFacing[] aenumdirection = EnumFacing.Plane.HORIZONTAL.func_179516_a();
                int i = aenumdirection.length;

                for (int j = 0; j < i; ++j) {
                    EnumFacing enumdirection = aenumdirection[j];

                    if (Blocks.field_150395_bd.func_176198_a(world, blockposition, enumdirection)) {
                        IBlockState iblockdata = Blocks.field_150395_bd.func_176223_P().func_177226_a(BlockVine.field_176273_b, Boolean.valueOf(enumdirection == EnumFacing.NORTH)).func_177226_a(BlockVine.field_176278_M, Boolean.valueOf(enumdirection == EnumFacing.EAST)).func_177226_a(BlockVine.field_176279_N, Boolean.valueOf(enumdirection == EnumFacing.SOUTH)).func_177226_a(BlockVine.field_176280_O, Boolean.valueOf(enumdirection == EnumFacing.WEST));

                        world.func_180501_a(blockposition, iblockdata, 2);
                        break;
                    }
                }
            } else {
                blockposition = blockposition.func_177982_a(random.nextInt(4) - random.nextInt(4), 0, random.nextInt(4) - random.nextInt(4));
            }
        }

        return true;
    }
}
