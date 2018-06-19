package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenLiquids extends WorldGenerator {

    private final Block field_150521_a;

    public WorldGenLiquids(Block block) {
        this.field_150521_a = block;
    }

    public boolean func_180709_b(World world, Random random, BlockPos blockposition) {
        if (world.func_180495_p(blockposition.func_177984_a()).func_177230_c() != Blocks.field_150348_b) {
            return false;
        } else if (world.func_180495_p(blockposition.func_177977_b()).func_177230_c() != Blocks.field_150348_b) {
            return false;
        } else {
            IBlockState iblockdata = world.func_180495_p(blockposition);

            if (iblockdata.func_185904_a() != Material.field_151579_a && iblockdata.func_177230_c() != Blocks.field_150348_b) {
                return false;
            } else {
                int i = 0;

                if (world.func_180495_p(blockposition.func_177976_e()).func_177230_c() == Blocks.field_150348_b) {
                    ++i;
                }

                if (world.func_180495_p(blockposition.func_177974_f()).func_177230_c() == Blocks.field_150348_b) {
                    ++i;
                }

                if (world.func_180495_p(blockposition.func_177978_c()).func_177230_c() == Blocks.field_150348_b) {
                    ++i;
                }

                if (world.func_180495_p(blockposition.func_177968_d()).func_177230_c() == Blocks.field_150348_b) {
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

                if (i == 3 && j == 1) {
                    IBlockState iblockdata1 = this.field_150521_a.func_176223_P();

                    world.func_180501_a(blockposition, iblockdata1, 2);
                    world.func_189507_a(blockposition, iblockdata1, random);
                }

                return true;
            }
        }
    }
}
