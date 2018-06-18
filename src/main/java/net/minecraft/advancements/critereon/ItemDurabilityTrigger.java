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
import net.minecraft.server.CriterionTriggerItemDurabilityChanged.a;
import net.minecraft.server.CriterionTriggerItemDurabilityChanged.b;
import net.minecraft.util.ResourceLocation;

public class ItemDurabilityTrigger implements ICriterionTrigger<CriterionTriggerItemDurabilityChanged.b> {

    private static final ResourceLocation ID = new ResourceLocation("item_durability_changed");
    private final Map<PlayerAdvancements, CriterionTriggerItemDurabilityChanged.a> listeners = Maps.newHashMap();

    public ItemDurabilityTrigger() {}

    public ResourceLocation getId() {
        return ItemDurabilityTrigger.ID;
    }

    public void a(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerItemDurabilityChanged.b> criteriontrigger_a) {
        CriterionTriggerItemDurabilityChanged.a criteriontriggeritemdurabilitychanged_a = (CriterionTriggerItemDurabilityChanged.a) this.listeners.get(advancementdataplayer);

        if (criteriontriggeritemdurabilitychanged_a == null) {
            criteriontriggeritemdurabilitychanged_a = new CriterionTriggerItemDurabilityChanged.a(advancementdataplayer);
            this.listeners.put(advancementdataplayer, criteriontriggeritemdurabilitychanged_a);
        }

        criteriontriggeritemdurabilitychanged_a.a(criteriontrigger_a);
    }

    public void b(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerItemDurabilityChanged.b> criteriontrigger_a) {
        CriterionTriggerItemDurabilityChanged.a criteriontriggeritemdurabilitychanged_a = (CriterionTriggerItemDurabilityChanged.a) this.listeners.get(advancementdataplayer);

        if (criteriontriggeritemdurabilitychanged_a != null) {
            criteriontriggeritemdurabilitychanged_a.b(criteriontrigger_a);
            if (criteriontriggeritemdurabilitychanged_a.a()) {
                this.listeners.remove(advancementdataplayer);
            }
        }

    }

    public void removeAllListeners(PlayerAdvancements advancementdataplayer) {
        this.listeners.remove(advancementdataplayer);
    }

    public CriterionTriggerItemDurabilityChanged.b b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        ItemPredicate criterionconditionitem = ItemPredicate.deserialize(jsonobject.get("item"));
        MinMaxBounds criterionconditionvalue = MinMaxBounds.deserialize(jsonobject.get("durability"));
        MinMaxBounds criterionconditionvalue1 = MinMaxBounds.deserialize(jsonobject.get("delta"));

        return new CriterionTriggerItemDurabilityChanged.b(criterionconditionitem, criterionconditionvalue, criterionconditionvalue1);
    }

    public void trigger(EntityPlayerMP entityplayer, ItemStack itemstack, int i) {
        CriterionTriggerItemDurabilityChanged.a criteriontriggeritemdurabilitychanged_a = (CriterionTriggerItemDurabilityChanged.a) this.listeners.get(entityplayer.getAdvancements());

        if (criteriontriggeritemdurabilitychanged_a != null) {
            criteriontriggeritemdurabilitychanged_a.a(itemstack, i);
        }

    }

    public ICriterionInstance deserializeInstance(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        return this.b(jsonobject, jsondeserializationcontext);
    }

    static class a {

        private final PlayerAdvancements a;
        private final Set<CriterionTrigger.a<CriterionTriggerItemDurabilityChanged.b>> b = Sets.newHashSet();

        public a(PlayerAdvancements advancementdataplayer) {
            this.a = advancementdataplayer;
        }

        public boolean a() {
            return this.b.isEmpty();
        }

        public void a(CriterionTrigger.a<CriterionTriggerItemDurabilityChanged.b> criteriontrigger_a) {
            this.b.add(criteriontrigger_a);
        }

        public void b(CriterionTrigger.a<CriterionTriggerItemDurabilityChanged.b> criteriontrigger_a) {
            this.b.remove(criteriontrigger_a);
        }

        public void a(ItemStack itemstack, int i) {
            ArrayList arraylist = null;
            Iterator iterator = this.b.iterator();

            CriterionTrigger.a criteriontrigger_a;

            while (iterator.hasNext()) {
                criteriontrigger_a = (CriterionTrigger.a) iterator.next();
                if (((CriterionTriggerItemDurabilityChanged.b) criteriontrigger_a.a()).a(itemstack, i)) {
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
        private final MinMaxBounds c;

        public b(ItemPredicate criterionconditionitem, MinMaxBounds criterionconditionvalue, MinMaxBounds criterionconditionvalue1) {
            super(ItemDurabilityTrigger.ID);
            this.a = criterionconditionitem;
            this.b = criterionconditionvalue;
            this.c = criterionconditionvalue1;
        }

        public boolean a(ItemStack itemstack, int i) {
            return !this.a.test(itemstack) ? false : (!this.b.test((float) (itemstack.getMaxDamage() - i)) ? false : this.c.test((float) (itemstack.getItemDamage() - i)));
        }
    }
}
