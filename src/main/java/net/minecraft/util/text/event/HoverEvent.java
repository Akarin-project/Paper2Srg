package net.minecraft.util.text.event;

import com.google.common.collect.Maps;
import java.util.Map;

import net.minecraft.util.text.ITextComponent;

public class HoverEvent {

    private final HoverEvent.Action action;
    private final ITextComponent value;

    public HoverEvent(HoverEvent.Action chathoverable_enumhoveraction, ITextComponent ichatbasecomponent) {
        this.action = chathoverable_enumhoveraction;
        this.value = ichatbasecomponent;
    }

    public HoverEvent.Action getAction() {
        return this.action;
    }

    public ITextComponent getValue() {
        return this.value;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object != null && this.getClass() == object.getClass()) {
            HoverEvent chathoverable = (HoverEvent) object;

            if (this.action != chathoverable.action) {
                return false;
            } else {
                if (this.value != null) {
                    if (!this.value.equals(chathoverable.value)) {
                        return false;
                    }
                } else if (chathoverable.value != null) {
                    return false;
                }

                return true;
            }
        } else {
            return false;
        }
    }

    public String toString() {
        return "HoverEvent{action=" + this.action + ", value=\'" + this.value + '\'' + '}';
    }

    public int hashCode() {
        int i = this.action.hashCode();

        i = 31 * i + (this.value != null ? this.value.hashCode() : 0);
        return i;
    }

    public static enum Action {

        SHOW_TEXT("show_text", true), SHOW_ITEM("show_item", true), SHOW_ENTITY("show_entity", true);

        private static final Map<String, HoverEvent.Action> NAME_MAPPING = Maps.newHashMap();
        private final boolean allowedInChat;
        private final String canonicalName;

        private Action(String s, boolean flag) {
            this.canonicalName = s;
            this.allowedInChat = flag;
        }

        public boolean shouldAllowInChat() {
            return this.allowedInChat;
        }

        public String getCanonicalName() {
            return this.canonicalName;
        }

        public static HoverEvent.Action getValueByCanonicalName(String s) {
            return (HoverEvent.Action) HoverEvent.Action.NAME_MAPPING.get(s);
        }

        static {
            HoverEvent.Action[] achathoverable_enumhoveraction = values();
            int i = achathoverable_enumhoveraction.length;

            for (int j = 0; j < i; ++j) {
                HoverEvent.Action chathoverable_enumhoveraction = achathoverable_enumhoveraction[j];

                HoverEvent.Action.NAME_MAPPING.put(chathoverable_enumhoveraction.getCanonicalName(), chathoverable_enumhoveraction);
            }

        }
    }
}
