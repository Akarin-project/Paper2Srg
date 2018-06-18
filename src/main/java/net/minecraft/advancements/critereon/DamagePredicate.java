package net.minecraft.advancements.critereon;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.JsonUtils;

public class DamagePredicate {

    public static DamagePredicate ANY = new DamagePredicate();
    private final MinMaxBounds dealt;
    private final MinMaxBounds taken;
    private final EntityPredicate sourceEntity;
    private final Boolean blocked;
    private final DamageSourcePredicate type;

    public DamagePredicate() {
        this.dealt = MinMaxBounds.UNBOUNDED;
        this.taken = MinMaxBounds.UNBOUNDED;
        this.sourceEntity = EntityPredicate.ANY;
        this.blocked = null;
        this.type = DamageSourcePredicate.ANY;
    }

    public DamagePredicate(MinMaxBounds criterionconditionvalue, MinMaxBounds criterionconditionvalue1, EntityPredicate criterionconditionentity, @Nullable Boolean obool, DamageSourcePredicate criterionconditiondamagesource) {
        this.dealt = criterionconditionvalue;
        this.taken = criterionconditionvalue1;
        this.sourceEntity = criterionconditionentity;
        this.blocked = obool;
        this.type = criterionconditiondamagesource;
    }

    public boolean test(EntityPlayerMP entityplayer, DamageSource damagesource, float f, float f1, boolean flag) {
        return this == DamagePredicate.ANY ? true : (!this.dealt.test(f) ? false : (!this.taken.test(f1) ? false : (!this.sourceEntity.test(entityplayer, damagesource.getTrueSource()) ? false : (this.blocked != null && this.blocked.booleanValue() != flag ? false : this.type.test(entityplayer, damagesource)))));
    }

    public static DamagePredicate deserialize(@Nullable JsonElement jsonelement) {
        if (jsonelement != null && !jsonelement.isJsonNull()) {
            JsonObject jsonobject = JsonUtils.getJsonObject(jsonelement, "damage");
            MinMaxBounds criterionconditionvalue = MinMaxBounds.deserialize(jsonobject.get("dealt"));
            MinMaxBounds criterionconditionvalue1 = MinMaxBounds.deserialize(jsonobject.get("taken"));
            Boolean obool = jsonobject.has("blocked") ? Boolean.valueOf(JsonUtils.getBoolean(jsonobject, "blocked")) : null;
            EntityPredicate criterionconditionentity = EntityPredicate.deserialize(jsonobject.get("source_entity"));
            DamageSourcePredicate criterionconditiondamagesource = DamageSourcePredicate.deserialize(jsonobject.get("type"));

            return new DamagePredicate(criterionconditionvalue, criterionconditionvalue1, criterionconditionentity, obool, criterionconditiondamagesource);
        } else {
            return DamagePredicate.ANY;
        }
    }
}
