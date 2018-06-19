package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class WorldGenEndIsland extends WorldGenerator {

    public WorldGenEndIsland() {}

    public boolean func_180709_b(World world, Random random, BlockPos blockposition) {
        float f = (float) (random.nextInt(3) + 4);

        for (int i = 0; f > 0.5F; --i) {
            for (int j = MathHelper.func_76141_d(-f); j <= MathHelper.func_76123_f(f); ++j) {
                for (int k = MathHelper.func_76141_d(-f); k <= MathHelper.func_76123_f(f); ++k) {
                    if ((float) (j * j + k * k) <= (f + 1.0F) * (f + 1.0F)) {
                        this.func_175903_a(world, blockposition.func_177982_a(j, i, k), Blocks.field_150377_bs.func_176223_P());
                    }
                }
            }

            f = (float) ((double) f - ((double) random.nextInt(2) + 0.5D));
        }

        return true;
    }
}
