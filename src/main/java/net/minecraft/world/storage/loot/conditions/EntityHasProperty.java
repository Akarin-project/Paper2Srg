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

    private final EntityProperty[] field_186623_a;
    private final LootContext.EntityTarget field_186624_b;

    public EntityHasProperty(EntityProperty[] alootentityproperty, LootContext.EntityTarget loottableinfo_entitytarget) {
        this.field_186623_a = alootentityproperty;
        this.field_186624_b = loottableinfo_entitytarget;
    }

    @Override
    public boolean func_186618_a(Random random, LootContext loottableinfo) {
        Entity entity = loottableinfo.func_186494_a(this.field_186624_b);

        if (entity == null) {
            return false;
        } else {
            EntityProperty[] alootentityproperty = this.field_186623_a;
            int i = alootentityproperty.length;

            for (int j = 0; j < i; ++j) {
                EntityProperty lootentityproperty = alootentityproperty[j];

                if (!lootentityproperty.func_186657_a(random, entity)) {
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
            EntityProperty[] alootentityproperty = lootitemconditionentityproperty.field_186623_a;
            int i = alootentityproperty.length;

            for (int j = 0; j < i; ++j) {
                EntityProperty lootentityproperty = alootentityproperty[j];
                EntityProperty.a lootentityproperty_a = EntityPropertyManager.a(lootentityproperty);

                jsonobject1.add(lootentityproperty_a.a().toString(), lootentityproperty_a.a(lootentityproperty, jsonserializationcontext));
            }

            jsonobject.add("properties", jsonobject1);
            jsonobject.add("entity", jsonserializationcontext.serialize(lootitemconditionentityproperty.field_186624_b));
        }

        public EntityHasProperty a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
            Set set = JsonUtils.func_152754_s(jsonobject, "properties").entrySet();
            EntityProperty[] alootentityproperty = new EntityProperty[set.size()];
            int i = 0;

            Entry entry;

            for (Iterator iterator = set.iterator(); iterator.hasNext(); alootentityproperty[i++] = EntityPropertyManager.a(new ResourceLocation((String) entry.getKey())).a((JsonElement) entry.getValue(), jsondeserializationcontext)) {
                entry = (Entry) iterator.next();
            }

            return new EntityHasProperty(alootentityproperty, JsonUtils.func_188174_a(jsonobject, "entity", jsondeserializationcontext, LootContext.EntityTarget.class));
        }

        @Override
        public EntityHasProperty b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
            return this.a(jsonobject, jsondeserializationcontext);
        }
    }
}
