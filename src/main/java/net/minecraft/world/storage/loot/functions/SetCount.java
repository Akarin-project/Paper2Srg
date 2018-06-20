package net.minecraft.world.storage.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public class SetCount extends LootFunction {

    private final RandomValueRange field_186568_a;

    public SetCount(LootCondition[] alootitemcondition, RandomValueRange lootvaluebounds) {
        super(alootitemcondition);
        this.field_186568_a = lootvaluebounds;
    }

    @Override
    public ItemStack func_186553_a(ItemStack itemstack, Random random, LootContext loottableinfo) {
        itemstack.func_190920_e(this.field_186568_a.func_186511_a(random));
        return itemstack;
    }

    public static class a extends LootFunction.a<SetCount> {

        protected a() {
            super(new ResourceLocation("set_count"), SetCount.class);
        }

        @Override
        public void a(JsonObject jsonobject, SetCount lootitemfunctionsetcount, JsonSerializationContext jsonserializationcontext) {
            jsonobject.add("count", jsonserializationcontext.serialize(lootitemfunctionsetcount.field_186568_a));
        }

        public SetCount a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext, LootCondition[] alootitemcondition) {
            return new SetCount(alootitemcondition, JsonUtils.func_188174_a(jsonobject, "count", jsondeserializationcontext, RandomValueRange.class));
        }

        @Override
        public SetCount b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext, LootCondition[] alootitemcondition) {
            return this.a(jsonobject, jsondeserializationcontext, alootitemcondition);
        }
    }
}
