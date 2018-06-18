package net.minecraft.entity.ai;

public abstract class EntityAIBase {

    private int mutexBits;

    public EntityAIBase() {}

    public abstract boolean shouldExecute();

    public boolean shouldContinueExecuting() {
        return this.shouldExecute();
    }

    public boolean isInterruptible() {
        return true;
    }

    public void startExecuting() {}

    public void resetTask() {
        onTaskReset(); // Paper
    }
    public void onTaskReset() {} // Paper

    public void updateTask() {}

    public void setMutexBits(int i) {
        this.mutexBits = i;
    }

    public int getMutexBits() {
        return this.mutexBits;
    }
}
