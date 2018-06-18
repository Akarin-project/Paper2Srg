package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenShrub extends WorldGenTrees {

    private final IBlockState leavesMetadata;
    private final IBlockState woodMetadata;

    public WorldGenShrub(IBlockState iblockdata, IBlockState iblockdata1) {
        super(false);
        this.woodMetadata = iblockdata;
        this.leavesMetadata = iblockdata1;
    }

    public boolean generate(World world, Random random, BlockPos blockposition) {
        for (IBlockState iblockdata = world.getBlockState(blockposition); (iblockdata.getMaterial() == Material.AIR || iblockdata.getMaterial() == Material.LEAVES) && blockposition.getY() > 0; iblockdata = world.getBlockState(blockposition)) {
            blockposition = blockposition.down();
        }

        Block block = world.getBlockState(blockposition).getBlock();

        if (block == Blocks.DIRT || block == Blocks.GRASS) {
            blockposition = blockposition.up();
            this.setBlockAndNotifyAdequately(world, blockposition, this.woodMetadata);

            for (int i = blockposition.getY(); i <= blockposition.getY() + 2; ++i) {
                int j = i - blockposition.getY();
                int k = 2 - j;

                for (int l = blockposition.getX() - k; l <= blockposition.getX() + k; ++l) {
                    int i1 = l - blockposition.getX();

                    for (int j1 = blockposition.getZ() - k; j1 <= blockposition.getZ() + k; ++j1) {
                        int k1 = j1 - blockposition.getZ();

                        if (Math.abs(i1) != k || Math.abs(k1) != k || random.nextInt(2) != 0) {
                            BlockPos blockposition1 = new BlockPos(l, i, j1);
                            Material material = world.getBlockState(blockposition1).getMaterial();

                            if (material == Material.AIR || material == Material.LEAVES) {
                                this.setBlockAndNotifyAdequately(world, blockposition1, this.leavesMetadata);
                            }
                        }
                    }
                }
            }
        // CraftBukkit start - Return false if gen was unsuccessful
        } else {
            return false;
        }
        // CraftBukkit end


        return true;
    }
}
