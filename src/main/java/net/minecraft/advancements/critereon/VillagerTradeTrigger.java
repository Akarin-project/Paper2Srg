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
import net.minecraft.util.ResourceLocation;

public class VillagerTradeTrigger implements ICriterionTrigger<VillagerTradeTrigger.b> {

    private static final ResourceLocation field_192237_a = new ResourceLocation("villager_trade");
    private final Map<PlayerAdvancements, VillagerTradeTrigger.a> field_192238_b = Maps.newHashMap();

    public VillagerTradeTrigger() {}

    @Override
    public ResourceLocation func_192163_a() {
        return VillagerTradeTrigger.field_192237_a;
    }

    @Override
    public void a(PlayerAdvancements advancementdataplayer, ICriterionTrigger.a<VillagerTradeTrigger.b> criteriontrigger_a) {
        VillagerTradeTrigger.a criteriontriggervillagertrade_a = this.field_192238_b.get(advancementdataplayer);

        if (criteriontriggervillagertrade_a == null) {
            criteriontriggervillagertrade_a = new VillagerTradeTrigger.a(advancementdataplayer);
            this.field_192238_b.put(advancementdataplayer, criteriontriggervillagertrade_a);
        }

        criteriontriggervillagertrade_a.a(criteriontrigger_a);
    }

    @Override
    public void b(PlayerAdvancements advancementdataplayer, ICriterionTrigger.a<VillagerTradeTrigger.b> criteriontrigger_a) {
        VillagerTradeTrigger.a criteriontriggervillagertrade_a = this.field_192238_b.get(advancementdataplayer);

        if (criteriontriggervillagertrade_a != null) {
            criteriontriggervillagertrade_a.b(criteriontrigger_a);
            if (criteriontriggervillagertrade_a.a()) {
                this.field_192238_b.remove(advancementdataplayer);
            }
        }

    }

    @Override
    public void func_192167_a(PlayerAdvancements advancementdataplayer) {
        this.field_192238_b.remove(advancementdataplayer);
    }

    public VillagerTradeTrigger.b b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        EntityPredicate criterionconditionentity = EntityPredicate.func_192481_a(jsonobject.get("villager"));
        ItemPredicate criterionconditionitem = ItemPredicate.func_192492_a(jsonobject.get("item"));

        return new VillagerTradeTrigger.b(criterionconditionentity, criterionconditionitem);
    }

    public void func_192234_a(EntityPlayerMP entityplayer, EntityVillager entityvillager, ItemStack itemstack) {
        VillagerTradeTrigger.a criteriontriggervillagertrade_a = this.field_192238_b.get(entityplayer.func_192039_O());

        if (criteriontriggervillagertrade_a != null) {
            criteriontriggervillagertrade_a.a(entityplayer, entityvillager, itemstack);
        }

    }

    @Override
    public b func_192166_a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        return this.b(jsonobject, jsondeserializationcontext);
    }

    static class a {

        private final PlayerAdvancements a;
        private final Set<ICriterionTrigger.a<VillagerTradeTrigger.b>> b = Sets.newHashSet();

        public a(PlayerAdvancements advancementdataplayer) {
            this.a = advancementdataplayer;
        }

        public boolean a() {
            return this.b.isEmpty();
        }

        public void a(ICriterionTrigger.a<VillagerTradeTrigger.b> criteriontrigger_a) {
            this.b.add(criteriontrigger_a);
        }

        public void b(ICriterionTrigger.a<VillagerTradeTrigger.b> criteriontrigger_a) {
            this.b.remove(criteriontrigger_a);
        }

        public void a(EntityPlayerMP entityplayer, EntityVillager entityvillager, ItemStack itemstack) {
            ArrayList arraylist = null;
            Iterator iterator = this.b.iterator();

            ICriterionTrigger.a criteriontrigger_a;

            while (iterator.hasNext()) {
                criteriontrigger_a = (ICriterionTrigger.a) iterator.next();
                if (((VillagerTradeTrigger.b) criteriontrigger_a.a()).a(entityplayer, entityvillager, itemstack)) {
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
        private final ItemPredicate b;

        public b(EntityPredicate criterionconditionentity, ItemPredicate criterionconditionitem) {
            super(VillagerTradeTrigger.field_192237_a);
            this.a = criterionconditionentity;
            this.b = criterionconditionitem;
        }

        public boolean a(EntityPlayerMP entityplayer, EntityVillager entityvillager, ItemStack itemstack) {
            return !this.a.func_192482_a(entityplayer, entityvillager) ? false : this.b.func_192493_a(itemstack);
        }
    }
}
