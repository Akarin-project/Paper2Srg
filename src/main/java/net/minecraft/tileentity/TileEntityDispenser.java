package net.minecraft.tileentity;

import java.util.Iterator;
import java.util.Random;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerDispenser;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;

// CraftBukkit start
import java.util.List;

import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
// CraftBukkit end

public class TileEntityDispenser extends TileEntityLockableLoot {

    private static final Random RNG = new Random();
    private NonNullList<ItemStack> stacks;

    // CraftBukkit start - add fields and methods
    public List<HumanEntity> transaction = new java.util.ArrayList<HumanEntity>();
    private int maxStack = MAX_STACK;

    public List<ItemStack> getContents() {
        return this.stacks;
    }

    public void onOpen(CraftHumanEntity who) {
        transaction.add(who);
    }

    public void onClose(CraftHumanEntity who) {
        transaction.remove(who);
    }

    public List<HumanEntity> getViewers() {
        return transaction;
    }

    public void setMaxStackSize(int size) {
        maxStack = size;
    }
    // CraftBukkit end

    public TileEntityDispenser() {
        this.stacks = NonNullList.withSize(9, ItemStack.EMPTY);
    }

    public int getSizeInventory() {
        return 9;
    }

    public boolean isEmpty() {
        Iterator iterator = this.stacks.iterator();

        ItemStack itemstack;

        do {
            if (!iterator.hasNext()) {
                return true;
            }

            itemstack = (ItemStack) iterator.next();
        } while (itemstack.isEmpty());

        return false;
    }

    public int getDispenseSlot() {
        this.fillWithLoot((EntityPlayer) null);
        int i = -1;
        int j = 1;

        for (int k = 0; k < this.stacks.size(); ++k) {
            if (!((ItemStack) this.stacks.get(k)).isEmpty() && TileEntityDispenser.RNG.nextInt(j++) == 0) {
                i = k;
            }
        }

        return i;
    }

    public int addItemStack(ItemStack itemstack) {
        for (int i = 0; i < this.stacks.size(); ++i) {
            if (((ItemStack) this.stacks.get(i)).isEmpty()) {
                this.setInventorySlotContents(i, itemstack);
                return i;
            }
        }

        return -1;
    }

    public String getName() {
        return this.hasCustomName() ? this.customName : "container.dispenser";
    }

    public static void registerFixes(DataFixer dataconvertermanager) {
        dataconvertermanager.registerWalker(FixTypes.BLOCK_ENTITY, (IDataWalker) (new ItemStackDataLists(TileEntityDispenser.class, new String[] { "Items"})));
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.stacks = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        if (!this.checkLootAndRead(nbttagcompound)) {
            ItemStackHelper.loadAllItems(nbttagcompound, this.stacks);
        }

        if (nbttagcompound.hasKey("CustomName", 8)) {
            this.customName = nbttagcompound.getString("CustomName");
        }

    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        if (!this.checkLootAndWrite(nbttagcompound)) {
            ItemStackHelper.saveAllItems(nbttagcompound, this.stacks);
        }

        if (this.hasCustomName()) {
            nbttagcompound.setString("CustomName", this.customName);
        }

        return nbttagcompound;
    }

    public int getInventoryStackLimit() {
        return maxStack; // CraftBukkit
    }

    public String getGuiID() {
        return "minecraft:dispenser";
    }

    public Container createContainer(InventoryPlayer playerinventory, EntityPlayer entityhuman) {
        this.fillWithLoot(entityhuman);
        return new ContainerDispenser(playerinventory, this);
    }

    protected NonNullList<ItemStack> getItems() {
        return this.stacks;
    }
}
