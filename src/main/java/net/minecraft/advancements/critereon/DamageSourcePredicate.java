package net.minecraft.advancements.critereon;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.JsonUtils;

public class DamageSourcePredicate {

    public static DamageSourcePredicate field_192449_a = new DamageSourcePredicate();
    private final Boolean field_192450_b;
    private final Boolean field_192451_c;
    private final Boolean field_192452_d;
    private final Boolean field_192453_e;
    private final Boolean field_192454_f;
    private final Boolean field_192455_g;
    private final Boolean field_192456_h;
    private final EntityPredicate field_193419_i;
    private final EntityPredicate field_193420_j;

    public DamageSourcePredicate() {
        this.field_192450_b = null;
        this.field_192451_c = null;
        this.field_192452_d = null;
        this.field_192453_e = null;
        this.field_192454_f = null;
        this.field_192455_g = null;
        this.field_192456_h = null;
        this.field_193419_i = EntityPredicate.field_192483_a;
        this.field_193420_j = EntityPredicate.field_192483_a;
    }

    public DamageSourcePredicate(@Nullable Boolean obool, @Nullable Boolean obool1, @Nullable Boolean obool2, @Nullable Boolean obool3, @Nullable Boolean obool4, @Nullable Boolean obool5, @Nullable Boolean obool6, EntityPredicate criterionconditionentity, EntityPredicate criterionconditionentity1) {
        this.field_192450_b = obool;
        this.field_192451_c = obool1;
        this.field_192452_d = obool2;
        this.field_192453_e = obool3;
        this.field_192454_f = obool4;
        this.field_192455_g = obool5;
        this.field_192456_h = obool6;
        this.field_193419_i = criterionconditionentity;
        this.field_193420_j = criterionconditionentity1;
    }

    public boolean func_193418_a(EntityPlayerMP entityplayer, DamageSource damagesource) {
        return this == DamageSourcePredicate.field_192449_a ? true : (this.field_192450_b != null && this.field_192450_b.booleanValue() != damagesource.func_76352_a() ? false : (this.field_192451_c != null && this.field_192451_c.booleanValue() != damagesource.func_94541_c() ? false : (this.field_192452_d != null && this.field_192452_d.booleanValue() != damagesource.func_76363_c() ? false : (this.field_192453_e != null && this.field_192453_e.booleanValue() != damagesource.func_76357_e() ? false : (this.field_192454_f != null && this.field_192454_f.booleanValue() != damagesource.func_151517_h() ? false : (this.field_192455_g != null && this.field_192455_g.booleanValue() != damagesource.func_76347_k() ? false : (this.field_192456_h != null && this.field_192456_h.booleanValue() != damagesource.func_82725_o() ? false : (!this.field_193419_i.func_192482_a(entityplayer, damagesource.func_76364_f()) ? false : this.field_193420_j.func_192482_a(entityplayer, damagesource.func_76346_g())))))))));
    }

    public static DamageSourcePredicate func_192447_a(@Nullable JsonElement jsonelement) {
        if (jsonelement != null && !jsonelement.isJsonNull()) {
            JsonObject jsonobject = JsonUtils.func_151210_l(jsonelement, "damage type");
            Boolean obool = func_192448_a(jsonobject, "is_projectile");
            Boolean obool1 = func_192448_a(jsonobject, "is_explosion");
            Boolean obool2 = func_192448_a(jsonobject, "bypasses_armor");
            Boolean obool3 = func_192448_a(jsonobject, "bypasses_invulnerability");
            Boolean obool4 = func_192448_a(jsonobject, "bypasses_magic");
            Boolean obool5 = func_192448_a(jsonobject, "is_fire");
            Boolean obool6 = func_192448_a(jsonobject, "is_magic");
            EntityPredicate criterionconditionentity = EntityPredicate.func_192481_a(jsonobject.get("direct_entity"));
            EntityPredicate criterionconditionentity1 = EntityPredicate.func_192481_a(jsonobject.get("source_entity"));

            return new DamageSourcePredicate(obool, obool1, obool2, obool3, obool4, obool5, obool6, criterionconditionentity, criterionconditionentity1);
        } else {
            return DamageSourcePredicate.field_192449_a;
        }
    }

    @Nullable
    private static Boolean func_192448_a(JsonObject jsonobject, String s) {
        return jsonobject.has(s) ? Boolean.valueOf(JsonUtils.func_151212_i(jsonobject, s)) : null;
    }
}
