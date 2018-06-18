package net.minecraft.advancements.critereon;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.CriterionTriggerTick.a;
import net.minecraft.server.CriterionTriggerTick.b;
import net.minecraft.util.ResourceLocation;

public class TickTrigger implements ICriterionTrigger<CriterionTriggerTick.b> {

    public static final ResourceLocation ID = new ResourceLocation("tick");
    private final Map<PlayerAdvancements, CriterionTriggerTick.a> listeners = Maps.newHashMap();

    public TickTrigger() {}

    public ResourceLocation getId() {
        return TickTrigger.ID;
    }

    public void a(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerTick.b> criteriontrigger_a) {
        CriterionTriggerTick.a criteriontriggertick_a = (CriterionTriggerTick.a) this.listeners.get(advancementdataplayer);

        if (criteriontriggertick_a == null) {
            criteriontriggertick_a = new CriterionTriggerTick.a(advancementdataplayer);
            this.listeners.put(advancementdataplayer, criteriontriggertick_a);
        }

        criteriontriggertick_a.a(criteriontrigger_a);
    }

    public void b(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerTick.b> criteriontrigger_a) {
        CriterionTriggerTick.a criteriontriggertick_a = (CriterionTriggerTick.a) this.listeners.get(advancementdataplayer);

        if (criteriontriggertick_a != null) {
            criteriontriggertick_a.b(criteriontrigger_a);
            if (criteriontriggertick_a.a()) {
                this.listeners.remove(advancementdataplayer);
            }
        }

    }

    public void removeAllListeners(PlayerAdvancements advancementdataplayer) {
        this.listeners.remove(advancementdataplayer);
    }

    public CriterionTriggerTick.b b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        return new CriterionTriggerTick.b();
    }

    public void trigger(EntityPlayerMP entityplayer) {
        CriterionTriggerTick.a criteriontriggertick_a = (CriterionTriggerTick.a) this.listeners.get(entityplayer.getAdvancements());

        if (criteriontriggertick_a != null) {
            criteriontriggertick_a.b();
        }

    }

    public ICriterionInstance deserializeInstance(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        return this.b(jsonobject, jsondeserializationcontext);
    }

    static class a {

        private final PlayerAdvancements a;
        private final Set<CriterionTrigger.a<CriterionTriggerTick.b>> b = Sets.newHashSet();

        public a(PlayerAdvancements advancementdataplayer) {
            this.a = advancementdataplayer;
        }

        public boolean a() {
            return this.b.isEmpty();
        }

        public void a(CriterionTrigger.a<CriterionTriggerTick.b> criteriontrigger_a) {
            this.b.add(criteriontrigger_a);
        }

        public void b(CriterionTrigger.a<CriterionTriggerTick.b> criteriontrigger_a) {
            this.b.remove(criteriontrigger_a);
        }

        public void b() {
            Iterator iterator = Lists.newArrayList(this.b).iterator();

            while (iterator.hasNext()) {
                CriterionTrigger.a criteriontrigger_a = (CriterionTrigger.a) iterator.next();

                criteriontrigger_a.a(this.a);
            }

        }
    }

    public static class b extends AbstractCriterionInstance {

        public b() {
            super(TickTrigger.ID);
        }
    }
}
