package net.minecraft.block;

import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemLead;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFence extends Block {

    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");
    protected static final AxisAlignedBB[] BOUNDING_BOXES = new AxisAlignedBB[] { new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 1.0D, 0.625D), new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.375D, 0.625D, 1.0D, 0.625D), new AxisAlignedBB(0.0D, 0.0D, 0.375D, 0.625D, 1.0D, 1.0D), new AxisAlignedBB(0.375D, 0.0D, 0.0D, 0.625D, 1.0D, 0.625D), new AxisAlignedBB(0.375D, 0.0D, 0.0D, 0.625D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.625D, 1.0D, 0.625D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.625D, 1.0D, 1.0D), new AxisAlignedBB(0.375D, 0.0D, 0.375D, 1.0D, 1.0D, 0.625D), new AxisAlignedBB(0.375D, 0.0D, 0.375D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.375D, 1.0D, 1.0D, 0.625D), new AxisAlignedBB(0.0D, 0.0D, 0.375D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.375D, 0.0D, 0.0D, 1.0D, 1.0D, 0.625D), new AxisAlignedBB(0.375D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.625D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)};
    public static final AxisAlignedBB PILLAR_AABB = new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 1.5D, 0.625D);
    public static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.375D, 0.0D, 0.625D, 0.625D, 1.5D, 1.0D);
    public static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.375D, 0.375D, 1.5D, 0.625D);
    public static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.375D, 0.0D, 0.0D, 0.625D, 1.5D, 0.375D);
    public static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.625D, 0.0D, 0.375D, 1.0D, 1.5D, 0.625D);

    public BlockFence(Material material, MapColor materialmapcolor) {
        super(material, materialmapcolor);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockFence.NORTH, Boolean.valueOf(false)).withProperty(BlockFence.EAST, Boolean.valueOf(false)).withProperty(BlockFence.SOUTH, Boolean.valueOf(false)).withProperty(BlockFence.WEST, Boolean.valueOf(false)));
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }

    public void addCollisionBoxToList(IBlockState iblockdata, World world, BlockPos blockposition, AxisAlignedBB axisalignedbb, List<AxisAlignedBB> list, @Nullable Entity entity, boolean flag) {
        if (!flag) {
            iblockdata = iblockdata.getActualState(world, blockposition);
        }

        addCollisionBoxToList(blockposition, axisalignedbb, list, BlockFence.PILLAR_AABB);
        if (((Boolean) iblockdata.getValue(BlockFence.NORTH)).booleanValue()) {
            addCollisionBoxToList(blockposition, axisalignedbb, list, BlockFence.NORTH_AABB);
        }

        if (((Boolean) iblockdata.getValue(BlockFence.EAST)).booleanValue()) {
            addCollisionBoxToList(blockposition, axisalignedbb, list, BlockFence.EAST_AABB);
        }

        if (((Boolean) iblockdata.getValue(BlockFence.SOUTH)).booleanValue()) {
            addCollisionBoxToList(blockposition, axisalignedbb, list, BlockFence.SOUTH_AABB);
        }

        if (((Boolean) iblockdata.getValue(BlockFence.WEST)).booleanValue()) {
            addCollisionBoxToList(blockposition, axisalignedbb, list, BlockFence.WEST_AABB);
        }

    }

    public AxisAlignedBB getBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        iblockdata = this.getActualState(iblockdata, iblockaccess, blockposition);
        return BlockFence.BOUNDING_BOXES[getBoundingBoxIdx(iblockdata)];
    }

    private static int getBoundingBoxIdx(IBlockState iblockdata) {
        int i = 0;

        if (((Boolean) iblockdata.getValue(BlockFence.NORTH)).booleanValue()) {
            i |= 1 << EnumFacing.NORTH.getHorizontalIndex();
        }

        if (((Boolean) iblockdata.getValue(BlockFence.EAST)).booleanValue()) {
            i |= 1 << EnumFacing.EAST.getHorizontalIndex();
        }

        if (((Boolean) iblockdata.getValue(BlockFence.SOUTH)).booleanValue()) {
            i |= 1 << EnumFacing.SOUTH.getHorizontalIndex();
        }

        if (((Boolean) iblockdata.getValue(BlockFence.WEST)).booleanValue()) {
            i |= 1 << EnumFacing.WEST.getHorizontalIndex();
        }

        return i;
    }

    public boolean isOpaqueCube(IBlockState iblockdata) {
        return false;
    }

    public boolean isFullCube(IBlockState iblockdata) {
        return false;
    }

    public boolean isPassable(IBlockAccess iblockaccess, BlockPos blockposition) {
        return false;
    }

    public boolean canConnectTo(IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        IBlockState iblockdata = iblockaccess.getBlockState(blockposition);
        BlockFaceShape enumblockfaceshape = iblockdata.getBlockFaceShape(iblockaccess, blockposition, enumdirection);
        Block block = iblockdata.getBlock();
        boolean flag = enumblockfaceshape == BlockFaceShape.MIDDLE_POLE && (iblockdata.getMaterial() == this.blockMaterial || block instanceof BlockFenceGate);

        return !isExcepBlockForAttachWithPiston(block) && enumblockfaceshape == BlockFaceShape.SOLID || flag;
    }

    protected static boolean isExcepBlockForAttachWithPiston(Block block) {
        return Block.isExceptBlockForAttachWithPiston(block) || block == Blocks.BARRIER || block == Blocks.MELON_BLOCK || block == Blocks.PUMPKIN || block == Blocks.LIT_PUMPKIN;
    }

    public boolean onBlockActivated(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (!world.isRemote) {
            return ItemLead.attachToFence(entityhuman, world, blockposition);
        } else {
            ItemStack itemstack = entityhuman.getHeldItem(enumhand);

            return itemstack.getItem() == Items.LEAD || itemstack.isEmpty();
        }
    }

    public int getMetaFromState(IBlockState iblockdata) {
        return 0;
    }

    public IBlockState getActualState(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return iblockdata.withProperty(BlockFence.NORTH, Boolean.valueOf(this.canConnectTo(iblockaccess, blockposition.north(), EnumFacing.SOUTH))).withProperty(BlockFence.EAST, Boolean.valueOf(this.canConnectTo(iblockaccess, blockposition.east(), EnumFacing.WEST))).withProperty(BlockFence.SOUTH, Boolean.valueOf(this.canConnectTo(iblockaccess, blockposition.south(), EnumFacing.NORTH))).withProperty(BlockFence.WEST, Boolean.valueOf(this.canConnectTo(iblockaccess, blockposition.west(), EnumFacing.EAST)));
    }

    public IBlockState withRotation(IBlockState iblockdata, Rotation enumblockrotation) {
        switch (enumblockrotation) {
        case CLOCKWISE_180:
            return iblockdata.withProperty(BlockFence.NORTH, iblockdata.getValue(BlockFence.SOUTH)).withProperty(BlockFence.EAST, iblockdata.getValue(BlockFence.WEST)).withProperty(BlockFence.SOUTH, iblockdata.getValue(BlockFence.NORTH)).withProperty(BlockFence.WEST, iblockdata.getValue(BlockFence.EAST));

        case COUNTERCLOCKWISE_90:
            return iblockdata.withProperty(BlockFence.NORTH, iblockdata.getValue(BlockFence.EAST)).withProperty(BlockFence.EAST, iblockdata.getValue(BlockFence.SOUTH)).withProperty(BlockFence.SOUTH, iblockdata.getValue(BlockFence.WEST)).withProperty(BlockFence.WEST, iblockdata.getValue(BlockFence.NORTH));

        case CLOCKWISE_90:
            return iblockdata.withProperty(BlockFence.NORTH, iblockdata.getValue(BlockFence.WEST)).withProperty(BlockFence.EAST, iblockdata.getValue(BlockFence.NORTH)).withProperty(BlockFence.SOUTH, iblockdata.getValue(BlockFence.EAST)).withProperty(BlockFence.WEST, iblockdata.getValue(BlockFence.SOUTH));

        default:
            return iblockdata;
        }
    }

    public IBlockState withMirror(IBlockState iblockdata, Mirror enumblockmirror) {
        switch (enumblockmirror) {
        case LEFT_RIGHT:
            return iblockdata.withProperty(BlockFence.NORTH, iblockdata.getValue(BlockFence.SOUTH)).withProperty(BlockFence.SOUTH, iblockdata.getValue(BlockFence.NORTH));

        case FRONT_BACK:
            return iblockdata.withProperty(BlockFence.EAST, iblockdata.getValue(BlockFence.WEST)).withProperty(BlockFence.WEST, iblockdata.getValue(BlockFence.EAST));

        default:
            return super.withMirror(iblockdata, enumblockmirror);
        }
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockFence.NORTH, BlockFence.EAST, BlockFence.WEST, BlockFence.SOUTH});
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return enumdirection != EnumFacing.UP && enumdirection != EnumFacing.DOWN ? BlockFaceShape.MIDDLE_POLE : BlockFaceShape.CENTER;
    }
}
