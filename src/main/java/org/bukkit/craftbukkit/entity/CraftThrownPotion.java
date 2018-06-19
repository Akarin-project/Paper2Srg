package org.bukkit.craftbukkit.entity;

import java.util.Collection;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.potion.CraftPotionUtil;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import com.google.common.collect.ImmutableList;

public abstract class CraftThrownPotion extends CraftProjectile implements ThrownPotion {
    public CraftThrownPotion(CraftServer server, EntityPotion entity) {
        super(server, entity);
    }

    public Collection<PotionEffect> getEffects() {
        ImmutableList.Builder<PotionEffect> builder = ImmutableList.builder();
        for (PotionEffect effect : PotionUtils.func_185189_a(getHandle().func_184543_l())) {
            builder.add(CraftPotionUtil.toBukkit(effect));
        }
        return builder.build();
    }

    public ItemStack getItem() {
        return CraftItemStack.asBukkitCopy(getHandle().func_184543_l());
    }

    @Override
    public EntityPotion getHandle() {
        return (EntityPotion) entity;
    }
}
