package net.minecraft.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import net.minecraft.server.MinecraftKey.a;

public class ResourceLocation implements Comparable<ResourceLocation> {

    protected final String field_110626_a;
    protected final String field_110625_b;

    protected ResourceLocation(int i, String... astring) {
        this.field_110626_a = StringUtils.isEmpty(astring[0]) ? "minecraft" : astring[0].toLowerCase(Locale.ROOT);
        this.field_110625_b = astring[1].toLowerCase(Locale.ROOT);
        Validate.notNull(this.field_110625_b);
    }

    public ResourceLocation(String s) {
        this(0, func_177516_a(s));
    }

    public ResourceLocation(String s, String s1) {
        this(0, new String[] { s, s1});
    }

    protected static String[] func_177516_a(String s) {
        String[] astring = new String[] { "minecraft", s};
        int i = s.indexOf(58);

        if (i >= 0) {
            astring[1] = s.substring(i + 1, s.length());
            if (i > 1) {
                astring[0] = s.substring(0, i);
            }
        }

        return astring;
    }

    public String func_110623_a() {
        return this.field_110625_b;
    }

    public String func_110624_b() {
        return this.field_110626_a;
    }

    public String toString() {
        return this.field_110626_a + ':' + this.field_110625_b;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof ResourceLocation)) {
            return false;
        } else {
            ResourceLocation minecraftkey = (ResourceLocation) object;

            return this.field_110626_a.equals(minecraftkey.field_110626_a) && this.field_110625_b.equals(minecraftkey.field_110625_b);
        }
    }

    public int hashCode() {
        return 31 * this.field_110626_a.hashCode() + this.field_110625_b.hashCode();
    }

    public int compareTo(ResourceLocation minecraftkey) {
        int i = this.field_110626_a.compareTo(minecraftkey.field_110626_a);

        if (i == 0) {
            i = this.field_110625_b.compareTo(minecraftkey.field_110625_b);
        }

        return i;
    }

    public int compareTo(Object object) {
        return this.compareTo((ResourceLocation) object);
    }

    public static class a implements JsonDeserializer<ResourceLocation>, JsonSerializer<ResourceLocation> {

        public a() {}

        public ResourceLocation a(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
            return new ResourceLocation(JsonUtils.func_151206_a(jsonelement, "location"));
        }

        public JsonElement a(ResourceLocation minecraftkey, Type type, JsonSerializationContext jsonserializationcontext) {
            return new JsonPrimitive(minecraftkey.toString());
        }

        public JsonElement serialize(Object object, Type type, JsonSerializationContext jsonserializationcontext) {
            return this.a((ResourceLocation) object, type, jsonserializationcontext);
        }

        public Object deserialize(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
            return this.a(jsonelement, type, jsondeserializationcontext);
        }
    }
}
