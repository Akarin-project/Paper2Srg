package net.minecraft.inventory;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;


public class SlotFurnaceFuel extends Slot {

    public SlotFurnaceFuel(IInventory iinventory, int i, int j, int k) {
        super(iinventory, i, j, k);
    }

    public boolean func_75214_a(ItemStack itemstack) {
        return TileEntityFurnace.func_145954_b(itemstack) || func_178173_c_(itemstack);
    }

    public int func_178170_b(ItemStack itemstack) {
        return func_178173_c_(itemstack) ? 1 : super.func_178170_b(itemstack);
    }

    public static boolean func_178173_c_(ItemStack itemstack) {
        return itemstack.func_77973_b() == Items.field_151133_ar;
    }
}
