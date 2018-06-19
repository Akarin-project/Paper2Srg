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
import net.minecraft.server.CriterionTriggerKilled.a;
import net.minecraft.server.CriterionTriggerKilled.b;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;

public class KilledTrigger implements ICriterionTrigger<CriterionTriggerKilled.b> {

    private final Map<PlayerAdvancements, CriterionTriggerKilled.a> field_192213_a = Maps.newHashMap();
    private final ResourceLocation field_192214_b;

    public KilledTrigger(ResourceLocation minecraftkey) {
        this.field_192214_b = minecraftkey;
    }

    public ResourceLocation func_192163_a() {
        return this.field_192214_b;
    }

    public void a(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerKilled.b> criteriontrigger_a) {
        CriterionTriggerKilled.a criteriontriggerkilled_a = (CriterionTriggerKilled.a) this.field_192213_a.get(advancementdataplayer);

        if (criteriontriggerkilled_a == null) {
            criteriontriggerkilled_a = new CriterionTriggerKilled.a(advancementdataplayer);
            this.field_192213_a.put(advancementdataplayer, criteriontriggerkilled_a);
        }

        criteriontriggerkilled_a.a(criteriontrigger_a);
    }

    public void b(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerKilled.b> criteriontrigger_a) {
        CriterionTriggerKilled.a criteriontriggerkilled_a = (CriterionTriggerKilled.a) this.field_192213_a.get(advancementdataplayer);

        if (criteriontriggerkilled_a != null) {
            criteriontriggerkilled_a.b(criteriontrigger_a);
            if (criteriontriggerkilled_a.a()) {
                this.field_192213_a.remove(advancementdataplayer);
            }
        }

    }

    public void func_192167_a(PlayerAdvancements advancementdataplayer) {
        this.field_192213_a.remove(advancementdataplayer);
    }

    public CriterionTriggerKilled.b b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        return new CriterionTriggerKilled.b(this.field_192214_b, EntityPredicate.func_192481_a(jsonobject.get("entity")), DamageSourcePredicate.func_192447_a(jsonobject.get("killing_blow")));
    }

    public void func_192211_a(EntityPlayerMP entityplayer, Entity entity, DamageSource damagesource) {
        CriterionTriggerKilled.a criteriontriggerkilled_a = (CriterionTriggerKilled.a) this.field_192213_a.get(entityplayer.func_192039_O());

        if (criteriontriggerkilled_a != null) {
            criteriontriggerkilled_a.a(entityplayer, entity, damagesource);
        }

    }

    public ICriterionInstance func_192166_a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        return this.b(jsonobject, jsondeserializationcontext);
    }

    static class a {

        private final PlayerAdvancements a;
        private final Set<CriterionTrigger.a<CriterionTriggerKilled.b>> b = Sets.newHashSet();

        public a(PlayerAdvancements advancementdataplayer) {
            this.a = advancementdataplayer;
        }

        public boolean a() {
            return this.b.isEmpty();
        }

        public void a(CriterionTrigger.a<CriterionTriggerKilled.b> criteriontrigger_a) {
            this.b.add(criteriontrigger_a);
        }

        public void b(CriterionTrigger.a<CriterionTriggerKilled.b> criteriontrigger_a) {
            this.b.remove(criteriontrigger_a);
        }

        public void a(EntityPlayerMP entityplayer, Entity entity, DamageSource damagesource) {
            ArrayList arraylist = null;
            Iterator iterator = this.b.iterator();

            CriterionTrigger.a criteriontrigger_a;

            while (iterator.hasNext()) {
                criteriontrigger_a = (CriterionTrigger.a) iterator.next();
                if (((CriterionTriggerKilled.b) criteriontrigger_a.a()).a(entityplayer, entity, damagesource)) {
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
        private final DamageSourcePredicate b;

        public b(ResourceLocation minecraftkey, EntityPredicate criterionconditionentity, DamageSourcePredicate criterionconditiondamagesource) {
            super(minecraftkey);
            this.a = criterionconditionentity;
            this.b = criterionconditiondamagesource;
        }

        public boolean a(EntityPlayerMP entityplayer, Entity entity, DamageSource damagesource) {
            return !this.b.func_193418_a(entityplayer, damagesource) ? false : this.a.func_192482_a(entityplayer, entity);
        }
    }
}
