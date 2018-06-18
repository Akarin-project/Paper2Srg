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

    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    protected static final AxisAlignedBB ENDER_CHEST_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D);

    protected BlockEnderChest() {
        super(Material.ROCK);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockEnderChest.FACING, EnumFacing.NORTH));
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }

    public AxisAlignedBB getBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockEnderChest.ENDER_CHEST_AABB;
    }

    public boolean isOpaqueCube(IBlockState iblockdata) {
        return false;
    }

    public boolean isFullCube(IBlockState iblockdata) {
        return false;
    }

    public EnumBlockRenderType getRenderType(IBlockState iblockdata) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    public Item getItemDropped(IBlockState iblockdata, Random random, int i) {
        return Item.getItemFromBlock(Blocks.OBSIDIAN);
    }

    public int quantityDropped(Random random) {
        return 8;
    }

    protected boolean canSilkHarvest() {
        return true;
    }

    public IBlockState getStateForPlacement(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        return this.getDefaultState().withProperty(BlockEnderChest.FACING, entityliving.getHorizontalFacing().getOpposite());
    }

    public void onBlockPlacedBy(World world, BlockPos blockposition, IBlockState iblockdata, EntityLivingBase entityliving, ItemStack itemstack) {
        world.setBlockState(blockposition, iblockdata.withProperty(BlockEnderChest.FACING, entityliving.getHorizontalFacing().getOpposite()), 2);
    }

    public boolean onBlockActivated(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        InventoryEnderChest inventoryenderchest = entityhuman.getInventoryEnderChest();
        TileEntity tileentity = world.getTileEntity(blockposition);

        if (inventoryenderchest != null && tileentity instanceof TileEntityEnderChest) {
            if (world.getBlockState(blockposition.up()).isNormalCube()) {
                return true;
            } else if (world.isRemote) {
                return true;
            } else {
                inventoryenderchest.setChestTileEntity((TileEntityEnderChest) tileentity);
                entityhuman.displayGUIChest(inventoryenderchest);
                entityhuman.addStat(StatList.ENDERCHEST_OPENED);
                return true;
            }
        } else {
            return true;
        }
    }

    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityEnderChest();
    }

    public IBlockState getStateFromMeta(int i) {
        EnumFacing enumdirection = EnumFacing.getFront(i);

        if (enumdirection.getAxis() == EnumFacing.Axis.Y) {
            enumdirection = EnumFacing.NORTH;
        }

        return this.getDefaultState().withProperty(BlockEnderChest.FACING, enumdirection);
    }

    public int getMetaFromState(IBlockState iblockdata) {
        return ((EnumFacing) iblockdata.getValue(BlockEnderChest.FACING)).getIndex();
    }

    public IBlockState withRotation(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.withProperty(BlockEnderChest.FACING, enumblockrotation.rotate((EnumFacing) iblockdata.getValue(BlockEnderChest.FACING)));
    }

    public IBlockState withMirror(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata.withRotation(enumblockmirror.toRotation((EnumFacing) iblockdata.getValue(BlockEnderChest.FACING)));
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockEnderChest.FACING});
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }
}
