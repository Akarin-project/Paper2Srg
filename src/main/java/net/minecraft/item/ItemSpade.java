package net.minecraft.item;

import com.google.common.collect.Sets;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemSpade extends ItemTool {

    private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(new Block[] { Blocks.CLAY, Blocks.DIRT, Blocks.FARMLAND, Blocks.GRASS, Blocks.GRAVEL, Blocks.MYCELIUM, Blocks.SAND, Blocks.SNOW, Blocks.SNOW_LAYER, Blocks.SOUL_SAND, Blocks.GRASS_PATH, Blocks.CONCRETE_POWDER});

    public ItemSpade(Item.ToolMaterial item_enumtoolmaterial) {
        super(1.5F, -3.0F, item_enumtoolmaterial, ItemSpade.EFFECTIVE_ON);
    }

    public boolean canHarvestBlock(IBlockState iblockdata) {
        Block block = iblockdata.getBlock();

        return block == Blocks.SNOW_LAYER ? true : block == Blocks.SNOW;
    }

    public EnumActionResult onItemUse(EntityPlayer entityhuman, World world, BlockPos blockposition, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        ItemStack itemstack = entityhuman.getHeldItem(enumhand);

        if (!entityhuman.canPlayerEdit(blockposition.offset(enumdirection), enumdirection, itemstack)) {
            return EnumActionResult.FAIL;
        } else {
            IBlockState iblockdata = world.getBlockState(blockposition);
            Block block = iblockdata.getBlock();

            if (enumdirection != EnumFacing.DOWN && world.getBlockState(blockposition.up()).getMaterial() == Material.AIR && block == Blocks.GRASS) {
                IBlockState iblockdata1 = Blocks.GRASS_PATH.getDefaultState();

                world.playSound(entityhuman, blockposition, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);
                if (!world.isRemote) {
                    world.setBlockState(blockposition, iblockdata1, 11);
                    itemstack.damageItem(1, entityhuman);
                }

                return EnumActionResult.SUCCESS;
            } else {
                return EnumActionResult.PASS;
            }
        }
    }
}
