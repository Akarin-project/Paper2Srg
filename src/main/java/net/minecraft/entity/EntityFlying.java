package net.minecraft.entity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;


public abstract class EntityFlying extends EntityLiving {

    public EntityFlying(World world) {
        super(world);
    }

    public void fall(float f, float f1) {}

    protected void updateFallState(double d0, boolean flag, IBlockState iblockdata, BlockPos blockposition) {}

    public void travel(float f, float f1, float f2) {
        if (this.isInWater()) {
            this.moveRelative(f, f1, f2, 0.02F);
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.800000011920929D;
            this.motionY *= 0.800000011920929D;
            this.motionZ *= 0.800000011920929D;
        } else if (this.isInLava()) {
            this.moveRelative(f, f1, f2, 0.02F);
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.5D;
            this.motionY *= 0.5D;
            this.motionZ *= 0.5D;
        } else {
            float f3 = 0.91F;

            if (this.onGround) {
                f3 = this.world.getBlockState(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY) - 1, MathHelper.floor(this.posZ))).getBlock().slipperiness * 0.91F;
            }

            float f4 = 0.16277136F / (f3 * f3 * f3);

            this.moveRelative(f, f1, f2, this.onGround ? 0.1F * f4 : 0.02F);
            f3 = 0.91F;
            if (this.onGround) {
                f3 = this.world.getBlockState(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY) - 1, MathHelper.floor(this.posZ))).getBlock().slipperiness * 0.91F;
            }

            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= (double) f3;
            this.motionY *= (double) f3;
            this.motionZ *= (double) f3;
        }

        this.prevLimbSwingAmount = this.limbSwingAmount;
        double d0 = this.posX - this.prevPosX;
        double d1 = this.posZ - this.prevPosZ;
        float f5 = MathHelper.sqrt(d0 * d0 + d1 * d1) * 4.0F;

        if (f5 > 1.0F) {
            f5 = 1.0F;
        }

        this.limbSwingAmount += (f5 - this.limbSwingAmount) * 0.4F;
        this.limbSwing += this.limbSwingAmount;
    }

    public boolean isOnLadder() {
        return false;
    }
}
