package net.minecraft.block;

import java.util.Iterator;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLadder extends Block {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    protected static final AxisAlignedBB LADDER_EAST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.1875D, 1.0D, 1.0D);
    protected static final AxisAlignedBB LADDER_WEST_AABB = new AxisAlignedBB(0.8125D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB LADDER_SOUTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.1875D);
    protected static final AxisAlignedBB LADDER_NORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.8125D, 1.0D, 1.0D, 1.0D);

    protected BlockLadder() {
        super(Material.CIRCUITS);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockLadder.FACING, EnumFacing.NORTH));
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }

    public AxisAlignedBB getBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        switch ((EnumFacing) iblockdata.getValue(BlockLadder.FACING)) {
        case NORTH:
            return BlockLadder.LADDER_NORTH_AABB;

        case SOUTH:
            return BlockLadder.LADDER_SOUTH_AABB;

        case WEST:
            return BlockLadder.LADDER_WEST_AABB;

        case EAST:
        default:
            return BlockLadder.LADDER_EAST_AABB;
        }
    }

    public boolean isOpaqueCube(IBlockState iblockdata) {
        return false;
    }

    public boolean isFullCube(IBlockState iblockdata) {
        return false;
    }

    public boolean canPlaceBlockOnSide(World world, BlockPos blockposition, EnumFacing enumdirection) {
        return this.canAttachTo(world, blockposition.west(), enumdirection) ? true : (this.canAttachTo(world, blockposition.east(), enumdirection) ? true : (this.canAttachTo(world, blockposition.north(), enumdirection) ? true : this.canAttachTo(world, blockposition.south(), enumdirection)));
    }

    private boolean canAttachTo(World world, BlockPos blockposition, EnumFacing enumdirection) {
        IBlockState iblockdata = world.getBlockState(blockposition);
        boolean flag = isExceptBlockForAttachWithPiston(iblockdata.getBlock());

        return !flag && iblockdata.getBlockFaceShape(world, blockposition, enumdirection) == BlockFaceShape.SOLID && !iblockdata.canProvidePower();
    }

    public IBlockState getStateForPlacement(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        if (enumdirection.getAxis().isHorizontal() && this.canAttachTo(world, blockposition.offset(enumdirection.getOpposite()), enumdirection)) {
            return this.getDefaultState().withProperty(BlockLadder.FACING, enumdirection);
        } else {
            Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

            EnumFacing enumdirection1;

            do {
                if (!iterator.hasNext()) {
                    return this.getDefaultState();
                }

                enumdirection1 = (EnumFacing) iterator.next();
            } while (!this.canAttachTo(world, blockposition.offset(enumdirection1.getOpposite()), enumdirection1));

            return this.getDefaultState().withProperty(BlockLadder.FACING, enumdirection1);
        }
    }

    public void neighborChanged(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        EnumFacing enumdirection = (EnumFacing) iblockdata.getValue(BlockLadder.FACING);

        if (!this.canAttachTo(world, blockposition.offset(enumdirection.getOpposite()), enumdirection)) {
            this.dropBlockAsItem(world, blockposition, iblockdata, 0);
            world.setBlockToAir(blockposition);
        }

        super.neighborChanged(iblockdata, world, blockposition, block, blockposition1);
    }

    public IBlockState getStateFromMeta(int i) {
        EnumFacing enumdirection = EnumFacing.getFront(i);

        if (enumdirection.getAxis() == EnumFacing.Axis.Y) {
            enumdirection = EnumFacing.NORTH;
        }

        return this.getDefaultState().withProperty(BlockLadder.FACING, enumdirection);
    }

    public int getMetaFromState(IBlockState iblockdata) {
        return ((EnumFacing) iblockdata.getValue(BlockLadder.FACING)).getIndex();
    }

    public IBlockState withRotation(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.withProperty(BlockLadder.FACING, enumblockrotation.rotate((EnumFacing) iblockdata.getValue(BlockLadder.FACING)));
    }

    public IBlockState withMirror(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata.withRotation(enumblockmirror.toRotation((EnumFacing) iblockdata.getValue(BlockLadder.FACING)));
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockLadder.FACING});
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }
}
