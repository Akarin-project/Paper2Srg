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

    public static final PropertyEnum<BlockRailBase.EnumRailDirection> field_176568_b = PropertyEnum.func_177708_a("shape", BlockRailBase.EnumRailDirection.class, new Predicate() {
        public boolean a(@Nullable BlockRailBase.EnumRailDirection blockminecarttrackabstract_enumtrackposition) {
            return blockminecarttrackabstract_enumtrackposition != BlockRailBase.EnumRailDirection.NORTH_EAST && blockminecarttrackabstract_enumtrackposition != BlockRailBase.EnumRailDirection.NORTH_WEST && blockminecarttrackabstract_enumtrackposition != BlockRailBase.EnumRailDirection.SOUTH_EAST && blockminecarttrackabstract_enumtrackposition != BlockRailBase.EnumRailDirection.SOUTH_WEST;
        }

        public boolean apply(@Nullable Object object) {
            return this.a((BlockRailBase.EnumRailDirection) object);
        }
    });
    public static final PropertyBool field_176569_M = PropertyBool.func_177716_a("powered");

    protected BlockRailPowered() {
        super(true);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockRailPowered.field_176568_b, BlockRailBase.EnumRailDirection.NORTH_SOUTH).func_177226_a(BlockRailPowered.field_176569_M, Boolean.valueOf(false)));
    }

    protected boolean func_176566_a(World world, BlockPos blockposition, IBlockState iblockdata, boolean flag, int i) {
        if (i >= 8) {
            return false;
        } else {
            int j = blockposition.func_177958_n();
            int k = blockposition.func_177956_o();
            int l = blockposition.func_177952_p();
            boolean flag1 = true;
            BlockRailBase.EnumRailDirection blockminecarttrackabstract_enumtrackposition = (BlockRailBase.EnumRailDirection) iblockdata.func_177229_b(BlockRailPowered.field_176568_b);

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

            return this.func_176567_a(world, new BlockPos(j, k, l), flag, i, blockminecarttrackabstract_enumtrackposition) ? true : flag1 && this.func_176567_a(world, new BlockPos(j, k - 1, l), flag, i, blockminecarttrackabstract_enumtrackposition);
        }
    }

    protected boolean func_176567_a(World world, BlockPos blockposition, boolean flag, int i, BlockRailBase.EnumRailDirection blockminecarttrackabstract_enumtrackposition) {
        IBlockState iblockdata = world.func_180495_p(blockposition);

        if (iblockdata.func_177230_c() != this) {
            return false;
        } else {
            BlockRailBase.EnumRailDirection blockminecarttrackabstract_enumtrackposition1 = (BlockRailBase.EnumRailDirection) iblockdata.func_177229_b(BlockRailPowered.field_176568_b);

            return blockminecarttrackabstract_enumtrackposition == BlockRailBase.EnumRailDirection.EAST_WEST && (blockminecarttrackabstract_enumtrackposition1 == BlockRailBase.EnumRailDirection.NORTH_SOUTH || blockminecarttrackabstract_enumtrackposition1 == BlockRailBase.EnumRailDirection.ASCENDING_NORTH || blockminecarttrackabstract_enumtrackposition1 == BlockRailBase.EnumRailDirection.ASCENDING_SOUTH) ? false : (blockminecarttrackabstract_enumtrackposition == BlockRailBase.EnumRailDirection.NORTH_SOUTH && (blockminecarttrackabstract_enumtrackposition1 == BlockRailBase.EnumRailDirection.EAST_WEST || blockminecarttrackabstract_enumtrackposition1 == BlockRailBase.EnumRailDirection.ASCENDING_EAST || blockminecarttrackabstract_enumtrackposition1 == BlockRailBase.EnumRailDirection.ASCENDING_WEST) ? false : (((Boolean) iblockdata.func_177229_b(BlockRailPowered.field_176569_M)).booleanValue() ? (world.func_175640_z(blockposition) ? true : this.func_176566_a(world, blockposition, iblockdata, flag, i + 1)) : false));
        }
    }

    protected void func_189541_b(IBlockState iblockdata, World world, BlockPos blockposition, Block block) {
        boolean flag = ((Boolean) iblockdata.func_177229_b(BlockRailPowered.field_176569_M)).booleanValue();
        boolean flag1 = world.func_175640_z(blockposition) || this.func_176566_a(world, blockposition, iblockdata, true, 0) || this.func_176566_a(world, blockposition, iblockdata, false, 0);

        if (flag1 != flag) {
            // CraftBukkit start
            int power = (Boolean)iblockdata.func_177229_b(field_176569_M) ? 15 : 0;
            int newPower = CraftEventFactory.callRedstoneChange(world, blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p(), power, 15 - power).getNewCurrent();
            if (newPower == power) {
                return;
            }
            // CraftBukkit end
            world.func_180501_a(blockposition, iblockdata.func_177226_a(BlockRailPowered.field_176569_M, Boolean.valueOf(flag1)), 3);
            world.func_175685_c(blockposition.func_177977_b(), this, false);
            if (((BlockRailBase.EnumRailDirection) iblockdata.func_177229_b(BlockRailPowered.field_176568_b)).func_177018_c()) {
                world.func_175685_c(blockposition.func_177984_a(), this, false);
            }
        }

    }

    public IProperty<BlockRailBase.EnumRailDirection> func_176560_l() {
        return BlockRailPowered.field_176568_b;
    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockRailPowered.field_176568_b, BlockRailBase.EnumRailDirection.func_177016_a(i & 7)).func_177226_a(BlockRailPowered.field_176569_M, Boolean.valueOf((i & 8) > 0));
    }

    public int func_176201_c(IBlockState iblockdata) {
        byte b0 = 0;
        int i = b0 | ((BlockRailBase.EnumRailDirection) iblockdata.func_177229_b(BlockRailPowered.field_176568_b)).func_177015_a();

        if (((Boolean) iblockdata.func_177229_b(BlockRailPowered.field_176569_M)).booleanValue()) {
            i |= 8;
        }

        return i;
    }

    public IBlockState func_185499_a(IBlockState iblockdata, Rotation enumblockrotation) {
        switch (enumblockrotation) {
        case CLOCKWISE_180:
            switch ((BlockRailBase.EnumRailDirection) iblockdata.func_177229_b(BlockRailPowered.field_176568_b)) {
            case ASCENDING_EAST:
                return iblockdata.func_177226_a(BlockRailPowered.field_176568_b, BlockRailBase.EnumRailDirection.ASCENDING_WEST);

            case ASCENDING_WEST:
                return iblockdata.func_177226_a(BlockRailPowered.field_176568_b, BlockRailBase.EnumRailDirection.ASCENDING_EAST);

            case ASCENDING_NORTH:
                return iblockdata.func_177226_a(BlockRailPowered.field_176568_b, BlockRailBase.EnumRailDirection.ASCENDING_SOUTH);

            case ASCENDING_SOUTH:
                return iblockdata.func_177226_a(BlockRailPowered.field_176568_b, BlockRailBase.EnumRailDirection.ASCENDING_NORTH);

            case SOUTH_EAST:
                return iblockdata.func_177226_a(BlockRailPowered.field_176568_b, BlockRailBase.EnumRailDirection.NORTH_WEST);

            case SOUTH_WEST:
                return iblockdata.func_177226_a(BlockRailPowered.field_176568_b, BlockRailBase.EnumRailDirection.NORTH_EAST);

            case NORTH_WEST:
                return iblockdata.func_177226_a(BlockRailPowered.field_176568_b, BlockRailBase.EnumRailDirection.SOUTH_EAST);

            case NORTH_EAST:
                return iblockdata.func_177226_a(BlockRailPowered.field_176568_b, BlockRailBase.EnumRailDirection.SOUTH_WEST);
            }

        case COUNTERCLOCKWISE_90:
            switch ((BlockRailBase.EnumRailDirection) iblockdata.func_177229_b(BlockRailPowered.field_176568_b)) {
            case NORTH_SOUTH:
                return iblockdata.func_177226_a(BlockRailPowered.field_176568_b, BlockRailBase.EnumRailDirection.EAST_WEST);

            case EAST_WEST:
                return iblockdata.func_177226_a(BlockRailPowered.field_176568_b, BlockRailBase.EnumRailDirection.NORTH_SOUTH);

            case ASCENDING_EAST:
                return iblockdata.func_177226_a(BlockRailPowered.field_176568_b, BlockRailBase.EnumRailDirection.ASCENDING_NORTH);

            case ASCENDING_WEST:
                return iblockdata.func_177226_a(BlockRailPowered.field_176568_b, BlockRailBase.EnumRailDirection.ASCENDING_SOUTH);

            case ASCENDING_NORTH:
                return iblockdata.func_177226_a(BlockRailPowered.field_176568_b, BlockRailBase.EnumRailDirection.ASCENDING_WEST);

            case ASCENDING_SOUTH:
                return iblockdata.func_177226_a(BlockRailPowered.field_176568_b, BlockRailBase.EnumRailDirection.ASCENDING_EAST);

            case SOUTH_EAST:
                return iblockdata.func_177226_a(BlockRailPowered.field_176568_b, BlockRailBase.EnumRailDirection.NORTH_EAST);

            case SOUTH_WEST:
                return iblockdata.func_177226_a(BlockRailPowered.field_176568_b, BlockRailBase.EnumRailDirection.SOUTH_EAST);

            case NORTH_WEST:
                return iblockdata.func_177226_a(BlockRailPowered.field_176568_b, BlockRailBase.EnumRailDirection.SOUTH_WEST);

            case NORTH_EAST:
                return iblockdata.func_177226_a(BlockRailPowered.field_176568_b, BlockRailBase.EnumRailDirection.NORTH_WEST);
            }

        case CLOCKWISE_90:
            switch ((BlockRailBase.EnumRailDirection) iblockdata.func_177229_b(BlockRailPowered.field_176568_b)) {
            case NORTH_SOUTH:
                return iblockdata.func_177226_a(BlockRailPowered.field_176568_b, BlockRailBase.EnumRailDirection.EAST_WEST);

            case EAST_WEST:
                return iblockdata.func_177226_a(BlockRailPowered.field_176568_b, BlockRailBase.EnumRailDirection.NORTH_SOUTH);

            case ASCENDING_EAST:
                return iblockdata.func_177226_a(BlockRailPowered.field_176568_b, BlockRailBase.EnumRailDirection.ASCENDING_SOUTH);

            case ASCENDING_WEST:
                return iblockdata.func_177226_a(BlockRailPowered.field_176568_b, BlockRailBase.EnumRailDirection.ASCENDING_NORTH);

            case ASCENDING_NORTH:
                return iblockdata.func_177226_a(BlockRailPowered.field_176568_b, BlockRailBase.EnumRailDirection.ASCENDING_EAST);

            case ASCENDING_SOUTH:
                return iblockdata.func_177226_a(BlockRailPowered.field_176568_b, BlockRailBase.EnumRailDirection.ASCENDING_WEST);

            case SOUTH_EAST:
                return iblockdata.func_177226_a(BlockRailPowered.field_176568_b, BlockRailBase.EnumRailDirection.SOUTH_WEST);

            case SOUTH_WEST:
                return iblockdata.func_177226_a(BlockRailPowered.field_176568_b, BlockRailBase.EnumRailDirection.NORTH_WEST);

            case NORTH_WEST:
                return iblockdata.func_177226_a(BlockRailPowered.field_176568_b, BlockRailBase.EnumRailDirection.NORTH_EAST);

            case NORTH_EAST:
                return iblockdata.func_177226_a(BlockRailPowered.field_176568_b, BlockRailBase.EnumRailDirection.SOUTH_EAST);
            }

        default:
            return iblockdata;
        }
    }

    public IBlockState func_185471_a(IBlockState iblockdata, Mirror enumblockmirror) {
        BlockRailBase.EnumRailDirection blockminecarttrackabstract_enumtrackposition = (BlockRailBase.EnumRailDirection) iblockdata.func_177229_b(BlockRailPowered.field_176568_b);

        switch (enumblockmirror) {
        case LEFT_RIGHT:
            switch (blockminecarttrackabstract_enumtrackposition) {
            case ASCENDING_NORTH:
                return iblockdata.func_177226_a(BlockRailPowered.field_176568_b, BlockRailBase.EnumRailDirection.ASCENDING_SOUTH);

            case ASCENDING_SOUTH:
                return iblockdata.func_177226_a(BlockRailPowered.field_176568_b, BlockRailBase.EnumRailDirection.ASCENDING_NORTH);

            case SOUTH_EAST:
                return iblockdata.func_177226_a(BlockRailPowered.field_176568_b, BlockRailBase.EnumRailDirection.NORTH_EAST);

            case SOUTH_WEST:
                return iblockdata.func_177226_a(BlockRailPowered.field_176568_b, BlockRailBase.EnumRailDirection.NORTH_WEST);

            case NORTH_WEST:
                return iblockdata.func_177226_a(BlockRailPowered.field_176568_b, BlockRailBase.EnumRailDirection.SOUTH_WEST);

            case NORTH_EAST:
                return iblockdata.func_177226_a(BlockRailPowered.field_176568_b, BlockRailBase.EnumRailDirection.SOUTH_EAST);

            default:
                return super.func_185471_a(iblockdata, enumblockmirror);
            }

        case FRONT_BACK:
            switch (blockminecarttrackabstract_enumtrackposition) {
            case ASCENDING_EAST:
                return iblockdata.func_177226_a(BlockRailPowered.field_176568_b, BlockRailBase.EnumRailDirection.ASCENDING_WEST);

            case ASCENDING_WEST:
                return iblockdata.func_177226_a(BlockRailPowered.field_176568_b, BlockRailBase.EnumRailDirection.ASCENDING_EAST);

            case ASCENDING_NORTH:
            case ASCENDING_SOUTH:
            default:
                break;

            case SOUTH_EAST:
                return iblockdata.func_177226_a(BlockRailPowered.field_176568_b, BlockRailBase.EnumRailDirection.SOUTH_WEST);

            case SOUTH_WEST:
                return iblockdata.func_177226_a(BlockRailPowered.field_176568_b, BlockRailBase.EnumRailDirection.SOUTH_EAST);

            case NORTH_WEST:
                return iblockdata.func_177226_a(BlockRailPowered.field_176568_b, BlockRailBase.EnumRailDirection.NORTH_EAST);

            case NORTH_EAST:
                return iblockdata.func_177226_a(BlockRailPowered.field_176568_b, BlockRailBase.EnumRailDirection.NORTH_WEST);
            }
        }

        return super.func_185471_a(iblockdata, enumblockmirror);
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockRailPowered.field_176568_b, BlockRailPowered.field_176569_M});
    }
}
