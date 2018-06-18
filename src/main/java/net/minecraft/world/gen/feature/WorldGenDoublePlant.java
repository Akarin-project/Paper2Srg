package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.BlockDoublePlant;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenDoublePlant extends WorldGenerator {

    private BlockDoublePlant.EnumPlantType plantType;

    public WorldGenDoublePlant() {}

    public void setPlantType(BlockDoublePlant.EnumPlantType blocktallplant_enumtallflowervariants) {
        this.plantType = blocktallplant_enumtallflowervariants;
    }

    public boolean generate(World world, Random random, BlockPos blockposition) {
        boolean flag = false;

        for (int i = 0; i < 64; ++i) {
            BlockPos blockposition1 = blockposition.add(random.nextInt(8) - random.nextInt(8), random.nextInt(4) - random.nextInt(4), random.nextInt(8) - random.nextInt(8));

            if (world.isAirBlock(blockposition1) && (!world.provider.isNether() || blockposition1.getY() < 254) && Blocks.DOUBLE_PLANT.canPlaceBlockAt(world, blockposition1)) {
                Blocks.DOUBLE_PLANT.placeAt(world, blockposition1, this.plantType, 2);
                flag = true;
            }
        }

        return flag;
    }
}
