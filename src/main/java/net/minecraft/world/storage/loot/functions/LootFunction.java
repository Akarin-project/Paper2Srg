package net.minecraft.world.storage.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public abstract class LootFunction {

    private final LootCondition[] conditions;

    protected LootFunction(LootCondition[] alootitemcondition) {
        this.conditions = alootitemcondition;
    }

    public abstract ItemStack apply(ItemStack itemstack, Random random, LootContext loottableinfo);

    public LootCondition[] getConditions() {
        return this.conditions;
    }

    public abstract static class a<T extends LootFunction> {

        private final ResourceLocation a;
        private final Class<T> b;

        protected a(ResourceLocation minecraftkey, Class<T> oclass) {
            this.a = minecraftkey;
            this.b = oclass;
        }

        public ResourceLocation a() {
            return this.a;
        }

        public Class<T> b() {
            return this.b;
        }

        public abstract void a(JsonObject jsonobject, T t0, JsonSerializationContext jsonserializationcontext);

        public abstract T b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext, LootCondition[] alootitemcondition);
    }
}
