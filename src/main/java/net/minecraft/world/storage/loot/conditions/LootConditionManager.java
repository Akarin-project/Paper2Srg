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

    private static final Map<ResourceLocation, LootCondition.a<?>> field_186642_a = Maps.newHashMap();
    private static final Map<Class<? extends LootCondition>, LootCondition.a<?>> field_186643_b = Maps.newHashMap();

    public static <T extends LootCondition> void a(LootCondition.a<? extends T> LootCondition_a) {
        ResourceLocation minecraftkey = LootCondition_a.a();
        Class oclass = LootCondition_a.b();

        if (LootConditionManager.field_186642_a.containsKey(minecraftkey)) {
            throw new IllegalArgumentException("Can\'t re-register item condition name " + minecraftkey);
        } else if (LootConditionManager.field_186643_b.containsKey(oclass)) {
            throw new IllegalArgumentException("Can\'t re-register item condition class " + oclass.getName());
        } else {
            LootConditionManager.field_186642_a.put(minecraftkey, LootCondition_a);
            LootConditionManager.field_186643_b.put(oclass, LootCondition_a);
        }
    }

    public static boolean func_186638_a(@Nullable LootCondition[] aLootCondition, Random random, LootContext loottableinfo) {
        if (aLootCondition == null) {
            return true;
        } else {
            LootCondition[] aLootCondition1 = aLootCondition;
            int i = aLootCondition.length;

            for (int j = 0; j < i; ++j) {
                LootCondition LootCondition = aLootCondition1[j];

                if (!LootCondition.func_186618_a(random, loottableinfo)) {
                    return false;
                }
            }

            return true;
        }
    }

    public static LootCondition.a<?> a(ResourceLocation minecraftkey) {
        LootCondition.a LootCondition_a = LootConditionManager.field_186642_a.get(minecraftkey);

        if (LootCondition_a == null) {
            throw new IllegalArgumentException("Unknown loot item condition \'" + minecraftkey + "\'");
        } else {
            return LootCondition_a;
        }
    }

    public static <T extends LootCondition> LootCondition.a<T> a(T t0) {
        LootCondition.a LootCondition_a = LootConditionManager.field_186643_b.get(t0.getClass());

        if (LootCondition_a == null) {
            throw new IllegalArgumentException("Unknown loot item condition " + t0);
        } else {
            return LootCondition_a;
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

        @Override
        public LootCondition deserialize(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
            JsonObject jsonobject = JsonUtils.func_151210_l(jsonelement, "condition");
            ResourceLocation minecraftkey = new ResourceLocation(JsonUtils.func_151200_h(jsonobject, "condition"));

            LootCondition.a LootCondition_a;

            try {
                LootCondition_a = LootConditionManager.a(minecraftkey);
            } catch (IllegalArgumentException illegalargumentexception) {
                throw new JsonSyntaxException("Unknown condition \'" + minecraftkey + "\'");
            }

            return LootCondition_a.b(jsonobject, jsondeserializationcontext);
        }

        @Override
        public JsonElement serialize(LootCondition LootCondition, Type type, JsonSerializationContext jsonserializationcontext) {
            LootCondition.a LootCondition_a = LootConditionManager.a(LootCondition);
            JsonObject jsonobject = new JsonObject();

            LootCondition_a.a(jsonobject, LootCondition, jsonserializationcontext);
            jsonobject.addProperty("condition", LootCondition_a.a().toString());
            return jsonobject;
        }
    }
}
