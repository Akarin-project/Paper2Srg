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
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.CriterionTriggerInventoryChanged.a;
import net.minecraft.server.CriterionTriggerInventoryChanged.b;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class InventoryChangeTrigger implements ICriterionTrigger<CriterionTriggerInventoryChanged.b> {

    private static final ResourceLocation ID = new ResourceLocation("inventory_changed");
    private final Map<PlayerAdvancements, CriterionTriggerInventoryChanged.a> listeners = Maps.newHashMap();

    public InventoryChangeTrigger() {}

    public ResourceLocation getId() {
        return InventoryChangeTrigger.ID;
    }

    public void a(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerInventoryChanged.b> criteriontrigger_a) {
        CriterionTriggerInventoryChanged.a criteriontriggerinventorychanged_a = (CriterionTriggerInventoryChanged.a) this.listeners.get(advancementdataplayer);

        if (criteriontriggerinventorychanged_a == null) {
            criteriontriggerinventorychanged_a = new CriterionTriggerInventoryChanged.a(advancementdataplayer);
            this.listeners.put(advancementdataplayer, criteriontriggerinventorychanged_a);
        }

        criteriontriggerinventorychanged_a.a(criteriontrigger_a);
    }

    public void b(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerInventoryChanged.b> criteriontrigger_a) {
        CriterionTriggerInventoryChanged.a criteriontriggerinventorychanged_a = (CriterionTriggerInventoryChanged.a) this.listeners.get(advancementdataplayer);

        if (criteriontriggerinventorychanged_a != null) {
            criteriontriggerinventorychanged_a.b(criteriontrigger_a);
            if (criteriontriggerinventorychanged_a.a()) {
                this.listeners.remove(advancementdataplayer);
            }
        }

    }

    public void removeAllListeners(PlayerAdvancements advancementdataplayer) {
        this.listeners.remove(advancementdataplayer);
    }

    public CriterionTriggerInventoryChanged.b b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        JsonObject jsonobject1 = JsonUtils.getJsonObject(jsonobject, "slots", new JsonObject());
        MinMaxBounds criterionconditionvalue = MinMaxBounds.deserialize(jsonobject1.get("occupied"));
        MinMaxBounds criterionconditionvalue1 = MinMaxBounds.deserialize(jsonobject1.get("full"));
        MinMaxBounds criterionconditionvalue2 = MinMaxBounds.deserialize(jsonobject1.get("empty"));
        ItemPredicate[] acriterionconditionitem = ItemPredicate.deserializeArray(jsonobject.get("items"));

        return new CriterionTriggerInventoryChanged.b(criterionconditionvalue, criterionconditionvalue1, criterionconditionvalue2, acriterionconditionitem);
    }

    public void trigger(EntityPlayerMP entityplayer, InventoryPlayer playerinventory) {
        CriterionTriggerInventoryChanged.a criteriontriggerinventorychanged_a = (CriterionTriggerInventoryChanged.a) this.listeners.get(entityplayer.getAdvancements());

        if (criteriontriggerinventorychanged_a != null) {
            criteriontriggerinventorychanged_a.a(playerinventory);
        }

    }

    public ICriterionInstance deserializeInstance(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        return this.b(jsonobject, jsondeserializationcontext);
    }

    static class a {

        private final PlayerAdvancements a;
        private final Set<CriterionTrigger.a<CriterionTriggerInventoryChanged.b>> b = Sets.newHashSet();

        public a(PlayerAdvancements advancementdataplayer) {
            this.a = advancementdataplayer;
        }

        public boolean a() {
            return this.b.isEmpty();
        }

        public void a(CriterionTrigger.a<CriterionTriggerInventoryChanged.b> criteriontrigger_a) {
            this.b.add(criteriontrigger_a);
        }

        public void b(CriterionTrigger.a<CriterionTriggerInventoryChanged.b> criteriontrigger_a) {
            this.b.remove(criteriontrigger_a);
        }

        public void a(InventoryPlayer playerinventory) {
            ArrayList arraylist = null;
            Iterator iterator = this.b.iterator();

            CriterionTrigger.a criteriontrigger_a;

            while (iterator.hasNext()) {
                criteriontrigger_a = (CriterionTrigger.a) iterator.next();
                if (((CriterionTriggerInventoryChanged.b) criteriontrigger_a.a()).a(playerinventory)) {
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

        private final MinMaxBounds a;
        private final MinMaxBounds b;
        private final MinMaxBounds c;
        private final ItemPredicate[] d;

        public b(MinMaxBounds criterionconditionvalue, MinMaxBounds criterionconditionvalue1, MinMaxBounds criterionconditionvalue2, ItemPredicate[] acriterionconditionitem) {
            super(InventoryChangeTrigger.ID);
            this.a = criterionconditionvalue;
            this.b = criterionconditionvalue1;
            this.c = criterionconditionvalue2;
            this.d = acriterionconditionitem;
        }

        public boolean a(InventoryPlayer playerinventory) {
            int i = 0;
            int j = 0;
            int k = 0;
            ArrayList arraylist = Lists.newArrayList(this.d);

            for (int l = 0; l < playerinventory.getSizeInventory(); ++l) {
                ItemStack itemstack = playerinventory.getStackInSlot(l);

                if (itemstack.isEmpty()) {
                    ++j;
                } else {
                    ++k;
                    if (itemstack.getCount() >= itemstack.getMaxStackSize()) {
                        ++i;
                    }

                    Iterator iterator = arraylist.iterator();

                    while (iterator.hasNext()) {
                        ItemPredicate criterionconditionitem = (ItemPredicate) iterator.next();

                        if (criterionconditionitem.test(itemstack)) {
                            iterator.remove();
                        }
                    }
                }
            }

            if (!this.b.test((float) i)) {
                return false;
            } else if (!this.c.test((float) j)) {
                return false;
            } else if (!this.a.test((float) k)) {
                return false;
            } else if (!arraylist.isEmpty()) {
                return false;
            } else {
                return true;
            }
        }
    }
}
