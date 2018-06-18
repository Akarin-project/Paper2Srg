package net.minecraft.advancements.critereon;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.CriterionTriggerSummonedEntity.a;
import net.minecraft.server.CriterionTriggerSummonedEntity.b;
import net.minecraft.util.ResourceLocation;

public class SummonedEntityTrigger implements ICriterionTrigger<CriterionTriggerSummonedEntity.b> {

    private static final ResourceLocation ID = new ResourceLocation("summoned_entity");
    private final Map<PlayerAdvancements, CriterionTriggerSummonedEntity.a> listeners = Maps.newHashMap();

    public SummonedEntityTrigger() {}

    public ResourceLocation getId() {
        return SummonedEntityTrigger.ID;
    }

    public void a(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerSummonedEntity.b> criteriontrigger_a) {
        CriterionTriggerSummonedEntity.a criteriontriggersummonedentity_a = (CriterionTriggerSummonedEntity.a) this.listeners.get(advancementdataplayer);

        if (criteriontriggersummonedentity_a == null) {
            criteriontriggersummonedentity_a = new CriterionTriggerSummonedEntity.a(advancementdataplayer);
            this.listeners.put(advancementdataplayer, criteriontriggersummonedentity_a);
        }

        criteriontriggersummonedentity_a.a(criteriontrigger_a);
    }

    public void b(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerSummonedEntity.b> criteriontrigger_a) {
        CriterionTriggerSummonedEntity.a criteriontriggersummonedentity_a = (CriterionTriggerSummonedEntity.a) this.listeners.get(advancementdataplayer);

        if (criteriontriggersummonedentity_a != null) {
            criteriontriggersummonedentity_a.b(criteriontrigger_a);
            if (criteriontriggersummonedentity_a.a()) {
                this.listeners.remove(advancementdataplayer);
            }
        }

    }

    public void removeAllListeners(PlayerAdvancements advancementdataplayer) {
        this.listeners.remove(advancementdataplayer);
    }

    public CriterionTriggerSummonedEntity.b b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        EntityPredicate criterionconditionentity = EntityPredicate.deserialize(jsonobject.get("entity"));

        return new CriterionTriggerSummonedEntity.b(criterionconditionentity);
    }

    public void trigger(EntityPlayerMP entityplayer, Entity entity) {
        CriterionTriggerSummonedEntity.a criteriontriggersummonedentity_a = (CriterionTriggerSummonedEntity.a) this.listeners.get(entityplayer.getAdvancements());

        if (criteriontriggersummonedentity_a != null) {
            criteriontriggersummonedentity_a.a(entityplayer, entity);
        }

    }

    public ICriterionInstance deserializeInstance(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        return this.b(jsonobject, jsondeserializationcontext);
    }

    static class a {

        private final PlayerAdvancements a;
        private final Set<CriterionTrigger.a<CriterionTriggerSummonedEntity.b>> b = Sets.newHashSet();

        public a(PlayerAdvancements advancementdataplayer) {
            this.a = advancementdataplayer;
        }

        public boolean a() {
            return this.b.isEmpty();
        }

        public void a(CriterionTrigger.a<CriterionTriggerSummonedEntity.b> criteriontrigger_a) {
            this.b.add(criteriontrigger_a);
        }

        public void b(CriterionTrigger.a<CriterionTriggerSummonedEntity.b> criteriontrigger_a) {
            this.b.remove(criteriontrigger_a);
        }

        public void a(EntityPlayerMP entityplayer, Entity entity) {
            ArrayList arraylist = null;
            Iterator iterator = this.b.iterator();

            CriterionTrigger.a criteriontrigger_a;

            while (iterator.hasNext()) {
                criteriontrigger_a = (CriterionTrigger.a) iterator.next();
                if (((CriterionTriggerSummonedEntity.b) criteriontrigger_a.a()).a(entityplayer, entity)) {
                    if (arraylist == null) {
                        arraylist = Lists.newArrayList();
                    }

                    arraylist.add(criteriontrigger_a);
                }
            }

            if (arraylist != null) {
                iterator = arraylist.iterator();

                while (iterator.hasNext()) {
                    criteriontrigger_a = (CriterionTrigger.a) iterator.next();
                    criteriontrigger_a.a(this.a);
                }
            }

        }
    }

    public static class b extends AbstractCriterionInstance {

        private final EntityPredicate a;

        public b(EntityPredicate criterionconditionentity) {
            super(SummonedEntityTrigger.ID);
            this.a = criterionconditionentity;
        }

        public boolean a(EntityPlayerMP entityplayer, Entity entity) {
            return this.a.test(entityplayer, entity);
        }
    }
}
