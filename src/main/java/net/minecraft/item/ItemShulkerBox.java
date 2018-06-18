package net.minecraft.item;
import net.minecraft.block.Block;


public class ItemShulkerBox extends ItemBlock {

    public ItemShulkerBox(Block block) {
        super(block);
        this.setMaxStackSize(1);
    }
}
