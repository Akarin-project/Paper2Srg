package net.minecraft.item;
import net.minecraft.block.Block;


public class ItemColored extends ItemBlock {

    private String[] field_150945_c;

    public ItemColored(Block block, boolean flag) {
        super(block);
        if (flag) {
            this.func_77656_e(0);
            this.func_77627_a(true);
        }

    }

    public int func_77647_b(int i) {
        return i;
    }

    public ItemColored func_150943_a(String[] astring) {
        this.field_150945_c = astring;
        return this;
    }

    public String func_77667_c(ItemStack itemstack) {
        if (this.field_150945_c == null) {
            return super.func_77667_c(itemstack);
        } else {
            int i = itemstack.func_77960_j();

            return i >= 0 && i < this.field_150945_c.length ? super.func_77667_c(itemstack) + "." + this.field_150945_c[i] : super.func_77667_c(itemstack);
        }
    }
}
