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

    public static final PropertyDirection field_176382_a = BlockHorizontal.field_185512_D;
    protected static final AxisAlignedBB field_185687_b = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.1875D, 1.0D, 1.0D);
    protected static final AxisAlignedBB field_185688_c = new AxisAlignedBB(0.8125D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB field_185689_d = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.1875D);
    protected static final AxisAlignedBB field_185690_e = new AxisAlignedBB(0.0D, 0.0D, 0.8125D, 1.0D, 1.0D, 1.0D);

    protected BlockLadder() {
        super(Material.field_151594_q);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockLadder.field_176382_a, EnumFacing.NORTH));
        this.func_149647_a(CreativeTabs.field_78031_c);
    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        switch ((EnumFacing) iblockdata.func_177229_b(BlockLadder.field_176382_a)) {
        case NORTH:
            return BlockLadder.field_185690_e;

        case SOUTH:
            return BlockLadder.field_185689_d;

        case WEST:
            return BlockLadder.field_185688_c;

        case EAST:
        default:
            return BlockLadder.field_185687_b;
        }
    }

    public boolean func_149662_c(IBlockState iblockdata) {
        return false;
    }

    public boolean func_149686_d(IBlockState iblockdata) {
        return false;
    }

    public boolean func_176198_a(World world, BlockPos blockposition, EnumFacing enumdirection) {
        return this.func_193392_c(world, blockposition.func_177976_e(), enumdirection) ? true : (this.func_193392_c(world, blockposition.func_177974_f(), enumdirection) ? true : (this.func_193392_c(world, blockposition.func_177978_c(), enumdirection) ? true : this.func_193392_c(world, blockposition.func_177968_d(), enumdirection)));
    }

    private boolean func_193392_c(World world, BlockPos blockposition, EnumFacing enumdirection) {
        IBlockState iblockdata = world.func_180495_p(blockposition);
        boolean flag = func_193382_c(iblockdata.func_177230_c());

        return !flag && iblockdata.func_193401_d(world, blockposition, enumdirection) == BlockFaceShape.SOLID && !iblockdata.func_185897_m();
    }

    public IBlockState func_180642_a(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        if (enumdirection.func_176740_k().func_176722_c() && this.func_193392_c(world, blockposition.func_177972_a(enumdirection.func_176734_d()), enumdirection)) {
            return this.func_176223_P().func_177226_a(BlockLadder.field_176382_a, enumdirection);
        } else {
            Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

            EnumFacing enumdirection1;

            do {
                if (!iterator.hasNext()) {
                    return this.func_176223_P();
                }

                enumdirection1 = (EnumFacing) iterator.next();
            } while (!this.func_193392_c(world, blockposition.func_177972_a(enumdirection1.func_176734_d()), enumdirection1));

            return this.func_176223_P().func_177226_a(BlockLadder.field_176382_a, enumdirection1);
        }
    }

    public void func_189540_a(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        EnumFacing enumdirection = (EnumFacing) iblockdata.func_177229_b(BlockLadder.field_176382_a);

        if (!this.func_193392_c(world, blockposition.func_177972_a(enumdirection.func_176734_d()), enumdirection)) {
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

        return this.func_176223_P().func_177226_a(BlockLadder.field_176382_a, enumdirection);
    }

    public int func_176201_c(IBlockState iblockdata) {
        return ((EnumFacing) iblockdata.func_177229_b(BlockLadder.field_176382_a)).func_176745_a();
    }

    public IBlockState func_185499_a(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.func_177226_a(BlockLadder.field_176382_a, enumblockrotation.func_185831_a((EnumFacing) iblockdata.func_177229_b(BlockLadder.field_176382_a)));
    }

    public IBlockState func_185471_a(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata.func_185907_a(enumblockmirror.func_185800_a((EnumFacing) iblockdata.func_177229_b(BlockLadder.field_176382_a)));
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockLadder.field_176382_a});
    }

    public BlockFaceShape func_193383_a(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }
}
