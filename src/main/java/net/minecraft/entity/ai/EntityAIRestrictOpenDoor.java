package net.minecraft.entity.ai;
import net.minecraft.entity.EntityCreature;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.Village;
import net.minecraft.village.VillageDoorInfo;


public class EntityAIRestrictOpenDoor extends EntityAIBase {

    private final EntityCreature entity;
    private VillageDoorInfo frontDoor;

    public EntityAIRestrictOpenDoor(EntityCreature entitycreature) {
        this.entity = entitycreature;
        if (!(entitycreature.getNavigator() instanceof PathNavigateGround)) {
            throw new IllegalArgumentException("Unsupported mob type for RestrictOpenDoorGoal");
        }
    }

    public boolean shouldExecute() {
        if (this.entity.world.isDaytime()) {
            return false;
        } else {
            BlockPos blockposition = new BlockPos(this.entity);
            Village village = this.entity.world.getVillageCollection().getNearestVillage(blockposition, 16);

            if (village == null) {
                return false;
            } else {
                this.frontDoor = village.getNearestDoor(blockposition);
                return this.frontDoor == null ? false : (double) this.frontDoor.getDistanceToInsideBlockSq(blockposition) < 2.25D;
            }
        }
    }

    public boolean shouldContinueExecuting() {
        return this.entity.world.isDaytime() ? false : !this.frontDoor.getIsDetachedFromVillageFlag() && this.frontDoor.isInsideSide(new BlockPos(this.entity));
    }

    public void startExecuting() {
        ((PathNavigateGround) this.entity.getNavigator()).setBreakDoors(false);
        ((PathNavigateGround) this.entity.getNavigator()).setEnterDoors(false);
    }

    public void resetTask() {
        ((PathNavigateGround) this.entity.getNavigator()).setBreakDoors(true);
        ((PathNavigateGround) this.entity.getNavigator()).setEnterDoors(true);
        this.frontDoor = null;
    }

    public void updateTask() {
        this.frontDoor.incrementDoorOpeningRestrictionCounter();
    }
}
