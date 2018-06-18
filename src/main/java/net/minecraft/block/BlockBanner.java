package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBanner extends BlockContainer {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyInteger ROTATION = PropertyInteger.create("rotation", 0, 15);
    protected static final AxisAlignedBB STANDING_AABB = new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D);

    protected BlockBanner() {
        super(Material.WOOD);
    }

    public String getLocalizedName() {
        return I18n.translateToLocal("item.banner.white.name");
    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockBanner.NULL_AABB;
    }

    public boolean isFullCube(IBlockState iblockdata) {
        return false;
    }

    public boolean isPassable(IBlockAccess iblockaccess, BlockPos blockposition) {
        return true;
    }

    public boolean isOpaqueCube(IBlockState iblockdata) {
        return false;
    }

    public boolean canSpawnInBlock() {
        return true;
    }

    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityBanner();
    }

    public Item getItemDropped(IBlockState iblockdata, Random random, int i) {
        return Items.BANNER;
    }

    private ItemStack getTileDataItemStack(World world, BlockPos blockposition) {
        TileEntity tileentity = world.getTileEntity(blockposition);

        return tileentity instanceof TileEntityBanner ? ((TileEntityBanner) tileentity).getItem() : ItemStack.EMPTY;
    }

    public ItemStack getItem(World world, BlockPos blockposition, IBlockState iblockdata) {
        ItemStack itemstack = this.getTileDataItemStack(world, blockposition);

        return itemstack.isEmpty() ? new ItemStack(Items.BANNER) : itemstack;
    }

    public void dropBlockAsItemWithChance(World world, BlockPos blockposition, IBlockState iblockdata, float f, int i) {
        ItemStack itemstack = this.getTileDataItemStack(world, blockposition);

        if (itemstack.isEmpty()) {
            super.dropBlockAsItemWithChance(world, blockposition, iblockdata, f, i);
        } else {
            spawnAsEntity(world, blockposition, itemstack);
        }

    }

    public boolean canPlaceBlockAt(World world, BlockPos blockposition) {
        return !this.hasInvalidNeighbor(world, blockposition) && super.canPlaceBlockAt(world, blockposition);
    }

    public void harvestBlock(World world, EntityPlayer entityhuman, BlockPos blockposition, IBlockState iblockdata, @Nullable TileEntity tileentity, ItemStack itemstack) {
        if (tileentity instanceof TileEntityBanner) {
            TileEntityBanner tileentitybanner = (TileEntityBanner) tileentity;
            ItemStack itemstack1 = tileentitybanner.getItem();

            spawnAsEntity(world, blockposition, itemstack1);
        } else {
            super.harvestBlock(world, entityhuman, blockposition, iblockdata, (TileEntity) null, itemstack);
        }

    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }

    public static class BlockBannerStanding extends BlockBanner {

        public BlockBannerStanding() {
            this.setDefaultState(this.blockState.getBaseState().withProperty(BlockBanner.BlockBannerStanding.ROTATION, Integer.valueOf(0)));
        }

        public AxisAlignedBB getBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
            return BlockBanner.BlockBannerStanding.STANDING_AABB;
        }

        public IBlockState withRotation(IBlockState iblockdata, Rotation enumblockrotation) {
            return iblockdata.withProperty(BlockBanner.BlockBannerStanding.ROTATION, Integer.valueOf(enumblockrotation.rotate(((Integer) iblockdata.getValue(BlockBanner.BlockBannerStanding.ROTATION)).intValue(), 16)));
        }

        public IBlockState withMirror(IBlockState iblockdata, Mirror enumblockmirror) {
            return iblockdata.withProperty(BlockBanner.BlockBannerStanding.ROTATION, Integer.valueOf(enumblockmirror.mirrorRotation(((Integer) iblockdata.getValue(BlockBanner.BlockBannerStanding.ROTATION)).intValue(), 16)));
        }

        public void neighborChanged(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
            if (!world.getBlockState(blockposition.down()).getMaterial().isSolid()) {
                this.dropBlockAsItem(world, blockposition, iblockdata, 0);
                world.setBlockToAir(blockposition);
            }

            super.neighborChanged(iblockdata, world, blockposition, block, blockposition1);
        }

        public IBlockState getStateFromMeta(int i) {
            return this.getDefaultState().withProperty(BlockBanner.BlockBannerStanding.ROTATION, Integer.valueOf(i));
        }

        public int getMetaFromState(IBlockState iblockdata) {
            return ((Integer) iblockdata.getValue(BlockBanner.BlockBannerStanding.ROTATION)).intValue();
        }

        protected BlockStateContainer createBlockState() {
            return new BlockStateContainer(this, new IProperty[] { BlockBanner.BlockBannerStanding.ROTATION});
        }
    }

    public static class BlockBannerHanging extends BlockBanner {

        protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.875D, 1.0D, 0.78125D, 1.0D);
        protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.78125D, 0.125D);
        protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.875D, 0.0D, 0.0D, 1.0D, 0.78125D, 1.0D);
        protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.125D, 0.78125D, 1.0D);

        public BlockBannerHanging() {
            this.setDefaultState(this.blockState.getBaseState().withProperty(BlockBanner.BlockBannerHanging.FACING, EnumFacing.NORTH));
        }

        public IBlockState withRotation(IBlockState iblockdata, Rotation enumblockrotation) {
            return iblockdata.withProperty(BlockBanner.BlockBannerHanging.FACING, enumblockrotation.rotate((EnumFacing) iblockdata.getValue(BlockBanner.BlockBannerHanging.FACING)));
        }

        public IBlockState withMirror(IBlockState iblockdata, Mirror enumblockmirror) {
            return iblockdata.withRotation(enumblockmirror.toRotation((EnumFacing) iblockdata.getValue(BlockBanner.BlockBannerHanging.FACING)));
        }

        public AxisAlignedBB getBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
            switch ((EnumFacing) iblockdata.getValue(BlockBanner.BlockBannerHanging.FACING)) {
            case NORTH:
            default:
                return BlockBanner.BlockBannerHanging.NORTH_AABB;

            case SOUTH:
                return BlockBanner.BlockBannerHanging.SOUTH_AABB;

            case WEST:
                return BlockBanner.BlockBannerHanging.WEST_AABB;

            case EAST:
                return BlockBanner.BlockBannerHanging.EAST_AABB;
            }
        }

        public void neighborChanged(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
            EnumFacing enumdirection = (EnumFacing) iblockdata.getValue(BlockBanner.BlockBannerHanging.FACING);

            if (!world.getBlockState(blockposition.offset(enumdirection.getOpposite())).getMaterial().isSolid()) {
                this.dropBlockAsItem(world, blockposition, iblockdata, 0);
                world.setBlockToAir(blockposition);
            }

            super.neighborChanged(iblockdata, world, blockposition, block, blockposition1);
        }

        public IBlockState getStateFromMeta(int i) {
            EnumFacing enumdirection = EnumFacing.getFront(i);

            if (enumdirection.getAxis() == EnumFacing.Axis.Y) {
                enumdirection = EnumFacing.NORTH;
            }

            return this.getDefaultState().withProperty(BlockBanner.BlockBannerHanging.FACING, enumdirection);
        }

        public int getMetaFromState(IBlockState iblockdata) {
            return ((EnumFacing) iblockdata.getValue(BlockBanner.BlockBannerHanging.FACING)).getIndex();
        }

        protected BlockStateContainer createBlockState() {
            return new BlockStateContainer(this, new IProperty[] { BlockBanner.BlockBannerHanging.FACING});
        }
    }
}
