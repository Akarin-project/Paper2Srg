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

    private static final Map<ResourceLocation, LootFunction.a<?>> field_186584_a = Maps.newHashMap();
    private static final Map<Class<? extends LootFunction>, LootFunction.a<?>> field_186585_b = Maps.newHashMap();

    public static <T extends LootFunction> void a(LootFunction.a<? extends T> LootFunction_a) {
        ResourceLocation minecraftkey = LootFunction_a.a();
        Class oclass = LootFunction_a.b();

        if (LootFunctionManager.field_186584_a.containsKey(minecraftkey)) {
            throw new IllegalArgumentException("Can\'t re-register item function name " + minecraftkey);
        } else if (LootFunctionManager.field_186585_b.containsKey(oclass)) {
            throw new IllegalArgumentException("Can\'t re-register item function class " + oclass.getName());
        } else {
            LootFunctionManager.field_186584_a.put(minecraftkey, LootFunction_a);
            LootFunctionManager.field_186585_b.put(oclass, LootFunction_a);
        }
    }

    public static LootFunction.a<?> a(ResourceLocation minecraftkey) {
        LootFunction.a LootFunction_a = LootFunctionManager.field_186584_a.get(minecraftkey);

        if (LootFunction_a == null) {
            throw new IllegalArgumentException("Unknown loot item function \'" + minecraftkey + "\'");
        } else {
            return LootFunction_a;
        }
    }

    public static <T extends LootFunction> LootFunction.a<T> a(T t0) {
        LootFunction.a LootFunction_a = LootFunctionManager.field_186585_b.get(t0.getClass());

        if (LootFunction_a == null) {
            throw new IllegalArgumentException("Unknown loot item function " + t0);
        } else {
            return LootFunction_a;
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

        @Override
        public LootFunction deserialize(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
            JsonObject jsonobject = JsonUtils.func_151210_l(jsonelement, "function");
            ResourceLocation minecraftkey = new ResourceLocation(JsonUtils.func_151200_h(jsonobject, "function"));

            LootFunction.a LootFunction_a;

            try {
                LootFunction_a = LootFunctionManager.a(minecraftkey);
            } catch (IllegalArgumentException illegalargumentexception) {
                throw new JsonSyntaxException("Unknown function \'" + minecraftkey + "\'");
            }

            return LootFunction_a.b(jsonobject, jsondeserializationcontext, JsonUtils.func_188177_a(jsonobject, "conditions", new LootCondition[0], jsondeserializationcontext, LootCondition[].class));
        }

        @Override
        public JsonElement serialize(LootFunction LootFunction, Type type, JsonSerializationContext jsonserializationcontext) {
            LootFunction.a LootFunction_a = LootFunctionManager.a(LootFunction);
            JsonObject jsonobject = new JsonObject();

            LootFunction_a.a(jsonobject, LootFunction, jsonserializationcontext);
            jsonobject.addProperty("function", LootFunction_a.a().toString());
            if (LootFunction.func_186554_a() != null && LootFunction.func_186554_a().length > 0) {
                jsonobject.add("conditions", jsonserializationcontext.serialize(LootFunction.func_186554_a()));
            }

            return jsonobject;
        }
    }
}
