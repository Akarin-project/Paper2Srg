package net.minecraft.inventory;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityBeacon;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;

public class ContainerBeacon extends Container {

    private final IInventory tileBeacon;
    private final ContainerBeacon.BeaconSlot beaconSlot;
    // CraftBukkit start
    private CraftInventoryView bukkitEntity = null;
    private InventoryPlayer player;
    // CraftBukkit end

    public ContainerBeacon(IInventory iinventory, IInventory iinventory1) {
        player = (InventoryPlayer) iinventory; // CraftBukkit - TODO: check this
        this.tileBeacon = iinventory1;
        this.beaconSlot = new ContainerBeacon.BeaconSlot(iinventory1, 0, 136, 110);
        this.addSlotToContainer((Slot) this.beaconSlot);
        boolean flag = true;
        boolean flag1 = true;

        int i;

        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(iinventory, j + i * 9 + 9, 36 + j * 18, 137 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(iinventory, i, 36 + i * 18, 195));
        }

    }

    public void addListener(IContainerListener icrafting) {
        super.addListener(icrafting);
        icrafting.sendAllWindowProperties(this, this.tileBeacon);
    }

    public IInventory getTileEntity() {
        return this.tileBeacon;
    }

    public void onContainerClosed(EntityPlayer entityhuman) {
        super.onContainerClosed(entityhuman);
        if (!entityhuman.world.isRemote) {
            ItemStack itemstack = this.beaconSlot.decrStackSize(this.beaconSlot.getSlotStackLimit());

            if (!itemstack.isEmpty()) {
                entityhuman.dropItem(itemstack, false);
            }

        }
    }

    public boolean canInteractWith(EntityPlayer entityhuman) {
        if (!this.checkReachable) return true; // CraftBukkit
        return this.tileBeacon.isUsableByPlayer(entityhuman);
    }

    public ItemStack transferStackInSlot(EntityPlayer entityhuman, int i) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) this.inventorySlots.get(i);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();

            itemstack = itemstack1.copy();
            if (i == 0) {
                if (!this.mergeItemStack(itemstack1, 1, 37, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (!this.beaconSlot.getHasStack() && this.beaconSlot.isItemValid(itemstack1) && itemstack1.getCount() == 1) {
                if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (i >= 1 && i < 28) {
                if (!this.mergeItemStack(itemstack1, 28, 37, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (i >= 28 && i < 37) {
                if (!this.mergeItemStack(itemstack1, 1, 28, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 1, 37, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(entityhuman, itemstack1);
        }

        return itemstack;
    }

    class BeaconSlot extends Slot {

        public BeaconSlot(IInventory iinventory, int i, int j, int k) {
            super(iinventory, i, j, k);
        }

        public boolean isItemValid(ItemStack itemstack) {
            Item item = itemstack.getItem();

            return item == Items.EMERALD || item == Items.DIAMOND || item == Items.GOLD_INGOT || item == Items.IRON_INGOT;
        }

        public int getSlotStackLimit() {
            return 1;
        }
    }

    // CraftBukkit start
    @Override
    public CraftInventoryView getBukkitView() {
        if (bukkitEntity != null) {
            return bukkitEntity;
        }

        org.bukkit.craftbukkit.inventory.CraftInventory inventory = new org.bukkit.craftbukkit.inventory.CraftInventoryBeacon((TileEntityBeacon) this.tileBeacon); // TODO - check this
        bukkitEntity = new CraftInventoryView(this.player.player.getBukkitEntity(), inventory, this);
        return bukkitEntity;
    }
    // CraftBukkit end
}
