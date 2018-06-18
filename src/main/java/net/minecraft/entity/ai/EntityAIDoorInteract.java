package net.minecraft.entity.ai;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.BlockPos;


public abstract class EntityAIDoorInteract extends EntityAIBase {

    protected EntityLiving entity;
    protected BlockPos doorPosition;
    protected BlockDoor doorBlock;
    boolean hasStoppedDoorInteraction;
    float entityPositionX;
    float entityPositionZ;

    public EntityAIDoorInteract(EntityLiving entityinsentient) {
        this.doorPosition = BlockPos.ORIGIN;
        this.entity = entityinsentient;
        if (!(entityinsentient.getNavigator() instanceof PathNavigateGround)) {
            throw new IllegalArgumentException("Unsupported mob type for DoorInteractGoal");
        }
    }

    public boolean shouldExecute() {
        if (!this.entity.collidedHorizontally) {
            return false;
        } else {
            PathNavigateGround navigation = (PathNavigateGround) this.entity.getNavigator();
            Path pathentity = navigation.getPath();

            if (pathentity != null && !pathentity.isFinished() && navigation.getEnterDoors()) {
                for (int i = 0; i < Math.min(pathentity.getCurrentPathIndex() + 2, pathentity.getCurrentPathLength()); ++i) {
                    PathPoint pathpoint = pathentity.getPathPointFromIndex(i);

                    this.doorPosition = new BlockPos(pathpoint.x, pathpoint.y + 1, pathpoint.z);
                    if (this.entity.getDistanceSq((double) this.doorPosition.getX(), this.entity.posY, (double) this.doorPosition.getZ()) <= 2.25D) {
                        this.doorBlock = this.getBlockDoor(this.doorPosition);
                        if (this.doorBlock != null) {
                            return true;
                        }
                    }
                }

                this.doorPosition = (new BlockPos(this.entity)).up();
                this.doorBlock = this.getBlockDoor(this.doorPosition);
                return this.doorBlock != null;
            } else {
                return false;
            }
        }
    }

    public boolean shouldContinueExecuting() {
        return !this.hasStoppedDoorInteraction;
    }

    public void startExecuting() {
        this.hasStoppedDoorInteraction = false;
        this.entityPositionX = (float) ((double) ((float) this.doorPosition.getX() + 0.5F) - this.entity.posX);
        this.entityPositionZ = (float) ((double) ((float) this.doorPosition.getZ() + 0.5F) - this.entity.posZ);
    }

    public void updateTask() {
        float f = (float) ((double) ((float) this.doorPosition.getX() + 0.5F) - this.entity.posX);
        float f1 = (float) ((double) ((float) this.doorPosition.getZ() + 0.5F) - this.entity.posZ);
        float f2 = this.entityPositionX * f + this.entityPositionZ * f1;

        if (f2 < 0.0F) {
            this.hasStoppedDoorInteraction = true;
        }

    }

    private BlockDoor getBlockDoor(BlockPos blockposition) {
        IBlockState iblockdata = this.entity.world.getBlockState(blockposition);
        Block block = iblockdata.getBlock();

        return block instanceof BlockDoor && iblockdata.getMaterial() == Material.WOOD ? (BlockDoor) block : null;
    }
}
