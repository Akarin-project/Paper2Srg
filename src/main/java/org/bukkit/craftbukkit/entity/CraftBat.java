package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.passive.EntityBat;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Bat;
import org.bukkit.entity.EntityType;

public class CraftBat extends CraftAmbient implements Bat {
    public CraftBat(CraftServer server, EntityBat entity) {
        super(server, entity);
    }

    @Override
    public EntityBat getHandle() {
        return (EntityBat) entity;
    }

    @Override
    public String toString() {
        return "CraftBat";
    }

    public EntityType getType() {
        return EntityType.BAT;
    }

    @Override
    public boolean isAwake() {
        return !getHandle().func_82235_h();
    }

    @Override
    public void setAwake(boolean state) {
        getHandle().func_82236_f(!state);
    }
}
