package net.minecraft.world.biome;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BiomeVoidDecorator extends BiomeDecorator {

    public BiomeVoidDecorator() {}

    public void func_180292_a(World world, Random random, Biome biomebase, BlockPos blockposition) {
        BlockPos blockposition1 = world.func_175694_M();
        boolean flag = true;
        double d0 = blockposition1.func_177951_i(blockposition.func_177982_a(8, blockposition1.func_177956_o(), 8));

        if (d0 <= 1024.0D) {
            BlockPos blockposition2 = new BlockPos(blockposition1.func_177958_n() - 16, blockposition1.func_177956_o() - 1, blockposition1.func_177952_p() - 16);
            BlockPos blockposition3 = new BlockPos(blockposition1.func_177958_n() + 16, blockposition1.func_177956_o() - 1, blockposition1.func_177952_p() + 16);
            BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos(blockposition2);

            for (int i = blockposition.func_177952_p(); i < blockposition.func_177952_p() + 16; ++i) {
                for (int j = blockposition.func_177958_n(); j < blockposition.func_177958_n() + 16; ++j) {
                    if (i >= blockposition2.func_177952_p() && i <= blockposition3.func_177952_p() && j >= blockposition2.func_177958_n() && j <= blockposition3.func_177958_n()) {
                        blockposition_mutableblockposition.func_181079_c(j, blockposition_mutableblockposition.func_177956_o(), i);
                        if (blockposition1.func_177958_n() == j && blockposition1.func_177952_p() == i) {
                            world.func_180501_a(blockposition_mutableblockposition, Blocks.field_150347_e.func_176223_P(), 2);
                        } else {
                            world.func_180501_a(blockposition_mutableblockposition, Blocks.field_150348_b.func_176223_P(), 2);
                        }
                    }
                }
            }

        }
    }
}
