package net.minecraft.inventory;

// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.bukkit.inventory.InventoryView;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractChestHorse;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

// CraftBukkit end

public class ContainerHorseInventory extends Container {

    private final IInventory horseInventory;
    private final AbstractHorse horse;

    // CraftBukkit start
    org.bukkit.craftbukkit.inventory.CraftInventoryView bukkitEntity;
    InventoryPlayer player;

    @Override
    public InventoryView getBukkitView() {
        if (bukkitEntity != null) {
            return bukkitEntity;
        }

        return bukkitEntity = new CraftInventoryView(player.player.getBukkitEntity(), horseInventory.getOwner().getInventory(), this);
    }

    public ContainerHorseInventory(IInventory iinventory, final IInventory iinventory1, final AbstractHorse entityhorseabstract, EntityPlayer entityhuman) {
        player = (InventoryPlayer) iinventory;
        // CraftBukkit end
        this.horseInventory = iinventory1;
        this.horse = entityhorseabstract;
        boolean flag = true;

        iinventory1.openInventory(entityhuman);
        boolean flag1 = true;

        this.addSlotToContainer(new Slot(iinventory1, 0, 8, 18) {
            public boolean isItemValid(ItemStack itemstack) {
                return itemstack.getItem() == Items.SADDLE && !this.getHasStack() && entityhorseabstract.canBeSaddled();
            }
        });
        this.addSlotToContainer(new Slot(iinventory1, 1, 8, 36) {
            public boolean isItemValid(ItemStack itemstack) {
                return entityhorseabstract.isArmor(itemstack);
            }

            public int getSlotStackLimit() {
                return 1;
            }
        });
        int i;
        int j;

        if (entityhorseabstract instanceof AbstractChestHorse && ((AbstractChestHorse) entityhorseabstract).hasChest()) {
            for (i = 0; i < 3; ++i) {
                for (j = 0; j < ((AbstractChestHorse) entityhorseabstract).getInventoryColumns(); ++j) {
                    this.addSlotToContainer(new Slot(iinventory1, 2 + j + i * ((AbstractChestHorse) entityhorseabstract).getInventoryColumns(), 80 + j * 18, 18 + i * 18));
                }
            }
        }

        for (i = 0; i < 3; ++i) {
            for (j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(iinventory, j + i * 9 + 9, 8 + j * 18, 102 + i * 18 + -18));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(iinventory, i, 8 + i * 18, 142));
        }

    }

    public boolean canInteractWith(EntityPlayer entityhuman) {
        return this.horseInventory.isUsableByPlayer(entityhuman) && this.horse.isEntityAlive() && this.horse.getDistance((Entity) entityhuman) < 8.0F;
    }

    public ItemStack transferStackInSlot(EntityPlayer entityhuman, int i) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) this.inventorySlots.get(i);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();

            itemstack = itemstack1.copy();
            if (i < this.horseInventory.getSizeInventory()) {
                if (!this.mergeItemStack(itemstack1, this.horseInventory.getSizeInventory(), this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(1).isItemValid(itemstack1) && !this.getSlot(1).getHasStack()) {
                if (!this.mergeItemStack(itemstack1, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(0).isItemValid(itemstack1)) {
                if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.horseInventory.getSizeInventory() <= 2 || !this.mergeItemStack(itemstack1, 2, this.horseInventory.getSizeInventory(), false)) {
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
        this.horseInventory.closeInventory(entityhuman);
    }
}
