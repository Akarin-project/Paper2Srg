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
import net.minecraft.server.LootSelector.a;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;

public class LootPool {

    private final LootEntry[] field_186453_a;
    private final LootCondition[] field_186454_b;
    private final RandomValueRange field_186455_c;
    private final RandomValueRange field_186456_d;

    public LootPool(LootEntry[] alotoselectorentry, LootCondition[] alootitemcondition, RandomValueRange lootvaluebounds, RandomValueRange lootvaluebounds1) {
        this.field_186453_a = alotoselectorentry;
        this.field_186454_b = alootitemcondition;
        this.field_186455_c = lootvaluebounds;
        this.field_186456_d = lootvaluebounds1;
    }

    protected void func_186452_a(Collection<ItemStack> collection, Random random, LootContext loottableinfo) {
        ArrayList arraylist = Lists.newArrayList();
        int i = 0;
        LootEntry[] alotoselectorentry = this.field_186453_a;
        int j = alotoselectorentry.length;

        for (int k = 0; k < j; ++k) {
            LootEntry lotoselectorentry = alotoselectorentry[k];

            if (LootConditionManager.func_186638_a(lotoselectorentry.field_186366_e, random, loottableinfo)) {
                int l = lotoselectorentry.func_186361_a(loottableinfo.func_186491_f());

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
                i1 -= lotoselectorentry1.func_186361_a(loottableinfo.func_186491_f());
            } while (i1 >= 0);

            lotoselectorentry1.func_186363_a(collection, random, loottableinfo);
        }
    }

    public void func_186449_b(Collection<ItemStack> collection, Random random, LootContext loottableinfo) {
        if (LootConditionManager.func_186638_a(this.field_186454_b, random, loottableinfo)) {
            int i = this.field_186455_c.func_186511_a(random) + MathHelper.func_76141_d(this.field_186456_d.func_186507_b(random) * loottableinfo.func_186491_f());

            for (int j = 0; j < i; ++j) {
                this.func_186452_a(collection, random, loottableinfo);
            }

        }
    }

    public static class a implements JsonDeserializer<LootPool>, JsonSerializer<LootPool> {

        public a() {}

        public LootPool a(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
            JsonObject jsonobject = JsonUtils.func_151210_l(jsonelement, "loot pool");
            LootEntry[] alotoselectorentry = (LootEntry[]) JsonUtils.func_188174_a(jsonobject, "entries", jsondeserializationcontext, LootEntry[].class);
            LootCondition[] alootitemcondition = (LootCondition[]) JsonUtils.func_188177_a(jsonobject, "conditions", new LootCondition[0], jsondeserializationcontext, LootCondition[].class);
            RandomValueRange lootvaluebounds = (RandomValueRange) JsonUtils.func_188174_a(jsonobject, "rolls", jsondeserializationcontext, RandomValueRange.class);
            RandomValueRange lootvaluebounds1 = (RandomValueRange) JsonUtils.func_188177_a(jsonobject, "bonus_rolls", new RandomValueRange(0.0F, 0.0F), jsondeserializationcontext, RandomValueRange.class);

            return new LootPool(alotoselectorentry, alootitemcondition, lootvaluebounds, lootvaluebounds1);
        }

        public JsonElement a(LootPool lootselector, Type type, JsonSerializationContext jsonserializationcontext) {
            JsonObject jsonobject = new JsonObject();

            jsonobject.add("entries", jsonserializationcontext.serialize(lootselector.field_186453_a));
            jsonobject.add("rolls", jsonserializationcontext.serialize(lootselector.field_186455_c));
            if (lootselector.field_186456_d.func_186509_a() != 0.0F && lootselector.field_186456_d.func_186512_b() != 0.0F) {
                jsonobject.add("bonus_rolls", jsonserializationcontext.serialize(lootselector.field_186456_d));
            }

            if (!ArrayUtils.isEmpty(lootselector.field_186454_b)) {
                jsonobject.add("conditions", jsonserializationcontext.serialize(lootselector.field_186454_b));
            }

            return jsonobject;
        }

        public JsonElement serialize(Object object, Type type, JsonSerializationContext jsonserializationcontext) {
            return this.a((LootPool) object, type, jsonserializationcontext);
        }

        public Object deserialize(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
            return this.a(jsonelement, type, jsondeserializationcontext);
        }
    }
}
