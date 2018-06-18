package net.minecraft.entity.ai;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;


public class EntityLookHelper {

    private final EntityLiving entity;
    private float deltaLookYaw;
    private float deltaLookPitch;
    private boolean isLooking;
    private double posX;
    private double posY;
    private double posZ;

    public EntityLookHelper(EntityLiving entityinsentient) {
        this.entity = entityinsentient;
    }

    public void setLookPositionWithEntity(Entity entity, float f, float f1) {
        this.posX = entity.posX;
        if (entity instanceof EntityLivingBase) {
            this.posY = entity.posY + (double) entity.getEyeHeight();
        } else {
            this.posY = (entity.getEntityBoundingBox().minY + entity.getEntityBoundingBox().maxY) / 2.0D;
        }

        this.posZ = entity.posZ;
        this.deltaLookYaw = f;
        this.deltaLookPitch = f1;
        this.isLooking = true;
    }

    public void setLookPosition(double d0, double d1, double d2, float f, float f1) {
        this.posX = d0;
        this.posY = d1;
        this.posZ = d2;
        this.deltaLookYaw = f;
        this.deltaLookPitch = f1;
        this.isLooking = true;
    }

    public void onUpdateLook() {
        this.entity.rotationPitch = 0.0F;
        if (this.isLooking) {
            this.isLooking = false;
            double d0 = this.posX - this.entity.posX;
            double d1 = this.posY - (this.entity.posY + (double) this.entity.getEyeHeight());
            double d2 = this.posZ - this.entity.posZ;
            double d3 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);
            float f = (float) (MathHelper.atan2(d2, d0) * 57.2957763671875D) - 90.0F;
            float f1 = (float) (-(MathHelper.atan2(d1, d3) * 57.2957763671875D));

            this.entity.rotationPitch = this.updateRotation(this.entity.rotationPitch, f1, this.deltaLookPitch);
            this.entity.rotationYawHead = this.updateRotation(this.entity.rotationYawHead, f, this.deltaLookYaw);
        } else {
            this.entity.rotationYawHead = this.updateRotation(this.entity.rotationYawHead, this.entity.renderYawOffset, 10.0F);
        }

        float f2 = MathHelper.wrapDegrees(this.entity.rotationYawHead - this.entity.renderYawOffset);

        if (!this.entity.getNavigator().noPath()) {
            if (f2 < -75.0F) {
                this.entity.rotationYawHead = this.entity.renderYawOffset - 75.0F;
            }

            if (f2 > 75.0F) {
                this.entity.rotationYawHead = this.entity.renderYawOffset + 75.0F;
            }
        }

    }

    private float updateRotation(float f, float f1, float f2) {
        float f3 = MathHelper.wrapDegrees(f1 - f);

        if (f3 > f2) {
            f3 = f2;
        }

        if (f3 < -f2) {
            f3 = -f2;
        }

        return f + f3;
    }

    public boolean getIsLooking() {
        return this.isLooking;
    }

    public double getLookPosX() {
        return this.posX;
    }

    public double getLookPosY() {
        return this.posY;
    }

    public double getLookPosZ() {
        return this.posZ;
    }
}
