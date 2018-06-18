package net.minecraft.inventory;

import com.google.common.collect.Lists;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;

public class SlotCrafting extends Slot {

    private final InventoryCrafting craftMatrix;
    private final EntityPlayer player;
    private int amountCrafted;

    public SlotCrafting(EntityPlayer entityhuman, InventoryCrafting inventorycrafting, IInventory iinventory, int i, int j, int k) {
        super(iinventory, i, j, k);
        this.player = entityhuman;
        this.craftMatrix = inventorycrafting;
    }

    public boolean isItemValid(ItemStack itemstack) {
        return false;
    }

    public ItemStack decrStackSize(int i) {
        if (this.getHasStack()) {
            this.amountCrafted += Math.min(i, this.getStack().getCount());
        }

        return super.decrStackSize(i);
    }

    protected void onCrafting(ItemStack itemstack, int i) {
        this.amountCrafted += i;
        this.onCrafting(itemstack);
    }

    protected void onSwapCraft(int i) {
        this.amountCrafted += i;
    }

    protected void onCrafting(ItemStack itemstack) {
        if (this.amountCrafted > 0) {
            itemstack.onCrafting(this.player.world, this.player, this.amountCrafted);
        }

        this.amountCrafted = 0;
        InventoryCraftResult inventorycraftresult = (InventoryCraftResult) this.inventory;
        IRecipe irecipe = inventorycraftresult.getRecipeUsed();

        if (irecipe != null && !irecipe.isDynamic()) {
            this.player.unlockRecipes((List) Lists.newArrayList(new IRecipe[] { irecipe}));
            inventorycraftresult.setRecipeUsed((IRecipe) null);
        }

    }

    public ItemStack onTake(EntityPlayer entityhuman, ItemStack itemstack) {
        this.onCrafting(itemstack);
        NonNullList nonnulllist = CraftingManager.getRemainingItems(this.craftMatrix, entityhuman.world);

        for (int i = 0; i < nonnulllist.size(); ++i) {
            ItemStack itemstack1 = this.craftMatrix.getStackInSlot(i);
            ItemStack itemstack2 = (ItemStack) nonnulllist.get(i);

            if (!itemstack1.isEmpty()) {
                this.craftMatrix.decrStackSize(i, 1);
                itemstack1 = this.craftMatrix.getStackInSlot(i);
            }

            if (!itemstack2.isEmpty()) {
                if (itemstack1.isEmpty()) {
                    this.craftMatrix.setInventorySlotContents(i, itemstack2);
                } else if (ItemStack.areItemsEqual(itemstack1, itemstack2) && ItemStack.areItemStackTagsEqual(itemstack1, itemstack2)) {
                    itemstack2.grow(itemstack1.getCount());
                    this.craftMatrix.setInventorySlotContents(i, itemstack2);
                } else if (!this.player.inventory.addItemStackToInventory(itemstack2)) {
                    this.player.dropItem(itemstack2, false);
                }
            }
        }

        return itemstack;
    }
}
