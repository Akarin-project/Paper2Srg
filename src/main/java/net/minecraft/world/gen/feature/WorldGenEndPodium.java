package net.minecraft.world.gen.feature;

import java.util.Iterator;
import java.util.Random;

import net.minecraft.block.BlockTorch;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenEndPodium extends WorldGenerator {

    public static final BlockPos field_186139_a = BlockPos.field_177992_a;
    public static final BlockPos field_186140_b = new BlockPos(WorldGenEndPodium.field_186139_a.func_177958_n() - 4 & -16, 0, WorldGenEndPodium.field_186139_a.func_177952_p() - 4 & -16);
    private final boolean field_186141_c;

    public WorldGenEndPodium(boolean flag) {
        this.field_186141_c = flag;
    }

    public boolean func_180709_b(World world, Random random, BlockPos blockposition) {
        Iterator iterator = BlockPos.func_177975_b(new BlockPos(blockposition.func_177958_n() - 4, blockposition.func_177956_o() - 1, blockposition.func_177952_p() - 4), new BlockPos(blockposition.func_177958_n() + 4, blockposition.func_177956_o() + 32, blockposition.func_177952_p() + 4)).iterator();

        while (iterator.hasNext()) {
            BlockPos.MutableBlockPos blockposition_mutableblockposition = (BlockPos.MutableBlockPos) iterator.next();
            double d0 = blockposition_mutableblockposition.func_185332_f(blockposition.func_177958_n(), blockposition_mutableblockposition.func_177956_o(), blockposition.func_177952_p());

            if (d0 <= 3.5D) {
                if (blockposition_mutableblockposition.func_177956_o() < blockposition.func_177956_o()) {
                    if (d0 <= 2.5D) {
                        this.func_175903_a(world, blockposition_mutableblockposition, Blocks.field_150357_h.func_176223_P());
                    } else if (blockposition_mutableblockposition.func_177956_o() < blockposition.func_177956_o()) {
                        this.func_175903_a(world, blockposition_mutableblockposition, Blocks.field_150377_bs.func_176223_P());
                    }
                } else if (blockposition_mutableblockposition.func_177956_o() > blockposition.func_177956_o()) {
                    this.func_175903_a(world, blockposition_mutableblockposition, Blocks.field_150350_a.func_176223_P());
                } else if (d0 > 2.5D) {
                    this.func_175903_a(world, blockposition_mutableblockposition, Blocks.field_150357_h.func_176223_P());
                } else if (this.field_186141_c) {
                    this.func_175903_a(world, new BlockPos(blockposition_mutableblockposition), Blocks.field_150384_bq.func_176223_P());
                } else {
                    this.func_175903_a(world, new BlockPos(blockposition_mutableblockposition), Blocks.field_150350_a.func_176223_P());
                }
            }
        }

        for (int i = 0; i < 4; ++i) {
            this.func_175903_a(world, blockposition.func_177981_b(i), Blocks.field_150357_h.func_176223_P());
        }

        BlockPos blockposition1 = blockposition.func_177981_b(2);
        Iterator iterator1 = EnumFacing.Plane.HORIZONTAL.iterator();

        while (iterator1.hasNext()) {
            EnumFacing enumdirection = (EnumFacing) iterator1.next();

            this.func_175903_a(world, blockposition1.func_177972_a(enumdirection), Blocks.field_150478_aa.func_176223_P().func_177226_a(BlockTorch.field_176596_a, enumdirection));
        }

        return true;
    }
}
