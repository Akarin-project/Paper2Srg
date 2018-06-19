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

    protected static final AxisAlignedBB field_185630_a = new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 1.0D, 0.625D);
    protected static final AxisAlignedBB field_185631_b = new AxisAlignedBB(0.375D, 0.375D, 0.0D, 0.625D, 0.625D, 1.0D);
    protected static final AxisAlignedBB field_185632_c = new AxisAlignedBB(0.0D, 0.375D, 0.375D, 1.0D, 0.625D, 0.625D);

    protected BlockEndRod() {
        super(Material.field_151594_q);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockEndRod.field_176387_N, EnumFacing.UP));
        this.func_149647_a(CreativeTabs.field_78031_c);
    }

    public IBlockState func_185499_a(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.func_177226_a(BlockEndRod.field_176387_N, enumblockrotation.func_185831_a((EnumFacing) iblockdata.func_177229_b(BlockEndRod.field_176387_N)));
    }

    public IBlockState func_185471_a(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata.func_177226_a(BlockEndRod.field_176387_N, enumblockmirror.func_185803_b((EnumFacing) iblockdata.func_177229_b(BlockEndRod.field_176387_N)));
    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        switch (((EnumFacing) iblockdata.func_177229_b(BlockEndRod.field_176387_N)).func_176740_k()) {
        case X:
        default:
            return BlockEndRod.field_185632_c;

        case Z:
            return BlockEndRod.field_185631_b;

        case Y:
            return BlockEndRod.field_185630_a;
        }
    }

    public boolean func_149662_c(IBlockState iblockdata) {
        return false;
    }

    public boolean func_149686_d(IBlockState iblockdata) {
        return false;
    }

    public boolean func_176196_c(World world, BlockPos blockposition) {
        return true;
    }

    public IBlockState func_180642_a(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        IBlockState iblockdata = world.func_180495_p(blockposition.func_177972_a(enumdirection.func_176734_d()));

        if (iblockdata.func_177230_c() == Blocks.field_185764_cQ) {
            EnumFacing enumdirection1 = (EnumFacing) iblockdata.func_177229_b(BlockEndRod.field_176387_N);

            if (enumdirection1 == enumdirection) {
                return this.func_176223_P().func_177226_a(BlockEndRod.field_176387_N, enumdirection.func_176734_d());
            }
        }

        return this.func_176223_P().func_177226_a(BlockEndRod.field_176387_N, enumdirection);
    }

    public IBlockState func_176203_a(int i) {
        IBlockState iblockdata = this.func_176223_P();

        iblockdata = iblockdata.func_177226_a(BlockEndRod.field_176387_N, EnumFacing.func_82600_a(i));
        return iblockdata;
    }

    public int func_176201_c(IBlockState iblockdata) {
        return ((EnumFacing) iblockdata.func_177229_b(BlockEndRod.field_176387_N)).func_176745_a();
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockEndRod.field_176387_N});
    }

    public EnumPushReaction func_149656_h(IBlockState iblockdata) {
        return EnumPushReaction.NORMAL;
    }

    public BlockFaceShape func_193383_a(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }
}
