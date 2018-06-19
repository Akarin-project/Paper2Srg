package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenIcePath extends WorldGenerator {

    private final Block field_150555_a;
    private final int field_150554_b;

    public WorldGenIcePath(int i) {
        this.field_150555_a = Blocks.field_150403_cj;
        this.field_150554_b = i;
    }

    public boolean func_180709_b(World world, Random random, BlockPos blockposition) {
        while (world.func_175623_d(blockposition) && blockposition.func_177956_o() > 2) {
            blockposition = blockposition.func_177977_b();
        }

        if (world.func_180495_p(blockposition).func_177230_c() != Blocks.field_150433_aE) {
            return false;
        } else {
            int i = random.nextInt(this.field_150554_b - 2) + 2;
            boolean flag = true;

            for (int j = blockposition.func_177958_n() - i; j <= blockposition.func_177958_n() + i; ++j) {
                for (int k = blockposition.func_177952_p() - i; k <= blockposition.func_177952_p() + i; ++k) {
                    int l = j - blockposition.func_177958_n();
                    int i1 = k - blockposition.func_177952_p();

                    if (l * l + i1 * i1 <= i * i) {
                        for (int j1 = blockposition.func_177956_o() - 1; j1 <= blockposition.func_177956_o() + 1; ++j1) {
                            BlockPos blockposition1 = new BlockPos(j, j1, k);
                            Block block = world.func_180495_p(blockposition1).func_177230_c();

                            if (block == Blocks.field_150346_d || block == Blocks.field_150433_aE || block == Blocks.field_150432_aD) {
                                world.func_180501_a(blockposition1, this.field_150555_a.func_176223_P(), 2);
                            }
                        }
                    }
                }
            }

            return true;
        }
    }
}
