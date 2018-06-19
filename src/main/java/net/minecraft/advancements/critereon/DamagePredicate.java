package net.minecraft.advancements.critereon;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.JsonUtils;

public class DamagePredicate {

    public static DamagePredicate field_192366_a = new DamagePredicate();
    private final MinMaxBounds field_192367_b;
    private final MinMaxBounds field_192368_c;
    private final EntityPredicate field_192369_d;
    private final Boolean field_192370_e;
    private final DamageSourcePredicate field_192371_f;

    public DamagePredicate() {
        this.field_192367_b = MinMaxBounds.field_192516_a;
        this.field_192368_c = MinMaxBounds.field_192516_a;
        this.field_192369_d = EntityPredicate.field_192483_a;
        this.field_192370_e = null;
        this.field_192371_f = DamageSourcePredicate.field_192449_a;
    }

    public DamagePredicate(MinMaxBounds criterionconditionvalue, MinMaxBounds criterionconditionvalue1, EntityPredicate criterionconditionentity, @Nullable Boolean obool, DamageSourcePredicate criterionconditiondamagesource) {
        this.field_192367_b = criterionconditionvalue;
        this.field_192368_c = criterionconditionvalue1;
        this.field_192369_d = criterionconditionentity;
        this.field_192370_e = obool;
        this.field_192371_f = criterionconditiondamagesource;
    }

    public boolean func_192365_a(EntityPlayerMP entityplayer, DamageSource damagesource, float f, float f1, boolean flag) {
        return this == DamagePredicate.field_192366_a ? true : (!this.field_192367_b.func_192514_a(f) ? false : (!this.field_192368_c.func_192514_a(f1) ? false : (!this.field_192369_d.func_192482_a(entityplayer, damagesource.func_76346_g()) ? false : (this.field_192370_e != null && this.field_192370_e.booleanValue() != flag ? false : this.field_192371_f.func_193418_a(entityplayer, damagesource)))));
    }

    public static DamagePredicate func_192364_a(@Nullable JsonElement jsonelement) {
        if (jsonelement != null && !jsonelement.isJsonNull()) {
            JsonObject jsonobject = JsonUtils.func_151210_l(jsonelement, "damage");
            MinMaxBounds criterionconditionvalue = MinMaxBounds.func_192515_a(jsonobject.get("dealt"));
            MinMaxBounds criterionconditionvalue1 = MinMaxBounds.func_192515_a(jsonobject.get("taken"));
            Boolean obool = jsonobject.has("blocked") ? Boolean.valueOf(JsonUtils.func_151212_i(jsonobject, "blocked")) : null;
            EntityPredicate criterionconditionentity = EntityPredicate.func_192481_a(jsonobject.get("source_entity"));
            DamageSourcePredicate criterionconditiondamagesource = DamageSourcePredicate.func_192447_a(jsonobject.get("type"));

            return new DamagePredicate(criterionconditionvalue, criterionconditionvalue1, criterionconditionentity, obool, criterionconditiondamagesource);
        } else {
            return DamagePredicate.field_192366_a;
        }
    }
}
