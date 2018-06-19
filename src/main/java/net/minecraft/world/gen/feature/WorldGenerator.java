package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class WorldGenerator {

    private final boolean field_76488_a;

    public WorldGenerator() {
        this(false);
    }

    public WorldGenerator(boolean flag) {
        this.field_76488_a = flag;
    }

    public abstract boolean func_180709_b(World world, Random random, BlockPos blockposition);

    public void func_175904_e() {}

    protected void func_175903_a(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (this.field_76488_a) {
            world.func_180501_a(blockposition, iblockdata, 3);
        } else {
            world.func_180501_a(blockposition, iblockdata, 2);
        }

    }
}
