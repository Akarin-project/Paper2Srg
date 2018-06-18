package net.minecraft.network.datasync;

public class DataParameter<T> {

    private final int id;
    private final DataSerializer<T> serializer;

    public DataParameter(int i, DataSerializer<T> datawatcherserializer) {
        this.id = i;
        this.serializer = datawatcherserializer;
    }

    public int getId() {
        return this.id;
    }

    public DataSerializer<T> getSerializer() {
        return this.serializer;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object != null && this.getClass() == object.getClass()) {
            DataParameter datawatcherobject = (DataParameter) object;

            return this.id == datawatcherobject.id;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return this.id;
    }
}
