package net.minecraft.world.storage.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public abstract class LootEntry {

    protected final int field_186364_c; public int getWeight() { return field_186364_c; } // Paper - OBFHELPER
    protected final int field_186365_d; public int getQuality() { return field_186365_d; } // Paper - OBFHELPER
    protected final LootCondition[] field_186366_e;

    protected LootEntry(int i, int j, LootCondition[] alootitemcondition) {
        this.field_186364_c = i;
        this.field_186365_d = j;
        this.field_186366_e = alootitemcondition;
    }

    public int func_186361_a(float f) {
        // Paper start - Offer an alternative loot formula to refactor how luck bonus applies
        // SEE: https://luckformula.emc.gs for details and data
        if (lastLuck != null && lastLuck == f) {
            return lastWeight;
        }
        // This is vanilla
        float qualityModifer = this.getQuality() * f;
        double baseWeight = (this.getWeight() + qualityModifer);
        if (com.destroystokyo.paper.PaperConfig.useAlternativeLuckFormula) {
            // Random boost to avoid losing precision in the final int cast on return
            final int weightBoost = 100;
            baseWeight *= weightBoost;
            // If we have vanilla 1, bump that down to 0 so nothing is is impacted
            // vanilla 3 = 300, 200 basis = impact 2%
            // =($B2*(($B2-100)/100/100))
            double impacted = baseWeight * ((baseWeight - weightBoost) / weightBoost / 100);
            // =($B$7/100)
            float luckModifier = Math.min(100, f * 10) / 100;
            // =B2 - (C2 *($B$7/100))
            baseWeight = Math.ceil(baseWeight - (impacted * luckModifier));
        }
        lastLuck = f;
        lastWeight = (int) Math.max(0, Math.floor(baseWeight));
        return lastWeight;
    }
    private Float lastLuck = null;
    private int lastWeight = 0;
    // Paper end

    public abstract void func_186363_a(Collection<ItemStack> collection, Random random, LootContext loottableinfo);

    protected abstract void func_186362_a(JsonObject jsonobject, JsonSerializationContext jsonserializationcontext);

    public static class a implements JsonDeserializer<LootEntry>, JsonSerializer<LootEntry> {

        public a() {}

        public LootEntry a(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
            JsonObject jsonobject = JsonUtils.func_151210_l(jsonelement, "loot item");
            String s = JsonUtils.func_151200_h(jsonobject, "type");
            int i = JsonUtils.func_151208_a(jsonobject, "weight", 1);
            int j = JsonUtils.func_151208_a(jsonobject, "quality", 0);
            LootCondition[] alootitemcondition;

            if (jsonobject.has("conditions")) {
                alootitemcondition = JsonUtils.func_188174_a(jsonobject, "conditions", jsondeserializationcontext, LootCondition[].class);
            } else {
                alootitemcondition = new LootCondition[0];
            }

            if ("item".equals(s)) {
                return LootEntryItem.func_186367_a(jsonobject, jsondeserializationcontext, i, j, alootitemcondition);
            } else if ("loot_table".equals(s)) {
                return LootEntryTable.func_186370_a(jsonobject, jsondeserializationcontext, i, j, alootitemcondition);
            } else if ("empty".equals(s)) {
                return LootEntryEmpty.func_186372_a(jsonobject, jsondeserializationcontext, i, j, alootitemcondition);
            } else {
                throw new JsonSyntaxException("Unknown loot entry type \'" + s + "\'");
            }
        }

        public JsonElement a(LootEntry lotoselectorentry, Type type, JsonSerializationContext jsonserializationcontext) {
            JsonObject jsonobject = new JsonObject();

            jsonobject.addProperty("weight", Integer.valueOf(lotoselectorentry.field_186364_c));
            jsonobject.addProperty("quality", Integer.valueOf(lotoselectorentry.field_186365_d));
            if (lotoselectorentry.field_186366_e.length > 0) {
                jsonobject.add("conditions", jsonserializationcontext.serialize(lotoselectorentry.field_186366_e));
            }

            if (lotoselectorentry instanceof LootEntryItem) {
                jsonobject.addProperty("type", "item");
            } else if (lotoselectorentry instanceof LootEntryTable) {
                jsonobject.addProperty("type", "loot_table");
            } else {
                if (!(lotoselectorentry instanceof LootEntryEmpty)) {
                    throw new IllegalArgumentException("Don\'t know how to serialize " + lotoselectorentry);
                }

                jsonobject.addProperty("type", "empty");
            }

            lotoselectorentry.func_186362_a(jsonobject, jsonserializationcontext);
            return jsonobject;
        }

        @Override
        public JsonElement serialize(LootEntry object, Type type, JsonSerializationContext jsonserializationcontext) {
            return this.a(object, type, jsonserializationcontext);
        }

        @Override
        public LootEntry deserialize(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
            return this.a(jsonelement, type, jsondeserializationcontext);
        }
    }
}
