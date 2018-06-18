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

    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    private final boolean isBurning;
    private static boolean keepInventory;

    protected BlockFurnace(boolean flag) {
        super(Material.ROCK);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockFurnace.FACING, EnumFacing.NORTH));
        this.isBurning = flag;
    }

    public Item getItemDropped(IBlockState iblockdata, Random random, int i) {
        return Item.getItemFromBlock(Blocks.FURNACE);
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

    public boolean onBlockActivated(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (world.isRemote) {
            return true;
        } else {
            TileEntity tileentity = world.getTileEntity(blockposition);

            if (tileentity instanceof TileEntityFurnace) {
                entityhuman.displayGUIChest((TileEntityFurnace) tileentity);
                entityhuman.addStat(StatList.FURNACE_INTERACTION);
            }

            return true;
        }
    }

    public static void setState(boolean flag, World world, BlockPos blockposition) {
        IBlockState iblockdata = world.getBlockState(blockposition);
        TileEntity tileentity = world.getTileEntity(blockposition);

        BlockFurnace.keepInventory = true;
        if (flag) {
            world.setBlockState(blockposition, Blocks.LIT_FURNACE.getDefaultState().withProperty(BlockFurnace.FACING, iblockdata.getValue(BlockFurnace.FACING)), 3);
            world.setBlockState(blockposition, Blocks.LIT_FURNACE.getDefaultState().withProperty(BlockFurnace.FACING, iblockdata.getValue(BlockFurnace.FACING)), 3);
        } else {
            world.setBlockState(blockposition, Blocks.FURNACE.getDefaultState().withProperty(BlockFurnace.FACING, iblockdata.getValue(BlockFurnace.FACING)), 3);
            world.setBlockState(blockposition, Blocks.FURNACE.getDefaultState().withProperty(BlockFurnace.FACING, iblockdata.getValue(BlockFurnace.FACING)), 3);
        }

        BlockFurnace.keepInventory = false;
        if (tileentity != null) {
            tileentity.validate();
            world.setTileEntity(blockposition, tileentity);
        }

    }

    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityFurnace();
    }

    public IBlockState getStateForPlacement(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        return this.getDefaultState().withProperty(BlockFurnace.FACING, entityliving.getHorizontalFacing().getOpposite());
    }

    public void onBlockPlacedBy(World world, BlockPos blockposition, IBlockState iblockdata, EntityLivingBase entityliving, ItemStack itemstack) {
        world.setBlockState(blockposition, iblockdata.withProperty(BlockFurnace.FACING, entityliving.getHorizontalFacing().getOpposite()), 2);
        if (itemstack.hasDisplayName()) {
            TileEntity tileentity = world.getTileEntity(blockposition);

            if (tileentity instanceof TileEntityFurnace) {
                ((TileEntityFurnace) tileentity).setCustomInventoryName(itemstack.getDisplayName());
            }
        }

    }

    public void breakBlock(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (!BlockFurnace.keepInventory) {
            TileEntity tileentity = world.getTileEntity(blockposition);

            if (tileentity instanceof TileEntityFurnace) {
                InventoryHelper.dropInventoryItems(world, blockposition, (TileEntityFurnace) tileentity);
                world.updateComparatorOutputLevel(blockposition, this);
            }
        }

        super.breakBlock(world, blockposition, iblockdata);
    }

    public boolean hasComparatorInputOverride(IBlockState iblockdata) {
        return true;
    }

    public int getComparatorInputOverride(IBlockState iblockdata, World world, BlockPos blockposition) {
        return Container.calcRedstone(world.getTileEntity(blockposition));
    }

    public ItemStack getItem(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(Blocks.FURNACE);
    }

    public EnumBlockRenderType getRenderType(IBlockState iblockdata) {
        return EnumBlockRenderType.MODEL;
    }

    public IBlockState getStateFromMeta(int i) {
        EnumFacing enumdirection = EnumFacing.getFront(i);

        if (enumdirection.getAxis() == EnumFacing.Axis.Y) {
            enumdirection = EnumFacing.NORTH;
        }

        return this.getDefaultState().withProperty(BlockFurnace.FACING, enumdirection);
    }

    public int getMetaFromState(IBlockState iblockdata) {
        return ((EnumFacing) iblockdata.getValue(BlockFurnace.FACING)).getIndex();
    }

    public IBlockState withRotation(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.withProperty(BlockFurnace.FACING, enumblockrotation.rotate((EnumFacing) iblockdata.getValue(BlockFurnace.FACING)));
    }

    public IBlockState withMirror(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata.withRotation(enumblockmirror.toRotation((EnumFacing) iblockdata.getValue(BlockFurnace.FACING)));
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockFurnace.FACING});
    }
}
