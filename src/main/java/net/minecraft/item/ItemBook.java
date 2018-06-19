package net.minecraft.item;

public class ItemBook extends Item {

    public ItemBook() {}

    public boolean func_77616_k(ItemStack itemstack) {
        return itemstack.func_190916_E() == 1;
    }

    public int func_77619_b() {
        return 1;
    }
}
