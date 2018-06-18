package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenHellLava extends WorldGenerator {

    private final Block block;
    private final boolean insideRock;

    public WorldGenHellLava(Block block, boolean flag) {
        this.block = block;
        this.insideRock = flag;
    }

    public boolean generate(World world, Random random, BlockPos blockposition) {
        if (world.getBlockState(blockposition.up()).getBlock() != Blocks.NETHERRACK) {
            return false;
        } else if (world.getBlockState(blockposition).getMaterial() != Material.AIR && world.getBlockState(blockposition).getBlock() != Blocks.NETHERRACK) {
            return false;
        } else {
            int i = 0;

            if (world.getBlockState(blockposition.west()).getBlock() == Blocks.NETHERRACK) {
                ++i;
            }

            if (world.getBlockState(blockposition.east()).getBlock() == Blocks.NETHERRACK) {
                ++i;
            }

            if (world.getBlockState(blockposition.north()).getBlock() == Blocks.NETHERRACK) {
                ++i;
            }

            if (world.getBlockState(blockposition.south()).getBlock() == Blocks.NETHERRACK) {
                ++i;
            }

            if (world.getBlockState(blockposition.down()).getBlock() == Blocks.NETHERRACK) {
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

            if (world.isAirBlock(blockposition.down())) {
                ++j;
            }

            if (!this.insideRock && i == 4 && j == 1 || i == 5) {
                IBlockState iblockdata = this.block.getDefaultState();

                world.setBlockState(blockposition, iblockdata, 2);
                world.immediateBlockTick(blockposition, iblockdata, random);
            }

            return true;
        }
    }
}
