package net.minecraft.world.storage.loot.conditions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Random;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.server.LootItemConditionRandomChanceWithLooting.a;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;

public class RandomChanceWithLooting implements LootCondition {

    private final float chance;
    private final float lootingMultiplier;

    public RandomChanceWithLooting(float f, float f1) {
        this.chance = f;
        this.lootingMultiplier = f1;
    }

    public boolean testCondition(Random random, LootContext loottableinfo) {
        int i = 0;

        if (loottableinfo.getKiller() instanceof EntityLivingBase) {
            i = EnchantmentHelper.getLootingModifier((EntityLivingBase) loottableinfo.getKiller());
        }

        return random.nextFloat() < this.chance + (float) i * this.lootingMultiplier;
    }

    public static class a extends LootItemCondition.a<RandomChanceWithLooting> {

        protected a() {
            super(new ResourceLocation("random_chance_with_looting"), RandomChanceWithLooting.class);
        }

        public void a(JsonObject jsonobject, RandomChanceWithLooting lootitemconditionrandomchancewithlooting, JsonSerializationContext jsonserializationcontext) {
            jsonobject.addProperty("chance", Float.valueOf(lootitemconditionrandomchancewithlooting.chance));
            jsonobject.addProperty("looting_multiplier", Float.valueOf(lootitemconditionrandomchancewithlooting.lootingMultiplier));
        }

        public RandomChanceWithLooting a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
            return new RandomChanceWithLooting(JsonUtils.getFloat(jsonobject, "chance"), JsonUtils.getFloat(jsonobject, "looting_multiplier"));
        }

        public LootCondition b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
            return this.a(jsonobject, jsondeserializationcontext);
        }
    }
}
