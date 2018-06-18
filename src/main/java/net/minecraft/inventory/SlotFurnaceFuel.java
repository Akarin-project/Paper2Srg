package net.minecraft.inventory;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;


public class SlotFurnaceFuel extends Slot {

    public SlotFurnaceFuel(IInventory iinventory, int i, int j, int k) {
        super(iinventory, i, j, k);
    }

    public boolean isItemValid(ItemStack itemstack) {
        return TileEntityFurnace.isItemFuel(itemstack) || isBucket(itemstack);
    }

    public int getItemStackLimit(ItemStack itemstack) {
        return isBucket(itemstack) ? 1 : super.getItemStackLimit(itemstack);
    }

    public static boolean isBucket(ItemStack itemstack) {
        return itemstack.getItem() == Items.BUCKET;
    }
}
