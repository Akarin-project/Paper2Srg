package net.minecraft.world.border;

public interface IBorderListener {

    void onSizeChanged(WorldBorder worldborder, double d0);

    void onTransitionStarted(WorldBorder worldborder, double d0, double d1, long i);

    void onCenterChanged(WorldBorder worldborder, double d0, double d1);

    void onWarningTimeChanged(WorldBorder worldborder, int i);

    void onWarningDistanceChanged(WorldBorder worldborder, int i);

    void onDamageAmountChanged(WorldBorder worldborder, double d0);

    void onDamageBufferChanged(WorldBorder worldborder, double d0);
}
