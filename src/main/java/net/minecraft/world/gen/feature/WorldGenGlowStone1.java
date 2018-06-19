package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenGlowStone1 extends WorldGenerator {

    public WorldGenGlowStone1() {}

    public boolean func_180709_b(World world, Random random, BlockPos blockposition) {
        if (!world.func_175623_d(blockposition)) {
            return false;
        } else if (world.func_180495_p(blockposition.func_177984_a()).func_177230_c() != Blocks.field_150424_aL) {
            return false;
        } else {
            world.func_180501_a(blockposition, Blocks.field_150426_aN.func_176223_P(), 2);

            for (int i = 0; i < 1500; ++i) {
                BlockPos blockposition1 = blockposition.func_177982_a(random.nextInt(8) - random.nextInt(8), -random.nextInt(12), random.nextInt(8) - random.nextInt(8));

                if (world.func_180495_p(blockposition1).func_185904_a() == Material.field_151579_a) {
                    int j = 0;
                    EnumFacing[] aenumdirection = EnumFacing.values();
                    int k = aenumdirection.length;

                    for (int l = 0; l < k; ++l) {
                        EnumFacing enumdirection = aenumdirection[l];

                        if (world.func_180495_p(blockposition1.func_177972_a(enumdirection)).func_177230_c() == Blocks.field_150426_aN) {
                            ++j;
                        }

                        if (j > 1) {
                            break;
                        }
                    }

                    if (j == 1) {
                        world.func_180501_a(blockposition1, Blocks.field_150426_aN.func_176223_P(), 2);
                    }
                }
            }

            return true;
        }
    }
}
