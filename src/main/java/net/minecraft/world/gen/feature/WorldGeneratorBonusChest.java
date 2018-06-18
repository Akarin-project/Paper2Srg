package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class WorldGeneratorBonusChest extends WorldGenerator {

    public WorldGeneratorBonusChest() {}

    public boolean generate(World world, Random random, BlockPos blockposition) {
        for (IBlockState iblockdata = world.getBlockState(blockposition); (iblockdata.getMaterial() == Material.AIR || iblockdata.getMaterial() == Material.LEAVES) && blockposition.getY() > 1; iblockdata = world.getBlockState(blockposition)) {
            blockposition = blockposition.down();
        }

        if (blockposition.getY() < 1) {
            return false;
        } else {
            blockposition = blockposition.up();

            for (int i = 0; i < 4; ++i) {
                BlockPos blockposition1 = blockposition.add(random.nextInt(4) - random.nextInt(4), random.nextInt(3) - random.nextInt(3), random.nextInt(4) - random.nextInt(4));

                if (world.isAirBlock(blockposition1) && world.getBlockState(blockposition1.down()).isTopSolid()) {
                    world.setBlockState(blockposition1, Blocks.CHEST.getDefaultState(), 2);
                    TileEntity tileentity = world.getTileEntity(blockposition1);

                    if (tileentity instanceof TileEntityChest) {
                        ((TileEntityChest) tileentity).setLootTable(LootTableList.CHESTS_SPAWN_BONUS_CHEST, random.nextLong());
                    }

                    BlockPos blockposition2 = blockposition1.east();
                    BlockPos blockposition3 = blockposition1.west();
                    BlockPos blockposition4 = blockposition1.north();
                    BlockPos blockposition5 = blockposition1.south();

                    if (world.isAirBlock(blockposition3) && world.getBlockState(blockposition3.down()).isTopSolid()) {
                        world.setBlockState(blockposition3, Blocks.TORCH.getDefaultState(), 2);
                    }

                    if (world.isAirBlock(blockposition2) && world.getBlockState(blockposition2.down()).isTopSolid()) {
                        world.setBlockState(blockposition2, Blocks.TORCH.getDefaultState(), 2);
                    }

                    if (world.isAirBlock(blockposition4) && world.getBlockState(blockposition4.down()).isTopSolid()) {
                        world.setBlockState(blockposition4, Blocks.TORCH.getDefaultState(), 2);
                    }

                    if (world.isAirBlock(blockposition5) && world.getBlockState(blockposition5.down()).isTopSolid()) {
                        world.setBlockState(blockposition5, Blocks.TORCH.getDefaultState(), 2);
                    }

                    return true;
                }
            }

            return false;
        }
    }
}
