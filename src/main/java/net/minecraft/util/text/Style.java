package net.minecraft.util.text;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import javax.annotation.Nullable;

import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

public class Style {

    private Style parentStyle;
    private TextFormatting color;
    private Boolean bold;
    private Boolean italic;
    private Boolean underlined;
    private Boolean strikethrough;
    private Boolean obfuscated;
    private ClickEvent clickEvent;
    private HoverEvent hoverEvent;
    private String insertion;
    private static final Style ROOT = new Style() {
        @Override
        @Nullable
        public TextFormatting getColor() {
            return null;
        }

        @Override
        public boolean getBold() {
            return false;
        }

        @Override
        public boolean getItalic() {
            return false;
        }

        @Override
        public boolean getStrikethrough() {
            return false;
        }

        @Override
        public boolean getUnderlined() {
            return false;
        }

        @Override
        public boolean getObfuscated() {
            return false;
        }

        @Override
        @Nullable
        public ClickEvent getClickEvent() {
            return null;
        }

        @Override
        @Nullable
        public HoverEvent getHoverEvent() {
            return null;
        }

        @Override
        @Nullable
        public String getInsertion() {
            return null;
        }

        @Override
        public Style setColor(TextFormatting enumchatformat) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Style setBold(Boolean obool) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Style setItalic(Boolean obool) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Style setStrikethrough(Boolean obool) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Style setUnderlined(Boolean obool) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Style setObfuscated(Boolean obool) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Style setClickEvent(ClickEvent chatclickable) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Style setHoverEvent(HoverEvent chathoverable) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Style setParentStyle(Style chatmodifier) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String toString() {
            return "Style.ROOT";
        }

        @Override
        public Style clone() {
            return this;
        }

        @Override
        public Style createDeepCopy() {
            return this;
        }
    };

    public Style() {}

    @Nullable
    public TextFormatting getColor() {
        return this.color == null ? this.getParent().getColor() : this.color;
    }

    public boolean getBold() {
        return this.bold == null ? this.getParent().getBold() : this.bold.booleanValue();
    }

    public boolean getItalic() {
        return this.italic == null ? this.getParent().getItalic() : this.italic.booleanValue();
    }

    public boolean getStrikethrough() {
        return this.strikethrough == null ? this.getParent().getStrikethrough() : this.strikethrough.booleanValue();
    }

    public boolean getUnderlined() {
        return this.underlined == null ? this.getParent().getUnderlined() : this.underlined.booleanValue();
    }

    public boolean getObfuscated() {
        return this.obfuscated == null ? this.getParent().getObfuscated() : this.obfuscated.booleanValue();
    }

    public boolean isEmpty() {
        return this.bold == null && this.italic == null && this.strikethrough == null && this.underlined == null && this.obfuscated == null && this.color == null && this.clickEvent == null && this.hoverEvent == null && this.insertion == null;
    }

    @Nullable
    public ClickEvent getClickEvent() {
        return this.clickEvent == null ? this.getParent().getClickEvent() : this.clickEvent;
    }

    @Nullable
    public HoverEvent getHoverEvent() {
        return this.hoverEvent == null ? this.getParent().getHoverEvent() : this.hoverEvent;
    }

    @Nullable
    public String getInsertion() {
        return this.insertion == null ? this.getParent().getInsertion() : this.insertion;
    }

    public Style setColor(TextFormatting enumchatformat) {
        this.color = enumchatformat;
        return this;
    }

    public Style setBold(Boolean obool) {
        this.bold = obool;
        return this;
    }

    public Style setItalic(Boolean obool) {
        this.italic = obool;
        return this;
    }

    public Style setStrikethrough(Boolean obool) {
        this.strikethrough = obool;
        return this;
    }

    public Style setUnderlined(Boolean obool) {
        this.underlined = obool;
        return this;
    }

    public Style setObfuscated(Boolean obool) {
        this.obfuscated = obool;
        return this;
    }

    public Style setClickEvent(ClickEvent chatclickable) {
        this.clickEvent = chatclickable;
        return this;
    }

    public Style setHoverEvent(HoverEvent chathoverable) {
        this.hoverEvent = chathoverable;
        return this;
    }

    public Style setInsertion(String s) {
        this.insertion = s;
        return this;
    }

    public Style setParentStyle(Style chatmodifier) {
        this.parentStyle = chatmodifier;
        return this;
    }

    private Style getParent() {
        return this.parentStyle == null ? Style.ROOT : this.parentStyle;
    }

