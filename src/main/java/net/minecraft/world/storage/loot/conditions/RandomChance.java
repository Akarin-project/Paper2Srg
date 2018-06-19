package net.minecraft.world.storage.loot.conditions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Random;

import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;

public class RandomChance implements LootCondition {

    private final float chance;

    public RandomChance(float f) {
        this.chance = f;
    }

    @Override
    public boolean testCondition(Random random, LootContext loottableinfo) {
        return random.nextFloat() < this.chance;
    }

    public static class a extends LootCondition.a<RandomChance> {

        protected a() {
            super(new ResourceLocation("random_chance"), RandomChance.class);
        }

        @Override
        public void a(JsonObject jsonobject, RandomChance lootitemconditionrandomchance, JsonSerializationContext jsonserializationcontext) {
            jsonobject.addProperty("chance", Float.valueOf(lootitemconditionrandomchance.chance));
        }

        public RandomChance a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
            return new RandomChance(JsonUtils.getFloat(jsonobject, "chance"));
        }

        @Override
        public RandomChance b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
            return this.a(jsonobject, jsondeserializationcontext);
        }
    }
}
