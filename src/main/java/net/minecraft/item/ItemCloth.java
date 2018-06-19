package net.minecraft.item;
import net.minecraft.block.Block;


public class ItemCloth extends ItemBlock {

    public ItemCloth(Block block) {
        super(block);
        this.func_77656_e(0);
        this.func_77627_a(true);
    }

    public int func_77647_b(int i) {
        return i;
    }

    public String func_77667_c(ItemStack itemstack) {
        return super.func_77658_a() + "." + EnumDyeColor.func_176764_b(itemstack.func_77960_j()).func_176762_d();
    }
}
