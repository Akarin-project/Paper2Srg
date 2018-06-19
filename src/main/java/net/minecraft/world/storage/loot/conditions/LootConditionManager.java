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

import net.minecraft.server.LootItemConditions.a;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;

public class LootConditionManager {

    private static final Map<ResourceLocation, LootItemCondition.a<?>> field_186642_a = Maps.newHashMap();
    private static final Map<Class<? extends LootCondition>, LootItemCondition.a<?>> field_186643_b = Maps.newHashMap();

    public static <T extends LootCondition> void a(LootItemCondition.a<? extends T> lootitemcondition_a) {
        ResourceLocation minecraftkey = lootitemcondition_a.a();
        Class oclass = lootitemcondition_a.b();

        if (LootConditionManager.field_186642_a.containsKey(minecraftkey)) {
            throw new IllegalArgumentException("Can\'t re-register item condition name " + minecraftkey);
        } else if (LootConditionManager.field_186643_b.containsKey(oclass)) {
            throw new IllegalArgumentException("Can\'t re-register item condition class " + oclass.getName());
        } else {
            LootConditionManager.field_186642_a.put(minecraftkey, lootitemcondition_a);
            LootConditionManager.field_186643_b.put(oclass, lootitemcondition_a);
        }
    }

    public static boolean func_186638_a(@Nullable LootCondition[] alootitemcondition, Random random, LootContext loottableinfo) {
        if (alootitemcondition == null) {
            return true;
        } else {
            LootCondition[] alootitemcondition1 = alootitemcondition;
            int i = alootitemcondition.length;

            for (int j = 0; j < i; ++j) {
                LootCondition lootitemcondition = alootitemcondition1[j];

                if (!lootitemcondition.func_186618_a(random, loottableinfo)) {
                    return false;
                }
            }

            return true;
        }
    }

    public static LootItemCondition.a<?> a(ResourceLocation minecraftkey) {
        LootItemCondition.a lootitemcondition_a = (LootItemCondition.a) LootConditionManager.field_186642_a.get(minecraftkey);

        if (lootitemcondition_a == null) {
            throw new IllegalArgumentException("Unknown loot item condition \'" + minecraftkey + "\'");
        } else {
            return lootitemcondition_a;
        }
    }

    public static <T extends LootCondition> LootItemCondition.a<T> a(T t0) {
        LootItemCondition.a lootitemcondition_a = (LootItemCondition.a) LootConditionManager.field_186643_b.get(t0.getClass());

        if (lootitemcondition_a == null) {
            throw new IllegalArgumentException("Unknown loot item condition " + t0);
        } else {
            return lootitemcondition_a;
        }
    }

    static {
        a((LootItemCondition.a) (new LootItemConditionRandomChance.a()));
        a((LootItemCondition.a) (new LootItemConditionRandomChanceWithLooting.a()));
        a((LootItemCondition.a) (new LootItemConditionEntityProperty.a()));
        a((LootItemCondition.a) (new LootItemConditionKilledByPlayer.a()));
        a((LootItemCondition.a) (new LootItemConditionEntityScore.a()));
    }

    public static class a implements JsonDeserializer<LootCondition>, JsonSerializer<LootCondition> {

        public a() {}

        public LootCondition a(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
            JsonObject jsonobject = JsonUtils.func_151210_l(jsonelement, "condition");
            ResourceLocation minecraftkey = new ResourceLocation(JsonUtils.func_151200_h(jsonobject, "condition"));

            LootItemCondition.a lootitemcondition_a;

            try {
                lootitemcondition_a = LootConditionManager.a(minecraftkey);
            } catch (IllegalArgumentException illegalargumentexception) {
                throw new JsonSyntaxException("Unknown condition \'" + minecraftkey + "\'");
            }

            return lootitemcondition_a.b(jsonobject, jsondeserializationcontext);
        }

        public JsonElement a(LootCondition lootitemcondition, Type type, JsonSerializationContext jsonserializationcontext) {
            LootItemCondition.a lootitemcondition_a = LootConditionManager.a(lootitemcondition);
            JsonObject jsonobject = new JsonObject();

            lootitemcondition_a.a(jsonobject, lootitemcondition, jsonserializationcontext);
            jsonobject.addProperty("condition", lootitemcondition_a.a().toString());
            return jsonobject;
        }

        public JsonElement serialize(Object object, Type type, JsonSerializationContext jsonserializationcontext) {
            return this.a((LootCondition) object, type, jsonserializationcontext);
        }

        public Object deserialize(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
            return this.a(jsonelement, type, jsondeserializationcontext);
        }
    }
}
