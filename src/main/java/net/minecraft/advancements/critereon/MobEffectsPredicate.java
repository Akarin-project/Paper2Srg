package net.minecraft.advancements.critereon;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.CriterionConditionMobEffect.a;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class MobEffectsPredicate {

    public static final MobEffectsPredicate ANY = new MobEffectsPredicate(Collections.emptyMap());
    private final Map<Potion, CriterionConditionMobEffect.a> effects;

    public MobEffectsPredicate(Map<Potion, CriterionConditionMobEffect.a> map) {
        this.effects = map;
    }

    public boolean test(Entity entity) {
        return this == MobEffectsPredicate.ANY ? true : (entity instanceof EntityLivingBase ? this.test(((EntityLivingBase) entity).getActivePotionMap()) : false);
    }

    public boolean test(EntityLivingBase entityliving) {
        return this == MobEffectsPredicate.ANY ? true : this.test(entityliving.getActivePotionMap());
    }

    public boolean test(Map<Potion, PotionEffect> map) {
        if (this == MobEffectsPredicate.ANY) {
            return true;
        } else {
            Iterator iterator = this.effects.entrySet().iterator();

            Entry entry;
            PotionEffect mobeffect;

            do {
                if (!iterator.hasNext()) {
                    return true;
                }

                entry = (Entry) iterator.next();
                mobeffect = (PotionEffect) map.get(entry.getKey());
            } while (((CriterionConditionMobEffect.a) entry.getValue()).a(mobeffect));

            return false;
        }
    }

    public static MobEffectsPredicate deserialize(@Nullable JsonElement jsonelement) {
        if (jsonelement != null && !jsonelement.isJsonNull()) {
            JsonObject jsonobject = JsonUtils.getJsonObject(jsonelement, "effects");
            HashMap hashmap = Maps.newHashMap();
            Iterator iterator = jsonobject.entrySet().iterator();

            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();
                ResourceLocation minecraftkey = new ResourceLocation((String) entry.getKey());
                Potion mobeffectlist = (Potion) Potion.REGISTRY.getObject(minecraftkey);

                if (mobeffectlist == null) {
                    throw new JsonSyntaxException("Unknown effect \'" + minecraftkey + "\'");
                }

                CriterionConditionMobEffect.a criterionconditionmobeffect_a = MobEffectsPredicate.ANY.deserialize(JsonUtils.getJsonObject((JsonElement) entry.getValue(), (String) entry.getKey()));

                hashmap.put(mobeffectlist, criterionconditionmobeffect_a);
            }

            return new MobEffectsPredicate(hashmap);
        } else {
            return MobEffectsPredicate.ANY;
        }
    }

    public static class a {

        private final MinMaxBounds a;
        private final MinMaxBounds b;
        @Nullable
        private final Boolean c;
        @Nullable
        private final Boolean d;

        public a(MinMaxBounds criterionconditionvalue, MinMaxBounds criterionconditionvalue1, @Nullable Boolean obool, @Nullable Boolean obool1) {
            this.a = criterionconditionvalue;
            this.b = criterionconditionvalue1;
            this.c = obool;
            this.d = obool1;
        }

        public boolean a(@Nullable PotionEffect mobeffect) {
            return mobeffect == null ? false : (!this.a.test((float) mobeffect.getAmplifier()) ? false : (!this.b.test((float) mobeffect.getDuration()) ? false : (this.c != null && this.c.booleanValue() != mobeffect.getIsAmbient() ? false : this.d == null || this.d.booleanValue() == mobeffect.doesShowParticles())));
        }

        public static CriterionConditionMobEffect.a a(JsonObject jsonobject) {
            MinMaxBounds criterionconditionvalue = MinMaxBounds.deserialize(jsonobject.get("amplifier"));
            MinMaxBounds criterionconditionvalue1 = MinMaxBounds.deserialize(jsonobject.get("duration"));
            Boolean obool = jsonobject.has("ambient") ? Boolean.valueOf(JsonUtils.getBoolean(jsonobject, "ambient")) : null;
            Boolean obool1 = jsonobject.has("visible") ? Boolean.valueOf(JsonUtils.getBoolean(jsonobject, "visible")) : null;

            return new CriterionConditionMobEffect.a(criterionconditionvalue, criterionconditionvalue1, obool, obool1);
        }
    }
}
