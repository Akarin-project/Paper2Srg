package net.minecraft.world.storage.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Random;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.server.LootEnchantLevel.a;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public class EnchantWithLevels extends LootFunction {

    private final RandomValueRange randomLevel;
    private final boolean isTreasure;

    public EnchantWithLevels(LootCondition[] alootitemcondition, RandomValueRange lootvaluebounds, boolean flag) {
        super(alootitemcondition);
        this.randomLevel = lootvaluebounds;
        this.isTreasure = flag;
    }

    public ItemStack apply(ItemStack itemstack, Random random, LootContext loottableinfo) {
        return EnchantmentHelper.addRandomEnchantment(random, itemstack, this.randomLevel.generateInt(random), this.isTreasure);
    }

    public static class a extends LootItemFunction.a<EnchantWithLevels> {

        public a() {
            super(new ResourceLocation("enchant_with_levels"), EnchantWithLevels.class);
        }

        public void a(JsonObject jsonobject, EnchantWithLevels lootenchantlevel, JsonSerializationContext jsonserializationcontext) {
            jsonobject.add("levels", jsonserializationcontext.serialize(lootenchantlevel.randomLevel));
            jsonobject.addProperty("treasure", Boolean.valueOf(lootenchantlevel.isTreasure));
        }

        public EnchantWithLevels a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext, LootCondition[] alootitemcondition) {
            RandomValueRange lootvaluebounds = (RandomValueRange) JsonUtils.deserializeClass(jsonobject, "levels", jsondeserializationcontext, RandomValueRange.class);
            boolean flag = JsonUtils.getBoolean(jsonobject, "treasure", false);

            return new EnchantWithLevels(alootitemcondition, lootvaluebounds, flag);
        }

        public LootFunction b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext, LootCondition[] alootitemcondition) {
            return this.a(jsonobject, jsondeserializationcontext, alootitemcondition);
        }
    }
}
