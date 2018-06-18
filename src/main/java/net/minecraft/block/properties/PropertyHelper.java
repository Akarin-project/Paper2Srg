package net.minecraft.block.properties;

import com.google.common.base.MoreObjects;

public abstract class PropertyHelper<T extends Comparable<T>> implements IProperty<T> {

    private final Class<T> valueClass;
    private final String name;

    protected PropertyHelper(String s, Class<T> oclass) {
        this.valueClass = oclass;
        this.name = s;
    }

    public String getName() {
        return this.name;
    }

    public Class<T> getValueClass() {
        return this.valueClass;
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("name", this.name).add("clazz", this.valueClass).add("values", this.getAllowedValues()).toString();
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof PropertyHelper)) {
            return false;
        } else {
            PropertyHelper blockstate = (PropertyHelper) object;

            return this.valueClass.equals(blockstate.valueClass) && this.name.equals(blockstate.name);
        }
    }

    // Spigot start
    private int hashCode;
    public int hashCode() {
        int hash = hashCode;
        if (hash == 0) {
            hash = 31 * this.valueClass.hashCode() + this.name.hashCode();
            hashCode = hash;
        }
        return hash;
    }
    // Spigot end
}
