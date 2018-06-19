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

    private final float min;
    private final float max;

    public RandomValueRange(float f, float f1) {
        this.min = f;
        this.max = f1;
    }

    public RandomValueRange(float f) {
        this.min = f;
        this.max = f;
    }

    public float getMin() {
        return this.min;
    }

    public float getMax() {
        return this.max;
    }

    public int generateInt(Random random) {
        return MathHelper.getInt(random, MathHelper.floor(this.min), MathHelper.floor(this.max));
    }

    public float generateFloat(Random random) {
        return MathHelper.nextFloat(random, this.min, this.max);
    }

    public boolean isInRange(int i) {
        return i <= this.max && i >= this.min;
    }

    public static class a implements JsonDeserializer<RandomValueRange>, JsonSerializer<RandomValueRange> {

        public a() {}

        public RandomValueRange a(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
            if (JsonUtils.isNumber(jsonelement)) {
                return new RandomValueRange(JsonUtils.getFloat(jsonelement, "value"));
            } else {
                JsonObject jsonobject = JsonUtils.getJsonObject(jsonelement, "value");
                float f = JsonUtils.getFloat(jsonobject, "min");
                float f1 = JsonUtils.getFloat(jsonobject, "max");

                return new RandomValueRange(f, f1);
            }
        }

        public JsonElement a(RandomValueRange lootvaluebounds, Type type, JsonSerializationContext jsonserializationcontext) {
            if (lootvaluebounds.min == lootvaluebounds.max) {
                return new JsonPrimitive(Float.valueOf(lootvaluebounds.min));
            } else {
                JsonObject jsonobject = new JsonObject();

                jsonobject.addProperty("min", Float.valueOf(lootvaluebounds.min));
                jsonobject.addProperty("max", Float.valueOf(lootvaluebounds.max));
                return jsonobject;
            }
        }

        @Override
        public JsonElement serialize(RandomValueRange object, Type type, JsonSerializationContext jsonserializationcontext) {
            return this.a(object, type, jsonserializationcontext);
        }

        @Override
        public RandomValueRange deserialize(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
            return this.a(jsonelement, type, jsondeserializationcontext);
        }
    }
}
