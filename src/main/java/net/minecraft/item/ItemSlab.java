package net.minecraft.item;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class ItemSlab extends ItemBlock {

    private final BlockSlab singleSlab;
    private final BlockSlab doubleSlab;

    public ItemSlab(Block block, BlockSlab blockstepabstract, BlockSlab blockstepabstract1) {
        super(block);
        this.singleSlab = blockstepabstract;
        this.doubleSlab = blockstepabstract1;
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    public int getMetadata(int i) {
        return i;
    }

    public String getUnlocalizedName(ItemStack itemstack) {
        return this.singleSlab.getUnlocalizedName(itemstack.getMetadata());
    }

    public EnumActionResult onItemUse(EntityPlayer entityhuman, World world, BlockPos blockposition, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        ItemStack itemstack = entityhuman.getHeldItem(enumhand);

        if (!itemstack.isEmpty() && entityhuman.canPlayerEdit(blockposition.offset(enumdirection), enumdirection, itemstack)) {
            Comparable comparable = this.singleSlab.getTypeForItem(itemstack);
            IBlockState iblockdata = world.getBlockState(blockposition);

            if (iblockdata.getBlock() == this.singleSlab) {
                IProperty iblockstate = this.singleSlab.getVariantProperty();
                Comparable comparable1 = iblockdata.getValue(iblockstate);
                BlockSlab.EnumBlockHalf blockstepabstract_enumslabhalf = (BlockSlab.EnumBlockHalf) iblockdata.getValue(BlockSlab.HALF);

                if ((enumdirection == EnumFacing.UP && blockstepabstract_enumslabhalf == BlockSlab.EnumBlockHalf.BOTTOM || enumdirection == EnumFacing.DOWN && blockstepabstract_enumslabhalf == BlockSlab.EnumBlockHalf.TOP) && comparable1 == comparable) {
                    IBlockState iblockdata1 = this.makeState(iblockstate, comparable1);
                    AxisAlignedBB axisalignedbb = iblockdata1.getCollisionBoundingBox(world, blockposition);

                    if (axisalignedbb != Block.NULL_AABB && world.checkNoEntityCollision(axisalignedbb.offset(blockposition)) && world.setBlockState(blockposition, iblockdata1, 11)) {
                        SoundType soundeffecttype = this.doubleSlab.getSoundType();

                        world.playSound(entityhuman, blockposition, soundeffecttype.getPlaceSound(), SoundCategory.BLOCKS, (soundeffecttype.getVolume() + 1.0F) / 2.0F, soundeffecttype.getPitch() * 0.8F);
                        itemstack.shrink(1);
                        if (entityhuman instanceof EntityPlayerMP) {
                            CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) entityhuman, blockposition, itemstack);
                        }
                    }

                    return EnumActionResult.SUCCESS;
                }
            }

            return this.tryPlace(entityhuman, itemstack, world, blockposition.offset(enumdirection), (Object) comparable) ? EnumActionResult.SUCCESS : super.onItemUse(entityhuman, world, blockposition, enumhand, enumdirection, f, f1, f2);
        } else {
            return EnumActionResult.FAIL;
        }
    }

    private boolean tryPlace(EntityPlayer entityhuman, ItemStack itemstack, World world, BlockPos blockposition, Object object) {
        IBlockState iblockdata = world.getBlockState(blockposition);

        if (iblockdata.getBlock() == this.singleSlab) {
            Comparable comparable = iblockdata.getValue(this.singleSlab.getVariantProperty());

            if (comparable == object) {
                IBlockState iblockdata1 = this.makeState(this.singleSlab.getVariantProperty(), comparable);
                AxisAlignedBB axisalignedbb = iblockdata1.getCollisionBoundingBox(world, blockposition);

                if (axisalignedbb != Block.NULL_AABB && world.checkNoEntityCollision(axisalignedbb.offset(blockposition)) && world.setBlockState(blockposition, iblockdata1, 11)) {
                    SoundType soundeffecttype = this.doubleSlab.getSoundType();

                    world.playSound(entityhuman, blockposition, soundeffecttype.getPlaceSound(), SoundCategory.BLOCKS, (soundeffecttype.getVolume() + 1.0F) / 2.0F, soundeffecttype.getPitch() * 0.8F);
                    itemstack.shrink(1);
                }

                return true;
            }
        }

        return false;
    }

    protected <T extends Comparable<T>> IBlockState makeState(IProperty<T> iblockstate, Comparable<?> comparable) {
        return this.doubleSlab.getDefaultState().withProperty(iblockstate, comparable);
    }
}
