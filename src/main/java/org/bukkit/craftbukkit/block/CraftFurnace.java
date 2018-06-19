package org.bukkit.craftbukkit.block;

import net.minecraft.tileentity.TileEntityFurnace;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.craftbukkit.inventory.CraftInventoryFurnace;
import org.bukkit.inventory.FurnaceInventory;

public class CraftFurnace extends CraftContainer<TileEntityFurnace> implements Furnace {

    public CraftFurnace(final Block block) {
        super(block, TileEntityFurnace.class);
    }

    public CraftFurnace(final Material material, final TileEntityFurnace te) {
        super(material, te);
    }

    @Override
    public FurnaceInventory getSnapshotInventory() {
        return new CraftInventoryFurnace(this.getSnapshot());
    }

    @Override
    public FurnaceInventory getInventory() {
        if (!this.isPlaced()) {
            return this.getSnapshotInventory();
        }

        return new CraftInventoryFurnace(this.getTileEntity());
    }

    @Override
    public short getBurnTime() {
        return (short) this.getSnapshot().func_174887_a_(0);
    }

    @Override
    public void setBurnTime(short burnTime) {
        this.getSnapshot().func_174885_b(0, burnTime);
    }

    @Override
    public short getCookTime() {
        return (short) this.getSnapshot().func_174887_a_(2);
    }

    @Override
    public void setCookTime(short cookTime) {
        this.getSnapshot().func_174885_b(2, cookTime);
    }

    @Override
    public String getCustomName() {
        TileEntityFurnace furnace = this.getSnapshot();
        return furnace.func_145818_k_() ? furnace.func_70005_c_() : null;
    }

    @Override
    public void setCustomName(String name) {
        this.getSnapshot().func_145951_a(name);
    }

    @Override
    public void applyTo(TileEntityFurnace furnace) {
        super.applyTo(furnace);

        if (!this.getSnapshot().func_145818_k_()) {
            furnace.func_145951_a(null);
        }
    }
}
