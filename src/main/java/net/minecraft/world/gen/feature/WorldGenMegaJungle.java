package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.BlockVine;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class WorldGenMegaJungle extends WorldGenHugeTrees {

    public WorldGenMegaJungle(boolean flag, int i, int j, IBlockState iblockdata, IBlockState iblockdata1) {
        super(flag, i, j, iblockdata, iblockdata1);
    }

    public boolean func_180709_b(World world, Random random, BlockPos blockposition) {
        int i = this.func_150533_a(random);

        if (!this.func_175929_a(world, random, blockposition, i)) {
            return false;
        } else {
            this.func_175930_c(world, blockposition.func_177981_b(i), 2);

            for (int j = blockposition.func_177956_o() + i - 2 - random.nextInt(4); j > blockposition.func_177956_o() + i / 2; j -= 2 + random.nextInt(4)) {
                float f = random.nextFloat() * 6.2831855F;
                int k = blockposition.func_177958_n() + (int) (0.5F + MathHelper.func_76134_b(f) * 4.0F);
                int l = blockposition.func_177952_p() + (int) (0.5F + MathHelper.func_76126_a(f) * 4.0F);

                int i1;

                for (i1 = 0; i1 < 5; ++i1) {
                    k = blockposition.func_177958_n() + (int) (1.5F + MathHelper.func_76134_b(f) * (float) i1);
                    l = blockposition.func_177952_p() + (int) (1.5F + MathHelper.func_76126_a(f) * (float) i1);
                    this.func_175903_a(world, new BlockPos(k, j - 3 + i1 / 2, l), this.field_76520_b);
                }

                i1 = 1 + random.nextInt(2);
                int j1 = j;

                for (int k1 = j - i1; k1 <= j1; ++k1) {
                    int l1 = k1 - j1;

                    this.func_175928_b(world, new BlockPos(k, k1, l), 1 - l1);
                }
            }

            for (int i2 = 0; i2 < i; ++i2) {
                BlockPos blockposition1 = blockposition.func_177981_b(i2);

                if (this.func_150523_a(world.func_180495_p(blockposition1).func_177230_c())) {
                    this.func_175903_a(world, blockposition1, this.field_76520_b);
                    if (i2 > 0) {
                        this.func_181632_a(world, random, blockposition1.func_177976_e(), BlockVine.field_176278_M);
                        this.func_181632_a(world, random, blockposition1.func_177978_c(), BlockVine.field_176279_N);
                    }
                }

                if (i2 < i - 1) {
                    BlockPos blockposition2 = blockposition1.func_177974_f();

                    if (this.func_150523_a(world.func_180495_p(blockposition2).func_177230_c())) {
                        this.func_175903_a(world, blockposition2, this.field_76520_b);
                        if (i2 > 0) {
                            this.func_181632_a(world, random, blockposition2.func_177974_f(), BlockVine.field_176280_O);
                            this.func_181632_a(world, random, blockposition2.func_177978_c(), BlockVine.field_176279_N);
                        }
                    }

                    BlockPos blockposition3 = blockposition1.func_177968_d().func_177974_f();

                    if (this.func_150523_a(world.func_180495_p(blockposition3).func_177230_c())) {
                        this.func_175903_a(world, blockposition3, this.field_76520_b);
                        if (i2 > 0) {
                            this.func_181632_a(world, random, blockposition3.func_177974_f(), BlockVine.field_176280_O);
                            this.func_181632_a(world, random, blockposition3.func_177968_d(), BlockVine.field_176273_b);
                        }
                    }

                    BlockPos blockposition4 = blockposition1.func_177968_d();

                    if (this.func_150523_a(world.func_180495_p(blockposition4).func_177230_c())) {
                        this.func_175903_a(world, blockposition4, this.field_76520_b);
                        if (i2 > 0) {
                            this.func_181632_a(world, random, blockposition4.func_177976_e(), BlockVine.field_176278_M);
                            this.func_181632_a(world, random, blockposition4.func_177968_d(), BlockVine.field_176273_b);
                        }
                    }
                }
            }

            return true;
        }
    }

    private void func_181632_a(World world, Random random, BlockPos blockposition, PropertyBool blockstateboolean) {
        if (random.nextInt(3) > 0 && world.func_175623_d(blockposition)) {
            this.func_175903_a(world, blockposition, Blocks.field_150395_bd.func_176223_P().func_177226_a(blockstateboolean, Boolean.valueOf(true)));
        }

    }

    private void func_175930_c(World world, BlockPos blockposition, int i) {
        boolean flag = true;

        for (int j = -2; j <= 0; ++j) {
            this.func_175925_a(world, blockposition.func_177981_b(j), i + 1 - j);
        }

    }
}
