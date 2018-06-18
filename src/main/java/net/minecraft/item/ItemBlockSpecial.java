package net.minecraft.item;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;


public class ItemBlockSpecial extends Item {

    public final Block block; // PAIL: private->public

    public ItemBlockSpecial(Block block) {
        this.block = block;
    }

    public EnumActionResult onItemUse(EntityPlayer entityhuman, World world, BlockPos blockposition, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        IBlockState iblockdata = world.getBlockState(blockposition);
        Block block = iblockdata.getBlock();

        if (block == Blocks.SNOW_LAYER && ((Integer) iblockdata.getValue(BlockSnow.LAYERS)).intValue() < 1) {
            enumdirection = EnumFacing.UP;
        } else if (!block.isReplaceable((IBlockAccess) world, blockposition)) {
            blockposition = blockposition.offset(enumdirection);
        }

        ItemStack itemstack = entityhuman.getHeldItem(enumhand);

        if (!itemstack.isEmpty() && entityhuman.canPlayerEdit(blockposition, enumdirection, itemstack) && world.mayPlace(this.block, blockposition, false, enumdirection, (Entity) null)) {
            IBlockState iblockdata1 = this.block.getStateForPlacement(world, blockposition, enumdirection, f, f1, f2, 0, entityhuman);

            if (!world.setBlockState(blockposition, iblockdata1, 11)) {
                return EnumActionResult.FAIL;
            } else {
                iblockdata1 = world.getBlockState(blockposition);
                if (iblockdata1.getBlock() == this.block) {
                    ItemBlock.setTileEntityNBT(world, entityhuman, blockposition, itemstack);
                    iblockdata1.getBlock().onBlockPlacedBy(world, blockposition, iblockdata1, entityhuman, itemstack);
                    if (entityhuman instanceof EntityPlayerMP) {
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) entityhuman, blockposition, itemstack);
                    }
                }

                SoundType soundeffecttype = this.block.getSoundType();

                world.playSound(entityhuman, blockposition, soundeffecttype.getPlaceSound(), SoundCategory.BLOCKS, (soundeffecttype.getVolume() + 1.0F) / 2.0F, soundeffecttype.getPitch() * 0.8F);
                itemstack.shrink(1);
                return EnumActionResult.SUCCESS;
            }
        } else {
            return EnumActionResult.FAIL;
        }
    }
}
