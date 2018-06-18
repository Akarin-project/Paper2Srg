package net.minecraft.util;

public class TupleIntJsonSerializable {

    private int integerValue;
    private IJsonSerializable jsonSerializableValue;

    public TupleIntJsonSerializable() {}

    public int getIntegerValue() {
        return this.integerValue;
    }

    public void setIntegerValue(int i) {
        this.integerValue = i;
    }

    public <T extends IJsonSerializable> T getJsonSerializableValue() {
        return this.jsonSerializableValue;
    }

    public void setJsonSerializableValue(IJsonSerializable ijsonstatistic) {
        this.jsonSerializableValue = ijsonstatistic;
    }
}
