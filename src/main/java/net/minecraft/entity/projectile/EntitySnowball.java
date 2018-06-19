package net.minecraft.entity.projectile;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.util.DamageSource;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;


public class EntitySnowball extends EntityThrowable {

    public EntitySnowball(World world) {
        super(world);
    }

    public EntitySnowball(World world, EntityLivingBase entityliving) {
        super(world, entityliving);
    }

    public EntitySnowball(World world, double d0, double d1, double d2) {
        super(world, d0, d1, d2);
    }

    public static void func_189662_a(DataFixer dataconvertermanager) {
        EntityThrowable.func_189661_a(dataconvertermanager, "Snowball");
    }

    protected void func_70184_a(RayTraceResult movingobjectposition) {
        if (movingobjectposition.field_72308_g != null) {
            byte b0 = 0;

            if (movingobjectposition.field_72308_g instanceof EntityBlaze) {
                b0 = 3;
            }

            movingobjectposition.field_72308_g.func_70097_a(DamageSource.func_76356_a(this, this.func_85052_h()), (float) b0);
        }

        if (!this.field_70170_p.field_72995_K) {
            this.field_70170_p.func_72960_a(this, (byte) 3);
            this.func_70106_y();
        }

    }
}
