package net.minecraft.dispenser;

public class PositionImpl implements IPosition {

    protected final double field_82630_a;
    protected final double field_82628_b;
    protected final double field_82629_c;

    public PositionImpl(double d0, double d1, double d2) {
        this.field_82630_a = d0;
        this.field_82628_b = d1;
        this.field_82629_c = d2;
    }

    public double func_82615_a() {
        return this.field_82630_a;
    }

    public double func_82617_b() {
        return this.field_82628_b;
    }

    public double func_82616_c() {
        return this.field_82629_c;
    }
}
