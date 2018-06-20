package net.minecraft.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Type;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;

import net.minecraft.item.Item;

public class JsonUtils {

    public static boolean func_151205_a(JsonObject jsonobject, String s) {
        return !func_151201_f(jsonobject, s) ? false : jsonobject.getAsJsonPrimitive(s).isString();
    }

    public static boolean func_188175_b(JsonElement jsonelement) {
        return !jsonelement.isJsonPrimitive() ? false : jsonelement.getAsJsonPrimitive().isNumber();
    }

    public static boolean func_151202_d(JsonObject jsonobject, String s) {
        return !func_151204_g(jsonobject, s) ? false : jsonobject.get(s).isJsonArray();
    }

    public static boolean func_151201_f(JsonObject jsonobject, String s) {
        return !func_151204_g(jsonobject, s) ? false : jsonobject.get(s).isJsonPrimitive();
    }

    public static boolean func_151204_g(JsonObject jsonobject, String s) {
        return jsonobject == null ? false : jsonobject.get(s) != null;
    }

    public static String func_151206_a(JsonElement jsonelement, String s) {
        if (jsonelement.isJsonPrimitive()) {
            return jsonelement.getAsString();
        } else {
            throw new JsonSyntaxException("Expected " + s + " to be a string, was " + func_151222_d(jsonelement));
        }
    }

    public static String func_151200_h(JsonObject jsonobject, String s) {
        if (jsonobject.has(s)) {
            return func_151206_a(jsonobject.get(s), s);
        } else {
            throw new JsonSyntaxException("Missing " + s + ", expected to find a string");
        }
    }

    public static String func_151219_a(JsonObject jsonobject, String s, String s1) {
        return jsonobject.has(s) ? func_151206_a(jsonobject.get(s), s) : s1;
    }

    public static Item func_188172_b(JsonElement jsonelement, String s) {
        if (jsonelement.isJsonPrimitive()) {
            String s1 = jsonelement.getAsString();
            Item item = Item.func_111206_d(s1);

            if (item == null) {
                throw new JsonSyntaxException("Expected " + s + " to be an item, was unknown string \'" + s1 + "\'");
            } else {
                return item;
            }
        } else {
            throw new JsonSyntaxException("Expected " + s + " to be an item, was " + func_151222_d(jsonelement));
        }
    }

    public static Item func_188180_i(JsonObject jsonobject, String s) {
        if (jsonobject.has(s)) {
            return func_188172_b(jsonobject.get(s), s);
        } else {
            throw new JsonSyntaxException("Missing " + s + ", expected to find an item");
        }
    }

    public static boolean func_151216_b(JsonElement jsonelement, String s) {
        if (jsonelement.isJsonPrimitive()) {
            return jsonelement.getAsBoolean();
        } else {
            throw new JsonSyntaxException("Expected " + s + " to be a Boolean, was " + func_151222_d(jsonelement));
        }
    }

    public static boolean func_151212_i(JsonObject jsonobject, String s) {
        if (jsonobject.has(s)) {
            return func_151216_b(jsonobject.get(s), s);
        } else {
            throw new JsonSyntaxException("Missing " + s + ", expected to find a Boolean");
        }
    }

    public static boolean func_151209_a(JsonObject jsonobject, String s, boolean flag) {
        return jsonobject.has(s) ? func_151216_b(jsonobject.get(s), s) : flag;
    }

    public static float func_151220_d(JsonElement jsonelement, String s) {
        if (jsonelement.isJsonPrimitive() && jsonelement.getAsJsonPrimitive().isNumber()) {
            return jsonelement.getAsFloat();
        } else {
            throw new JsonSyntaxException("Expected " + s + " to be a Float, was " + func_151222_d(jsonelement));
        }
    }

    public static float func_151217_k(JsonObject jsonobject, String s) {
        if (jsonobject.has(s)) {
            return func_151220_d(jsonobject.get(s), s);
        } else {
            throw new JsonSyntaxException("Missing " + s + ", expected to find a Float");
        }
    }

    public static float func_151221_a(JsonObject jsonobject, String s, float f) {
        return jsonobject.has(s) ? func_151220_d(jsonobject.get(s), s) : f;
    }

    public static int func_151215_f(JsonElement jsonelement, String s) {
        if (jsonelement.isJsonPrimitive() && jsonelement.getAsJsonPrimitive().isNumber()) {
            return jsonelement.getAsInt();
        } else {
            throw new JsonSyntaxException("Expected " + s + " to be a Int, was " + func_151222_d(jsonelement));
        }
    }

    public static int func_151203_m(JsonObject jsonobject, String s) {
        if (jsonobject.has(s)) {
            return func_151215_f(jsonobject.get(s), s);
        } else {
            throw new JsonSyntaxException("Missing " + s + ", expected to find a Int");
        }
    }

    public static int func_151208_a(JsonObject jsonobject, String s, int i) {
        return jsonobject.has(s) ? func_151215_f(jsonobject.get(s), s) : i;
    }

    public static JsonObject func_151210_l(JsonElement jsonelement, String s) {
        if (jsonelement.isJsonObject()) {
            return jsonelement.getAsJsonObject();
        } else {
            throw new JsonSyntaxException("Expected " + s + " to be a JsonObject, was " + func_151222_d(jsonelement));
        }
    }

