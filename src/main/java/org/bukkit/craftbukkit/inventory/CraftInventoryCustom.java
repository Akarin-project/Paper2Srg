package org.bukkit.craftbukkit.inventory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.util.text.TextComponentString;

import net.minecraft.util.text.ITextComponent;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryHolder;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class CraftInventoryCustom extends CraftInventory {
    public CraftInventoryCustom(InventoryHolder owner, InventoryType type) {
        super(new MinecraftInventory(owner, type));
    }

    public CraftInventoryCustom(InventoryHolder owner, InventoryType type, String title) {
        super(new MinecraftInventory(owner, type, title));
    }

    public CraftInventoryCustom(InventoryHolder owner, int size) {
        super(new MinecraftInventory(owner, size));
    }

    public CraftInventoryCustom(InventoryHolder owner, int size, String title) {
        super(new MinecraftInventory(owner, size, title));
    }

    static class MinecraftInventory implements IInventory {
        private final NonNullList<ItemStack> items;
        private int maxStack = MAX_STACK;
        private final List<HumanEntity> viewers;
        private final String title;
        private InventoryType type;
        private final InventoryHolder owner;

        public MinecraftInventory(InventoryHolder owner, InventoryType type) {
            this(owner, type.getDefaultSize(), type.getDefaultTitle());
            this.type = type;
        }

        public MinecraftInventory(InventoryHolder owner, InventoryType type, String title) {
            this(owner, type.getDefaultSize(), title);
            this.type = type;
        }

        public MinecraftInventory(InventoryHolder owner, int size) {
            this(owner, size, "Chest");
        }

        public MinecraftInventory(InventoryHolder owner, int size, String title) {
            Validate.notNull(title, "Title cannot be null");
            this.items = NonNullList.func_191197_a(size, ItemStack.field_190927_a);
            this.title = title;
            this.viewers = new ArrayList<HumanEntity>();
            this.owner = owner;
            this.type = InventoryType.CHEST;
        }

        public int func_70302_i_() {
            return items.size();
        }

        public ItemStack func_70301_a(int i) {
            return items.get(i);
        }

        public ItemStack func_70298_a(int i, int j) {
            ItemStack stack = this.func_70301_a(i);
            ItemStack result;
            if (stack == ItemStack.field_190927_a) return stack;
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

        public ItemStack func_70304_b(int i) {
            ItemStack stack = this.func_70301_a(i);
            ItemStack result;
            if (stack == ItemStack.field_190927_a) return stack;
            if (stack.func_190916_E() <= 1) {
                this.func_70299_a(i, null);
                result = stack;
            } else {
                result = CraftItemStack.copyNMSStack(stack, 1);
                stack.func_190918_g(1);
            }
            return result;
        }

        public void func_70299_a(int i, ItemStack itemstack) {
            items.set(i, itemstack);
            if (itemstack != ItemStack.field_190927_a && this.func_70297_j_() > 0 && itemstack.func_190916_E() > this.func_70297_j_()) {
                itemstack.func_190920_e(this.func_70297_j_());
            }
        }

        public int func_70297_j_() {
            return maxStack;
        }

        public void setMaxStackSize(int size) {
            maxStack = size;
        }

        public void func_70296_d() {}

        public boolean func_70300_a(EntityPlayer entityhuman) {
            return true;
        }

        public List<ItemStack> getContents() {
            return items;
        }

        public void onOpen(CraftHumanEntity who) {
            viewers.add(who);
        }

        public void onClose(CraftHumanEntity who) {
            viewers.remove(who);
        }

        public List<HumanEntity> getViewers() {
            return viewers;
        }

        public InventoryType getType() {
            return type;
        }

        public InventoryHolder getOwner() {
            return owner;
        }

        public boolean func_94041_b(int i, ItemStack itemstack) {
            return true;
        }

        @Override
        public void func_174889_b(EntityPlayer entityHuman) {

        }

        @Override
        public void func_174886_c(EntityPlayer entityHuman) {

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
            items.clear();
        }

        @Override
        public String func_70005_c_() {
            return title;
        }

        @Override
        public boolean func_145818_k_() {
            return title != null;
        }

        @Override
        public ITextComponent func_145748_c_() {
            return new TextComponentString(title);
        }

        @Override
        public Location getLocation() {
            return null;
        }

        @Override
        public boolean func_191420_l() {
            Iterator iterator = this.items.iterator();

            ItemStack itemstack;

            do {
                if (!iterator.hasNext()) {
                    return true;
                }

                itemstack = (ItemStack) iterator.next();
            } while (itemstack.func_190926_b());

            return false;
        }
    }
}
