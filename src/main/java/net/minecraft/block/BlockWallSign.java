package net.minecraft.block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;


public class BlockWallSign extends BlockSign {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    protected static final AxisAlignedBB SIGN_EAST_AABB = new AxisAlignedBB(0.0D, 0.28125D, 0.0D, 0.125D, 0.78125D, 1.0D);
    protected static final AxisAlignedBB SIGN_WEST_AABB = new AxisAlignedBB(0.875D, 0.28125D, 0.0D, 1.0D, 0.78125D, 1.0D);
    protected static final AxisAlignedBB SIGN_SOUTH_AABB = new AxisAlignedBB(0.0D, 0.28125D, 0.0D, 1.0D, 0.78125D, 0.125D);
    protected static final AxisAlignedBB SIGN_NORTH_AABB = new AxisAlignedBB(0.0D, 0.28125D, 0.875D, 1.0D, 0.78125D, 1.0D);

    public BlockWallSign() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockWallSign.FACING, EnumFacing.NORTH));
    }

    public AxisAlignedBB getBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        switch ((EnumFacing) iblockdata.getValue(BlockWallSign.FACING)) {
        case NORTH:
        default:
            return BlockWallSign.SIGN_NORTH_AABB;

        case SOUTH:
            return BlockWallSign.SIGN_SOUTH_AABB;

        case WEST:
            return BlockWallSign.SIGN_WEST_AABB;

        case EAST:
            return BlockWallSign.SIGN_EAST_AABB;
        }
    }

    public void neighborChanged(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        EnumFacing enumdirection = (EnumFacing) iblockdata.getValue(BlockWallSign.FACING);

        if (!world.getBlockState(blockposition.offset(enumdirection.getOpposite())).getMaterial().isSolid()) {
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

        return this.getDefaultState().withProperty(BlockWallSign.FACING, enumdirection);
    }

    public int getMetaFromState(IBlockState iblockdata) {
        return ((EnumFacing) iblockdata.getValue(BlockWallSign.FACING)).getIndex();
    }

    public IBlockState withRotation(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.withProperty(BlockWallSign.FACING, enumblockrotation.rotate((EnumFacing) iblockdata.getValue(BlockWallSign.FACING)));
    }

    public IBlockState withMirror(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata.withRotation(enumblockmirror.toRotation((EnumFacing) iblockdata.getValue(BlockWallSign.FACING)));
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockWallSign.FACING});
    }
}
