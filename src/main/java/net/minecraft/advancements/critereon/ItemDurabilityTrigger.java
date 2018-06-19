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

    private static final ResourceLocation field_193159_a = new ResourceLocation("item_durability_changed");
    private final Map<PlayerAdvancements, CriterionTriggerItemDurabilityChanged.a> field_193160_b = Maps.newHashMap();

    public ItemDurabilityTrigger() {}

    public ResourceLocation func_192163_a() {
        return ItemDurabilityTrigger.field_193159_a;
    }

    public void a(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerItemDurabilityChanged.b> criteriontrigger_a) {
        CriterionTriggerItemDurabilityChanged.a criteriontriggeritemdurabilitychanged_a = (CriterionTriggerItemDurabilityChanged.a) this.field_193160_b.get(advancementdataplayer);

        if (criteriontriggeritemdurabilitychanged_a == null) {
            criteriontriggeritemdurabilitychanged_a = new CriterionTriggerItemDurabilityChanged.a(advancementdataplayer);
            this.field_193160_b.put(advancementdataplayer, criteriontriggeritemdurabilitychanged_a);
        }

        criteriontriggeritemdurabilitychanged_a.a(criteriontrigger_a);
    }

    public void b(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerItemDurabilityChanged.b> criteriontrigger_a) {
        CriterionTriggerItemDurabilityChanged.a criteriontriggeritemdurabilitychanged_a = (CriterionTriggerItemDurabilityChanged.a) this.field_193160_b.get(advancementdataplayer);

        if (criteriontriggeritemdurabilitychanged_a != null) {
            criteriontriggeritemdurabilitychanged_a.b(criteriontrigger_a);
            if (criteriontriggeritemdurabilitychanged_a.a()) {
                this.field_193160_b.remove(advancementdataplayer);
            }
        }

    }

    public void func_192167_a(PlayerAdvancements advancementdataplayer) {
        this.field_193160_b.remove(advancementdataplayer);
    }

    public CriterionTriggerItemDurabilityChanged.b b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        ItemPredicate criterionconditionitem = ItemPredicate.func_192492_a(jsonobject.get("item"));
        MinMaxBounds criterionconditionvalue = MinMaxBounds.func_192515_a(jsonobject.get("durability"));
        MinMaxBounds criterionconditionvalue1 = MinMaxBounds.func_192515_a(jsonobject.get("delta"));

        return new CriterionTriggerItemDurabilityChanged.b(criterionconditionitem, criterionconditionvalue, criterionconditionvalue1);
    }

    public void func_193158_a(EntityPlayerMP entityplayer, ItemStack itemstack, int i) {
        CriterionTriggerItemDurabilityChanged.a criteriontriggeritemdurabilitychanged_a = (CriterionTriggerItemDurabilityChanged.a) this.field_193160_b.get(entityplayer.func_192039_O());

        if (criteriontriggeritemdurabilitychanged_a != null) {
            criteriontriggeritemdurabilitychanged_a.a(itemstack, i);
        }

    }

    public ICriterionInstance func_192166_a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
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
            super(ItemDurabilityTrigger.field_193159_a);
            this.a = criterionconditionitem;
            this.b = criterionconditionvalue;
            this.c = criterionconditionvalue1;
        }

        public boolean a(ItemStack itemstack, int i) {
            return !this.a.func_192493_a(itemstack) ? false : (!this.b.func_192514_a((float) (itemstack.func_77958_k() - i)) ? false : this.c.func_192514_a((float) (itemstack.func_77952_i() - i)));
        }
    }
}
