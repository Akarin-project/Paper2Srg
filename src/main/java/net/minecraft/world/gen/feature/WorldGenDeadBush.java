package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenDeadBush extends WorldGenerator {

    public WorldGenDeadBush() {}

    public boolean generate(World world, Random random, BlockPos blockposition) {
        for (IBlockState iblockdata = world.getBlockState(blockposition); (iblockdata.getMaterial() == Material.AIR || iblockdata.getMaterial() == Material.LEAVES) && blockposition.getY() > 0; iblockdata = world.getBlockState(blockposition)) {
            blockposition = blockposition.down();
        }

        for (int i = 0; i < 4; ++i) {
            BlockPos blockposition1 = blockposition.add(random.nextInt(8) - random.nextInt(8), random.nextInt(4) - random.nextInt(4), random.nextInt(8) - random.nextInt(8));

            if (world.isAirBlock(blockposition1) && Blocks.DEADBUSH.canBlockStay(world, blockposition1, Blocks.DEADBUSH.getDefaultState())) {
                world.setBlockState(blockposition1, Blocks.DEADBUSH.getDefaultState(), 2);
            }
        }

        return true;
    }
}
