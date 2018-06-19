package net.minecraft.world.gen.feature;

import java.util.Iterator;
import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenEndGateway extends WorldGenerator {

    public WorldGenEndGateway() {}

    public boolean func_180709_b(World world, Random random, BlockPos blockposition) {
        Iterator iterator = BlockPos.func_177975_b(blockposition.func_177982_a(-1, -2, -1), blockposition.func_177982_a(1, 2, 1)).iterator();

        while (iterator.hasNext()) {
            BlockPos.MutableBlockPos blockposition_mutableblockposition = (BlockPos.MutableBlockPos) iterator.next();
            boolean flag = blockposition_mutableblockposition.func_177958_n() == blockposition.func_177958_n();
            boolean flag1 = blockposition_mutableblockposition.func_177956_o() == blockposition.func_177956_o();
            boolean flag2 = blockposition_mutableblockposition.func_177952_p() == blockposition.func_177952_p();
            boolean flag3 = Math.abs(blockposition_mutableblockposition.func_177956_o() - blockposition.func_177956_o()) == 2;

            if (flag && flag1 && flag2) {
                this.func_175903_a(world, new BlockPos(blockposition_mutableblockposition), Blocks.field_185775_db.func_176223_P());
            } else if (flag1) {
                this.func_175903_a(world, blockposition_mutableblockposition, Blocks.field_150350_a.func_176223_P());
            } else if (flag3 && flag && flag2) {
                this.func_175903_a(world, blockposition_mutableblockposition, Blocks.field_150357_h.func_176223_P());
            } else if ((flag || flag2) && !flag3) {
                this.func_175903_a(world, blockposition_mutableblockposition, Blocks.field_150357_h.func_176223_P());
            } else {
                this.func_175903_a(world, blockposition_mutableblockposition, Blocks.field_150350_a.func_176223_P());
            }
        }

        return true;
    }
}
