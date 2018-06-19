package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.passive.EntitySheep;

import net.minecraft.item.EnumDyeColor;
import org.bukkit.DyeColor;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Sheep;

public class CraftSheep extends CraftAnimals implements Sheep {
    public CraftSheep(CraftServer server, EntitySheep entity) {
        super(server, entity);
    }

    public DyeColor getColor() {
        return DyeColor.getByWoolData((byte) getHandle().func_175509_cj().func_176765_a());
    }

    public void setColor(DyeColor color) {
        getHandle().func_175512_b(EnumDyeColor.func_176764_b(color.getWoolData()));
    }

    public boolean isSheared() {
        return getHandle().func_70892_o();
    }

    public void setSheared(boolean flag) {
        getHandle().func_70893_e(flag);
    }

    @Override
    public EntitySheep getHandle() {
        return (EntitySheep) entity;
    }

    @Override
    public String toString() {
        return "CraftSheep";
    }

    public EntityType getType() {
        return EntityType.SHEEP;
    }
}
