package net.minecraft.dispenser;
import net.minecraft.item.ItemStack;


public interface IBehaviorDispenseItem {

    IBehaviorDispenseItem DEFAULT_BEHAVIOR = new IBehaviorDispenseItem() {
        public ItemStack dispense(IBlockSource isourceblock, ItemStack itemstack) {
            return itemstack;
        }
    };

    ItemStack dispense(IBlockSource isourceblock, ItemStack itemstack);
}
