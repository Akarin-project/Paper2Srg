package net.minecraft.item;
import net.minecraft.block.BlockLeaves;


public class ItemLeaves extends ItemBlock {

    private final BlockLeaves leaves;

    public ItemLeaves(BlockLeaves blockleaves) {
        super(blockleaves);
        this.leaves = blockleaves;
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    public int getMetadata(int i) {
        return i | 4;
    }

    public String getUnlocalizedName(ItemStack itemstack) {
        return super.getUnlocalizedName() + "." + this.leaves.getWoodType(itemstack.getMetadata()).getUnlocalizedName();
    }
}
