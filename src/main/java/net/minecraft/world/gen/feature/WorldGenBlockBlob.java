package net.minecraft.world.gen.feature;

import java.util.Iterator;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenBlockBlob extends WorldGenerator {

    private final Block field_150545_a;
    private final int field_150544_b;

    public WorldGenBlockBlob(Block block, int i) {
        super(false);
        this.field_150545_a = block;
        this.field_150544_b = i;
    }

    public boolean func_180709_b(World world, Random random, BlockPos blockposition) {
        while (true) {
            if (blockposition.func_177956_o() > 3) {
                label47: {
                    if (!world.func_175623_d(blockposition.func_177977_b())) {
                        Block block = world.func_180495_p(blockposition.func_177977_b()).func_177230_c();

                        if (block == Blocks.field_150349_c || block == Blocks.field_150346_d || block == Blocks.field_150348_b) {
                            break label47;
                        }
                    }

                    blockposition = blockposition.func_177977_b();
                    continue;
                }
            }

            if (blockposition.func_177956_o() <= 3) {
                return false;
            }

            int i = this.field_150544_b;

            for (int j = 0; i >= 0 && j < 3; ++j) {
                int k = i + random.nextInt(2);
                int l = i + random.nextInt(2);
                int i1 = i + random.nextInt(2);
                float f = (float) (k + l + i1) * 0.333F + 0.5F;
                Iterator iterator = BlockPos.func_177980_a(blockposition.func_177982_a(-k, -l, -i1), blockposition.func_177982_a(k, l, i1)).iterator();

                while (iterator.hasNext()) {
                    BlockPos blockposition1 = (BlockPos) iterator.next();

                    if (blockposition1.func_177951_i(blockposition) <= (double) (f * f)) {
                        world.func_180501_a(blockposition1, this.field_150545_a.func_176223_P(), 4);
                    }
                }

                blockposition = blockposition.func_177982_a(-(i + 1) + random.nextInt(2 + i * 2), 0 - random.nextInt(2), -(i + 1) + random.nextInt(2 + i * 2));
            }

            return true;
        }
    }
}
