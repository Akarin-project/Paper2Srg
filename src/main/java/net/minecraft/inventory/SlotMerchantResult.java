package net.minecraft.inventory;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.village.MerchantRecipe;


public class SlotMerchantResult extends Slot {

    private final InventoryMerchant merchantInventory;
    private final EntityPlayer player;
    private int removeCount;
    private final IMerchant merchant;

    public SlotMerchantResult(EntityPlayer entityhuman, IMerchant imerchant, InventoryMerchant inventorymerchant, int i, int j, int k) {
        super(inventorymerchant, i, j, k);
        this.player = entityhuman;
        this.merchant = imerchant;
        this.merchantInventory = inventorymerchant;
    }

    public boolean isItemValid(ItemStack itemstack) {
        return false;
    }

    public ItemStack decrStackSize(int i) {
        if (this.getHasStack()) {
            this.removeCount += Math.min(i, this.getStack().getCount());
        }

        return super.decrStackSize(i);
    }

    protected void onCrafting(ItemStack itemstack, int i) {
        this.removeCount += i;
        this.onCrafting(itemstack);
    }

    protected void onCrafting(ItemStack itemstack) {
        itemstack.onCrafting(this.player.world, this.player, this.removeCount);
        this.removeCount = 0;
    }

    public ItemStack onTake(EntityPlayer entityhuman, ItemStack itemstack) {
        this.onCrafting(itemstack);
        MerchantRecipe merchantrecipe = this.merchantInventory.getCurrentRecipe();

        if (merchantrecipe != null) {
            ItemStack itemstack1 = this.merchantInventory.getStackInSlot(0);
            ItemStack itemstack2 = this.merchantInventory.getStackInSlot(1);

            if (this.doTrade(merchantrecipe, itemstack1, itemstack2) || this.doTrade(merchantrecipe, itemstack2, itemstack1)) {
                this.merchant.useRecipe(merchantrecipe);
                entityhuman.addStat(StatList.TRADED_WITH_VILLAGER);
                this.merchantInventory.setInventorySlotContents(0, itemstack1);
                this.merchantInventory.setInventorySlotContents(1, itemstack2);
            }
        }

        return itemstack;
    }

    private boolean doTrade(MerchantRecipe merchantrecipe, ItemStack itemstack, ItemStack itemstack1) {
        ItemStack itemstack2 = merchantrecipe.getItemToBuy();
        ItemStack itemstack3 = merchantrecipe.getSecondItemToBuy();

        if (itemstack.getItem() == itemstack2.getItem() && itemstack.getCount() >= itemstack2.getCount()) {
            if (!itemstack3.isEmpty() && !itemstack1.isEmpty() && itemstack3.getItem() == itemstack1.getItem() && itemstack1.getCount() >= itemstack3.getCount()) {
                itemstack.shrink(itemstack2.getCount());
                itemstack1.shrink(itemstack3.getCount());
                return true;
            }

            if (itemstack3.isEmpty() && itemstack1.isEmpty()) {
                itemstack.shrink(itemstack2.getCount());
                return true;
            }
        }

        return false;
    }
}
