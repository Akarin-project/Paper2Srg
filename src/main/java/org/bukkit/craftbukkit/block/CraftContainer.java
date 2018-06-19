package org.bukkit.craftbukkit.block;

import net.minecraft.world.LockCode;
import net.minecraft.tileentity.TileEntityLockable;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;

public abstract class CraftContainer<T extends TileEntityLockable> extends CraftBlockEntityState<T> implements Container {

    public CraftContainer(Block block, Class<T> tileEntityClass) {
        super(block, tileEntityClass);
    }

    public CraftContainer(final Material material, T tileEntity) {
        super(material, tileEntity);
    }

    @Override
    public boolean isLocked() {
        return this.getSnapshot().func_174893_q_();
    }

    @Override
    public String getLock() {
        return this.getSnapshot().func_174891_i().func_180159_b();
    }

    @Override
    public void setLock(String key) {
        this.getSnapshot().func_174892_a(key == null ? LockCode.field_180162_a : new LockCode(key));
    }
}
