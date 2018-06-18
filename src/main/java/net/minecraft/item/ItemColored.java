package net.minecraft.item;
import net.minecraft.block.Block;


public class ItemColored extends ItemBlock {

    private String[] subtypeNames;

    public ItemColored(Block block, boolean flag) {
        super(block);
        if (flag) {
            this.setMaxDamage(0);
            this.setHasSubtypes(true);
        }

    }

    public int getMetadata(int i) {
        return i;
    }

    public ItemColored setSubtypeNames(String[] astring) {
        this.subtypeNames = astring;
        return this;
    }

    public String getUnlocalizedName(ItemStack itemstack) {
        if (this.subtypeNames == null) {
            return super.getUnlocalizedName(itemstack);
        } else {
            int i = itemstack.getMetadata();

            return i >= 0 && i < this.subtypeNames.length ? super.getUnlocalizedName(itemstack) + "." + this.subtypeNames[i] : super.getUnlocalizedName(itemstack);
        }
    }
}
