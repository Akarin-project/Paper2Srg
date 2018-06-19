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

    public static final EnchantmentPredicate field_192466_a = new EnchantmentPredicate();
    private final Enchantment field_192467_b;
    private final MinMaxBounds field_192468_c;

    public EnchantmentPredicate() {
        this.field_192467_b = null;
        this.field_192468_c = MinMaxBounds.field_192516_a;
    }

    public EnchantmentPredicate(@Nullable Enchantment enchantment, MinMaxBounds criterionconditionvalue) {
        this.field_192467_b = enchantment;
        this.field_192468_c = criterionconditionvalue;
    }

    public boolean func_192463_a(Map<Enchantment, Integer> map) {
        if (this.field_192467_b != null) {
            if (!map.containsKey(this.field_192467_b)) {
                return false;
            }

            int i = ((Integer) map.get(this.field_192467_b)).intValue();

            if (this.field_192468_c != null && !this.field_192468_c.func_192514_a((float) i)) {
                return false;
            }
        } else if (this.field_192468_c != null) {
            Iterator iterator = map.values().iterator();

            Integer integer;

            do {
                if (!iterator.hasNext()) {
                    return false;
                }

                integer = (Integer) iterator.next();
            } while (!this.field_192468_c.func_192514_a((float) integer.intValue()));

            return true;
        }

        return true;
    }

    public static EnchantmentPredicate func_192464_a(@Nullable JsonElement jsonelement) {
        if (jsonelement != null && !jsonelement.isJsonNull()) {
            JsonObject jsonobject = JsonUtils.func_151210_l(jsonelement, "enchantment");
            Enchantment enchantment = null;

            if (jsonobject.has("enchantment")) {
                ResourceLocation minecraftkey = new ResourceLocation(JsonUtils.func_151200_h(jsonobject, "enchantment"));

                enchantment = (Enchantment) Enchantment.field_185264_b.func_82594_a(minecraftkey);
                if (enchantment == null) {
                    throw new JsonSyntaxException("Unknown enchantment \'" + minecraftkey + "\'");
                }
            }

            MinMaxBounds criterionconditionvalue = MinMaxBounds.func_192515_a(jsonobject.get("levels"));

            return new EnchantmentPredicate(enchantment, criterionconditionvalue);
        } else {
            return EnchantmentPredicate.field_192466_a;
        }
    }

    public static EnchantmentPredicate[] func_192465_b(@Nullable JsonElement jsonelement) {
        if (jsonelement != null && !jsonelement.isJsonNull()) {
            JsonArray jsonarray = JsonUtils.func_151207_m(jsonelement, "enchantments");
            EnchantmentPredicate[] acriterionconditionenchantments = new EnchantmentPredicate[jsonarray.size()];

            for (int i = 0; i < acriterionconditionenchantments.length; ++i) {
                acriterionconditionenchantments[i] = func_192464_a(jsonarray.get(i));
            }

            return acriterionconditionenchantments;
        } else {
            return new EnchantmentPredicate[0];
        }
    }
}
