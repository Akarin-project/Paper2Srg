package net.minecraft.advancements.critereon;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.Nullable;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class EnchantmentPredicate {

    public static final EnchantmentPredicate ANY = new EnchantmentPredicate();
    private final Enchantment enchantment;
    private final MinMaxBounds levels;

    public EnchantmentPredicate() {
        this.enchantment = null;
        this.levels = MinMaxBounds.UNBOUNDED;
    }

    public EnchantmentPredicate(@Nullable Enchantment enchantment, MinMaxBounds criterionconditionvalue) {
        this.enchantment = enchantment;
        this.levels = criterionconditionvalue;
    }

    public boolean test(Map<Enchantment, Integer> map) {
        if (this.enchantment != null) {
            if (!map.containsKey(this.enchantment)) {
                return false;
            }

            int i = ((Integer) map.get(this.enchantment)).intValue();

            if (this.levels != null && !this.levels.test((float) i)) {
                return false;
            }
        } else if (this.levels != null) {
            Iterator iterator = map.values().iterator();

            Integer integer;

            do {
                if (!iterator.hasNext()) {
                    return false;
                }

                integer = (Integer) iterator.next();
            } while (!this.levels.test((float) integer.intValue()));

            return true;
        }

        return true;
    }

    public static EnchantmentPredicate deserialize(@Nullable JsonElement jsonelement) {
        if (jsonelement != null && !jsonelement.isJsonNull()) {
            JsonObject jsonobject = JsonUtils.getJsonObject(jsonelement, "enchantment");
            Enchantment enchantment = null;

            if (jsonobject.has("enchantment")) {
                ResourceLocation minecraftkey = new ResourceLocation(JsonUtils.getString(jsonobject, "enchantment"));

                enchantment = (Enchantment) Enchantment.REGISTRY.getObject(minecraftkey);
                if (enchantment == null) {
                    throw new JsonSyntaxException("Unknown enchantment \'" + minecraftkey + "\'");
                }
            }

            MinMaxBounds criterionconditionvalue = MinMaxBounds.deserialize(jsonobject.get("levels"));

            return new EnchantmentPredicate(enchantment, criterionconditionvalue);
        } else {
            return EnchantmentPredicate.ANY;
        }
    }

    public static EnchantmentPredicate[] deserializeArray(@Nullable JsonElement jsonelement) {
        if (jsonelement != null && !jsonelement.isJsonNull()) {
            JsonArray jsonarray = JsonUtils.getJsonArray(jsonelement, "enchantments");
            EnchantmentPredicate[] acriterionconditionenchantments = new EnchantmentPredicate[jsonarray.size()];

            for (int i = 0; i < acriterionconditionenchantments.length; ++i) {
                acriterionconditionenchantments[i] = deserialize(jsonarray.get(i));
            }

            return acriterionconditionenchantments;
        } else {
            return new EnchantmentPredicate[0];
        }
    }
}
