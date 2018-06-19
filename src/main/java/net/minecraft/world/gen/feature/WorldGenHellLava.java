package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenHellLava extends WorldGenerator {

    private final Block field_150553_a;
    private final boolean field_94524_b;

    public WorldGenHellLava(Block block, boolean flag) {
        this.field_150553_a = block;
        this.field_94524_b = flag;
    }

    public boolean func_180709_b(World world, Random random, BlockPos blockposition) {
        if (world.func_180495_p(blockposition.func_177984_a()).func_177230_c() != Blocks.field_150424_aL) {
            return false;
        } else if (world.func_180495_p(blockposition).func_185904_a() != Material.field_151579_a && world.func_180495_p(blockposition).func_177230_c() != Blocks.field_150424_aL) {
            return false;
        } else {
            int i = 0;

            if (world.func_180495_p(blockposition.func_177976_e()).func_177230_c() == Blocks.field_150424_aL) {
                ++i;
            }

            if (world.func_180495_p(blockposition.func_177974_f()).func_177230_c() == Blocks.field_150424_aL) {
                ++i;
            }

            if (world.func_180495_p(blockposition.func_177978_c()).func_177230_c() == Blocks.field_150424_aL) {
                ++i;
            }

            if (world.func_180495_p(blockposition.func_177968_d()).func_177230_c() == Blocks.field_150424_aL) {
                ++i;
            }

            if (world.func_180495_p(blockposition.func_177977_b()).func_177230_c() == Blocks.field_150424_aL) {
                ++i;
            }

            int j = 0;

            if (world.func_175623_d(blockposition.func_177976_e())) {
                ++j;
            }

            if (world.func_175623_d(blockposition.func_177974_f())) {
                ++j;
            }

            if (world.func_175623_d(blockposition.func_177978_c())) {
                ++j;
            }

            if (world.func_175623_d(blockposition.func_177968_d())) {
                ++j;
            }

            if (world.func_175623_d(blockposition.func_177977_b())) {
                ++j;
            }

            if (!this.field_94524_b && i == 4 && j == 1 || i == 5) {
                IBlockState iblockdata = this.field_150553_a.func_176223_P();

                world.func_180501_a(blockposition, iblockdata, 2);
                world.func_189507_a(blockposition, iblockdata, random);
            }

            return true;
        }
    }
}
