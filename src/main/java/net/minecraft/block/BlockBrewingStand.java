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
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBrewingStand extends BlockContainer {

    public static final PropertyBool[] field_176451_a = new PropertyBool[] { PropertyBool.func_177716_a("has_bottle_0"), PropertyBool.func_177716_a("has_bottle_1"), PropertyBool.func_177716_a("has_bottle_2")};
    protected static final AxisAlignedBB field_185555_b = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);
    protected static final AxisAlignedBB field_185556_c = new AxisAlignedBB(0.4375D, 0.0D, 0.4375D, 0.5625D, 0.875D, 0.5625D);

    public BlockBrewingStand() {
        super(Material.field_151573_f);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockBrewingStand.field_176451_a[0], Boolean.valueOf(false)).func_177226_a(BlockBrewingStand.field_176451_a[1], Boolean.valueOf(false)).func_177226_a(BlockBrewingStand.field_176451_a[2], Boolean.valueOf(false)));
    }

    public String func_149732_F() {
        return I18n.func_74838_a("item.brewingStand.name");
    }

    public boolean func_149662_c(IBlockState iblockdata) {
        return false;
    }

    public EnumBlockRenderType func_149645_b(IBlockState iblockdata) {
        return EnumBlockRenderType.MODEL;
    }

    public TileEntity func_149915_a(World world, int i) {
        return new TileEntityBrewingStand();
    }

    public boolean func_149686_d(IBlockState iblockdata) {
        return false;
    }

    public void func_185477_a(IBlockState iblockdata, World world, BlockPos blockposition, AxisAlignedBB axisalignedbb, List<AxisAlignedBB> list, @Nullable Entity entity, boolean flag) {
        func_185492_a(blockposition, axisalignedbb, list, BlockBrewingStand.field_185556_c);
        func_185492_a(blockposition, axisalignedbb, list, BlockBrewingStand.field_185555_b);
    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockBrewingStand.field_185555_b;
    }

    public boolean func_180639_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (world.field_72995_K) {
            return true;
        } else {
            TileEntity tileentity = world.func_175625_s(blockposition);

            if (tileentity instanceof TileEntityBrewingStand) {
                entityhuman.func_71007_a((TileEntityBrewingStand) tileentity);
                entityhuman.func_71029_a(StatList.field_188081_O);
            }

            return true;
        }
    }

    public void func_180633_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityLivingBase entityliving, ItemStack itemstack) {
        if (itemstack.func_82837_s()) {
            TileEntity tileentity = world.func_175625_s(blockposition);

            if (tileentity instanceof TileEntityBrewingStand) {
                ((TileEntityBrewingStand) tileentity).func_145937_a(itemstack.func_82833_r());
            }
        }

    }

    public void func_180663_b(World world, BlockPos blockposition, IBlockState iblockdata) {
        TileEntity tileentity = world.func_175625_s(blockposition);

        if (tileentity instanceof TileEntityBrewingStand) {
            InventoryHelper.func_180175_a(world, blockposition, (TileEntityBrewingStand) tileentity);
        }

        super.func_180663_b(world, blockposition, iblockdata);
    }

    public Item func_180660_a(IBlockState iblockdata, Random random, int i) {
        return Items.field_151067_bt;
    }

    public ItemStack func_185473_a(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(Items.field_151067_bt);
    }

    public boolean func_149740_M(IBlockState iblockdata) {
        return true;
    }

    public int func_180641_l(IBlockState iblockdata, World world, BlockPos blockposition) {
        return Container.func_178144_a(world.func_175625_s(blockposition));
    }

    public IBlockState func_176203_a(int i) {
        IBlockState iblockdata = this.func_176223_P();

        for (int j = 0; j < 3; ++j) {
            iblockdata = iblockdata.func_177226_a(BlockBrewingStand.field_176451_a[j], Boolean.valueOf((i & 1 << j) > 0));
        }

        return iblockdata;
    }

    public int func_176201_c(IBlockState iblockdata) {
        int i = 0;

        for (int j = 0; j < 3; ++j) {
            if (((Boolean) iblockdata.func_177229_b(BlockBrewingStand.field_176451_a[j])).booleanValue()) {
                i |= 1 << j;
            }
        }

        return i;
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockBrewingStand.field_176451_a[0], BlockBrewingStand.field_176451_a[1], BlockBrewingStand.field_176451_a[2]});
    }

    public BlockFaceShape func_193383_a(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }
}
