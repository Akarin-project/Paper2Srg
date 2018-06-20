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
import net.minecraft.util.ResourceLocation;

public class EnchantedItemTrigger implements ICriterionTrigger<EnchantedItemTrigger.b> {

    private static final ResourceLocation field_192191_a = new ResourceLocation("enchanted_item");
    private final Map<PlayerAdvancements, EnchantedItemTrigger.a> field_192192_b = Maps.newHashMap();

    public EnchantedItemTrigger() {}

    @Override
    public ResourceLocation func_192163_a() {
        return EnchantedItemTrigger.field_192191_a;
    }

    @Override
    public void a(PlayerAdvancements advancementdataplayer, ICriterionTrigger.a<EnchantedItemTrigger.b> criteriontrigger_a) {
        EnchantedItemTrigger.a criteriontriggerenchanteditem_a = this.field_192192_b.get(advancementdataplayer);

        if (criteriontriggerenchanteditem_a == null) {
            criteriontriggerenchanteditem_a = new EnchantedItemTrigger.a(advancementdataplayer);
            this.field_192192_b.put(advancementdataplayer, criteriontriggerenchanteditem_a);
        }

        criteriontriggerenchanteditem_a.a(criteriontrigger_a);
    }

    @Override
    public void b(PlayerAdvancements advancementdataplayer, ICriterionTrigger.a<EnchantedItemTrigger.b> criteriontrigger_a) {
        EnchantedItemTrigger.a criteriontriggerenchanteditem_a = this.field_192192_b.get(advancementdataplayer);

        if (criteriontriggerenchanteditem_a != null) {
            criteriontriggerenchanteditem_a.b(criteriontrigger_a);
            if (criteriontriggerenchanteditem_a.a()) {
                this.field_192192_b.remove(advancementdataplayer);
            }
        }

    }

    @Override
    public void func_192167_a(PlayerAdvancements advancementdataplayer) {
        this.field_192192_b.remove(advancementdataplayer);
    }

    public EnchantedItemTrigger.b b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        ItemPredicate criterionconditionitem = ItemPredicate.func_192492_a(jsonobject.get("item"));
        MinMaxBounds criterionconditionvalue = MinMaxBounds.func_192515_a(jsonobject.get("levels"));

        return new EnchantedItemTrigger.b(criterionconditionitem, criterionconditionvalue);
    }

    public void func_192190_a(EntityPlayerMP entityplayer, ItemStack itemstack, int i) {
        EnchantedItemTrigger.a criteriontriggerenchanteditem_a = this.field_192192_b.get(entityplayer.func_192039_O());

        if (criteriontriggerenchanteditem_a != null) {
            criteriontriggerenchanteditem_a.a(itemstack, i);
        }

    }

    @Override
    public b func_192166_a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        return this.b(jsonobject, jsondeserializationcontext);
    }

    static class a {

        private final PlayerAdvancements a;
        private final Set<ICriterionTrigger.a<EnchantedItemTrigger.b>> b = Sets.newHashSet();

        public a(PlayerAdvancements advancementdataplayer) {
            this.a = advancementdataplayer;
        }

        public boolean a() {
            return this.b.isEmpty();
        }

        public void a(ICriterionTrigger.a<EnchantedItemTrigger.b> criteriontrigger_a) {
            this.b.add(criteriontrigger_a);
        }

        public void b(ICriterionTrigger.a<EnchantedItemTrigger.b> criteriontrigger_a) {
            this.b.remove(criteriontrigger_a);
        }

        public void a(ItemStack itemstack, int i) {
            ArrayList arraylist = null;
            Iterator iterator = this.b.iterator();

            ICriterionTrigger.a criteriontrigger_a;

            while (iterator.hasNext()) {
                criteriontrigger_a = (ICriterionTrigger.a) iterator.next();
                if (((EnchantedItemTrigger.b) criteriontrigger_a.a()).a(itemstack, i)) {
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

        private final ItemPredicate a;
        private final MinMaxBounds b;

        public b(ItemPredicate criterionconditionitem, MinMaxBounds criterionconditionvalue) {
            super(EnchantedItemTrigger.field_192191_a);
            this.a = criterionconditionitem;
            this.b = criterionconditionvalue;
        }

        public boolean a(ItemStack itemstack, int i) {
            return !this.a.func_192493_a(itemstack) ? false : this.b.func_192514_a(i);
        }
    }
}
