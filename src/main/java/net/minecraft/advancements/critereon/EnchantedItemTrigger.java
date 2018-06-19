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

    private static final ResourceLocation field_192191_a = new ResourceLocation("enchanted_item");
    private final Map<PlayerAdvancements, CriterionTriggerEnchantedItem.a> field_192192_b = Maps.newHashMap();

    public EnchantedItemTrigger() {}

    public ResourceLocation func_192163_a() {
        return EnchantedItemTrigger.field_192191_a;
    }

    public void a(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerEnchantedItem.b> criteriontrigger_a) {
        CriterionTriggerEnchantedItem.a criteriontriggerenchanteditem_a = (CriterionTriggerEnchantedItem.a) this.field_192192_b.get(advancementdataplayer);

        if (criteriontriggerenchanteditem_a == null) {
            criteriontriggerenchanteditem_a = new CriterionTriggerEnchantedItem.a(advancementdataplayer);
            this.field_192192_b.put(advancementdataplayer, criteriontriggerenchanteditem_a);
        }

        criteriontriggerenchanteditem_a.a(criteriontrigger_a);
    }

    public void b(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerEnchantedItem.b> criteriontrigger_a) {
        CriterionTriggerEnchantedItem.a criteriontriggerenchanteditem_a = (CriterionTriggerEnchantedItem.a) this.field_192192_b.get(advancementdataplayer);

        if (criteriontriggerenchanteditem_a != null) {
            criteriontriggerenchanteditem_a.b(criteriontrigger_a);
            if (criteriontriggerenchanteditem_a.a()) {
                this.field_192192_b.remove(advancementdataplayer);
            }
        }

    }

    public void func_192167_a(PlayerAdvancements advancementdataplayer) {
        this.field_192192_b.remove(advancementdataplayer);
    }

    public CriterionTriggerEnchantedItem.b b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        ItemPredicate criterionconditionitem = ItemPredicate.func_192492_a(jsonobject.get("item"));
        MinMaxBounds criterionconditionvalue = MinMaxBounds.func_192515_a(jsonobject.get("levels"));

        return new CriterionTriggerEnchantedItem.b(criterionconditionitem, criterionconditionvalue);
    }

    public void func_192190_a(EntityPlayerMP entityplayer, ItemStack itemstack, int i) {
        CriterionTriggerEnchantedItem.a criteriontriggerenchanteditem_a = (CriterionTriggerEnchantedItem.a) this.field_192192_b.get(entityplayer.func_192039_O());

        if (criteriontriggerenchanteditem_a != null) {
            criteriontriggerenchanteditem_a.a(itemstack, i);
        }

    }

    public ICriterionInstance func_192166_a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
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
            super(EnchantedItemTrigger.field_192191_a);
            this.a = criterionconditionitem;
            this.b = criterionconditionvalue;
        }

        public boolean a(ItemStack itemstack, int i) {
            return !this.a.func_192493_a(itemstack) ? false : this.b.func_192514_a((float) i);
        }
    }
}
