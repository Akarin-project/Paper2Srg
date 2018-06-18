package net.minecraft.item;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;


public class ItemSnow extends ItemBlock {

    public ItemSnow(Block block) {
        super(block);
        this.setMaxDamage(0);
    }

    public EnumActionResult onItemUse(EntityPlayer entityhuman, World world, BlockPos blockposition, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        ItemStack itemstack = entityhuman.getHeldItem(enumhand);

        if (!itemstack.isEmpty() && entityhuman.canPlayerEdit(blockposition, enumdirection, itemstack)) {
            IBlockState iblockdata = world.getBlockState(blockposition);
            Block block = iblockdata.getBlock();
            BlockPos blockposition1 = blockposition;

            if ((enumdirection != EnumFacing.UP || block != this.block) && !block.isReplaceable((IBlockAccess) world, blockposition)) {
                blockposition1 = blockposition.offset(enumdirection);
                iblockdata = world.getBlockState(blockposition1);
                block = iblockdata.getBlock();
            }

            if (block == this.block) {
                int i = ((Integer) iblockdata.getValue(BlockSnow.LAYERS)).intValue();

                if (i < 8) {
                    IBlockState iblockdata1 = iblockdata.withProperty(BlockSnow.LAYERS, Integer.valueOf(i + 1));
                    AxisAlignedBB axisalignedbb = iblockdata1.getCollisionBoundingBox(world, blockposition1);

                    if (axisalignedbb != Block.NULL_AABB && world.checkNoEntityCollision(axisalignedbb.offset(blockposition1)) && world.setBlockState(blockposition1, iblockdata1, 10)) {
                        SoundType soundeffecttype = this.block.getSoundType();

                        world.playSound(entityhuman, blockposition1, soundeffecttype.getPlaceSound(), SoundCategory.BLOCKS, (soundeffecttype.getVolume() + 1.0F) / 2.0F, soundeffecttype.getPitch() * 0.8F);
                        if (entityhuman instanceof EntityPlayerMP) {
                            CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) entityhuman, blockposition, itemstack);
                        }

                        itemstack.shrink(1);
                        return EnumActionResult.SUCCESS;
                    }
                }
            }

            return super.onItemUse(entityhuman, world, blockposition, enumhand, enumdirection, f, f1, f2);
        } else {
            return EnumActionResult.FAIL;
        }
    }

    public int getMetadata(int i) {
        return i;
    }
}
