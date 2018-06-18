package net.minecraft.entity.ai;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.MathHelper;


public class EntityMoveHelper {

    protected final EntityLiving entity;
    protected double posX;
    protected double posY;
    protected double posZ;
    protected double speed;
    protected float moveForward;
    protected float moveStrafe;
    public EntityMoveHelper.Action action;

    public EntityMoveHelper(EntityLiving entityinsentient) {
        this.action = EntityMoveHelper.Action.WAIT;
        this.entity = entityinsentient;
    }

    public boolean isUpdating() {
        return this.action == EntityMoveHelper.Action.MOVE_TO;
    }

    public double getSpeed() {
        return this.speed;
    }

    public void setMoveTo(double d0, double d1, double d2, double d3) {
        this.posX = d0;
        this.posY = d1;
        this.posZ = d2;
        this.speed = d3;
        this.action = EntityMoveHelper.Action.MOVE_TO;
    }

    public void strafe(float f, float f1) {
        this.action = EntityMoveHelper.Action.STRAFE;
        this.moveForward = f;
        this.moveStrafe = f1;
        this.speed = 0.25D;
    }

    public void read(EntityMoveHelper controllermove) {
        this.action = controllermove.action;
        this.posX = controllermove.posX;
        this.posY = controllermove.posY;
        this.posZ = controllermove.posZ;
        this.speed = Math.max(controllermove.speed, 1.0D);
        this.moveForward = controllermove.moveForward;
        this.moveStrafe = controllermove.moveStrafe;
    }

    public void onUpdateMoveHelper() {
        float f;

        if (this.action == EntityMoveHelper.Action.STRAFE) {
            float f1 = (float) this.entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue();
            float f2 = (float) this.speed * f1;
            float f3 = this.moveForward;
            float f4 = this.moveStrafe;
            float f5 = MathHelper.sqrt(f3 * f3 + f4 * f4);

            if (f5 < 1.0F) {
                f5 = 1.0F;
            }

            f5 = f2 / f5;
            f3 *= f5;
            f4 *= f5;
            float f6 = MathHelper.sin(this.entity.rotationYaw * 0.017453292F);
            float f7 = MathHelper.cos(this.entity.rotationYaw * 0.017453292F);
            float f8 = f3 * f7 - f4 * f6;

            f = f4 * f7 + f3 * f6;
            PathNavigate navigationabstract = this.entity.getNavigator();

            if (navigationabstract != null) {
                NodeProcessor pathfinderabstract = navigationabstract.getNodeProcessor();

                if (pathfinderabstract != null && pathfinderabstract.getPathNodeType(this.entity.world, MathHelper.floor(this.entity.posX + (double) f8), MathHelper.floor(this.entity.posY), MathHelper.floor(this.entity.posZ + (double) f)) != PathNodeType.WALKABLE) {
                    this.moveForward = 1.0F;
                    this.moveStrafe = 0.0F;
                    f2 = f1;
                }
            }

            this.entity.setAIMoveSpeed(f2);
            this.entity.setMoveForward(this.moveForward);
            this.entity.setMoveStrafing(this.moveStrafe);
            this.action = EntityMoveHelper.Action.WAIT;
        } else if (this.action == EntityMoveHelper.Action.MOVE_TO) {
            this.action = EntityMoveHelper.Action.WAIT;
            double d0 = this.posX - this.entity.posX;
            double d1 = this.posZ - this.entity.posZ;
            double d2 = this.posY - this.entity.posY;
            double d3 = d0 * d0 + d2 * d2 + d1 * d1;

            if (d3 < 2.500000277905201E-7D) {
                this.entity.setMoveForward(0.0F);
                return;
            }

            f = (float) (MathHelper.atan2(d1, d0) * 57.2957763671875D) - 90.0F;
            this.entity.rotationYaw = this.limitAngle(this.entity.rotationYaw, f, 90.0F);
            this.entity.setAIMoveSpeed((float) (this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue()));
            if (d2 > (double) this.entity.stepHeight && d0 * d0 + d1 * d1 < (double) Math.max(1.0F, this.entity.width)) {
                this.entity.getJumpHelper().setJumping();
                this.action = EntityMoveHelper.Action.JUMPING;
            }
        } else if (this.action == EntityMoveHelper.Action.JUMPING) {
            this.entity.setAIMoveSpeed((float) (this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue()));
            if (this.entity.onGround) {
                this.action = EntityMoveHelper.Action.WAIT;
            }
        } else {
            this.entity.setMoveForward(0.0F);
        }

    }

    protected float limitAngle(float f, float f1, float f2) {
        float f3 = MathHelper.wrapDegrees(f1 - f);

        if (f3 > f2) {
            f3 = f2;
        }

        if (f3 < -f2) {
            f3 = -f2;
        }

        float f4 = f + f3;

        if (f4 < 0.0F) {
            f4 += 360.0F;
        } else if (f4 > 360.0F) {
            f4 -= 360.0F;
        }

        return f4;
    }

    public double getX() {
        return this.posX;
    }

    public double getY() {
        return this.posY;
    }

    public double getZ() {
        return this.posZ;
    }

    public static enum Action {

        WAIT, MOVE_TO, STRAFE, JUMPING;

        private Action() {}
    }
}
