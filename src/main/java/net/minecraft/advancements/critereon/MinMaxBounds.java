package net.minecraft.advancements.critereon;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javax.annotation.Nullable;

import net.minecraft.util.JsonUtils;

public class MinMaxBounds {

    public static final MinMaxBounds field_192516_a = new MinMaxBounds((Float) null, (Float) null);
    private final Float field_192517_b;
    private final Float field_192518_c;

    public MinMaxBounds(@Nullable Float ofloat, @Nullable Float ofloat1) {
        this.field_192517_b = ofloat;
        this.field_192518_c = ofloat1;
    }

    public boolean func_192514_a(float f) {
        return this.field_192517_b != null && this.field_192517_b.floatValue() > f ? false : this.field_192518_c == null || this.field_192518_c.floatValue() >= f;
    }

    public boolean func_192513_a(double d0) {
        return this.field_192517_b != null && (double) (this.field_192517_b.floatValue() * this.field_192517_b.floatValue()) > d0 ? false : this.field_192518_c == null || (double) (this.field_192518_c.floatValue() * this.field_192518_c.floatValue()) >= d0;
    }

    public static MinMaxBounds func_192515_a(@Nullable JsonElement jsonelement) {
        if (jsonelement != null && !jsonelement.isJsonNull()) {
            if (JsonUtils.func_188175_b(jsonelement)) {
                float f = JsonUtils.func_151220_d(jsonelement, "value");

                return new MinMaxBounds(Float.valueOf(f), Float.valueOf(f));
            } else {
                JsonObject jsonobject = JsonUtils.func_151210_l(jsonelement, "value");
                Float ofloat = jsonobject.has("min") ? Float.valueOf(JsonUtils.func_151217_k(jsonobject, "min")) : null;
                Float ofloat1 = jsonobject.has("max") ? Float.valueOf(JsonUtils.func_151217_k(jsonobject, "max")) : null;

                return new MinMaxBounds(ofloat, ofloat1);
            }
        } else {
            return MinMaxBounds.field_192516_a;
        }
    }
}
