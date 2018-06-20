package net.minecraft.world.storage.loot.conditions;

import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.RandomValueRange;

public class EntityHasScore implements LootCondition {

    private final Map<String, RandomValueRange> field_186634_a;
    private final LootContext.EntityTarget field_186635_b;

    public EntityHasScore(Map<String, RandomValueRange> map, LootContext.EntityTarget loottableinfo_entitytarget) {
        this.field_186634_a = map;
        this.field_186635_b = loottableinfo_entitytarget;
    }

    @Override
    public boolean func_186618_a(Random random, LootContext loottableinfo) {
        Entity entity = loottableinfo.func_186494_a(this.field_186635_b);

        if (entity == null) {
            return false;
        } else {
            Scoreboard scoreboard = entity.field_70170_p.func_96441_U();
            Iterator iterator = this.field_186634_a.entrySet().iterator();

            Entry entry;

            do {
                if (!iterator.hasNext()) {
                    return true;
                }

                entry = (Entry) iterator.next();
            } while (this.func_186631_a(entity, scoreboard, (String) entry.getKey(), (RandomValueRange) entry.getValue()));

            return false;
        }
    }

    protected boolean func_186631_a(Entity entity, Scoreboard scoreboard, String s, RandomValueRange lootvaluebounds) {
        ScoreObjective scoreboardobjective = scoreboard.func_96518_b(s);

        if (scoreboardobjective == null) {
            return false;
        } else {
            String s1 = entity instanceof EntityPlayerMP ? entity.func_70005_c_() : entity.func_189512_bd();

            return !scoreboard.func_178819_b(s1, scoreboardobjective) ? false : lootvaluebounds.func_186510_a(scoreboard.func_96529_a(s1, scoreboardobjective).func_96652_c());
        }
    }

    public static class a extends LootCondition.a<EntityHasScore> {

        protected a() {
            super(new ResourceLocation("entity_scores"), EntityHasScore.class);
        }

        @Override
        public void a(JsonObject jsonobject, EntityHasScore LootConditionentityscore, JsonSerializationContext jsonserializationcontext) {
            JsonObject jsonobject1 = new JsonObject();
            Iterator iterator = LootConditionentityscore.field_186634_a.entrySet().iterator();

            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();

                jsonobject1.add((String) entry.getKey(), jsonserializationcontext.serialize(entry.getValue()));
            }

            jsonobject.add("scores", jsonobject1);
            jsonobject.add("entity", jsonserializationcontext.serialize(LootConditionentityscore.field_186635_b));
        }

        public EntityHasScore a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
            Set set = JsonUtils.func_152754_s(jsonobject, "scores").entrySet();
            LinkedHashMap linkedhashmap = Maps.newLinkedHashMap();
            Iterator iterator = set.iterator();

            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();

                linkedhashmap.put(entry.getKey(), JsonUtils.func_188179_a((JsonElement) entry.getValue(), "score", jsondeserializationcontext, RandomValueRange.class));
            }

            return new EntityHasScore(linkedhashmap, JsonUtils.func_188174_a(jsonobject, "entity", jsondeserializationcontext, LootContext.EntityTarget.class));
        }

        @Override
        public EntityHasScore b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
            return this.a(jsonobject, jsondeserializationcontext);
        }
    }
}
