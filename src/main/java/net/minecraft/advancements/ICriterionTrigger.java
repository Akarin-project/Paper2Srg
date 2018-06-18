package net.minecraft.advancements;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import net.minecraft.server.CriterionTrigger;
import net.minecraft.server.CriterionTrigger.a;
import net.minecraft.util.ResourceLocation;

public interface ICriterionTrigger<T extends ICriterionInstance> {

    ResourceLocation getId();

    void a(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<T> criteriontrigger_a);

    void b(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<T> criteriontrigger_a);

    void removeAllListeners(PlayerAdvancements advancementdataplayer);

    T deserializeInstance(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext);

    public static class a<T extends ICriterionInstance> {

        private final T a;
        private final Advancement b;
        private final String c;

        public a(T t0, Advancement advancement, String s) {
            this.a = t0;
            this.b = advancement;
            this.c = s;
        }

        public T a() {
            return this.a;
        }

        public void a(PlayerAdvancements advancementdataplayer) {
            advancementdataplayer.grantCriterion(this.b, this.c);
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) {
                return true;
            } else if (object != null && this.getClass() == object.getClass()) {
                CriterionTrigger.a criteriontrigger_a = (CriterionTrigger.a) object;

                return !this.a.equals(criteriontrigger_a.a) ? false : (!this.b.equals(criteriontrigger_a.b) ? false : this.c.equals(criteriontrigger_a.c));
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            int i = this.a.hashCode();

            i = 31 * i + this.b.hashCode();
            i = 31 * i + this.c.hashCode();
            return i;
        }
    }
}
