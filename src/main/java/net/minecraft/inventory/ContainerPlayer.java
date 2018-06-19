package net.minecraft.inventory;

// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftInventoryCrafting;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

// CraftBukkit end

public class ContainerPlayer extends Container {

    private static final EntityEquipmentSlot[] VALID_EQUIPMENT_SLOTS = new EntityEquipmentSlot[] { EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET};
    public InventoryCrafting craftMatrix = new InventoryCrafting(this, 2, 2);
    public InventoryCraftResult craftResult = new InventoryCraftResult();
    public boolean isLocalWorld;
    private final EntityPlayer player;
    // CraftBukkit start
    private CraftInventoryView bukkitEntity = null;
    private InventoryPlayer player;
    // CraftBukkit end

    public ContainerPlayer(final InventoryPlayer playerinventory, boolean flag, EntityPlayer entityhuman) {
        this.isLocalWorld = flag;
        this.player = entityhuman;
        // CraftBukkit start
        this.craftResult = new InventoryCraftResult(); // CraftBukkit - moved to before InventoryCrafting construction
        this.craftMatrix = new InventoryCrafting(this, 2, 2, playerinventory.player); // CraftBukkit - pass player
        this.craftMatrix.resultInventory = this.craftResult; // CraftBukkit - let InventoryCrafting know about its result slot
        this.player = playerinventory; // CraftBukkit - save player
        // CraftBukkit end
        this.addSlotToContainer((new SlotCrafting(playerinventory.player, this.craftMatrix, this.craftResult, 0, 154, 28)));

        int i;
        int j;

        for (i = 0; i < 2; ++i) {
            for (j = 0; j < 2; ++j) {
                this.addSlotToContainer(new Slot(this.craftMatrix, j + i * 2, 98 + j * 18, 18 + i * 18));
            }
        }

        for (i = 0; i < 4; ++i) {
            final EntityEquipmentSlot enumitemslot1 = ContainerPlayer.VALID_EQUIPMENT_SLOTS[i];

            this.addSlotToContainer(new Slot(playerinventory, 36 + (3 - i), 8, 8 + i * 18) {
                @Override
                public int getSlotStackLimit() {
                    return 1;
                }

                @Override
                public boolean isItemValid(ItemStack itemstack) {
                    return enumitemslot1 == EntityLiving.getSlotForItemStack(itemstack); // CraftBukkit - decompile error
                }

                @Override
                public boolean canTakeStack(EntityPlayer entityhuman) {
                    ItemStack itemstack = this.getStack();

                    return !itemstack.isEmpty() && !entityhuman.isCreative() && EnchantmentHelper.hasBindingCurse(itemstack) ? false : super.canTakeStack(entityhuman);
                }
            });
        }

        for (i = 0; i < 3; ++i) {
            for (j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(playerinventory, j + (i + 1) * 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(playerinventory, i, 8 + i * 18, 142));
        }

        this.addSlotToContainer(new Slot(playerinventory, 40, 77, 62) {
        });
    }

    @Override
    public void onCraftMatrixChanged(IInventory iinventory) {
        this.slotChangedCraftingGrid(this.player.world, this.player, this.craftMatrix, this.craftResult);
    }

    @Override
    public void onContainerClosed(EntityPlayer entityhuman) {
        super.onContainerClosed(entityhuman);
        this.craftResult.clear();
        if (!entityhuman.world.isRemote) {
            this.clearContainer(entityhuman, entityhuman.world, this.craftMatrix);
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityhuman) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer entityhuman, int i) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(i);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();

            itemstack = itemstack1.copy();
            EntityEquipmentSlot enumitemslot = EntityLiving.getSlotForItemStack(itemstack);

            if (i == 0) {
                if (!this.mergeItemStack(itemstack1, 9, 45, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (i >= 1 && i < 5) {
                if (!this.mergeItemStack(itemstack1, 9, 45, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (i >= 5 && i < 9) {
                if (!this.mergeItemStack(itemstack1, 9, 45, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (enumitemslot.getSlotType() == EntityEquipmentSlot.Type.ARMOR && !this.inventorySlots.get(8 - enumitemslot.getIndex()).getHasStack()) {
                int j = 8 - enumitemslot.getIndex();

                if (!this.mergeItemStack(itemstack1, j, j + 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (enumitemslot == EntityEquipmentSlot.OFFHAND && !this.inventorySlots.get(45).getHasStack()) {
                if (!this.mergeItemStack(itemstack1, 45, 46, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (i >= 9 && i < 36) {
                if (!this.mergeItemStack(itemstack1, 36, 45, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (i >= 36 && i < 45) {
                if (!this.mergeItemStack(itemstack1, 9, 36, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 9, 45, false)) {
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

            ItemStack itemstack2 = slot.onTake(entityhuman, itemstack1);

            if (i == 0) {
                entityhuman.dropItem(itemstack2, false);
            }
        }

        return itemstack;
    }

    @Override
    public boolean canMergeSlot(ItemStack itemstack, Slot slot) {
        return slot.inventory != this.craftResult && super.canMergeSlot(itemstack, slot);
    }

    // CraftBukkit start
    @Override
    public CraftInventoryView getBukkitView() {
        if (bukkitEntity != null) {
            return bukkitEntity;
        }

        CraftInventoryCrafting inventory = new CraftInventoryCrafting(this.craftMatrix, this.craftResult);
        bukkitEntity = new CraftInventoryView(this.player.player.getBukkitEntity(), inventory, this);
        return bukkitEntity;
    }
    // CraftBukkit end
}
