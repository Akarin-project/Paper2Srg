package net.minecraft.block;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPane extends Block {

    public static final PropertyBool field_176241_b = PropertyBool.func_177716_a("north");
    public static final PropertyBool field_176242_M = PropertyBool.func_177716_a("east");
    public static final PropertyBool field_176243_N = PropertyBool.func_177716_a("south");
    public static final PropertyBool field_176244_O = PropertyBool.func_177716_a("west");
    protected static final AxisAlignedBB[] field_185730_f = new AxisAlignedBB[] { new AxisAlignedBB(0.4375D, 0.0D, 0.4375D, 0.5625D, 1.0D, 0.5625D), new AxisAlignedBB(0.4375D, 0.0D, 0.4375D, 0.5625D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.4375D, 0.5625D, 1.0D, 0.5625D), new AxisAlignedBB(0.0D, 0.0D, 0.4375D, 0.5625D, 1.0D, 1.0D), new AxisAlignedBB(0.4375D, 0.0D, 0.0D, 0.5625D, 1.0D, 0.5625D), new AxisAlignedBB(0.4375D, 0.0D, 0.0D, 0.5625D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5625D, 1.0D, 0.5625D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5625D, 1.0D, 1.0D), new AxisAlignedBB(0.4375D, 0.0D, 0.4375D, 1.0D, 1.0D, 0.5625D), new AxisAlignedBB(0.4375D, 0.0D, 0.4375D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.4375D, 1.0D, 1.0D, 0.5625D), new AxisAlignedBB(0.0D, 0.0D, 0.4375D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.4375D, 0.0D, 0.0D, 1.0D, 1.0D, 0.5625D), new AxisAlignedBB(0.4375D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.5625D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)};
    private final boolean field_150099_b;

    protected BlockPane(Material material, boolean flag) {
        super(material);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockPane.field_176241_b, Boolean.valueOf(false)).func_177226_a(BlockPane.field_176242_M, Boolean.valueOf(false)).func_177226_a(BlockPane.field_176243_N, Boolean.valueOf(false)).func_177226_a(BlockPane.field_176244_O, Boolean.valueOf(false)));
        this.field_150099_b = flag;
        this.func_149647_a(CreativeTabs.field_78031_c);
    }

    public void func_185477_a(IBlockState iblockdata, World world, BlockPos blockposition, AxisAlignedBB axisalignedbb, List<AxisAlignedBB> list, @Nullable Entity entity, boolean flag) {
        if (!flag) {
            iblockdata = this.func_176221_a(iblockdata, world, blockposition);
        }

        func_185492_a(blockposition, axisalignedbb, list, BlockPane.field_185730_f[0]);
        if (((Boolean) iblockdata.func_177229_b(BlockPane.field_176241_b)).booleanValue()) {
            func_185492_a(blockposition, axisalignedbb, list, BlockPane.field_185730_f[func_185729_a(EnumFacing.NORTH)]);
        }

        if (((Boolean) iblockdata.func_177229_b(BlockPane.field_176243_N)).booleanValue()) {
            func_185492_a(blockposition, axisalignedbb, list, BlockPane.field_185730_f[func_185729_a(EnumFacing.SOUTH)]);
        }

        if (((Boolean) iblockdata.func_177229_b(BlockPane.field_176242_M)).booleanValue()) {
            func_185492_a(blockposition, axisalignedbb, list, BlockPane.field_185730_f[func_185729_a(EnumFacing.EAST)]);
        }

        if (((Boolean) iblockdata.func_177229_b(BlockPane.field_176244_O)).booleanValue()) {
            func_185492_a(blockposition, axisalignedbb, list, BlockPane.field_185730_f[func_185729_a(EnumFacing.WEST)]);
        }

    }

    private static int func_185729_a(EnumFacing enumdirection) {
        return 1 << enumdirection.func_176736_b();
    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        iblockdata = this.func_176221_a(iblockdata, iblockaccess, blockposition);
        return BlockPane.field_185730_f[func_185728_i(iblockdata)];
    }

    private static int func_185728_i(IBlockState iblockdata) {
        int i = 0;

        if (((Boolean) iblockdata.func_177229_b(BlockPane.field_176241_b)).booleanValue()) {
            i |= func_185729_a(EnumFacing.NORTH);
        }

        if (((Boolean) iblockdata.func_177229_b(BlockPane.field_176242_M)).booleanValue()) {
            i |= func_185729_a(EnumFacing.EAST);
        }

        if (((Boolean) iblockdata.func_177229_b(BlockPane.field_176243_N)).booleanValue()) {
            i |= func_185729_a(EnumFacing.SOUTH);
        }

        if (((Boolean) iblockdata.func_177229_b(BlockPane.field_176244_O)).booleanValue()) {
            i |= func_185729_a(EnumFacing.WEST);
        }

        return i;
    }

    public IBlockState func_176221_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return iblockdata.func_177226_a(BlockPane.field_176241_b, Boolean.valueOf(this.func_193393_b(iblockaccess, iblockaccess.func_180495_p(blockposition.func_177978_c()), blockposition.func_177978_c(), EnumFacing.SOUTH))).func_177226_a(BlockPane.field_176243_N, Boolean.valueOf(this.func_193393_b(iblockaccess, iblockaccess.func_180495_p(blockposition.func_177968_d()), blockposition.func_177968_d(), EnumFacing.NORTH))).func_177226_a(BlockPane.field_176244_O, Boolean.valueOf(this.func_193393_b(iblockaccess, iblockaccess.func_180495_p(blockposition.func_177976_e()), blockposition.func_177976_e(), EnumFacing.EAST))).func_177226_a(BlockPane.field_176242_M, Boolean.valueOf(this.func_193393_b(iblockaccess, iblockaccess.func_180495_p(blockposition.func_177974_f()), blockposition.func_177974_f(), EnumFacing.WEST)));
    }

    public Item func_180660_a(IBlockState iblockdata, Random random, int i) {
        return !this.field_150099_b ? Items.field_190931_a : super.func_180660_a(iblockdata, random, i);
    }

    public boolean func_149662_c(IBlockState iblockdata) {
        return false;
    }

    public boolean func_149686_d(IBlockState iblockdata) {
        return false;
    }

    public final boolean func_193393_b(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        Block block = iblockdata.func_177230_c();
        BlockFaceShape enumblockfaceshape = iblockdata.func_193401_d(iblockaccess, blockposition, enumdirection);

        return !func_193394_e(block) && enumblockfaceshape == BlockFaceShape.SOLID || enumblockfaceshape == BlockFaceShape.MIDDLE_POLE_THIN;
    }

    protected static boolean func_193394_e(Block block) {
        return block instanceof BlockShulkerBox || block instanceof BlockLeaves || block == Blocks.field_150461_bJ || block == Blocks.field_150383_bp || block == Blocks.field_150426_aN || block == Blocks.field_150432_aD || block == Blocks.field_180398_cJ || block == Blocks.field_150331_J || block == Blocks.field_150320_F || block == Blocks.field_150332_K || block == Blocks.field_150440_ba || block == Blocks.field_150423_aK || block == Blocks.field_150428_aP || block == Blocks.field_180401_cv;
    }

    protected boolean func_149700_E() {
        return true;
    }

    public int func_176201_c(IBlockState iblockdata) {
        return 0;
    }

    public IBlockState func_185499_a(IBlockState iblockdata, Rotation enumblockrotation) {
        switch (enumblockrotation) {
        case CLOCKWISE_180:
            return iblockdata.func_177226_a(BlockPane.field_176241_b, iblockdata.func_177229_b(BlockPane.field_176243_N)).func_177226_a(BlockPane.field_176242_M, iblockdata.func_177229_b(BlockPane.field_176244_O)).func_177226_a(BlockPane.field_176243_N, iblockdata.func_177229_b(BlockPane.field_176241_b)).func_177226_a(BlockPane.field_176244_O, iblockdata.func_177229_b(BlockPane.field_176242_M));

        case COUNTERCLOCKWISE_90:
            return iblockdata.func_177226_a(BlockPane.field_176241_b, iblockdata.func_177229_b(BlockPane.field_176242_M)).func_177226_a(BlockPane.field_176242_M, iblockdata.func_177229_b(BlockPane.field_176243_N)).func_177226_a(BlockPane.field_176243_N, iblockdata.func_177229_b(BlockPane.field_176244_O)).func_177226_a(BlockPane.field_176244_O, iblockdata.func_177229_b(BlockPane.field_176241_b));

        case CLOCKWISE_90:
            return iblockdata.func_177226_a(BlockPane.field_176241_b, iblockdata.func_177229_b(BlockPane.field_176244_O)).func_177226_a(BlockPane.field_176242_M, iblockdata.func_177229_b(BlockPane.field_176241_b)).func_177226_a(BlockPane.field_176243_N, iblockdata.func_177229_b(BlockPane.field_176242_M)).func_177226_a(BlockPane.field_176244_O, iblockdata.func_177229_b(BlockPane.field_176243_N));

        default:
            return iblockdata;
        }
    }

    public IBlockState func_185471_a(IBlockState iblockdata, Mirror enumblockmirror) {
        switch (enumblockmirror) {
        case LEFT_RIGHT:
            return iblockdata.func_177226_a(BlockPane.field_176241_b, iblockdata.func_177229_b(BlockPane.field_176243_N)).func_177226_a(BlockPane.field_176243_N, iblockdata.func_177229_b(BlockPane.field_176241_b));

        case FRONT_BACK:
            return iblockdata.func_177226_a(BlockPane.field_176242_M, iblockdata.func_177229_b(BlockPane.field_176244_O)).func_177226_a(BlockPane.field_176244_O, iblockdata.func_177229_b(BlockPane.field_176242_M));

        default:
            return super.func_185471_a(iblockdata, enumblockmirror);
        }
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockPane.field_176241_b, BlockPane.field_176242_M, BlockPane.field_176244_O, BlockPane.field_176243_N});
    }

    public BlockFaceShape func_193383_a(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return enumdirection != EnumFacing.UP && enumdirection != EnumFacing.DOWN ? BlockFaceShape.MIDDLE_POLE_THIN : BlockFaceShape.CENTER_SMALL;
    }
}
