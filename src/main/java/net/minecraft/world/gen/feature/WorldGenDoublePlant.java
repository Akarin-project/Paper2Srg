package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.BlockDoublePlant;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenDoublePlant extends WorldGenerator {

    private BlockDoublePlant.EnumPlantType field_150549_a;

    public WorldGenDoublePlant() {}

    public void func_180710_a(BlockDoublePlant.EnumPlantType blocktallplant_enumtallflowervariants) {
        this.field_150549_a = blocktallplant_enumtallflowervariants;
    }

    public boolean func_180709_b(World world, Random random, BlockPos blockposition) {
        boolean flag = false;

        for (int i = 0; i < 64; ++i) {
            BlockPos blockposition1 = blockposition.func_177982_a(random.nextInt(8) - random.nextInt(8), random.nextInt(4) - random.nextInt(4), random.nextInt(8) - random.nextInt(8));

            if (world.func_175623_d(blockposition1) && (!world.field_73011_w.func_177495_o() || blockposition1.func_177956_o() < 254) && Blocks.field_150398_cm.func_176196_c(world, blockposition1)) {
                Blocks.field_150398_cm.func_176491_a(world, blockposition1, this.field_150549_a, 2);
                flag = true;
            }
        }

        return flag;
    }
}
