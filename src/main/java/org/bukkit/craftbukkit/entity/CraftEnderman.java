package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.monster.EntityEnderman;

import net.minecraft.block.state.IBlockState;
import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.EntityType;
import org.bukkit.material.MaterialData;

public class CraftEnderman extends CraftMonster implements Enderman {
    public CraftEnderman(CraftServer server, EntityEnderman entity) {
        super(server, entity);
    }

    @Override public boolean teleportRandomly() { return getHandle().teleportRandomly(); } // Paper
    public MaterialData getCarriedMaterial() {
        IBlockState blockData = getHandle().func_175489_ck();
        return (blockData == null) ? Material.AIR.getNewData((byte) 0) : CraftMagicNumbers.getMaterial(blockData.func_177230_c()).getNewData((byte) blockData.func_177230_c().func_176201_c(blockData));
    }

    public void setCarriedMaterial(MaterialData data) {
        getHandle().func_175490_a(CraftMagicNumbers.getBlock(data.getItemTypeId()).func_176203_a(data.getData()));
    }

    @Override
    public EntityEnderman getHandle() {
        return (EntityEnderman) entity;
    }

    @Override
    public String toString() {
        return "CraftEnderman";
    }

    public EntityType getType() {
        return EntityType.ENDERMAN;
    }
}
