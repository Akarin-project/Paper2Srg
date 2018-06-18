package net.minecraft.inventory;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IWorldNameable;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;

public interface IInventory extends IWorldNameable {

    int getSizeInventory();

    boolean isEmpty();

    ItemStack getStackInSlot(int i);

    ItemStack decrStackSize(int i, int j);

    ItemStack removeStackFromSlot(int i);

    void setInventorySlotContents(int i, ItemStack itemstack);

    int getInventoryStackLimit();

    void markDirty();

    boolean isUsableByPlayer(EntityPlayer entityhuman);

    void openInventory(EntityPlayer entityhuman);

    void closeInventory(EntityPlayer entityhuman);

    boolean isItemValidForSlot(int i, ItemStack itemstack);

    int getField(int i);

    void setField(int i, int j);

    int getFieldCount();

    void clear();

    // CraftBukkit start
    java.util.List<ItemStack> getContents();

    void onOpen(CraftHumanEntity who);

    void onClose(CraftHumanEntity who);

    java.util.List<org.bukkit.entity.HumanEntity> getViewers();

    org.bukkit.inventory.InventoryHolder getOwner();

    void setMaxStackSize(int size);

    org.bukkit.Location getLocation();

    int MAX_STACK = 64;
    // CraftBukkit end
}
