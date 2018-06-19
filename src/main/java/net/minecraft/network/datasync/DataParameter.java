package net.minecraft.network.datasync;

public class DataParameter<T> {

    private final int field_187157_a;
    private final DataSerializer<T> field_187158_b;

    public DataParameter(int i, DataSerializer<T> datawatcherserializer) {
        this.field_187157_a = i;
        this.field_187158_b = datawatcherserializer;
    }

    public int func_187155_a() {
        return this.field_187157_a;
    }

    public DataSerializer<T> func_187156_b() {
        return this.field_187158_b;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object != null && this.getClass() == object.getClass()) {
            DataParameter datawatcherobject = (DataParameter) object;

            return this.field_187157_a == datawatcherobject.field_187157_a;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return this.field_187157_a;
    }
}
