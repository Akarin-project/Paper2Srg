package org.bukkit.craftbukkit.entity;

import java.util.List;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.Potion;

import org.apache.commons.lang.Validate;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.CraftParticle;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.potion.CraftPotionUtil;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.potion.PotionData;

import com.google.common.collect.ImmutableList;

public class CraftAreaEffectCloud extends CraftEntity implements AreaEffectCloud {

    public CraftAreaEffectCloud(CraftServer server, EntityAreaEffectCloud entity) {
        super(server, entity);
    }

    @Override
    public EntityAreaEffectCloud getHandle() {
        return (EntityAreaEffectCloud) super.getHandle();
    }

    @Override
    public EntityType getType() {
        return EntityType.AREA_EFFECT_CLOUD;
    }

    @Override
    public int getDuration() {
        return getHandle().func_184489_o();
    }

    @Override
    public void setDuration(int duration) {
        getHandle().func_184486_b(duration);
    }

    @Override
    public int getWaitTime() {
        return getHandle().field_184506_as;
    }

    @Override
    public void setWaitTime(int waitTime) {
        getHandle().func_184485_d(waitTime);
    }

    @Override
    public int getReapplicationDelay() {
        return getHandle().field_184507_at;
    }

    @Override
    public void setReapplicationDelay(int delay) {
        getHandle().field_184507_at = delay;
    }

    @Override
    public int getDurationOnUse() {
        return getHandle().field_184509_av;
    }

    @Override
    public void setDurationOnUse(int duration) {
        getHandle().field_184509_av = duration;
    }

    @Override
    public float getRadius() {
        return getHandle().func_184490_j();
    }

    @Override
    public void setRadius(float radius) {
        getHandle().func_184483_a(radius);
    }

    @Override
    public float getRadiusOnUse() {
        return getHandle().field_184510_aw;
    }

    @Override
    public void setRadiusOnUse(float radius) {
        getHandle().func_184495_b(radius);
    }

    @Override
    public float getRadiusPerTick() {
        return getHandle().field_184511_ax;
    }

    @Override
    public void setRadiusPerTick(float radius) {
        getHandle().func_184487_c(radius);
    }

    @Override
    public Particle getParticle() {
        return CraftParticle.toBukkit(getHandle().func_184493_l());
    }

    @Override
    public void setParticle(Particle particle) {
        getHandle().func_184491_a(CraftParticle.toNMS(particle));
    }

    @Override
    public Color getColor() {
        return Color.fromRGB(getHandle().func_184492_k());
    }

    @Override
    public void setColor(Color color) {
        getHandle().func_184482_a(color.asRGB());
    }

    @Override
    public boolean addCustomEffect(org.bukkit.potion.PotionEffect effect, boolean override) {
        int effectId = effect.getType().getId();
        PotionEffect existing = null;
        for (PotionEffect mobEffect : getHandle().field_184503_f) {
            if (Potion.func_188409_a(mobEffect.func_188419_a()) == effectId) {
                existing = mobEffect;
            }
        }
        if (existing != null) {
            if (!override) {
                return false;
            }
            getHandle().field_184503_f.remove(existing);
        }
        getHandle().func_184496_a(CraftPotionUtil.fromBukkit(effect));
        getHandle().refreshEffects();
        return true;
    }

    @Override
    public void clearCustomEffects() {
        getHandle().field_184503_f.clear();
        getHandle().refreshEffects();
    }

    @Override
    public List<org.bukkit.potion.PotionEffect> getCustomEffects() {
        ImmutableList.Builder<org.bukkit.potion.PotionEffect> builder = ImmutableList.builder();
        for (PotionEffect effect : getHandle().field_184503_f) {
            builder.add(CraftPotionUtil.toBukkit(effect));
        }
        return builder.build();
    }

    @Override
    public boolean hasCustomEffect(PotionEffectType type) {
        for (PotionEffect effect : getHandle().field_184503_f) {
            if (CraftPotionUtil.equals(effect.func_188419_a(), type)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasCustomEffects() {
        return !getHandle().field_184503_f.isEmpty();
    }

    @Override
    public boolean removeCustomEffect(PotionEffectType effect) {
        int effectId = effect.getId();
        PotionEffect existing = null;
        for (PotionEffect mobEffect : getHandle().field_184503_f) {
            if (Potion.func_188409_a(mobEffect.func_188419_a()) == effectId) {
                existing = mobEffect;
            }
        }
        if (existing == null) {
            return false;
        }
        getHandle().field_184503_f.remove(existing);
        getHandle().refreshEffects();
        return true;
    }

    @Override
    public void setBasePotionData(PotionData data) {
        Validate.notNull(data, "PotionData cannot be null");
        getHandle().setType(CraftPotionUtil.fromBukkit(data));
    }

    @Override
    public PotionData getBasePotionData() {
        return CraftPotionUtil.toBukkit(getHandle().getType());
    }

    @Override
    public ProjectileSource getSource() {
        EntityLivingBase source = getHandle().func_184494_w();
        return (source == null) ? null : (LivingEntity) source.getBukkitEntity();
    }

    @Override
    public void setSource(ProjectileSource shooter) {
        if (shooter instanceof CraftLivingEntity) {
            getHandle().func_184481_a(((CraftLivingEntity) shooter).getHandle());
        } else {
            getHandle().func_184481_a((EntityLivingBase) null);
        }
    }
}
