package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDaylightDetector;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDaylightDetector extends BlockContainer {

    public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);
    protected static final AxisAlignedBB DAYLIGHT_DETECTOR_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.375D, 1.0D);
    private final boolean inverted;

    public BlockDaylightDetector(boolean flag) {
        super(Material.WOOD);
        this.inverted = flag;
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockDaylightDetector.POWER, Integer.valueOf(0)));
        this.setCreativeTab(CreativeTabs.REDSTONE);
        this.setHardness(0.2F);
        this.setSoundType(SoundType.WOOD);
        this.setUnlocalizedName("daylightDetector");
    }

    public AxisAlignedBB getBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockDaylightDetector.DAYLIGHT_DETECTOR_AABB;
    }

    public int getWeakPower(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        return ((Integer) iblockdata.getValue(BlockDaylightDetector.POWER)).intValue();
    }

    public void updatePower(World world, BlockPos blockposition) {
        if (world.provider.hasSkyLight()) {
            IBlockState iblockdata = world.getBlockState(blockposition);
            int i = world.getLightFor(EnumSkyBlock.SKY, blockposition) - world.getSkylightSubtracted();
            float f = world.getCelestialAngleRadians(1.0F);

            if (this.inverted) {
                i = 15 - i;
            }

            if (i > 0 && !this.inverted) {
                float f1 = f < 3.1415927F ? 0.0F : 6.2831855F;

                f += (f1 - f) * 0.2F;
                i = Math.round((float) i * MathHelper.cos(f));
            }

            i = MathHelper.clamp(i, 0, 15);
            if (((Integer) iblockdata.getValue(BlockDaylightDetector.POWER)).intValue() != i) {
                i = org.bukkit.craftbukkit.event.CraftEventFactory.callRedstoneChange(world, blockposition.getX(), blockposition.getY(), blockposition.getZ(), ((Integer) iblockdata.getValue(POWER)), i).getNewCurrent(); // CraftBukkit - Call BlockRedstoneEvent
                world.setBlockState(blockposition, iblockdata.withProperty(BlockDaylightDetector.POWER, Integer.valueOf(i)), 3);
            }

        }
    }

    public boolean onBlockActivated(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (entityhuman.isAllowEdit()) {
            if (world.isRemote) {
                return true;
            } else {
                if (this.inverted) {
                    world.setBlockState(blockposition, Blocks.DAYLIGHT_DETECTOR.getDefaultState().withProperty(BlockDaylightDetector.POWER, iblockdata.getValue(BlockDaylightDetector.POWER)), 4);
                    Blocks.DAYLIGHT_DETECTOR.updatePower(world, blockposition);
                } else {
                    world.setBlockState(blockposition, Blocks.DAYLIGHT_DETECTOR_INVERTED.getDefaultState().withProperty(BlockDaylightDetector.POWER, iblockdata.getValue(BlockDaylightDetector.POWER)), 4);
                    Blocks.DAYLIGHT_DETECTOR_INVERTED.updatePower(world, blockposition);
                }

                return true;
            }
        } else {
            return super.onBlockActivated(world, blockposition, iblockdata, entityhuman, enumhand, enumdirection, f, f1, f2);
        }
    }

    public Item getItemDropped(IBlockState iblockdata, Random random, int i) {
        return Item.getItemFromBlock(Blocks.DAYLIGHT_DETECTOR);
    }

    public ItemStack getItem(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(Blocks.DAYLIGHT_DETECTOR);
    }

    public boolean isFullCube(IBlockState iblockdata) {
        return false;
    }

    public boolean isOpaqueCube(IBlockState iblockdata) {
        return false;
    }

    public EnumBlockRenderType getRenderType(IBlockState iblockdata) {
        return EnumBlockRenderType.MODEL;
    }

    public boolean canProvidePower(IBlockState iblockdata) {
        return true;
    }

    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityDaylightDetector();
    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockDaylightDetector.POWER, Integer.valueOf(i));
    }

    public int getMetaFromState(IBlockState iblockdata) {
        return ((Integer) iblockdata.getValue(BlockDaylightDetector.POWER)).intValue();
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockDaylightDetector.POWER});
    }

    public void getSubBlocks(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        if (!this.inverted) {
            super.getSubBlocks(creativemodetab, nonnulllist);
        }

    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return enumdirection == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }
}
