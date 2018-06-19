package net.minecraft.world.storage.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraft.server.LootItemFunction.a;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public abstract class LootFunction {

    private final LootCondition[] field_186555_a;

    protected LootFunction(LootCondition[] alootitemcondition) {
        this.field_186555_a = alootitemcondition;
    }

    public abstract ItemStack func_186553_a(ItemStack itemstack, Random random, LootContext loottableinfo);

    public LootCondition[] func_186554_a() {
        return this.field_186555_a;
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
