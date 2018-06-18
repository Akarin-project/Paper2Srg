package net.minecraft.item;
import net.minecraft.block.Block;


public class ItemAir extends Item {

    private final Block block;

    public ItemAir(Block block) {
        this.block = block;
    }

    public String getUnlocalizedName(ItemStack itemstack) {
        return this.block.getUnlocalizedName();
    }

    public String getUnlocalizedName() {
        return this.block.getUnlocalizedName();
    }
}
