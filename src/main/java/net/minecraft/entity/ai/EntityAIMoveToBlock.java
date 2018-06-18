package net.minecraft.entity.ai;
import net.minecraft.entity.EntityCreature;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public abstract class EntityAIMoveToBlock extends EntityAIBase {

    private final EntityCreature creature; public EntityCreature getEntity() { return creature; } // Paper - OBFHELPER
    private final double movementSpeed;
    protected int runDelay;
    private int timeoutCounter;
    private int maxStayTicks;
    protected BlockPos destinationBlock; public BlockPos getTarget() { return destinationBlock; } public void setTarget(BlockPos pos) { this.destinationBlock = pos; getEntity().movingTarget = pos != BlockPos.ORIGIN ? pos : null; } // Paper - OBFHELPER

    // Paper start
    @Override
    public void onTaskReset() {
        super.onTaskReset();
        setTarget(BlockPos.ORIGIN);
    }
    // Paper end

    private boolean isAboveDestination;
    private final int searchLength;

    public EntityAIMoveToBlock(EntityCreature entitycreature, double d0, int i) {
        this.destinationBlock = BlockPos.ORIGIN;
        this.creature = entitycreature;
        this.movementSpeed = d0;
        this.searchLength = i;
        this.setMutexBits(5);
    }

    public boolean shouldExecute() {
        if (this.runDelay > 0) {
            --this.runDelay;
            return false;
        } else {
            this.runDelay = 200 + this.creature.getRNG().nextInt(200);
            return this.searchForDestination();
        }
    }

    public boolean shouldContinueExecuting() {
        return this.timeoutCounter >= -this.maxStayTicks && this.timeoutCounter <= 1200 && this.shouldMoveTo(this.creature.world, this.destinationBlock);
    }

    public void startExecuting() {
        this.creature.getNavigator().tryMoveToXYZ((double) ((float) this.destinationBlock.getX()) + 0.5D, (double) (this.destinationBlock.getY() + 1), (double) ((float) this.destinationBlock.getZ()) + 0.5D, this.movementSpeed);
        this.timeoutCounter = 0;
        this.maxStayTicks = this.creature.getRNG().nextInt(this.creature.getRNG().nextInt(1200) + 1200) + 1200;
    }

    public void updateTask() {
        if (this.creature.getDistanceSqToCenter(this.destinationBlock.up()) > 1.0D) {
            this.isAboveDestination = false;
            ++this.timeoutCounter;
            if (this.timeoutCounter % 40 == 0) {
                this.creature.getNavigator().tryMoveToXYZ((double) ((float) this.destinationBlock.getX()) + 0.5D, (double) (this.destinationBlock.getY() + 1), (double) ((float) this.destinationBlock.getZ()) + 0.5D, this.movementSpeed);
            }
        } else {
            this.isAboveDestination = true;
            --this.timeoutCounter;
        }

    }

    protected boolean getIsAboveDestination() {
        return this.isAboveDestination;
    }

    private boolean searchForDestination() {
        int i = this.searchLength;
        boolean flag = true;
        BlockPos blockposition = new BlockPos(this.creature);

        for (int j = 0; j <= 1; j = j > 0 ? -j : 1 - j) {
            for (int k = 0; k < i; ++k) {
                for (int l = 0; l <= k; l = l > 0 ? -l : 1 - l) {
                    for (int i1 = l < k && l > -k ? k : 0; i1 <= k; i1 = i1 > 0 ? -i1 : 1 - i1) {
                        BlockPos blockposition1 = blockposition.add(l, j - 1, i1);

                        if (this.creature.isWithinHomeDistanceFromPosition(blockposition1) && this.shouldMoveTo(this.creature.world, blockposition1)) {
                            setTarget(blockposition1); // Paper
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    protected abstract boolean shouldMoveTo(World world, BlockPos blockposition);
}
