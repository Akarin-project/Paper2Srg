package net.minecraft.world.storage.loot.properties;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public interface EntityProperty {

    boolean func_186657_a(Random random, Entity entity);

    public abstract static class a<T extends EntityProperty> {

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

        public abstract JsonElement a(T t0, JsonSerializationContext jsonserializationcontext);

        public abstract T a(JsonElement jsonelement, JsonDeserializationContext jsondeserializationcontext);
    }
}
