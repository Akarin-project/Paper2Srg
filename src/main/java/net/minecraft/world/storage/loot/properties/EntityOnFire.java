package net.minecraft.world.storage.loot.properties;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.server.LootEntityPropertyOnFire.a;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class EntityOnFire implements EntityProperty {

    private final boolean onFire;

    public EntityOnFire(boolean flag) {
        this.onFire = flag;
    }

    public boolean testProperty(Random random, Entity entity) {
        return entity.isBurning() == this.onFire;
    }

    public static class a extends LootEntityProperty.a<EntityOnFire> {

        protected a() {
            super(new ResourceLocation("on_fire"), EntityOnFire.class);
        }

        public JsonElement a(EntityOnFire lootentitypropertyonfire, JsonSerializationContext jsonserializationcontext) {
            return new JsonPrimitive(Boolean.valueOf(lootentitypropertyonfire.onFire));
        }

        public EntityOnFire b(JsonElement jsonelement, JsonDeserializationContext jsondeserializationcontext) {
            return new EntityOnFire(JsonUtils.getBoolean(jsonelement, "on_fire"));
        }

        public EntityProperty a(JsonElement jsonelement, JsonDeserializationContext jsondeserializationcontext) {
            return this.b(jsonelement, jsondeserializationcontext);
        }
    }
}
