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

    public static final PropertyDirection field_176412_a = BlockHorizontal.field_185512_D;
    protected static final AxisAlignedBB field_185578_c = new AxisAlignedBB(0.0D, 0.28125D, 0.0D, 0.125D, 0.78125D, 1.0D);
    protected static final AxisAlignedBB field_185579_d = new AxisAlignedBB(0.875D, 0.28125D, 0.0D, 1.0D, 0.78125D, 1.0D);
    protected static final AxisAlignedBB field_185580_e = new AxisAlignedBB(0.0D, 0.28125D, 0.0D, 1.0D, 0.78125D, 0.125D);
    protected static final AxisAlignedBB field_185581_f = new AxisAlignedBB(0.0D, 0.28125D, 0.875D, 1.0D, 0.78125D, 1.0D);

    public BlockWallSign() {
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockWallSign.field_176412_a, EnumFacing.NORTH));
    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        switch ((EnumFacing) iblockdata.func_177229_b(BlockWallSign.field_176412_a)) {
        case NORTH:
        default:
            return BlockWallSign.field_185581_f;

        case SOUTH:
            return BlockWallSign.field_185580_e;

        case WEST:
            return BlockWallSign.field_185579_d;

        case EAST:
            return BlockWallSign.field_185578_c;
        }
    }

    public void func_189540_a(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        EnumFacing enumdirection = (EnumFacing) iblockdata.func_177229_b(BlockWallSign.field_176412_a);

        if (!world.func_180495_p(blockposition.func_177972_a(enumdirection.func_176734_d())).func_185904_a().func_76220_a()) {
            this.func_176226_b(world, blockposition, iblockdata, 0);
            world.func_175698_g(blockposition);
        }

        super.func_189540_a(iblockdata, world, blockposition, block, blockposition1);
    }

    public IBlockState func_176203_a(int i) {
        EnumFacing enumdirection = EnumFacing.func_82600_a(i);

        if (enumdirection.func_176740_k() == EnumFacing.Axis.Y) {
            enumdirection = EnumFacing.NORTH;
        }

        return this.func_176223_P().func_177226_a(BlockWallSign.field_176412_a, enumdirection);
    }

    public int func_176201_c(IBlockState iblockdata) {
        return ((EnumFacing) iblockdata.func_177229_b(BlockWallSign.field_176412_a)).func_176745_a();
    }

    public IBlockState func_185499_a(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.func_177226_a(BlockWallSign.field_176412_a, enumblockrotation.func_185831_a((EnumFacing) iblockdata.func_177229_b(BlockWallSign.field_176412_a)));
    }

    public IBlockState func_185471_a(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata.func_185907_a(enumblockmirror.func_185800_a((EnumFacing) iblockdata.func_177229_b(BlockWallSign.field_176412_a)));
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockWallSign.field_176412_a});
    }
}
