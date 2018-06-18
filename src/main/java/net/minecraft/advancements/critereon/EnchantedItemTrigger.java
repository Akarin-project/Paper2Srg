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
import net.minecraft.server.CriterionTriggerEnchantedItem.a;
import net.minecraft.server.CriterionTriggerEnchantedItem.b;
import net.minecraft.util.ResourceLocation;

public class EnchantedItemTrigger implements ICriterionTrigger<CriterionTriggerEnchantedItem.b> {

    private static final ResourceLocation ID = new ResourceLocation("enchanted_item");
    private final Map<PlayerAdvancements, CriterionTriggerEnchantedItem.a> listeners = Maps.newHashMap();

    public EnchantedItemTrigger() {}

    public ResourceLocation getId() {
        return EnchantedItemTrigger.ID;
    }

    public void a(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerEnchantedItem.b> criteriontrigger_a) {
        CriterionTriggerEnchantedItem.a criteriontriggerenchanteditem_a = (CriterionTriggerEnchantedItem.a) this.listeners.get(advancementdataplayer);

        if (criteriontriggerenchanteditem_a == null) {
            criteriontriggerenchanteditem_a = new CriterionTriggerEnchantedItem.a(advancementdataplayer);
            this.listeners.put(advancementdataplayer, criteriontriggerenchanteditem_a);
        }

        criteriontriggerenchanteditem_a.a(criteriontrigger_a);
    }

    public void b(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerEnchantedItem.b> criteriontrigger_a) {
        CriterionTriggerEnchantedItem.a criteriontriggerenchanteditem_a = (CriterionTriggerEnchantedItem.a) this.listeners.get(advancementdataplayer);

        if (criteriontriggerenchanteditem_a != null) {
            criteriontriggerenchanteditem_a.b(criteriontrigger_a);
            if (criteriontriggerenchanteditem_a.a()) {
                this.listeners.remove(advancementdataplayer);
            }
        }

    }

    public void removeAllListeners(PlayerAdvancements advancementdataplayer) {
        this.listeners.remove(advancementdataplayer);
    }

    public CriterionTriggerEnchantedItem.b b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        ItemPredicate criterionconditionitem = ItemPredicate.deserialize(jsonobject.get("item"));
        MinMaxBounds criterionconditionvalue = MinMaxBounds.deserialize(jsonobject.get("levels"));

        return new CriterionTriggerEnchantedItem.b(criterionconditionitem, criterionconditionvalue);
    }

    public void trigger(EntityPlayerMP entityplayer, ItemStack itemstack, int i) {
        CriterionTriggerEnchantedItem.a criteriontriggerenchanteditem_a = (CriterionTriggerEnchantedItem.a) this.listeners.get(entityplayer.getAdvancements());

        if (criteriontriggerenchanteditem_a != null) {
            criteriontriggerenchanteditem_a.a(itemstack, i);
        }

    }

    public ICriterionInstance deserializeInstance(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        return this.b(jsonobject, jsondeserializationcontext);
    }

    static class a {

        private final PlayerAdvancements a;
        private final Set<CriterionTrigger.a<CriterionTriggerEnchantedItem.b>> b = Sets.newHashSet();

        public a(PlayerAdvancements advancementdataplayer) {
            this.a = advancementdataplayer;
        }

        public boolean a() {
            return this.b.isEmpty();
        }

        public void a(CriterionTrigger.a<CriterionTriggerEnchantedItem.b> criteriontrigger_a) {
            this.b.add(criteriontrigger_a);
        }

        public void b(CriterionTrigger.a<CriterionTriggerEnchantedItem.b> criteriontrigger_a) {
            this.b.remove(criteriontrigger_a);
        }

        public void a(ItemStack itemstack, int i) {
            ArrayList arraylist = null;
            Iterator iterator = this.b.iterator();

            CriterionTrigger.a criteriontrigger_a;

            while (iterator.hasNext()) {
                criteriontrigger_a = (CriterionTrigger.a) iterator.next();
                if (((CriterionTriggerEnchantedItem.b) criteriontrigger_a.a()).a(itemstack, i)) {
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
        private final MinMaxBounds b;

        public b(ItemPredicate criterionconditionitem, MinMaxBounds criterionconditionvalue) {
            super(EnchantedItemTrigger.ID);
            this.a = criterionconditionitem;
            this.b = criterionconditionvalue;
        }

        public boolean a(ItemStack itemstack, int i) {
            return !this.a.test(itemstack) ? false : this.b.test((float) i);
        }
    }
}
