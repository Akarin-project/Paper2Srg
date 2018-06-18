package net.minecraft.inventory;

import org.bukkit.Location;
import org.bukkit.inventory.InventoryHolder;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityEnderChest;

public class InventoryEnderChest extends InventoryBasic {

    private TileEntityEnderChest associatedChest; public TileEntityEnderChest getTileEntity() { return associatedChest; } // Paper - OBFHELPER
    // CraftBukkit start
    private final EntityPlayer owner;

    public InventoryHolder getBukkitOwner() {
        return owner.getBukkitEntity();
    }

    @Override
    public Location getLocation() {
        if (getTileEntity() == null) return null; // Paper - return null if there is no TE bound (opened by plugin)
        return new Location(this.associatedChest.getWorld().getWorld(), this.associatedChest.getPos().getX(), this.associatedChest.getPos().getY(), this.associatedChest.getPos().getZ());
    }

    public InventoryEnderChest(EntityPlayer owner) {
        super("container.enderchest", false, 27);
        this.owner = owner;
        // CraftBukkit end
    }

    public void setChestTileEntity(TileEntityEnderChest tileentityenderchest) {
        this.associatedChest = tileentityenderchest;
    }

    public void loadInventoryFromNBT(NBTTagList nbttaglist) {
        int i;

        for (i = 0; i < this.getSizeInventory(); ++i) {
            this.setInventorySlotContents(i, ItemStack.EMPTY);
        }

        for (i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound.getByte("Slot") & 255;

            if (j >= 0 && j < this.getSizeInventory()) {
                this.setInventorySlotContents(j, new ItemStack(nbttagcompound));
            }
        }

    }

    public NBTTagList saveInventoryToNBT() {
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.getSizeInventory(); ++i) {
            ItemStack itemstack = this.getStackInSlot(i);

            if (!itemstack.isEmpty()) {
                NBTTagCompound nbttagcompound = new NBTTagCompound();

                nbttagcompound.setByte("Slot", (byte) i);
                itemstack.writeToNBT(nbttagcompound);
                nbttaglist.appendTag(nbttagcompound);
            }
        }

        return nbttaglist;
    }

    public boolean isUsableByPlayer(EntityPlayer entityhuman) {
        return this.associatedChest != null && !this.associatedChest.canBeUsed(entityhuman) ? false : super.isUsableByPlayer(entityhuman);
    }

    public void openInventory(EntityPlayer entityhuman) {
        if (this.associatedChest != null) {
            this.associatedChest.openChest();
        }

        super.openInventory(entityhuman);
    }

    public void closeInventory(EntityPlayer entityhuman) {
        if (this.associatedChest != null) {
            this.associatedChest.closeChest();
        }

        super.closeInventory(entityhuman);
        this.associatedChest = null;
    }
}
