package net.minecraft.inventory;

// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

// CraftBukkit end

public class ContainerShulkerBox extends Container {

    private final IInventory inventory;
    // CraftBukkit start
    private CraftInventoryView bukkitEntity;
    private InventoryPlayer player;

    @Override
    public CraftInventoryView getBukkitView() {
        if (bukkitEntity != null) {
            return bukkitEntity;
        }

        bukkitEntity = new CraftInventoryView(this.player.player.getBukkitEntity(), new CraftInventory(this.inventory), this);
        return bukkitEntity;
    }
    // CraftBukkit end

    public ContainerShulkerBox(InventoryPlayer playerinventory, IInventory iinventory, EntityPlayer entityhuman) {
        this.inventory = iinventory;
        this.player = playerinventory; // CraftBukkit - save player
        iinventory.openInventory(entityhuman);
        boolean flag = true;
        boolean flag1 = true;

        int i;
        int j;

        for (i = 0; i < 3; ++i) {
            for (j = 0; j < 9; ++j) {
                this.addSlotToContainer((Slot) (new SlotShulkerBox(iinventory, j + i * 9, 8 + j * 18, 18 + i * 18)));
            }
        }

        for (i = 0; i < 3; ++i) {
            for (j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(playerinventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(playerinventory, i, 8 + i * 18, 142));
        }

    }

    public boolean canInteractWith(EntityPlayer entityhuman) {
        return this.inventory.isUsableByPlayer(entityhuman);
    }

    public ItemStack transferStackInSlot(EntityPlayer entityhuman, int i) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) this.inventorySlots.get(i);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();

            itemstack = itemstack1.copy();
            if (i < this.inventory.getSizeInventory()) {
                if (!this.mergeItemStack(itemstack1, this.inventory.getSizeInventory(), this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, this.inventory.getSizeInventory(), false)) {
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
        this.inventory.closeInventory(entityhuman);
    }
}
