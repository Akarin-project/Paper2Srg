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
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.CriterionTriggerCuredZombieVillager.a;
import net.minecraft.server.CriterionTriggerCuredZombieVillager.b;
import net.minecraft.util.ResourceLocation;

public class CuredZombieVillagerTrigger implements ICriterionTrigger<CriterionTriggerCuredZombieVillager.b> {

    private static final ResourceLocation field_192186_a = new ResourceLocation("cured_zombie_villager");
    private final Map<PlayerAdvancements, CriterionTriggerCuredZombieVillager.a> field_192187_b = Maps.newHashMap();

    public CuredZombieVillagerTrigger() {}

    public ResourceLocation func_192163_a() {
        return CuredZombieVillagerTrigger.field_192186_a;
    }

    public void a(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerCuredZombieVillager.b> criteriontrigger_a) {
        CriterionTriggerCuredZombieVillager.a criteriontriggercuredzombievillager_a = (CriterionTriggerCuredZombieVillager.a) this.field_192187_b.get(advancementdataplayer);

        if (criteriontriggercuredzombievillager_a == null) {
            criteriontriggercuredzombievillager_a = new CriterionTriggerCuredZombieVillager.a(advancementdataplayer);
            this.field_192187_b.put(advancementdataplayer, criteriontriggercuredzombievillager_a);
        }

        criteriontriggercuredzombievillager_a.a(criteriontrigger_a);
    }

    public void b(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerCuredZombieVillager.b> criteriontrigger_a) {
        CriterionTriggerCuredZombieVillager.a criteriontriggercuredzombievillager_a = (CriterionTriggerCuredZombieVillager.a) this.field_192187_b.get(advancementdataplayer);

        if (criteriontriggercuredzombievillager_a != null) {
            criteriontriggercuredzombievillager_a.b(criteriontrigger_a);
            if (criteriontriggercuredzombievillager_a.a()) {
                this.field_192187_b.remove(advancementdataplayer);
            }
        }

    }

    public void func_192167_a(PlayerAdvancements advancementdataplayer) {
        this.field_192187_b.remove(advancementdataplayer);
    }

    public CriterionTriggerCuredZombieVillager.b b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        EntityPredicate criterionconditionentity = EntityPredicate.func_192481_a(jsonobject.get("zombie"));
        EntityPredicate criterionconditionentity1 = EntityPredicate.func_192481_a(jsonobject.get("villager"));

        return new CriterionTriggerCuredZombieVillager.b(criterionconditionentity, criterionconditionentity1);
    }

    public void func_192183_a(EntityPlayerMP entityplayer, EntityZombie entityzombie, EntityVillager entityvillager) {
        CriterionTriggerCuredZombieVillager.a criteriontriggercuredzombievillager_a = (CriterionTriggerCuredZombieVillager.a) this.field_192187_b.get(entityplayer.func_192039_O());

        if (criteriontriggercuredzombievillager_a != null) {
            criteriontriggercuredzombievillager_a.a(entityplayer, entityzombie, entityvillager);
        }

    }

    public ICriterionInstance func_192166_a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        return this.b(jsonobject, jsondeserializationcontext);
    }

    static class a {

        private final PlayerAdvancements a;
        private final Set<CriterionTrigger.a<CriterionTriggerCuredZombieVillager.b>> b = Sets.newHashSet();

        public a(PlayerAdvancements advancementdataplayer) {
            this.a = advancementdataplayer;
        }

        public boolean a() {
            return this.b.isEmpty();
        }

        public void a(CriterionTrigger.a<CriterionTriggerCuredZombieVillager.b> criteriontrigger_a) {
            this.b.add(criteriontrigger_a);
        }

        public void b(CriterionTrigger.a<CriterionTriggerCuredZombieVillager.b> criteriontrigger_a) {
            this.b.remove(criteriontrigger_a);
        }

        public void a(EntityPlayerMP entityplayer, EntityZombie entityzombie, EntityVillager entityvillager) {
            ArrayList arraylist = null;
            Iterator iterator = this.b.iterator();

            CriterionTrigger.a criteriontrigger_a;

            while (iterator.hasNext()) {
                criteriontrigger_a = (CriterionTrigger.a) iterator.next();
                if (((CriterionTriggerCuredZombieVillager.b) criteriontrigger_a.a()).a(entityplayer, entityzombie, entityvillager)) {
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

        public b(EntityPredicate criterionconditionentity, EntityPredicate criterionconditionentity1) {
            super(CuredZombieVillagerTrigger.field_192186_a);
            this.a = criterionconditionentity;
            this.b = criterionconditionentity1;
        }

        public boolean a(EntityPlayerMP entityplayer, EntityZombie entityzombie, EntityVillager entityvillager) {
            return !this.a.func_192482_a(entityplayer, entityzombie) ? false : this.b.func_192482_a(entityplayer, entityvillager);
        }
    }
}
