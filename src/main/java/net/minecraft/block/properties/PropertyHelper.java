package net.minecraft.block.properties;

import com.google.common.base.MoreObjects;

public abstract class PropertyHelper<T extends Comparable<T>> implements IProperty<T> {

    private final Class<T> field_177704_a;
    private final String field_177703_b;

    protected PropertyHelper(String s, Class<T> oclass) {
        this.field_177704_a = oclass;
        this.field_177703_b = s;
    }

    public String func_177701_a() {
        return this.field_177703_b;
    }

    public Class<T> func_177699_b() {
        return this.field_177704_a;
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("name", this.field_177703_b).add("clazz", this.field_177704_a).add("values", this.func_177700_c()).toString();
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof PropertyHelper)) {
            return false;
        } else {
            PropertyHelper blockstate = (PropertyHelper) object;

            return this.field_177704_a.equals(blockstate.field_177704_a) && this.field_177703_b.equals(blockstate.field_177703_b);
        }
    }

    // Spigot start
    private int hashCode;
    public int hashCode() {
        int hash = hashCode;
        if (hash == 0) {
            hash = 31 * this.field_177704_a.hashCode() + this.field_177703_b.hashCode();
            hashCode = hash;
        }
        return hash;
    }
    // Spigot end
}
