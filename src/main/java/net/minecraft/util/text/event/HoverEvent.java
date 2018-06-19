package net.minecraft.util.text.event;

import com.google.common.collect.Maps;
import java.util.Map;

import net.minecraft.util.text.ITextComponent;

public class HoverEvent {

    private final HoverEvent.Action field_150704_a;
    private final ITextComponent field_150703_b;

    public HoverEvent(HoverEvent.Action chathoverable_enumhoveraction, ITextComponent ichatbasecomponent) {
        this.field_150704_a = chathoverable_enumhoveraction;
        this.field_150703_b = ichatbasecomponent;
    }

    public HoverEvent.Action func_150701_a() {
        return this.field_150704_a;
    }

    public ITextComponent func_150702_b() {
        return this.field_150703_b;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object != null && this.getClass() == object.getClass()) {
            HoverEvent chathoverable = (HoverEvent) object;

            if (this.field_150704_a != chathoverable.field_150704_a) {
                return false;
            } else {
                if (this.field_150703_b != null) {
                    if (!this.field_150703_b.equals(chathoverable.field_150703_b)) {
                        return false;
                    }
                } else if (chathoverable.field_150703_b != null) {
                    return false;
                }

                return true;
            }
        } else {
            return false;
        }
    }

    public String toString() {
        return "HoverEvent{action=" + this.field_150704_a + ", value=\'" + this.field_150703_b + '\'' + '}';
    }

    public int hashCode() {
        int i = this.field_150704_a.hashCode();

        i = 31 * i + (this.field_150703_b != null ? this.field_150703_b.hashCode() : 0);
        return i;
    }

    public static enum Action {

        SHOW_TEXT("show_text", true), SHOW_ITEM("show_item", true), SHOW_ENTITY("show_entity", true);

        private static final Map<String, HoverEvent.Action> field_150690_d = Maps.newHashMap();
        private final boolean field_150691_e;
        private final String field_150688_f;

        private Action(String s, boolean flag) {
            this.field_150688_f = s;
            this.field_150691_e = flag;
        }

        public boolean func_150686_a() {
            return this.field_150691_e;
        }

        public String func_150685_b() {
            return this.field_150688_f;
        }

        public static HoverEvent.Action func_150684_a(String s) {
            return (HoverEvent.Action) HoverEvent.Action.field_150690_d.get(s);
        }

        static {
            HoverEvent.Action[] achathoverable_enumhoveraction = values();
            int i = achathoverable_enumhoveraction.length;

            for (int j = 0; j < i; ++j) {
                HoverEvent.Action chathoverable_enumhoveraction = achathoverable_enumhoveraction[j];

                HoverEvent.Action.field_150690_d.put(chathoverable_enumhoveraction.func_150685_b(), chathoverable_enumhoveraction);
            }

        }
    }
}
