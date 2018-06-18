package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.BlockFlower;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenFlowers extends WorldGenerator {

    private BlockFlower flower;
    private IBlockState state;

    public WorldGenFlowers(BlockFlower blockflowers, BlockFlower.EnumFlowerType blockflowers_enumflowervarient) {
        this.setGeneratedBlock(blockflowers, blockflowers_enumflowervarient);
    }

    public void setGeneratedBlock(BlockFlower blockflowers, BlockFlower.EnumFlowerType blockflowers_enumflowervarient) {
        this.flower = blockflowers;
        this.state = blockflowers.getDefaultState().withProperty(blockflowers.getTypeProperty(), blockflowers_enumflowervarient);
    }

    public boolean generate(World world, Random random, BlockPos blockposition) {
        for (int i = 0; i < 64; ++i) {
            BlockPos blockposition1 = blockposition.add(random.nextInt(8) - random.nextInt(8), random.nextInt(4) - random.nextInt(4), random.nextInt(8) - random.nextInt(8));

            if (world.isAirBlock(blockposition1) && (!world.provider.isNether() || blockposition1.getY() < 255) && this.flower.canBlockStay(world, blockposition1, this.state)) {
                world.setBlockState(blockposition1, this.state, 2);
            }
        }

        return true;
    }
}
