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
import net.minecraft.util.ResourceLocation;

public class BredAnimalsTrigger implements ICriterionTrigger<BredAnimalsTrigger.b> {

    private static final ResourceLocation field_192171_a = new ResourceLocation("bred_animals");
    private final Map<PlayerAdvancements, BredAnimalsTrigger.a> field_192172_b = Maps.newHashMap();

    public BredAnimalsTrigger() {}

    @Override
    public ResourceLocation func_192163_a() {
        return BredAnimalsTrigger.field_192171_a;
    }

    @Override
    public void a(PlayerAdvancements advancementdataplayer, ICriterionTrigger.a<BredAnimalsTrigger.b> criteriontrigger_a) {
        BredAnimalsTrigger.a criteriontriggerbredanimals_a = this.field_192172_b.get(advancementdataplayer);

        if (criteriontriggerbredanimals_a == null) {
            criteriontriggerbredanimals_a = new BredAnimalsTrigger.a(advancementdataplayer);
            this.field_192172_b.put(advancementdataplayer, criteriontriggerbredanimals_a);
        }

        criteriontriggerbredanimals_a.a(criteriontrigger_a);
    }

    @Override
    public void b(PlayerAdvancements advancementdataplayer, ICriterionTrigger.a<BredAnimalsTrigger.b> criteriontrigger_a) {
        BredAnimalsTrigger.a criteriontriggerbredanimals_a = this.field_192172_b.get(advancementdataplayer);

        if (criteriontriggerbredanimals_a != null) {
            criteriontriggerbredanimals_a.b(criteriontrigger_a);
            if (criteriontriggerbredanimals_a.a()) {
                this.field_192172_b.remove(advancementdataplayer);
            }
        }

    }

    @Override
    public void func_192167_a(PlayerAdvancements advancementdataplayer) {
        this.field_192172_b.remove(advancementdataplayer);
    }

    public BredAnimalsTrigger.b b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        EntityPredicate criterionconditionentity = EntityPredicate.func_192481_a(jsonobject.get("parent"));
        EntityPredicate criterionconditionentity1 = EntityPredicate.func_192481_a(jsonobject.get("partner"));
        EntityPredicate criterionconditionentity2 = EntityPredicate.func_192481_a(jsonobject.get("child"));

        return new BredAnimalsTrigger.b(criterionconditionentity, criterionconditionentity1, criterionconditionentity2);
    }

    public void func_192168_a(EntityPlayerMP entityplayer, EntityAnimal entityanimal, EntityAnimal entityanimal1, EntityAgeable entityageable) {
        BredAnimalsTrigger.a criteriontriggerbredanimals_a = this.field_192172_b.get(entityplayer.func_192039_O());

        if (criteriontriggerbredanimals_a != null) {
            criteriontriggerbredanimals_a.a(entityplayer, entityanimal, entityanimal1, entityageable);
        }

    }

    @Override
    public b func_192166_a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        return this.b(jsonobject, jsondeserializationcontext);
    }

    static class a {

        private final PlayerAdvancements a;
        private final Set<ICriterionTrigger.a<BredAnimalsTrigger.b>> b = Sets.newHashSet();

        public a(PlayerAdvancements advancementdataplayer) {
            this.a = advancementdataplayer;
        }

        public boolean a() {
            return this.b.isEmpty();
        }

        public void a(ICriterionTrigger.a<BredAnimalsTrigger.b> criteriontrigger_a) {
            this.b.add(criteriontrigger_a);
        }

        public void b(ICriterionTrigger.a<BredAnimalsTrigger.b> criteriontrigger_a) {
            this.b.remove(criteriontrigger_a);
        }

        public void a(EntityPlayerMP entityplayer, EntityAnimal entityanimal, EntityAnimal entityanimal1, EntityAgeable entityageable) {
            ArrayList arraylist = null;
            Iterator iterator = this.b.iterator();

            ICriterionTrigger.a criteriontrigger_a;

            while (iterator.hasNext()) {
                criteriontrigger_a = (ICriterionTrigger.a) iterator.next();
                if (((BredAnimalsTrigger.b) criteriontrigger_a.a()).a(entityplayer, entityanimal, entityanimal1, entityageable)) {
                    if (arraylist == null) {
                        arraylist = Lists.newArrayList();
                    }

                    arraylist.add(criteriontrigger_a);
                }
            }

            if (arraylist != null) {
                iterator = arraylist.iterator();

                while (iterator.hasNext()) {
                    criteriontrigger_a = (ICriterionTrigger.a) iterator.next();
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
            super(BredAnimalsTrigger.field_192171_a);
            this.a = criterionconditionentity;
            this.b = criterionconditionentity1;
            this.c = criterionconditionentity2;
        }

        public boolean a(EntityPlayerMP entityplayer, EntityAnimal entityanimal, EntityAnimal entityanimal1, EntityAgeable entityageable) {
            return !this.c.func_192482_a(entityplayer, entityageable) ? false : this.a.func_192482_a(entityplayer, entityanimal) && this.b.func_192482_a(entityplayer, entityanimal1) || this.a.func_192482_a(entityplayer, entityanimal1) && this.b.func_192482_a(entityplayer, entityanimal);
        }
    }
}
