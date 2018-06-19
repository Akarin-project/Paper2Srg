package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.BlockBush;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenBush extends WorldGenerator {

    private final BlockBush field_175908_a;

    public WorldGenBush(BlockBush blockplant) {
        this.field_175908_a = blockplant;
    }

    public boolean func_180709_b(World world, Random random, BlockPos blockposition) {
        for (int i = 0; i < 64; ++i) {
            BlockPos blockposition1 = blockposition.func_177982_a(random.nextInt(8) - random.nextInt(8), random.nextInt(4) - random.nextInt(4), random.nextInt(8) - random.nextInt(8));

            if (world.func_175623_d(blockposition1) && (!world.field_73011_w.func_177495_o() || blockposition1.func_177956_o() < 255) && this.field_175908_a.func_180671_f(world, blockposition1, this.field_175908_a.func_176223_P())) {
                world.func_180501_a(blockposition1, this.field_175908_a.func_176223_P(), 2);
            }
        }

        return true;
    }
}
