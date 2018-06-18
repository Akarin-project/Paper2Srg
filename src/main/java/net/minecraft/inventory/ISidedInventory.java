package net.minecraft.inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;


public interface ISidedInventory extends IInventory {

    int[] getSlotsForFace(EnumFacing enumdirection);

    boolean canInsertItem(int i, ItemStack itemstack, EnumFacing enumdirection);

    boolean canExtractItem(int i, ItemStack itemstack, EnumFacing enumdirection);
}
