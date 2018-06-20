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

public class UsedTotemTrigger implements ICriterionTrigger<UsedTotemTrigger.b> {

    private static final ResourceLocation field_193188_a = new ResourceLocation("used_totem");
    private final Map<PlayerAdvancements, UsedTotemTrigger.a> field_193189_b = Maps.newHashMap();

    public UsedTotemTrigger() {}

    @Override
    public ResourceLocation func_192163_a() {
        return UsedTotemTrigger.field_193188_a;
    }

    @Override
    public void a(PlayerAdvancements advancementdataplayer, ICriterionTrigger.a<UsedTotemTrigger.b> criteriontrigger_a) {
        UsedTotemTrigger.a criteriontriggerusedtotem_a = this.field_193189_b.get(advancementdataplayer);

        if (criteriontriggerusedtotem_a == null) {
            criteriontriggerusedtotem_a = new UsedTotemTrigger.a(advancementdataplayer);
            this.field_193189_b.put(advancementdataplayer, criteriontriggerusedtotem_a);
        }

        criteriontriggerusedtotem_a.a(criteriontrigger_a);
    }

    @Override
    public void b(PlayerAdvancements advancementdataplayer, ICriterionTrigger.a<UsedTotemTrigger.b> criteriontrigger_a) {
        UsedTotemTrigger.a criteriontriggerusedtotem_a = this.field_193189_b.get(advancementdataplayer);

        if (criteriontriggerusedtotem_a != null) {
            criteriontriggerusedtotem_a.b(criteriontrigger_a);
            if (criteriontriggerusedtotem_a.a()) {
                this.field_193189_b.remove(advancementdataplayer);
            }
        }

    }

    @Override
    public void func_192167_a(PlayerAdvancements advancementdataplayer) {
        this.field_193189_b.remove(advancementdataplayer);
    }

    public UsedTotemTrigger.b b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        ItemPredicate criterionconditionitem = ItemPredicate.func_192492_a(jsonobject.get("item"));

        return new UsedTotemTrigger.b(criterionconditionitem);
    }

    public void func_193187_a(EntityPlayerMP entityplayer, ItemStack itemstack) {
        UsedTotemTrigger.a criteriontriggerusedtotem_a = this.field_193189_b.get(entityplayer.func_192039_O());

        if (criteriontriggerusedtotem_a != null) {
            criteriontriggerusedtotem_a.a(itemstack);
        }

    }

    @Override
    public b func_192166_a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        return this.b(jsonobject, jsondeserializationcontext);
    }

    static class a {

        private final PlayerAdvancements a;
        private final Set<ICriterionTrigger.a<UsedTotemTrigger.b>> b = Sets.newHashSet();

        public a(PlayerAdvancements advancementdataplayer) {
            this.a = advancementdataplayer;
        }

        public boolean a() {
            return this.b.isEmpty();
        }

        public void a(ICriterionTrigger.a<UsedTotemTrigger.b> criteriontrigger_a) {
            this.b.add(criteriontrigger_a);
        }

        public void b(ICriterionTrigger.a<UsedTotemTrigger.b> criteriontrigger_a) {
            this.b.remove(criteriontrigger_a);
        }

        public void a(ItemStack itemstack) {
            ArrayList arraylist = null;
            Iterator iterator = this.b.iterator();

            ICriterionTrigger.a criteriontrigger_a;

            while (iterator.hasNext()) {
                criteriontrigger_a = (ICriterionTrigger.a) iterator.next();
                if (((UsedTotemTrigger.b) criteriontrigger_a.a()).a(itemstack)) {
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

        public b(ItemPredicate criterionconditionitem) {
            super(UsedTotemTrigger.field_193188_a);
            this.a = criterionconditionitem;
        }

        public boolean a(ItemStack itemstack) {
            return this.a.func_192493_a(itemstack);
        }
    }
}
