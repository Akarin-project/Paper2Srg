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

    public static final PropertyDirection FACING = BlockDirectional.FACING;
    public static final PropertyBool TRIGGERED = PropertyBool.create("triggered");
    public static final RegistryDefaulted<Item, IBehaviorDispenseItem> DISPENSE_BEHAVIOR_REGISTRY = new RegistryDefaulted(new BehaviorDefaultDispenseItem());
    protected Random rand = new Random();
    public static boolean eventFired = false; // CraftBukkit

    protected BlockDispenser() {
        super(Material.ROCK);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockDispenser.FACING, EnumFacing.NORTH).withProperty(BlockDispenser.TRIGGERED, Boolean.valueOf(false)));
        this.setCreativeTab(CreativeTabs.REDSTONE);
    }

    public int tickRate(World world) {
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

    public boolean onBlockActivated(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (world.isRemote) {
            return true;
        } else {
            TileEntity tileentity = world.getTileEntity(blockposition);

            if (tileentity instanceof TileEntityDispenser) {
                entityhuman.displayGUIChest((TileEntityDispenser) tileentity);
                if (tileentity instanceof TileEntityDropper) {
                    entityhuman.addStat(StatList.DROPPER_INSPECTED);
                } else {
                    entityhuman.addStat(StatList.DISPENSER_INSPECTED);
                }
            }

            return true;
        }
    }

    public void dispense(World world, BlockPos blockposition) {
        BlockSourceImpl sourceblock = new BlockSourceImpl(world, blockposition);
        TileEntityDispenser tileentitydispenser = (TileEntityDispenser) sourceblock.getBlockTileEntity();

        if (tileentitydispenser != null) {
            int i = tileentitydispenser.getDispenseSlot();

            if (i < 0) {
                world.playEvent(1001, blockposition, 0);
            } else {
                ItemStack itemstack = tileentitydispenser.getStackInSlot(i);
                IBehaviorDispenseItem idispensebehavior = this.getBehavior(itemstack);

                if (idispensebehavior != IBehaviorDispenseItem.DEFAULT_BEHAVIOR) {
                    eventFired = false; // CraftBukkit - reset event status
                    tileentitydispenser.setInventorySlotContents(i, idispensebehavior.dispense(sourceblock, itemstack));
                }

            }
        }
    }

    protected IBehaviorDispenseItem getBehavior(ItemStack itemstack) {
        return (IBehaviorDispenseItem) BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.getObject(itemstack.getItem());
    }

    public void neighborChanged(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        boolean flag = world.isBlockPowered(blockposition) || world.isBlockPowered(blockposition.up());
        boolean flag1 = ((Boolean) iblockdata.getValue(BlockDispenser.TRIGGERED)).booleanValue();

        if (flag && !flag1) {
            world.scheduleUpdate(blockposition, (Block) this, this.tickRate(world));
            world.setBlockState(blockposition, iblockdata.withProperty(BlockDispenser.TRIGGERED, Boolean.valueOf(true)), 4);
        } else if (!flag && flag1) {
            world.setBlockState(blockposition, iblockdata.withProperty(BlockDispenser.TRIGGERED, Boolean.valueOf(false)), 4);
        }

    }

    public void updateTick(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        if (!world.isRemote) {
            this.dispense(world, blockposition);
        }

    }

    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityDispenser();
    }

    public IBlockState getStateForPlacement(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        return this.getDefaultState().withProperty(BlockDispenser.FACING, EnumFacing.getDirectionFromEntityLiving(blockposition, entityliving)).withProperty(BlockDispenser.TRIGGERED, Boolean.valueOf(false));
    }

    public void onBlockPlacedBy(World world, BlockPos blockposition, IBlockState iblockdata, EntityLivingBase entityliving, ItemStack itemstack) {
        world.setBlockState(blockposition, iblockdata.withProperty(BlockDispenser.FACING, EnumFacing.getDirectionFromEntityLiving(blockposition, entityliving)), 2);
        if (itemstack.hasDisplayName()) {
            TileEntity tileentity = world.getTileEntity(blockposition);

            if (tileentity instanceof TileEntityDispenser) {
                ((TileEntityDispenser) tileentity).setCustomName(itemstack.getDisplayName());
            }
        }

    }

    public void breakBlock(World world, BlockPos blockposition, IBlockState iblockdata) {
        TileEntity tileentity = world.getTileEntity(blockposition);

        if (tileentity instanceof TileEntityDispenser) {
            InventoryHelper.dropInventoryItems(world, blockposition, (TileEntityDispenser) tileentity);
            world.updateComparatorOutputLevel(blockposition, this);
        }

        super.breakBlock(world, blockposition, iblockdata);
    }

    public static IPosition getDispensePosition(IBlockSource isourceblock) {
        EnumFacing enumdirection = (EnumFacing) isourceblock.getBlockState().getValue(BlockDispenser.FACING);
        double d0 = isourceblock.getX() + 0.7D * (double) enumdirection.getFrontOffsetX();
        double d1 = isourceblock.getY() + 0.7D * (double) enumdirection.getFrontOffsetY();
        double d2 = isourceblock.getZ() + 0.7D * (double) enumdirection.getFrontOffsetZ();

        return new PositionImpl(d0, d1, d2);
    }

    public boolean hasComparatorInputOverride(IBlockState iblockdata) {
        return true;
    }

    public int getComparatorInputOverride(IBlockState iblockdata, World world, BlockPos blockposition) {
        return Container.calcRedstone(world.getTileEntity(blockposition));
    }

    public EnumBlockRenderType getRenderType(IBlockState iblockdata) {
        return EnumBlockRenderType.MODEL;
    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockDispenser.FACING, EnumFacing.getFront(i & 7)).withProperty(BlockDispenser.TRIGGERED, Boolean.valueOf((i & 8) > 0));
    }

    public int getMetaFromState(IBlockState iblockdata) {
        byte b0 = 0;
        int i = b0 | ((EnumFacing) iblockdata.getValue(BlockDispenser.FACING)).getIndex();

        if (((Boolean) iblockdata.getValue(BlockDispenser.TRIGGERED)).booleanValue()) {
            i |= 8;
        }

        return i;
    }

    public IBlockState withRotation(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.withProperty(BlockDispenser.FACING, enumblockrotation.rotate((EnumFacing) iblockdata.getValue(BlockDispenser.FACING)));
    }

    public IBlockState withMirror(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata.withRotation(enumblockmirror.toRotation((EnumFacing) iblockdata.getValue(BlockDispenser.FACING)));
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockDispenser.FACING, BlockDispenser.TRIGGERED});
    }
}
