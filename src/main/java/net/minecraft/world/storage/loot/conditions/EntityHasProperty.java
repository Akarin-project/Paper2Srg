package net.minecraft.world.storage.loot.conditions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;

import net.minecraft.entity.Entity;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.properties.EntityProperty;
import net.minecraft.world.storage.loot.properties.EntityPropertyManager;

public class EntityHasProperty implements LootCondition {

    private final EntityProperty[] properties;
    private final LootContext.EntityTarget target;

    public EntityHasProperty(EntityProperty[] alootentityproperty, LootContext.EntityTarget loottableinfo_entitytarget) {
        this.properties = alootentityproperty;
        this.target = loottableinfo_entitytarget;
    }

    @Override
    public boolean testCondition(Random random, LootContext loottableinfo) {
        Entity entity = loottableinfo.getEntity(this.target);

        if (entity == null) {
            return false;
        } else {
            EntityProperty[] alootentityproperty = this.properties;
            int i = alootentityproperty.length;

            for (int j = 0; j < i; ++j) {
                EntityProperty lootentityproperty = alootentityproperty[j];

                if (!lootentityproperty.testProperty(random, entity)) {
                    return false;
                }
            }

            return true;
        }
    }

    public static class a extends LootCondition.a<EntityHasProperty> {

        protected a() {
            super(new ResourceLocation("entity_properties"), EntityHasProperty.class);
        }

        @Override
        public void a(JsonObject jsonobject, EntityHasProperty lootitemconditionentityproperty, JsonSerializationContext jsonserializationcontext) {
            JsonObject jsonobject1 = new JsonObject();
            EntityProperty[] alootentityproperty = lootitemconditionentityproperty.properties;
            int i = alootentityproperty.length;

            for (int j = 0; j < i; ++j) {
                EntityProperty lootentityproperty = alootentityproperty[j];
                EntityProperty.a lootentityproperty_a = EntityPropertyManager.a(lootentityproperty);

                jsonobject1.add(lootentityproperty_a.a().toString(), lootentityproperty_a.a(lootentityproperty, jsonserializationcontext));
            }

            jsonobject.add("properties", jsonobject1);
            jsonobject.add("entity", jsonserializationcontext.serialize(lootitemconditionentityproperty.target));
        }

        public EntityHasProperty a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
            Set set = JsonUtils.getJsonObject(jsonobject, "properties").entrySet();
            EntityProperty[] alootentityproperty = new EntityProperty[set.size()];
            int i = 0;

            Entry entry;

            for (Iterator iterator = set.iterator(); iterator.hasNext(); alootentityproperty[i++] = EntityPropertyManager.a(new ResourceLocation((String) entry.getKey())).a((JsonElement) entry.getValue(), jsondeserializationcontext)) {
                entry = (Entry) iterator.next();
            }

            return new EntityHasProperty(alootentityproperty, JsonUtils.deserializeClass(jsonobject, "entity", jsondeserializationcontext, LootContext.EntityTarget.class));
        }

        @Override
        public EntityHasProperty b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
            return this.a(jsonobject, jsondeserializationcontext);
        }
    }
}
