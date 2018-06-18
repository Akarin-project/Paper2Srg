package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenLiquids extends WorldGenerator {

    private final Block block;

    public WorldGenLiquids(Block block) {
        this.block = block;
    }

    public boolean generate(World world, Random random, BlockPos blockposition) {
        if (world.getBlockState(blockposition.up()).getBlock() != Blocks.STONE) {
            return false;
        } else if (world.getBlockState(blockposition.down()).getBlock() != Blocks.STONE) {
            return false;
        } else {
            IBlockState iblockdata = world.getBlockState(blockposition);

            if (iblockdata.getMaterial() != Material.AIR && iblockdata.getBlock() != Blocks.STONE) {
                return false;
            } else {
                int i = 0;

                if (world.getBlockState(blockposition.west()).getBlock() == Blocks.STONE) {
                    ++i;
                }

                if (world.getBlockState(blockposition.east()).getBlock() == Blocks.STONE) {
                    ++i;
                }

                if (world.getBlockState(blockposition.north()).getBlock() == Blocks.STONE) {
                    ++i;
                }

                if (world.getBlockState(blockposition.south()).getBlock() == Blocks.STONE) {
                    ++i;
                }

                int j = 0;

                if (world.isAirBlock(blockposition.west())) {
                    ++j;
                }

                if (world.isAirBlock(blockposition.east())) {
                    ++j;
                }

                if (world.isAirBlock(blockposition.north())) {
                    ++j;
                }

                if (world.isAirBlock(blockposition.south())) {
                    ++j;
                }

                if (i == 3 && j == 1) {
                    IBlockState iblockdata1 = this.block.getDefaultState();

                    world.setBlockState(blockposition, iblockdata1, 2);
                    world.immediateBlockTick(blockposition, iblockdata1, random);
                }

                return true;
            }
        }
    }
}
