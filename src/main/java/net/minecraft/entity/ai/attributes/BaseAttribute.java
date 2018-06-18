package net.minecraft.entity.ai.attributes;

import javax.annotation.Nullable;

public abstract class BaseAttribute implements IAttribute {

    private final IAttribute parent;
    private final String unlocalizedName;
    private final double defaultValue;
    private boolean shouldWatch;

    protected BaseAttribute(@Nullable IAttribute iattribute, String s, double d0) {
        this.parent = iattribute;
        this.unlocalizedName = s;
        this.defaultValue = d0;
        if (s == null) {
            throw new IllegalArgumentException("Name cannot be null!");
        }
    }

    public String getName() {
        return this.unlocalizedName;
    }

    public double getDefaultValue() {
        return this.defaultValue;
    }

    public boolean getShouldWatch() {
        return this.shouldWatch;
    }

    public BaseAttribute setShouldWatch(boolean flag) {
        this.shouldWatch = flag;
        return this;
    }

    @Nullable
    public IAttribute getParent() {
        return this.parent;
    }

    public int hashCode() {
        return this.unlocalizedName.hashCode();
    }

    public boolean equals(Object object) {
        return object instanceof IAttribute && this.unlocalizedName.equals(((IAttribute) object).getName());
    }
}
