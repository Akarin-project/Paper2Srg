package net.minecraft.advancements.critereon;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javax.annotation.Nullable;

import net.minecraft.util.JsonUtils;

public class MinMaxBounds {

    public static final MinMaxBounds UNBOUNDED = new MinMaxBounds((Float) null, (Float) null);
    private final Float min;
    private final Float max;

    public MinMaxBounds(@Nullable Float ofloat, @Nullable Float ofloat1) {
        this.min = ofloat;
        this.max = ofloat1;
    }

    public boolean test(float f) {
        return this.min != null && this.min.floatValue() > f ? false : this.max == null || this.max.floatValue() >= f;
    }

    public boolean testSquare(double d0) {
        return this.min != null && (double) (this.min.floatValue() * this.min.floatValue()) > d0 ? false : this.max == null || (double) (this.max.floatValue() * this.max.floatValue()) >= d0;
    }

    public static MinMaxBounds deserialize(@Nullable JsonElement jsonelement) {
        if (jsonelement != null && !jsonelement.isJsonNull()) {
            if (JsonUtils.isNumber(jsonelement)) {
                float f = JsonUtils.getFloat(jsonelement, "value");

                return new MinMaxBounds(Float.valueOf(f), Float.valueOf(f));
            } else {
                JsonObject jsonobject = JsonUtils.getJsonObject(jsonelement, "value");
                Float ofloat = jsonobject.has("min") ? Float.valueOf(JsonUtils.getFloat(jsonobject, "min")) : null;
                Float ofloat1 = jsonobject.has("max") ? Float.valueOf(JsonUtils.getFloat(jsonobject, "max")) : null;

                return new MinMaxBounds(ofloat, ofloat1);
            }
        } else {
            return MinMaxBounds.UNBOUNDED;
        }
    }
}
