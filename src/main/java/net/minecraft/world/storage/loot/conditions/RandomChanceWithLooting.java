package net.minecraft.world.storage.loot.conditions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Random;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;

public class RandomChanceWithLooting implements LootCondition {

    private final float field_186627_a;
    private final float field_186628_b;

    public RandomChanceWithLooting(float f, float f1) {
        this.field_186627_a = f;
        this.field_186628_b = f1;
    }

    @Override
    public boolean func_186618_a(Random random, LootContext loottableinfo) {
        int i = 0;

        if (loottableinfo.func_186492_c() instanceof EntityLivingBase) {
            i = EnchantmentHelper.func_185283_h((EntityLivingBase) loottableinfo.func_186492_c());
        }

        return random.nextFloat() < this.field_186627_a + i * this.field_186628_b;
    }

    public static class a extends LootCondition.a<RandomChanceWithLooting> {

        protected a() {
            super(new ResourceLocation("random_chance_with_looting"), RandomChanceWithLooting.class);
        }

        @Override
        public void a(JsonObject jsonobject, RandomChanceWithLooting lootitemconditionrandomchancewithlooting, JsonSerializationContext jsonserializationcontext) {
            jsonobject.addProperty("chance", Float.valueOf(lootitemconditionrandomchancewithlooting.field_186627_a));
            jsonobject.addProperty("looting_multiplier", Float.valueOf(lootitemconditionrandomchancewithlooting.field_186628_b));
        }

        public RandomChanceWithLooting a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
            return new RandomChanceWithLooting(JsonUtils.func_151217_k(jsonobject, "chance"), JsonUtils.func_151217_k(jsonobject, "looting_multiplier"));
        }

        @Override
        public RandomChanceWithLooting b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
            return this.a(jsonobject, jsondeserializationcontext);
        }
    }
}
