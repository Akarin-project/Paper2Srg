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
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.CriterionTriggerVillagerTrade.a;
import net.minecraft.server.CriterionTriggerVillagerTrade.b;
import net.minecraft.util.ResourceLocation;

public class VillagerTradeTrigger implements ICriterionTrigger<CriterionTriggerVillagerTrade.b> {

    private static final ResourceLocation ID = new ResourceLocation("villager_trade");
    private final Map<PlayerAdvancements, CriterionTriggerVillagerTrade.a> listeners = Maps.newHashMap();

    public VillagerTradeTrigger() {}

    public ResourceLocation getId() {
        return VillagerTradeTrigger.ID;
    }

    public void a(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerVillagerTrade.b> criteriontrigger_a) {
        CriterionTriggerVillagerTrade.a criteriontriggervillagertrade_a = (CriterionTriggerVillagerTrade.a) this.listeners.get(advancementdataplayer);

        if (criteriontriggervillagertrade_a == null) {
            criteriontriggervillagertrade_a = new CriterionTriggerVillagerTrade.a(advancementdataplayer);
            this.listeners.put(advancementdataplayer, criteriontriggervillagertrade_a);
        }

        criteriontriggervillagertrade_a.a(criteriontrigger_a);
    }

    public void b(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerVillagerTrade.b> criteriontrigger_a) {
        CriterionTriggerVillagerTrade.a criteriontriggervillagertrade_a = (CriterionTriggerVillagerTrade.a) this.listeners.get(advancementdataplayer);

        if (criteriontriggervillagertrade_a != null) {
            criteriontriggervillagertrade_a.b(criteriontrigger_a);
            if (criteriontriggervillagertrade_a.a()) {
                this.listeners.remove(advancementdataplayer);
            }
        }

    }

    public void removeAllListeners(PlayerAdvancements advancementdataplayer) {
        this.listeners.remove(advancementdataplayer);
    }

    public CriterionTriggerVillagerTrade.b b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        EntityPredicate criterionconditionentity = EntityPredicate.deserialize(jsonobject.get("villager"));
        ItemPredicate criterionconditionitem = ItemPredicate.deserialize(jsonobject.get("item"));

        return new CriterionTriggerVillagerTrade.b(criterionconditionentity, criterionconditionitem);
    }

    public void trigger(EntityPlayerMP entityplayer, EntityVillager entityvillager, ItemStack itemstack) {
        CriterionTriggerVillagerTrade.a criteriontriggervillagertrade_a = (CriterionTriggerVillagerTrade.a) this.listeners.get(entityplayer.getAdvancements());

        if (criteriontriggervillagertrade_a != null) {
            criteriontriggervillagertrade_a.a(entityplayer, entityvillager, itemstack);
        }

    }

    public ICriterionInstance deserializeInstance(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        return this.b(jsonobject, jsondeserializationcontext);
    }

    static class a {

        private final PlayerAdvancements a;
        private final Set<CriterionTrigger.a<CriterionTriggerVillagerTrade.b>> b = Sets.newHashSet();

        public a(PlayerAdvancements advancementdataplayer) {
            this.a = advancementdataplayer;
        }

        public boolean a() {
            return this.b.isEmpty();
        }

        public void a(CriterionTrigger.a<CriterionTriggerVillagerTrade.b> criteriontrigger_a) {
            this.b.add(criteriontrigger_a);
        }

        public void b(CriterionTrigger.a<CriterionTriggerVillagerTrade.b> criteriontrigger_a) {
            this.b.remove(criteriontrigger_a);
        }

        public void a(EntityPlayerMP entityplayer, EntityVillager entityvillager, ItemStack itemstack) {
            ArrayList arraylist = null;
            Iterator iterator = this.b.iterator();

            CriterionTrigger.a criteriontrigger_a;

            while (iterator.hasNext()) {
                criteriontrigger_a = (CriterionTrigger.a) iterator.next();
                if (((CriterionTriggerVillagerTrade.b) criteriontrigger_a.a()).a(entityplayer, entityvillager, itemstack)) {
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
        private final ItemPredicate b;

        public b(EntityPredicate criterionconditionentity, ItemPredicate criterionconditionitem) {
            super(VillagerTradeTrigger.ID);
            this.a = criterionconditionentity;
            this.b = criterionconditionitem;
        }

        public boolean a(EntityPlayerMP entityplayer, EntityVillager entityvillager, ItemStack itemstack) {
            return !this.a.test(entityplayer, entityvillager) ? false : this.b.test(itemstack);
        }
    }
}
