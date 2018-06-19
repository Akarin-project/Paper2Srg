package net.minecraft.util.text;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import javax.annotation.Nullable;

import net.minecraft.util.EnumTypeAdapterFactory;
import net.minecraft.util.JsonUtils;

public interface ITextComponent extends Iterable<ITextComponent> {

    ITextComponent func_150255_a(Style chatmodifier);

    Style func_150256_b();

    ITextComponent func_150258_a(String s);

    ITextComponent func_150257_a(ITextComponent ichatbasecomponent);

    String func_150261_e();

    String func_150260_c();

    List<ITextComponent> func_150253_a();

    ITextComponent func_150259_f();

    public static class Serializer implements JsonDeserializer<ITextComponent>, JsonSerializer<ITextComponent> {

        private static final Gson field_150700_a;

        public Serializer() {}

        public ITextComponent deserialize(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
            if (jsonelement.isJsonPrimitive()) {
                return new TextComponentString(jsonelement.getAsString());
            } else if (!jsonelement.isJsonObject()) {
                if (jsonelement.isJsonArray()) {
                    JsonArray jsonarray = jsonelement.getAsJsonArray();
                    ITextComponent ichatbasecomponent = null;
                    Iterator iterator = jsonarray.iterator();

                    while (iterator.hasNext()) {
                        JsonElement jsonelement1 = (JsonElement) iterator.next();
                        ITextComponent ichatbasecomponent1 = this.deserialize(jsonelement1, (Type) jsonelement1.getClass(), jsondeserializationcontext);

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
            } else {
                JsonObject jsonobject = jsonelement.getAsJsonObject();
                Object object;

                if (jsonobject.has("text")) {
                    object = new TextComponentString(jsonobject.get("text").getAsString());
                } else if (jsonobject.has("translate")) {
                    String s = jsonobject.get("translate").getAsString();

                    if (jsonobject.has("with")) {
                        JsonArray jsonarray1 = jsonobject.getAsJsonArray("with");
                        Object[] aobject = new Object[jsonarray1.size()];

                        for (int i = 0; i < aobject.length; ++i) {
                            aobject[i] = this.deserialize(jsonarray1.get(i), type, jsondeserializationcontext);
                            if (aobject[i] instanceof TextComponentString) {
                                TextComponentString chatcomponenttext = (TextComponentString) aobject[i];

                                if (chatcomponenttext.func_150256_b().func_150229_g() && chatcomponenttext.func_150253_a().isEmpty()) {
                                    aobject[i] = chatcomponenttext.func_150265_g();
                                }
                            }
                        }

                        object = new TextComponentTranslation(s, aobject);
                    } else {
                        object = new TextComponentTranslation(s, new Object[0]);
                    }
                } else if (jsonobject.has("score")) {
                    JsonObject jsonobject1 = jsonobject.getAsJsonObject("score");

                    if (!jsonobject1.has("name") || !jsonobject1.has("objective")) {
                        throw new JsonParseException("A score component needs a least a name and an objective");
                    }

                    object = new TextComponentScore(JsonUtils.func_151200_h(jsonobject1, "name"), JsonUtils.func_151200_h(jsonobject1, "objective"));
                    if (jsonobject1.has("value")) {
                        ((TextComponentScore) object).func_179997_b(JsonUtils.func_151200_h(jsonobject1, "value"));
                    }
                } else if (jsonobject.has("selector")) {
                    object = new TextComponentSelector(JsonUtils.func_151200_h(jsonobject, "selector"));
                } else {
                    if (!jsonobject.has("keybind")) {
                        throw new JsonParseException("Don\'t know how to turn " + jsonelement + " into a Component");
                    }

                    object = new TextComponentKeybind(JsonUtils.func_151200_h(jsonobject, "keybind"));
                }

                if (jsonobject.has("extra")) {
                    JsonArray jsonarray2 = jsonobject.getAsJsonArray("extra");

                    if (jsonarray2.size() <= 0) {
                        throw new JsonParseException("Unexpected empty array of components");
                    }

                    for (int j = 0; j < jsonarray2.size(); ++j) {
                        ((ITextComponent) object).func_150257_a(this.deserialize(jsonarray2.get(j), type, jsondeserializationcontext));
                    }
                }

                ((ITextComponent) object).func_150255_a((Style) jsondeserializationcontext.deserialize(jsonelement, Style.class));
                return (ITextComponent) object;
            }
        }

        private void func_150695_a(Style chatmodifier, JsonObject jsonobject, JsonSerializationContext jsonserializationcontext) {
            JsonElement jsonelement = jsonserializationcontext.serialize(chatmodifier);

            if (jsonelement.isJsonObject()) {
                JsonObject jsonobject1 = (JsonObject) jsonelement;
                Iterator iterator = jsonobject1.entrySet().iterator();

                while (iterator.hasNext()) {
                    Entry entry = (Entry) iterator.next();

                    jsonobject.add((String) entry.getKey(), (JsonElement) entry.getValue());
                }
            }

        }

        public JsonElement serialize(ITextComponent ichatbasecomponent, Type type, JsonSerializationContext jsonserializationcontext) {
            JsonObject jsonobject = new JsonObject();

            if (!ichatbasecomponent.func_150256_b().func_150229_g()) {
                this.func_150695_a(ichatbasecomponent.func_150256_b(), jsonobject, jsonserializationcontext);
            }

            if (!ichatbasecomponent.func_150253_a().isEmpty()) {
                JsonArray jsonarray = new JsonArray();
                Iterator iterator = ichatbasecomponent.func_150253_a().iterator();

                while (iterator.hasNext()) {
                    ITextComponent ichatbasecomponent1 = (ITextComponent) iterator.next();

                    jsonarray.add(this.serialize(ichatbasecomponent1, (Type) ichatbasecomponent1.getClass(), jsonserializationcontext));
                }

                jsonobject.add("extra", jsonarray);
            }

            if (ichatbasecomponent instanceof TextComponentString) {
                jsonobject.addProperty("text", ((TextComponentString) ichatbasecomponent).func_150265_g());
            } else if (ichatbasecomponent instanceof TextComponentTranslation) {
                TextComponentTranslation chatmessage = (TextComponentTranslation) ichatbasecomponent;

                jsonobject.addProperty("translate", chatmessage.func_150268_i());
                if (chatmessage.func_150271_j() != null && chatmessage.func_150271_j().length > 0) {
                    JsonArray jsonarray1 = new JsonArray();
                    Object[] aobject = chatmessage.func_150271_j();
                    int i = aobject.length;

                    for (int j = 0; j < i; ++j) {
                        Object object = aobject[j];

                        if (object instanceof ITextComponent) {
                            jsonarray1.add(this.serialize((ITextComponent) object, (Type) object.getClass(), jsonserializationcontext));
                        } else {
                            jsonarray1.add(new JsonPrimitive(String.valueOf(object)));
                        }
                    }

                    jsonobject.add("with", jsonarray1);
                }
            } else if (ichatbasecomponent instanceof TextComponentScore) {
                TextComponentScore chatcomponentscore = (TextComponentScore) ichatbasecomponent;
                JsonObject jsonobject1 = new JsonObject();

                jsonobject1.addProperty("name", chatcomponentscore.func_179995_g());
                jsonobject1.addProperty("objective", chatcomponentscore.func_179994_h());
                jsonobject1.addProperty("value", chatcomponentscore.func_150261_e());
                jsonobject.add("score", jsonobject1);
            } else if (ichatbasecomponent instanceof TextComponentSelector) {
                TextComponentSelector chatcomponentselector = (TextComponentSelector) ichatbasecomponent;

                jsonobject.addProperty("selector", chatcomponentselector.func_179992_g());
            } else {
                if (!(ichatbasecomponent instanceof TextComponentKeybind)) {
                    throw new IllegalArgumentException("Don\'t know how to serialize " + ichatbasecomponent + " as a Component");
                }

                TextComponentKeybind chatcomponentkeybind = (TextComponentKeybind) ichatbasecomponent;

                jsonobject.addProperty("keybind", chatcomponentkeybind.func_193633_h());
            }

            return jsonobject;
        }

        public static String func_150696_a(ITextComponent ichatbasecomponent) {
            return ITextComponent.Serializer.field_150700_a.toJson(ichatbasecomponent);
        }

        @Nullable
        public static ITextComponent func_150699_a(String s) {
            return (ITextComponent) JsonUtils.func_188176_a(ITextComponent.Serializer.field_150700_a, s, ITextComponent.class, false);
        }

        @Nullable
        public static ITextComponent func_186877_b(String s) {
            return (ITextComponent) JsonUtils.func_188176_a(ITextComponent.Serializer.field_150700_a, s, ITextComponent.class, true);
        }

        public JsonElement serialize(Object object, Type type, JsonSerializationContext jsonserializationcontext) {
            return this.serialize((ITextComponent) object, type, jsonserializationcontext);
        }

        public Object deserialize(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
            return this.deserialize(jsonelement, type, jsondeserializationcontext);
        }

        static {
            GsonBuilder gsonbuilder = new GsonBuilder();

            gsonbuilder.registerTypeHierarchyAdapter(ITextComponent.class, new ITextComponent.Serializer());
            gsonbuilder.registerTypeHierarchyAdapter(Style.class, new Style.Serializer());
            gsonbuilder.registerTypeAdapterFactory(new EnumTypeAdapterFactory());
            field_150700_a = gsonbuilder.create();
        }
    }
}
