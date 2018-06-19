package net.minecraft.world.gen.feature;

import com.google.common.base.Predicates;
import java.util.Iterator;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenDesertWells extends WorldGenerator {

    private static final BlockStateMatcher field_175913_a = BlockStateMatcher.func_177638_a((Block) Blocks.field_150354_m).func_177637_a(BlockSand.field_176504_a, Predicates.equalTo(BlockSand.EnumType.SAND));
    private final IBlockState field_175911_b;
    private final IBlockState field_175912_c;
    private final IBlockState field_175910_d;

    public WorldGenDesertWells() {
        this.field_175911_b = Blocks.field_150333_U.func_176223_P().func_177226_a(BlockStoneSlab.field_176556_M, BlockStoneSlab.EnumType.SAND).func_177226_a(BlockSlab.field_176554_a, BlockSlab.EnumBlockHalf.BOTTOM);
        this.field_175912_c = Blocks.field_150322_A.func_176223_P();
        this.field_175910_d = Blocks.field_150358_i.func_176223_P();
    }

    public boolean func_180709_b(World world, Random random, BlockPos blockposition) {
        while (world.func_175623_d(blockposition) && blockposition.func_177956_o() > 2) {
            blockposition = blockposition.func_177977_b();
        }

        if (!WorldGenDesertWells.field_175913_a.apply(world.func_180495_p(blockposition))) {
            return false;
        } else {
            int i;
            int j;

            for (i = -2; i <= 2; ++i) {
                for (j = -2; j <= 2; ++j) {
                    if (world.func_175623_d(blockposition.func_177982_a(i, -1, j)) && world.func_175623_d(blockposition.func_177982_a(i, -2, j))) {
                        return false;
                    }
                }
            }

            for (i = -1; i <= 0; ++i) {
                for (j = -2; j <= 2; ++j) {
                    for (int k = -2; k <= 2; ++k) {
                        world.func_180501_a(blockposition.func_177982_a(j, i, k), this.field_175912_c, 2);
                    }
                }
            }

            world.func_180501_a(blockposition, this.field_175910_d, 2);
            Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

            while (iterator.hasNext()) {
                EnumFacing enumdirection = (EnumFacing) iterator.next();

                world.func_180501_a(blockposition.func_177972_a(enumdirection), this.field_175910_d, 2);
            }

            for (i = -2; i <= 2; ++i) {
                for (j = -2; j <= 2; ++j) {
                    if (i == -2 || i == 2 || j == -2 || j == 2) {
                        world.func_180501_a(blockposition.func_177982_a(i, 1, j), this.field_175912_c, 2);
                    }
                }
            }

            world.func_180501_a(blockposition.func_177982_a(2, 1, 0), this.field_175911_b, 2);
            world.func_180501_a(blockposition.func_177982_a(-2, 1, 0), this.field_175911_b, 2);
            world.func_180501_a(blockposition.func_177982_a(0, 1, 2), this.field_175911_b, 2);
            world.func_180501_a(blockposition.func_177982_a(0, 1, -2), this.field_175911_b, 2);

            for (i = -1; i <= 1; ++i) {
                for (j = -1; j <= 1; ++j) {
                    if (i == 0 && j == 0) {
                        world.func_180501_a(blockposition.func_177982_a(i, 4, j), this.field_175912_c, 2);
                    } else {
                        world.func_180501_a(blockposition.func_177982_a(i, 4, j), this.field_175911_b, 2);
                    }
                }
            }

            for (i = 1; i <= 3; ++i) {
                world.func_180501_a(blockposition.func_177982_a(-1, i, -1), this.field_175912_c, 2);
                world.func_180501_a(blockposition.func_177982_a(-1, i, 1), this.field_175912_c, 2);
                world.func_180501_a(blockposition.func_177982_a(1, i, -1), this.field_175912_c, 2);
                world.func_180501_a(blockposition.func_177982_a(1, i, 1), this.field_175912_c, 2);
            }

            return true;
        }
    }
}
