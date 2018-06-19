package net.minecraft.block;

import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockWall extends Block {

    public static final PropertyBool field_176256_a = PropertyBool.func_177716_a("up");
    public static final PropertyBool field_176254_b = PropertyBool.func_177716_a("north");
    public static final PropertyBool field_176257_M = PropertyBool.func_177716_a("east");
    public static final PropertyBool field_176258_N = PropertyBool.func_177716_a("south");
    public static final PropertyBool field_176259_O = PropertyBool.func_177716_a("west");
    public static final PropertyEnum<BlockWall.EnumType> field_176255_P = PropertyEnum.func_177709_a("variant", BlockWall.EnumType.class);
    protected static final AxisAlignedBB[] field_185751_g = new AxisAlignedBB[] { new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D), new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D), new AxisAlignedBB(0.0D, 0.0D, 0.25D, 0.75D, 1.0D, 1.0D), new AxisAlignedBB(0.25D, 0.0D, 0.0D, 0.75D, 1.0D, 0.75D), new AxisAlignedBB(0.3125D, 0.0D, 0.0D, 0.6875D, 0.875D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.75D, 1.0D, 0.75D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.75D, 1.0D, 1.0D), new AxisAlignedBB(0.25D, 0.0D, 0.25D, 1.0D, 1.0D, 0.75D), new AxisAlignedBB(0.25D, 0.0D, 0.25D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.3125D, 1.0D, 0.875D, 0.6875D), new AxisAlignedBB(0.0D, 0.0D, 0.25D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.25D, 0.0D, 0.0D, 1.0D, 1.0D, 0.75D), new AxisAlignedBB(0.25D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.75D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)};
    protected static final AxisAlignedBB[] field_185750_B = new AxisAlignedBB[] { BlockWall.field_185751_g[0].func_186666_e(1.5D), BlockWall.field_185751_g[1].func_186666_e(1.5D), BlockWall.field_185751_g[2].func_186666_e(1.5D), BlockWall.field_185751_g[3].func_186666_e(1.5D), BlockWall.field_185751_g[4].func_186666_e(1.5D), BlockWall.field_185751_g[5].func_186666_e(1.5D), BlockWall.field_185751_g[6].func_186666_e(1.5D), BlockWall.field_185751_g[7].func_186666_e(1.5D), BlockWall.field_185751_g[8].func_186666_e(1.5D), BlockWall.field_185751_g[9].func_186666_e(1.5D), BlockWall.field_185751_g[10].func_186666_e(1.5D), BlockWall.field_185751_g[11].func_186666_e(1.5D), BlockWall.field_185751_g[12].func_186666_e(1.5D), BlockWall.field_185751_g[13].func_186666_e(1.5D), BlockWall.field_185751_g[14].func_186666_e(1.5D), BlockWall.field_185751_g[15].func_186666_e(1.5D)};

    public BlockWall(Block block) {
        super(block.field_149764_J);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockWall.field_176256_a, Boolean.valueOf(false)).func_177226_a(BlockWall.field_176254_b, Boolean.valueOf(false)).func_177226_a(BlockWall.field_176257_M, Boolean.valueOf(false)).func_177226_a(BlockWall.field_176258_N, Boolean.valueOf(false)).func_177226_a(BlockWall.field_176259_O, Boolean.valueOf(false)).func_177226_a(BlockWall.field_176255_P, BlockWall.EnumType.NORMAL));
        this.func_149711_c(block.field_149782_v);
        this.func_149752_b(block.field_149781_w / 3.0F);
        this.func_149672_a(block.field_149762_H);
        this.func_149647_a(CreativeTabs.field_78031_c);
    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        iblockdata = this.func_176221_a(iblockdata, iblockaccess, blockposition);
        return BlockWall.field_185751_g[func_185749_i(iblockdata)];
    }

    public void func_185477_a(IBlockState iblockdata, World world, BlockPos blockposition, AxisAlignedBB axisalignedbb, List<AxisAlignedBB> list, @Nullable Entity entity, boolean flag) {
        if (!flag) {
            iblockdata = this.func_176221_a(iblockdata, world, blockposition);
        }

        func_185492_a(blockposition, axisalignedbb, list, BlockWall.field_185750_B[func_185749_i(iblockdata)]);
    }

    @Nullable
    public AxisAlignedBB func_180646_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        iblockdata = this.func_176221_a(iblockdata, iblockaccess, blockposition);
        return BlockWall.field_185750_B[func_185749_i(iblockdata)];
    }

    private static int func_185749_i(IBlockState iblockdata) {
        int i = 0;

        if (((Boolean) iblockdata.func_177229_b(BlockWall.field_176254_b)).booleanValue()) {
            i |= 1 << EnumFacing.NORTH.func_176736_b();
        }

        if (((Boolean) iblockdata.func_177229_b(BlockWall.field_176257_M)).booleanValue()) {
            i |= 1 << EnumFacing.EAST.func_176736_b();
        }

        if (((Boolean) iblockdata.func_177229_b(BlockWall.field_176258_N)).booleanValue()) {
            i |= 1 << EnumFacing.SOUTH.func_176736_b();
        }

        if (((Boolean) iblockdata.func_177229_b(BlockWall.field_176259_O)).booleanValue()) {
            i |= 1 << EnumFacing.WEST.func_176736_b();
        }

        return i;
    }

    public String func_149732_F() {
        return I18n.func_74838_a(this.func_149739_a() + "." + BlockWall.EnumType.NORMAL.func_176659_c() + ".name");
    }

    public boolean func_149686_d(IBlockState iblockdata) {
        return false;
    }

    public boolean func_176205_b(IBlockAccess iblockaccess, BlockPos blockposition) {
        return false;
    }

    public boolean func_149662_c(IBlockState iblockdata) {
        return false;
    }

    private boolean func_176253_e(IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        IBlockState iblockdata = iblockaccess.func_180495_p(blockposition);
        Block block = iblockdata.func_177230_c();
        BlockFaceShape enumblockfaceshape = iblockdata.func_193401_d(iblockaccess, blockposition, enumdirection);
        boolean flag = enumblockfaceshape == BlockFaceShape.MIDDLE_POLE_THICK || enumblockfaceshape == BlockFaceShape.MIDDLE_POLE && block instanceof BlockFenceGate;

        return !func_194143_e(block) && enumblockfaceshape == BlockFaceShape.SOLID || flag;
    }

    protected static boolean func_194143_e(Block block) {
        return Block.func_193382_c(block) || block == Blocks.field_180401_cv || block == Blocks.field_150440_ba || block == Blocks.field_150423_aK || block == Blocks.field_150428_aP;
    }

    public void func_149666_a(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        BlockWall.EnumType[] ablockcobblewall_enumcobblevariant = BlockWall.EnumType.values();
        int i = ablockcobblewall_enumcobblevariant.length;

        for (int j = 0; j < i; ++j) {
            BlockWall.EnumType blockcobblewall_enumcobblevariant = ablockcobblewall_enumcobblevariant[j];

            nonnulllist.add(new ItemStack(this, 1, blockcobblewall_enumcobblevariant.func_176657_a()));
        }

    }

    public int func_180651_a(IBlockState iblockdata) {
        return ((BlockWall.EnumType) iblockdata.func_177229_b(BlockWall.field_176255_P)).func_176657_a();
    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockWall.field_176255_P, BlockWall.EnumType.func_176660_a(i));
    }

    public int func_176201_c(IBlockState iblockdata) {
        return ((BlockWall.EnumType) iblockdata.func_177229_b(BlockWall.field_176255_P)).func_176657_a();
    }

    public IBlockState func_176221_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        boolean flag = this.func_176253_e(iblockaccess, blockposition.func_177978_c(), EnumFacing.SOUTH);
        boolean flag1 = this.func_176253_e(iblockaccess, blockposition.func_177974_f(), EnumFacing.WEST);
        boolean flag2 = this.func_176253_e(iblockaccess, blockposition.func_177968_d(), EnumFacing.NORTH);
        boolean flag3 = this.func_176253_e(iblockaccess, blockposition.func_177976_e(), EnumFacing.EAST);
        boolean flag4 = flag && !flag1 && flag2 && !flag3 || !flag && flag1 && !flag2 && flag3;

        return iblockdata.func_177226_a(BlockWall.field_176256_a, Boolean.valueOf(!flag4 || !iblockaccess.func_175623_d(blockposition.func_177984_a()))).func_177226_a(BlockWall.field_176254_b, Boolean.valueOf(flag)).func_177226_a(BlockWall.field_176257_M, Boolean.valueOf(flag1)).func_177226_a(BlockWall.field_176258_N, Boolean.valueOf(flag2)).func_177226_a(BlockWall.field_176259_O, Boolean.valueOf(flag3));
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockWall.field_176256_a, BlockWall.field_176254_b, BlockWall.field_176257_M, BlockWall.field_176259_O, BlockWall.field_176258_N, BlockWall.field_176255_P});
    }

    public BlockFaceShape func_193383_a(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return enumdirection != EnumFacing.UP && enumdirection != EnumFacing.DOWN ? BlockFaceShape.MIDDLE_POLE_THICK : BlockFaceShape.CENTER_BIG;
    }

    public static enum EnumType implements IStringSerializable {

        NORMAL(0, "cobblestone", "normal"), MOSSY(1, "mossy_cobblestone", "mossy");

        private static final BlockWall.EnumType[] field_176666_c = new BlockWall.EnumType[values().length];
        private final int field_176663_d;
        private final String field_176664_e;
        private final String field_176661_f;

        private EnumType(int i, String s, String s1) {
            this.field_176663_d = i;
            this.field_176664_e = s;
            this.field_176661_f = s1;
        }

        public int func_176657_a() {
            return this.field_176663_d;
        }

        public String toString() {
            return this.field_176664_e;
        }

        public static BlockWall.EnumType func_176660_a(int i) {
            if (i < 0 || i >= BlockWall.EnumType.field_176666_c.length) {
                i = 0;
            }

            return BlockWall.EnumType.field_176666_c[i];
        }

        public String func_176610_l() {
            return this.field_176664_e;
        }

        public String func_176659_c() {
            return this.field_176661_f;
        }

        static {
            BlockWall.EnumType[] ablockcobblewall_enumcobblevariant = values();
            int i = ablockcobblewall_enumcobblevariant.length;

            for (int j = 0; j < i; ++j) {
                BlockWall.EnumType blockcobblewall_enumcobblevariant = ablockcobblewall_enumcobblevariant[j];

                BlockWall.EnumType.field_176666_c[blockcobblewall_enumcobblevariant.func_176657_a()] = blockcobblewall_enumcobblevariant;
            }

        }
    }
}
