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
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.CriterionTriggerEntityHurtPlayer.a;
import net.minecraft.server.CriterionTriggerEntityHurtPlayer.b;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;

public class EntityHurtPlayerTrigger implements ICriterionTrigger<CriterionTriggerEntityHurtPlayer.b> {

    private static final ResourceLocation field_192201_a = new ResourceLocation("entity_hurt_player");
    private final Map<PlayerAdvancements, CriterionTriggerEntityHurtPlayer.a> field_192202_b = Maps.newHashMap();

    public EntityHurtPlayerTrigger() {}

    public ResourceLocation func_192163_a() {
        return EntityHurtPlayerTrigger.field_192201_a;
    }

    public void a(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerEntityHurtPlayer.b> criteriontrigger_a) {
        CriterionTriggerEntityHurtPlayer.a criteriontriggerentityhurtplayer_a = (CriterionTriggerEntityHurtPlayer.a) this.field_192202_b.get(advancementdataplayer);

        if (criteriontriggerentityhurtplayer_a == null) {
            criteriontriggerentityhurtplayer_a = new CriterionTriggerEntityHurtPlayer.a(advancementdataplayer);
            this.field_192202_b.put(advancementdataplayer, criteriontriggerentityhurtplayer_a);
        }

        criteriontriggerentityhurtplayer_a.a(criteriontrigger_a);
    }

    public void b(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerEntityHurtPlayer.b> criteriontrigger_a) {
        CriterionTriggerEntityHurtPlayer.a criteriontriggerentityhurtplayer_a = (CriterionTriggerEntityHurtPlayer.a) this.field_192202_b.get(advancementdataplayer);

        if (criteriontriggerentityhurtplayer_a != null) {
            criteriontriggerentityhurtplayer_a.b(criteriontrigger_a);
            if (criteriontriggerentityhurtplayer_a.a()) {
                this.field_192202_b.remove(advancementdataplayer);
            }
        }

    }

    public void func_192167_a(PlayerAdvancements advancementdataplayer) {
        this.field_192202_b.remove(advancementdataplayer);
    }

    public CriterionTriggerEntityHurtPlayer.b b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        DamagePredicate criterionconditiondamage = DamagePredicate.func_192364_a(jsonobject.get("damage"));

        return new CriterionTriggerEntityHurtPlayer.b(criterionconditiondamage);
    }

    public void func_192200_a(EntityPlayerMP entityplayer, DamageSource damagesource, float f, float f1, boolean flag) {
        CriterionTriggerEntityHurtPlayer.a criteriontriggerentityhurtplayer_a = (CriterionTriggerEntityHurtPlayer.a) this.field_192202_b.get(entityplayer.func_192039_O());

        if (criteriontriggerentityhurtplayer_a != null) {
            criteriontriggerentityhurtplayer_a.a(entityplayer, damagesource, f, f1, flag);
        }

    }

    public ICriterionInstance func_192166_a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        return this.b(jsonobject, jsondeserializationcontext);
    }

    static class a {

        private final PlayerAdvancements a;
        private final Set<CriterionTrigger.a<CriterionTriggerEntityHurtPlayer.b>> b = Sets.newHashSet();

        public a(PlayerAdvancements advancementdataplayer) {
            this.a = advancementdataplayer;
        }

        public boolean a() {
            return this.b.isEmpty();
        }

        public void a(CriterionTrigger.a<CriterionTriggerEntityHurtPlayer.b> criteriontrigger_a) {
            this.b.add(criteriontrigger_a);
        }

        public void b(CriterionTrigger.a<CriterionTriggerEntityHurtPlayer.b> criteriontrigger_a) {
            this.b.remove(criteriontrigger_a);
        }

        public void a(EntityPlayerMP entityplayer, DamageSource damagesource, float f, float f1, boolean flag) {
            ArrayList arraylist = null;
            Iterator iterator = this.b.iterator();

            CriterionTrigger.a criteriontrigger_a;

            while (iterator.hasNext()) {
                criteriontrigger_a = (CriterionTrigger.a) iterator.next();
                if (((CriterionTriggerEntityHurtPlayer.b) criteriontrigger_a.a()).a(entityplayer, damagesource, f, f1, flag)) {
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

        private final DamagePredicate a;

        public b(DamagePredicate criterionconditiondamage) {
            super(EntityHurtPlayerTrigger.field_192201_a);
            this.a = criterionconditiondamage;
        }

        public boolean a(EntityPlayerMP entityplayer, DamageSource damagesource, float f, float f1, boolean flag) {
            return this.a.func_192365_a(entityplayer, damagesource, f, f1, flag);
        }
    }
}
