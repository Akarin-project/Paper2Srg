package org.bukkit.craftbukkit.block;

import net.minecraft.tileentity.TileEntityLockableLoot;
import org.bukkit.Material;
import org.bukkit.Nameable;
import org.bukkit.block.Block;

public abstract class CraftLootable<T extends TileEntityLockableLoot> extends CraftContainer<T> implements Nameable {

    public CraftLootable(Block block, Class<T> tileEntityClass) {
        super(block, tileEntityClass);
    }

    public CraftLootable(Material material, T tileEntity) {
        super(material, tileEntity);
    }

    @Override
    public String getCustomName() {
        T lootable = this.getSnapshot();
        return lootable.func_145818_k_() ? lootable.func_70005_c_() : null;
    }

    @Override
    public void setCustomName(String name) {
        this.getSnapshot().func_190575_a(name);
    }

    @Override
    public void applyTo(T lootable) {
        super.applyTo(lootable);

        if (!this.getSnapshot().func_145818_k_()) {
            lootable.func_190575_a(null);
        }
    }
}
