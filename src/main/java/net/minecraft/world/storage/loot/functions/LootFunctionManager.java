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

import net.minecraft.server.LootItemFunctions.a;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public class LootFunctionManager {

    private static final Map<ResourceLocation, LootItemFunction.a<?>> field_186584_a = Maps.newHashMap();
    private static final Map<Class<? extends LootFunction>, LootItemFunction.a<?>> field_186585_b = Maps.newHashMap();

    public static <T extends LootFunction> void a(LootItemFunction.a<? extends T> lootitemfunction_a) {
        ResourceLocation minecraftkey = lootitemfunction_a.a();
        Class oclass = lootitemfunction_a.b();

        if (LootFunctionManager.field_186584_a.containsKey(minecraftkey)) {
            throw new IllegalArgumentException("Can\'t re-register item function name " + minecraftkey);
        } else if (LootFunctionManager.field_186585_b.containsKey(oclass)) {
            throw new IllegalArgumentException("Can\'t re-register item function class " + oclass.getName());
        } else {
            LootFunctionManager.field_186584_a.put(minecraftkey, lootitemfunction_a);
            LootFunctionManager.field_186585_b.put(oclass, lootitemfunction_a);
        }
    }

    public static LootItemFunction.a<?> a(ResourceLocation minecraftkey) {
        LootItemFunction.a lootitemfunction_a = (LootItemFunction.a) LootFunctionManager.field_186584_a.get(minecraftkey);

        if (lootitemfunction_a == null) {
            throw new IllegalArgumentException("Unknown loot item function \'" + minecraftkey + "\'");
        } else {
            return lootitemfunction_a;
        }
    }

    public static <T extends LootFunction> LootItemFunction.a<T> a(T t0) {
        LootItemFunction.a lootitemfunction_a = (LootItemFunction.a) LootFunctionManager.field_186585_b.get(t0.getClass());

        if (lootitemfunction_a == null) {
            throw new IllegalArgumentException("Unknown loot item function " + t0);
        } else {
            return lootitemfunction_a;
        }
    }

    static {
        a((LootItemFunction.a) (new LootItemFunctionSetCount.a()));
        a((LootItemFunction.a) (new LootItemFunctionSetData.a()));
        a((LootItemFunction.a) (new LootEnchantLevel.a()));
        a((LootItemFunction.a) (new LootItemFunctionEnchant.a()));
        a((LootItemFunction.a) (new LootItemFunctionSetTag.a()));
        a((LootItemFunction.a) (new LootItemFunctionSmelt.a()));
        a((LootItemFunction.a) (new LootEnchantFunction.a()));
        a((LootItemFunction.a) (new LootItemFunctionSetDamage.a()));
        a((LootItemFunction.a) (new LootItemFunctionSetAttribute.b()));
    }

    public static class a implements JsonDeserializer<LootFunction>, JsonSerializer<LootFunction> {

        public a() {}

        public LootFunction a(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
            JsonObject jsonobject = JsonUtils.func_151210_l(jsonelement, "function");
            ResourceLocation minecraftkey = new ResourceLocation(JsonUtils.func_151200_h(jsonobject, "function"));

            LootItemFunction.a lootitemfunction_a;

            try {
                lootitemfunction_a = LootFunctionManager.a(minecraftkey);
            } catch (IllegalArgumentException illegalargumentexception) {
                throw new JsonSyntaxException("Unknown function \'" + minecraftkey + "\'");
            }

            return lootitemfunction_a.b(jsonobject, jsondeserializationcontext, (LootCondition[]) JsonUtils.func_188177_a(jsonobject, "conditions", new LootCondition[0], jsondeserializationcontext, LootCondition[].class));
        }

        public JsonElement a(LootFunction lootitemfunction, Type type, JsonSerializationContext jsonserializationcontext) {
            LootItemFunction.a lootitemfunction_a = LootFunctionManager.a(lootitemfunction);
            JsonObject jsonobject = new JsonObject();

            lootitemfunction_a.a(jsonobject, lootitemfunction, jsonserializationcontext);
            jsonobject.addProperty("function", lootitemfunction_a.a().toString());
            if (lootitemfunction.func_186554_a() != null && lootitemfunction.func_186554_a().length > 0) {
                jsonobject.add("conditions", jsonserializationcontext.serialize(lootitemfunction.func_186554_a()));
            }

            return jsonobject;
        }

        public JsonElement serialize(Object object, Type type, JsonSerializationContext jsonserializationcontext) {
            return this.a((LootFunction) object, type, jsonserializationcontext);
        }

        public Object deserialize(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
            return this.a(jsonelement, type, jsondeserializationcontext);
        }
    }
}
