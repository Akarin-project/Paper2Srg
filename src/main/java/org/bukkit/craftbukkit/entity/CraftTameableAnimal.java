package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.passive.EntityTameable;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Tameable;

import java.util.UUID;

public class CraftTameableAnimal extends CraftAnimals implements Tameable, Creature {
    public CraftTameableAnimal(CraftServer server, EntityTameable entity) {
        super(server, entity);
    }

    @Override
    public EntityTameable getHandle() {
        return (EntityTameable)super.getHandle();
    }

    public UUID getOwnerUniqueId() {
        return getOwnerUUID();
    }
    public UUID getOwnerUUID() {
        try {
            return getHandle().func_184753_b();
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    public void setOwnerUUID(UUID uuid) {
        getHandle().func_184754_b(uuid);
    }

    public AnimalTamer getOwner() {
        if (getOwnerUUID() == null) {
            return null;
        }

        AnimalTamer owner = getServer().getPlayer(getOwnerUUID());
        if (owner == null) {
            owner = getServer().getOfflinePlayer(getOwnerUUID());
        }

        return owner;
    }

    public boolean isTamed() {
        return getHandle().func_70909_n();
    }

    public void setOwner(AnimalTamer tamer) {
        if (tamer != null) {
            setTamed(true);
            getHandle().setGoalTarget(null, null, false);
            setOwnerUUID(tamer.getUniqueId());
        } else {
            setTamed(false);
            setOwnerUUID(null);
        }
    }

    public void setTamed(boolean tame) {
        getHandle().func_70903_f(tame);
        if (!tame) {
            setOwnerUUID(null);
        }
    }

    public boolean isSitting() {
        return getHandle().func_70906_o();
    }

    public void setSitting(boolean sitting) {
        getHandle().func_70904_g(sitting);
        getHandle().func_70907_r().func_75270_a(sitting);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{owner=" + getOwner() + ",tamed=" + isTamed() + "}";
    }
}
