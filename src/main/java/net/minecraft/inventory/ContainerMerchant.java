package net.minecraft.inventory;


import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;

public class ContainerMerchant extends Container {

    private final IMerchant merchant;
    private final InventoryMerchant merchantInventory;
    private final World world;

    // CraftBukkit start
    private CraftInventoryView bukkitEntity = null;
    private InventoryPlayer player;

    @Override
    public CraftInventoryView getBukkitView() {
        if (bukkitEntity == null) {
            bukkitEntity = new CraftInventoryView(this.player.player.getBukkitEntity(), new org.bukkit.craftbukkit.inventory.CraftInventoryMerchant((InventoryMerchant) merchantInventory), this);
        }
        return bukkitEntity;
    }
    // CraftBukkit end

    public ContainerMerchant(InventoryPlayer playerinventory, IMerchant imerchant, World world) {
        this.merchant = imerchant;
        this.world = world;
        this.merchantInventory = new InventoryMerchant(playerinventory.player, imerchant);
        this.addSlotToContainer(new Slot(this.merchantInventory, 0, 36, 53));
        this.addSlotToContainer(new Slot(this.merchantInventory, 1, 62, 53));
        this.addSlotToContainer((Slot) (new SlotMerchantResult(playerinventory.player, imerchant, this.merchantInventory, 2, 120, 53)));
        this.player = playerinventory; // CraftBukkit - save player

        int i;

        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(playerinventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(playerinventory, i, 8 + i * 18, 142));
        }

    }

    public InventoryMerchant getMerchantInventory() {
        return this.merchantInventory;
    }

    public void onCraftMatrixChanged(IInventory iinventory) {
        this.merchantInventory.resetRecipeAndSlots();
        super.onCraftMatrixChanged(iinventory);
    }

    public void setCurrentRecipeIndex(int i) {
        this.merchantInventory.setCurrentRecipeIndex(i);
    }

    public boolean canInteractWith(EntityPlayer entityhuman) {
        return this.merchant.getCustomer() == entityhuman;
    }

    public ItemStack transferStackInSlot(EntityPlayer entityhuman, int i) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) this.inventorySlots.get(i);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();

            itemstack = itemstack1.copy();
            if (i == 2) {
                if (!this.mergeItemStack(itemstack1, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (i != 0 && i != 1) {
                if (i >= 3 && i < 30) {
                    if (!this.mergeItemStack(itemstack1, 30, 39, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (i >= 30 && i < 39 && !this.mergeItemStack(itemstack1, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 3, 39, false)) {
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

    public void onContainerClosed(EntityPlayer entityhuman) {
        super.onContainerClosed(entityhuman);
        this.merchant.setCustomer((EntityPlayer) null);
        super.onContainerClosed(entityhuman);
        if (!this.world.isRemote) {
            ItemStack itemstack = this.merchantInventory.removeStackFromSlot(0);

            if (!itemstack.isEmpty()) {
                entityhuman.dropItem(itemstack, false);
            }

            itemstack = this.merchantInventory.removeStackFromSlot(1);
            if (!itemstack.isEmpty()) {
                entityhuman.dropItem(itemstack, false);
            }

        }
    }
}
