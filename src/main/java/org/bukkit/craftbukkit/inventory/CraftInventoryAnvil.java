package org.bukkit.craftbukkit.inventory;

import net.minecraft.inventory.ContainerRepair;
import net.minecraft.inventory.IInventory;
import org.bukkit.Location;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;

import net;

public class CraftInventoryAnvil extends CraftInventory implements AnvilInventory {

    private final Location location;
    private final IInventory resultInventory;
    private final ContainerRepair container;

    public CraftInventoryAnvil(Location location, IInventory inventory, IInventory resultInventory, ContainerRepair container) {
        super(inventory);
        this.location = location;
        this.resultInventory = resultInventory;
        this.container = container;
    }

    public IInventory getResultInventory() {
        return resultInventory;
    }

    public IInventory getIngredientsInventory() {
        return inventory;
    }

    @Override
    public ItemStack getItem(int slot) {
        if (slot < getIngredientsInventory().func_70302_i_()) {
            net.minecraft.item.ItemStack item = getIngredientsInventory().func_70301_a(slot);
            return item.func_190926_b() ? null : CraftItemStack.asCraftMirror(item);
        } else {
            net.minecraft.item.ItemStack item = getResultInventory().func_70301_a(slot - getIngredientsInventory().func_70302_i_());
            return item.func_190926_b() ? null : CraftItemStack.asCraftMirror(item);
        }
    }

    @Override
    public void setItem(int index, ItemStack item) {
        if (index < getIngredientsInventory().func_70302_i_()) {
            getIngredientsInventory().func_70299_a(index, CraftItemStack.asNMSCopy(item));
        } else {
            getResultInventory().func_70299_a((index - getIngredientsInventory().func_70302_i_()), CraftItemStack.asNMSCopy(item));
        }
    }

    @Override
    public int getSize() {
        return getResultInventory().func_70302_i_() + getIngredientsInventory().func_70302_i_();
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public String getRenameText() {
        return container.field_82857_m;
    }

    @Override
    public int getRepairCost() {
        return container.field_82854_e;
    }

    @Override
    public void setRepairCost(int i) {
        container.field_82854_e = i;
    }
}
