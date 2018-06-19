package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockFurnace extends BlockContainer {

    public static final PropertyDirection field_176447_a = BlockHorizontal.field_185512_D;
    private final boolean field_149932_b;
    private static boolean field_149934_M;

    protected BlockFurnace(boolean flag) {
        super(Material.field_151576_e);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockFurnace.field_176447_a, EnumFacing.NORTH));
        this.field_149932_b = flag;
    }

    public Item func_180660_a(IBlockState iblockdata, Random random, int i) {
        return Item.func_150898_a(Blocks.field_150460_al);
    }

    // Paper start - Removed override of onPlace that was reversing placement direction when
    // adjacent to another block, which was not consistent with single player block placement
    /*
    public void onPlace(World world, BlockPosition blockposition, IBlockData iblockdata) {
        this.e(world, blockposition, iblockdata);
    }

    private void e(World world, BlockPosition blockposition, IBlockData iblockdata) {
        if (!world.isClientSide) {
            IBlockData iblockdata1 = world.getType(blockposition.north());
            IBlockData iblockdata2 = world.getType(blockposition.south());
            IBlockData iblockdata3 = world.getType(blockposition.west());
            IBlockData iblockdata4 = world.getType(blockposition.east());
            EnumDirection enumdirection = (EnumDirection) iblockdata.get(BlockFurnace.FACING);

            if (enumdirection == EnumDirection.NORTH && iblockdata1.b() && !iblockdata2.b()) {
                enumdirection = EnumDirection.SOUTH;
            } else if (enumdirection == EnumDirection.SOUTH && iblockdata2.b() && !iblockdata1.b()) {
                enumdirection = EnumDirection.NORTH;
            } else if (enumdirection == EnumDirection.WEST && iblockdata3.b() && !iblockdata4.b()) {
                enumdirection = EnumDirection.EAST;
            } else if (enumdirection == EnumDirection.EAST && iblockdata4.b() && !iblockdata3.b()) {
                enumdirection = EnumDirection.WEST;
            }

            world.setTypeAndData(blockposition, iblockdata.set(BlockFurnace.FACING, enumdirection), 2);
        }
    }
    */
    // Paper end

    public boolean func_180639_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (world.field_72995_K) {
            return true;
        } else {
            TileEntity tileentity = world.func_175625_s(blockposition);

            if (tileentity instanceof TileEntityFurnace) {
                entityhuman.func_71007_a((TileEntityFurnace) tileentity);
                entityhuman.func_71029_a(StatList.field_188061_aa);
            }

            return true;
        }
    }

    public static void func_176446_a(boolean flag, World world, BlockPos blockposition) {
        IBlockState iblockdata = world.func_180495_p(blockposition);
        TileEntity tileentity = world.func_175625_s(blockposition);

        BlockFurnace.field_149934_M = true;
        if (flag) {
            world.func_180501_a(blockposition, Blocks.field_150470_am.func_176223_P().func_177226_a(BlockFurnace.field_176447_a, iblockdata.func_177229_b(BlockFurnace.field_176447_a)), 3);
            world.func_180501_a(blockposition, Blocks.field_150470_am.func_176223_P().func_177226_a(BlockFurnace.field_176447_a, iblockdata.func_177229_b(BlockFurnace.field_176447_a)), 3);
        } else {
            world.func_180501_a(blockposition, Blocks.field_150460_al.func_176223_P().func_177226_a(BlockFurnace.field_176447_a, iblockdata.func_177229_b(BlockFurnace.field_176447_a)), 3);
            world.func_180501_a(blockposition, Blocks.field_150460_al.func_176223_P().func_177226_a(BlockFurnace.field_176447_a, iblockdata.func_177229_b(BlockFurnace.field_176447_a)), 3);
        }

        BlockFurnace.field_149934_M = false;
        if (tileentity != null) {
            tileentity.func_145829_t();
            world.func_175690_a(blockposition, tileentity);
        }

    }

    public TileEntity func_149915_a(World world, int i) {
        return new TileEntityFurnace();
    }

    public IBlockState func_180642_a(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        return this.func_176223_P().func_177226_a(BlockFurnace.field_176447_a, entityliving.func_174811_aO().func_176734_d());
    }

    public void func_180633_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityLivingBase entityliving, ItemStack itemstack) {
        world.func_180501_a(blockposition, iblockdata.func_177226_a(BlockFurnace.field_176447_a, entityliving.func_174811_aO().func_176734_d()), 2);
        if (itemstack.func_82837_s()) {
            TileEntity tileentity = world.func_175625_s(blockposition);

            if (tileentity instanceof TileEntityFurnace) {
                ((TileEntityFurnace) tileentity).func_145951_a(itemstack.func_82833_r());
            }
        }

    }

    public void func_180663_b(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (!BlockFurnace.field_149934_M) {
            TileEntity tileentity = world.func_175625_s(blockposition);

            if (tileentity instanceof TileEntityFurnace) {
                InventoryHelper.func_180175_a(world, blockposition, (TileEntityFurnace) tileentity);
                world.func_175666_e(blockposition, this);
            }
        }

        super.func_180663_b(world, blockposition, iblockdata);
    }

    public boolean func_149740_M(IBlockState iblockdata) {
        return true;
    }

    public int func_180641_l(IBlockState iblockdata, World world, BlockPos blockposition) {
        return Container.func_178144_a(world.func_175625_s(blockposition));
    }

    public ItemStack func_185473_a(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(Blocks.field_150460_al);
    }

    public EnumBlockRenderType func_149645_b(IBlockState iblockdata) {
        return EnumBlockRenderType.MODEL;
    }

    public IBlockState func_176203_a(int i) {
        EnumFacing enumdirection = EnumFacing.func_82600_a(i);

        if (enumdirection.func_176740_k() == EnumFacing.Axis.Y) {
            enumdirection = EnumFacing.NORTH;
        }

        return this.func_176223_P().func_177226_a(BlockFurnace.field_176447_a, enumdirection);
    }

    public int func_176201_c(IBlockState iblockdata) {
        return ((EnumFacing) iblockdata.func_177229_b(BlockFurnace.field_176447_a)).func_176745_a();
    }

    public IBlockState func_185499_a(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.func_177226_a(BlockFurnace.field_176447_a, enumblockrotation.func_185831_a((EnumFacing) iblockdata.func_177229_b(BlockFurnace.field_176447_a)));
    }

    public IBlockState func_185471_a(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata.func_185907_a(enumblockmirror.func_185800_a((EnumFacing) iblockdata.func_177229_b(BlockFurnace.field_176447_a)));
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockFurnace.field_176447_a});
    }
}
