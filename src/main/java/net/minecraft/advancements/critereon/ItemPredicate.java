package net.minecraft.advancements.critereon;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.util.Map;
import javax.annotation.Nullable;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class ItemPredicate {

    public static final ItemPredicate field_192495_a = new ItemPredicate();
    private final Item field_192496_b;
    private final Integer field_192497_c;
    private final MinMaxBounds field_192498_d;
    private final MinMaxBounds field_193444_e;
    private final EnchantmentPredicate[] field_192499_e;
    private final PotionType field_192500_f;
    private final NBTPredicate field_193445_h;

    public ItemPredicate() {
        this.field_192496_b = null;
        this.field_192497_c = null;
        this.field_192500_f = null;
        this.field_192498_d = MinMaxBounds.field_192516_a;
        this.field_193444_e = MinMaxBounds.field_192516_a;
        this.field_192499_e = new EnchantmentPredicate[0];
        this.field_193445_h = NBTPredicate.field_193479_a;
    }

    public ItemPredicate(@Nullable Item item, @Nullable Integer integer, MinMaxBounds criterionconditionvalue, MinMaxBounds criterionconditionvalue1, EnchantmentPredicate[] acriterionconditionenchantments, @Nullable PotionType potionregistry, NBTPredicate criterionconditionnbt) {
        this.field_192496_b = item;
        this.field_192497_c = integer;
        this.field_192498_d = criterionconditionvalue;
        this.field_193444_e = criterionconditionvalue1;
        this.field_192499_e = acriterionconditionenchantments;
        this.field_192500_f = potionregistry;
        this.field_193445_h = criterionconditionnbt;
    }

    public boolean func_192493_a(ItemStack itemstack) {
        if (this.field_192496_b != null && itemstack.func_77973_b() != this.field_192496_b) {
            return false;
        } else if (this.field_192497_c != null && itemstack.func_77960_j() != this.field_192497_c.intValue()) {
            return false;
        } else if (!this.field_192498_d.func_192514_a((float) itemstack.func_190916_E())) {
            return false;
        } else if (this.field_193444_e != MinMaxBounds.field_192516_a && !itemstack.func_77984_f()) {
            return false;
        } else if (!this.field_193444_e.func_192514_a((float) (itemstack.func_77958_k() - itemstack.func_77952_i()))) {
            return false;
        } else if (!this.field_193445_h.func_193478_a(itemstack)) {
            return false;
        } else {
            Map map = EnchantmentHelper.func_82781_a(itemstack);

            for (int i = 0; i < this.field_192499_e.length; ++i) {
                if (!this.field_192499_e[i].func_192463_a(map)) {
                    return false;
                }
            }

            PotionType potionregistry = PotionUtils.func_185191_c(itemstack);

            if (this.field_192500_f != null && this.field_192500_f != potionregistry) {
                return false;
            } else {
                return true;
            }
        }
    }

    public static ItemPredicate func_192492_a(@Nullable JsonElement jsonelement) {
        if (jsonelement != null && !jsonelement.isJsonNull()) {
            JsonObject jsonobject = JsonUtils.func_151210_l(jsonelement, "item");
            MinMaxBounds criterionconditionvalue = MinMaxBounds.func_192515_a(jsonobject.get("count"));
            MinMaxBounds criterionconditionvalue1 = MinMaxBounds.func_192515_a(jsonobject.get("durability"));
            Integer integer = jsonobject.has("data") ? Integer.valueOf(JsonUtils.func_151203_m(jsonobject, "data")) : null;
            NBTPredicate criterionconditionnbt = NBTPredicate.func_193476_a(jsonobject.get("nbt"));
            Item item = null;

            if (jsonobject.has("item")) {
                ResourceLocation minecraftkey = new ResourceLocation(JsonUtils.func_151200_h(jsonobject, "item"));

                item = (Item) Item.field_150901_e.func_82594_a(minecraftkey);
                if (item == null) {
                    throw new JsonSyntaxException("Unknown item id \'" + minecraftkey + "\'");
                }
            }

            EnchantmentPredicate[] acriterionconditionenchantments = EnchantmentPredicate.func_192465_b(jsonobject.get("enchantments"));
            PotionType potionregistry = null;

            if (jsonobject.has("potion")) {
                ResourceLocation minecraftkey1 = new ResourceLocation(JsonUtils.func_151200_h(jsonobject, "potion"));

                if (!PotionType.field_185176_a.func_148741_d(minecraftkey1)) {
                    throw new JsonSyntaxException("Unknown potion \'" + minecraftkey1 + "\'");
                }

                potionregistry = (PotionType) PotionType.field_185176_a.func_82594_a(minecraftkey1);
            }

            return new ItemPredicate(item, integer, criterionconditionvalue, criterionconditionvalue1, acriterionconditionenchantments, potionregistry, criterionconditionnbt);
        } else {
            return ItemPredicate.field_192495_a;
        }
    }

    public static ItemPredicate[] func_192494_b(@Nullable JsonElement jsonelement) {
        if (jsonelement != null && !jsonelement.isJsonNull()) {
            JsonArray jsonarray = JsonUtils.func_151207_m(jsonelement, "items");
            ItemPredicate[] acriterionconditionitem = new ItemPredicate[jsonarray.size()];

            for (int i = 0; i < acriterionconditionitem.length; ++i) {
                acriterionconditionitem[i] = func_192492_a(jsonarray.get(i));
            }

            return acriterionconditionitem;
        } else {
            return new ItemPredicate[0];
        }
    }
}
