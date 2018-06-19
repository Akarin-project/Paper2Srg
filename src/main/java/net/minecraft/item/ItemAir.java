package net.minecraft.item;
import net.minecraft.block.Block;


public class ItemAir extends Item {

    private final Block field_190904_a;

    public ItemAir(Block block) {
        this.field_190904_a = block;
    }

    public String func_77667_c(ItemStack itemstack) {
        return this.field_190904_a.func_149739_a();
    }

    public String func_77658_a() {
        return this.field_190904_a.func_149739_a();
    }
}
