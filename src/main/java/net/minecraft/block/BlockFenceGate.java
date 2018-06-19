package net.minecraft.block;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFenceGate extends BlockHorizontal {

    public static final PropertyBool field_176466_a = PropertyBool.func_177716_a("open");
    public static final PropertyBool field_176465_b = PropertyBool.func_177716_a("powered");
    public static final PropertyBool field_176467_M = PropertyBool.func_177716_a("in_wall");
    protected static final AxisAlignedBB field_185541_d = new AxisAlignedBB(0.0D, 0.0D, 0.375D, 1.0D, 1.0D, 0.625D);
    protected static final AxisAlignedBB field_185542_e = new AxisAlignedBB(0.375D, 0.0D, 0.0D, 0.625D, 1.0D, 1.0D);
    protected static final AxisAlignedBB field_185543_f = new AxisAlignedBB(0.0D, 0.0D, 0.375D, 1.0D, 0.8125D, 0.625D);
    protected static final AxisAlignedBB field_185544_g = new AxisAlignedBB(0.375D, 0.0D, 0.0D, 0.625D, 0.8125D, 1.0D);
    protected static final AxisAlignedBB field_185539_B = new AxisAlignedBB(0.0D, 0.0D, 0.375D, 1.0D, 1.5D, 0.625D);
    protected static final AxisAlignedBB field_185540_C = new AxisAlignedBB(0.375D, 0.0D, 0.0D, 0.625D, 1.5D, 1.0D);

    public BlockFenceGate(BlockPlanks.EnumType blockwood_enumlogvariant) {
        super(Material.field_151575_d, blockwood_enumlogvariant.func_181070_c());
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockFenceGate.field_176466_a, Boolean.valueOf(false)).func_177226_a(BlockFenceGate.field_176465_b, Boolean.valueOf(false)).func_177226_a(BlockFenceGate.field_176467_M, Boolean.valueOf(false)));
        this.func_149647_a(CreativeTabs.field_78028_d);
    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        iblockdata = this.func_176221_a(iblockdata, iblockaccess, blockposition);
        return ((Boolean) iblockdata.func_177229_b(BlockFenceGate.field_176467_M)).booleanValue() ? (((EnumFacing) iblockdata.func_177229_b(BlockFenceGate.field_185512_D)).func_176740_k() == EnumFacing.Axis.X ? BlockFenceGate.field_185544_g : BlockFenceGate.field_185543_f) : (((EnumFacing) iblockdata.func_177229_b(BlockFenceGate.field_185512_D)).func_176740_k() == EnumFacing.Axis.X ? BlockFenceGate.field_185542_e : BlockFenceGate.field_185541_d);
    }

    public IBlockState func_176221_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        EnumFacing.Axis enumdirection_enumaxis = ((EnumFacing) iblockdata.func_177229_b(BlockFenceGate.field_185512_D)).func_176740_k();

        if (enumdirection_enumaxis == EnumFacing.Axis.Z && (iblockaccess.func_180495_p(blockposition.func_177976_e()).func_177230_c() == Blocks.field_150463_bK || iblockaccess.func_180495_p(blockposition.func_177974_f()).func_177230_c() == Blocks.field_150463_bK) || enumdirection_enumaxis == EnumFacing.Axis.X && (iblockaccess.func_180495_p(blockposition.func_177978_c()).func_177230_c() == Blocks.field_150463_bK || iblockaccess.func_180495_p(blockposition.func_177968_d()).func_177230_c() == Blocks.field_150463_bK)) {
            iblockdata = iblockdata.func_177226_a(BlockFenceGate.field_176467_M, Boolean.valueOf(true));
        }

        return iblockdata;
    }

    public IBlockState func_185499_a(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.func_177226_a(BlockFenceGate.field_185512_D, enumblockrotation.func_185831_a((EnumFacing) iblockdata.func_177229_b(BlockFenceGate.field_185512_D)));
    }

    public IBlockState func_185471_a(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata.func_185907_a(enumblockmirror.func_185800_a((EnumFacing) iblockdata.func_177229_b(BlockFenceGate.field_185512_D)));
    }

    public boolean func_176196_c(World world, BlockPos blockposition) {
        return world.func_180495_p(blockposition.func_177977_b()).func_185904_a().func_76220_a() ? super.func_176196_c(world, blockposition) : false;
    }

    @Nullable
    public AxisAlignedBB func_180646_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return ((Boolean) iblockdata.func_177229_b(BlockFenceGate.field_176466_a)).booleanValue() ? BlockFenceGate.field_185506_k : (((EnumFacing) iblockdata.func_177229_b(BlockFenceGate.field_185512_D)).func_176740_k() == EnumFacing.Axis.Z ? BlockFenceGate.field_185539_B : BlockFenceGate.field_185540_C);
    }

    public boolean func_149662_c(IBlockState iblockdata) {
        return false;
    }

    public boolean func_149686_d(IBlockState iblockdata) {
        return false;
    }

    public boolean func_176205_b(IBlockAccess iblockaccess, BlockPos blockposition) {
        return ((Boolean) iblockaccess.func_180495_p(blockposition).func_177229_b(BlockFenceGate.field_176466_a)).booleanValue();
    }

    public IBlockState func_180642_a(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        boolean flag = world.func_175640_z(blockposition);

        return this.func_176223_P().func_177226_a(BlockFenceGate.field_185512_D, entityliving.func_174811_aO()).func_177226_a(BlockFenceGate.field_176466_a, Boolean.valueOf(flag)).func_177226_a(BlockFenceGate.field_176465_b, Boolean.valueOf(flag)).func_177226_a(BlockFenceGate.field_176467_M, Boolean.valueOf(false));
    }

    public boolean func_180639_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (((Boolean) iblockdata.func_177229_b(BlockFenceGate.field_176466_a)).booleanValue()) {
            iblockdata = iblockdata.func_177226_a(BlockFenceGate.field_176466_a, Boolean.valueOf(false));
            world.func_180501_a(blockposition, iblockdata, 10);
        } else {
            EnumFacing enumdirection1 = EnumFacing.func_176733_a((double) entityhuman.field_70177_z);

            if (iblockdata.func_177229_b(BlockFenceGate.field_185512_D) == enumdirection1.func_176734_d()) {
                iblockdata = iblockdata.func_177226_a(BlockFenceGate.field_185512_D, enumdirection1);
            }

            iblockdata = iblockdata.func_177226_a(BlockFenceGate.field_176466_a, Boolean.valueOf(true));
            world.func_180501_a(blockposition, iblockdata, 10);
        }

        world.func_180498_a(entityhuman, ((Boolean) iblockdata.func_177229_b(BlockFenceGate.field_176466_a)).booleanValue() ? 1008 : 1014, blockposition, 0);
        return true;
    }

    public void func_189540_a(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        if (!world.field_72995_K) {
            boolean flag = world.func_175640_z(blockposition);

            if (((Boolean) iblockdata.func_177229_b(BlockFenceGate.field_176465_b)).booleanValue() != flag) {
                world.func_180501_a(blockposition, iblockdata.func_177226_a(BlockFenceGate.field_176465_b, Boolean.valueOf(flag)).func_177226_a(BlockFenceGate.field_176466_a, Boolean.valueOf(flag)), 2);
                if (((Boolean) iblockdata.func_177229_b(BlockFenceGate.field_176466_a)).booleanValue() != flag) {
                    world.func_180498_a((EntityPlayer) null, flag ? 1008 : 1014, blockposition, 0);
                }
            }

        }
    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockFenceGate.field_185512_D, EnumFacing.func_176731_b(i)).func_177226_a(BlockFenceGate.field_176466_a, Boolean.valueOf((i & 4) != 0)).func_177226_a(BlockFenceGate.field_176465_b, Boolean.valueOf((i & 8) != 0));
    }

    public int func_176201_c(IBlockState iblockdata) {
        byte b0 = 0;
        int i = b0 | ((EnumFacing) iblockdata.func_177229_b(BlockFenceGate.field_185512_D)).func_176736_b();

        if (((Boolean) iblockdata.func_177229_b(BlockFenceGate.field_176465_b)).booleanValue()) {
            i |= 8;
        }

        if (((Boolean) iblockdata.func_177229_b(BlockFenceGate.field_176466_a)).booleanValue()) {
            i |= 4;
        }

        return i;
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockFenceGate.field_185512_D, BlockFenceGate.field_176466_a, BlockFenceGate.field_176465_b, BlockFenceGate.field_176467_M});
    }

    public BlockFaceShape func_193383_a(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return enumdirection != EnumFacing.UP && enumdirection != EnumFacing.DOWN ? (((EnumFacing) iblockdata.func_177229_b(BlockFenceGate.field_185512_D)).func_176740_k() == enumdirection.func_176746_e().func_176740_k() ? BlockFaceShape.MIDDLE_POLE : BlockFaceShape.UNDEFINED) : BlockFaceShape.UNDEFINED;
    }
}
