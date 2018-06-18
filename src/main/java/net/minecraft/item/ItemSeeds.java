package net.minecraft.item;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class ItemSeeds extends Item {

    private final Block crops;
    private final Block soilBlockID;

    public ItemSeeds(Block block, Block block1) {
        this.crops = block;
        this.soilBlockID = block1;
        this.setCreativeTab(CreativeTabs.MATERIALS);
    }

    public EnumActionResult onItemUse(EntityPlayer entityhuman, World world, BlockPos blockposition, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        ItemStack itemstack = entityhuman.getHeldItem(enumhand);

        if (enumdirection == EnumFacing.UP && entityhuman.canPlayerEdit(blockposition.offset(enumdirection), enumdirection, itemstack) && world.getBlockState(blockposition).getBlock() == this.soilBlockID && world.isAirBlock(blockposition.up())) {
            world.setBlockState(blockposition.up(), this.crops.getDefaultState());
            if (entityhuman instanceof EntityPlayerMP) {
                CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) entityhuman, blockposition.up(), itemstack);
            }

            itemstack.shrink(1);
            return EnumActionResult.SUCCESS;
        } else {
            return EnumActionResult.FAIL;
        }
    }
}
