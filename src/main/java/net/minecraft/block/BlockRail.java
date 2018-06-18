package net.minecraft.block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class BlockRail extends BlockRailBase {

    public static final PropertyEnum<BlockRailBase.EnumRailDirection> SHAPE = PropertyEnum.create("shape", BlockRailBase.EnumRailDirection.class);

    protected BlockRail() {
        super(false);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockRail.SHAPE, BlockRailBase.EnumRailDirection.NORTH_SOUTH));
    }

    protected void updateState(IBlockState iblockdata, World world, BlockPos blockposition, Block block) {
        if (block.getDefaultState().canProvidePower() && (new BlockRailBase.Rail(world, blockposition, iblockdata)).countAdjacentRails() == 3) {
            this.updateDir(world, blockposition, iblockdata, false);
        }

    }

    public IProperty<BlockRailBase.EnumRailDirection> getShapeProperty() {
        return BlockRail.SHAPE;
    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockRail.SHAPE, BlockRailBase.EnumRailDirection.byMetadata(i));
    }

    public int getMetaFromState(IBlockState iblockdata) {
        return ((BlockRailBase.EnumRailDirection) iblockdata.getValue(BlockRail.SHAPE)).getMetadata();
    }

    public IBlockState withRotation(IBlockState iblockdata, Rotation enumblockrotation) {
        switch (enumblockrotation) {
        case CLOCKWISE_180:
            switch ((BlockRailBase.EnumRailDirection) iblockdata.getValue(BlockRail.SHAPE)) {
            case ASCENDING_EAST:
                return iblockdata.withProperty(BlockRail.SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_WEST);

            case ASCENDING_WEST:
                return iblockdata.withProperty(BlockRail.SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_EAST);

            case ASCENDING_NORTH:
                return iblockdata.withProperty(BlockRail.SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_SOUTH);

            case ASCENDING_SOUTH:
                return iblockdata.withProperty(BlockRail.SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_NORTH);

            case SOUTH_EAST:
                return iblockdata.withProperty(BlockRail.SHAPE, BlockRailBase.EnumRailDirection.NORTH_WEST);

            case SOUTH_WEST:
                return iblockdata.withProperty(BlockRail.SHAPE, BlockRailBase.EnumRailDirection.NORTH_EAST);

            case NORTH_WEST:
                return iblockdata.withProperty(BlockRail.SHAPE, BlockRailBase.EnumRailDirection.SOUTH_EAST);

            case NORTH_EAST:
                return iblockdata.withProperty(BlockRail.SHAPE, BlockRailBase.EnumRailDirection.SOUTH_WEST);
            }

        case COUNTERCLOCKWISE_90:
            switch ((BlockRailBase.EnumRailDirection) iblockdata.getValue(BlockRail.SHAPE)) {
            case ASCENDING_EAST:
                return iblockdata.withProperty(BlockRail.SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_NORTH);

            case ASCENDING_WEST:
                return iblockdata.withProperty(BlockRail.SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_SOUTH);

            case ASCENDING_NORTH:
                return iblockdata.withProperty(BlockRail.SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_WEST);

            case ASCENDING_SOUTH:
                return iblockdata.withProperty(BlockRail.SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_EAST);

            case SOUTH_EAST:
                return iblockdata.withProperty(BlockRail.SHAPE, BlockRailBase.EnumRailDirection.NORTH_EAST);

            case SOUTH_WEST:
                return iblockdata.withProperty(BlockRail.SHAPE, BlockRailBase.EnumRailDirection.SOUTH_EAST);

            case NORTH_WEST:
                return iblockdata.withProperty(BlockRail.SHAPE, BlockRailBase.EnumRailDirection.SOUTH_WEST);

            case NORTH_EAST:
                return iblockdata.withProperty(BlockRail.SHAPE, BlockRailBase.EnumRailDirection.NORTH_WEST);

            case NORTH_SOUTH:
                return iblockdata.withProperty(BlockRail.SHAPE, BlockRailBase.EnumRailDirection.EAST_WEST);

            case EAST_WEST:
                return iblockdata.withProperty(BlockRail.SHAPE, BlockRailBase.EnumRailDirection.NORTH_SOUTH);
            }

        case CLOCKWISE_90:
            switch ((BlockRailBase.EnumRailDirection) iblockdata.getValue(BlockRail.SHAPE)) {
            case ASCENDING_EAST:
                return iblockdata.withProperty(BlockRail.SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_SOUTH);

            case ASCENDING_WEST:
                return iblockdata.withProperty(BlockRail.SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_NORTH);

            case ASCENDING_NORTH:
                return iblockdata.withProperty(BlockRail.SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_EAST);

            case ASCENDING_SOUTH:
                return iblockdata.withProperty(BlockRail.SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_WEST);

            case SOUTH_EAST:
                return iblockdata.withProperty(BlockRail.SHAPE, BlockRailBase.EnumRailDirection.SOUTH_WEST);

            case SOUTH_WEST:
                return iblockdata.withProperty(BlockRail.SHAPE, BlockRailBase.EnumRailDirection.NORTH_WEST);

            case NORTH_WEST:
                return iblockdata.withProperty(BlockRail.SHAPE, BlockRailBase.EnumRailDirection.NORTH_EAST);

            case NORTH_EAST:
                return iblockdata.withProperty(BlockRail.SHAPE, BlockRailBase.EnumRailDirection.SOUTH_EAST);

            case NORTH_SOUTH:
                return iblockdata.withProperty(BlockRail.SHAPE, BlockRailBase.EnumRailDirection.EAST_WEST);

            case EAST_WEST:
                return iblockdata.withProperty(BlockRail.SHAPE, BlockRailBase.EnumRailDirection.NORTH_SOUTH);
            }

        default:
            return iblockdata;
        }
    }

    public IBlockState withMirror(IBlockState iblockdata, Mirror enumblockmirror) {
        BlockRailBase.EnumRailDirection blockminecarttrackabstract_enumtrackposition = (BlockRailBase.EnumRailDirection) iblockdata.getValue(BlockRail.SHAPE);

        switch (enumblockmirror) {
        case LEFT_RIGHT:
            switch (blockminecarttrackabstract_enumtrackposition) {
            case ASCENDING_NORTH:
                return iblockdata.withProperty(BlockRail.SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_SOUTH);

            case ASCENDING_SOUTH:
                return iblockdata.withProperty(BlockRail.SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_NORTH);

            case SOUTH_EAST:
                return iblockdata.withProperty(BlockRail.SHAPE, BlockRailBase.EnumRailDirection.NORTH_EAST);

            case SOUTH_WEST:
                return iblockdata.withProperty(BlockRail.SHAPE, BlockRailBase.EnumRailDirection.NORTH_WEST);

            case NORTH_WEST:
                return iblockdata.withProperty(BlockRail.SHAPE, BlockRailBase.EnumRailDirection.SOUTH_WEST);

            case NORTH_EAST:
                return iblockdata.withProperty(BlockRail.SHAPE, BlockRailBase.EnumRailDirection.SOUTH_EAST);

            default:
                return super.withMirror(iblockdata, enumblockmirror);
            }

        case FRONT_BACK:
            switch (blockminecarttrackabstract_enumtrackposition) {
            case ASCENDING_EAST:
                return iblockdata.withProperty(BlockRail.SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_WEST);

            case ASCENDING_WEST:
                return iblockdata.withProperty(BlockRail.SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_EAST);

            case ASCENDING_NORTH:
            case ASCENDING_SOUTH:
            default:
                break;

            case SOUTH_EAST:
                return iblockdata.withProperty(BlockRail.SHAPE, BlockRailBase.EnumRailDirection.SOUTH_WEST);

            case SOUTH_WEST:
                return iblockdata.withProperty(BlockRail.SHAPE, BlockRailBase.EnumRailDirection.SOUTH_EAST);

            case NORTH_WEST:
                return iblockdata.withProperty(BlockRail.SHAPE, BlockRailBase.EnumRailDirection.NORTH_EAST);

            case NORTH_EAST:
                return iblockdata.withProperty(BlockRail.SHAPE, BlockRailBase.EnumRailDirection.NORTH_WEST);
            }
        }

        return super.withMirror(iblockdata, enumblockmirror);
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockRail.SHAPE});
    }
}
