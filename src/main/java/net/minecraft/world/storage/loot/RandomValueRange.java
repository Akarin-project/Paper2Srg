package net.minecraft.world.storage.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.Random;

import net.minecraft.util.JsonUtils;
import net.minecraft.util.math.MathHelper;

public class RandomValueRange {

    private final float field_186514_a;
    private final float field_186515_b;

    public RandomValueRange(float f, float f1) {
        this.field_186514_a = f;
        this.field_186515_b = f1;
    }

    public RandomValueRange(float f) {
        this.field_186514_a = f;
        this.field_186515_b = f;
    }

    public float func_186509_a() {
        return this.field_186514_a;
    }

    public float func_186512_b() {
        return this.field_186515_b;
    }

    public int func_186511_a(Random random) {
        return MathHelper.func_76136_a(random, MathHelper.func_76141_d(this.field_186514_a), MathHelper.func_76141_d(this.field_186515_b));
    }

    public float func_186507_b(Random random) {
        return MathHelper.func_151240_a(random, this.field_186514_a, this.field_186515_b);
    }

    public boolean func_186510_a(int i) {
        return i <= this.field_186515_b && i >= this.field_186514_a;
    }

    public static class a implements JsonDeserializer<RandomValueRange>, JsonSerializer<RandomValueRange> {

        public a() {}

        @Override
        public RandomValueRange deserialize(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
            if (JsonUtils.func_188175_b(jsonelement)) {
                return new RandomValueRange(JsonUtils.func_151220_d(jsonelement, "value"));
            } else {
                JsonObject jsonobject = JsonUtils.func_151210_l(jsonelement, "value");
                float f = JsonUtils.func_151217_k(jsonobject, "min");
                float f1 = JsonUtils.func_151217_k(jsonobject, "max");

                return new RandomValueRange(f, f1);
            }
        }

        @Override
        public JsonElement serialize(RandomValueRange lootvaluebounds, Type type, JsonSerializationContext jsonserializationcontext) {
            if (lootvaluebounds.field_186514_a == lootvaluebounds.field_186515_b) {
                return new JsonPrimitive(Float.valueOf(lootvaluebounds.field_186514_a));
            } else {
                JsonObject jsonobject = new JsonObject();

                jsonobject.addProperty("min", Float.valueOf(lootvaluebounds.field_186514_a));
                jsonobject.addProperty("max", Float.valueOf(lootvaluebounds.field_186515_b));
                return jsonobject;
            }
        }
    }
}
