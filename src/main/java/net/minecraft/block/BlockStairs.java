package net.minecraft.block;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockStairs extends Block {

    public static final PropertyDirection field_176309_a = BlockHorizontal.field_185512_D;
    public static final PropertyEnum<BlockStairs.EnumHalf> field_176308_b = PropertyEnum.func_177709_a("half", BlockStairs.EnumHalf.class);
    public static final PropertyEnum<BlockStairs.EnumShape> field_176310_M = PropertyEnum.func_177709_a("shape", BlockStairs.EnumShape.class);
    protected static final AxisAlignedBB field_185712_d = new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB field_185714_e = new AxisAlignedBB(0.0D, 0.5D, 0.0D, 0.5D, 1.0D, 1.0D);
    protected static final AxisAlignedBB field_185716_f = new AxisAlignedBB(0.5D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB field_185718_g = new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 0.5D);
    protected static final AxisAlignedBB field_185710_B = new AxisAlignedBB(0.0D, 0.5D, 0.5D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB field_185711_C = new AxisAlignedBB(0.0D, 0.5D, 0.0D, 0.5D, 1.0D, 0.5D);
    protected static final AxisAlignedBB field_185713_D = new AxisAlignedBB(0.5D, 0.5D, 0.0D, 1.0D, 1.0D, 0.5D);
    protected static final AxisAlignedBB field_185715_E = new AxisAlignedBB(0.0D, 0.5D, 0.5D, 0.5D, 1.0D, 1.0D);
    protected static final AxisAlignedBB field_185717_F = new AxisAlignedBB(0.5D, 0.5D, 0.5D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB field_185719_G = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
    protected static final AxisAlignedBB field_185720_H = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5D, 0.5D, 1.0D);
    protected static final AxisAlignedBB field_185721_I = new AxisAlignedBB(0.5D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
    protected static final AxisAlignedBB field_185722_J = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 0.5D);
    protected static final AxisAlignedBB field_185723_K = new AxisAlignedBB(0.0D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D);
    protected static final AxisAlignedBB field_185724_L = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5D, 0.5D, 0.5D);
    protected static final AxisAlignedBB field_185725_M = new AxisAlignedBB(0.5D, 0.0D, 0.0D, 1.0D, 0.5D, 0.5D);
    protected static final AxisAlignedBB field_185726_N = new AxisAlignedBB(0.0D, 0.0D, 0.5D, 0.5D, 0.5D, 1.0D);
    protected static final AxisAlignedBB field_185727_O = new AxisAlignedBB(0.5D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D);
    private final Block field_150149_b;
    private final IBlockState field_150151_M;

    protected BlockStairs(IBlockState iblockdata) {
        super(iblockdata.func_177230_c().field_149764_J);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockStairs.field_176309_a, EnumFacing.NORTH).func_177226_a(BlockStairs.field_176308_b, BlockStairs.EnumHalf.BOTTOM).func_177226_a(BlockStairs.field_176310_M, BlockStairs.EnumShape.STRAIGHT));
        this.field_150149_b = iblockdata.func_177230_c();
        this.field_150151_M = iblockdata;
        this.func_149711_c(this.field_150149_b.field_149782_v);
        this.func_149752_b(this.field_150149_b.field_149781_w / 3.0F);
        this.func_149672_a(this.field_150149_b.field_149762_H);
        this.func_149713_g(255);
        this.func_149647_a(CreativeTabs.field_78030_b);
    }

    public void func_185477_a(IBlockState iblockdata, World world, BlockPos blockposition, AxisAlignedBB axisalignedbb, List<AxisAlignedBB> list, @Nullable Entity entity, boolean flag) {
        if (!flag) {
            iblockdata = this.func_176221_a(iblockdata, world, blockposition);
        }

        Iterator iterator = func_185708_x(iblockdata).iterator();

        while (iterator.hasNext()) {
            AxisAlignedBB axisalignedbb1 = (AxisAlignedBB) iterator.next();

            func_185492_a(blockposition, axisalignedbb, list, axisalignedbb1);
        }

    }

    private static List<AxisAlignedBB> func_185708_x(IBlockState iblockdata) {
        ArrayList arraylist = Lists.newArrayList();
        boolean flag = iblockdata.func_177229_b(BlockStairs.field_176308_b) == BlockStairs.EnumHalf.TOP;

        arraylist.add(flag ? BlockStairs.field_185712_d : BlockStairs.field_185719_G);
        BlockStairs.EnumShape blockstairs_enumstairshape = (BlockStairs.EnumShape) iblockdata.func_177229_b(BlockStairs.field_176310_M);

        if (blockstairs_enumstairshape == BlockStairs.EnumShape.STRAIGHT || blockstairs_enumstairshape == BlockStairs.EnumShape.INNER_LEFT || blockstairs_enumstairshape == BlockStairs.EnumShape.INNER_RIGHT) {
            arraylist.add(func_185707_y(iblockdata));
        }

        if (blockstairs_enumstairshape != BlockStairs.EnumShape.STRAIGHT) {
            arraylist.add(func_185705_z(iblockdata));
        }

        return arraylist;
    }

    private static AxisAlignedBB func_185707_y(IBlockState iblockdata) {
        boolean flag = iblockdata.func_177229_b(BlockStairs.field_176308_b) == BlockStairs.EnumHalf.TOP;

        switch ((EnumFacing) iblockdata.func_177229_b(BlockStairs.field_176309_a)) {
        case NORTH:
        default:
            return flag ? BlockStairs.field_185722_J : BlockStairs.field_185718_g;

        case SOUTH:
            return flag ? BlockStairs.field_185723_K : BlockStairs.field_185710_B;

        case WEST:
            return flag ? BlockStairs.field_185720_H : BlockStairs.field_185714_e;

        case EAST:
            return flag ? BlockStairs.field_185721_I : BlockStairs.field_185716_f;
        }
    }

    private static AxisAlignedBB func_185705_z(IBlockState iblockdata) {
        EnumFacing enumdirection = (EnumFacing) iblockdata.func_177229_b(BlockStairs.field_176309_a);
        EnumFacing enumdirection1;

        switch ((BlockStairs.EnumShape) iblockdata.func_177229_b(BlockStairs.field_176310_M)) {
        case OUTER_LEFT:
        default:
            enumdirection1 = enumdirection;
            break;

        case OUTER_RIGHT:
            enumdirection1 = enumdirection.func_176746_e();
            break;

        case INNER_RIGHT:
            enumdirection1 = enumdirection.func_176734_d();
            break;

        case INNER_LEFT:
            enumdirection1 = enumdirection.func_176735_f();
        }

        boolean flag = iblockdata.func_177229_b(BlockStairs.field_176308_b) == BlockStairs.EnumHalf.TOP;

        switch (enumdirection1) {
        case NORTH:
        default:
            return flag ? BlockStairs.field_185724_L : BlockStairs.field_185711_C;

        case SOUTH:
            return flag ? BlockStairs.field_185727_O : BlockStairs.field_185717_F;

        case WEST:
            return flag ? BlockStairs.field_185726_N : BlockStairs.field_185715_E;

        case EAST:
            return flag ? BlockStairs.field_185725_M : BlockStairs.field_185713_D;
        }
    }

    public BlockFaceShape func_193383_a(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        iblockdata = this.func_176221_a(iblockdata, iblockaccess, blockposition);
        if (enumdirection.func_176740_k() == EnumFacing.Axis.Y) {
            return enumdirection == EnumFacing.UP == (iblockdata.func_177229_b(BlockStairs.field_176308_b) == BlockStairs.EnumHalf.TOP) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
        } else {
            BlockStairs.EnumShape blockstairs_enumstairshape = (BlockStairs.EnumShape) iblockdata.func_177229_b(BlockStairs.field_176310_M);

            if (blockstairs_enumstairshape != BlockStairs.EnumShape.OUTER_LEFT && blockstairs_enumstairshape != BlockStairs.EnumShape.OUTER_RIGHT) {
                EnumFacing enumdirection1 = (EnumFacing) iblockdata.func_177229_b(BlockStairs.field_176309_a);

                switch (blockstairs_enumstairshape) {
                case INNER_RIGHT:
                    return enumdirection1 != enumdirection && enumdirection1 != enumdirection.func_176735_f() ? BlockFaceShape.UNDEFINED : BlockFaceShape.SOLID;

                case INNER_LEFT:
                    return enumdirection1 != enumdirection && enumdirection1 != enumdirection.func_176746_e() ? BlockFaceShape.UNDEFINED : BlockFaceShape.SOLID;

                case STRAIGHT:
                    return enumdirection1 == enumdirection ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;

                default:
                    return BlockFaceShape.UNDEFINED;
                }
            } else {
                return BlockFaceShape.UNDEFINED;
            }
        }
    }

    public boolean func_149662_c(IBlockState iblockdata) {
        return false;
    }

    public boolean func_149686_d(IBlockState iblockdata) {
        return false;
    }

    public void func_180649_a(World world, BlockPos blockposition, EntityPlayer entityhuman) {
        this.field_150149_b.func_180649_a(world, blockposition, entityhuman);
    }

    public void func_176206_d(World world, BlockPos blockposition, IBlockState iblockdata) {
        this.field_150149_b.func_176206_d(world, blockposition, iblockdata);
    }

    public float func_149638_a(Entity entity) {
        return this.field_150149_b.func_149638_a(entity);
    }

    public int func_149738_a(World world) {
        return this.field_150149_b.func_149738_a(world);
    }

    public Vec3d func_176197_a(World world, BlockPos blockposition, Entity entity, Vec3d vec3d) {
        return this.field_150149_b.func_176197_a(world, blockposition, entity, vec3d);
    }

    public boolean func_149703_v() {
        return this.field_150149_b.func_149703_v();
    }

    public boolean func_176209_a(IBlockState iblockdata, boolean flag) {
        return this.field_150149_b.func_176209_a(iblockdata, flag);
    }

    public boolean func_176196_c(World world, BlockPos blockposition) {
        return this.field_150149_b.func_176196_c(world, blockposition);
    }

    public void func_176213_c(World world, BlockPos blockposition, IBlockState iblockdata) {
        this.field_150151_M.func_189546_a(world, blockposition, Blocks.field_150350_a, blockposition);
        this.field_150149_b.func_176213_c(world, blockposition, this.field_150151_M);
    }

    public void func_180663_b(World world, BlockPos blockposition, IBlockState iblockdata) {
        this.field_150149_b.func_180663_b(world, blockposition, this.field_150151_M);
    }

    public void func_176199_a(World world, BlockPos blockposition, Entity entity) {
        this.field_150149_b.func_176199_a(world, blockposition, entity);
    }

    public void func_180650_b(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        this.field_150149_b.func_180650_b(world, blockposition, iblockdata, random);
    }

    public boolean func_180639_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        return this.field_150149_b.func_180639_a(world, blockposition, this.field_150151_M, entityhuman, enumhand, EnumFacing.DOWN, 0.0F, 0.0F, 0.0F);
    }

    public void func_180652_a(World world, BlockPos blockposition, Explosion explosion) {
        this.field_150149_b.func_180652_a(world, blockposition, explosion);
    }

    public boolean func_185481_k(IBlockState iblockdata) {
        return iblockdata.func_177229_b(BlockStairs.field_176308_b) == BlockStairs.EnumHalf.TOP;
    }

    public MapColor func_180659_g(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return this.field_150149_b.func_180659_g(this.field_150151_M, iblockaccess, blockposition);
    }

    public IBlockState func_180642_a(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        IBlockState iblockdata = super.func_180642_a(world, blockposition, enumdirection, f, f1, f2, i, entityliving);

        iblockdata = iblockdata.func_177226_a(BlockStairs.field_176309_a, entityliving.func_174811_aO()).func_177226_a(BlockStairs.field_176310_M, BlockStairs.EnumShape.STRAIGHT);
        return enumdirection != EnumFacing.DOWN && (enumdirection == EnumFacing.UP || (double) f1 <= 0.5D) ? iblockdata.func_177226_a(BlockStairs.field_176308_b, BlockStairs.EnumHalf.BOTTOM) : iblockdata.func_177226_a(BlockStairs.field_176308_b, BlockStairs.EnumHalf.TOP);
    }

    @Nullable
    public RayTraceResult func_180636_a(IBlockState iblockdata, World world, BlockPos blockposition, Vec3d vec3d, Vec3d vec3d1) {
        ArrayList arraylist = Lists.newArrayList();
        Iterator iterator = func_185708_x(this.func_176221_a(iblockdata, world, blockposition)).iterator();

        while (iterator.hasNext()) {
            AxisAlignedBB axisalignedbb = (AxisAlignedBB) iterator.next();

            arraylist.add(this.func_185503_a(blockposition, vec3d, vec3d1, axisalignedbb));
        }

        RayTraceResult movingobjectposition = null;
        double d0 = 0.0D;
        Iterator iterator1 = arraylist.iterator();

        while (iterator1.hasNext()) {
            RayTraceResult movingobjectposition1 = (RayTraceResult) iterator1.next();

            if (movingobjectposition1 != null) {
                double d1 = movingobjectposition1.field_72307_f.func_72436_e(vec3d1);

                if (d1 > d0) {
                    movingobjectposition = movingobjectposition1;
                    d0 = d1;
                }
            }
        }

        return movingobjectposition;
    }

    public IBlockState func_176203_a(int i) {
        IBlockState iblockdata = this.func_176223_P().func_177226_a(BlockStairs.field_176308_b, (i & 4) > 0 ? BlockStairs.EnumHalf.TOP : BlockStairs.EnumHalf.BOTTOM);

        iblockdata = iblockdata.func_177226_a(BlockStairs.field_176309_a, EnumFacing.func_82600_a(5 - (i & 3)));
        return iblockdata;
    }

    public int func_176201_c(IBlockState iblockdata) {
        int i = 0;

        if (iblockdata.func_177229_b(BlockStairs.field_176308_b) == BlockStairs.EnumHalf.TOP) {
            i |= 4;
        }

        i |= 5 - ((EnumFacing) iblockdata.func_177229_b(BlockStairs.field_176309_a)).func_176745_a();
        return i;
    }

    public IBlockState func_176221_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return iblockdata.func_177226_a(BlockStairs.field_176310_M, func_185706_d(iblockdata, iblockaccess, blockposition));
    }

    private static BlockStairs.EnumShape func_185706_d(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        EnumFacing enumdirection = (EnumFacing) iblockdata.func_177229_b(BlockStairs.field_176309_a);
        IBlockState iblockdata1 = iblockaccess.func_180495_p(blockposition.func_177972_a(enumdirection));

        if (func_185709_i(iblockdata1) && iblockdata.func_177229_b(BlockStairs.field_176308_b) == iblockdata1.func_177229_b(BlockStairs.field_176308_b)) {
            EnumFacing enumdirection1 = (EnumFacing) iblockdata1.func_177229_b(BlockStairs.field_176309_a);

            if (enumdirection1.func_176740_k() != ((EnumFacing) iblockdata.func_177229_b(BlockStairs.field_176309_a)).func_176740_k() && func_185704_d(iblockdata, iblockaccess, blockposition, enumdirection1.func_176734_d())) {
                if (enumdirection1 == enumdirection.func_176735_f()) {
                    return BlockStairs.EnumShape.OUTER_LEFT;
                }

                return BlockStairs.EnumShape.OUTER_RIGHT;
            }
        }

        IBlockState iblockdata2 = iblockaccess.func_180495_p(blockposition.func_177972_a(enumdirection.func_176734_d()));

        if (func_185709_i(iblockdata2) && iblockdata.func_177229_b(BlockStairs.field_176308_b) == iblockdata2.func_177229_b(BlockStairs.field_176308_b)) {
            EnumFacing enumdirection2 = (EnumFacing) iblockdata2.func_177229_b(BlockStairs.field_176309_a);

            if (enumdirection2.func_176740_k() != ((EnumFacing) iblockdata.func_177229_b(BlockStairs.field_176309_a)).func_176740_k() && func_185704_d(iblockdata, iblockaccess, blockposition, enumdirection2)) {
                if (enumdirection2 == enumdirection.func_176735_f()) {
                    return BlockStairs.EnumShape.INNER_LEFT;
                }

                return BlockStairs.EnumShape.INNER_RIGHT;
            }
        }

        return BlockStairs.EnumShape.STRAIGHT;
    }

    private static boolean func_185704_d(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        IBlockState iblockdata1 = iblockaccess.func_180495_p(blockposition.func_177972_a(enumdirection));

        return !func_185709_i(iblockdata1) || iblockdata1.func_177229_b(BlockStairs.field_176309_a) != iblockdata.func_177229_b(BlockStairs.field_176309_a) || iblockdata1.func_177229_b(BlockStairs.field_176308_b) != iblockdata.func_177229_b(BlockStairs.field_176308_b);
    }

    public static boolean func_185709_i(IBlockState iblockdata) {
        return iblockdata.func_177230_c() instanceof BlockStairs;
    }

    public IBlockState func_185499_a(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.func_177226_a(BlockStairs.field_176309_a, enumblockrotation.func_185831_a((EnumFacing) iblockdata.func_177229_b(BlockStairs.field_176309_a)));
    }

    public IBlockState func_185471_a(IBlockState iblockdata, Mirror enumblockmirror) {
        EnumFacing enumdirection = (EnumFacing) iblockdata.func_177229_b(BlockStairs.field_176309_a);
        BlockStairs.EnumShape blockstairs_enumstairshape = (BlockStairs.EnumShape) iblockdata.func_177229_b(BlockStairs.field_176310_M);

        switch (enumblockmirror) {
        case LEFT_RIGHT:
            if (enumdirection.func_176740_k() == EnumFacing.Axis.Z) {
                switch (blockstairs_enumstairshape) {
                case OUTER_LEFT:
                    return iblockdata.func_185907_a(Rotation.CLOCKWISE_180).func_177226_a(BlockStairs.field_176310_M, BlockStairs.EnumShape.OUTER_RIGHT);

                case OUTER_RIGHT:
                    return iblockdata.func_185907_a(Rotation.CLOCKWISE_180).func_177226_a(BlockStairs.field_176310_M, BlockStairs.EnumShape.OUTER_LEFT);

                case INNER_RIGHT:
                    return iblockdata.func_185907_a(Rotation.CLOCKWISE_180).func_177226_a(BlockStairs.field_176310_M, BlockStairs.EnumShape.INNER_LEFT);

                case INNER_LEFT:
                    return iblockdata.func_185907_a(Rotation.CLOCKWISE_180).func_177226_a(BlockStairs.field_176310_M, BlockStairs.EnumShape.INNER_RIGHT);

                default:
                    return iblockdata.func_185907_a(Rotation.CLOCKWISE_180);
                }
            }
            break;

        case FRONT_BACK:
            if (enumdirection.func_176740_k() == EnumFacing.Axis.X) {
                switch (blockstairs_enumstairshape) {
                case OUTER_LEFT:
                    return iblockdata.func_185907_a(Rotation.CLOCKWISE_180).func_177226_a(BlockStairs.field_176310_M, BlockStairs.EnumShape.OUTER_RIGHT);

                case OUTER_RIGHT:
                    return iblockdata.func_185907_a(Rotation.CLOCKWISE_180).func_177226_a(BlockStairs.field_176310_M, BlockStairs.EnumShape.OUTER_LEFT);

                case INNER_RIGHT:
                    return iblockdata.func_185907_a(Rotation.CLOCKWISE_180).func_177226_a(BlockStairs.field_176310_M, BlockStairs.EnumShape.INNER_RIGHT);

                case INNER_LEFT:
                    return iblockdata.func_185907_a(Rotation.CLOCKWISE_180).func_177226_a(BlockStairs.field_176310_M, BlockStairs.EnumShape.INNER_LEFT);

                case STRAIGHT:
                    return iblockdata.func_185907_a(Rotation.CLOCKWISE_180);
                }
            }
        }

        return super.func_185471_a(iblockdata, enumblockmirror);
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockStairs.field_176309_a, BlockStairs.field_176308_b, BlockStairs.field_176310_M});
    }

    public static enum EnumShape implements IStringSerializable {

        STRAIGHT("straight"), INNER_LEFT("inner_left"), INNER_RIGHT("inner_right"), OUTER_LEFT("outer_left"), OUTER_RIGHT("outer_right");

        private final String field_176699_f;

        private EnumShape(String s) {
            this.field_176699_f = s;
        }

        public String toString() {
            return this.field_176699_f;
        }

        public String func_176610_l() {
            return this.field_176699_f;
        }
    }

    public static enum EnumHalf implements IStringSerializable {

        TOP("top"), BOTTOM("bottom");

        private final String field_176709_c;

        private EnumHalf(String s) {
            this.field_176709_c = s;
        }

        public String toString() {
            return this.field_176709_c;
        }

        public String func_176610_l() {
            return this.field_176709_c;
        }
    }
}
