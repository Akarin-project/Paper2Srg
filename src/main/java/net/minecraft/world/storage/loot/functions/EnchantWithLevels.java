package net.minecraft.world.storage.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Random;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public class EnchantWithLevels extends LootFunction {

    private final RandomValueRange field_186577_a;
    private final boolean field_186578_b;

    public EnchantWithLevels(LootCondition[] alootitemcondition, RandomValueRange lootvaluebounds, boolean flag) {
        super(alootitemcondition);
        this.field_186577_a = lootvaluebounds;
        this.field_186578_b = flag;
    }

    @Override
    public ItemStack func_186553_a(ItemStack itemstack, Random random, LootContext loottableinfo) {
        return EnchantmentHelper.func_77504_a(random, itemstack, this.field_186577_a.func_186511_a(random), this.field_186578_b);
    }

    public static class a extends LootFunction.a<EnchantWithLevels> {

        public a() {
            super(new ResourceLocation("enchant_with_levels"), EnchantWithLevels.class);
        }

        @Override
        public void a(JsonObject jsonobject, EnchantWithLevels lootenchantlevel, JsonSerializationContext jsonserializationcontext) {
            jsonobject.add("levels", jsonserializationcontext.serialize(lootenchantlevel.field_186577_a));
            jsonobject.addProperty("treasure", Boolean.valueOf(lootenchantlevel.field_186578_b));
        }

        public EnchantWithLevels a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext, LootCondition[] alootitemcondition) {
            RandomValueRange lootvaluebounds = JsonUtils.func_188174_a(jsonobject, "levels", jsondeserializationcontext, RandomValueRange.class);
            boolean flag = JsonUtils.func_151209_a(jsonobject, "treasure", false);

            return new EnchantWithLevels(alootitemcondition, lootvaluebounds, flag);
        }

        @Override
        public EnchantWithLevels b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext, LootCondition[] alootitemcondition) {
            return this.a(jsonobject, jsondeserializationcontext, alootitemcondition);
        }
    }
}
