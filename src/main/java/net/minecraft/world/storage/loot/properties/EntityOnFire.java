package net.minecraft.world.storage.loot.properties;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class EntityOnFire implements EntityProperty {

    private final boolean field_186659_a;

    public EntityOnFire(boolean flag) {
        this.field_186659_a = flag;
    }

    @Override
    public boolean func_186657_a(Random random, Entity entity) {
        return entity.func_70027_ad() == this.field_186659_a;
    }

    public static class a extends EntityProperty.a<EntityOnFire> {

        protected a() {
            super(new ResourceLocation("on_fire"), EntityOnFire.class);
        }

        @Override
        public JsonElement a(EntityOnFire lootentitypropertyonfire, JsonSerializationContext jsonserializationcontext) {
            return new JsonPrimitive(Boolean.valueOf(lootentitypropertyonfire.field_186659_a));
        }

        public EntityOnFire b(JsonElement jsonelement, JsonDeserializationContext jsondeserializationcontext) {
            return new EntityOnFire(JsonUtils.func_151216_b(jsonelement, "on_fire"));
        }

        @Override
        public EntityOnFire a(JsonElement jsonelement, JsonDeserializationContext jsondeserializationcontext) {
            return this.b(jsonelement, jsondeserializationcontext);
        }
    }
}
