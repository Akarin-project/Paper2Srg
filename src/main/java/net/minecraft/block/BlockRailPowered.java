package net.minecraft.block;

import com.google.common.base.Predicate;
import javax.annotation.Nullable;


import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.event.CraftEventFactory;

public class BlockRailPowered extends BlockRailBase {

    public static final PropertyEnum<BlockRailBase.EnumRailDirection> SHAPE = PropertyEnum.create("shape", BlockRailBase.EnumRailDirection.class, new Predicate() {
        public boolean a(@Nullable BlockRailBase.EnumRailDirection blockminecarttrackabstract_enumtrackposition) {
            return blockminecarttrackabstract_enumtrackposition != BlockRailBase.EnumRailDirection.NORTH_EAST && blockminecarttrackabstract_enumtrackposition != BlockRailBase.EnumRailDirection.NORTH_WEST && blockminecarttrackabstract_enumtrackposition != BlockRailBase.EnumRailDirection.SOUTH_EAST && blockminecarttrackabstract_enumtrackposition != BlockRailBase.EnumRailDirection.SOUTH_WEST;
        }

        public boolean apply(@Nullable Object object) {
            return this.a((BlockRailBase.EnumRailDirection) object);
        }
    });
    public static final PropertyBool POWERED = PropertyBool.create("powered");

