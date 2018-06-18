package net.minecraft.entity.ai;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.util.math.MathHelper;


public class EntityAIAttackRanged extends EntityAIBase {

    private final EntityLiving entityHost;
    private final IRangedAttackMob rangedAttackEntityHost;
    private EntityLivingBase attackTarget;
    private int rangedAttackTime;
    private final double entityMoveSpeed;
    private int seeTime;
    private final int attackIntervalMin;
    private final int maxRangedAttackTime;
    private final float attackRadius;
    private final float maxAttackDistance;

    public EntityAIAttackRanged(IRangedAttackMob irangedentity, double d0, int i, float f) {
        this(irangedentity, d0, i, i, f);
    }

    public EntityAIAttackRanged(IRangedAttackMob irangedentity, double d0, int i, int j, float f) {
        this.rangedAttackTime = -1;
        if (!(irangedentity instanceof EntityLivingBase)) {
            throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
        } else {
            this.rangedAttackEntityHost = irangedentity;
            this.entityHost = (EntityLiving) irangedentity;
            this.entityMoveSpeed = d0;
            this.attackIntervalMin = i;
            this.maxRangedAttackTime = j;
            this.attackRadius = f;
            this.maxAttackDistance = f * f;
            this.setMutexBits(3);
        }
    }

    public boolean shouldExecute() {
        EntityLivingBase entityliving = this.entityHost.getAttackTarget();

        if (entityliving == null) {
            return false;
        } else {
            this.attackTarget = entityliving;
            return true;
        }
    }

    public boolean shouldContinueExecuting() {
        return this.shouldExecute() || !this.entityHost.getNavigator().noPath();
    }

    public void resetTask() {
        this.attackTarget = null;
        this.seeTime = 0;
        this.rangedAttackTime = -1;
    }

    public void updateTask() {
        double d0 = this.entityHost.getDistanceSq(this.attackTarget.posX, this.attackTarget.getEntityBoundingBox().minY, this.attackTarget.posZ);
        boolean flag = this.entityHost.getEntitySenses().canSee(this.attackTarget);

        if (flag) {
            ++this.seeTime;
        } else {
            this.seeTime = 0;
        }

        if (d0 <= (double) this.maxAttackDistance && this.seeTime >= 20) {
            this.entityHost.getNavigator().clearPath();
        } else {
            this.entityHost.getNavigator().tryMoveToEntityLiving((Entity) this.attackTarget, this.entityMoveSpeed);
        }

        this.entityHost.getLookHelper().setLookPositionWithEntity(this.attackTarget, 30.0F, 30.0F);
        float f;

        if (--this.rangedAttackTime == 0) {
            if (!flag) {
                return;
            }

            f = MathHelper.sqrt(d0) / this.attackRadius;
            float f1 = MathHelper.clamp(f, 0.1F, 1.0F);

            this.rangedAttackEntityHost.attackEntityWithRangedAttack(this.attackTarget, f1);
            this.rangedAttackTime = MathHelper.floor(f * (float) (this.maxRangedAttackTime - this.attackIntervalMin) + (float) this.attackIntervalMin);
        } else if (this.rangedAttackTime < 0) {
            f = MathHelper.sqrt(d0) / this.attackRadius;
            this.rangedAttackTime = MathHelper.floor(f * (float) (this.maxRangedAttackTime - this.attackIntervalMin) + (float) this.attackIntervalMin);
        }

    }
}
