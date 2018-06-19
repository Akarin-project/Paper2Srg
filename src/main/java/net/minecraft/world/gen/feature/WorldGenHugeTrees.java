package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class WorldGenHugeTrees extends WorldGenAbstractTree {

    protected final int field_76522_a;
    protected final IBlockState field_76520_b;
    protected final IBlockState field_76521_c;
    protected int field_150538_d;

    public WorldGenHugeTrees(boolean flag, int i, int j, IBlockState iblockdata, IBlockState iblockdata1) {
        super(flag);
        this.field_76522_a = i;
        this.field_150538_d = j;
        this.field_76520_b = iblockdata;
        this.field_76521_c = iblockdata1;
    }

    protected int func_150533_a(Random random) {
        int i = random.nextInt(3) + this.field_76522_a;

        if (this.field_150538_d > 1) {
            i += random.nextInt(this.field_150538_d);
        }

        return i;
    }

    private boolean func_175926_c(World world, BlockPos blockposition, int i) {
        boolean flag = true;

        if (blockposition.func_177956_o() >= 1 && blockposition.func_177956_o() + i + 1 <= 256) {
            for (int j = 0; j <= 1 + i; ++j) {
                byte b0 = 2;

                if (j == 0) {
                    b0 = 1;
                } else if (j >= 1 + i - 2) {
                    b0 = 2;
                }

                for (int k = -b0; k <= b0 && flag; ++k) {
                    for (int l = -b0; l <= b0 && flag; ++l) {
                        if (blockposition.func_177956_o() + j < 0 || blockposition.func_177956_o() + j >= 256 || (!this.func_150523_a(world.func_180495_p(blockposition.func_177982_a(k, j, l)).func_177230_c()) && world.func_180495_p(blockposition.func_177982_a(k, j, l)).func_177230_c() != Blocks.field_150345_g)) { // CraftBukkit - ignore our own saplings
                            flag = false;
                        }
                    }
                }
            }

            return flag;
        } else {
            return false;
        }
    }

    private boolean func_175927_a(BlockPos blockposition, World world) {
        BlockPos blockposition1 = blockposition.func_177977_b();
        Block block = world.func_180495_p(blockposition1).func_177230_c();

        if ((block == Blocks.field_150349_c || block == Blocks.field_150346_d) && blockposition.func_177956_o() >= 2) {
            this.func_175921_a(world, blockposition1);
            this.func_175921_a(world, blockposition1.func_177974_f());
            this.func_175921_a(world, blockposition1.func_177968_d());
            this.func_175921_a(world, blockposition1.func_177968_d().func_177974_f());
            return true;
        } else {
            return false;
        }
    }

    protected boolean func_175929_a(World world, Random random, BlockPos blockposition, int i) {
        return this.func_175926_c(world, blockposition, i) && this.func_175927_a(blockposition, world);
    }

    protected void func_175925_a(World world, BlockPos blockposition, int i) {
        int j = i * i;

        for (int k = -i; k <= i + 1; ++k) {
            for (int l = -i; l <= i + 1; ++l) {
                int i1 = k - 1;
                int j1 = l - 1;

                if (k * k + l * l <= j || i1 * i1 + j1 * j1 <= j || k * k + j1 * j1 <= j || i1 * i1 + l * l <= j) {
                    BlockPos blockposition1 = blockposition.func_177982_a(k, 0, l);
                    Material material = world.func_180495_p(blockposition1).func_185904_a();

                    if (material == Material.field_151579_a || material == Material.field_151584_j) {
                        this.func_175903_a(world, blockposition1, this.field_76521_c);
                    }
                }
            }
        }

    }

    protected void func_175928_b(World world, BlockPos blockposition, int i) {
        int j = i * i;

        for (int k = -i; k <= i; ++k) {
            for (int l = -i; l <= i; ++l) {
                if (k * k + l * l <= j) {
                    BlockPos blockposition1 = blockposition.func_177982_a(k, 0, l);
                    Material material = world.func_180495_p(blockposition1).func_185904_a();

                    if (material == Material.field_151579_a || material == Material.field_151584_j) {
                        this.func_175903_a(world, blockposition1, this.field_76521_c);
                    }
                }
            }
        }

    }
}
