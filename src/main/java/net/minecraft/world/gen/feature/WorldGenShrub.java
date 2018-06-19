package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenShrub extends WorldGenTrees {

    private final IBlockState field_150528_a;
    private final IBlockState field_150527_b;

    public WorldGenShrub(IBlockState iblockdata, IBlockState iblockdata1) {
        super(false);
        this.field_150527_b = iblockdata;
        this.field_150528_a = iblockdata1;
    }

    public boolean func_180709_b(World world, Random random, BlockPos blockposition) {
        for (IBlockState iblockdata = world.func_180495_p(blockposition); (iblockdata.func_185904_a() == Material.field_151579_a || iblockdata.func_185904_a() == Material.field_151584_j) && blockposition.func_177956_o() > 0; iblockdata = world.func_180495_p(blockposition)) {
            blockposition = blockposition.func_177977_b();
        }

        Block block = world.func_180495_p(blockposition).func_177230_c();

        if (block == Blocks.field_150346_d || block == Blocks.field_150349_c) {
            blockposition = blockposition.func_177984_a();
            this.func_175903_a(world, blockposition, this.field_150527_b);

            for (int i = blockposition.func_177956_o(); i <= blockposition.func_177956_o() + 2; ++i) {
                int j = i - blockposition.func_177956_o();
                int k = 2 - j;

                for (int l = blockposition.func_177958_n() - k; l <= blockposition.func_177958_n() + k; ++l) {
                    int i1 = l - blockposition.func_177958_n();

                    for (int j1 = blockposition.func_177952_p() - k; j1 <= blockposition.func_177952_p() + k; ++j1) {
                        int k1 = j1 - blockposition.func_177952_p();

                        if (Math.abs(i1) != k || Math.abs(k1) != k || random.nextInt(2) != 0) {
                            BlockPos blockposition1 = new BlockPos(l, i, j1);
                            Material material = world.func_180495_p(blockposition1).func_185904_a();

                            if (material == Material.field_151579_a || material == Material.field_151584_j) {
                                this.func_175903_a(world, blockposition1, this.field_150528_a);
                            }
                        }
                    }
                }
            }
        // CraftBukkit start - Return false if gen was unsuccessful
        } else {
            return false;
        }
        // CraftBukkit end


        return true;
    }
}
