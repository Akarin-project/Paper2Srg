package net.minecraft.item;

public class ItemBook extends Item {

    public ItemBook() {}

    public boolean isEnchantable(ItemStack itemstack) {
        return itemstack.getCount() == 1;
    }

    public int getItemEnchantability() {
        return 1;
    }
}
