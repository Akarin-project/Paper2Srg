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
import net.minecraft.server.CriterionTriggerUsedTotem.a;
import net.minecraft.server.CriterionTriggerUsedTotem.b;
import net.minecraft.util.ResourceLocation;

public class UsedTotemTrigger implements ICriterionTrigger<CriterionTriggerUsedTotem.b> {

    private static final ResourceLocation field_193188_a = new ResourceLocation("used_totem");
    private final Map<PlayerAdvancements, CriterionTriggerUsedTotem.a> field_193189_b = Maps.newHashMap();

    public UsedTotemTrigger() {}

    public ResourceLocation func_192163_a() {
        return UsedTotemTrigger.field_193188_a;
    }

    public void a(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerUsedTotem.b> criteriontrigger_a) {
        CriterionTriggerUsedTotem.a criteriontriggerusedtotem_a = (CriterionTriggerUsedTotem.a) this.field_193189_b.get(advancementdataplayer);

        if (criteriontriggerusedtotem_a == null) {
            criteriontriggerusedtotem_a = new CriterionTriggerUsedTotem.a(advancementdataplayer);
            this.field_193189_b.put(advancementdataplayer, criteriontriggerusedtotem_a);
        }

        criteriontriggerusedtotem_a.a(criteriontrigger_a);
    }

    public void b(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerUsedTotem.b> criteriontrigger_a) {
        CriterionTriggerUsedTotem.a criteriontriggerusedtotem_a = (CriterionTriggerUsedTotem.a) this.field_193189_b.get(advancementdataplayer);

        if (criteriontriggerusedtotem_a != null) {
            criteriontriggerusedtotem_a.b(criteriontrigger_a);
            if (criteriontriggerusedtotem_a.a()) {
                this.field_193189_b.remove(advancementdataplayer);
            }
        }

    }

    public void func_192167_a(PlayerAdvancements advancementdataplayer) {
        this.field_193189_b.remove(advancementdataplayer);
    }

    public CriterionTriggerUsedTotem.b b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        ItemPredicate criterionconditionitem = ItemPredicate.func_192492_a(jsonobject.get("item"));

        return new CriterionTriggerUsedTotem.b(criterionconditionitem);
    }

    public void func_193187_a(EntityPlayerMP entityplayer, ItemStack itemstack) {
        CriterionTriggerUsedTotem.a criteriontriggerusedtotem_a = (CriterionTriggerUsedTotem.a) this.field_193189_b.get(entityplayer.func_192039_O());

        if (criteriontriggerusedtotem_a != null) {
            criteriontriggerusedtotem_a.a(itemstack);
        }

    }

    public ICriterionInstance func_192166_a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        return this.b(jsonobject, jsondeserializationcontext);
    }

    static class a {

        private final PlayerAdvancements a;
        private final Set<CriterionTrigger.a<CriterionTriggerUsedTotem.b>> b = Sets.newHashSet();

        public a(PlayerAdvancements advancementdataplayer) {
            this.a = advancementdataplayer;
        }

        public boolean a() {
            return this.b.isEmpty();
        }

        public void a(CriterionTrigger.a<CriterionTriggerUsedTotem.b> criteriontrigger_a) {
            this.b.add(criteriontrigger_a);
        }

        public void b(CriterionTrigger.a<CriterionTriggerUsedTotem.b> criteriontrigger_a) {
            this.b.remove(criteriontrigger_a);
        }

        public void a(ItemStack itemstack) {
            ArrayList arraylist = null;
            Iterator iterator = this.b.iterator();

            CriterionTrigger.a criteriontrigger_a;

            while (iterator.hasNext()) {
                criteriontrigger_a = (CriterionTrigger.a) iterator.next();
                if (((CriterionTriggerUsedTotem.b) criteriontrigger_a.a()).a(itemstack)) {
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
            super(UsedTotemTrigger.field_193188_a);
            this.a = criterionconditionitem;
        }

        public boolean a(ItemStack itemstack) {
            return this.a.func_192493_a(itemstack);
        }
    }
}
