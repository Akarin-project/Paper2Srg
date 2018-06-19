package org.bukkit.craftbukkit.block;

import net.minecraft.tileentity.TileEntityBrewingStand;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BrewingStand;
import org.bukkit.craftbukkit.inventory.CraftInventoryBrewer;
import org.bukkit.inventory.BrewerInventory;

public class CraftBrewingStand extends CraftContainer<TileEntityBrewingStand> implements BrewingStand {

    public CraftBrewingStand(Block block) {
        super(block, TileEntityBrewingStand.class);
    }

    public CraftBrewingStand(final Material material, final TileEntityBrewingStand te) {
        super(material, te);
    }

    @Override
    public BrewerInventory getSnapshotInventory() {
        return new CraftInventoryBrewer(this.getSnapshot());
    }

    @Override
    public BrewerInventory getInventory() {
        if (!this.isPlaced()) {
            return this.getSnapshotInventory();
        }

        return new CraftInventoryBrewer(this.getTileEntity());
    }

    @Override
    public int getBrewingTime() {
        return this.getSnapshot().func_174887_a_(0);
    }

    @Override
    public void setBrewingTime(int brewTime) {
        this.getSnapshot().func_174885_b(0, brewTime);
    }

    @Override
    public int getFuelLevel() {
        return this.getSnapshot().func_174887_a_(1);
    }

    @Override
    public void setFuelLevel(int level) {
        this.getSnapshot().func_174885_b(1, level);
    }

    @Override
    public String getCustomName() {
        TileEntityBrewingStand brewingStand = this.getSnapshot();
        return brewingStand.func_145818_k_() ? brewingStand.func_70005_c_() : null;
    }

    @Override
    public void setCustomName(String name) {
        this.getSnapshot().func_145937_a(name);
    }

    @Override
    public void applyTo(TileEntityBrewingStand brewingStand) {
        super.applyTo(brewingStand);

        if (!this.getSnapshot().func_145818_k_()) {
            brewingStand.func_145937_a(null);
        }
    }
}
