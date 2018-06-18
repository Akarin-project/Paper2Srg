package net.minecraft.entity;

import java.util.UUID;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.bukkit.event.entity.EntityUnleashEvent;

// CraftBukkit start
import org.bukkit.event.entity.EntityUnleashEvent;
// CraftBukkit end

public abstract class EntityCreature extends EntityLiving {

    public static final UUID FLEEING_SPEED_MODIFIER_UUID = UUID.fromString("E199AD21-BA8A-4C53-8D13-6182D5C69D3A");
    public static final AttributeModifier FLEEING_SPEED_MODIFIER = (new AttributeModifier(EntityCreature.FLEEING_SPEED_MODIFIER_UUID, "Fleeing speed bonus", 2.0D, 2)).setSaved(false);
    public BlockPos movingTarget = null; public BlockPos getMovingTarget() { return movingTarget; } // Paper
    private BlockPos homePosition;
    private float maximumHomeDistance;
    private final float restoreWaterCost;

    public EntityCreature(World world) {
        super(world);
        this.homePosition = BlockPos.ORIGIN;
        this.maximumHomeDistance = -1.0F;
        this.restoreWaterCost = PathNodeType.WATER.getPriority();
    }

    public float getBlockPathWeight(BlockPos blockposition) {
        return 0.0F;
    }

    public boolean getCanSpawnHere() {
        return super.getCanSpawnHere() && this.getBlockPathWeight(new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ)) >= 0.0F;
    }

    public boolean hasPath() {
        return !this.navigator.noPath();
    }

    public boolean isWithinHomeDistanceCurrentPosition() {
        return this.isWithinHomeDistanceFromPosition(new BlockPos(this));
    }

    public boolean isWithinHomeDistanceFromPosition(BlockPos blockposition) {
        return this.maximumHomeDistance == -1.0F ? true : this.homePosition.distanceSq(blockposition) < (double) (this.maximumHomeDistance * this.maximumHomeDistance);
    }

    public void setHomePosAndDistance(BlockPos blockposition, int i) {
        this.homePosition = blockposition;
        this.maximumHomeDistance = (float) i;
    }

    public BlockPos getHomePosition() {
        return this.homePosition;
    }

    public float getMaximumHomeDistance() {
        return this.maximumHomeDistance;
    }

    public void detachHome() {
        this.maximumHomeDistance = -1.0F;
    }

    public boolean hasHome() {
        return this.maximumHomeDistance != -1.0F;
    }

    protected void updateLeashedState() {
        super.updateLeashedState();
        if (this.getLeashed() && this.getLeashHolder() != null && this.getLeashHolder().world == this.world) {
            Entity entity = this.getLeashHolder();

            this.setHomePosAndDistance(new BlockPos((int) entity.posX, (int) entity.posY, (int) entity.posZ), 5);
            float f = this.getDistance(entity);

            if (this instanceof EntityTameable && ((EntityTameable) this).isSitting()) {
                if (f > 10.0F) {
                    this.world.getServer().getPluginManager().callEvent(new EntityUnleashEvent(this.getBukkitEntity(), EntityUnleashEvent.UnleashReason.DISTANCE)); // CraftBukkit
                    this.clearLeashed(true, true);
                }

                return;
            }

            this.onLeashDistance(f);
            if (f > 10.0F) {
                this.world.getServer().getPluginManager().callEvent(new EntityUnleashEvent(this.getBukkitEntity(), EntityUnleashEvent.UnleashReason.DISTANCE)); // CraftBukkit
                this.clearLeashed(true, true);
                this.tasks.disableControlFlag(1);
            } else if (f > 6.0F) {
                double d0 = (entity.posX - this.posX) / (double) f;
                double d1 = (entity.posY - this.posY) / (double) f;
                double d2 = (entity.posZ - this.posZ) / (double) f;

                this.motionX += d0 * Math.abs(d0) * 0.4D;
                this.motionY += d1 * Math.abs(d1) * 0.4D;
                this.motionZ += d2 * Math.abs(d2) * 0.4D;
            } else {
                this.tasks.enableControlFlag(1);
                float f1 = 2.0F;
                Vec3d vec3d = (new Vec3d(entity.posX - this.posX, entity.posY - this.posY, entity.posZ - this.posZ)).normalize().scale((double) Math.max(f - 2.0F, 0.0F));

                this.getNavigator().tryMoveToXYZ(this.posX + vec3d.x, this.posY + vec3d.y, this.posZ + vec3d.z, this.followLeashSpeed());
            }
        }

    }

    protected double followLeashSpeed() {
        return 1.0D;
    }

    protected void onLeashDistance(float f) {}
}
