package net.minecraft.entity.ai;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityShoulderRiding;
import net.minecraft.entity.player.EntityPlayer;


public class EntityAILandOnOwnersShoulder extends EntityAIBase {

    private final EntityShoulderRiding entity;
    private EntityPlayer owner;
    private boolean isSittingOnShoulder;

    public EntityAILandOnOwnersShoulder(EntityShoulderRiding entityperchable) {
        this.entity = entityperchable;
    }

    public boolean shouldExecute() {
        EntityLivingBase entityliving = this.entity.getOwner();
        boolean flag = entityliving != null && !((EntityPlayer) entityliving).isSpectator() && !((EntityPlayer) entityliving).capabilities.isFlying && !entityliving.isInWater();

        return !this.entity.isSitting() && flag && this.entity.canSitOnShoulder();
    }

    public boolean isInterruptible() {
        return !this.isSittingOnShoulder;
    }

    public void startExecuting() {
        this.owner = (EntityPlayer) this.entity.getOwner();
        this.isSittingOnShoulder = false;
    }

    public void updateTask() {
        if (!this.isSittingOnShoulder && !this.entity.isSitting() && !this.entity.getLeashed()) {
            if (this.entity.getEntityBoundingBox().intersects(this.owner.getEntityBoundingBox())) {
                this.isSittingOnShoulder = this.entity.setEntityOnShoulder(this.owner);
            }

        }
    }
}
