package net.minecraft.util.text.event;

import com.google.common.collect.Maps;
import java.util.Map;

public class ClickEvent {

    private final ClickEvent.Action field_150671_a;
    private final String field_150670_b;

    public ClickEvent(ClickEvent.Action chatclickable_enumclickaction, String s) {
        this.field_150671_a = chatclickable_enumclickaction;
        this.field_150670_b = s;
    }

    public ClickEvent.Action func_150669_a() {
        return this.field_150671_a;
    }

    public String func_150668_b() {
        return this.field_150670_b;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object != null && this.getClass() == object.getClass()) {
            ClickEvent chatclickable = (ClickEvent) object;

            if (this.field_150671_a != chatclickable.field_150671_a) {
                return false;
            } else {
                if (this.field_150670_b != null) {
                    if (!this.field_150670_b.equals(chatclickable.field_150670_b)) {
                        return false;
                    }
                } else if (chatclickable.field_150670_b != null) {
                    return false;
                }

                return true;
            }
        } else {
            return false;
        }
    }

    public String toString() {
        return "ClickEvent{action=" + this.field_150671_a + ", value=\'" + this.field_150670_b + '\'' + '}';
    }

    public int hashCode() {
        int i = this.field_150671_a.hashCode();

        i = 31 * i + (this.field_150670_b != null ? this.field_150670_b.hashCode() : 0);
        return i;
    }

    public static enum Action {

        OPEN_URL("open_url", true), OPEN_FILE("open_file", false), RUN_COMMAND("run_command", true), SUGGEST_COMMAND("suggest_command", true), CHANGE_PAGE("change_page", true);

        private static final Map<String, ClickEvent.Action> field_150679_e = Maps.newHashMap();
        private final boolean field_150676_f;
        private final String field_150677_g;

        private Action(String s, boolean flag) {
            this.field_150677_g = s;
            this.field_150676_f = flag;
        }

        public boolean func_150674_a() {
            return this.field_150676_f;
        }

        public String func_150673_b() {
            return this.field_150677_g;
        }

        public static ClickEvent.Action func_150672_a(String s) {
            return (ClickEvent.Action) ClickEvent.Action.field_150679_e.get(s);
        }

        static {
            ClickEvent.Action[] achatclickable_enumclickaction = values();
            int i = achatclickable_enumclickaction.length;

            for (int j = 0; j < i; ++j) {
                ClickEvent.Action chatclickable_enumclickaction = achatclickable_enumclickaction[j];

                ClickEvent.Action.field_150679_e.put(chatclickable_enumclickaction.func_150673_b(), chatclickable_enumclickaction);
            }

        }
    }
}
