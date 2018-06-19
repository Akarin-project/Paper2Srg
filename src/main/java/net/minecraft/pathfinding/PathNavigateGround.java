package net.minecraft.pathfinding;

import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PathNavigateGround extends PathNavigate {

    private boolean field_179694_f;

    public PathNavigateGround(EntityLiving entityinsentient, World world) {
        super(entityinsentient, world);
    }

    protected PathFinder func_179679_a() {
        this.field_179695_a = new WalkNodeProcessor();
        this.field_179695_a.func_186317_a(true);
        return new PathFinder(this.field_179695_a);
    }

    protected boolean func_75485_k() {
        return this.field_75515_a.field_70122_E || this.func_179684_h() && this.func_75506_l() || this.field_75515_a.func_184218_aH();
    }

    protected Vec3d func_75502_i() {
        return new Vec3d(this.field_75515_a.field_70165_t, (double) this.func_179687_p(), this.field_75515_a.field_70161_v);
    }

    public Path func_179680_a(BlockPos blockposition) {
        BlockPos blockposition1;

        if (this.field_75513_b.func_180495_p(blockposition).func_185904_a() == Material.field_151579_a) {
            for (blockposition1 = blockposition.func_177977_b(); blockposition1.func_177956_o() > 0 && this.field_75513_b.func_180495_p(blockposition1).func_185904_a() == Material.field_151579_a; blockposition1 = blockposition1.func_177977_b()) {
                ;
            }

            if (blockposition1.func_177956_o() > 0) {
                return super.func_179680_a(blockposition1.func_177984_a());
            }

            while (blockposition1.func_177956_o() < this.field_75513_b.func_72800_K() && this.field_75513_b.func_180495_p(blockposition1).func_185904_a() == Material.field_151579_a) {
                blockposition1 = blockposition1.func_177984_a();
            }

            blockposition = blockposition1;
        }

        if (!this.field_75513_b.func_180495_p(blockposition).func_185904_a().func_76220_a()) {
            return super.func_179680_a(blockposition);
        } else {
            for (blockposition1 = blockposition.func_177984_a(); blockposition1.func_177956_o() < this.field_75513_b.func_72800_K() && this.field_75513_b.func_180495_p(blockposition1).func_185904_a().func_76220_a(); blockposition1 = blockposition1.func_177984_a()) {
                ;
            }

            return super.func_179680_a(blockposition1);
        }
    }

    public Path func_75494_a(Entity entity) {
        return this.func_179680_a(new BlockPos(entity));
    }

    private int func_179687_p() {
        if (this.field_75515_a.func_70090_H() && this.func_179684_h()) {
            int i = (int) this.field_75515_a.func_174813_aQ().field_72338_b;
            Block block = this.field_75513_b.func_180495_p(new BlockPos(MathHelper.func_76128_c(this.field_75515_a.field_70165_t), i, MathHelper.func_76128_c(this.field_75515_a.field_70161_v))).func_177230_c();
            int j = 0;

            do {
                if (block != Blocks.field_150358_i && block != Blocks.field_150355_j) {
                    return i;
                }

                ++i;
                block = this.field_75513_b.func_180495_p(new BlockPos(MathHelper.func_76128_c(this.field_75515_a.field_70165_t), i, MathHelper.func_76128_c(this.field_75515_a.field_70161_v))).func_177230_c();
                ++j;
            } while (j <= 16);

            return (int) this.field_75515_a.func_174813_aQ().field_72338_b;
        } else {
            return (int) (this.field_75515_a.func_174813_aQ().field_72338_b + 0.5D);
        }
    }

    protected void func_75487_m() {
        super.func_75487_m();
        if (this.field_179694_f) {
            if (this.field_75513_b.func_175678_i(new BlockPos(MathHelper.func_76128_c(this.field_75515_a.field_70165_t), (int) (this.field_75515_a.func_174813_aQ().field_72338_b + 0.5D), MathHelper.func_76128_c(this.field_75515_a.field_70161_v)))) {
                return;
            }

            for (int i = 0; i < this.field_75514_c.func_75874_d(); ++i) {
                PathPoint pathpoint = this.field_75514_c.func_75877_a(i);

                if (this.field_75513_b.func_175678_i(new BlockPos(pathpoint.field_75839_a, pathpoint.field_75837_b, pathpoint.field_75838_c))) {
                    this.field_75514_c.func_75871_b(i - 1);
                    return;
                }
            }
        }

    }

    protected boolean func_75493_a(Vec3d vec3d, Vec3d vec3d1, int i, int j, int k) {
        int l = MathHelper.func_76128_c(vec3d.field_72450_a);
        int i1 = MathHelper.func_76128_c(vec3d.field_72449_c);
        double d0 = vec3d1.field_72450_a - vec3d.field_72450_a;
        double d1 = vec3d1.field_72449_c - vec3d.field_72449_c;
        double d2 = d0 * d0 + d1 * d1;

        if (d2 < 1.0E-8D) {
            return false;
        } else {
            double d3 = 1.0D / Math.sqrt(d2);

            d0 *= d3;
            d1 *= d3;
            i += 2;
            k += 2;
            if (!this.func_179683_a(l, (int) vec3d.field_72448_b, i1, i, j, k, vec3d, d0, d1)) {
                return false;
            } else {
                i -= 2;
                k -= 2;
                double d4 = 1.0D / Math.abs(d0);
                double d5 = 1.0D / Math.abs(d1);
                double d6 = (double) l - vec3d.field_72450_a;
                double d7 = (double) i1 - vec3d.field_72449_c;

                if (d0 >= 0.0D) {
                    ++d6;
                }

                if (d1 >= 0.0D) {
                    ++d7;
                }

                d6 /= d0;
                d7 /= d1;
                int j1 = d0 < 0.0D ? -1 : 1;
                int k1 = d1 < 0.0D ? -1 : 1;
                int l1 = MathHelper.func_76128_c(vec3d1.field_72450_a);
                int i2 = MathHelper.func_76128_c(vec3d1.field_72449_c);
                int j2 = l1 - l;
                int k2 = i2 - i1;

                do {
                    if (j2 * j1 <= 0 && k2 * k1 <= 0) {
                        return true;
                    }

                    if (d6 < d7) {
                        d6 += d4;
                        l += j1;
                        j2 = l1 - l;
                    } else {
                        d7 += d5;
                        i1 += k1;
                        k2 = i2 - i1;
                    }
                } while (this.func_179683_a(l, (int) vec3d.field_72448_b, i1, i, j, k, vec3d, d0, d1));

                return false;
            }
        }
    }

    private boolean func_179683_a(int i, int j, int k, int l, int i1, int j1, Vec3d vec3d, double d0, double d1) {
        int k1 = i - l / 2;
        int l1 = k - j1 / 2;

        if (!this.func_179692_b(k1, j, l1, l, i1, j1, vec3d, d0, d1)) {
            return false;
        } else {
            for (int i2 = k1; i2 < k1 + l; ++i2) {
                for (int j2 = l1; j2 < l1 + j1; ++j2) {
                    double d2 = (double) i2 + 0.5D - vec3d.field_72450_a;
                    double d3 = (double) j2 + 0.5D - vec3d.field_72449_c;

                    if (d2 * d0 + d3 * d1 >= 0.0D) {
                        PathNodeType pathtype = this.field_179695_a.func_186319_a(this.field_75513_b, i2, j - 1, j2, this.field_75515_a, l, i1, j1, true, true);

                        if (pathtype == PathNodeType.WATER) {
                            return false;
                        }

                        if (pathtype == PathNodeType.LAVA) {
                            return false;
                        }

                        if (pathtype == PathNodeType.OPEN) {
                            return false;
                        }

                        pathtype = this.field_179695_a.func_186319_a(this.field_75513_b, i2, j, j2, this.field_75515_a, l, i1, j1, true, true);
                        float f = this.field_75515_a.func_184643_a(pathtype);

                        if (f < 0.0F || f >= 8.0F) {
                            return false;
                        }

                        if (pathtype == PathNodeType.DAMAGE_FIRE || pathtype == PathNodeType.DANGER_FIRE || pathtype == PathNodeType.DAMAGE_OTHER) {
                            return false;
                        }
                    }
                }
            }

            return true;
        }
    }

    private boolean func_179692_b(int i, int j, int k, int l, int i1, int j1, Vec3d vec3d, double d0, double d1) {
        Iterator iterator = BlockPos.func_177980_a(new BlockPos(i, j, k), new BlockPos(i + l - 1, j + i1 - 1, k + j1 - 1)).iterator();

        while (iterator.hasNext()) {
            BlockPos blockposition = (BlockPos) iterator.next();
            double d2 = (double) blockposition.func_177958_n() + 0.5D - vec3d.field_72450_a;
            double d3 = (double) blockposition.func_177952_p() + 0.5D - vec3d.field_72449_c;

            if (d2 * d0 + d3 * d1 >= 0.0D) {
                Block block = this.field_75513_b.func_180495_p(blockposition).func_177230_c();

                if (!block.func_176205_b(this.field_75513_b, blockposition)) {
                    return false;
                }
            }
        }

        return true;
    }

    public void func_179688_b(boolean flag) {
        this.field_179695_a.func_186321_b(flag);
    }

    public void func_179691_c(boolean flag) {
        this.field_179695_a.func_186317_a(flag);
    }

    public boolean func_179686_g() {
        return this.field_179695_a.func_186323_c();
    }

    public void func_179693_d(boolean flag) {
        this.field_179695_a.func_186316_c(flag);
    }

    public boolean func_179684_h() {
        return this.field_179695_a.func_186322_e();
    }

    public void func_179685_e(boolean flag) {
        this.field_179694_f = flag;
    }
}
