package net.minecraft.item;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.NonNullList;


public class ItemCoal extends Item {

    public ItemCoal() {
        this.func_77627_a(true);
        this.func_77656_e(0);
        this.func_77637_a(CreativeTabs.field_78035_l);
    }

    public String func_77667_c(ItemStack itemstack) {
        return itemstack.func_77960_j() == 1 ? "item.charcoal" : "item.coal";
    }

    public void func_150895_a(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        if (this.func_194125_a(creativemodetab)) {
            nonnulllist.add(new ItemStack(this, 1, 0));
            nonnulllist.add(new ItemStack(this, 1, 1));
        }

    }
}
