package net.minecraft.entity.ai;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class EntityAIAttackMelee extends EntityAIBase {

    World world;
    protected EntityCreature attacker;
    protected int attackTick;
    double speedTowardsTarget;
    boolean longMemory;
    Path path;
    private int delayCounter;
    private double targetX;
    private double targetY;
    private double targetZ;
    protected final int attackInterval = 20;

    public EntityAIAttackMelee(EntityCreature entitycreature, double d0, boolean flag) {
        this.attacker = entitycreature;
        this.world = entitycreature.world;
        this.speedTowardsTarget = d0;
        this.longMemory = flag;
        this.setMutexBits(3);
    }

    public boolean shouldExecute() {
        EntityLivingBase entityliving = this.attacker.getAttackTarget();

        if (entityliving == null) {
            return false;
        } else if (!entityliving.isEntityAlive()) {
            return false;
        } else {
            this.path = this.attacker.getNavigator().getPathToEntityLiving((Entity) entityliving);
            return this.path != null ? true : this.getAttackReachSqr(entityliving) >= this.attacker.getDistanceSq(entityliving.posX, entityliving.getEntityBoundingBox().minY, entityliving.posZ);
        }
    }

    public boolean shouldContinueExecuting() {
        EntityLivingBase entityliving = this.attacker.getAttackTarget();

        return entityliving == null ? false : (!entityliving.isEntityAlive() ? false : (!this.longMemory ? !this.attacker.getNavigator().noPath() : (!this.attacker.isWithinHomeDistanceFromPosition(new BlockPos(entityliving)) ? false : !(entityliving instanceof EntityPlayer) || !((EntityPlayer) entityliving).isSpectator() && !((EntityPlayer) entityliving).isCreative())));
    }

    public void startExecuting() {
        this.attacker.getNavigator().setPath(this.path, this.speedTowardsTarget);
        this.delayCounter = 0;
    }

    public void resetTask() {
        EntityLivingBase entityliving = this.attacker.getAttackTarget();

        if (entityliving instanceof EntityPlayer && (((EntityPlayer) entityliving).isSpectator() || ((EntityPlayer) entityliving).isCreative())) {
            this.attacker.setAttackTarget((EntityLivingBase) null);
        }

        this.attacker.getNavigator().clearPath();
    }

    public void updateTask() {
        EntityLivingBase entityliving = this.attacker.getAttackTarget();

        this.attacker.getLookHelper().setLookPositionWithEntity(entityliving, 30.0F, 30.0F);
        double d0 = this.attacker.getDistanceSq(entityliving.posX, entityliving.getEntityBoundingBox().minY, entityliving.posZ);

        --this.delayCounter;
        if ((this.longMemory || this.attacker.getEntitySenses().canSee(entityliving)) && this.delayCounter <= 0 && (this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D || entityliving.getDistanceSq(this.targetX, this.targetY, this.targetZ) >= 1.0D || this.attacker.getRNG().nextFloat() < 0.05F)) {
            this.targetX = entityliving.posX;
            this.targetY = entityliving.getEntityBoundingBox().minY;
            this.targetZ = entityliving.posZ;
            this.delayCounter = 4 + this.attacker.getRNG().nextInt(7);
            if (d0 > 1024.0D) {
                this.delayCounter += 10;
            } else if (d0 > 256.0D) {
                this.delayCounter += 5;
            }

            if (!this.attacker.getNavigator().tryMoveToEntityLiving((Entity) entityliving, this.speedTowardsTarget)) {
                this.delayCounter += 15;
            }
        }

        this.attackTick = Math.max(this.attackTick - 1, 0);
        this.checkAndPerformAttack(entityliving, d0);
    }

    protected void checkAndPerformAttack(EntityLivingBase entityliving, double d0) {
        double d1 = this.getAttackReachSqr(entityliving);

        if (d0 <= d1 && this.attackTick <= 0) {
            this.attackTick = 20;
            this.attacker.swingArm(EnumHand.MAIN_HAND);
            this.attacker.attackEntityAsMob(entityliving);
        }

    }

    protected double getAttackReachSqr(EntityLivingBase entityliving) {
        return (double) (this.attacker.width * 2.0F * this.attacker.width * 2.0F + entityliving.width);
    }
}