    public static JsonObject func_152754_s(JsonObject jsonobject, String s) {
        if (jsonobject.has(s)) {
            return func_151210_l(jsonobject.get(s), s);
        } else {
            throw new JsonSyntaxException("Missing " + s + ", expected to find a JsonObject");
        }
    }

    public static JsonObject func_151218_a(JsonObject jsonobject, String s, JsonObject jsonobject1) {
        return jsonobject.has(s) ? func_151210_l(jsonobject.get(s), s) : jsonobject1;
    }

    public static JsonArray func_151207_m(JsonElement jsonelement, String s) {
        if (jsonelement.isJsonArray()) {
            return jsonelement.getAsJsonArray();
        } else {
            throw new JsonSyntaxException("Expected " + s + " to be a JsonArray, was " + func_151222_d(jsonelement));
        }
    }

    public static JsonArray func_151214_t(JsonObject jsonobject, String s) {
        if (jsonobject.has(s)) {
            return func_151207_m(jsonobject.get(s), s);
        } else {
            throw new JsonSyntaxException("Missing " + s + ", expected to find a JsonArray");
        }
    }

    public static JsonArray func_151213_a(JsonObject jsonobject, String s, @Nullable JsonArray jsonarray) {
        return jsonobject.has(s) ? func_151207_m(jsonobject.get(s), s) : jsonarray;
    }

    public static <T> T func_188179_a(@Nullable JsonElement jsonelement, String s, JsonDeserializationContext jsondeserializationcontext, Class<? extends T> oclass) {
        if (jsonelement != null) {
            return jsondeserializationcontext.deserialize(jsonelement, oclass);
        } else {
            throw new JsonSyntaxException("Missing " + s);
        }
    }

    public static <T> T func_188174_a(JsonObject jsonobject, String s, JsonDeserializationContext jsondeserializationcontext, Class<? extends T> oclass) {
        if (jsonobject.has(s)) {
            return func_188179_a(jsonobject.get(s), s, jsondeserializationcontext, oclass);
        } else {
            throw new JsonSyntaxException("Missing " + s);
        }
    }

    public static <T> T func_188177_a(JsonObject jsonobject, String s, T t0, JsonDeserializationContext jsondeserializationcontext, Class<? extends T> oclass) {
        return jsonobject.has(s) ? func_188179_a(jsonobject.get(s), s, jsondeserializationcontext, oclass) : t0;
    }

    public static String func_151222_d(JsonElement jsonelement) {
        String s = StringUtils.abbreviateMiddle(String.valueOf(jsonelement), "...", 10);

        if (jsonelement == null) {
            return "null (missing)";
        } else if (jsonelement.isJsonNull()) {
            return "null (json)";
        } else if (jsonelement.isJsonArray()) {
            return "an array (" + s + ")";
        } else if (jsonelement.isJsonObject()) {
            return "an object (" + s + ")";
        } else {
            if (jsonelement.isJsonPrimitive()) {
                JsonPrimitive jsonprimitive = jsonelement.getAsJsonPrimitive();

                if (jsonprimitive.isNumber()) {
                    return "a number (" + s + ")";
                }

                if (jsonprimitive.isBoolean()) {
                    return "a boolean (" + s + ")";
                }
            }

            return s;
        }
    }

    @Nullable
    public static <T> T func_188173_a(Gson gson, Reader reader, Class<T> oclass, boolean flag) {
        try {
            JsonReader jsonreader = new JsonReader(reader);

            jsonreader.setLenient(flag);
            return gson.getAdapter(oclass).read(jsonreader);
        } catch (IOException ioexception) {
            throw new JsonParseException(ioexception);
        }
    }

    @Nullable
    public static <T> T func_193838_a(Gson gson, Reader reader, Type type, boolean flag) {
        try {
            JsonReader jsonreader = new JsonReader(reader);

            jsonreader.setLenient(flag);
            return (T) gson.getAdapter(TypeToken.get(type)).read(jsonreader);
        } catch (IOException ioexception) {
            throw new JsonParseException(ioexception);
        }
    }

    @Nullable
    public static <T> T func_193837_a(Gson gson, String s, Type type, boolean flag) {
        return func_193838_a(gson, (Reader) (new StringReader(s)), type, flag);
    }

    @Nullable
    public static <T> T func_188176_a(Gson gson, String s, Class<T> oclass, boolean flag) {
        return func_188173_a(gson, (Reader) (new StringReader(s)), oclass, flag);
    }

    @Nullable
    public static <T> T func_193841_a(Gson gson, Reader reader, Type type) {
        return func_193838_a(gson, reader, type, false);
    }

    @Nullable
    public static <T> T func_193840_a(Gson gson, String s, Type type) {
        return func_193837_a(gson, s, type, false);
    }

    @Nullable
    public static <T> T func_193839_a(Gson gson, Reader reader, Class<T> oclass) {
        return func_188173_a(gson, reader, oclass, false);
    }

    @Nullable
    public static <T> T func_188178_a(Gson gson, String s, Class<T> oclass) {
        return func_188176_a(gson, s, oclass, false);
    }
}
