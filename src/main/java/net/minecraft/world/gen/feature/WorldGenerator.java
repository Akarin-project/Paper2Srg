package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class WorldGenerator {

    private final boolean doBlockNotify;

    public WorldGenerator() {
        this(false);
    }

    public WorldGenerator(boolean flag) {
        this.doBlockNotify = flag;
    }

    public abstract boolean generate(World world, Random random, BlockPos blockposition);

    public void setDecorationDefaults() {}

    protected void setBlockAndNotifyAdequately(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (this.doBlockNotify) {
            world.setBlockState(blockposition, iblockdata, 3);
        } else {
            world.setBlockState(blockposition, iblockdata, 2);
        }

    }
}
