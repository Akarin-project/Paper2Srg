package org.bukkit.craftbukkit.entity;

import java.util.UUID;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.AbstractHorse;
import org.apache.commons.lang.Validate;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftSaddledInventory;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Horse;
import org.bukkit.inventory.AbstractHorseInventory;

public abstract class CraftAbstractHorse extends CraftAnimals implements org.bukkit.entity.AbstractHorse {

    public CraftAbstractHorse(CraftServer server, AbstractHorse entity) {
        super(server, entity);
    }

    @Override
    public AbstractHorse getHandle() {
        return (AbstractHorse) entity;
    }

    @Override
    public void setVariant(Horse.Variant variant) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public int getDomestication() {
        return getHandle().func_110252_cg();
    }

    @Override
    public void setDomestication(int value) {
        Validate.isTrue(value >= 0, "Domestication cannot be less than zero");
        Validate.isTrue(value <= getMaxDomestication(), "Domestication cannot be greater than the max domestication");
        getHandle().func_110238_s(value);
    }

    @Override
    public int getMaxDomestication() {
        return getHandle().func_190676_dC();
    }

    @Override
    public void setMaxDomestication(int value) {
        Validate.isTrue(value > 0, "Max domestication cannot be zero or less");
        getHandle().maxDomestication = value;
    }

    @Override
    public double getJumpStrength() {
        return getHandle().func_110215_cj();
    }

    @Override
    public void setJumpStrength(double strength) {
        Validate.isTrue(strength >= 0, "Jump strength cannot be less than zero");
        getHandle().func_110148_a(EntityHorse.field_110271_bv).func_111128_a(strength);
    }

    @Override
    public boolean isTamed() {
        return getHandle().func_110248_bS();
    }

    @Override
    public void setTamed(boolean tamed) {
        getHandle().func_110234_j(tamed);
    }

    @Override
    public AnimalTamer getOwner() {
        if (getOwnerUUID() == null) return null;
        return getServer().getOfflinePlayer(getOwnerUUID());
    }

    @Override
    public void setOwner(AnimalTamer owner) {
        if (owner != null) {
            setTamed(true);
            getHandle().setGoalTarget(null, null, false);
            setOwnerUUID(owner.getUniqueId());
        } else {
            setTamed(false);
            setOwnerUUID(null);
        }
    }

    @Override
    public UUID getOwnerUniqueId() {
        return getOwnerUUID();
    }
    public UUID getOwnerUUID() {
        return getHandle().func_184780_dh();
    }

    public void setOwnerUUID(UUID uuid) {
        getHandle().func_184779_b(uuid);
    }

    @Override
    public AbstractHorseInventory getInventory() {
        return new CraftSaddledInventory(getHandle().field_110296_bG);
    }
}
