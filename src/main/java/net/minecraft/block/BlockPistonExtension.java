package net.minecraft.block;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPistonExtension extends BlockDirectional {

    public static final PropertyEnum<BlockPistonExtension.EnumPistonType> field_176325_b = PropertyEnum.func_177709_a("type", BlockPistonExtension.EnumPistonType.class);
    public static final PropertyBool field_176327_M = PropertyBool.func_177716_a("short");
    protected static final AxisAlignedBB field_185635_c = new AxisAlignedBB(0.75D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB field_185637_d = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.25D, 1.0D, 1.0D);
    protected static final AxisAlignedBB field_185639_e = new AxisAlignedBB(0.0D, 0.0D, 0.75D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB field_185641_f = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.25D);
    protected static final AxisAlignedBB field_185643_g = new AxisAlignedBB(0.0D, 0.75D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB field_185634_B = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D);
    protected static final AxisAlignedBB field_185636_C = new AxisAlignedBB(0.375D, -0.25D, 0.375D, 0.625D, 0.75D, 0.625D);
    protected static final AxisAlignedBB field_185638_D = new AxisAlignedBB(0.375D, 0.25D, 0.375D, 0.625D, 1.25D, 0.625D);
    protected static final AxisAlignedBB field_185640_E = new AxisAlignedBB(0.375D, 0.375D, -0.25D, 0.625D, 0.625D, 0.75D);
    protected static final AxisAlignedBB field_185642_F = new AxisAlignedBB(0.375D, 0.375D, 0.25D, 0.625D, 0.625D, 1.25D);
    protected static final AxisAlignedBB field_185644_G = new AxisAlignedBB(-0.25D, 0.375D, 0.375D, 0.75D, 0.625D, 0.625D);
    protected static final AxisAlignedBB field_185645_I = new AxisAlignedBB(0.25D, 0.375D, 0.375D, 1.25D, 0.625D, 0.625D);
    protected static final AxisAlignedBB field_190964_J = new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.75D, 0.625D);
    protected static final AxisAlignedBB field_190965_K = new AxisAlignedBB(0.375D, 0.25D, 0.375D, 0.625D, 1.0D, 0.625D);
    protected static final AxisAlignedBB field_190966_L = new AxisAlignedBB(0.375D, 0.375D, 0.0D, 0.625D, 0.625D, 0.75D);
    protected static final AxisAlignedBB field_190967_M = new AxisAlignedBB(0.375D, 0.375D, 0.25D, 0.625D, 0.625D, 1.0D);
    protected static final AxisAlignedBB field_190968_N = new AxisAlignedBB(0.0D, 0.375D, 0.375D, 0.75D, 0.625D, 0.625D);
    protected static final AxisAlignedBB field_190969_O = new AxisAlignedBB(0.25D, 0.375D, 0.375D, 1.0D, 0.625D, 0.625D);

    public BlockPistonExtension() {
        super(Material.field_76233_E);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockPistonExtension.field_176387_N, EnumFacing.NORTH).func_177226_a(BlockPistonExtension.field_176325_b, BlockPistonExtension.EnumPistonType.DEFAULT).func_177226_a(BlockPistonExtension.field_176327_M, Boolean.valueOf(false)));
        this.func_149672_a(SoundType.field_185851_d);
        this.func_149711_c(0.5F);
    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        switch ((EnumFacing) iblockdata.func_177229_b(BlockPistonExtension.field_176387_N)) {
        case DOWN:
        default:
            return BlockPistonExtension.field_185634_B;

        case UP:
            return BlockPistonExtension.field_185643_g;

        case NORTH:
            return BlockPistonExtension.field_185641_f;

        case SOUTH:
            return BlockPistonExtension.field_185639_e;

        case WEST:
            return BlockPistonExtension.field_185637_d;

        case EAST:
            return BlockPistonExtension.field_185635_c;
        }
    }

    public void func_185477_a(IBlockState iblockdata, World world, BlockPos blockposition, AxisAlignedBB axisalignedbb, List<AxisAlignedBB> list, @Nullable Entity entity, boolean flag) {
        func_185492_a(blockposition, axisalignedbb, list, iblockdata.func_185900_c(world, blockposition));
        func_185492_a(blockposition, axisalignedbb, list, this.func_185633_i(iblockdata));
    }

    private AxisAlignedBB func_185633_i(IBlockState iblockdata) {
        boolean flag = ((Boolean) iblockdata.func_177229_b(BlockPistonExtension.field_176327_M)).booleanValue();

        switch ((EnumFacing) iblockdata.func_177229_b(BlockPistonExtension.field_176387_N)) {
        case DOWN:
        default:
            return flag ? BlockPistonExtension.field_190965_K : BlockPistonExtension.field_185638_D;

        case UP:
            return flag ? BlockPistonExtension.field_190964_J : BlockPistonExtension.field_185636_C;

        case NORTH:
            return flag ? BlockPistonExtension.field_190967_M : BlockPistonExtension.field_185642_F;

        case SOUTH:
            return flag ? BlockPistonExtension.field_190966_L : BlockPistonExtension.field_185640_E;

        case WEST:
            return flag ? BlockPistonExtension.field_190969_O : BlockPistonExtension.field_185645_I;

        case EAST:
            return flag ? BlockPistonExtension.field_190968_N : BlockPistonExtension.field_185644_G;
        }
    }

    public boolean func_185481_k(IBlockState iblockdata) {
        return iblockdata.func_177229_b(BlockPistonExtension.field_176387_N) == EnumFacing.UP;
    }

    public void func_176208_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman) {
        if (entityhuman.field_71075_bZ.field_75098_d) {
            BlockPos blockposition1 = blockposition.func_177972_a(((EnumFacing) iblockdata.func_177229_b(BlockPistonExtension.field_176387_N)).func_176734_d());
            Block block = world.func_180495_p(blockposition1).func_177230_c();

            if (block == Blocks.field_150331_J || block == Blocks.field_150320_F) {
                world.func_175698_g(blockposition1);
            }
        }

        super.func_176208_a(world, blockposition, iblockdata, entityhuman);
    }

    public void func_180663_b(World world, BlockPos blockposition, IBlockState iblockdata) {
        super.func_180663_b(world, blockposition, iblockdata);
        EnumFacing enumdirection = ((EnumFacing) iblockdata.func_177229_b(BlockPistonExtension.field_176387_N)).func_176734_d();

        blockposition = blockposition.func_177972_a(enumdirection);
        IBlockState iblockdata1 = world.func_180495_p(blockposition);

        if ((iblockdata1.func_177230_c() == Blocks.field_150331_J || iblockdata1.func_177230_c() == Blocks.field_150320_F) && ((Boolean) iblockdata1.func_177229_b(BlockPistonBase.field_176320_b)).booleanValue()) {
            iblockdata1.func_177230_c().func_176226_b(world, blockposition, iblockdata1, 0);
            world.func_175698_g(blockposition);
        }

    }

    public boolean func_149662_c(IBlockState iblockdata) {
        return false;
    }

    public boolean func_149686_d(IBlockState iblockdata) {
        return false;
    }

    public boolean func_176196_c(World world, BlockPos blockposition) {
        return false;
    }

    public boolean func_176198_a(World world, BlockPos blockposition, EnumFacing enumdirection) {
        return false;
    }

    public int func_149745_a(Random random) {
        return 0;
    }

    public void func_189540_a(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        EnumFacing enumdirection = (EnumFacing) iblockdata.func_177229_b(BlockPistonExtension.field_176387_N);
        BlockPos blockposition2 = blockposition.func_177972_a(enumdirection.func_176734_d());
        IBlockState iblockdata1 = world.func_180495_p(blockposition2);

        if (iblockdata1.func_177230_c() != Blocks.field_150331_J && iblockdata1.func_177230_c() != Blocks.field_150320_F) {
            world.func_175698_g(blockposition);
        } else {
            iblockdata1.func_189546_a(world, blockposition2, block, blockposition1);
        }

    }

    @Nullable
    public static EnumFacing func_176322_b(int i) {
        int j = i & 7;

        return j > 5 ? null : EnumFacing.func_82600_a(j);
    }

    public ItemStack func_185473_a(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(iblockdata.func_177229_b(BlockPistonExtension.field_176325_b) == BlockPistonExtension.EnumPistonType.STICKY ? Blocks.field_150320_F : Blocks.field_150331_J);
    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockPistonExtension.field_176387_N, func_176322_b(i)).func_177226_a(BlockPistonExtension.field_176325_b, (i & 8) > 0 ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT);
    }

    public int func_176201_c(IBlockState iblockdata) {
        byte b0 = 0;
        int i = b0 | ((EnumFacing) iblockdata.func_177229_b(BlockPistonExtension.field_176387_N)).func_176745_a();

        if (iblockdata.func_177229_b(BlockPistonExtension.field_176325_b) == BlockPistonExtension.EnumPistonType.STICKY) {
            i |= 8;
        }

        return i;
    }

    public IBlockState func_185499_a(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.func_177226_a(BlockPistonExtension.field_176387_N, enumblockrotation.func_185831_a((EnumFacing) iblockdata.func_177229_b(BlockPistonExtension.field_176387_N)));
    }

    public IBlockState func_185471_a(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata.func_185907_a(enumblockmirror.func_185800_a((EnumFacing) iblockdata.func_177229_b(BlockPistonExtension.field_176387_N)));
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockPistonExtension.field_176387_N, BlockPistonExtension.field_176325_b, BlockPistonExtension.field_176327_M});
    }

    public BlockFaceShape func_193383_a(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return enumdirection == iblockdata.func_177229_b(BlockPistonExtension.field_176387_N) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }

    public static enum EnumPistonType implements IStringSerializable {

        DEFAULT("normal"), STICKY("sticky");

        private final String field_176714_c;

        private EnumPistonType(String s) {
            this.field_176714_c = s;
        }

        public String toString() {
            return this.field_176714_c;
        }

        public String func_176610_l() {
            return this.field_176714_c;
        }
    }
}
