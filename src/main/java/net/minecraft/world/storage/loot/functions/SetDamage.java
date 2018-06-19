package net.minecraft.world.storage.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public class SetDamage extends LootFunction {

    private static final Logger LOGGER = LogManager.getLogger();
    private final RandomValueRange damageRange;

    public SetDamage(LootCondition[] alootitemcondition, RandomValueRange lootvaluebounds) {
        super(alootitemcondition);
        this.damageRange = lootvaluebounds;
    }

    @Override
    public ItemStack apply(ItemStack itemstack, Random random, LootContext loottableinfo) {
        if (itemstack.isItemStackDamageable()) {
            float f = 1.0F - this.damageRange.generateFloat(random);

            itemstack.setItemDamage(MathHelper.floor(f * itemstack.getMaxDamage()));
        } else {
            SetDamage.LOGGER.warn("Couldn\'t set damage of loot item {}", itemstack);
        }

        return itemstack;
    }

    public static class a extends LootFunction.a<SetDamage> {

        protected a() {
            super(new ResourceLocation("set_damage"), SetDamage.class);
        }

        @Override
        public void a(JsonObject jsonobject, SetDamage lootitemfunctionsetdamage, JsonSerializationContext jsonserializationcontext) {
            jsonobject.add("damage", jsonserializationcontext.serialize(lootitemfunctionsetdamage.damageRange));
        }

        public SetDamage a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext, LootCondition[] alootitemcondition) {
            return new SetDamage(alootitemcondition, JsonUtils.deserializeClass(jsonobject, "damage", jsondeserializationcontext, RandomValueRange.class));
        }

        @Override
        public SetDamage b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext, LootCondition[] alootitemcondition) {
            return this.a(jsonobject, jsondeserializationcontext, alootitemcondition);
        }
    }
}
