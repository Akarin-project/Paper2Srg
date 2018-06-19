package net.minecraft.item;
import net.minecraft.block.BlockLeaves;


public class ItemLeaves extends ItemBlock {

    private final BlockLeaves field_150940_b;

    public ItemLeaves(BlockLeaves blockleaves) {
        super(blockleaves);
        this.field_150940_b = blockleaves;
        this.func_77656_e(0);
        this.func_77627_a(true);
    }

    public int func_77647_b(int i) {
        return i | 4;
    }

    public String func_77667_c(ItemStack itemstack) {
        return super.func_77658_a() + "." + this.field_150940_b.func_176233_b(itemstack.func_77960_j()).func_176840_c();
    }
}
