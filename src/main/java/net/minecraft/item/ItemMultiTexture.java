package net.minecraft.item;
import net.minecraft.block.Block;
import net.minecraft.server.ItemMultiTexture.a;


public class ItemMultiTexture extends ItemBlock {

    protected final Block unused;
    protected final ItemMultiTexture.a nameFunction;

    public ItemMultiTexture(Block block, Block block1, ItemMultiTexture.a itemmultitexture_a) {
        super(block);
        this.unused = block1;
        this.nameFunction = itemmultitexture_a;
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    public ItemMultiTexture(Block block, Block block1, final String[] astring) {
        this(block, block1, new ItemMultiTexture.a() {
            public String a(ItemStack itemstack) {
                int i = itemstack.getMetadata();

                if (i < 0 || i >= astring.length) {
                    i = 0;
                }

                return astring[i];
            }
        });
    }

    public int getMetadata(int i) {
        return i;
    }

    public String getUnlocalizedName(ItemStack itemstack) {
        return super.getUnlocalizedName() + "." + this.nameFunction.a(itemstack);
    }

    public interface a {

        String a(ItemStack itemstack);
    }
}
