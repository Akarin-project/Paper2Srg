package net.minecraft.item;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.NonNullList;


public class ItemCoal extends Item {

    public ItemCoal() {
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(CreativeTabs.MATERIALS);
    }

    public String getUnlocalizedName(ItemStack itemstack) {
        return itemstack.getMetadata() == 1 ? "item.charcoal" : "item.coal";
    }

    public void getSubItems(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        if (this.isInCreativeTab(creativemodetab)) {
            nonnulllist.add(new ItemStack(this, 1, 0));
            nonnulllist.add(new ItemStack(this, 1, 1));
        }

    }
}
