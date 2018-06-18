package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.BlockBush;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenBush extends WorldGenerator {

    private final BlockBush block;

    public WorldGenBush(BlockBush blockplant) {
        this.block = blockplant;
    }

    public boolean generate(World world, Random random, BlockPos blockposition) {
        for (int i = 0; i < 64; ++i) {
            BlockPos blockposition1 = blockposition.add(random.nextInt(8) - random.nextInt(8), random.nextInt(4) - random.nextInt(4), random.nextInt(8) - random.nextInt(8));

            if (world.isAirBlock(blockposition1) && (!world.provider.isNether() || blockposition1.getY() < 255) && this.block.canBlockStay(world, blockposition1, this.block.getDefaultState())) {
                world.setBlockState(blockposition1, this.block.getDefaultState(), 2);
            }
        }

        return true;
    }
}
