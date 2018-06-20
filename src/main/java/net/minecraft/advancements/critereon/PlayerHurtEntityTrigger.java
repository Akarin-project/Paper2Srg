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
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;

public class PlayerHurtEntityTrigger implements ICriterionTrigger<PlayerHurtEntityTrigger.b> {

    private static final ResourceLocation field_192222_a = new ResourceLocation("player_hurt_entity");
    private final Map<PlayerAdvancements, PlayerHurtEntityTrigger.a> field_192223_b = Maps.newHashMap();

    public PlayerHurtEntityTrigger() {}

    @Override
    public ResourceLocation func_192163_a() {
        return PlayerHurtEntityTrigger.field_192222_a;
    }

    @Override
    public void a(PlayerAdvancements advancementdataplayer, ICriterionTrigger.a<PlayerHurtEntityTrigger.b> criteriontrigger_a) {
        PlayerHurtEntityTrigger.a criteriontriggerplayerhurtentity_a = this.field_192223_b.get(advancementdataplayer);

        if (criteriontriggerplayerhurtentity_a == null) {
            criteriontriggerplayerhurtentity_a = new PlayerHurtEntityTrigger.a(advancementdataplayer);
            this.field_192223_b.put(advancementdataplayer, criteriontriggerplayerhurtentity_a);
        }

        criteriontriggerplayerhurtentity_a.a(criteriontrigger_a);
    }

    @Override
    public void b(PlayerAdvancements advancementdataplayer, ICriterionTrigger.a<PlayerHurtEntityTrigger.b> criteriontrigger_a) {
        PlayerHurtEntityTrigger.a criteriontriggerplayerhurtentity_a = this.field_192223_b.get(advancementdataplayer);

        if (criteriontriggerplayerhurtentity_a != null) {
            criteriontriggerplayerhurtentity_a.b(criteriontrigger_a);
            if (criteriontriggerplayerhurtentity_a.a()) {
                this.field_192223_b.remove(advancementdataplayer);
            }
        }

    }

    @Override
    public void func_192167_a(PlayerAdvancements advancementdataplayer) {
        this.field_192223_b.remove(advancementdataplayer);
    }

    public PlayerHurtEntityTrigger.b b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        DamagePredicate criterionconditiondamage = DamagePredicate.func_192364_a(jsonobject.get("damage"));
        EntityPredicate criterionconditionentity = EntityPredicate.func_192481_a(jsonobject.get("entity"));

        return new PlayerHurtEntityTrigger.b(criterionconditiondamage, criterionconditionentity);
    }

    public void func_192220_a(EntityPlayerMP entityplayer, Entity entity, DamageSource damagesource, float f, float f1, boolean flag) {
        PlayerHurtEntityTrigger.a criteriontriggerplayerhurtentity_a = this.field_192223_b.get(entityplayer.func_192039_O());

        if (criteriontriggerplayerhurtentity_a != null) {
            criteriontriggerplayerhurtentity_a.a(entityplayer, entity, damagesource, f, f1, flag);
        }

    }

    @Override
    public b func_192166_a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        return this.b(jsonobject, jsondeserializationcontext);
    }

    static class a {

        private final PlayerAdvancements a;
        private final Set<ICriterionTrigger.a<PlayerHurtEntityTrigger.b>> b = Sets.newHashSet();

        public a(PlayerAdvancements advancementdataplayer) {
            this.a = advancementdataplayer;
        }

        public boolean a() {
            return this.b.isEmpty();
        }

        public void a(ICriterionTrigger.a<PlayerHurtEntityTrigger.b> criteriontrigger_a) {
            this.b.add(criteriontrigger_a);
        }

        public void b(ICriterionTrigger.a<PlayerHurtEntityTrigger.b> criteriontrigger_a) {
            this.b.remove(criteriontrigger_a);
        }

        public void a(EntityPlayerMP entityplayer, Entity entity, DamageSource damagesource, float f, float f1, boolean flag) {
            ArrayList arraylist = null;
            Iterator iterator = this.b.iterator();

            ICriterionTrigger.a criteriontrigger_a;

            while (iterator.hasNext()) {
                criteriontrigger_a = (ICriterionTrigger.a) iterator.next();
                if (((PlayerHurtEntityTrigger.b) criteriontrigger_a.a()).a(entityplayer, entity, damagesource, f, f1, flag)) {
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

        private final DamagePredicate a;
        private final EntityPredicate b;

        public b(DamagePredicate criterionconditiondamage, EntityPredicate criterionconditionentity) {
            super(PlayerHurtEntityTrigger.field_192222_a);
            this.a = criterionconditiondamage;
            this.b = criterionconditionentity;
        }

        public boolean a(EntityPlayerMP entityplayer, Entity entity, DamageSource damagesource, float f, float f1, boolean flag) {
            return !this.a.func_192365_a(entityplayer, damagesource, f, f1, flag) ? false : this.b.func_192482_a(entityplayer, entity);
        }
    }
}
