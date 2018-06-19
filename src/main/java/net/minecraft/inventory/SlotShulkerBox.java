package net.minecraft.inventory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.item.ItemStack;


public class SlotShulkerBox extends Slot {

    public SlotShulkerBox(IInventory iinventory, int i, int j, int k) {
        super(iinventory, i, j, k);
    }

    public boolean func_75214_a(ItemStack itemstack) {
        return !(Block.func_149634_a(itemstack.func_77973_b()) instanceof BlockShulkerBox);
    }
}
