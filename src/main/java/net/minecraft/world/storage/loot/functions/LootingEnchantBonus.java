package net.minecraft.world.storage.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Random;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public class LootingEnchantBonus extends LootFunction {

    private final RandomValueRange field_186563_a;
    private final int field_189971_b;

    public LootingEnchantBonus(LootCondition[] alootitemcondition, RandomValueRange lootvaluebounds, int i) {
        super(alootitemcondition);
        this.field_186563_a = lootvaluebounds;
        this.field_189971_b = i;
    }

    @Override
    public ItemStack func_186553_a(ItemStack itemstack, Random random, LootContext loottableinfo) {
        Entity entity = loottableinfo.func_186492_c();

        if (entity instanceof EntityLivingBase) {
            int i = EnchantmentHelper.func_185283_h((EntityLivingBase) entity);

            if (i == 0) {
                return itemstack;
            }

            float f = i * this.field_186563_a.func_186507_b(random);

            itemstack.func_190917_f(Math.round(f));
            if (this.field_189971_b != 0 && itemstack.func_190916_E() > this.field_189971_b) {
                itemstack.func_190920_e(this.field_189971_b);
            }
        }

        return itemstack;
    }

    public static class a extends LootFunction.a<LootingEnchantBonus> {

        protected a() {
            super(new ResourceLocation("looting_enchant"), LootingEnchantBonus.class);
        }

        @Override
        public void a(JsonObject jsonobject, LootingEnchantBonus lootenchantfunction, JsonSerializationContext jsonserializationcontext) {
            jsonobject.add("count", jsonserializationcontext.serialize(lootenchantfunction.field_186563_a));
            if (lootenchantfunction.field_189971_b > 0) {
                jsonobject.add("limit", jsonserializationcontext.serialize(Integer.valueOf(lootenchantfunction.field_189971_b)));
            }

        }

        public LootingEnchantBonus a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext, LootCondition[] alootitemcondition) {
            int i = JsonUtils.func_151208_a(jsonobject, "limit", 0);

            return new LootingEnchantBonus(alootitemcondition, JsonUtils.func_188174_a(jsonobject, "count", jsondeserializationcontext, RandomValueRange.class), i);
        }

        @Override
        public LootingEnchantBonus b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext, LootCondition[] alootitemcondition) {
            return this.a(jsonobject, jsondeserializationcontext, alootitemcondition);
        }
    }
}
