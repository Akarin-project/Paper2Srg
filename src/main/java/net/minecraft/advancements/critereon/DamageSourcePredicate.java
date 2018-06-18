package net.minecraft.advancements.critereon;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.JsonUtils;

public class DamageSourcePredicate {

    public static DamageSourcePredicate ANY = new DamageSourcePredicate();
    private final Boolean isProjectile;
    private final Boolean isExplosion;
    private final Boolean bypassesArmor;
    private final Boolean bypassesInvulnerability;
    private final Boolean bypassesMagic;
    private final Boolean isFire;
    private final Boolean isMagic;
    private final EntityPredicate directEntity;
    private final EntityPredicate sourceEntity;

    public DamageSourcePredicate() {
        this.isProjectile = null;
        this.isExplosion = null;
        this.bypassesArmor = null;
        this.bypassesInvulnerability = null;
        this.bypassesMagic = null;
        this.isFire = null;
        this.isMagic = null;
        this.directEntity = EntityPredicate.ANY;
        this.sourceEntity = EntityPredicate.ANY;
    }

    public DamageSourcePredicate(@Nullable Boolean obool, @Nullable Boolean obool1, @Nullable Boolean obool2, @Nullable Boolean obool3, @Nullable Boolean obool4, @Nullable Boolean obool5, @Nullable Boolean obool6, EntityPredicate criterionconditionentity, EntityPredicate criterionconditionentity1) {
        this.isProjectile = obool;
        this.isExplosion = obool1;
        this.bypassesArmor = obool2;
        this.bypassesInvulnerability = obool3;
        this.bypassesMagic = obool4;
        this.isFire = obool5;
        this.isMagic = obool6;
        this.directEntity = criterionconditionentity;
        this.sourceEntity = criterionconditionentity1;
    }

    public boolean test(EntityPlayerMP entityplayer, DamageSource damagesource) {
        return this == DamageSourcePredicate.ANY ? true : (this.isProjectile != null && this.isProjectile.booleanValue() != damagesource.isProjectile() ? false : (this.isExplosion != null && this.isExplosion.booleanValue() != damagesource.isExplosion() ? false : (this.bypassesArmor != null && this.bypassesArmor.booleanValue() != damagesource.isUnblockable() ? false : (this.bypassesInvulnerability != null && this.bypassesInvulnerability.booleanValue() != damagesource.canHarmInCreative() ? false : (this.bypassesMagic != null && this.bypassesMagic.booleanValue() != damagesource.isDamageAbsolute() ? false : (this.isFire != null && this.isFire.booleanValue() != damagesource.isFireDamage() ? false : (this.isMagic != null && this.isMagic.booleanValue() != damagesource.isMagicDamage() ? false : (!this.directEntity.test(entityplayer, damagesource.getImmediateSource()) ? false : this.sourceEntity.test(entityplayer, damagesource.getTrueSource())))))))));
    }

    public static DamageSourcePredicate deserialize(@Nullable JsonElement jsonelement) {
        if (jsonelement != null && !jsonelement.isJsonNull()) {
            JsonObject jsonobject = JsonUtils.getJsonObject(jsonelement, "damage type");
            Boolean obool = optionalBoolean(jsonobject, "is_projectile");
            Boolean obool1 = optionalBoolean(jsonobject, "is_explosion");
            Boolean obool2 = optionalBoolean(jsonobject, "bypasses_armor");
            Boolean obool3 = optionalBoolean(jsonobject, "bypasses_invulnerability");
            Boolean obool4 = optionalBoolean(jsonobject, "bypasses_magic");
            Boolean obool5 = optionalBoolean(jsonobject, "is_fire");
            Boolean obool6 = optionalBoolean(jsonobject, "is_magic");
            EntityPredicate criterionconditionentity = EntityPredicate.deserialize(jsonobject.get("direct_entity"));
            EntityPredicate criterionconditionentity1 = EntityPredicate.deserialize(jsonobject.get("source_entity"));

            return new DamageSourcePredicate(obool, obool1, obool2, obool3, obool4, obool5, obool6, criterionconditionentity, criterionconditionentity1);
        } else {
            return DamageSourcePredicate.ANY;
        }
    }

    @Nullable
    private static Boolean optionalBoolean(JsonObject jsonobject, String s) {
        return jsonobject.has(s) ? Boolean.valueOf(JsonUtils.getBoolean(jsonobject, s)) : null;
    }
}
