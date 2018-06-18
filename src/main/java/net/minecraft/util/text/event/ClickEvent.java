package net.minecraft.util.text.event;

import com.google.common.collect.Maps;
import java.util.Map;

public class ClickEvent {

    private final ClickEvent.Action action;
    private final String value;

    public ClickEvent(ClickEvent.Action chatclickable_enumclickaction, String s) {
        this.action = chatclickable_enumclickaction;
        this.value = s;
    }

    public ClickEvent.Action getAction() {
        return this.action;
    }

    public String getValue() {
        return this.value;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object != null && this.getClass() == object.getClass()) {
            ClickEvent chatclickable = (ClickEvent) object;

            if (this.action != chatclickable.action) {
                return false;
            } else {
                if (this.value != null) {
                    if (!this.value.equals(chatclickable.value)) {
                        return false;
                    }
                } else if (chatclickable.value != null) {
                    return false;
                }

                return true;
            }
        } else {
            return false;
        }
    }

    public String toString() {
        return "ClickEvent{action=" + this.action + ", value=\'" + this.value + '\'' + '}';
    }

    public int hashCode() {
        int i = this.action.hashCode();

        i = 31 * i + (this.value != null ? this.value.hashCode() : 0);
        return i;
    }

    public static enum Action {

        OPEN_URL("open_url", true), OPEN_FILE("open_file", false), RUN_COMMAND("run_command", true), SUGGEST_COMMAND("suggest_command", true), CHANGE_PAGE("change_page", true);

        private static final Map<String, ClickEvent.Action> NAME_MAPPING = Maps.newHashMap();
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

        public static ClickEvent.Action getValueByCanonicalName(String s) {
            return (ClickEvent.Action) ClickEvent.Action.NAME_MAPPING.get(s);
        }

        static {
            ClickEvent.Action[] achatclickable_enumclickaction = values();
            int i = achatclickable_enumclickaction.length;

            for (int j = 0; j < i; ++j) {
                ClickEvent.Action chatclickable_enumclickaction = achatclickable_enumclickaction[j];

                ClickEvent.Action.NAME_MAPPING.put(chatclickable_enumclickaction.getCanonicalName(), chatclickable_enumclickaction);
            }

        }
    }
}
