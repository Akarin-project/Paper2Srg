package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenDeadBush extends WorldGenerator {

    public WorldGenDeadBush() {}

    public boolean func_180709_b(World world, Random random, BlockPos blockposition) {
        for (IBlockState iblockdata = world.func_180495_p(blockposition); (iblockdata.func_185904_a() == Material.field_151579_a || iblockdata.func_185904_a() == Material.field_151584_j) && blockposition.func_177956_o() > 0; iblockdata = world.func_180495_p(blockposition)) {
            blockposition = blockposition.func_177977_b();
        }

        for (int i = 0; i < 4; ++i) {
            BlockPos blockposition1 = blockposition.func_177982_a(random.nextInt(8) - random.nextInt(8), random.nextInt(4) - random.nextInt(4), random.nextInt(8) - random.nextInt(8));

            if (world.func_175623_d(blockposition1) && Blocks.field_150330_I.func_180671_f(world, blockposition1, Blocks.field_150330_I.func_176223_P())) {
                world.func_180501_a(blockposition1, Blocks.field_150330_I.func_176223_P(), 2);
            }
        }

        return true;
    }
}
