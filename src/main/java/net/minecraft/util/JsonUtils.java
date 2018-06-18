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

    public static boolean isString(JsonObject jsonobject, String s) {
        return !isJsonPrimitive(jsonobject, s) ? false : jsonobject.getAsJsonPrimitive(s).isString();
    }

    public static boolean isNumber(JsonElement jsonelement) {
        return !jsonelement.isJsonPrimitive() ? false : jsonelement.getAsJsonPrimitive().isNumber();
    }

    public static boolean isJsonArray(JsonObject jsonobject, String s) {
        return !hasField(jsonobject, s) ? false : jsonobject.get(s).isJsonArray();
    }

    public static boolean isJsonPrimitive(JsonObject jsonobject, String s) {
        return !hasField(jsonobject, s) ? false : jsonobject.get(s).isJsonPrimitive();
    }

    public static boolean hasField(JsonObject jsonobject, String s) {
        return jsonobject == null ? false : jsonobject.get(s) != null;
    }

    public static String getString(JsonElement jsonelement, String s) {
        if (jsonelement.isJsonPrimitive()) {
            return jsonelement.getAsString();
        } else {
            throw new JsonSyntaxException("Expected " + s + " to be a string, was " + toString(jsonelement));
        }
    }

    public static String getString(JsonObject jsonobject, String s) {
        if (jsonobject.has(s)) {
            return getString(jsonobject.get(s), s);
        } else {
            throw new JsonSyntaxException("Missing " + s + ", expected to find a string");
        }
    }

    public static String getString(JsonObject jsonobject, String s, String s1) {
        return jsonobject.has(s) ? getString(jsonobject.get(s), s) : s1;
    }

    public static Item getItem(JsonElement jsonelement, String s) {
        if (jsonelement.isJsonPrimitive()) {
            String s1 = jsonelement.getAsString();
            Item item = Item.getByNameOrId(s1);

            if (item == null) {
                throw new JsonSyntaxException("Expected " + s + " to be an item, was unknown string \'" + s1 + "\'");
            } else {
                return item;
            }
        } else {
            throw new JsonSyntaxException("Expected " + s + " to be an item, was " + toString(jsonelement));
        }
    }

    public static Item getItem(JsonObject jsonobject, String s) {
        if (jsonobject.has(s)) {
            return getItem(jsonobject.get(s), s);
        } else {
            throw new JsonSyntaxException("Missing " + s + ", expected to find an item");
        }
    }

    public static boolean getBoolean(JsonElement jsonelement, String s) {
        if (jsonelement.isJsonPrimitive()) {
            return jsonelement.getAsBoolean();
        } else {
            throw new JsonSyntaxException("Expected " + s + " to be a Boolean, was " + toString(jsonelement));
        }
    }

    public static boolean getBoolean(JsonObject jsonobject, String s) {
        if (jsonobject.has(s)) {
            return getBoolean(jsonobject.get(s), s);
        } else {
            throw new JsonSyntaxException("Missing " + s + ", expected to find a Boolean");
        }
    }

    public static boolean getBoolean(JsonObject jsonobject, String s, boolean flag) {
        return jsonobject.has(s) ? getBoolean(jsonobject.get(s), s) : flag;
    }

    public static float getFloat(JsonElement jsonelement, String s) {
        if (jsonelement.isJsonPrimitive() && jsonelement.getAsJsonPrimitive().isNumber()) {
            return jsonelement.getAsFloat();
        } else {
            throw new JsonSyntaxException("Expected " + s + " to be a Float, was " + toString(jsonelement));
        }
    }

    public static float getFloat(JsonObject jsonobject, String s) {
        if (jsonobject.has(s)) {
            return getFloat(jsonobject.get(s), s);
        } else {
            throw new JsonSyntaxException("Missing " + s + ", expected to find a Float");
        }
    }

    public static float getFloat(JsonObject jsonobject, String s, float f) {
        return jsonobject.has(s) ? getFloat(jsonobject.get(s), s) : f;
    }

    public static int getInt(JsonElement jsonelement, String s) {
        if (jsonelement.isJsonPrimitive() && jsonelement.getAsJsonPrimitive().isNumber()) {
            return jsonelement.getAsInt();
        } else {
            throw new JsonSyntaxException("Expected " + s + " to be a Int, was " + toString(jsonelement));
        }
    }

    public static int getInt(JsonObject jsonobject, String s) {
        if (jsonobject.has(s)) {
            return getInt(jsonobject.get(s), s);
        } else {
            throw new JsonSyntaxException("Missing " + s + ", expected to find a Int");
        }
    }

    public static int getInt(JsonObject jsonobject, String s, int i) {
        return jsonobject.has(s) ? getInt(jsonobject.get(s), s) : i;
    }

    public static JsonObject getJsonObject(JsonElement jsonelement, String s) {
        if (jsonelement.isJsonObject()) {
            return jsonelement.getAsJsonObject();
        } else {
            throw new JsonSyntaxException("Expected " + s + " to be a JsonObject, was " + toString(jsonelement));
        }
    }

    public static JsonObject getJsonObject(JsonObject jsonobject, String s) {
        if (jsonobject.has(s)) {
            return getJsonObject(jsonobject.get(s), s);
        } else {
            throw new JsonSyntaxException("Missing " + s + ", expected to find a JsonObject");
        }
    }

    public static JsonObject getJsonObject(JsonObject jsonobject, String s, JsonObject jsonobject1) {
        return jsonobject.has(s) ? getJsonObject(jsonobject.get(s), s) : jsonobject1;
    }

    public static JsonArray getJsonArray(JsonElement jsonelement, String s) {
        if (jsonelement.isJsonArray()) {
            return jsonelement.getAsJsonArray();
        } else {
            throw new JsonSyntaxException("Expected " + s + " to be a JsonArray, was " + toString(jsonelement));
        }
    }

    public static JsonArray getJsonArray(JsonObject jsonobject, String s) {
        if (jsonobject.has(s)) {
            return getJsonArray(jsonobject.get(s), s);
        } else {
            throw new JsonSyntaxException("Missing " + s + ", expected to find a JsonArray");
        }
    }

    public static JsonArray getJsonArray(JsonObject jsonobject, String s, @Nullable JsonArray jsonarray) {
        return jsonobject.has(s) ? getJsonArray(jsonobject.get(s), s) : jsonarray;
    }

    public static <T> T deserializeClass(@Nullable JsonElement jsonelement, String s, JsonDeserializationContext jsondeserializationcontext, Class<? extends T> oclass) {
        if (jsonelement != null) {
            return jsondeserializationcontext.deserialize(jsonelement, oclass);
        } else {
            throw new JsonSyntaxException("Missing " + s);
        }
    }

    public static <T> T deserializeClass(JsonObject jsonobject, String s, JsonDeserializationContext jsondeserializationcontext, Class<? extends T> oclass) {
        if (jsonobject.has(s)) {
            return deserializeClass(jsonobject.get(s), s, jsondeserializationcontext, oclass);
        } else {
            throw new JsonSyntaxException("Missing " + s);
        }
    }

    public static <T> T deserializeClass(JsonObject jsonobject, String s, T t0, JsonDeserializationContext jsondeserializationcontext, Class<? extends T> oclass) {
        return jsonobject.has(s) ? deserializeClass(jsonobject.get(s), s, jsondeserializationcontext, oclass) : t0;
    }

    public static String toString(JsonElement jsonelement) {
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
    public static <T> T gsonDeserialize(Gson gson, Reader reader, Class<T> oclass, boolean flag) {
        try {
            JsonReader jsonreader = new JsonReader(reader);

            jsonreader.setLenient(flag);
            return gson.getAdapter(oclass).read(jsonreader);
        } catch (IOException ioexception) {
            throw new JsonParseException(ioexception);
        }
    }

    @Nullable
    public static <T> T fromJson(Gson gson, Reader reader, Type type, boolean flag) {
        try {
            JsonReader jsonreader = new JsonReader(reader);

            jsonreader.setLenient(flag);
            return gson.getAdapter(TypeToken.get(type)).read(jsonreader);
        } catch (IOException ioexception) {
            throw new JsonParseException(ioexception);
        }
    }

    @Nullable
    public static <T> T fromJson(Gson gson, String s, Type type, boolean flag) {
        return fromJson(gson, (Reader) (new StringReader(s)), type, flag);
    }

    @Nullable
    public static <T> T gsonDeserialize(Gson gson, String s, Class<T> oclass, boolean flag) {
        return gsonDeserialize(gson, (Reader) (new StringReader(s)), oclass, flag);
    }

    @Nullable
    public static <T> T fromJson(Gson gson, Reader reader, Type type) {
        return fromJson(gson, reader, type, false);
    }

    @Nullable
    public static <T> T gsonDeserialize(Gson gson, String s, Type type) {
        return fromJson(gson, s, type, false);
    }

    @Nullable
    public static <T> T fromJson(Gson gson, Reader reader, Class<T> oclass) {
        return gsonDeserialize(gson, reader, oclass, false);
    }

    @Nullable
    public static <T> T gsonDeserialize(Gson gson, String s, Class<T> oclass) {
        return gsonDeserialize(gson, s, oclass, false);
    }
}
