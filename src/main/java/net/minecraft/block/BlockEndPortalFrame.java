package net.minecraft.block;

import com.google.common.base.Predicates;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
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

public class BlockEndPortalFrame extends Block {

    public static final PropertyDirection field_176508_a = BlockHorizontal.field_185512_D;
    public static final PropertyBool field_176507_b = PropertyBool.func_177716_a("eye");
    protected static final AxisAlignedBB field_185662_c = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.8125D, 1.0D);
    protected static final AxisAlignedBB field_185663_d = new AxisAlignedBB(0.3125D, 0.8125D, 0.3125D, 0.6875D, 1.0D, 0.6875D);
    private static BlockPattern field_185664_e;

    public BlockEndPortalFrame() {
        super(Material.field_151576_e, MapColor.field_151651_C);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockEndPortalFrame.field_176508_a, EnumFacing.NORTH).func_177226_a(BlockEndPortalFrame.field_176507_b, Boolean.valueOf(false)));
    }

    public boolean func_149662_c(IBlockState iblockdata) {
        return false;
    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockEndPortalFrame.field_185662_c;
    }

    public void func_185477_a(IBlockState iblockdata, World world, BlockPos blockposition, AxisAlignedBB axisalignedbb, List<AxisAlignedBB> list, @Nullable Entity entity, boolean flag) {
        func_185492_a(blockposition, axisalignedbb, list, BlockEndPortalFrame.field_185662_c);
        if (((Boolean) world.func_180495_p(blockposition).func_177229_b(BlockEndPortalFrame.field_176507_b)).booleanValue()) {
            func_185492_a(blockposition, axisalignedbb, list, BlockEndPortalFrame.field_185663_d);
        }

    }

    public Item func_180660_a(IBlockState iblockdata, Random random, int i) {
        return Items.field_190931_a;
    }

    public IBlockState func_180642_a(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        return this.func_176223_P().func_177226_a(BlockEndPortalFrame.field_176508_a, entityliving.func_174811_aO().func_176734_d()).func_177226_a(BlockEndPortalFrame.field_176507_b, Boolean.valueOf(false));
    }

    public boolean func_149740_M(IBlockState iblockdata) {
        return true;
    }

    public int func_180641_l(IBlockState iblockdata, World world, BlockPos blockposition) {
        return ((Boolean) iblockdata.func_177229_b(BlockEndPortalFrame.field_176507_b)).booleanValue() ? 15 : 0;
    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockEndPortalFrame.field_176507_b, Boolean.valueOf((i & 4) != 0)).func_177226_a(BlockEndPortalFrame.field_176508_a, EnumFacing.func_176731_b(i & 3));
    }

    public int func_176201_c(IBlockState iblockdata) {
        byte b0 = 0;
        int i = b0 | ((EnumFacing) iblockdata.func_177229_b(BlockEndPortalFrame.field_176508_a)).func_176736_b();

        if (((Boolean) iblockdata.func_177229_b(BlockEndPortalFrame.field_176507_b)).booleanValue()) {
            i |= 4;
        }

        return i;
    }

    public IBlockState func_185499_a(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.func_177226_a(BlockEndPortalFrame.field_176508_a, enumblockrotation.func_185831_a((EnumFacing) iblockdata.func_177229_b(BlockEndPortalFrame.field_176508_a)));
    }

    public IBlockState func_185471_a(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata.func_185907_a(enumblockmirror.func_185800_a((EnumFacing) iblockdata.func_177229_b(BlockEndPortalFrame.field_176508_a)));
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockEndPortalFrame.field_176508_a, BlockEndPortalFrame.field_176507_b});
    }

    public boolean func_149686_d(IBlockState iblockdata) {
        return false;
    }

    public static BlockPattern func_185661_e() {
        if (BlockEndPortalFrame.field_185664_e == null) {
            BlockEndPortalFrame.field_185664_e = FactoryBlockPattern.func_177660_a().func_177659_a(new String[] { "?vvv?", ">???<", ">???<", ">???<", "?^^^?"}).func_177662_a('?', BlockWorldState.func_177510_a(BlockStateMatcher.field_185928_a)).func_177662_a('^', BlockWorldState.func_177510_a(BlockStateMatcher.func_177638_a(Blocks.field_150378_br).func_177637_a(BlockEndPortalFrame.field_176507_b, Predicates.equalTo(Boolean.valueOf(true))).func_177637_a(BlockEndPortalFrame.field_176508_a, Predicates.equalTo(EnumFacing.SOUTH)))).func_177662_a('>', BlockWorldState.func_177510_a(BlockStateMatcher.func_177638_a(Blocks.field_150378_br).func_177637_a(BlockEndPortalFrame.field_176507_b, Predicates.equalTo(Boolean.valueOf(true))).func_177637_a(BlockEndPortalFrame.field_176508_a, Predicates.equalTo(EnumFacing.WEST)))).func_177662_a('v', BlockWorldState.func_177510_a(BlockStateMatcher.func_177638_a(Blocks.field_150378_br).func_177637_a(BlockEndPortalFrame.field_176507_b, Predicates.equalTo(Boolean.valueOf(true))).func_177637_a(BlockEndPortalFrame.field_176508_a, Predicates.equalTo(EnumFacing.NORTH)))).func_177662_a('<', BlockWorldState.func_177510_a(BlockStateMatcher.func_177638_a(Blocks.field_150378_br).func_177637_a(BlockEndPortalFrame.field_176507_b, Predicates.equalTo(Boolean.valueOf(true))).func_177637_a(BlockEndPortalFrame.field_176508_a, Predicates.equalTo(EnumFacing.EAST)))).func_177661_b();
        }

        return BlockEndPortalFrame.field_185664_e;
    }

    public BlockFaceShape func_193383_a(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return enumdirection == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }
}
