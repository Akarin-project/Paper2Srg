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

    public static final PropertyBool[] HAS_BOTTLE = new PropertyBool[] { PropertyBool.create("has_bottle_0"), PropertyBool.create("has_bottle_1"), PropertyBool.create("has_bottle_2")};
    protected static final AxisAlignedBB BASE_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);
    protected static final AxisAlignedBB STICK_AABB = new AxisAlignedBB(0.4375D, 0.0D, 0.4375D, 0.5625D, 0.875D, 0.5625D);

    public BlockBrewingStand() {
        super(Material.IRON);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockBrewingStand.HAS_BOTTLE[0], Boolean.valueOf(false)).withProperty(BlockBrewingStand.HAS_BOTTLE[1], Boolean.valueOf(false)).withProperty(BlockBrewingStand.HAS_BOTTLE[2], Boolean.valueOf(false)));
    }

    public String getLocalizedName() {
        return I18n.translateToLocal("item.brewingStand.name");
    }

    public boolean isOpaqueCube(IBlockState iblockdata) {
        return false;
    }

    public EnumBlockRenderType getRenderType(IBlockState iblockdata) {
        return EnumBlockRenderType.MODEL;
    }

    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityBrewingStand();
    }

    public boolean isFullCube(IBlockState iblockdata) {
        return false;
    }

    public void addCollisionBoxToList(IBlockState iblockdata, World world, BlockPos blockposition, AxisAlignedBB axisalignedbb, List<AxisAlignedBB> list, @Nullable Entity entity, boolean flag) {
        addCollisionBoxToList(blockposition, axisalignedbb, list, BlockBrewingStand.STICK_AABB);
        addCollisionBoxToList(blockposition, axisalignedbb, list, BlockBrewingStand.BASE_AABB);
    }

    public AxisAlignedBB getBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockBrewingStand.BASE_AABB;
    }

    public boolean onBlockActivated(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (world.isRemote) {
            return true;
        } else {
            TileEntity tileentity = world.getTileEntity(blockposition);

            if (tileentity instanceof TileEntityBrewingStand) {
                entityhuman.displayGUIChest((TileEntityBrewingStand) tileentity);
                entityhuman.addStat(StatList.BREWINGSTAND_INTERACTION);
            }

            return true;
        }
    }

    public void onBlockPlacedBy(World world, BlockPos blockposition, IBlockState iblockdata, EntityLivingBase entityliving, ItemStack itemstack) {
        if (itemstack.hasDisplayName()) {
            TileEntity tileentity = world.getTileEntity(blockposition);

            if (tileentity instanceof TileEntityBrewingStand) {
                ((TileEntityBrewingStand) tileentity).setName(itemstack.getDisplayName());
            }
        }

    }

    public void breakBlock(World world, BlockPos blockposition, IBlockState iblockdata) {
        TileEntity tileentity = world.getTileEntity(blockposition);

        if (tileentity instanceof TileEntityBrewingStand) {
            InventoryHelper.dropInventoryItems(world, blockposition, (TileEntityBrewingStand) tileentity);
        }

        super.breakBlock(world, blockposition, iblockdata);
    }

    public Item getItemDropped(IBlockState iblockdata, Random random, int i) {
        return Items.BREWING_STAND;
    }

    public ItemStack getItem(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(Items.BREWING_STAND);
    }

    public boolean hasComparatorInputOverride(IBlockState iblockdata) {
        return true;
    }

    public int getComparatorInputOverride(IBlockState iblockdata, World world, BlockPos blockposition) {
        return Container.calcRedstone(world.getTileEntity(blockposition));
    }

    public IBlockState getStateFromMeta(int i) {
        IBlockState iblockdata = this.getDefaultState();

        for (int j = 0; j < 3; ++j) {
            iblockdata = iblockdata.withProperty(BlockBrewingStand.HAS_BOTTLE[j], Boolean.valueOf((i & 1 << j) > 0));
        }

        return iblockdata;
    }

    public int getMetaFromState(IBlockState iblockdata) {
        int i = 0;

        for (int j = 0; j < 3; ++j) {
            if (((Boolean) iblockdata.getValue(BlockBrewingStand.HAS_BOTTLE[j])).booleanValue()) {
                i |= 1 << j;
            }
        }

        return i;
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockBrewingStand.HAS_BOTTLE[0], BlockBrewingStand.HAS_BOTTLE[1], BlockBrewingStand.HAS_BOTTLE[2]});
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }
}
