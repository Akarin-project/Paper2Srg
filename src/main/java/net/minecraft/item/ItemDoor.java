package net.minecraft.item;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;


public class ItemDoor extends Item {

    private final Block block;

    public ItemDoor(Block block) {
        this.block = block;
        this.setCreativeTab(CreativeTabs.REDSTONE);
    }

    public EnumActionResult onItemUse(EntityPlayer entityhuman, World world, BlockPos blockposition, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (enumdirection != EnumFacing.UP) {
            return EnumActionResult.FAIL;
        } else {
            IBlockState iblockdata = world.getBlockState(blockposition);
            Block block = iblockdata.getBlock();

            if (!block.isReplaceable((IBlockAccess) world, blockposition)) {
                blockposition = blockposition.offset(enumdirection);
            }

            ItemStack itemstack = entityhuman.getHeldItem(enumhand);

            if (entityhuman.canPlayerEdit(blockposition, enumdirection, itemstack) && this.block.canPlaceBlockAt(world, blockposition)) {
                EnumFacing enumdirection1 = EnumFacing.fromAngle((double) entityhuman.rotationYaw);
                int i = enumdirection1.getFrontOffsetX();
                int j = enumdirection1.getFrontOffsetZ();
                boolean flag = i < 0 && f2 < 0.5F || i > 0 && f2 > 0.5F || j < 0 && f > 0.5F || j > 0 && f < 0.5F;

                placeDoor(world, blockposition, enumdirection1, this.block, flag);
                SoundType soundeffecttype = this.block.getSoundType();

                world.playSound(entityhuman, blockposition, soundeffecttype.getPlaceSound(), SoundCategory.BLOCKS, (soundeffecttype.getVolume() + 1.0F) / 2.0F, soundeffecttype.getPitch() * 0.8F);
                itemstack.shrink(1);
                return EnumActionResult.SUCCESS;
            } else {
                return EnumActionResult.FAIL;
            }
        }
    }

    public static void placeDoor(World world, BlockPos blockposition, EnumFacing enumdirection, Block block, boolean flag) {
        BlockPos blockposition1 = blockposition.offset(enumdirection.rotateY());
        BlockPos blockposition2 = blockposition.offset(enumdirection.rotateYCCW());
        int i = (world.getBlockState(blockposition2).isNormalCube() ? 1 : 0) + (world.getBlockState(blockposition2.up()).isNormalCube() ? 1 : 0);
        int j = (world.getBlockState(blockposition1).isNormalCube() ? 1 : 0) + (world.getBlockState(blockposition1.up()).isNormalCube() ? 1 : 0);
        boolean flag1 = world.getBlockState(blockposition2).getBlock() == block || world.getBlockState(blockposition2.up()).getBlock() == block;
        boolean flag2 = world.getBlockState(blockposition1).getBlock() == block || world.getBlockState(blockposition1.up()).getBlock() == block;

        if ((!flag1 || flag2) && j <= i) {
            if (flag2 && !flag1 || j < i) {
                flag = false;
            }
        } else {
            flag = true;
        }

        BlockPos blockposition3 = blockposition.up();
        boolean flag3 = world.isBlockPowered(blockposition) || world.isBlockPowered(blockposition3);
        IBlockState iblockdata = block.getDefaultState().withProperty(BlockDoor.FACING, enumdirection).withProperty(BlockDoor.HINGE, flag ? BlockDoor.EnumHingePosition.RIGHT : BlockDoor.EnumHingePosition.LEFT).withProperty(BlockDoor.POWERED, Boolean.valueOf(flag3)).withProperty(BlockDoor.OPEN, Boolean.valueOf(flag3));

        // Spigot start - update physics after the block multi place event
        world.setBlockState(blockposition, iblockdata.withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.LOWER), 3);
        world.setBlockState(blockposition3, iblockdata.withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER), 3);
        // world.applyPhysics(blockposition, block, false);
        // world.applyPhysics(blockposition3, block, false);
        // Spigot end
    }
}
