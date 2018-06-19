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

    public static final Gson field_188225_a = (new GsonBuilder()).registerTypeAdapter(ITextComponent.class, new JsonDeserializer() {
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
                        ichatbasecomponent.func_150257_a(ichatbasecomponent1);
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

    public int func_188216_a() {
        return 101;
    }

    public NBTTagCompound func_188217_a(NBTTagCompound nbttagcompound) {
        if ("Sign".equals(nbttagcompound.func_74779_i("id"))) {
            this.func_188224_a(nbttagcompound, "Text1");
            this.func_188224_a(nbttagcompound, "Text2");
            this.func_188224_a(nbttagcompound, "Text3");
            this.func_188224_a(nbttagcompound, "Text4");
        }

        return nbttagcompound;
    }

    private void func_188224_a(NBTTagCompound nbttagcompound, String s) {
        String s1 = nbttagcompound.func_74779_i(s);
        Object object = null;

        if (!"null".equals(s1) && !StringUtils.func_151246_b(s1)) {
            if ((s1.charAt(0) != 34 || s1.charAt(s1.length() - 1) != 34) && (s1.charAt(0) != 123 || s1.charAt(s1.length() - 1) != 125)) {
                object = new TextComponentString(s1);
            } else {
                try {
                    object = (ITextComponent) JsonUtils.func_188176_a(SignStrictJSON.field_188225_a, s1, ITextComponent.class, true);
                    if (object == null) {
                        object = new TextComponentString("");
                    }
                } catch (JsonParseException jsonparseexception) {
                    ;
                }

                if (object == null) {
                    try {
                        object = ITextComponent.Serializer.func_150699_a(s1);
                    } catch (JsonParseException jsonparseexception1) {
                        ;
                    }
                }

                if (object == null) {
                    try {
                        object = ITextComponent.Serializer.func_186877_b(s1);
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

        nbttagcompound.func_74778_a(s, ITextComponent.Serializer.func_150696_a((ITextComponent) object));
    }
}