    @Override
    public String toString() {
        return "Style{hasParent=" + (this.parentStyle != null) + ", color=" + this.color + ", bold=" + this.bold + ", italic=" + this.italic + ", underlined=" + this.underlined + ", obfuscated=" + this.obfuscated + ", clickEvent=" + this.getClickEvent() + ", hoverEvent=" + this.getHoverEvent() + ", insertion=" + this.getInsertion() + '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof Style)) {
            return false;
        } else {
            Style chatmodifier = (Style) object;
            boolean flag;

            if (this.getBold() == chatmodifier.getBold() && this.getColor() == chatmodifier.getColor() && this.getItalic() == chatmodifier.getItalic() && this.getObfuscated() == chatmodifier.getObfuscated() && this.getStrikethrough() == chatmodifier.getStrikethrough() && this.getUnderlined() == chatmodifier.getUnderlined()) {
                label65: {
                    if (this.getClickEvent() != null) {
                        if (!this.getClickEvent().equals(chatmodifier.getClickEvent())) {
                            break label65;
                        }
                    } else if (chatmodifier.getClickEvent() != null) {
                        break label65;
                    }

                    if (this.getHoverEvent() != null) {
                        if (!this.getHoverEvent().equals(chatmodifier.getHoverEvent())) {
                            break label65;
                        }
                    } else if (chatmodifier.getHoverEvent() != null) {
                        break label65;
                    }

                    if (this.getInsertion() != null) {
                        if (!this.getInsertion().equals(chatmodifier.getInsertion())) {
                            break label65;
                        }
                    } else if (chatmodifier.getInsertion() != null) {
                        break label65;
                    }

                    flag = true;
                    return flag;
                }
            }

            flag = false;
            return flag;
        }
    }

    @Override
    public int hashCode() {
        // CraftBukkit start - fix npe
        int i = color == null ? 0 : this.color.hashCode();

        i = 31 * i + (bold == null ? 0 : this.bold.hashCode());
        i = 31 * i + (italic == null ? 0 : this.italic.hashCode());
        i = 31 * i + (underlined == null ? 0 : this.underlined.hashCode());
        i = 31 * i + (strikethrough == null ? 0 : this.strikethrough.hashCode());
        i = 31 * i + (obfuscated == null ? 0 : this.obfuscated.hashCode());
        i = 31 * i + (clickEvent == null ? 0 : this.clickEvent.hashCode());
        i = 31 * i + (this.hoverEvent == null ? 0 : this.hoverEvent.hashCode());
        i = 31 * i + (insertion == null ? 0 : this.insertion.hashCode());
        // CraftBukkit end
        return i;
    }

    public Style createShallowCopy() {
        Style chatmodifier = new Style();

        chatmodifier.bold = this.bold;
        chatmodifier.italic = this.italic;
        chatmodifier.strikethrough = this.strikethrough;
        chatmodifier.underlined = this.underlined;
        chatmodifier.obfuscated = this.obfuscated;
        chatmodifier.color = this.color;
        chatmodifier.clickEvent = this.clickEvent;
        chatmodifier.hoverEvent = this.hoverEvent;
        chatmodifier.parentStyle = this.parentStyle;
        chatmodifier.insertion = this.insertion;
        return chatmodifier;
    }

    public Style createDeepCopy() {
        Style chatmodifier = new Style();

        chatmodifier.setBold(Boolean.valueOf(this.getBold()));
        chatmodifier.setItalic(Boolean.valueOf(this.getItalic()));
        chatmodifier.setStrikethrough(Boolean.valueOf(this.getStrikethrough()));
        chatmodifier.setUnderlined(Boolean.valueOf(this.getUnderlined()));
        chatmodifier.setObfuscated(Boolean.valueOf(this.getObfuscated()));
        chatmodifier.setColor(this.getColor());
        chatmodifier.setClickEvent(this.getClickEvent());
        chatmodifier.setHoverEvent(this.getHoverEvent());
        chatmodifier.setInsertion(this.getInsertion());
        return chatmodifier;
    }

    public static class Serializer implements JsonDeserializer<Style>, JsonSerializer<Style> {

        public Serializer() {}

        @Override
        @Nullable
        public Style deserialize(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
            if (jsonelement.isJsonObject()) {
                Style chatmodifier = new Style();
                JsonObject jsonobject = jsonelement.getAsJsonObject();

                if (jsonobject == null) {
                    return null;
                } else {
                    if (jsonobject.has("bold")) {
                        chatmodifier.bold = Boolean.valueOf(jsonobject.get("bold").getAsBoolean());
                    }

                    if (jsonobject.has("italic")) {
                        chatmodifier.italic = Boolean.valueOf(jsonobject.get("italic").getAsBoolean());
                    }

                    if (jsonobject.has("underlined")) {
                        chatmodifier.underlined = Boolean.valueOf(jsonobject.get("underlined").getAsBoolean());
                    }

                    if (jsonobject.has("strikethrough")) {
                        chatmodifier.strikethrough = Boolean.valueOf(jsonobject.get("strikethrough").getAsBoolean());
                    }

                    if (jsonobject.has("obfuscated")) {
                        chatmodifier.obfuscated = Boolean.valueOf(jsonobject.get("obfuscated").getAsBoolean());
                    }

                    if (jsonobject.has("color")) {
                        chatmodifier.color = (TextFormatting) jsondeserializationcontext.deserialize(jsonobject.get("color"), TextFormatting.class);
                    }

                    if (jsonobject.has("insertion")) {
                        chatmodifier.insertion = jsonobject.get("insertion").getAsString();
                    }

                    JsonObject jsonobject1;
                    JsonPrimitive jsonprimitive;

                    if (jsonobject.has("clickEvent")) {
                        jsonobject1 = jsonobject.getAsJsonObject("clickEvent");
                        if (jsonobject1 != null) {
                            jsonprimitive = jsonobject1.getAsJsonPrimitive("action");
                            ClickEvent.Action chatclickable_enumclickaction = jsonprimitive == null ? null : ClickEvent.Action.getValueByCanonicalName(jsonprimitive.getAsString());
                            JsonPrimitive jsonprimitive1 = jsonobject1.getAsJsonPrimitive("value");
                            String s = jsonprimitive1 == null ? null : jsonprimitive1.getAsString();

                            if (chatclickable_enumclickaction != null && s != null && chatclickable_enumclickaction.shouldAllowInChat()) {
                                chatmodifier.clickEvent = new ClickEvent(chatclickable_enumclickaction, s);
                            }
                        }
                    }

                    if (jsonobject.has("hoverEvent")) {
                        jsonobject1 = jsonobject.getAsJsonObject("hoverEvent");
                        if (jsonobject1 != null) {
                            jsonprimitive = jsonobject1.getAsJsonPrimitive("action");
                            HoverEvent.Action chathoverable_enumhoveraction = jsonprimitive == null ? null : HoverEvent.Action.getValueByCanonicalName(jsonprimitive.getAsString());
                            ITextComponent ichatbasecomponent = (ITextComponent) jsondeserializationcontext.deserialize(jsonobject1.get("value"), ITextComponent.class);

                            if (chathoverable_enumhoveraction != null && ichatbasecomponent != null && chathoverable_enumhoveraction.shouldAllowInChat()) {
                                chatmodifier.hoverEvent = new HoverEvent(chathoverable_enumhoveraction, ichatbasecomponent);
                            }
                        }
                    }

                    return chatmodifier;
                }
            } else {
                return null;
            }
        }

        @Override
        @Nullable
        public JsonElement serialize(Style chatmodifier, Type type, JsonSerializationContext jsonserializationcontext) {
            if (chatmodifier.isEmpty()) {
                return null;
            } else {
                JsonObject jsonobject = new JsonObject();

                if (chatmodifier.bold != null) {
                    jsonobject.addProperty("bold", chatmodifier.bold);
                }

                if (chatmodifier.italic != null) {
                    jsonobject.addProperty("italic", chatmodifier.italic);
                }

                if (chatmodifier.underlined != null) {
                    jsonobject.addProperty("underlined", chatmodifier.underlined);
                }

                if (chatmodifier.strikethrough != null) {
                    jsonobject.addProperty("strikethrough", chatmodifier.strikethrough);
                }

                if (chatmodifier.obfuscated != null) {
                    jsonobject.addProperty("obfuscated", chatmodifier.obfuscated);
                }

                if (chatmodifier.color != null) {
                    jsonobject.add("color", jsonserializationcontext.serialize(chatmodifier.color));
                }

                if (chatmodifier.insertion != null) {
                    jsonobject.add("insertion", jsonserializationcontext.serialize(chatmodifier.insertion));
                }

                JsonObject jsonobject1;

                if (chatmodifier.clickEvent != null) {
                    jsonobject1 = new JsonObject();
                    jsonobject1.addProperty("action", chatmodifier.clickEvent.getAction().getCanonicalName());
                    jsonobject1.addProperty("value", chatmodifier.clickEvent.getValue());
                    jsonobject.add("clickEvent", jsonobject1);
                }

                if (chatmodifier.hoverEvent != null) {
                    jsonobject1 = new JsonObject();
                    jsonobject1.addProperty("action", chatmodifier.hoverEvent.getAction().getCanonicalName());
                    jsonobject1.add("value", jsonserializationcontext.serialize(chatmodifier.hoverEvent.getValue()));
                    jsonobject.add("hoverEvent", jsonobject1);
                }

                return jsonobject;
            }
        }
    }
}
