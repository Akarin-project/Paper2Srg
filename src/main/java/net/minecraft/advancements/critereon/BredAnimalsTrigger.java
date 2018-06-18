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
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.CriterionTrigger;
import net.minecraft.server.CriterionTriggerBredAnimals;
import net.minecraft.server.CriterionTriggerBredAnimals.a;
import net.minecraft.server.CriterionTriggerBredAnimals.b;
import net.minecraft.util.ResourceLocation;

public class BredAnimalsTrigger implements ICriterionTrigger<CriterionTriggerBredAnimals.b> {

    private static final ResourceLocation ID = new ResourceLocation("bred_animals");
    private final Map<PlayerAdvancements, CriterionTriggerBredAnimals.a> listeners = Maps.newHashMap();

    public BredAnimalsTrigger() {}

    @Override
    public ResourceLocation getId() {
        return BredAnimalsTrigger.ID;
    }

    @Override
    public void a(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerBredAnimals.b> criteriontrigger_a) {
        CriterionTriggerBredAnimals.a criteriontriggerbredanimals_a = this.listeners.get(advancementdataplayer);

        if (criteriontriggerbredanimals_a == null) {
            criteriontriggerbredanimals_a = new CriterionTriggerBredAnimals.a(advancementdataplayer);
            this.listeners.put(advancementdataplayer, criteriontriggerbredanimals_a);
        }

        criteriontriggerbredanimals_a.a(criteriontrigger_a);
    }

    @Override
    public void b(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerBredAnimals.b> criteriontrigger_a) {
        CriterionTriggerBredAnimals.a criteriontriggerbredanimals_a = this.listeners.get(advancementdataplayer);

        if (criteriontriggerbredanimals_a != null) {
            criteriontriggerbredanimals_a.b(criteriontrigger_a);
            if (criteriontriggerbredanimals_a.a()) {
                this.listeners.remove(advancementdataplayer);
            }
        }

    }

    @Override
    public void removeAllListeners(PlayerAdvancements advancementdataplayer) {
        this.listeners.remove(advancementdataplayer);
    }

    public CriterionTriggerBredAnimals.b b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        EntityPredicate criterionconditionentity = EntityPredicate.deserialize(jsonobject.get("parent"));
        EntityPredicate criterionconditionentity1 = EntityPredicate.deserialize(jsonobject.get("partner"));
        EntityPredicate criterionconditionentity2 = EntityPredicate.deserialize(jsonobject.get("child"));

        return new CriterionTriggerBredAnimals.b(criterionconditionentity, criterionconditionentity1, criterionconditionentity2);
    }

    public void trigger(EntityPlayerMP entityplayer, EntityAnimal entityanimal, EntityAnimal entityanimal1, EntityAgeable entityageable) {
        CriterionTriggerBredAnimals.a criteriontriggerbredanimals_a = this.listeners.get(entityplayer.getAdvancements());

        if (criteriontriggerbredanimals_a != null) {
            criteriontriggerbredanimals_a.a(entityplayer, entityanimal, entityanimal1, entityageable);
        }

    }

    @Override
    public ICriterionInstance deserializeInstance(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        return this.b(jsonobject, jsondeserializationcontext);
    }

    static class a {

        private final PlayerAdvancements a;
        private final Set<CriterionTrigger.a<CriterionTriggerBredAnimals.b>> b = Sets.newHashSet();

        public a(PlayerAdvancements advancementdataplayer) {
            this.a = advancementdataplayer;
        }

        public boolean a() {
            return this.b.isEmpty();
        }

        public void a(CriterionTrigger.a<CriterionTriggerBredAnimals.b> criteriontrigger_a) {
            this.b.add(criteriontrigger_a);
        }

        public void b(CriterionTrigger.a<CriterionTriggerBredAnimals.b> criteriontrigger_a) {
            this.b.remove(criteriontrigger_a);
        }

        public void a(EntityPlayerMP entityplayer, EntityAnimal entityanimal, EntityAnimal entityanimal1, EntityAgeable entityageable) {
            ArrayList arraylist = null;
            Iterator iterator = this.b.iterator();

            CriterionTrigger.a criteriontrigger_a;

            while (iterator.hasNext()) {
                criteriontrigger_a = (CriterionTrigger.a) iterator.next();
                if (((CriterionTriggerBredAnimals.b) criteriontrigger_a.a()).a(entityplayer, entityanimal, entityanimal1, entityageable)) {
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
        private final EntityPredicate b;
        private final EntityPredicate c;

        public b(EntityPredicate criterionconditionentity, EntityPredicate criterionconditionentity1, EntityPredicate criterionconditionentity2) {
            super(BredAnimalsTrigger.ID);
            this.a = criterionconditionentity;
            this.b = criterionconditionentity1;
            this.c = criterionconditionentity2;
        }

        public boolean a(EntityPlayerMP entityplayer, EntityAnimal entityanimal, EntityAnimal entityanimal1, EntityAgeable entityageable) {
            return !this.c.test(entityplayer, entityageable) ? false : this.a.test(entityplayer, entityanimal) && this.b.test(entityplayer, entityanimal1) || this.a.test(entityplayer, entityanimal1) && this.b.test(entityplayer, entityanimal);
        }
    }
}
