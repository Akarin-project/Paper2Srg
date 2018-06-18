package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenTallGrass extends WorldGenerator {

    private final IBlockState tallGrassState;

    public WorldGenTallGrass(BlockTallGrass.EnumType blocklonggrass_enumtallgrasstype) {
        this.tallGrassState = Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, blocklonggrass_enumtallgrasstype);
    }

    public boolean generate(World world, Random random, BlockPos blockposition) {
        for (IBlockState iblockdata = world.getBlockState(blockposition); (iblockdata.getMaterial() == Material.AIR || iblockdata.getMaterial() == Material.LEAVES) && blockposition.getY() > 0; iblockdata = world.getBlockState(blockposition)) {
            blockposition = blockposition.down();
        }

        for (int i = 0; i < 128; ++i) {
            BlockPos blockposition1 = blockposition.add(random.nextInt(8) - random.nextInt(8), random.nextInt(4) - random.nextInt(4), random.nextInt(8) - random.nextInt(8));

            if (world.isAirBlock(blockposition1) && Blocks.TALLGRASS.canBlockStay(world, blockposition1, this.tallGrassState)) {
                world.setBlockState(blockposition1, this.tallGrassState, 2);
            }
        }

        return true;
    }
}
