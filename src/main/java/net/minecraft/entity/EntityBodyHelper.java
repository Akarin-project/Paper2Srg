package net.minecraft.entity;
import net.minecraft.util.math.MathHelper;


public class EntityBodyHelper {

    private final EntityLivingBase living;
    private int rotationTickCounter;
    private float prevRenderYawHead;

    public EntityBodyHelper(EntityLivingBase entityliving) {
        this.living = entityliving;
    }

    public void updateRenderAngles() {
        double d0 = this.living.posX - this.living.prevPosX;
        double d1 = this.living.posZ - this.living.prevPosZ;

        if (d0 * d0 + d1 * d1 > 2.500000277905201E-7D) {
            this.living.renderYawOffset = this.living.rotationYaw;
            this.living.rotationYawHead = this.computeAngleWithBound(this.living.renderYawOffset, this.living.rotationYawHead, 75.0F);
            this.prevRenderYawHead = this.living.rotationYawHead;
            this.rotationTickCounter = 0;
        } else {
            if (this.living.getPassengers().isEmpty() || !(this.living.getPassengers().get(0) instanceof EntityLiving)) {
                float f = 75.0F;

                if (Math.abs(this.living.rotationYawHead - this.prevRenderYawHead) > 15.0F) {
                    this.rotationTickCounter = 0;
                    this.prevRenderYawHead = this.living.rotationYawHead;
                } else {
                    ++this.rotationTickCounter;
                    boolean flag = true;

                    if (this.rotationTickCounter > 10) {
                        f = Math.max(1.0F - (float) (this.rotationTickCounter - 10) / 10.0F, 0.0F) * 75.0F;
                    }
                }

                this.living.renderYawOffset = this.computeAngleWithBound(this.living.rotationYawHead, this.living.renderYawOffset, f);
            }

        }
    }

    private float computeAngleWithBound(float f, float f1, float f2) {
        float f3 = MathHelper.wrapDegrees(f - f1);

        if (f3 < -f2) {
            f3 = -f2;
        }

        if (f3 >= f2) {
            f3 = f2;
        }

        return f - f3;
    }
}
