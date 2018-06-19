package net.minecraft.dispenser;
import net.minecraft.item.ItemStack;


public interface IBehaviorDispenseItem {

    IBehaviorDispenseItem field_82483_a = new IBehaviorDispenseItem() {
        public ItemStack func_82482_a(IBlockSource isourceblock, ItemStack itemstack) {
            return itemstack;
        }
    };

    ItemStack func_82482_a(IBlockSource isourceblock, ItemStack itemstack);
}
