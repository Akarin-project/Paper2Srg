package net.minecraft.world.storage.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Random;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.server.LootEnchantFunction.a;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public class LootingEnchantBonus extends LootFunction {

    private final RandomValueRange count;
    private final int limit;

    public LootingEnchantBonus(LootCondition[] alootitemcondition, RandomValueRange lootvaluebounds, int i) {
        super(alootitemcondition);
        this.count = lootvaluebounds;
        this.limit = i;
    }

    public ItemStack apply(ItemStack itemstack, Random random, LootContext loottableinfo) {
        Entity entity = loottableinfo.getKiller();

        if (entity instanceof EntityLivingBase) {
            int i = EnchantmentHelper.getLootingModifier((EntityLivingBase) entity);

            if (i == 0) {
                return itemstack;
            }

            float f = (float) i * this.count.generateFloat(random);

            itemstack.grow(Math.round(f));
            if (this.limit != 0 && itemstack.getCount() > this.limit) {
                itemstack.setCount(this.limit);
            }
        }

        return itemstack;
    }

    public static class a extends LootItemFunction.a<LootingEnchantBonus> {

        protected a() {
            super(new ResourceLocation("looting_enchant"), LootingEnchantBonus.class);
        }

        public void a(JsonObject jsonobject, LootingEnchantBonus lootenchantfunction, JsonSerializationContext jsonserializationcontext) {
            jsonobject.add("count", jsonserializationcontext.serialize(lootenchantfunction.count));
            if (lootenchantfunction.limit > 0) {
                jsonobject.add("limit", jsonserializationcontext.serialize(Integer.valueOf(lootenchantfunction.limit)));
            }

        }

        public LootingEnchantBonus a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext, LootCondition[] alootitemcondition) {
            int i = JsonUtils.getInt(jsonobject, "limit", 0);

            return new LootingEnchantBonus(alootitemcondition, (RandomValueRange) JsonUtils.deserializeClass(jsonobject, "count", jsondeserializationcontext, RandomValueRange.class), i);
        }

        public LootFunction b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext, LootCondition[] alootitemcondition) {
            return this.a(jsonobject, jsondeserializationcontext, alootitemcondition);
        }
    }
}
