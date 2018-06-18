package net.minecraft.item;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockStandingSign;
import net.minecraft.block.BlockWallSign;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;


public class ItemSign extends Item {

    public ItemSign() {
        this.maxStackSize = 16;
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }

    public EnumActionResult onItemUse(EntityPlayer entityhuman, World world, BlockPos blockposition, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        IBlockState iblockdata = world.getBlockState(blockposition);
        boolean flag = iblockdata.getBlock().isReplaceable((IBlockAccess) world, blockposition);

        if (enumdirection != EnumFacing.DOWN && (iblockdata.getMaterial().isSolid() || flag) && (!flag || enumdirection == EnumFacing.UP)) {
            blockposition = blockposition.offset(enumdirection);
            ItemStack itemstack = entityhuman.getHeldItem(enumhand);

            if (entityhuman.canPlayerEdit(blockposition, enumdirection, itemstack) && Blocks.STANDING_SIGN.canPlaceBlockAt(world, blockposition)) {
                if (world.isRemote) {
                    return EnumActionResult.SUCCESS;
                } else {
                    blockposition = flag ? blockposition.down() : blockposition;
                    if (enumdirection == EnumFacing.UP) {
                        int i = MathHelper.floor((double) ((entityhuman.rotationYaw + 180.0F) * 16.0F / 360.0F) + 0.5D) & 15;

                        world.setBlockState(blockposition, Blocks.STANDING_SIGN.getDefaultState().withProperty(BlockStandingSign.ROTATION, Integer.valueOf(i)), 11);
                    } else {
                        world.setBlockState(blockposition, Blocks.WALL_SIGN.getDefaultState().withProperty(BlockWallSign.FACING, enumdirection), 11);
                    }

                    TileEntity tileentity = world.getTileEntity(blockposition);

                    if (tileentity instanceof TileEntitySign && !ItemBlock.setTileEntityNBT(world, entityhuman, blockposition, itemstack)) {
                        entityhuman.openEditSign((TileEntitySign) tileentity);
                    }

                    if (entityhuman instanceof EntityPlayerMP) {
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) entityhuman, blockposition, itemstack);
                    }

                    itemstack.shrink(1);
                    return EnumActionResult.SUCCESS;
                }
            } else {
                return EnumActionResult.FAIL;
            }
        } else {
            return EnumActionResult.FAIL;
        }
    }
}
