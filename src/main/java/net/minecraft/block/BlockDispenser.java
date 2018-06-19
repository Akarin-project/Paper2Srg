package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.dispenser.PositionImpl;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryDefaulted;
import net.minecraft.world.World;

public class BlockDispenser extends BlockContainer {

    public static final PropertyDirection field_176441_a = BlockDirectional.field_176387_N;
    public static final PropertyBool field_176440_b = PropertyBool.func_177716_a("triggered");
    public static final RegistryDefaulted<Item, IBehaviorDispenseItem> field_149943_a = new RegistryDefaulted(new BehaviorDefaultDispenseItem());
    protected Random field_149942_b = new Random();
    public static boolean eventFired = false; // CraftBukkit

    protected BlockDispenser() {
        super(Material.field_151576_e);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockDispenser.field_176441_a, EnumFacing.NORTH).func_177226_a(BlockDispenser.field_176440_b, Boolean.valueOf(false)));
        this.func_149647_a(CreativeTabs.field_78028_d);
    }

    public int func_149738_a(World world) {
        return 4;
    }

    // Paper start - Removed override of onPlace that was reversing placement direction when
    // adjacent to another block, which was not consistent with single player block placement
    /*
    public void onPlace(World world, BlockPosition blockposition, IBlockData iblockdata) {
        super.onPlace(world, blockposition, iblockdata);
        this.e(world, blockposition, iblockdata);
    }

    private void e(World world, BlockPosition blockposition, IBlockData iblockdata) {
        if (!world.isClientSide) {
            EnumDirection enumdirection = (EnumDirection) iblockdata.get(BlockDispenser.FACING);
            boolean flag = world.getType(blockposition.north()).b();
            boolean flag1 = world.getType(blockposition.south()).b();

            if (enumdirection == EnumDirection.NORTH && flag && !flag1) {
                enumdirection = EnumDirection.SOUTH;
            } else if (enumdirection == EnumDirection.SOUTH && flag1 && !flag) {
                enumdirection = EnumDirection.NORTH;
            } else {
                boolean flag2 = world.getType(blockposition.west()).b();
                boolean flag3 = world.getType(blockposition.east()).b();

                if (enumdirection == EnumDirection.WEST && flag2 && !flag3) {
                    enumdirection = EnumDirection.EAST;
                } else if (enumdirection == EnumDirection.EAST && flag3 && !flag2) {
                    enumdirection = EnumDirection.WEST;
                }
            }

            world.setTypeAndData(blockposition, iblockdata.set(BlockDispenser.FACING, enumdirection).set(BlockDispenser.TRIGGERED, Boolean.valueOf(false)), 2);
        }
    }
    */
    // Paper end

    public boolean func_180639_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (world.field_72995_K) {
            return true;
        } else {
            TileEntity tileentity = world.func_175625_s(blockposition);

            if (tileentity instanceof TileEntityDispenser) {
                entityhuman.func_71007_a((TileEntityDispenser) tileentity);
                if (tileentity instanceof TileEntityDropper) {
                    entityhuman.func_71029_a(StatList.field_188083_Q);
                } else {
                    entityhuman.func_71029_a(StatList.field_188085_S);
                }
            }

            return true;
        }
    }

    public void func_176439_d(World world, BlockPos blockposition) {
        BlockSourceImpl sourceblock = new BlockSourceImpl(world, blockposition);
        TileEntityDispenser tileentitydispenser = (TileEntityDispenser) sourceblock.func_150835_j();

        if (tileentitydispenser != null) {
            int i = tileentitydispenser.func_146017_i();

            if (i < 0) {
                world.func_175718_b(1001, blockposition, 0);
            } else {
                ItemStack itemstack = tileentitydispenser.func_70301_a(i);
                IBehaviorDispenseItem idispensebehavior = this.func_149940_a(itemstack);

                if (idispensebehavior != IBehaviorDispenseItem.field_82483_a) {
                    eventFired = false; // CraftBukkit - reset event status
                    tileentitydispenser.func_70299_a(i, idispensebehavior.func_82482_a(sourceblock, itemstack));
                }

            }
        }
    }

    protected IBehaviorDispenseItem func_149940_a(ItemStack itemstack) {
        return (IBehaviorDispenseItem) BlockDispenser.field_149943_a.func_82594_a(itemstack.func_77973_b());
    }

    public void func_189540_a(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        boolean flag = world.func_175640_z(blockposition) || world.func_175640_z(blockposition.func_177984_a());
        boolean flag1 = ((Boolean) iblockdata.func_177229_b(BlockDispenser.field_176440_b)).booleanValue();

        if (flag && !flag1) {
            world.func_175684_a(blockposition, (Block) this, this.func_149738_a(world));
            world.func_180501_a(blockposition, iblockdata.func_177226_a(BlockDispenser.field_176440_b, Boolean.valueOf(true)), 4);
        } else if (!flag && flag1) {
            world.func_180501_a(blockposition, iblockdata.func_177226_a(BlockDispenser.field_176440_b, Boolean.valueOf(false)), 4);
        }

    }

    public void func_180650_b(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        if (!world.field_72995_K) {
            this.func_176439_d(world, blockposition);
        }

    }

    public TileEntity func_149915_a(World world, int i) {
        return new TileEntityDispenser();
    }

    public IBlockState func_180642_a(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        return this.func_176223_P().func_177226_a(BlockDispenser.field_176441_a, EnumFacing.func_190914_a(blockposition, entityliving)).func_177226_a(BlockDispenser.field_176440_b, Boolean.valueOf(false));
    }

    public void func_180633_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityLivingBase entityliving, ItemStack itemstack) {
        world.func_180501_a(blockposition, iblockdata.func_177226_a(BlockDispenser.field_176441_a, EnumFacing.func_190914_a(blockposition, entityliving)), 2);
        if (itemstack.func_82837_s()) {
            TileEntity tileentity = world.func_175625_s(blockposition);

            if (tileentity instanceof TileEntityDispenser) {
                ((TileEntityDispenser) tileentity).func_190575_a(itemstack.func_82833_r());
            }
        }

    }

    public void func_180663_b(World world, BlockPos blockposition, IBlockState iblockdata) {
        TileEntity tileentity = world.func_175625_s(blockposition);

        if (tileentity instanceof TileEntityDispenser) {
            InventoryHelper.func_180175_a(world, blockposition, (TileEntityDispenser) tileentity);
            world.func_175666_e(blockposition, this);
        }

        super.func_180663_b(world, blockposition, iblockdata);
    }

    public static IPosition func_149939_a(IBlockSource isourceblock) {
        EnumFacing enumdirection = (EnumFacing) isourceblock.func_189992_e().func_177229_b(BlockDispenser.field_176441_a);
        double d0 = isourceblock.func_82615_a() + 0.7D * (double) enumdirection.func_82601_c();
        double d1 = isourceblock.func_82617_b() + 0.7D * (double) enumdirection.func_96559_d();
        double d2 = isourceblock.func_82616_c() + 0.7D * (double) enumdirection.func_82599_e();

        return new PositionImpl(d0, d1, d2);
    }

    public boolean func_149740_M(IBlockState iblockdata) {
        return true;
    }

    public int func_180641_l(IBlockState iblockdata, World world, BlockPos blockposition) {
        return Container.func_178144_a(world.func_175625_s(blockposition));
    }

    public EnumBlockRenderType func_149645_b(IBlockState iblockdata) {
        return EnumBlockRenderType.MODEL;
    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockDispenser.field_176441_a, EnumFacing.func_82600_a(i & 7)).func_177226_a(BlockDispenser.field_176440_b, Boolean.valueOf((i & 8) > 0));
    }

    public int func_176201_c(IBlockState iblockdata) {
        byte b0 = 0;
        int i = b0 | ((EnumFacing) iblockdata.func_177229_b(BlockDispenser.field_176441_a)).func_176745_a();

        if (((Boolean) iblockdata.func_177229_b(BlockDispenser.field_176440_b)).booleanValue()) {
            i |= 8;
        }

        return i;
    }

    public IBlockState func_185499_a(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.func_177226_a(BlockDispenser.field_176441_a, enumblockrotation.func_185831_a((EnumFacing) iblockdata.func_177229_b(BlockDispenser.field_176441_a)));
    }

    public IBlockState func_185471_a(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata.func_185907_a(enumblockmirror.func_185800_a((EnumFacing) iblockdata.func_177229_b(BlockDispenser.field_176441_a)));
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockDispenser.field_176441_a, BlockDispenser.field_176440_b});
    }
}
