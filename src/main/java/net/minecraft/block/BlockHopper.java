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

    public static final PropertyDirection FACING = PropertyDirection.create("facing", new Predicate() {
        public boolean a(@Nullable EnumFacing enumdirection) {
            return enumdirection != EnumFacing.UP;
        }

        public boolean apply(@Nullable Object object) {
            return this.a((EnumFacing) object);
        }
    });
    public static final PropertyBool ENABLED = PropertyBool.create("enabled");
    protected static final AxisAlignedBB BASE_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.625D, 1.0D);
    protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.125D);
    protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.875D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.875D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.125D, 1.0D, 1.0D);

    public BlockHopper() {
        super(Material.IRON, MapColor.STONE);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockHopper.FACING, EnumFacing.DOWN).withProperty(BlockHopper.ENABLED, Boolean.valueOf(true)));
        this.setCreativeTab(CreativeTabs.REDSTONE);
    }

    public AxisAlignedBB getBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockHopper.FULL_BLOCK_AABB;
    }

    public void addCollisionBoxToList(IBlockState iblockdata, World world, BlockPos blockposition, AxisAlignedBB axisalignedbb, List<AxisAlignedBB> list, @Nullable Entity entity, boolean flag) {
        addCollisionBoxToList(blockposition, axisalignedbb, list, BlockHopper.BASE_AABB);
        addCollisionBoxToList(blockposition, axisalignedbb, list, BlockHopper.EAST_AABB);
        addCollisionBoxToList(blockposition, axisalignedbb, list, BlockHopper.WEST_AABB);
        addCollisionBoxToList(blockposition, axisalignedbb, list, BlockHopper.SOUTH_AABB);
        addCollisionBoxToList(blockposition, axisalignedbb, list, BlockHopper.NORTH_AABB);
    }

    public IBlockState getStateForPlacement(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        EnumFacing enumdirection1 = enumdirection.getOpposite();

        if (enumdirection1 == EnumFacing.UP) {
            enumdirection1 = EnumFacing.DOWN;
        }

        return this.getDefaultState().withProperty(BlockHopper.FACING, enumdirection1).withProperty(BlockHopper.ENABLED, Boolean.valueOf(true));
    }

    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityHopper();
    }

    public void onBlockPlacedBy(World world, BlockPos blockposition, IBlockState iblockdata, EntityLivingBase entityliving, ItemStack itemstack) {
        super.onBlockPlacedBy(world, blockposition, iblockdata, entityliving, itemstack);
        if (itemstack.hasDisplayName()) {
            TileEntity tileentity = world.getTileEntity(blockposition);

            if (tileentity instanceof TileEntityHopper) {
                ((TileEntityHopper) tileentity).setCustomName(itemstack.getDisplayName());
            }
        }

    }

    public boolean isTopSolid(IBlockState iblockdata) {
        return true;
    }

    public void onBlockAdded(World world, BlockPos blockposition, IBlockState iblockdata) {
        this.updateState(world, blockposition, iblockdata);
    }

    public boolean onBlockActivated(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (world.isRemote) {
            return true;
        } else {
            TileEntity tileentity = world.getTileEntity(blockposition);

            if (tileentity instanceof TileEntityHopper) {
                entityhuman.displayGUIChest((TileEntityHopper) tileentity);
                entityhuman.addStat(StatList.HOPPER_INSPECTED);
            }

            return true;
        }
    }

    public void neighborChanged(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        this.updateState(world, blockposition, iblockdata);
    }

    private void updateState(World world, BlockPos blockposition, IBlockState iblockdata) {
        boolean flag = !world.isBlockPowered(blockposition);

        if (flag != ((Boolean) iblockdata.getValue(BlockHopper.ENABLED)).booleanValue()) {
            world.setBlockState(blockposition, iblockdata.withProperty(BlockHopper.ENABLED, Boolean.valueOf(flag)), 4);
        }

    }

    public void breakBlock(World world, BlockPos blockposition, IBlockState iblockdata) {
        TileEntity tileentity = world.getTileEntity(blockposition);

        if (tileentity instanceof TileEntityHopper) {
            InventoryHelper.dropInventoryItems(world, blockposition, (TileEntityHopper) tileentity);
            world.updateComparatorOutputLevel(blockposition, this);
        }

        super.breakBlock(world, blockposition, iblockdata);
    }

    public EnumBlockRenderType getRenderType(IBlockState iblockdata) {
        return EnumBlockRenderType.MODEL;
    }

    public boolean isFullCube(IBlockState iblockdata) {
        return false;
    }

    public boolean isOpaqueCube(IBlockState iblockdata) {
        return false;
    }

    public static EnumFacing getFacing(int i) {
        return EnumFacing.getFront(i & 7);
    }

    public static boolean isEnabled(int i) {
        return (i & 8) != 8;
    }

    public boolean hasComparatorInputOverride(IBlockState iblockdata) {
        return true;
    }

    public int getComparatorInputOverride(IBlockState iblockdata, World world, BlockPos blockposition) {
        return Container.calcRedstone(world.getTileEntity(blockposition));
    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockHopper.FACING, getFacing(i)).withProperty(BlockHopper.ENABLED, Boolean.valueOf(isEnabled(i)));
    }

    public int getMetaFromState(IBlockState iblockdata) {
        byte b0 = 0;
        int i = b0 | ((EnumFacing) iblockdata.getValue(BlockHopper.FACING)).getIndex();

        if (!((Boolean) iblockdata.getValue(BlockHopper.ENABLED)).booleanValue()) {
            i |= 8;
        }

        return i;
    }

    public IBlockState withRotation(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.withProperty(BlockHopper.FACING, enumblockrotation.rotate((EnumFacing) iblockdata.getValue(BlockHopper.FACING)));
    }

    public IBlockState withMirror(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata.withRotation(enumblockmirror.toRotation((EnumFacing) iblockdata.getValue(BlockHopper.FACING)));
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockHopper.FACING, BlockHopper.ENABLED});
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return enumdirection == EnumFacing.UP ? BlockFaceShape.BOWL : BlockFaceShape.UNDEFINED;
    }
}
