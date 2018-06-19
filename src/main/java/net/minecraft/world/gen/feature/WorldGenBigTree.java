package net.minecraft.world.gen.feature;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class WorldGenBigTree extends WorldGenAbstractTree {

    private Random field_175949_k;
    private World field_175946_l;
    private BlockPos field_175947_m;
    int field_76504_e;
    int field_76501_f;
    double field_76502_g;
    double field_175944_d;
    double field_175945_e;
    double field_76513_k;
    int field_175943_g;
    int field_175950_h;
    int field_76508_n;
    List<WorldGenBigTree.FoliageCoordinates> field_175948_j;

    public WorldGenBigTree(boolean flag) {
        super(flag);
        this.field_175947_m = BlockPos.field_177992_a;
        this.field_76502_g = 0.618D;
        this.field_175944_d = 0.381D;
        this.field_175945_e = 1.0D;
        this.field_76513_k = 1.0D;
        this.field_175943_g = 1;
        this.field_175950_h = 12;
        this.field_76508_n = 4;
    }

    void func_76489_a() {
        this.field_76501_f = (int) ((double) this.field_76504_e * this.field_76502_g);
        if (this.field_76501_f >= this.field_76504_e) {
            this.field_76501_f = this.field_76504_e - 1;
        }

        int i = (int) (1.382D + Math.pow(this.field_76513_k * (double) this.field_76504_e / 13.0D, 2.0D));

        if (i < 1) {
            i = 1;
        }

        int j = this.field_175947_m.func_177956_o() + this.field_76501_f;
        int k = this.field_76504_e - this.field_76508_n;

        this.field_175948_j = Lists.newArrayList();
        this.field_175948_j.add(new WorldGenBigTree.FoliageCoordinates(this.field_175947_m.func_177981_b(k), j));

        for (; k >= 0; --k) {
            float f = this.func_76490_a(k);

            if (f >= 0.0F) {
                for (int l = 0; l < i; ++l) {
                    double d0 = this.field_175945_e * (double) f * ((double) this.field_175949_k.nextFloat() + 0.328D);
                    double d1 = (double) (this.field_175949_k.nextFloat() * 2.0F) * 3.141592653589793D;
                    double d2 = d0 * Math.sin(d1) + 0.5D;
                    double d3 = d0 * Math.cos(d1) + 0.5D;
                    BlockPos blockposition = this.field_175947_m.func_177963_a(d2, (double) (k - 1), d3);
                    BlockPos blockposition1 = blockposition.func_177981_b(this.field_76508_n);

                    if (this.func_175936_a(blockposition, blockposition1) == -1) {
                        int i1 = this.field_175947_m.func_177958_n() - blockposition.func_177958_n();
                        int j1 = this.field_175947_m.func_177952_p() - blockposition.func_177952_p();
                        double d4 = (double) blockposition.func_177956_o() - Math.sqrt((double) (i1 * i1 + j1 * j1)) * this.field_175944_d;
                        int k1 = d4 > (double) j ? j : (int) d4;
                        BlockPos blockposition2 = new BlockPos(this.field_175947_m.func_177958_n(), k1, this.field_175947_m.func_177952_p());

                        if (this.func_175936_a(blockposition2, blockposition) == -1) {
                            this.field_175948_j.add(new WorldGenBigTree.FoliageCoordinates(blockposition, blockposition2.func_177956_o()));
                        }
                    }
                }
            }
        }

    }

    void func_181631_a(BlockPos blockposition, float f, IBlockState iblockdata) {
        int i = (int) ((double) f + 0.618D);

        for (int j = -i; j <= i; ++j) {
            for (int k = -i; k <= i; ++k) {
                if (Math.pow((double) Math.abs(j) + 0.5D, 2.0D) + Math.pow((double) Math.abs(k) + 0.5D, 2.0D) <= (double) (f * f)) {
                    BlockPos blockposition1 = blockposition.func_177982_a(j, 0, k);
                    Material material = this.field_175946_l.func_180495_p(blockposition1).func_185904_a();

                    if (material == Material.field_151579_a || material == Material.field_151584_j) {
                        this.func_175903_a(this.field_175946_l, blockposition1, iblockdata);
                    }
                }
            }
        }

    }

    float func_76490_a(int i) {
        if ((float) i < (float) this.field_76504_e * 0.3F) {
            return -1.0F;
        } else {
            float f = (float) this.field_76504_e / 2.0F;
            float f1 = f - (float) i;
            float f2 = MathHelper.func_76129_c(f * f - f1 * f1);

            if (f1 == 0.0F) {
                f2 = f;
            } else if (Math.abs(f1) >= f) {
                return 0.0F;
            }

            return f2 * 0.5F;
        }
    }

    float func_76495_b(int i) {
        return i >= 0 && i < this.field_76508_n ? (i != 0 && i != this.field_76508_n - 1 ? 3.0F : 2.0F) : -1.0F;
    }

    void func_175940_a(BlockPos blockposition) {
        for (int i = 0; i < this.field_76508_n; ++i) {
            this.func_181631_a(blockposition.func_177981_b(i), this.func_76495_b(i), Blocks.field_150362_t.func_176223_P().func_177226_a(BlockLeaves.field_176236_b, Boolean.valueOf(false)));
        }

    }

    void func_175937_a(BlockPos blockposition, BlockPos blockposition1, Block block) {
        BlockPos blockposition2 = blockposition1.func_177982_a(-blockposition.func_177958_n(), -blockposition.func_177956_o(), -blockposition.func_177952_p());
        int i = this.func_175935_b(blockposition2);
        float f = (float) blockposition2.func_177958_n() / (float) i;
        float f1 = (float) blockposition2.func_177956_o() / (float) i;
        float f2 = (float) blockposition2.func_177952_p() / (float) i;

        for (int j = 0; j <= i; ++j) {
            BlockPos blockposition3 = blockposition.func_177963_a((double) (0.5F + (float) j * f), (double) (0.5F + (float) j * f1), (double) (0.5F + (float) j * f2));
            BlockLog.EnumAxis blocklogabstract_enumlogrotation = this.func_175938_b(blockposition, blockposition3);

            this.func_175903_a(this.field_175946_l, blockposition3, block.func_176223_P().func_177226_a(BlockLog.field_176299_a, blocklogabstract_enumlogrotation));
        }

    }

    private int func_175935_b(BlockPos blockposition) {
        int i = MathHelper.func_76130_a(blockposition.func_177958_n());
        int j = MathHelper.func_76130_a(blockposition.func_177956_o());
        int k = MathHelper.func_76130_a(blockposition.func_177952_p());

        return k > i && k > j ? k : (j > i ? j : i);
    }

    private BlockLog.EnumAxis func_175938_b(BlockPos blockposition, BlockPos blockposition1) {
        BlockLog.EnumAxis blocklogabstract_enumlogrotation = BlockLog.EnumAxis.Y;
        int i = Math.abs(blockposition1.func_177958_n() - blockposition.func_177958_n());
        int j = Math.abs(blockposition1.func_177952_p() - blockposition.func_177952_p());
        int k = Math.max(i, j);

        if (k > 0) {
            if (i == k) {
                blocklogabstract_enumlogrotation = BlockLog.EnumAxis.X;
            } else if (j == k) {
                blocklogabstract_enumlogrotation = BlockLog.EnumAxis.Z;
            }
        }

        return blocklogabstract_enumlogrotation;
    }

    void func_175941_b() {
        Iterator iterator = this.field_175948_j.iterator();

        while (iterator.hasNext()) {
            WorldGenBigTree.FoliageCoordinates worldgenbigtree_position = (WorldGenBigTree.FoliageCoordinates) iterator.next();

            this.func_175940_a((BlockPos) worldgenbigtree_position);
        }

    }

    boolean func_76493_c(int i) {
        return (double) i >= (double) this.field_76504_e * 0.2D;
    }

    void func_175942_c() {
        BlockPos blockposition = this.field_175947_m;
        BlockPos blockposition1 = this.field_175947_m.func_177981_b(this.field_76501_f);
        Block block = Blocks.field_150364_r;

        this.func_175937_a(blockposition, blockposition1, block);
        if (this.field_175943_g == 2) {
            this.func_175937_a(blockposition.func_177974_f(), blockposition1.func_177974_f(), block);
            this.func_175937_a(blockposition.func_177974_f().func_177968_d(), blockposition1.func_177974_f().func_177968_d(), block);
            this.func_175937_a(blockposition.func_177968_d(), blockposition1.func_177968_d(), block);
        }

    }

    void func_175939_d() {
        Iterator iterator = this.field_175948_j.iterator();

        while (iterator.hasNext()) {
            WorldGenBigTree.FoliageCoordinates worldgenbigtree_position = (WorldGenBigTree.FoliageCoordinates) iterator.next();
            int i = worldgenbigtree_position.func_177999_q();
            BlockPos blockposition = new BlockPos(this.field_175947_m.func_177958_n(), i, this.field_175947_m.func_177952_p());

            if (!blockposition.equals(worldgenbigtree_position) && this.func_76493_c(i - this.field_175947_m.func_177956_o())) {
                this.func_175937_a(blockposition, (BlockPos) worldgenbigtree_position, Blocks.field_150364_r);
            }
        }

    }

    int func_175936_a(BlockPos blockposition, BlockPos blockposition1) {
        BlockPos blockposition2 = blockposition1.func_177982_a(-blockposition.func_177958_n(), -blockposition.func_177956_o(), -blockposition.func_177952_p());
        int i = this.func_175935_b(blockposition2);
        float f = (float) blockposition2.func_177958_n() / (float) i;
        float f1 = (float) blockposition2.func_177956_o() / (float) i;
        float f2 = (float) blockposition2.func_177952_p() / (float) i;

        if (i == 0) {
            return -1;
        } else {
            for (int j = 0; j <= i; ++j) {
                BlockPos blockposition3 = blockposition.func_177963_a((double) (0.5F + (float) j * f), (double) (0.5F + (float) j * f1), (double) (0.5F + (float) j * f2));

                if (!this.func_150523_a(this.field_175946_l.func_180495_p(blockposition3).func_177230_c())) {
                    return j;
                }
            }

            return -1;
        }
    }

    public void func_175904_e() {
        this.field_76508_n = 5;
    }

    public boolean func_180709_b(World world, Random random, BlockPos blockposition) {
        this.field_175946_l = world;
        this.field_175947_m = blockposition;
        this.field_175949_k = new Random(random.nextLong());
        if (this.field_76504_e == 0) {
            this.field_76504_e = 5 + this.field_175949_k.nextInt(this.field_175950_h);
        }

        if (!this.func_76497_e()) {
            return false;
        } else {
            this.func_76489_a();
            this.func_175941_b();
            this.func_175942_c();
            this.func_175939_d();
            return true;
        }
    }

    private boolean func_76497_e() {
        Block block = this.field_175946_l.func_180495_p(this.field_175947_m.func_177977_b()).func_177230_c();

        if (block != Blocks.field_150346_d && block != Blocks.field_150349_c && block != Blocks.field_150458_ak) {
            return false;
        } else {
            int i = this.func_175936_a(this.field_175947_m, this.field_175947_m.func_177981_b(this.field_76504_e - 1));

            if (i == -1) {
                return true;
            } else if (i < 6) {
                return false;
            } else {
                this.field_76504_e = i;
                return true;
            }
        }
    }

    static class FoliageCoordinates extends BlockPos {

        private final int field_178000_b;

        public FoliageCoordinates(BlockPos blockposition, int i) {
            super(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p());
            this.field_178000_b = i;
        }

        public int func_177999_q() {
            return this.field_178000_b;
        }
    }
}
