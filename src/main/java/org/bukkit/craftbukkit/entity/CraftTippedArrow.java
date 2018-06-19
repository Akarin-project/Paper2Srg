package org.bukkit.craftbukkit.entity;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.bukkit.Color;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.potion.CraftPotionUtil;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TippedArrow;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import com.google.common.collect.ImmutableList;

import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.Potion;

public class CraftTippedArrow extends CraftArrow implements TippedArrow {

    public CraftTippedArrow(CraftServer server, EntityTippedArrow entity) {
        super(server, entity);
    }

    @Override
    public EntityTippedArrow getHandle() {
        return (EntityTippedArrow) entity;
    }

    @Override
    public String toString() {
        return "CraftTippedArrow";
    }

    @Override
    public EntityType getType() {
        return EntityType.TIPPED_ARROW;
    }

    @Override
    public boolean addCustomEffect(PotionEffect effect, boolean override) {
        int effectId = effect.getType().getId();
        PotionEffect existing = null;
        for (PotionEffect mobEffect : getHandle().field_184561_h) {
            if (Potion.func_188409_a(mobEffect.func_188419_a()) == effectId) {
                existing = mobEffect;
            }
        }
        if (existing != null) {
            if (!override) {
                return false;
            }
            getHandle().field_184561_h.remove(existing);
        }
        getHandle().func_184558_a(CraftPotionUtil.fromBukkit(effect));
        getHandle().refreshEffects();
        return true;
    }

    @Override
    public void clearCustomEffects() {
        Validate.isTrue(getBasePotionData().getType() != PotionType.UNCRAFTABLE, "Tipped Arrows must have at least 1 effect");
        getHandle().field_184561_h.clear();
        getHandle().refreshEffects();
    }

    @Override
    public List<PotionEffect> getCustomEffects() {
        ImmutableList.Builder<PotionEffect> builder = ImmutableList.builder();
        for (PotionEffect effect : getHandle().field_184561_h) {
            builder.add(CraftPotionUtil.toBukkit(effect));
        }
        return builder.build();
    }

    @Override
    public boolean hasCustomEffect(PotionEffectType type) {
        for (PotionEffect effect : getHandle().field_184561_h) {
            if (CraftPotionUtil.equals(effect.func_188419_a(), type)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasCustomEffects() {
        return !getHandle().field_184561_h.isEmpty();
    }

    @Override
    public boolean removeCustomEffect(PotionEffectType effect) {
        int effectId = effect.getId();
        PotionEffect existing = null;
        for (PotionEffect mobEffect : getHandle().field_184561_h) {
            if (Potion.func_188409_a(mobEffect.func_188419_a()) == effectId) {
                existing = mobEffect;
            }
        }
        if (existing == null) {
            return false;
        }
        Validate.isTrue(getBasePotionData().getType() != PotionType.UNCRAFTABLE || !getHandle().field_184561_h.isEmpty(), "Tipped Arrows must have at least 1 effect");
        getHandle().field_184561_h.remove(existing);
        getHandle().refreshEffects();
        return true;
    }

    @Override
    public void setBasePotionData(PotionData data) {
        Validate.notNull(data, "PotionData cannot be null");
        Validate.isTrue(data.getType() != PotionType.UNCRAFTABLE || !getHandle().field_184561_h.isEmpty(), "Tipped Arrows must have at least 1 effect");
        getHandle().setType(CraftPotionUtil.fromBukkit(data));
    }

    @Override
    public PotionData getBasePotionData() {
        return CraftPotionUtil.toBukkit(getHandle().getType());
    }

    @Override
    public void setColor(Color color) {
        getHandle().func_191507_d(color.asRGB());
    }

    @Override
    public Color getColor() {
        return Color.fromRGB(getHandle().func_184557_n());
    }
}
