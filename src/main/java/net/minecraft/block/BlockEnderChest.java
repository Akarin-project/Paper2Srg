package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockEnderChest extends BlockContainer {

    public static final PropertyDirection field_176437_a = BlockHorizontal.field_185512_D;
    protected static final AxisAlignedBB field_185569_b = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D);

    protected BlockEnderChest() {
        super(Material.field_151576_e);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockEnderChest.field_176437_a, EnumFacing.NORTH));
        this.func_149647_a(CreativeTabs.field_78031_c);
    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockEnderChest.field_185569_b;
    }

    public boolean func_149662_c(IBlockState iblockdata) {
        return false;
    }

    public boolean func_149686_d(IBlockState iblockdata) {
        return false;
    }

    public EnumBlockRenderType func_149645_b(IBlockState iblockdata) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    public Item func_180660_a(IBlockState iblockdata, Random random, int i) {
        return Item.func_150898_a(Blocks.field_150343_Z);
    }

    public int func_149745_a(Random random) {
        return 8;
    }

    protected boolean func_149700_E() {
        return true;
    }

    public IBlockState func_180642_a(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        return this.func_176223_P().func_177226_a(BlockEnderChest.field_176437_a, entityliving.func_174811_aO().func_176734_d());
    }

    public void func_180633_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityLivingBase entityliving, ItemStack itemstack) {
        world.func_180501_a(blockposition, iblockdata.func_177226_a(BlockEnderChest.field_176437_a, entityliving.func_174811_aO().func_176734_d()), 2);
    }

    public boolean func_180639_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        InventoryEnderChest inventoryenderchest = entityhuman.func_71005_bN();
        TileEntity tileentity = world.func_175625_s(blockposition);

        if (inventoryenderchest != null && tileentity instanceof TileEntityEnderChest) {
            if (world.func_180495_p(blockposition.func_177984_a()).func_185915_l()) {
                return true;
            } else if (world.field_72995_K) {
                return true;
            } else {
                inventoryenderchest.func_146031_a((TileEntityEnderChest) tileentity);
                entityhuman.func_71007_a(inventoryenderchest);
                entityhuman.func_71029_a(StatList.field_188090_X);
                return true;
            }
        } else {
            return true;
        }
    }

    public TileEntity func_149915_a(World world, int i) {
        return new TileEntityEnderChest();
    }

    public IBlockState func_176203_a(int i) {
        EnumFacing enumdirection = EnumFacing.func_82600_a(i);

        if (enumdirection.func_176740_k() == EnumFacing.Axis.Y) {
            enumdirection = EnumFacing.NORTH;
        }

        return this.func_176223_P().func_177226_a(BlockEnderChest.field_176437_a, enumdirection);
    }

    public int func_176201_c(IBlockState iblockdata) {
        return ((EnumFacing) iblockdata.func_177229_b(BlockEnderChest.field_176437_a)).func_176745_a();
    }

    public IBlockState func_185499_a(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.func_177226_a(BlockEnderChest.field_176437_a, enumblockrotation.func_185831_a((EnumFacing) iblockdata.func_177229_b(BlockEnderChest.field_176437_a)));
    }

    public IBlockState func_185471_a(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata.func_185907_a(enumblockmirror.func_185800_a((EnumFacing) iblockdata.func_177229_b(BlockEnderChest.field_176437_a)));
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockEnderChest.field_176437_a});
    }

    public BlockFaceShape func_193383_a(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }
}
