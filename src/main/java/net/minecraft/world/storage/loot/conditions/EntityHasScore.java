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

    private final Map<String, RandomValueRange> scores;
    private final LootContext.EntityTarget target;

    public EntityHasScore(Map<String, RandomValueRange> map, LootContext.EntityTarget loottableinfo_entitytarget) {
        this.scores = map;
        this.target = loottableinfo_entitytarget;
    }

    @Override
    public boolean testCondition(Random random, LootContext loottableinfo) {
        Entity entity = loottableinfo.getEntity(this.target);

        if (entity == null) {
            return false;
        } else {
            Scoreboard scoreboard = entity.world.getScoreboard();
            Iterator iterator = this.scores.entrySet().iterator();

            Entry entry;

            do {
                if (!iterator.hasNext()) {
                    return true;
                }

                entry = (Entry) iterator.next();
            } while (this.entityScoreMatch(entity, scoreboard, (String) entry.getKey(), (RandomValueRange) entry.getValue()));

            return false;
        }
    }

    protected boolean entityScoreMatch(Entity entity, Scoreboard scoreboard, String s, RandomValueRange lootvaluebounds) {
        ScoreObjective scoreboardobjective = scoreboard.getObjective(s);

        if (scoreboardobjective == null) {
            return false;
        } else {
            String s1 = entity instanceof EntityPlayerMP ? entity.getName() : entity.getCachedUniqueIdString();

            return !scoreboard.entityHasObjective(s1, scoreboardobjective) ? false : lootvaluebounds.isInRange(scoreboard.getOrCreateScore(s1, scoreboardobjective).getScorePoints());
        }
    }

    public static class a extends LootCondition.a<EntityHasScore> {

        protected a() {
            super(new ResourceLocation("entity_scores"), EntityHasScore.class);
        }

        @Override
        public void a(JsonObject jsonobject, EntityHasScore lootitemconditionentityscore, JsonSerializationContext jsonserializationcontext) {
            JsonObject jsonobject1 = new JsonObject();
            Iterator iterator = lootitemconditionentityscore.scores.entrySet().iterator();

            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();

                jsonobject1.add((String) entry.getKey(), jsonserializationcontext.serialize(entry.getValue()));
            }

            jsonobject.add("scores", jsonobject1);
            jsonobject.add("entity", jsonserializationcontext.serialize(lootitemconditionentityscore.target));
        }

        public EntityHasScore a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
            Set set = JsonUtils.getJsonObject(jsonobject, "scores").entrySet();
            LinkedHashMap linkedhashmap = Maps.newLinkedHashMap();
            Iterator iterator = set.iterator();

            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();

                linkedhashmap.put(entry.getKey(), JsonUtils.deserializeClass((JsonElement) entry.getValue(), "score", jsondeserializationcontext, RandomValueRange.class));
            }

            return new EntityHasScore(linkedhashmap, JsonUtils.deserializeClass(jsonobject, "entity", jsondeserializationcontext, LootContext.EntityTarget.class));
        }

        @Override
        public EntityHasScore b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
            return this.a(jsonobject, jsondeserializationcontext);
        }
    }
}
