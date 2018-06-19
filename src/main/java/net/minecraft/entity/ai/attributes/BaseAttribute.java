package net.minecraft.entity.ai.attributes;

import javax.annotation.Nullable;

public abstract class BaseAttribute implements IAttribute {

    private final IAttribute field_180373_a;
    private final String field_111115_a;
    private final double field_111113_b;
    private boolean field_111114_c;

    protected BaseAttribute(@Nullable IAttribute iattribute, String s, double d0) {
        this.field_180373_a = iattribute;
        this.field_111115_a = s;
        this.field_111113_b = d0;
        if (s == null) {
            throw new IllegalArgumentException("Name cannot be null!");
        }
    }

    public String func_111108_a() {
        return this.field_111115_a;
    }

    public double func_111110_b() {
        return this.field_111113_b;
    }

    public boolean func_111111_c() {
        return this.field_111114_c;
    }

    public BaseAttribute func_111112_a(boolean flag) {
        this.field_111114_c = flag;
        return this;
    }

    @Nullable
    public IAttribute func_180372_d() {
        return this.field_180373_a;
    }

    public int hashCode() {
        return this.field_111115_a.hashCode();
    }

    public boolean equals(Object object) {
        return object instanceof IAttribute && this.field_111115_a.equals(((IAttribute) object).func_111108_a());
    }
}
