package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.BlockFlower;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenFlowers extends WorldGenerator {

    private BlockFlower field_150552_a;
    private IBlockState field_175915_b;

    public WorldGenFlowers(BlockFlower blockflowers, BlockFlower.EnumFlowerType blockflowers_enumflowervarient) {
        this.func_175914_a(blockflowers, blockflowers_enumflowervarient);
    }

    public void func_175914_a(BlockFlower blockflowers, BlockFlower.EnumFlowerType blockflowers_enumflowervarient) {
        this.field_150552_a = blockflowers;
        this.field_175915_b = blockflowers.func_176223_P().func_177226_a(blockflowers.func_176494_l(), blockflowers_enumflowervarient);
    }

    public boolean func_180709_b(World world, Random random, BlockPos blockposition) {
        for (int i = 0; i < 64; ++i) {
            BlockPos blockposition1 = blockposition.func_177982_a(random.nextInt(8) - random.nextInt(8), random.nextInt(4) - random.nextInt(4), random.nextInt(8) - random.nextInt(8));

            if (world.func_175623_d(blockposition1) && (!world.field_73011_w.func_177495_o() || blockposition1.func_177956_o() < 255) && this.field_150552_a.func_180671_f(world, blockposition1, this.field_175915_b)) {
                world.func_180501_a(blockposition1, this.field_175915_b, 2);
            }
        }

        return true;
    }
}
