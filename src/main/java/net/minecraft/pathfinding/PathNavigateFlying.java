package net.minecraft.pathfinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;


public class PathNavigateFlying extends PathNavigate {

    public PathNavigateFlying(EntityLiving entityinsentient, World world) {
        super(entityinsentient, world);
    }

    protected PathFinder func_179679_a() {
        this.field_179695_a = new FlyingNodeProcessor();
        this.field_179695_a.func_186317_a(true);
        return new PathFinder(this.field_179695_a);
    }

    protected boolean func_75485_k() {
        return this.func_192880_g() && this.func_75506_l() || !this.field_75515_a.func_184218_aH();
    }

    protected Vec3d func_75502_i() {
        return new Vec3d(this.field_75515_a.field_70165_t, this.field_75515_a.field_70163_u, this.field_75515_a.field_70161_v);
    }

    public Path func_75494_a(Entity entity) {
        return this.func_179680_a(new BlockPos(entity));
    }

    public void func_75501_e() {
        ++this.field_75510_g;
        if (this.field_188562_p) {
            this.func_188554_j();
        }

        if (!this.func_75500_f()) {
            Vec3d vec3d;

            if (this.func_75485_k()) {
                this.func_75508_h();
            } else if (this.field_75514_c != null && this.field_75514_c.func_75873_e() < this.field_75514_c.func_75874_d()) {
                vec3d = this.field_75514_c.func_75881_a(this.field_75515_a, this.field_75514_c.func_75873_e());
                if (MathHelper.func_76128_c(this.field_75515_a.field_70165_t) == MathHelper.func_76128_c(vec3d.field_72450_a) && MathHelper.func_76128_c(this.field_75515_a.field_70163_u) == MathHelper.func_76128_c(vec3d.field_72448_b) && MathHelper.func_76128_c(this.field_75515_a.field_70161_v) == MathHelper.func_76128_c(vec3d.field_72449_c)) {
                    this.field_75514_c.func_75872_c(this.field_75514_c.func_75873_e() + 1);
                }
            }

            this.func_192876_m();
            if (!this.func_75500_f()) {
                vec3d = this.field_75514_c.func_75878_a((Entity) this.field_75515_a);
                this.field_75515_a.func_70605_aq().func_75642_a(vec3d.field_72450_a, vec3d.field_72448_b, vec3d.field_72449_c, this.field_75511_d);
            }
        }
    }

    protected boolean func_75493_a(Vec3d vec3d, Vec3d vec3d1, int i, int j, int k) {
        int l = MathHelper.func_76128_c(vec3d.field_72450_a);
        int i1 = MathHelper.func_76128_c(vec3d.field_72448_b);
        int j1 = MathHelper.func_76128_c(vec3d.field_72449_c);
        double d0 = vec3d1.field_72450_a - vec3d.field_72450_a;
        double d1 = vec3d1.field_72448_b - vec3d.field_72448_b;
        double d2 = vec3d1.field_72449_c - vec3d.field_72449_c;
        double d3 = d0 * d0 + d1 * d1 + d2 * d2;

        if (d3 < 1.0E-8D) {
            return false;
        } else {
            double d4 = 1.0D / Math.sqrt(d3);

            d0 *= d4;
            d1 *= d4;
            d2 *= d4;
            double d5 = 1.0D / Math.abs(d0);
            double d6 = 1.0D / Math.abs(d1);
            double d7 = 1.0D / Math.abs(d2);
            double d8 = (double) l - vec3d.field_72450_a;
            double d9 = (double) i1 - vec3d.field_72448_b;
            double d10 = (double) j1 - vec3d.field_72449_c;

            if (d0 >= 0.0D) {
                ++d8;
            }

            if (d1 >= 0.0D) {
                ++d9;
            }

            if (d2 >= 0.0D) {
                ++d10;
            }

            d8 /= d0;
            d9 /= d1;
            d10 /= d2;
            int k1 = d0 < 0.0D ? -1 : 1;
            int l1 = d1 < 0.0D ? -1 : 1;
            int i2 = d2 < 0.0D ? -1 : 1;
            int j2 = MathHelper.func_76128_c(vec3d1.field_72450_a);
            int k2 = MathHelper.func_76128_c(vec3d1.field_72448_b);
            int l2 = MathHelper.func_76128_c(vec3d1.field_72449_c);
            int i3 = j2 - l;
            int j3 = k2 - i1;
            int k3 = l2 - j1;

            while (i3 * k1 > 0 || j3 * l1 > 0 || k3 * i2 > 0) {
                if (d8 < d10 && d8 <= d9) {
                    d8 += d5;
                    l += k1;
                    i3 = j2 - l;
                } else if (d9 < d8 && d9 <= d10) {
                    d9 += d6;
                    i1 += l1;
                    j3 = k2 - i1;
                } else {
                    d10 += d7;
                    j1 += i2;
                    k3 = l2 - j1;
                }
            }

            return true;
        }
    }

    public void func_192879_a(boolean flag) {
        this.field_179695_a.func_186321_b(flag);
    }

    public void func_192878_b(boolean flag) {
        this.field_179695_a.func_186317_a(flag);
    }

    public void func_192877_c(boolean flag) {
        this.field_179695_a.func_186316_c(flag);
    }

    public boolean func_192880_g() {
        return this.field_179695_a.func_186322_e();
    }

    public boolean func_188555_b(BlockPos blockposition) {
        return this.field_75513_b.func_180495_p(blockposition).func_185896_q();
    }
}
