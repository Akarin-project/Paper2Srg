package net.minecraft.world.storage.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraft.server.LootItemFunctionSetCount.a;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public class SetCount extends LootFunction {

    private final RandomValueRange countRange;

    public SetCount(LootCondition[] alootitemcondition, RandomValueRange lootvaluebounds) {
        super(alootitemcondition);
        this.countRange = lootvaluebounds;
    }

    public ItemStack apply(ItemStack itemstack, Random random, LootContext loottableinfo) {
        itemstack.setCount(this.countRange.generateInt(random));
        return itemstack;
    }

    public static class a extends LootItemFunction.a<SetCount> {

        protected a() {
            super(new ResourceLocation("set_count"), SetCount.class);
        }

        public void a(JsonObject jsonobject, SetCount lootitemfunctionsetcount, JsonSerializationContext jsonserializationcontext) {
            jsonobject.add("count", jsonserializationcontext.serialize(lootitemfunctionsetcount.countRange));
        }

        public SetCount a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext, LootCondition[] alootitemcondition) {
            return new SetCount(alootitemcondition, (RandomValueRange) JsonUtils.deserializeClass(jsonobject, "count", jsondeserializationcontext, RandomValueRange.class));
        }

        public LootFunction b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext, LootCondition[] alootitemcondition) {
            return this.a(jsonobject, jsondeserializationcontext, alootitemcondition);
        }
    }
}
