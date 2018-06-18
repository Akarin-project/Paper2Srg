package net.minecraft.item;

import com.google.common.collect.Sets;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public class ItemAxe extends ItemTool {

    private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(new Block[] { Blocks.PLANKS, Blocks.BOOKSHELF, Blocks.LOG, Blocks.LOG2, Blocks.CHEST, Blocks.PUMPKIN, Blocks.LIT_PUMPKIN, Blocks.MELON_BLOCK, Blocks.LADDER, Blocks.WOODEN_BUTTON, Blocks.WOODEN_PRESSURE_PLATE});
    private static final float[] ATTACK_DAMAGES = new float[] { 6.0F, 8.0F, 8.0F, 8.0F, 6.0F};
    private static final float[] ATTACK_SPEEDS = new float[] { -3.2F, -3.2F, -3.1F, -3.0F, -3.0F};

    protected ItemAxe(Item.ToolMaterial item_enumtoolmaterial) {
        super(item_enumtoolmaterial, ItemAxe.EFFECTIVE_ON);
        this.attackDamage = ItemAxe.ATTACK_DAMAGES[item_enumtoolmaterial.ordinal()];
        this.attackSpeed = ItemAxe.ATTACK_SPEEDS[item_enumtoolmaterial.ordinal()];
    }

    public float getDestroySpeed(ItemStack itemstack, IBlockState iblockdata) {
        Material material = iblockdata.getMaterial();

        return material != Material.WOOD && material != Material.PLANTS && material != Material.VINE ? super.getDestroySpeed(itemstack, iblockdata) : this.efficiency;
    }
}
