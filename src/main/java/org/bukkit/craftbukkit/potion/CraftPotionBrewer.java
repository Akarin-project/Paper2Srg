package org.bukkit.craftbukkit.potion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;

import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionBrewer;
import org.bukkit.potion.PotionData;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

public class CraftPotionBrewer implements PotionBrewer {
    private static final Map<org.bukkit.potion.PotionType, Collection<org.bukkit.potion.PotionEffect>> cache = Maps.newHashMap();

    @Override
    public Collection<org.bukkit.potion.PotionEffect> getEffects(org.bukkit.potion.PotionType damage, boolean upgraded, boolean extended) {
        if (cache.containsKey(damage))
            return cache.get(damage);

        List<PotionEffect> mcEffects = PotionType.func_185168_a(CraftPotionUtil.fromBukkit(new PotionData(damage, extended, upgraded))).func_185170_a();

        ImmutableList.Builder<org.bukkit.potion.PotionEffect> builder = new ImmutableList.Builder<org.bukkit.potion.PotionEffect>();
        for (PotionEffect effect : mcEffects) {
            builder.add(CraftPotionUtil.toBukkit(effect));
        }

        cache.put(damage, builder.build());

        return cache.get(damage);
    }

    @Override
    public Collection<org.bukkit.potion.PotionEffect> getEffectsFromDamage(int damage) {
        return new ArrayList<org.bukkit.potion.PotionEffect>();
    }

    @Override
    public org.bukkit.potion.PotionEffect createEffect(PotionEffectType potion, int duration, int amplifier) {
        return new org.bukkit.potion.PotionEffect(potion, potion.isInstant() ? 1 : (int) (duration * potion.getDurationModifier()), amplifier);
    }
}
