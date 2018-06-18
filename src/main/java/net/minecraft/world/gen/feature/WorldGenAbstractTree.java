package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class WorldGenAbstractTree extends WorldGenerator {

    public WorldGenAbstractTree(boolean flag) {
        super(flag);
    }

    protected boolean canGrowInto(Block block) {
        Material material = block.getDefaultState().getMaterial();

        return material == Material.AIR || material == Material.LEAVES || block == Blocks.GRASS || block == Blocks.DIRT || block == Blocks.LOG || block == Blocks.LOG2 || block == Blocks.SAPLING || block == Blocks.VINE;
    }

    public void generateSaplings(World world, Random random, BlockPos blockposition) {}

    protected void setDirtAt(World world, BlockPos blockposition) {
        if (world.getBlockState(blockposition).getBlock() != Blocks.DIRT) {
            this.setBlockAndNotifyAdequately(world, blockposition, Blocks.DIRT.getDefaultState());
        }

    }
}
