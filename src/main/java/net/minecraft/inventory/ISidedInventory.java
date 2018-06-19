package net.minecraft.inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;


public interface ISidedInventory extends IInventory {

    int[] func_180463_a(EnumFacing enumdirection);

    boolean func_180462_a(int i, ItemStack itemstack, EnumFacing enumdirection);

    boolean func_180461_b(int i, ItemStack itemstack, EnumFacing enumdirection);
}
