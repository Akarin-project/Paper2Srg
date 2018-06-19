package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class WorldGenMegaPineTree extends WorldGenHugeTrees {

    private static final IBlockState field_181633_e = Blocks.field_150364_r.func_176223_P().func_177226_a(BlockOldLog.field_176301_b, BlockPlanks.EnumType.SPRUCE);
    private static final IBlockState field_181634_f = Blocks.field_150362_t.func_176223_P().func_177226_a(BlockOldLeaf.field_176239_P, BlockPlanks.EnumType.SPRUCE).func_177226_a(BlockLeaves.field_176236_b, Boolean.valueOf(false));
    private static final IBlockState field_181635_g = Blocks.field_150346_d.func_176223_P().func_177226_a(BlockDirt.field_176386_a, BlockDirt.DirtType.PODZOL);
    private final boolean field_150542_e;

    public WorldGenMegaPineTree(boolean flag, boolean flag1) {
        super(flag, 13, 15, WorldGenMegaPineTree.field_181633_e, WorldGenMegaPineTree.field_181634_f);
        this.field_150542_e = flag1;
    }

    public boolean func_180709_b(World world, Random random, BlockPos blockposition) {
        int i = this.func_150533_a(random);

        if (!this.func_175929_a(world, random, blockposition, i)) {
            return false;
        } else {
            this.func_150541_c(world, blockposition.func_177958_n(), blockposition.func_177952_p(), blockposition.func_177956_o() + i, 0, random);

            for (int j = 0; j < i; ++j) {
                IBlockState iblockdata = world.func_180495_p(blockposition.func_177981_b(j));

                if (iblockdata.func_185904_a() == Material.field_151579_a || iblockdata.func_185904_a() == Material.field_151584_j) {
                    this.func_175903_a(world, blockposition.func_177981_b(j), this.field_76520_b);
                }

                if (j < i - 1) {
                    iblockdata = world.func_180495_p(blockposition.func_177982_a(1, j, 0));
                    if (iblockdata.func_185904_a() == Material.field_151579_a || iblockdata.func_185904_a() == Material.field_151584_j) {
                        this.func_175903_a(world, blockposition.func_177982_a(1, j, 0), this.field_76520_b);
                    }

                    iblockdata = world.func_180495_p(blockposition.func_177982_a(1, j, 1));
                    if (iblockdata.func_185904_a() == Material.field_151579_a || iblockdata.func_185904_a() == Material.field_151584_j) {
                        this.func_175903_a(world, blockposition.func_177982_a(1, j, 1), this.field_76520_b);
                    }

                    iblockdata = world.func_180495_p(blockposition.func_177982_a(0, j, 1));
                    if (iblockdata.func_185904_a() == Material.field_151579_a || iblockdata.func_185904_a() == Material.field_151584_j) {
                        this.func_175903_a(world, blockposition.func_177982_a(0, j, 1), this.field_76520_b);
                    }
                }
            }

            return true;
        }
    }

    private void func_150541_c(World world, int i, int j, int k, int l, Random random) {
        int i1 = random.nextInt(5) + (this.field_150542_e ? this.field_76522_a : 3);
        int j1 = 0;

        for (int k1 = k - i1; k1 <= k; ++k1) {
            int l1 = k - k1;
            int i2 = l + MathHelper.func_76141_d((float) l1 / (float) i1 * 3.5F);

            this.func_175925_a(world, new BlockPos(i, k1, j), i2 + (l1 > 0 && i2 == j1 && (k1 & 1) == 0 ? 1 : 0));
            j1 = i2;
        }

    }

    public void func_180711_a(World world, Random random, BlockPos blockposition) {
        this.func_175933_b(world, blockposition.func_177976_e().func_177978_c());
        this.func_175933_b(world, blockposition.func_177965_g(2).func_177978_c());
        this.func_175933_b(world, blockposition.func_177976_e().func_177970_e(2));
        this.func_175933_b(world, blockposition.func_177965_g(2).func_177970_e(2));

        for (int i = 0; i < 5; ++i) {
            int j = random.nextInt(64);
            int k = j % 8;
            int l = j / 8;

            if (k == 0 || k == 7 || l == 0 || l == 7) {
                this.func_175933_b(world, blockposition.func_177982_a(-3 + k, 0, -3 + l));
            }
        }

    }

    private void func_175933_b(World world, BlockPos blockposition) {
        for (int i = -2; i <= 2; ++i) {
            for (int j = -2; j <= 2; ++j) {
                if (Math.abs(i) != 2 || Math.abs(j) != 2) {
                    this.func_175934_c(world, blockposition.func_177982_a(i, 0, j));
                }
            }
        }

    }

    private void func_175934_c(World world, BlockPos blockposition) {
        for (int i = 2; i >= -3; --i) {
            BlockPos blockposition1 = blockposition.func_177981_b(i);
            IBlockState iblockdata = world.func_180495_p(blockposition1);
            Block block = iblockdata.func_177230_c();

            if (block == Blocks.field_150349_c || block == Blocks.field_150346_d) {
                this.func_175903_a(world, blockposition1, WorldGenMegaPineTree.field_181635_g);
                break;
            }

            if (iblockdata.func_185904_a() != Material.field_151579_a && i < 0) {
                break;
            }
        }

    }
}
