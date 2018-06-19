package net.minecraft.world.storage.loot.conditions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Random;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;

public interface LootCondition {

    boolean testCondition(Random random, LootContext loottableinfo);

    public abstract static class a<T extends LootCondition> {

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

        public abstract T b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext);
    }
}
