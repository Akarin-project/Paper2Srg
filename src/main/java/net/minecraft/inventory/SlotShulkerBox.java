package net.minecraft.inventory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.item.ItemStack;


public class SlotShulkerBox extends Slot {

    public SlotShulkerBox(IInventory iinventory, int i, int j, int k) {
        super(iinventory, i, j, k);
    }

    public boolean isItemValid(ItemStack itemstack) {
        return !(Block.getBlockFromItem(itemstack.getItem()) instanceof BlockShulkerBox);
    }
}
