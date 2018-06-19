package net.minecraft.world.storage.loot.functions;

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


import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public class LootFunctionManager {

    private static final Map<ResourceLocation, LootFunction.a<?>> NAME_TO_SERIALIZER_MAP = Maps.newHashMap();
    private static final Map<Class<? extends LootFunction>, LootFunction.a<?>> CLASS_TO_SERIALIZER_MAP = Maps.newHashMap();

    public static <T extends LootFunction> void a(LootFunction.a<? extends T> lootitemfunction_a) {
        ResourceLocation minecraftkey = lootitemfunction_a.a();
        Class oclass = lootitemfunction_a.b();

        if (LootFunctionManager.NAME_TO_SERIALIZER_MAP.containsKey(minecraftkey)) {
            throw new IllegalArgumentException("Can\'t re-register item function name " + minecraftkey);
        } else if (LootFunctionManager.CLASS_TO_SERIALIZER_MAP.containsKey(oclass)) {
            throw new IllegalArgumentException("Can\'t re-register item function class " + oclass.getName());
        } else {
            LootFunctionManager.NAME_TO_SERIALIZER_MAP.put(minecraftkey, lootitemfunction_a);
            LootFunctionManager.CLASS_TO_SERIALIZER_MAP.put(oclass, lootitemfunction_a);
        }
    }

    public static LootFunction.a<?> a(ResourceLocation minecraftkey) {
        LootFunction.a lootitemfunction_a = LootFunctionManager.NAME_TO_SERIALIZER_MAP.get(minecraftkey);

        if (lootitemfunction_a == null) {
            throw new IllegalArgumentException("Unknown loot item function \'" + minecraftkey + "\'");
        } else {
            return lootitemfunction_a;
        }
    }

    public static <T extends LootFunction> LootFunction.a<T> a(T t0) {
        LootFunction.a lootitemfunction_a = LootFunctionManager.CLASS_TO_SERIALIZER_MAP.get(t0.getClass());

        if (lootitemfunction_a == null) {
            throw new IllegalArgumentException("Unknown loot item function " + t0);
        } else {
            return lootitemfunction_a;
        }
    }

    static {
        a(new SetCount.a());
        a(new SetMetadata.a());
        a(new EnchantWithLevels.a());
        a(new EnchantRandomly.a());
        a(new SetNBT.a());
        a(new Smelt.a());
        a(new LootingEnchantBonus.a());
        a(new SetDamage.a());
        a(new SetAttributes.b());
    }

    public static class a implements JsonDeserializer<LootFunction>, JsonSerializer<LootFunction> {

        public a() {}

        public LootFunction a(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
            JsonObject jsonobject = JsonUtils.getJsonObject(jsonelement, "function");
            ResourceLocation minecraftkey = new ResourceLocation(JsonUtils.getString(jsonobject, "function"));

            LootFunction.a lootitemfunction_a;

            try {
                lootitemfunction_a = LootFunctionManager.a(minecraftkey);
            } catch (IllegalArgumentException illegalargumentexception) {
                throw new JsonSyntaxException("Unknown function \'" + minecraftkey + "\'");
            }

            return lootitemfunction_a.b(jsonobject, jsondeserializationcontext, JsonUtils.deserializeClass(jsonobject, "conditions", new LootCondition[0], jsondeserializationcontext, LootCondition[].class));
        }

        public JsonElement a(LootFunction lootitemfunction, Type type, JsonSerializationContext jsonserializationcontext) {
            LootFunction.a lootitemfunction_a = LootFunctionManager.a(lootitemfunction);
            JsonObject jsonobject = new JsonObject();

            lootitemfunction_a.a(jsonobject, lootitemfunction, jsonserializationcontext);
            jsonobject.addProperty("function", lootitemfunction_a.a().toString());
            if (lootitemfunction.getConditions() != null && lootitemfunction.getConditions().length > 0) {
                jsonobject.add("conditions", jsonserializationcontext.serialize(lootitemfunction.getConditions()));
            }

            return jsonobject;
        }

        @Override
        public JsonElement serialize(LootFunction object, Type type, JsonSerializationContext jsonserializationcontext) {
            return this.a(object, type, jsonserializationcontext);
        }

        @Override
        public LootFunction deserialize(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
            return this.a(jsonelement, type, jsondeserializationcontext);
        }
    }
}
