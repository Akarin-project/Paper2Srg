package net.minecraft.util.datafix.fixes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.Iterator;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.StringUtils;
import net.minecraft.util.datafix.IFixableData;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class SignStrictJSON implements IFixableData {

    public static final Gson GSON_INSTANCE = (new GsonBuilder()).registerTypeAdapter(ITextComponent.class, new JsonDeserializer() {
        public ITextComponent a(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
            if (jsonelement.isJsonPrimitive()) {
                return new TextComponentString(jsonelement.getAsString());
            } else if (jsonelement.isJsonArray()) {
                JsonArray jsonarray = jsonelement.getAsJsonArray();
                ITextComponent ichatbasecomponent = null;
                Iterator iterator = jsonarray.iterator();

                while (iterator.hasNext()) {
                    JsonElement jsonelement1 = (JsonElement) iterator.next();
                    ITextComponent ichatbasecomponent1 = this.a(jsonelement1, jsonelement1.getClass(), jsondeserializationcontext);

                    if (ichatbasecomponent == null) {
                        ichatbasecomponent = ichatbasecomponent1;
                    } else {
                        ichatbasecomponent.appendSibling(ichatbasecomponent1);
                    }
                }

                return ichatbasecomponent;
            } else {
                throw new JsonParseException("Don\'t know how to turn " + jsonelement + " into a Component");
            }
        }

        public Object deserialize(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
            return this.a(jsonelement, type, jsondeserializationcontext);
        }
    }).create();

    public SignStrictJSON() {}

    public int getFixVersion() {
        return 101;
    }

    public NBTTagCompound fixTagCompound(NBTTagCompound nbttagcompound) {
        if ("Sign".equals(nbttagcompound.getString("id"))) {
            this.updateLine(nbttagcompound, "Text1");
            this.updateLine(nbttagcompound, "Text2");
            this.updateLine(nbttagcompound, "Text3");
            this.updateLine(nbttagcompound, "Text4");
        }

        return nbttagcompound;
    }

    private void updateLine(NBTTagCompound nbttagcompound, String s) {
        String s1 = nbttagcompound.getString(s);
        Object object = null;

        if (!"null".equals(s1) && !StringUtils.isNullOrEmpty(s1)) {
            if ((s1.charAt(0) != 34 || s1.charAt(s1.length() - 1) != 34) && (s1.charAt(0) != 123 || s1.charAt(s1.length() - 1) != 125)) {
                object = new TextComponentString(s1);
            } else {
                try {
                    object = (ITextComponent) JsonUtils.gsonDeserialize(SignStrictJSON.GSON_INSTANCE, s1, ITextComponent.class, true);
                    if (object == null) {
                        object = new TextComponentString("");
                    }
                } catch (JsonParseException jsonparseexception) {
                    ;
                }

                if (object == null) {
                    try {
                        object = ITextComponent.Serializer.jsonToComponent(s1);
                    } catch (JsonParseException jsonparseexception1) {
                        ;
                    }
                }

                if (object == null) {
                    try {
                        object = ITextComponent.Serializer.fromJsonLenient(s1);
                    } catch (JsonParseException jsonparseexception2) {
                        ;
                    }
                }

                if (object == null) {
                    object = new TextComponentString(s1);
                }
            }
        } else {
            object = new TextComponentString("");
        }

        nbttagcompound.setString(s, ITextComponent.Serializer.componentToJson((ITextComponent) object));
    }
}
