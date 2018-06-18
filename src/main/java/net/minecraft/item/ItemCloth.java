package net.minecraft.item;
import net.minecraft.block.Block;


public class ItemCloth extends ItemBlock {

    public ItemCloth(Block block) {
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    public int getMetadata(int i) {
        return i;
    }

    public String getUnlocalizedName(ItemStack itemstack) {
        return super.getUnlocalizedName() + "." + EnumDyeColor.byMetadata(itemstack.getMetadata()).getUnlocalizedName();
    }
}
