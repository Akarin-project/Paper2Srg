package net.minecraft.inventory;

// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftInventoryCrafting;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

// CraftBukkit end

public class ContainerWorkbench extends Container {

    public InventoryCrafting craftMatrix; // CraftBukkit - move initialization into constructor
    public InventoryCraftResult craftResult; // CraftBukkit - move initialization into constructor
    private final World world;
    private final BlockPos pos;
    private final EntityPlayer player;
    // CraftBukkit start
    private CraftInventoryView bukkitEntity = null;
    private InventoryPlayer player;
    // CraftBukkit end

    public ContainerWorkbench(InventoryPlayer playerinventory, World world, BlockPos blockposition) {
        // CraftBukkit start - Switched order of IInventory construction and stored player
        this.craftResult = new InventoryCraftResult();
        this.craftMatrix = new InventoryCrafting(this, 3, 3, playerinventory.player); // CraftBukkit - pass player
        this.craftMatrix.resultInventory = this.craftResult;
        this.player = playerinventory;
        // CraftBukkit end
        this.world = world;
        this.pos = blockposition;
        this.player = playerinventory.player;
        this.addSlotToContainer((Slot) (new SlotCrafting(playerinventory.player, this.craftMatrix, this.craftResult, 0, 124, 35)));

        int i;
        int j;

        for (i = 0; i < 3; ++i) {
            for (j = 0; j < 3; ++j) {
                this.addSlotToContainer(new Slot(this.craftMatrix, j + i * 3, 30 + j * 18, 17 + i * 18));
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

    public void onCraftMatrixChanged(IInventory iinventory) {
        this.slotChangedCraftingGrid(this.world, this.player, this.craftMatrix, this.craftResult);
    }

    public void onContainerClosed(EntityPlayer entityhuman) {
        super.onContainerClosed(entityhuman);
        if (!this.world.isRemote) {
            this.clearContainer(entityhuman, this.world, this.craftMatrix);
        }
    }

    public boolean canInteractWith(EntityPlayer entityhuman) {
        if (!this.checkReachable) return true; // CraftBukkit
        return this.world.getBlockState(this.pos).getBlock() != Blocks.CRAFTING_TABLE ? false : entityhuman.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
    }

    public ItemStack transferStackInSlot(EntityPlayer entityhuman, int i) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) this.inventorySlots.get(i);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();

            itemstack = itemstack1.copy();
            if (i == 0) {
                itemstack1.getItem().onCreated(itemstack1, this.world, entityhuman);
                if (!this.mergeItemStack(itemstack1, 10, 46, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (i >= 10 && i < 37) {
                if (!this.mergeItemStack(itemstack1, 37, 46, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (i >= 37 && i < 46) {
                if (!this.mergeItemStack(itemstack1, 10, 37, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 10, 46, false)) {
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
