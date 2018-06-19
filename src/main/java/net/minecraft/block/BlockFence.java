package net.minecraft.block;

import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemLead;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFence extends Block {

    public static final PropertyBool field_176526_a = PropertyBool.func_177716_a("north");
    public static final PropertyBool field_176525_b = PropertyBool.func_177716_a("east");
    public static final PropertyBool field_176527_M = PropertyBool.func_177716_a("south");
    public static final PropertyBool field_176528_N = PropertyBool.func_177716_a("west");
    protected static final AxisAlignedBB[] field_185670_e = new AxisAlignedBB[] { new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 1.0D, 0.625D), new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.375D, 0.625D, 1.0D, 0.625D), new AxisAlignedBB(0.0D, 0.0D, 0.375D, 0.625D, 1.0D, 1.0D), new AxisAlignedBB(0.375D, 0.0D, 0.0D, 0.625D, 1.0D, 0.625D), new AxisAlignedBB(0.375D, 0.0D, 0.0D, 0.625D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.625D, 1.0D, 0.625D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.625D, 1.0D, 1.0D), new AxisAlignedBB(0.375D, 0.0D, 0.375D, 1.0D, 1.0D, 0.625D), new AxisAlignedBB(0.375D, 0.0D, 0.375D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.375D, 1.0D, 1.0D, 0.625D), new AxisAlignedBB(0.0D, 0.0D, 0.375D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.375D, 0.0D, 0.0D, 1.0D, 1.0D, 0.625D), new AxisAlignedBB(0.375D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.625D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)};
    public static final AxisAlignedBB field_185671_f = new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 1.5D, 0.625D);
    public static final AxisAlignedBB field_185672_g = new AxisAlignedBB(0.375D, 0.0D, 0.625D, 0.625D, 1.5D, 1.0D);
    public static final AxisAlignedBB field_185667_B = new AxisAlignedBB(0.0D, 0.0D, 0.375D, 0.375D, 1.5D, 0.625D);
    public static final AxisAlignedBB field_185668_C = new AxisAlignedBB(0.375D, 0.0D, 0.0D, 0.625D, 1.5D, 0.375D);
    public static final AxisAlignedBB field_185669_D = new AxisAlignedBB(0.625D, 0.0D, 0.375D, 1.0D, 1.5D, 0.625D);

    public BlockFence(Material material, MapColor materialmapcolor) {
        super(material, materialmapcolor);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockFence.field_176526_a, Boolean.valueOf(false)).func_177226_a(BlockFence.field_176525_b, Boolean.valueOf(false)).func_177226_a(BlockFence.field_176527_M, Boolean.valueOf(false)).func_177226_a(BlockFence.field_176528_N, Boolean.valueOf(false)));
        this.func_149647_a(CreativeTabs.field_78031_c);
    }

    public void func_185477_a(IBlockState iblockdata, World world, BlockPos blockposition, AxisAlignedBB axisalignedbb, List<AxisAlignedBB> list, @Nullable Entity entity, boolean flag) {
        if (!flag) {
            iblockdata = iblockdata.func_185899_b(world, blockposition);
        }

        func_185492_a(blockposition, axisalignedbb, list, BlockFence.field_185671_f);
        if (((Boolean) iblockdata.func_177229_b(BlockFence.field_176526_a)).booleanValue()) {
            func_185492_a(blockposition, axisalignedbb, list, BlockFence.field_185668_C);
        }

        if (((Boolean) iblockdata.func_177229_b(BlockFence.field_176525_b)).booleanValue()) {
            func_185492_a(blockposition, axisalignedbb, list, BlockFence.field_185669_D);
        }

        if (((Boolean) iblockdata.func_177229_b(BlockFence.field_176527_M)).booleanValue()) {
            func_185492_a(blockposition, axisalignedbb, list, BlockFence.field_185672_g);
        }

        if (((Boolean) iblockdata.func_177229_b(BlockFence.field_176528_N)).booleanValue()) {
            func_185492_a(blockposition, axisalignedbb, list, BlockFence.field_185667_B);
        }

    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        iblockdata = this.func_176221_a(iblockdata, iblockaccess, blockposition);
        return BlockFence.field_185670_e[func_185666_i(iblockdata)];
    }

    private static int func_185666_i(IBlockState iblockdata) {
        int i = 0;

        if (((Boolean) iblockdata.func_177229_b(BlockFence.field_176526_a)).booleanValue()) {
            i |= 1 << EnumFacing.NORTH.func_176736_b();
        }

        if (((Boolean) iblockdata.func_177229_b(BlockFence.field_176525_b)).booleanValue()) {
            i |= 1 << EnumFacing.EAST.func_176736_b();
        }

        if (((Boolean) iblockdata.func_177229_b(BlockFence.field_176527_M)).booleanValue()) {
            i |= 1 << EnumFacing.SOUTH.func_176736_b();
        }

        if (((Boolean) iblockdata.func_177229_b(BlockFence.field_176528_N)).booleanValue()) {
            i |= 1 << EnumFacing.WEST.func_176736_b();
        }

        return i;
    }

    public boolean func_149662_c(IBlockState iblockdata) {
        return false;
    }

    public boolean func_149686_d(IBlockState iblockdata) {
        return false;
    }

    public boolean func_176205_b(IBlockAccess iblockaccess, BlockPos blockposition) {
        return false;
    }

    public boolean func_176524_e(IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        IBlockState iblockdata = iblockaccess.func_180495_p(blockposition);
        BlockFaceShape enumblockfaceshape = iblockdata.func_193401_d(iblockaccess, blockposition, enumdirection);
        Block block = iblockdata.func_177230_c();
        boolean flag = enumblockfaceshape == BlockFaceShape.MIDDLE_POLE && (iblockdata.func_185904_a() == this.field_149764_J || block instanceof BlockFenceGate);

        return !func_194142_e(block) && enumblockfaceshape == BlockFaceShape.SOLID || flag;
    }

    protected static boolean func_194142_e(Block block) {
        return Block.func_193382_c(block) || block == Blocks.field_180401_cv || block == Blocks.field_150440_ba || block == Blocks.field_150423_aK || block == Blocks.field_150428_aP;
    }

    public boolean func_180639_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (!world.field_72995_K) {
            return ItemLead.func_180618_a(entityhuman, world, blockposition);
        } else {
            ItemStack itemstack = entityhuman.func_184586_b(enumhand);

            return itemstack.func_77973_b() == Items.field_151058_ca || itemstack.func_190926_b();
        }
    }

    public int func_176201_c(IBlockState iblockdata) {
        return 0;
    }

    public IBlockState func_176221_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return iblockdata.func_177226_a(BlockFence.field_176526_a, Boolean.valueOf(this.func_176524_e(iblockaccess, blockposition.func_177978_c(), EnumFacing.SOUTH))).func_177226_a(BlockFence.field_176525_b, Boolean.valueOf(this.func_176524_e(iblockaccess, blockposition.func_177974_f(), EnumFacing.WEST))).func_177226_a(BlockFence.field_176527_M, Boolean.valueOf(this.func_176524_e(iblockaccess, blockposition.func_177968_d(), EnumFacing.NORTH))).func_177226_a(BlockFence.field_176528_N, Boolean.valueOf(this.func_176524_e(iblockaccess, blockposition.func_177976_e(), EnumFacing.EAST)));
    }

    public IBlockState func_185499_a(IBlockState iblockdata, Rotation enumblockrotation) {
        switch (enumblockrotation) {
        case CLOCKWISE_180:
            return iblockdata.func_177226_a(BlockFence.field_176526_a, iblockdata.func_177229_b(BlockFence.field_176527_M)).func_177226_a(BlockFence.field_176525_b, iblockdata.func_177229_b(BlockFence.field_176528_N)).func_177226_a(BlockFence.field_176527_M, iblockdata.func_177229_b(BlockFence.field_176526_a)).func_177226_a(BlockFence.field_176528_N, iblockdata.func_177229_b(BlockFence.field_176525_b));

        case COUNTERCLOCKWISE_90:
            return iblockdata.func_177226_a(BlockFence.field_176526_a, iblockdata.func_177229_b(BlockFence.field_176525_b)).func_177226_a(BlockFence.field_176525_b, iblockdata.func_177229_b(BlockFence.field_176527_M)).func_177226_a(BlockFence.field_176527_M, iblockdata.func_177229_b(BlockFence.field_176528_N)).func_177226_a(BlockFence.field_176528_N, iblockdata.func_177229_b(BlockFence.field_176526_a));

        case CLOCKWISE_90:
            return iblockdata.func_177226_a(BlockFence.field_176526_a, iblockdata.func_177229_b(BlockFence.field_176528_N)).func_177226_a(BlockFence.field_176525_b, iblockdata.func_177229_b(BlockFence.field_176526_a)).func_177226_a(BlockFence.field_176527_M, iblockdata.func_177229_b(BlockFence.field_176525_b)).func_177226_a(BlockFence.field_176528_N, iblockdata.func_177229_b(BlockFence.field_176527_M));

        default:
            return iblockdata;
        }
    }

    public IBlockState func_185471_a(IBlockState iblockdata, Mirror enumblockmirror) {
        switch (enumblockmirror) {
        case LEFT_RIGHT:
            return iblockdata.func_177226_a(BlockFence.field_176526_a, iblockdata.func_177229_b(BlockFence.field_176527_M)).func_177226_a(BlockFence.field_176527_M, iblockdata.func_177229_b(BlockFence.field_176526_a));

        case FRONT_BACK:
            return iblockdata.func_177226_a(BlockFence.field_176525_b, iblockdata.func_177229_b(BlockFence.field_176528_N)).func_177226_a(BlockFence.field_176528_N, iblockdata.func_177229_b(BlockFence.field_176525_b));

        default:
            return super.func_185471_a(iblockdata, enumblockmirror);
        }
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockFence.field_176526_a, BlockFence.field_176525_b, BlockFence.field_176528_N, BlockFence.field_176527_M});
    }

    public BlockFaceShape func_193383_a(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return enumdirection != EnumFacing.UP && enumdirection != EnumFacing.DOWN ? BlockFaceShape.MIDDLE_POLE : BlockFaceShape.CENTER;
    }
}
