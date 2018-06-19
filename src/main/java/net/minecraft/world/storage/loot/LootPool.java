package net.minecraft.world.storage.loot;

import com.google.common.collect.Lists;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import org.apache.commons.lang3.ArrayUtils;

import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;

public class LootPool {

    private final LootEntry[] lootEntries;
    private final LootCondition[] poolConditions;
    private final RandomValueRange rolls;
    private final RandomValueRange bonusRolls;

    public LootPool(LootEntry[] alotoselectorentry, LootCondition[] alootitemcondition, RandomValueRange lootvaluebounds, RandomValueRange lootvaluebounds1) {
        this.lootEntries = alotoselectorentry;
        this.poolConditions = alootitemcondition;
        this.rolls = lootvaluebounds;
        this.bonusRolls = lootvaluebounds1;
    }

    protected void createLootRoll(Collection<ItemStack> collection, Random random, LootContext loottableinfo) {
        ArrayList arraylist = Lists.newArrayList();
        int i = 0;
        LootEntry[] alotoselectorentry = this.lootEntries;
        int j = alotoselectorentry.length;

        for (int k = 0; k < j; ++k) {
            LootEntry lotoselectorentry = alotoselectorentry[k];

            if (LootConditionManager.testAllConditions(lotoselectorentry.conditions, random, loottableinfo)) {
                int l = lotoselectorentry.getEffectiveWeight(loottableinfo.getLuck());

                if (l > 0) {
                    arraylist.add(lotoselectorentry);
                    i += l;
                }
            }
        }

        if (i != 0 && !arraylist.isEmpty()) {
            int i1 = random.nextInt(i);
            Iterator iterator = arraylist.iterator();

            LootEntry lotoselectorentry1;

            do {
                if (!iterator.hasNext()) {
                    return;
                }

                lotoselectorentry1 = (LootEntry) iterator.next();
                i1 -= lotoselectorentry1.getEffectiveWeight(loottableinfo.getLuck());
            } while (i1 >= 0);

            lotoselectorentry1.addLoot(collection, random, loottableinfo);
        }
    }

    public void generateLoot(Collection<ItemStack> collection, Random random, LootContext loottableinfo) {
        if (LootConditionManager.testAllConditions(this.poolConditions, random, loottableinfo)) {
            int i = this.rolls.generateInt(random) + MathHelper.floor(this.bonusRolls.generateFloat(random) * loottableinfo.getLuck());

            for (int j = 0; j < i; ++j) {
                this.createLootRoll(collection, random, loottableinfo);
            }

        }
    }

    public static class a implements JsonDeserializer<LootPool>, JsonSerializer<LootPool> {

        public a() {}

        public LootPool a(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
            JsonObject jsonobject = JsonUtils.getJsonObject(jsonelement, "loot pool");
            LootEntry[] alotoselectorentry = JsonUtils.deserializeClass(jsonobject, "entries", jsondeserializationcontext, LootEntry[].class);
            LootCondition[] alootitemcondition = JsonUtils.deserializeClass(jsonobject, "conditions", new LootCondition[0], jsondeserializationcontext, LootCondition[].class);
            RandomValueRange lootvaluebounds = JsonUtils.deserializeClass(jsonobject, "rolls", jsondeserializationcontext, RandomValueRange.class);
            RandomValueRange lootvaluebounds1 = JsonUtils.deserializeClass(jsonobject, "bonus_rolls", new RandomValueRange(0.0F, 0.0F), jsondeserializationcontext, RandomValueRange.class);

            return new LootPool(alotoselectorentry, alootitemcondition, lootvaluebounds, lootvaluebounds1);
        }

        public JsonElement a(LootPool lootselector, Type type, JsonSerializationContext jsonserializationcontext) {
            JsonObject jsonobject = new JsonObject();

            jsonobject.add("entries", jsonserializationcontext.serialize(lootselector.lootEntries));
            jsonobject.add("rolls", jsonserializationcontext.serialize(lootselector.rolls));
            if (lootselector.bonusRolls.getMin() != 0.0F && lootselector.bonusRolls.getMax() != 0.0F) {
                jsonobject.add("bonus_rolls", jsonserializationcontext.serialize(lootselector.bonusRolls));
            }

            if (!ArrayUtils.isEmpty(lootselector.poolConditions)) {
                jsonobject.add("conditions", jsonserializationcontext.serialize(lootselector.poolConditions));
            }

            return jsonobject;
        }

        @Override
        public JsonElement serialize(LootPool object, Type type, JsonSerializationContext jsonserializationcontext) {
            return this.a(object, type, jsonserializationcontext);
        }

        @Override
        public LootPool deserialize(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
            return this.a(jsonelement, type, jsondeserializationcontext);
        }
    }
}
