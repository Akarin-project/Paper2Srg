package net.minecraft.dispenser;

public class PositionImpl implements IPosition {

    protected final double x;
    protected final double y;
    protected final double z;

    public PositionImpl(double d0, double d1, double d2) {
        this.x = d0;
        this.y = d1;
        this.z = d2;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }
}
