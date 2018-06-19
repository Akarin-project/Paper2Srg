package net.minecraft.entity.projectile;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public final class ProjectileHelper {

    public static RayTraceResult func_188802_a(Entity entity, boolean flag, boolean flag1, Entity entity1) {
        double d0 = entity.field_70165_t;
        double d1 = entity.field_70163_u;
        double d2 = entity.field_70161_v;
        double d3 = entity.field_70159_w;
        double d4 = entity.field_70181_x;
        double d5 = entity.field_70179_y;
        World world = entity.field_70170_p;
        Vec3d vec3d = new Vec3d(d0, d1, d2);
        Vec3d vec3d1 = new Vec3d(d0 + d3, d1 + d4, d2 + d5);
        RayTraceResult movingobjectposition = world.func_147447_a(vec3d, vec3d1, false, true, false);

        if (flag) {
            if (movingobjectposition != null) {
                vec3d1 = new Vec3d(movingobjectposition.field_72307_f.field_72450_a, movingobjectposition.field_72307_f.field_72448_b, movingobjectposition.field_72307_f.field_72449_c);
            }

            Entity entity2 = null;
            List list = world.func_72839_b(entity, entity.func_174813_aQ().func_72321_a(d3, d4, d5).func_186662_g(1.0D));
            double d6 = 0.0D;

            for (int i = 0; i < list.size(); ++i) {
                Entity entity3 = (Entity) list.get(i);

                if (entity3.func_70067_L() && (flag1 || !entity3.func_70028_i(entity1)) && !entity3.field_70145_X) {
                    AxisAlignedBB axisalignedbb = entity3.func_174813_aQ().func_186662_g(0.30000001192092896D);
                    RayTraceResult movingobjectposition1 = axisalignedbb.func_72327_a(vec3d, vec3d1);

                    if (movingobjectposition1 != null) {
                        double d7 = vec3d.func_72436_e(movingobjectposition1.field_72307_f);

                        if (d7 < d6 || d6 == 0.0D) {
                            entity2 = entity3;
                            d6 = d7;
                        }
                    }
                }
            }

            if (entity2 != null) {
                movingobjectposition = new RayTraceResult(entity2);
            }
        }

        return movingobjectposition;
    }

    public static final void func_188803_a(Entity entity, float f) {
        double d0 = entity.field_70159_w;
        double d1 = entity.field_70181_x;
        double d2 = entity.field_70179_y;
        float f1 = MathHelper.func_76133_a(d0 * d0 + d2 * d2);

        entity.field_70177_z = (float) (MathHelper.func_181159_b(d2, d0) * 57.2957763671875D) + 90.0F;

        for (entity.field_70125_A = (float) (MathHelper.func_181159_b((double) f1, d1) * 57.2957763671875D) - 90.0F; entity.field_70125_A - entity.field_70127_C < -180.0F; entity.field_70127_C -= 360.0F) {
            ;
        }

        while (entity.field_70125_A - entity.field_70127_C >= 180.0F) {
            entity.field_70127_C += 360.0F;
        }

        while (entity.field_70177_z - entity.field_70126_B < -180.0F) {
            entity.field_70126_B -= 360.0F;
        }

        while (entity.field_70177_z - entity.field_70126_B >= 180.0F) {
            entity.field_70126_B += 360.0F;
        }

        entity.field_70125_A = entity.field_70127_C + (entity.field_70125_A - entity.field_70127_C) * f;
        entity.field_70177_z = entity.field_70126_B + (entity.field_70177_z - entity.field_70126_B) * f;
    }
}
