package net.minecraft.entity.ai;

import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.entity.EntityCreature;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityAIFleeSun extends EntityAIBase {

    private final EntityCreature creature;
    private double shelterX;
    private double shelterY;
    private double shelterZ;
    private final double movementSpeed;
    private final World world;

    public EntityAIFleeSun(EntityCreature entitycreature, double d0) {
        this.creature = entitycreature;
        this.movementSpeed = d0;
        this.world = entitycreature.world;
        this.setMutexBits(1);
    }

    public boolean shouldExecute() {
        if (!this.world.isDaytime()) {
            return false;
        } else if (!this.creature.isBurning()) {
            return false;
        } else if (!this.world.canSeeSky(new BlockPos(this.creature.posX, this.creature.getEntityBoundingBox().minY, this.creature.posZ))) {
            return false;
        } else if (!this.creature.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty()) {
            return false;
        } else {
            Vec3d vec3d = this.findPossibleShelter();

            if (vec3d == null) {
                return false;
            } else {
                this.shelterX = vec3d.x;
                this.shelterY = vec3d.y;
                this.shelterZ = vec3d.z;
                return true;
            }
        }
    }

    public boolean shouldContinueExecuting() {
        return !this.creature.getNavigator().noPath();
    }

    public void startExecuting() {
        this.creature.getNavigator().tryMoveToXYZ(this.shelterX, this.shelterY, this.shelterZ, this.movementSpeed);
    }

    @Nullable
    private Vec3d findPossibleShelter() {
        Random random = this.creature.getRNG();
        BlockPos blockposition = new BlockPos(this.creature.posX, this.creature.getEntityBoundingBox().minY, this.creature.posZ);

        for (int i = 0; i < 10; ++i) {
            BlockPos blockposition1 = blockposition.add(random.nextInt(20) - 10, random.nextInt(6) - 3, random.nextInt(20) - 10);

            if (!this.world.canSeeSky(blockposition1) && this.creature.getBlockPathWeight(blockposition1) < 0.0F) {
                return new Vec3d((double) blockposition1.getX(), (double) blockposition1.getY(), (double) blockposition1.getZ());
            }
        }

        return null;
    }
}
