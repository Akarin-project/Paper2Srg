package net.minecraft.world.storage.loot.conditions;

import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;

public class LootConditionManager {

    private static final Map<ResourceLocation, LootCondition.a<?>> NAME_TO_SERIALIZER_MAP = Maps.newHashMap();
    private static final Map<Class<? extends LootCondition>, LootCondition.a<?>> CLASS_TO_SERIALIZER_MAP = Maps.newHashMap();

    public static <T extends LootCondition> void a(LootCondition.a<? extends T> lootitemcondition_a) {
        ResourceLocation minecraftkey = lootitemcondition_a.a();
        Class oclass = lootitemcondition_a.b();

        if (LootConditionManager.NAME_TO_SERIALIZER_MAP.containsKey(minecraftkey)) {
            throw new IllegalArgumentException("Can\'t re-register item condition name " + minecraftkey);
        } else if (LootConditionManager.CLASS_TO_SERIALIZER_MAP.containsKey(oclass)) {
            throw new IllegalArgumentException("Can\'t re-register item condition class " + oclass.getName());
        } else {
            LootConditionManager.NAME_TO_SERIALIZER_MAP.put(minecraftkey, lootitemcondition_a);
            LootConditionManager.CLASS_TO_SERIALIZER_MAP.put(oclass, lootitemcondition_a);
        }
    }

    public static boolean testAllConditions(@Nullable LootCondition[] alootitemcondition, Random random, LootContext loottableinfo) {
        if (alootitemcondition == null) {
            return true;
        } else {
            LootCondition[] alootitemcondition1 = alootitemcondition;
            int i = alootitemcondition.length;

            for (int j = 0; j < i; ++j) {
                LootCondition lootitemcondition = alootitemcondition1[j];

                if (!lootitemcondition.testCondition(random, loottableinfo)) {
                    return false;
                }
            }

            return true;
        }
    }

    public static LootCondition.a<?> a(ResourceLocation minecraftkey) {
        LootCondition.a lootitemcondition_a = LootConditionManager.NAME_TO_SERIALIZER_MAP.get(minecraftkey);

        if (lootitemcondition_a == null) {
            throw new IllegalArgumentException("Unknown loot item condition \'" + minecraftkey + "\'");
        } else {
            return lootitemcondition_a;
        }
    }

    public static <T extends LootCondition> LootCondition.a<T> a(T t0) {
        LootCondition.a lootitemcondition_a = LootConditionManager.CLASS_TO_SERIALIZER_MAP.get(t0.getClass());

        if (lootitemcondition_a == null) {
            throw new IllegalArgumentException("Unknown loot item condition " + t0);
        } else {
            return lootitemcondition_a;
        }
    }

    static {
        a(new RandomChance.a());
        a(new RandomChanceWithLooting.a());
        a(new EntityHasProperty.a());
        a(new KilledByPlayer.a());
        a(new EntityHasScore.a());
    }

    public static class a implements JsonDeserializer<LootCondition>, JsonSerializer<LootCondition> {

        public a() {}

        public LootCondition a(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
            JsonObject jsonobject = JsonUtils.getJsonObject(jsonelement, "condition");
            ResourceLocation minecraftkey = new ResourceLocation(JsonUtils.getString(jsonobject, "condition"));

            LootCondition.a lootitemcondition_a;

            try {
                lootitemcondition_a = LootConditionManager.a(minecraftkey);
            } catch (IllegalArgumentException illegalargumentexception) {
                throw new JsonSyntaxException("Unknown condition \'" + minecraftkey + "\'");
            }

            return lootitemcondition_a.b(jsonobject, jsondeserializationcontext);
        }

        public JsonElement a(LootCondition lootitemcondition, Type type, JsonSerializationContext jsonserializationcontext) {
            LootCondition.a lootitemcondition_a = LootConditionManager.a(lootitemcondition);
            JsonObject jsonobject = new JsonObject();

            lootitemcondition_a.a(jsonobject, lootitemcondition, jsonserializationcontext);
            jsonobject.addProperty("condition", lootitemcondition_a.a().toString());
            return jsonobject;
        }

        @Override
        public JsonElement serialize(LootCondition object, Type type, JsonSerializationContext jsonserializationcontext) {
            return this.a(object, type, jsonserializationcontext);
        }

        @Override
        public LootCondition deserialize(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
            return this.a(jsonelement, type, jsondeserializationcontext);
        }
    }
}
