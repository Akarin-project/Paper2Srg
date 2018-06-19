package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class InventoryWrapper implements IInventory {

    private final Inventory inventory;
    private final List<HumanEntity> viewers = new ArrayList<HumanEntity>();

    public InventoryWrapper(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public int func_70302_i_() {
        return inventory.getSize();
    }

    @Override
    public ItemStack func_70301_a(int i) {
        return CraftItemStack.asNMSCopy(inventory.getItem(i));
    }

    @Override
    public ItemStack func_70298_a(int i, int j) {
        // Copied from CraftItemStack
        ItemStack stack = func_70301_a(i);
        ItemStack result;
        if (stack.func_190926_b()) {
            return stack;
        }
        if (stack.func_190916_E() <= j) {
            this.func_70299_a(i, ItemStack.field_190927_a);
            result = stack;
        } else {
            result = CraftItemStack.copyNMSStack(stack, j);
            stack.func_190918_g(j);
        }
        this.func_70296_d();
        return result;
    }

    @Override
    public ItemStack func_70304_b(int i) {
        // Copied from CraftItemStack
        ItemStack stack = func_70301_a(i);
        ItemStack result;
        if (stack.func_190926_b()) {
            return stack;
        }
        if (stack.func_190916_E() <= 1) {
            this.func_70299_a(i, ItemStack.field_190927_a);
            result = stack;
        } else {
            result = CraftItemStack.copyNMSStack(stack, 1);
            stack.func_190918_g(1);
        }
        return result;
    }

    @Override
    public void func_70299_a(int i, ItemStack itemstack) {
        inventory.setItem(i, CraftItemStack.asBukkitCopy(itemstack));
    }

    @Override
    public int func_70297_j_() {
        return inventory.getMaxStackSize();
    }

    @Override
    public void func_70296_d() {
    }

    @Override
    public boolean func_70300_a(EntityPlayer entityhuman) {
        return true;
    }

    @Override
    public void func_174889_b(EntityPlayer entityhuman) {
    }

    @Override
    public void func_174886_c(EntityPlayer entityhuman) {
    }

    @Override
    public boolean func_94041_b(int i, ItemStack itemstack) {
        return true;
    }

    @Override
    public int func_174887_a_(int i) {
        return 0;
    }

    @Override
    public void func_174885_b(int i, int j) {
    }

    @Override
    public int func_174890_g() {
        return 0;
    }

    @Override
    public void func_174888_l() {
        inventory.clear();
    }

    @Override
    public List<ItemStack> getContents() {
        int size = func_70302_i_();
        List<ItemStack> items = new ArrayList<ItemStack>(size);

        for (int i = 0; i < size; i++) {
            items.set(i, func_70301_a(i));
        }

        return items;
    }

    @Override
    public void onOpen(CraftHumanEntity who) {
        viewers.add(who);
    }

    @Override
    public void onClose(CraftHumanEntity who) {
        viewers.remove(who);
    }

    @Override
    public List<HumanEntity> getViewers() {
        return viewers;
    }

    @Override
    public InventoryHolder getOwner() {
        return inventory.getHolder();
    }

    @Override
    public void setMaxStackSize(int size) {
        inventory.setMaxStackSize(size);
    }

    @Override
    public String func_70005_c_() {
        return inventory.getName();
    }

    @Override
    public boolean func_145818_k_() {
        return func_70005_c_() != null;
    }

    @Override
    public ITextComponent func_145748_c_() {
        return CraftChatMessage.fromString(func_70005_c_())[0];
    }

    @Override
    public Location getLocation() {
        return inventory.getLocation();
    }

    @Override
    public boolean func_191420_l() {
        return Iterables.any(inventory, Predicates.notNull());
    }
}
