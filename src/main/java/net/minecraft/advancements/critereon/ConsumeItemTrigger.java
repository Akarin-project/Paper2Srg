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
import net.minecraft.item.ItemStack;
import net.minecraft.server.CriterionTriggerConsumeItem.a;
import net.minecraft.server.CriterionTriggerConsumeItem.b;
import net.minecraft.util.ResourceLocation;

public class ConsumeItemTrigger implements ICriterionTrigger<CriterionTriggerConsumeItem.b> {

    private static final ResourceLocation ID = new ResourceLocation("consume_item");
    private final Map<PlayerAdvancements, CriterionTriggerConsumeItem.a> listeners = Maps.newHashMap();

    public ConsumeItemTrigger() {}

    public ResourceLocation getId() {
        return ConsumeItemTrigger.ID;
    }

    public void a(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerConsumeItem.b> criteriontrigger_a) {
        CriterionTriggerConsumeItem.a criteriontriggerconsumeitem_a = (CriterionTriggerConsumeItem.a) this.listeners.get(advancementdataplayer);

        if (criteriontriggerconsumeitem_a == null) {
            criteriontriggerconsumeitem_a = new CriterionTriggerConsumeItem.a(advancementdataplayer);
            this.listeners.put(advancementdataplayer, criteriontriggerconsumeitem_a);
        }

        criteriontriggerconsumeitem_a.a(criteriontrigger_a);
    }

    public void b(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerConsumeItem.b> criteriontrigger_a) {
        CriterionTriggerConsumeItem.a criteriontriggerconsumeitem_a = (CriterionTriggerConsumeItem.a) this.listeners.get(advancementdataplayer);

        if (criteriontriggerconsumeitem_a != null) {
            criteriontriggerconsumeitem_a.b(criteriontrigger_a);
            if (criteriontriggerconsumeitem_a.a()) {
                this.listeners.remove(advancementdataplayer);
            }
        }

    }

    public void removeAllListeners(PlayerAdvancements advancementdataplayer) {
        this.listeners.remove(advancementdataplayer);
    }

    public CriterionTriggerConsumeItem.b b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        ItemPredicate criterionconditionitem = ItemPredicate.deserialize(jsonobject.get("item"));

        return new CriterionTriggerConsumeItem.b(criterionconditionitem);
    }

    public void trigger(EntityPlayerMP entityplayer, ItemStack itemstack) {
        CriterionTriggerConsumeItem.a criteriontriggerconsumeitem_a = (CriterionTriggerConsumeItem.a) this.listeners.get(entityplayer.getAdvancements());

        if (criteriontriggerconsumeitem_a != null) {
            criteriontriggerconsumeitem_a.a(itemstack);
        }

    }

    public ICriterionInstance deserializeInstance(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        return this.b(jsonobject, jsondeserializationcontext);
    }

    static class a {

        private final PlayerAdvancements a;
        private final Set<CriterionTrigger.a<CriterionTriggerConsumeItem.b>> b = Sets.newHashSet();

        public a(PlayerAdvancements advancementdataplayer) {
            this.a = advancementdataplayer;
        }

        public boolean a() {
            return this.b.isEmpty();
        }

        public void a(CriterionTrigger.a<CriterionTriggerConsumeItem.b> criteriontrigger_a) {
            this.b.add(criteriontrigger_a);
        }

        public void b(CriterionTrigger.a<CriterionTriggerConsumeItem.b> criteriontrigger_a) {
            this.b.remove(criteriontrigger_a);
        }

        public void a(ItemStack itemstack) {
            ArrayList arraylist = null;
            Iterator iterator = this.b.iterator();

            CriterionTrigger.a criteriontrigger_a;

            while (iterator.hasNext()) {
                criteriontrigger_a = (CriterionTrigger.a) iterator.next();
                if (((CriterionTriggerConsumeItem.b) criteriontrigger_a.a()).a(itemstack)) {
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

        private final ItemPredicate a;

        public b(ItemPredicate criterionconditionitem) {
            super(ConsumeItemTrigger.ID);
            this.a = criterionconditionitem;
        }

        public boolean a(ItemStack itemstack) {
            return this.a.test(itemstack);
        }
    }
}
