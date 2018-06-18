package net.minecraft.block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;


public class BlockEndRod extends BlockDirectional {

    protected static final AxisAlignedBB END_ROD_VERTICAL_AABB = new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 1.0D, 0.625D);
    protected static final AxisAlignedBB END_ROD_NS_AABB = new AxisAlignedBB(0.375D, 0.375D, 0.0D, 0.625D, 0.625D, 1.0D);
    protected static final AxisAlignedBB END_ROD_EW_AABB = new AxisAlignedBB(0.0D, 0.375D, 0.375D, 1.0D, 0.625D, 0.625D);

    protected BlockEndRod() {
        super(Material.CIRCUITS);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockEndRod.FACING, EnumFacing.UP));
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }

    public IBlockState withRotation(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.withProperty(BlockEndRod.FACING, enumblockrotation.rotate((EnumFacing) iblockdata.getValue(BlockEndRod.FACING)));
    }

    public IBlockState withMirror(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata.withProperty(BlockEndRod.FACING, enumblockmirror.mirror((EnumFacing) iblockdata.getValue(BlockEndRod.FACING)));
    }

    public AxisAlignedBB getBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        switch (((EnumFacing) iblockdata.getValue(BlockEndRod.FACING)).getAxis()) {
        case X:
        default:
            return BlockEndRod.END_ROD_EW_AABB;

        case Z:
            return BlockEndRod.END_ROD_NS_AABB;

        case Y:
            return BlockEndRod.END_ROD_VERTICAL_AABB;
        }
    }

    public boolean isOpaqueCube(IBlockState iblockdata) {
        return false;
    }

    public boolean isFullCube(IBlockState iblockdata) {
        return false;
    }

    public boolean canPlaceBlockAt(World world, BlockPos blockposition) {
        return true;
    }

    public IBlockState getStateForPlacement(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        IBlockState iblockdata = world.getBlockState(blockposition.offset(enumdirection.getOpposite()));

        if (iblockdata.getBlock() == Blocks.END_ROD) {
            EnumFacing enumdirection1 = (EnumFacing) iblockdata.getValue(BlockEndRod.FACING);

            if (enumdirection1 == enumdirection) {
                return this.getDefaultState().withProperty(BlockEndRod.FACING, enumdirection.getOpposite());
            }
        }

        return this.getDefaultState().withProperty(BlockEndRod.FACING, enumdirection);
    }

    public IBlockState getStateFromMeta(int i) {
        IBlockState iblockdata = this.getDefaultState();

        iblockdata = iblockdata.withProperty(BlockEndRod.FACING, EnumFacing.getFront(i));
        return iblockdata;
    }

    public int getMetaFromState(IBlockState iblockdata) {
        return ((EnumFacing) iblockdata.getValue(BlockEndRod.FACING)).getIndex();
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockEndRod.FACING});
    }

    public EnumPushReaction getMobilityFlag(IBlockState iblockdata) {
        return EnumPushReaction.NORMAL;
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }
}
