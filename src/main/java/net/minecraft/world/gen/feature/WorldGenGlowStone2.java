package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenGlowStone2 extends WorldGenerator {

    public WorldGenGlowStone2() {}

    public boolean generate(World world, Random random, BlockPos blockposition) {
        if (!world.isAirBlock(blockposition)) {
            return false;
        } else if (world.getBlockState(blockposition.up()).getBlock() != Blocks.NETHERRACK) {
            return false;
        } else {
            world.setBlockState(blockposition, Blocks.GLOWSTONE.getDefaultState(), 2);

            for (int i = 0; i < 1500; ++i) {
                BlockPos blockposition1 = blockposition.add(random.nextInt(8) - random.nextInt(8), -random.nextInt(12), random.nextInt(8) - random.nextInt(8));

                if (world.getBlockState(blockposition1).getMaterial() == Material.AIR) {
                    int j = 0;
                    EnumFacing[] aenumdirection = EnumFacing.values();
                    int k = aenumdirection.length;

                    for (int l = 0; l < k; ++l) {
                        EnumFacing enumdirection = aenumdirection[l];

                        if (world.getBlockState(blockposition1.offset(enumdirection)).getBlock() == Blocks.GLOWSTONE) {
                            ++j;
                        }

                        if (j > 1) {
                            break;
                        }
                    }

                    if (j == 1) {
                        world.setBlockState(blockposition1, Blocks.GLOWSTONE.getDefaultState(), 2);
                    }
                }
            }

            return true;
        }
    }
}
