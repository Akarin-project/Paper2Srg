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

    public static final MobEffectsPredicate field_193473_a = new MobEffectsPredicate(Collections.emptyMap());
    private final Map<Potion, CriterionConditionMobEffect.a> field_193474_b;

    public MobEffectsPredicate(Map<Potion, CriterionConditionMobEffect.a> map) {
        this.field_193474_b = map;
    }

    public boolean func_193469_a(Entity entity) {
        return this == MobEffectsPredicate.field_193473_a ? true : (entity instanceof EntityLivingBase ? this.func_193470_a(((EntityLivingBase) entity).func_193076_bZ()) : false);
    }

    public boolean func_193472_a(EntityLivingBase entityliving) {
        return this == MobEffectsPredicate.field_193473_a ? true : this.func_193470_a(entityliving.func_193076_bZ());
    }

    public boolean func_193470_a(Map<Potion, PotionEffect> map) {
        if (this == MobEffectsPredicate.field_193473_a) {
            return true;
        } else {
            Iterator iterator = this.field_193474_b.entrySet().iterator();

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

    public static MobEffectsPredicate func_193471_a(@Nullable JsonElement jsonelement) {
        if (jsonelement != null && !jsonelement.isJsonNull()) {
            JsonObject jsonobject = JsonUtils.func_151210_l(jsonelement, "effects");
            HashMap hashmap = Maps.newHashMap();
            Iterator iterator = jsonobject.entrySet().iterator();

            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();
                ResourceLocation minecraftkey = new ResourceLocation((String) entry.getKey());
                Potion mobeffectlist = (Potion) Potion.field_188414_b.func_82594_a(minecraftkey);

                if (mobeffectlist == null) {
                    throw new JsonSyntaxException("Unknown effect \'" + minecraftkey + "\'");
                }

                CriterionConditionMobEffect.a criterionconditionmobeffect_a = MobEffectsPredicate.field_193473_a.func_193471_a(JsonUtils.func_151210_l((JsonElement) entry.getValue(), (String) entry.getKey()));

                hashmap.put(mobeffectlist, criterionconditionmobeffect_a);
            }

            return new MobEffectsPredicate(hashmap);
        } else {
            return MobEffectsPredicate.field_193473_a;
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
            return mobeffect == null ? false : (!this.a.func_192514_a((float) mobeffect.func_76458_c()) ? false : (!this.b.func_192514_a((float) mobeffect.func_76459_b()) ? false : (this.c != null && this.c.booleanValue() != mobeffect.func_82720_e() ? false : this.d == null || this.d.booleanValue() == mobeffect.func_188418_e())));
        }

        public static CriterionConditionMobEffect.a a(JsonObject jsonobject) {
            MinMaxBounds criterionconditionvalue = MinMaxBounds.func_192515_a(jsonobject.get("amplifier"));
            MinMaxBounds criterionconditionvalue1 = MinMaxBounds.func_192515_a(jsonobject.get("duration"));
            Boolean obool = jsonobject.has("ambient") ? Boolean.valueOf(JsonUtils.func_151212_i(jsonobject, "ambient")) : null;
            Boolean obool1 = jsonobject.has("visible") ? Boolean.valueOf(JsonUtils.func_151212_i(jsonobject, "visible")) : null;

            return new CriterionConditionMobEffect.a(criterionconditionvalue, criterionconditionvalue1, obool, obool1);
        }
    }
}
