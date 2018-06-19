package net.minecraft.pathfinding;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;


public class PathNavigateSwimmer extends PathNavigate {

    public PathNavigateSwimmer(EntityLiving entityinsentient, World world) {
        super(entityinsentient, world);
    }

    protected PathFinder func_179679_a() {
        return new PathFinder(new SwimNodeProcessor());
    }

    protected boolean func_75485_k() {
        return this.func_75506_l();
    }

    protected Vec3d func_75502_i() {
        return new Vec3d(this.field_75515_a.field_70165_t, this.field_75515_a.field_70163_u + (double) this.field_75515_a.field_70131_O * 0.5D, this.field_75515_a.field_70161_v);
    }

    protected void func_75508_h() {
        Vec3d vec3d = this.func_75502_i();
        float f = this.field_75515_a.field_70130_N * this.field_75515_a.field_70130_N;
        boolean flag = true;

        if (vec3d.func_72436_e(this.field_75514_c.func_75881_a(this.field_75515_a, this.field_75514_c.func_75873_e())) < (double) f) {
            this.field_75514_c.func_75875_a();
        }

        for (int i = Math.min(this.field_75514_c.func_75873_e() + 6, this.field_75514_c.func_75874_d() - 1); i > this.field_75514_c.func_75873_e(); --i) {
            Vec3d vec3d1 = this.field_75514_c.func_75881_a(this.field_75515_a, i);

            if (vec3d1.func_72436_e(vec3d) <= 36.0D && this.func_75493_a(vec3d, vec3d1, 0, 0, 0)) {
                this.field_75514_c.func_75872_c(i);
                break;
            }
        }

        this.func_179677_a(vec3d);
    }

    protected boolean func_75493_a(Vec3d vec3d, Vec3d vec3d1, int i, int j, int k) {
        RayTraceResult movingobjectposition = this.field_75513_b.func_147447_a(vec3d, new Vec3d(vec3d1.field_72450_a, vec3d1.field_72448_b + (double) this.field_75515_a.field_70131_O * 0.5D, vec3d1.field_72449_c), false, true, false);

        return movingobjectposition == null || movingobjectposition.field_72313_a == RayTraceResult.Type.MISS;
    }

    public boolean func_188555_b(BlockPos blockposition) {
        return !this.field_75513_b.func_180495_p(blockposition).func_185913_b();
    }
}
