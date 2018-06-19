package org.bukkit.craftbukkit.inventory;

import java.util.Arrays;
import java.util.List;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;

import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import net;

public class CraftInventoryCrafting extends CraftInventory implements CraftingInventory {
    private final IInventory resultInventory;

    public CraftInventoryCrafting(InventoryCrafting inventory, IInventory resultInventory) {
        super(inventory);
        this.resultInventory = resultInventory;
    }

    public IInventory getResultInventory() {
        return resultInventory;
    }

    public IInventory getMatrixInventory() {
        return inventory;
    }

    @Override
    public int getSize() {
        return getResultInventory().func_70302_i_() + getMatrixInventory().func_70302_i_();
    }

    @Override
    public void setContents(ItemStack[] items) {
        if (getSize() > items.length) {
            throw new IllegalArgumentException("Invalid inventory size; expected " + getSize() + " or less");
        }
        setContents(items[0], Arrays.copyOfRange(items, 1, items.length));
    }

    @Override
    public ItemStack[] getContents() {
        ItemStack[] items = new ItemStack[getSize()];
        List<net.minecraft.item.ItemStack> mcResultItems = getResultInventory().getContents();

        int i = 0;
        for (i = 0; i < mcResultItems.size(); i++ ) {
            items[i] = CraftItemStack.asCraftMirror(mcResultItems.get(i));
        }

        List<net.minecraft.item.ItemStack> mcItems = getMatrixInventory().getContents();

        for (int j = 0; j < mcItems.size(); j++) {
            items[i + j] = CraftItemStack.asCraftMirror(mcItems.get(j));
        }

        return items;
    }

    public void setContents(ItemStack result, ItemStack[] contents) {
        setResult(result);
        setMatrix(contents);
    }

    @Override
    public CraftItemStack getItem(int index) {
        if (index < getResultInventory().func_70302_i_()) {
            net.minecraft.item.ItemStack item = getResultInventory().func_70301_a(index);
            return item.func_190926_b() ? null : CraftItemStack.asCraftMirror(item);
        } else {
            net.minecraft.item.ItemStack item = getMatrixInventory().func_70301_a(index - getResultInventory().func_70302_i_());
            return item.func_190926_b() ? null : CraftItemStack.asCraftMirror(item);
        }
    }

    @Override
    public void setItem(int index, ItemStack item) {
        if (index < getResultInventory().func_70302_i_()) {
            getResultInventory().func_70299_a(index, CraftItemStack.asNMSCopy(item));
        } else {
            getMatrixInventory().func_70299_a((index - getResultInventory().func_70302_i_()), CraftItemStack.asNMSCopy(item));
        }
    }

    public ItemStack[] getMatrix() {
        List<net.minecraft.item.ItemStack> matrix = getMatrixInventory().getContents();

        return asCraftMirror(matrix);
    }

    public ItemStack getResult() {
        net.minecraft.item.ItemStack item = getResultInventory().func_70301_a(0);
        if (!item.func_190926_b()) return CraftItemStack.asCraftMirror(item);
        return null;
    }

    public void setMatrix(ItemStack[] contents) {
        if (getMatrixInventory().func_70302_i_() > contents.length) {
            throw new IllegalArgumentException("Invalid inventory size; expected " + getMatrixInventory().func_70302_i_() + " or less");
        }

        for (int i = 0; i < getMatrixInventory().func_70302_i_(); i++) {
            if (i < contents.length) {
                getMatrixInventory().func_70299_a(i, CraftItemStack.asNMSCopy(contents[i]));
            } else {
                getMatrixInventory().func_70299_a(i, net.minecraft.item.ItemStack.field_190927_a);
            }
        }
    }

    public void setResult(ItemStack item) {
        List<net.minecraft.item.ItemStack> contents = getResultInventory().getContents();
        contents.set(0, CraftItemStack.asNMSCopy(item));
    }

    public Recipe getRecipe() {
        IRecipe recipe = ((InventoryCrafting)getInventory()).currentRecipe;
        return recipe == null ? null : recipe.toBukkitRecipe();
    }
}
