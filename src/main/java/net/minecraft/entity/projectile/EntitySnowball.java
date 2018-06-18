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

    public static void registerFixesSnowball(DataFixer dataconvertermanager) {
        EntityThrowable.registerFixesThrowable(dataconvertermanager, "Snowball");
    }

    protected void onImpact(RayTraceResult movingobjectposition) {
        if (movingobjectposition.entityHit != null) {
            byte b0 = 0;

            if (movingobjectposition.entityHit instanceof EntityBlaze) {
                b0 = 3;
            }

            movingobjectposition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), (float) b0);
        }

        if (!this.world.isRemote) {
            this.world.setEntityState(this, (byte) 3);
            this.setDead();
        }

    }
}
