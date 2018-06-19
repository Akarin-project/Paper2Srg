package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.item.EnumDyeColor;
import org.bukkit.DyeColor;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Wolf;

public class CraftWolf extends CraftTameableAnimal implements Wolf {
    public CraftWolf(CraftServer server, EntityWolf wolf) {
        super(server, wolf);
    }

    public boolean isAngry() {
        return getHandle().func_70919_bu();
    }

    public void setAngry(boolean angry) {
        getHandle().func_70916_h(angry);
    }

    @Override
    public EntityWolf getHandle() {
        return (EntityWolf) entity;
    }

    @Override
    public EntityType getType() {
        return EntityType.WOLF;
    }

    public DyeColor getCollarColor() {
        return DyeColor.getByWoolData((byte) getHandle().func_175546_cu().func_176765_a());
    }

    public void setCollarColor(DyeColor color) {
        getHandle().func_175547_a(EnumDyeColor.func_176764_b(color.getWoolData()));
    }
}
