package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.item.EntityFallingBlock;

import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;

public class CraftFallingBlock extends CraftEntity implements FallingBlock {

    public CraftFallingBlock(CraftServer server, EntityFallingBlock entity) {
        super(server, entity);
    }

    @Override
    public EntityFallingBlock getHandle() {
        return (EntityFallingBlock) entity;
    }

    @Override
    public String toString() {
        return "CraftFallingBlock";
    }

    public EntityType getType() {
        return EntityType.FALLING_BLOCK;
    }

    public Material getMaterial() {
        return Material.getMaterial(getBlockId());
    }

    public int getBlockId() {
        return CraftMagicNumbers.getId(getHandle().func_175131_l().func_177230_c());
    }

    public byte getBlockData() {
        return (byte) getHandle().func_175131_l().func_177230_c().func_176201_c(getHandle().func_175131_l());
    }

    public boolean getDropItem() {
        return getHandle().field_145813_c;
    }

    public void setDropItem(boolean drop) {
        getHandle().field_145813_c = drop;
    }

    @Override
    public boolean canHurtEntities() {
        return getHandle().field_145809_g;
    }

    @Override
    public void setHurtEntities(boolean hurtEntities) {
        getHandle().field_145809_g = hurtEntities;
    }

    @Override
    public void setTicksLived(int value) {
        super.setTicksLived(value);

        // Second field for EntityFallingBlock
        getHandle().field_145812_b = value;
    }
}
