package net.minecraft.entity;

public interface IJumpingMount {

    boolean canJump();

    void handleStartJump(int i);

    void handleStopJump();
}
