package net.minecraft.entity.ai;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.math.MathHelper;


public class EntityFlyHelper extends EntityMoveHelper {

    public EntityFlyHelper(EntityLiving entityinsentient) {
        super(entityinsentient);
    }

    public void onUpdateMoveHelper() {
        if (this.action == EntityMoveHelper.Action.MOVE_TO) {
            this.action = EntityMoveHelper.Action.WAIT;
            this.entity.setNoGravity(true);
            double d0 = this.posX - this.entity.posX;
            double d1 = this.posY - this.entity.posY;
            double d2 = this.posZ - this.entity.posZ;
            double d3 = d0 * d0 + d1 * d1 + d2 * d2;

            if (d3 < 2.500000277905201E-7D) {
                this.entity.setMoveVertical(0.0F);
                this.entity.setMoveForward(0.0F);
                return;
            }

            float f = (float) (MathHelper.atan2(d2, d0) * 57.2957763671875D) - 90.0F;

            this.entity.rotationYaw = this.limitAngle(this.entity.rotationYaw, f, 10.0F);
            float f1;

            if (this.entity.onGround) {
                f1 = (float) (this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
            } else {
                f1 = (float) (this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.FLYING_SPEED).getAttributeValue());
            }

            this.entity.setAIMoveSpeed(f1);
            double d4 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);
            float f2 = (float) (-(MathHelper.atan2(d1, d4) * 57.2957763671875D));

            this.entity.rotationPitch = this.limitAngle(this.entity.rotationPitch, f2, 10.0F);
            this.entity.setMoveVertical(d1 > 0.0D ? f1 : -f1);
        } else {
            this.entity.setNoGravity(false);
            this.entity.setMoveVertical(0.0F);
            this.entity.setMoveForward(0.0F);
        }

    }
}
