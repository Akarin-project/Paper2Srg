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

    public static final ItemPredicate ANY = new ItemPredicate();
    private final Item item;
    private final Integer data;
    private final MinMaxBounds count;
    private final MinMaxBounds durability;
    private final EnchantmentPredicate[] enchantments;
    private final PotionType potion;
    private final NBTPredicate nbt;

    public ItemPredicate() {
        this.item = null;
        this.data = null;
        this.potion = null;
        this.count = MinMaxBounds.UNBOUNDED;
        this.durability = MinMaxBounds.UNBOUNDED;
        this.enchantments = new EnchantmentPredicate[0];
        this.nbt = NBTPredicate.ANY;
    }

    public ItemPredicate(@Nullable Item item, @Nullable Integer integer, MinMaxBounds criterionconditionvalue, MinMaxBounds criterionconditionvalue1, EnchantmentPredicate[] acriterionconditionenchantments, @Nullable PotionType potionregistry, NBTPredicate criterionconditionnbt) {
        this.item = item;
        this.data = integer;
        this.count = criterionconditionvalue;
        this.durability = criterionconditionvalue1;
        this.enchantments = acriterionconditionenchantments;
        this.potion = potionregistry;
        this.nbt = criterionconditionnbt;
    }

    public boolean test(ItemStack itemstack) {
        if (this.item != null && itemstack.getItem() != this.item) {
            return false;
        } else if (this.data != null && itemstack.getMetadata() != this.data.intValue()) {
            return false;
        } else if (!this.count.test((float) itemstack.getCount())) {
            return false;
        } else if (this.durability != MinMaxBounds.UNBOUNDED && !itemstack.isItemStackDamageable()) {
            return false;
        } else if (!this.durability.test((float) (itemstack.getMaxDamage() - itemstack.getItemDamage()))) {
            return false;
        } else if (!this.nbt.test(itemstack)) {
            return false;
        } else {
            Map map = EnchantmentHelper.getEnchantments(itemstack);

            for (int i = 0; i < this.enchantments.length; ++i) {
                if (!this.enchantments[i].test(map)) {
                    return false;
                }
            }

            PotionType potionregistry = PotionUtils.getPotionFromItem(itemstack);

            if (this.potion != null && this.potion != potionregistry) {
                return false;
            } else {
                return true;
            }
        }
    }

    public static ItemPredicate deserialize(@Nullable JsonElement jsonelement) {
        if (jsonelement != null && !jsonelement.isJsonNull()) {
            JsonObject jsonobject = JsonUtils.getJsonObject(jsonelement, "item");
            MinMaxBounds criterionconditionvalue = MinMaxBounds.deserialize(jsonobject.get("count"));
            MinMaxBounds criterionconditionvalue1 = MinMaxBounds.deserialize(jsonobject.get("durability"));
            Integer integer = jsonobject.has("data") ? Integer.valueOf(JsonUtils.getInt(jsonobject, "data")) : null;
            NBTPredicate criterionconditionnbt = NBTPredicate.deserialize(jsonobject.get("nbt"));
            Item item = null;

            if (jsonobject.has("item")) {
                ResourceLocation minecraftkey = new ResourceLocation(JsonUtils.getString(jsonobject, "item"));

                item = (Item) Item.REGISTRY.getObject(minecraftkey);
                if (item == null) {
                    throw new JsonSyntaxException("Unknown item id \'" + minecraftkey + "\'");
                }
            }

            EnchantmentPredicate[] acriterionconditionenchantments = EnchantmentPredicate.deserializeArray(jsonobject.get("enchantments"));
            PotionType potionregistry = null;

            if (jsonobject.has("potion")) {
                ResourceLocation minecraftkey1 = new ResourceLocation(JsonUtils.getString(jsonobject, "potion"));

                if (!PotionType.REGISTRY.containsKey(minecraftkey1)) {
                    throw new JsonSyntaxException("Unknown potion \'" + minecraftkey1 + "\'");
                }

                potionregistry = (PotionType) PotionType.REGISTRY.getObject(minecraftkey1);
            }

            return new ItemPredicate(item, integer, criterionconditionvalue, criterionconditionvalue1, acriterionconditionenchantments, potionregistry, criterionconditionnbt);
        } else {
            return ItemPredicate.ANY;
        }
    }

    public static ItemPredicate[] deserializeArray(@Nullable JsonElement jsonelement) {
        if (jsonelement != null && !jsonelement.isJsonNull()) {
            JsonArray jsonarray = JsonUtils.getJsonArray(jsonelement, "items");
            ItemPredicate[] acriterionconditionitem = new ItemPredicate[jsonarray.size()];

            for (int i = 0; i < acriterionconditionitem.length; ++i) {
                acriterionconditionitem[i] = deserialize(jsonarray.get(i));
            }

            return acriterionconditionitem;
        } else {
            return new ItemPredicate[0];
        }
    }
}
