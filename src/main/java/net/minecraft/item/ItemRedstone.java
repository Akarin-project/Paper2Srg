package net.minecraft.item;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;


public class ItemRedstone extends Item {

    public ItemRedstone() {
        this.setCreativeTab(CreativeTabs.REDSTONE);
    }

    public EnumActionResult onItemUse(EntityPlayer entityhuman, World world, BlockPos blockposition, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        boolean flag = world.getBlockState(blockposition).getBlock().isReplaceable((IBlockAccess) world, blockposition);
        BlockPos blockposition1 = flag ? blockposition : blockposition.offset(enumdirection);
        ItemStack itemstack = entityhuman.getHeldItem(enumhand);

        if (!itemstack.isEmpty() && entityhuman.canPlayerEdit(blockposition1, enumdirection, itemstack) && world.mayPlace(world.getBlockState(blockposition1).getBlock(), blockposition1, false, enumdirection, (Entity) null) && Blocks.REDSTONE_WIRE.canPlaceBlockAt(world, blockposition1)) { // CraftBukkit
            world.setBlockState(blockposition1, Blocks.REDSTONE_WIRE.getDefaultState());
            if (entityhuman instanceof EntityPlayerMP) {
                CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) entityhuman, blockposition1, itemstack);
            }

            itemstack.shrink(1);
            return EnumActionResult.SUCCESS;
        } else {
            return EnumActionResult.FAIL;
        }
    }
}
