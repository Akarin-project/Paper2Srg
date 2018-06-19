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

    public static final PropertyEnum<BlockRailBase.EnumRailDirection> field_176565_b = PropertyEnum.func_177709_a("shape", BlockRailBase.EnumRailDirection.class);

    protected BlockRail() {
        super(false);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockRail.field_176565_b, BlockRailBase.EnumRailDirection.NORTH_SOUTH));
    }

    protected void func_189541_b(IBlockState iblockdata, World world, BlockPos blockposition, Block block) {
        if (block.func_176223_P().func_185897_m() && (new BlockRailBase.Rail(world, blockposition, iblockdata)).func_150650_a() == 3) {
            this.func_176564_a(world, blockposition, iblockdata, false);
        }

    }

    public IProperty<BlockRailBase.EnumRailDirection> func_176560_l() {
        return BlockRail.field_176565_b;
    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockRail.field_176565_b, BlockRailBase.EnumRailDirection.func_177016_a(i));
    }

    public int func_176201_c(IBlockState iblockdata) {
        return ((BlockRailBase.EnumRailDirection) iblockdata.func_177229_b(BlockRail.field_176565_b)).func_177015_a();
    }

    public IBlockState func_185499_a(IBlockState iblockdata, Rotation enumblockrotation) {
        switch (enumblockrotation) {
        case CLOCKWISE_180:
            switch ((BlockRailBase.EnumRailDirection) iblockdata.func_177229_b(BlockRail.field_176565_b)) {
            case ASCENDING_EAST:
                return iblockdata.func_177226_a(BlockRail.field_176565_b, BlockRailBase.EnumRailDirection.ASCENDING_WEST);

            case ASCENDING_WEST:
                return iblockdata.func_177226_a(BlockRail.field_176565_b, BlockRailBase.EnumRailDirection.ASCENDING_EAST);

            case ASCENDING_NORTH:
                return iblockdata.func_177226_a(BlockRail.field_176565_b, BlockRailBase.EnumRailDirection.ASCENDING_SOUTH);

            case ASCENDING_SOUTH:
                return iblockdata.func_177226_a(BlockRail.field_176565_b, BlockRailBase.EnumRailDirection.ASCENDING_NORTH);

            case SOUTH_EAST:
                return iblockdata.func_177226_a(BlockRail.field_176565_b, BlockRailBase.EnumRailDirection.NORTH_WEST);

            case SOUTH_WEST:
                return iblockdata.func_177226_a(BlockRail.field_176565_b, BlockRailBase.EnumRailDirection.NORTH_EAST);

            case NORTH_WEST:
                return iblockdata.func_177226_a(BlockRail.field_176565_b, BlockRailBase.EnumRailDirection.SOUTH_EAST);

            case NORTH_EAST:
                return iblockdata.func_177226_a(BlockRail.field_176565_b, BlockRailBase.EnumRailDirection.SOUTH_WEST);
            }

        case COUNTERCLOCKWISE_90:
            switch ((BlockRailBase.EnumRailDirection) iblockdata.func_177229_b(BlockRail.field_176565_b)) {
            case ASCENDING_EAST:
                return iblockdata.func_177226_a(BlockRail.field_176565_b, BlockRailBase.EnumRailDirection.ASCENDING_NORTH);

            case ASCENDING_WEST:
                return iblockdata.func_177226_a(BlockRail.field_176565_b, BlockRailBase.EnumRailDirection.ASCENDING_SOUTH);

            case ASCENDING_NORTH:
                return iblockdata.func_177226_a(BlockRail.field_176565_b, BlockRailBase.EnumRailDirection.ASCENDING_WEST);

            case ASCENDING_SOUTH:
                return iblockdata.func_177226_a(BlockRail.field_176565_b, BlockRailBase.EnumRailDirection.ASCENDING_EAST);

            case SOUTH_EAST:
                return iblockdata.func_177226_a(BlockRail.field_176565_b, BlockRailBase.EnumRailDirection.NORTH_EAST);

            case SOUTH_WEST:
                return iblockdata.func_177226_a(BlockRail.field_176565_b, BlockRailBase.EnumRailDirection.SOUTH_EAST);

            case NORTH_WEST:
                return iblockdata.func_177226_a(BlockRail.field_176565_b, BlockRailBase.EnumRailDirection.SOUTH_WEST);

            case NORTH_EAST:
                return iblockdata.func_177226_a(BlockRail.field_176565_b, BlockRailBase.EnumRailDirection.NORTH_WEST);

            case NORTH_SOUTH:
                return iblockdata.func_177226_a(BlockRail.field_176565_b, BlockRailBase.EnumRailDirection.EAST_WEST);

            case EAST_WEST:
                return iblockdata.func_177226_a(BlockRail.field_176565_b, BlockRailBase.EnumRailDirection.NORTH_SOUTH);
            }

        case CLOCKWISE_90:
            switch ((BlockRailBase.EnumRailDirection) iblockdata.func_177229_b(BlockRail.field_176565_b)) {
            case ASCENDING_EAST:
                return iblockdata.func_177226_a(BlockRail.field_176565_b, BlockRailBase.EnumRailDirection.ASCENDING_SOUTH);

            case ASCENDING_WEST:
                return iblockdata.func_177226_a(BlockRail.field_176565_b, BlockRailBase.EnumRailDirection.ASCENDING_NORTH);

            case ASCENDING_NORTH:
                return iblockdata.func_177226_a(BlockRail.field_176565_b, BlockRailBase.EnumRailDirection.ASCENDING_EAST);

            case ASCENDING_SOUTH:
                return iblockdata.func_177226_a(BlockRail.field_176565_b, BlockRailBase.EnumRailDirection.ASCENDING_WEST);

            case SOUTH_EAST:
                return iblockdata.func_177226_a(BlockRail.field_176565_b, BlockRailBase.EnumRailDirection.SOUTH_WEST);

            case SOUTH_WEST:
                return iblockdata.func_177226_a(BlockRail.field_176565_b, BlockRailBase.EnumRailDirection.NORTH_WEST);

            case NORTH_WEST:
                return iblockdata.func_177226_a(BlockRail.field_176565_b, BlockRailBase.EnumRailDirection.NORTH_EAST);

            case NORTH_EAST:
                return iblockdata.func_177226_a(BlockRail.field_176565_b, BlockRailBase.EnumRailDirection.SOUTH_EAST);

            case NORTH_SOUTH:
                return iblockdata.func_177226_a(BlockRail.field_176565_b, BlockRailBase.EnumRailDirection.EAST_WEST);

            case EAST_WEST:
                return iblockdata.func_177226_a(BlockRail.field_176565_b, BlockRailBase.EnumRailDirection.NORTH_SOUTH);
            }

        default:
            return iblockdata;
        }
    }

    public IBlockState func_185471_a(IBlockState iblockdata, Mirror enumblockmirror) {
        BlockRailBase.EnumRailDirection blockminecarttrackabstract_enumtrackposition = (BlockRailBase.EnumRailDirection) iblockdata.func_177229_b(BlockRail.field_176565_b);

        switch (enumblockmirror) {
        case LEFT_RIGHT:
            switch (blockminecarttrackabstract_enumtrackposition) {
            case ASCENDING_NORTH:
                return iblockdata.func_177226_a(BlockRail.field_176565_b, BlockRailBase.EnumRailDirection.ASCENDING_SOUTH);

            case ASCENDING_SOUTH:
                return iblockdata.func_177226_a(BlockRail.field_176565_b, BlockRailBase.EnumRailDirection.ASCENDING_NORTH);

            case SOUTH_EAST:
                return iblockdata.func_177226_a(BlockRail.field_176565_b, BlockRailBase.EnumRailDirection.NORTH_EAST);

            case SOUTH_WEST:
                return iblockdata.func_177226_a(BlockRail.field_176565_b, BlockRailBase.EnumRailDirection.NORTH_WEST);

            case NORTH_WEST:
                return iblockdata.func_177226_a(BlockRail.field_176565_b, BlockRailBase.EnumRailDirection.SOUTH_WEST);

            case NORTH_EAST:
                return iblockdata.func_177226_a(BlockRail.field_176565_b, BlockRailBase.EnumRailDirection.SOUTH_EAST);

            default:
                return super.func_185471_a(iblockdata, enumblockmirror);
            }

        case FRONT_BACK:
            switch (blockminecarttrackabstract_enumtrackposition) {
            case ASCENDING_EAST:
                return iblockdata.func_177226_a(BlockRail.field_176565_b, BlockRailBase.EnumRailDirection.ASCENDING_WEST);

            case ASCENDING_WEST:
                return iblockdata.func_177226_a(BlockRail.field_176565_b, BlockRailBase.EnumRailDirection.ASCENDING_EAST);

            case ASCENDING_NORTH:
            case ASCENDING_SOUTH:
            default:
                break;

            case SOUTH_EAST:
                return iblockdata.func_177226_a(BlockRail.field_176565_b, BlockRailBase.EnumRailDirection.SOUTH_WEST);

            case SOUTH_WEST:
                return iblockdata.func_177226_a(BlockRail.field_176565_b, BlockRailBase.EnumRailDirection.SOUTH_EAST);

            case NORTH_WEST:
                return iblockdata.func_177226_a(BlockRail.field_176565_b, BlockRailBase.EnumRailDirection.NORTH_EAST);

            case NORTH_EAST:
                return iblockdata.func_177226_a(BlockRail.field_176565_b, BlockRailBase.EnumRailDirection.NORTH_WEST);
            }
        }

        return super.func_185471_a(iblockdata, enumblockmirror);
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockRail.field_176565_b});
    }
}
