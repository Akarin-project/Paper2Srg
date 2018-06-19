package net.minecraft.world.gen.feature;

import java.util.Iterator;
import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class WorldGenSpikes extends WorldGenerator {

    private boolean field_186145_a;
    private WorldGenSpikes.EndSpike field_186146_b;
    private BlockPos field_186147_c;

    public WorldGenSpikes() {}

    public void func_186143_a(WorldGenSpikes.EndSpike worldgenender_spike) {
        this.field_186146_b = worldgenender_spike;
    }

    public void func_186144_a(boolean flag) {
        this.field_186145_a = flag;
    }

    public boolean func_180709_b(World world, Random random, BlockPos blockposition) {
        if (this.field_186146_b == null) {
            throw new IllegalStateException("Decoration requires priming with a spike");
        } else {
            int i = this.field_186146_b.func_186148_c();
            Iterator iterator = BlockPos.func_177975_b(new BlockPos(blockposition.func_177958_n() - i, 0, blockposition.func_177952_p() - i), new BlockPos(blockposition.func_177958_n() + i, this.field_186146_b.func_186149_d() + 10, blockposition.func_177952_p() + i)).iterator();

            while (iterator.hasNext()) {
                BlockPos.MutableBlockPos blockposition_mutableblockposition = (BlockPos.MutableBlockPos) iterator.next();

                if (blockposition_mutableblockposition.func_177954_c((double) blockposition.func_177958_n(), (double) blockposition_mutableblockposition.func_177956_o(), (double) blockposition.func_177952_p()) <= (double) (i * i + 1) && blockposition_mutableblockposition.func_177956_o() < this.field_186146_b.func_186149_d()) {
                    this.func_175903_a(world, blockposition_mutableblockposition, Blocks.field_150343_Z.func_176223_P());
                } else if (blockposition_mutableblockposition.func_177956_o() > 65) {
                    this.func_175903_a(world, blockposition_mutableblockposition, Blocks.field_150350_a.func_176223_P());
                }
            }

            if (this.field_186146_b.func_186150_e()) {
                for (int j = -2; j <= 2; ++j) {
                    for (int k = -2; k <= 2; ++k) {
                        if (MathHelper.func_76130_a(j) == 2 || MathHelper.func_76130_a(k) == 2) {
                            this.func_175903_a(world, new BlockPos(blockposition.func_177958_n() + j, this.field_186146_b.func_186149_d(), blockposition.func_177952_p() + k), Blocks.field_150411_aY.func_176223_P());
                            this.func_175903_a(world, new BlockPos(blockposition.func_177958_n() + j, this.field_186146_b.func_186149_d() + 1, blockposition.func_177952_p() + k), Blocks.field_150411_aY.func_176223_P());
                            this.func_175903_a(world, new BlockPos(blockposition.func_177958_n() + j, this.field_186146_b.func_186149_d() + 2, blockposition.func_177952_p() + k), Blocks.field_150411_aY.func_176223_P());
                        }

                        this.func_175903_a(world, new BlockPos(blockposition.func_177958_n() + j, this.field_186146_b.func_186149_d() + 3, blockposition.func_177952_p() + k), Blocks.field_150411_aY.func_176223_P());
                    }
                }
            }

            EntityEnderCrystal entityendercrystal = new EntityEnderCrystal(world);

            entityendercrystal.func_184516_a(this.field_186147_c);
            entityendercrystal.func_184224_h(this.field_186145_a);
            entityendercrystal.func_70012_b((double) ((float) blockposition.func_177958_n() + 0.5F), (double) (this.field_186146_b.func_186149_d() + 1), (double) ((float) blockposition.func_177952_p() + 0.5F), random.nextFloat() * 360.0F, 0.0F);
            world.func_72838_d(entityendercrystal);
            this.func_175903_a(world, new BlockPos(blockposition.func_177958_n(), this.field_186146_b.func_186149_d(), blockposition.func_177952_p()), Blocks.field_150357_h.func_176223_P());
            return true;
        }
    }

    public void func_186142_a(@Nullable BlockPos blockposition) {
        this.field_186147_c = blockposition;
    }

    public static class EndSpike {

        private final int field_186155_a;
        private final int field_186156_b;
        private final int field_186157_c;
        private final int field_186158_d;
        private final boolean field_186159_e;
        private final AxisAlignedBB field_186160_f;

        public EndSpike(int i, int j, int k, int l, boolean flag) {
            this.field_186155_a = i;
            this.field_186156_b = j;
            this.field_186157_c = k;
            this.field_186158_d = l;
            this.field_186159_e = flag;
            this.field_186160_f = new AxisAlignedBB((double) (i - k), 0.0D, (double) (j - k), (double) (i + k), 256.0D, (double) (j + k));
        }

        public boolean func_186154_a(BlockPos blockposition) {
            int i = this.field_186155_a - this.field_186157_c;
            int j = this.field_186156_b - this.field_186157_c;

            return blockposition.func_177958_n() == (i & -16) && blockposition.func_177952_p() == (j & -16);
        }

        public int func_186151_a() {
            return this.field_186155_a;
        }

        public int func_186152_b() {
            return this.field_186156_b;
        }

        public int func_186148_c() {
            return this.field_186157_c;
        }

        public int func_186149_d() {
            return this.field_186158_d;
        }

        public boolean func_186150_e() {
            return this.field_186159_e;
        }

        public AxisAlignedBB func_186153_f() {
            return this.field_186160_f;
        }
    }
}
