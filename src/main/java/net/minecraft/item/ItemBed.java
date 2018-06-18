package net.minecraft.item;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;


public class ItemBed extends Item {

    public ItemBed() {
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    public EnumActionResult onItemUse(EntityPlayer entityhuman, World world, BlockPos blockposition, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (world.isRemote) {
            return EnumActionResult.SUCCESS;
        } else if (enumdirection != EnumFacing.UP) {
            return EnumActionResult.FAIL;
        } else {
            IBlockState iblockdata = world.getBlockState(blockposition);
            Block block = iblockdata.getBlock();
            boolean flag = block.isReplaceable((IBlockAccess) world, blockposition);

            if (!flag) {
                blockposition = blockposition.up();
            }

            int i = MathHelper.floor((double) (entityhuman.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            EnumFacing enumdirection1 = EnumFacing.getHorizontal(i);
            BlockPos blockposition1 = blockposition.offset(enumdirection1);
            ItemStack itemstack = entityhuman.getHeldItem(enumhand);

            if (entityhuman.canPlayerEdit(blockposition, enumdirection, itemstack) && entityhuman.canPlayerEdit(blockposition1, enumdirection, itemstack)) {
                IBlockState iblockdata1 = world.getBlockState(blockposition1);
                boolean flag1 = iblockdata1.getBlock().isReplaceable((IBlockAccess) world, blockposition1);
                boolean flag2 = flag || world.isAirBlock(blockposition);
                boolean flag3 = flag1 || world.isAirBlock(blockposition1);

                if (flag2 && flag3 && world.getBlockState(blockposition.down()).isTopSolid() && world.getBlockState(blockposition1.down()).isTopSolid()) {
                    IBlockState iblockdata2 = Blocks.BED.getDefaultState().withProperty(BlockBed.OCCUPIED, Boolean.valueOf(false)).withProperty(BlockBed.FACING, enumdirection1).withProperty(BlockBed.PART, BlockBed.EnumPartType.FOOT);

                    world.setBlockState(blockposition, iblockdata2, 10);
                    world.setBlockState(blockposition1, iblockdata2.withProperty(BlockBed.PART, BlockBed.EnumPartType.HEAD), 10);
                    SoundType soundeffecttype = iblockdata2.getBlock().getSoundType();

                    world.playSound((EntityPlayer) null, blockposition, soundeffecttype.getPlaceSound(), SoundCategory.BLOCKS, (soundeffecttype.getVolume() + 1.0F) / 2.0F, soundeffecttype.getPitch() * 0.8F);
                    TileEntity tileentity = world.getTileEntity(blockposition1);

                    if (tileentity instanceof TileEntityBed) {
                        ((TileEntityBed) tileentity).setItemValues(itemstack);
                    }

                    TileEntity tileentity1 = world.getTileEntity(blockposition);

                    if (tileentity1 instanceof TileEntityBed) {
                        ((TileEntityBed) tileentity1).setItemValues(itemstack);
                    }

                    world.notifyNeighborsRespectDebug(blockposition, block, false);
                    world.notifyNeighborsRespectDebug(blockposition1, iblockdata1.getBlock(), false);
                    if (entityhuman instanceof EntityPlayerMP) {
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) entityhuman, blockposition, itemstack);
                    }

                    itemstack.shrink(1);
                    return EnumActionResult.SUCCESS;
                } else {
                    return EnumActionResult.FAIL;
                }
            } else {
                return EnumActionResult.FAIL;
            }
        }
    }

    public String getUnlocalizedName(ItemStack itemstack) {
        return super.getUnlocalizedName() + "." + EnumDyeColor.byMetadata(itemstack.getMetadata()).getUnlocalizedName();
    }

    public void getSubItems(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        if (this.isInCreativeTab(creativemodetab)) {
            for (int i = 0; i < 16; ++i) {
                nonnulllist.add(new ItemStack(this, 1, i));
            }
        }

    }
}
