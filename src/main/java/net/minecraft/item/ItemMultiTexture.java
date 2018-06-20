package net.minecraft.item;

import net.minecraft.block.Block;

public class ItemMultiTexture extends ItemBlock {

    protected final Block field_179227_b;
    protected final ItemMultiTexture.a field_179228_c;

    public ItemMultiTexture(Block block, Block block1, ItemMultiTexture.a itemmultitexture_a) {
        super(block);
        this.field_179227_b = block1;
        this.field_179228_c = itemmultitexture_a;
        this.func_77656_e(0);
        this.func_77627_a(true);
    }

    public ItemMultiTexture(Block block, Block block1, final String[] astring) {
        this(block, block1, new ItemMultiTexture.a() {
            @Override
            public String a(ItemStack itemstack) {
                int i = itemstack.func_77960_j();

                if (i < 0 || i >= astring.length) {
                    i = 0;
                }

                return astring[i];
            }
        });
    }

    @Override
    public int func_77647_b(int i) {
        return i;
    }

    @Override
    public String func_77667_c(ItemStack itemstack) {
        return super.func_77658_a() + "." + this.field_179228_c.a(itemstack);
    }

    public interface a {

        String a(ItemStack itemstack);
    }
}
