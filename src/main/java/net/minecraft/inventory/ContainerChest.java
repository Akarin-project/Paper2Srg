package net.minecraft.inventory;

// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

// CraftBukkit end

public class ContainerChest extends Container {

    private final IInventory lowerChestInventory;
    private final int numRows;
    // CraftBukkit start
    private CraftInventoryView bukkitEntity = null;
    private InventoryPlayer player;

    @Override
    public CraftInventoryView getBukkitView() {
        if (bukkitEntity != null) {
            return bukkitEntity;
        }

        CraftInventory inventory;
        if (this.lowerChestInventory instanceof InventoryPlayer) {
            inventory = new org.bukkit.craftbukkit.inventory.CraftInventoryPlayer((InventoryPlayer) this.lowerChestInventory);
        } else if (this.lowerChestInventory instanceof InventoryLargeChest) {
            inventory = new org.bukkit.craftbukkit.inventory.CraftInventoryDoubleChest((InventoryLargeChest) this.lowerChestInventory);
        } else {
            inventory = new CraftInventory(this.lowerChestInventory);
        }

        bukkitEntity = new CraftInventoryView(this.player.player.getBukkitEntity(), inventory, this);
        return bukkitEntity;
    }
    // CraftBukkit end

    public ContainerChest(IInventory iinventory, IInventory iinventory1, EntityPlayer entityhuman) {
        this.lowerChestInventory = iinventory1;
        this.numRows = iinventory1.getSizeInventory() / 9;
        iinventory1.openInventory(entityhuman);
        int i = (this.numRows - 4) * 18;

        // CraftBukkit start - Save player
        // TODO: Should we check to make sure it really is an InventoryPlayer?
        this.player = (InventoryPlayer) iinventory;
        // CraftBukkit end

        int j;
        int k;

        for (j = 0; j < this.numRows; ++j) {
            for (k = 0; k < 9; ++k) {
                this.addSlotToContainer(new Slot(iinventory1, k + j * 9, 8 + k * 18, 18 + j * 18));
            }
        }

        for (j = 0; j < 3; ++j) {
            for (k = 0; k < 9; ++k) {
                this.addSlotToContainer(new Slot(iinventory, k + j * 9 + 9, 8 + k * 18, 103 + j * 18 + i));
            }
        }

        for (j = 0; j < 9; ++j) {
            this.addSlotToContainer(new Slot(iinventory, j, 8 + j * 18, 161 + i));
        }

    }

    public boolean canInteractWith(EntityPlayer entityhuman) {
        if (!this.checkReachable) return true; // CraftBukkit
        return this.lowerChestInventory.isUsableByPlayer(entityhuman);
    }

    public ItemStack transferStackInSlot(EntityPlayer entityhuman, int i) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) this.inventorySlots.get(i);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();

            itemstack = itemstack1.copy();
            if (i < this.numRows * 9) {
                if (!this.mergeItemStack(itemstack1, this.numRows * 9, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, this.numRows * 9, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    public void onContainerClosed(EntityPlayer entityhuman) {
        super.onContainerClosed(entityhuman);
        this.lowerChestInventory.closeInventory(entityhuman);
    }

    public IInventory getLowerChestInventory() {
        return this.lowerChestInventory;
    }
}