    protected BlockRailPowered() {
        super(true);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockRailPowered.SHAPE, BlockRailBase.EnumRailDirection.NORTH_SOUTH).withProperty(BlockRailPowered.POWERED, Boolean.valueOf(false)));
    }

    protected boolean findPoweredRailSignal(World world, BlockPos blockposition, IBlockState iblockdata, boolean flag, int i) {
        if (i >= 8) {
            return false;
        } else {
            int j = blockposition.getX();
            int k = blockposition.getY();
            int l = blockposition.getZ();
            boolean flag1 = true;
            BlockRailBase.EnumRailDirection blockminecarttrackabstract_enumtrackposition = (BlockRailBase.EnumRailDirection) iblockdata.getValue(BlockRailPowered.SHAPE);

            switch (blockminecarttrackabstract_enumtrackposition) {
            case NORTH_SOUTH:
                if (flag) {
                    ++l;
                } else {
                    --l;
                }
                break;

            case EAST_WEST:
                if (flag) {
                    --j;
                } else {
                    ++j;
                }
                break;

            case ASCENDING_EAST:
                if (flag) {
                    --j;
                } else {
                    ++j;
                    ++k;
                    flag1 = false;
                }

                blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.EAST_WEST;
                break;

            case ASCENDING_WEST:
                if (flag) {
                    --j;
                    ++k;
                    flag1 = false;
                } else {
                    ++j;
                }

                blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.EAST_WEST;
                break;

            case ASCENDING_NORTH:
                if (flag) {
                    ++l;
                } else {
                    --l;
                    ++k;
                    flag1 = false;
                }

                blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
                break;

            case ASCENDING_SOUTH:
                if (flag) {
                    ++l;
                    ++k;
                    flag1 = false;
                } else {
                    --l;
                }

                blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
            }

            return this.isSameRailWithPower(world, new BlockPos(j, k, l), flag, i, blockminecarttrackabstract_enumtrackposition) ? true : flag1 && this.isSameRailWithPower(world, new BlockPos(j, k - 1, l), flag, i, blockminecarttrackabstract_enumtrackposition);
        }
    }

    protected boolean isSameRailWithPower(World world, BlockPos blockposition, boolean flag, int i, BlockRailBase.EnumRailDirection blockminecarttrackabstract_enumtrackposition) {
        IBlockState iblockdata = world.getBlockState(blockposition);

        if (iblockdata.getBlock() != this) {
            return false;
        } else {
            BlockRailBase.EnumRailDirection blockminecarttrackabstract_enumtrackposition1 = (BlockRailBase.EnumRailDirection) iblockdata.getValue(BlockRailPowered.SHAPE);

            return blockminecarttrackabstract_enumtrackposition == BlockRailBase.EnumRailDirection.EAST_WEST && (blockminecarttrackabstract_enumtrackposition1 == BlockRailBase.EnumRailDirection.NORTH_SOUTH || blockminecarttrackabstract_enumtrackposition1 == BlockRailBase.EnumRailDirection.ASCENDING_NORTH || blockminecarttrackabstract_enumtrackposition1 == BlockRailBase.EnumRailDirection.ASCENDING_SOUTH) ? false : (blockminecarttrackabstract_enumtrackposition == BlockRailBase.EnumRailDirection.NORTH_SOUTH && (blockminecarttrackabstract_enumtrackposition1 == BlockRailBase.EnumRailDirection.EAST_WEST || blockminecarttrackabstract_enumtrackposition1 == BlockRailBase.EnumRailDirection.ASCENDING_EAST || blockminecarttrackabstract_enumtrackposition1 == BlockRailBase.EnumRailDirection.ASCENDING_WEST) ? false : (((Boolean) iblockdata.getValue(BlockRailPowered.POWERED)).booleanValue() ? (world.isBlockPowered(blockposition) ? true : this.findPoweredRailSignal(world, blockposition, iblockdata, flag, i + 1)) : false));
        }
    }

    protected void updateState(IBlockState iblockdata, World world, BlockPos blockposition, Block block) {
        boolean flag = ((Boolean) iblockdata.getValue(BlockRailPowered.POWERED)).booleanValue();
        boolean flag1 = world.isBlockPowered(blockposition) || this.findPoweredRailSignal(world, blockposition, iblockdata, true, 0) || this.findPoweredRailSignal(world, blockposition, iblockdata, false, 0);

        if (flag1 != flag) {
            // CraftBukkit start
            int power = (Boolean)iblockdata.getValue(POWERED) ? 15 : 0;
            int newPower = CraftEventFactory.callRedstoneChange(world, blockposition.getX(), blockposition.getY(), blockposition.getZ(), power, 15 - power).getNewCurrent();
            if (newPower == power) {
                return;
            }
            // CraftBukkit end
            world.setBlockState(blockposition, iblockdata.withProperty(BlockRailPowered.POWERED, Boolean.valueOf(flag1)), 3);
            world.notifyNeighborsOfStateChange(blockposition.down(), this, false);
            if (((BlockRailBase.EnumRailDirection) iblockdata.getValue(BlockRailPowered.SHAPE)).isAscending()) {
                world.notifyNeighborsOfStateChange(blockposition.up(), this, false);
            }
        }

    }

    public IProperty<BlockRailBase.EnumRailDirection> getShapeProperty() {
        return BlockRailPowered.SHAPE;
    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockRailPowered.SHAPE, BlockRailBase.EnumRailDirection.byMetadata(i & 7)).withProperty(BlockRailPowered.POWERED, Boolean.valueOf((i & 8) > 0));
    }

    public int getMetaFromState(IBlockState iblockdata) {
        byte b0 = 0;
        int i = b0 | ((BlockRailBase.EnumRailDirection) iblockdata.getValue(BlockRailPowered.SHAPE)).getMetadata();

        if (((Boolean) iblockdata.getValue(BlockRailPowered.POWERED)).booleanValue()) {
            i |= 8;
        }

        return i;
    }

    public IBlockState withRotation(IBlockState iblockdata, Rotation enumblockrotation) {
        switch (enumblockrotation) {
        case CLOCKWISE_180:
            switch ((BlockRailBase.EnumRailDirection) iblockdata.getValue(BlockRailPowered.SHAPE)) {
            case ASCENDING_EAST:
                return iblockdata.withProperty(BlockRailPowered.SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_WEST);

            case ASCENDING_WEST:
                return iblockdata.withProperty(BlockRailPowered.SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_EAST);

            case ASCENDING_NORTH:
                return iblockdata.withProperty(BlockRailPowered.SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_SOUTH);

            case ASCENDING_SOUTH:
                return iblockdata.withProperty(BlockRailPowered.SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_NORTH);

            case SOUTH_EAST:
                return iblockdata.withProperty(BlockRailPowered.SHAPE, BlockRailBase.EnumRailDirection.NORTH_WEST);

            case SOUTH_WEST:
                return iblockdata.withProperty(BlockRailPowered.SHAPE, BlockRailBase.EnumRailDirection.NORTH_EAST);

            case NORTH_WEST:
                return iblockdata.withProperty(BlockRailPowered.SHAPE, BlockRailBase.EnumRailDirection.SOUTH_EAST);

            case NORTH_EAST:
                return iblockdata.withProperty(BlockRailPowered.SHAPE, BlockRailBase.EnumRailDirection.SOUTH_WEST);
            }

        case COUNTERCLOCKWISE_90:
            switch ((BlockRailBase.EnumRailDirection) iblockdata.getValue(BlockRailPowered.SHAPE)) {
            case NORTH_SOUTH:
                return iblockdata.withProperty(BlockRailPowered.SHAPE, BlockRailBase.EnumRailDirection.EAST_WEST);

            case EAST_WEST:
                return iblockdata.withProperty(BlockRailPowered.SHAPE, BlockRailBase.EnumRailDirection.NORTH_SOUTH);

            case ASCENDING_EAST:
                return iblockdata.withProperty(BlockRailPowered.SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_NORTH);

            case ASCENDING_WEST:
                return iblockdata.withProperty(BlockRailPowered.SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_SOUTH);

            case ASCENDING_NORTH:
                return iblockdata.withProperty(BlockRailPowered.SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_WEST);

            case ASCENDING_SOUTH:
                return iblockdata.withProperty(BlockRailPowered.SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_EAST);

            case SOUTH_EAST:
                return iblockdata.withProperty(BlockRailPowered.SHAPE, BlockRailBase.EnumRailDirection.NORTH_EAST);

            case SOUTH_WEST:
                return iblockdata.withProperty(BlockRailPowered.SHAPE, BlockRailBase.EnumRailDirection.SOUTH_EAST);

            case NORTH_WEST:
                return iblockdata.withProperty(BlockRailPowered.SHAPE, BlockRailBase.EnumRailDirection.SOUTH_WEST);

            case NORTH_EAST:
                return iblockdata.withProperty(BlockRailPowered.SHAPE, BlockRailBase.EnumRailDirection.NORTH_WEST);
            }

        case CLOCKWISE_90:
            switch ((BlockRailBase.EnumRailDirection) iblockdata.getValue(BlockRailPowered.SHAPE)) {
            case NORTH_SOUTH:
                return iblockdata.withProperty(BlockRailPowered.SHAPE, BlockRailBase.EnumRailDirection.EAST_WEST);

            case EAST_WEST:
                return iblockdata.withProperty(BlockRailPowered.SHAPE, BlockRailBase.EnumRailDirection.NORTH_SOUTH);

            case ASCENDING_EAST:
                return iblockdata.withProperty(BlockRailPowered.SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_SOUTH);

            case ASCENDING_WEST:
                return iblockdata.withProperty(BlockRailPowered.SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_NORTH);

            case ASCENDING_NORTH:
                return iblockdata.withProperty(BlockRailPowered.SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_EAST);

            case ASCENDING_SOUTH:
                return iblockdata.withProperty(BlockRailPowered.SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_WEST);

            case SOUTH_EAST:
                return iblockdata.withProperty(BlockRailPowered.SHAPE, BlockRailBase.EnumRailDirection.SOUTH_WEST);

            case SOUTH_WEST:
                return iblockdata.withProperty(BlockRailPowered.SHAPE, BlockRailBase.EnumRailDirection.NORTH_WEST);

            case NORTH_WEST:
                return iblockdata.withProperty(BlockRailPowered.SHAPE, BlockRailBase.EnumRailDirection.NORTH_EAST);

            case NORTH_EAST:
                return iblockdata.withProperty(BlockRailPowered.SHAPE, BlockRailBase.EnumRailDirection.SOUTH_EAST);
            }

        default:
            return iblockdata;
        }
    }

    public IBlockState withMirror(IBlockState iblockdata, Mirror enumblockmirror) {
        BlockRailBase.EnumRailDirection blockminecarttrackabstract_enumtrackposition = (BlockRailBase.EnumRailDirection) iblockdata.getValue(BlockRailPowered.SHAPE);

        switch (enumblockmirror) {
        case LEFT_RIGHT:
            switch (blockminecarttrackabstract_enumtrackposition) {
            case ASCENDING_NORTH:
                return iblockdata.withProperty(BlockRailPowered.SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_SOUTH);

            case ASCENDING_SOUTH:
                return iblockdata.withProperty(BlockRailPowered.SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_NORTH);

            case SOUTH_EAST:
                return iblockdata.withProperty(BlockRailPowered.SHAPE, BlockRailBase.EnumRailDirection.NORTH_EAST);

            case SOUTH_WEST:
                return iblockdata.withProperty(BlockRailPowered.SHAPE, BlockRailBase.EnumRailDirection.NORTH_WEST);

            case NORTH_WEST:
                return iblockdata.withProperty(BlockRailPowered.SHAPE, BlockRailBase.EnumRailDirection.SOUTH_WEST);

            case NORTH_EAST:
                return iblockdata.withProperty(BlockRailPowered.SHAPE, BlockRailBase.EnumRailDirection.SOUTH_EAST);

            default:
                return super.withMirror(iblockdata, enumblockmirror);
            }

        case FRONT_BACK:
            switch (blockminecarttrackabstract_enumtrackposition) {
            case ASCENDING_EAST:
                return iblockdata.withProperty(BlockRailPowered.SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_WEST);

            case ASCENDING_WEST:
                return iblockdata.withProperty(BlockRailPowered.SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_EAST);

            case ASCENDING_NORTH:
            case ASCENDING_SOUTH:
            default:
                break;

            case SOUTH_EAST:
                return iblockdata.withProperty(BlockRailPowered.SHAPE, BlockRailBase.EnumRailDirection.SOUTH_WEST);

            case SOUTH_WEST:
                return iblockdata.withProperty(BlockRailPowered.SHAPE, BlockRailBase.EnumRailDirection.SOUTH_EAST);

            case NORTH_WEST:
                return iblockdata.withProperty(BlockRailPowered.SHAPE, BlockRailBase.EnumRailDirection.NORTH_EAST);

            case NORTH_EAST:
                return iblockdata.withProperty(BlockRailPowered.SHAPE, BlockRailBase.EnumRailDirection.NORTH_WEST);
            }
        }

        return super.withMirror(iblockdata, enumblockmirror);
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockRailPowered.SHAPE, BlockRailPowered.POWERED});
    }
}
