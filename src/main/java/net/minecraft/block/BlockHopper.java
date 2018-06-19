package net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockHopper extends BlockContainer {

    public static final PropertyDirection field_176430_a = PropertyDirection.func_177712_a("facing", new Predicate() {
        public boolean a(@Nullable EnumFacing enumdirection) {
            return enumdirection != EnumFacing.UP;
        }

        public boolean apply(@Nullable Object object) {
            return this.a((EnumFacing) object);
        }
    });
    public static final PropertyBool field_176429_b = PropertyBool.func_177716_a("enabled");
    protected static final AxisAlignedBB field_185571_c = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.625D, 1.0D);
    protected static final AxisAlignedBB field_185572_d = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.125D);
    protected static final AxisAlignedBB field_185573_e = new AxisAlignedBB(0.0D, 0.0D, 0.875D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB field_185574_f = new AxisAlignedBB(0.875D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB field_185575_g = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.125D, 1.0D, 1.0D);

    public BlockHopper() {
        super(Material.field_151573_f, MapColor.field_151665_m);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockHopper.field_176430_a, EnumFacing.DOWN).func_177226_a(BlockHopper.field_176429_b, Boolean.valueOf(true)));
        this.func_149647_a(CreativeTabs.field_78028_d);
    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockHopper.field_185505_j;
    }

    public void func_185477_a(IBlockState iblockdata, World world, BlockPos blockposition, AxisAlignedBB axisalignedbb, List<AxisAlignedBB> list, @Nullable Entity entity, boolean flag) {
        func_185492_a(blockposition, axisalignedbb, list, BlockHopper.field_185571_c);
        func_185492_a(blockposition, axisalignedbb, list, BlockHopper.field_185575_g);
        func_185492_a(blockposition, axisalignedbb, list, BlockHopper.field_185574_f);
        func_185492_a(blockposition, axisalignedbb, list, BlockHopper.field_185572_d);
        func_185492_a(blockposition, axisalignedbb, list, BlockHopper.field_185573_e);
    }

    public IBlockState func_180642_a(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        EnumFacing enumdirection1 = enumdirection.func_176734_d();

        if (enumdirection1 == EnumFacing.UP) {
            enumdirection1 = EnumFacing.DOWN;
        }

        return this.func_176223_P().func_177226_a(BlockHopper.field_176430_a, enumdirection1).func_177226_a(BlockHopper.field_176429_b, Boolean.valueOf(true));
    }

    public TileEntity func_149915_a(World world, int i) {
        return new TileEntityHopper();
    }

    public void func_180633_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityLivingBase entityliving, ItemStack itemstack) {
        super.func_180633_a(world, blockposition, iblockdata, entityliving, itemstack);
        if (itemstack.func_82837_s()) {
            TileEntity tileentity = world.func_175625_s(blockposition);

            if (tileentity instanceof TileEntityHopper) {
                ((TileEntityHopper) tileentity).func_190575_a(itemstack.func_82833_r());
            }
        }

    }

    public boolean func_185481_k(IBlockState iblockdata) {
        return true;
    }

    public void func_176213_c(World world, BlockPos blockposition, IBlockState iblockdata) {
        this.func_176427_e(world, blockposition, iblockdata);
    }

    public boolean func_180639_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (world.field_72995_K) {
            return true;
        } else {
            TileEntity tileentity = world.func_175625_s(blockposition);

            if (tileentity instanceof TileEntityHopper) {
                entityhuman.func_71007_a((TileEntityHopper) tileentity);
                entityhuman.func_71029_a(StatList.field_188084_R);
            }

            return true;
        }
    }

    public void func_189540_a(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        this.func_176427_e(world, blockposition, iblockdata);
    }

    private void func_176427_e(World world, BlockPos blockposition, IBlockState iblockdata) {
        boolean flag = !world.func_175640_z(blockposition);

        if (flag != ((Boolean) iblockdata.func_177229_b(BlockHopper.field_176429_b)).booleanValue()) {
            world.func_180501_a(blockposition, iblockdata.func_177226_a(BlockHopper.field_176429_b, Boolean.valueOf(flag)), 4);
        }

    }

    public void func_180663_b(World world, BlockPos blockposition, IBlockState iblockdata) {
        TileEntity tileentity = world.func_175625_s(blockposition);

        if (tileentity instanceof TileEntityHopper) {
            InventoryHelper.func_180175_a(world, blockposition, (TileEntityHopper) tileentity);
            world.func_175666_e(blockposition, this);
        }

        super.func_180663_b(world, blockposition, iblockdata);
    }

    public EnumBlockRenderType func_149645_b(IBlockState iblockdata) {
        return EnumBlockRenderType.MODEL;
    }

    public boolean func_149686_d(IBlockState iblockdata) {
        return false;
    }

    public boolean func_149662_c(IBlockState iblockdata) {
        return false;
    }

    public static EnumFacing func_176428_b(int i) {
        return EnumFacing.func_82600_a(i & 7);
    }

    public static boolean func_149917_c(int i) {
        return (i & 8) != 8;
    }

    public boolean func_149740_M(IBlockState iblockdata) {
        return true;
    }

    public int func_180641_l(IBlockState iblockdata, World world, BlockPos blockposition) {
        return Container.func_178144_a(world.func_175625_s(blockposition));
    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockHopper.field_176430_a, func_176428_b(i)).func_177226_a(BlockHopper.field_176429_b, Boolean.valueOf(func_149917_c(i)));
    }

    public int func_176201_c(IBlockState iblockdata) {
        byte b0 = 0;
        int i = b0 | ((EnumFacing) iblockdata.func_177229_b(BlockHopper.field_176430_a)).func_176745_a();

        if (!((Boolean) iblockdata.func_177229_b(BlockHopper.field_176429_b)).booleanValue()) {
            i |= 8;
        }

        return i;
    }

    public IBlockState func_185499_a(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.func_177226_a(BlockHopper.field_176430_a, enumblockrotation.func_185831_a((EnumFacing) iblockdata.func_177229_b(BlockHopper.field_176430_a)));
    }

    public IBlockState func_185471_a(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata.func_185907_a(enumblockmirror.func_185800_a((EnumFacing) iblockdata.func_177229_b(BlockHopper.field_176430_a)));
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockHopper.field_176430_a, BlockHopper.field_176429_b});
    }

    public BlockFaceShape func_193383_a(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return enumdirection == EnumFacing.UP ? BlockFaceShape.BOWL : BlockFaceShape.UNDEFINED;
    }
}
