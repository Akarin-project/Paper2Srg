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

    public boolean func_180709_b(World world, Random random, BlockPos blockposition) {
        for (IBlockState iblockdata = world.func_180495_p(blockposition); (iblockdata.func_185904_a() == Material.field_151579_a || iblockdata.func_185904_a() == Material.field_151584_j) && blockposition.func_177956_o() > 1; iblockdata = world.func_180495_p(blockposition)) {
            blockposition = blockposition.func_177977_b();
        }

        if (blockposition.func_177956_o() < 1) {
            return false;
        } else {
            blockposition = blockposition.func_177984_a();

            for (int i = 0; i < 4; ++i) {
                BlockPos blockposition1 = blockposition.func_177982_a(random.nextInt(4) - random.nextInt(4), random.nextInt(3) - random.nextInt(3), random.nextInt(4) - random.nextInt(4));

                if (world.func_175623_d(blockposition1) && world.func_180495_p(blockposition1.func_177977_b()).func_185896_q()) {
                    world.func_180501_a(blockposition1, Blocks.field_150486_ae.func_176223_P(), 2);
                    TileEntity tileentity = world.func_175625_s(blockposition1);

                    if (tileentity instanceof TileEntityChest) {
                        ((TileEntityChest) tileentity).func_189404_a(LootTableList.field_186420_b, random.nextLong());
                    }

                    BlockPos blockposition2 = blockposition1.func_177974_f();
                    BlockPos blockposition3 = blockposition1.func_177976_e();
                    BlockPos blockposition4 = blockposition1.func_177978_c();
                    BlockPos blockposition5 = blockposition1.func_177968_d();

                    if (world.func_175623_d(blockposition3) && world.func_180495_p(blockposition3.func_177977_b()).func_185896_q()) {
                        world.func_180501_a(blockposition3, Blocks.field_150478_aa.func_176223_P(), 2);
                    }

                    if (world.func_175623_d(blockposition2) && world.func_180495_p(blockposition2.func_177977_b()).func_185896_q()) {
                        world.func_180501_a(blockposition2, Blocks.field_150478_aa.func_176223_P(), 2);
                    }

                    if (world.func_175623_d(blockposition4) && world.func_180495_p(blockposition4.func_177977_b()).func_185896_q()) {
                        world.func_180501_a(blockposition4, Blocks.field_150478_aa.func_176223_P(), 2);
                    }

                    if (world.func_175623_d(blockposition5) && world.func_180495_p(blockposition5.func_177977_b()).func_185896_q()) {
                        world.func_180501_a(blockposition5, Blocks.field_150478_aa.func_176223_P(), 2);
                    }

                    return true;
                }
            }

            return false;
        }
    }
}
