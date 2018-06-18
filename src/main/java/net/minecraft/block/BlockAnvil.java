package net.minecraft.block;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;

public class BlockAnvil extends BlockFalling {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyInteger DAMAGE = PropertyInteger.create("damage", 0, 2);
    protected static final AxisAlignedBB X_AXIS_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.125D, 1.0D, 1.0D, 0.875D);
    protected static final AxisAlignedBB Z_AXIS_AABB = new AxisAlignedBB(0.125D, 0.0D, 0.0D, 0.875D, 1.0D, 1.0D);
    protected static final Logger LOGGER = LogManager.getLogger();

    protected BlockAnvil() {
        super(Material.ANVIL);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockAnvil.FACING, EnumFacing.NORTH).withProperty(BlockAnvil.DAMAGE, Integer.valueOf(0)));
        this.setLightOpacity(0);
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }

    public boolean isFullCube(IBlockState iblockdata) {
        return false;
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }

    public boolean isOpaqueCube(IBlockState iblockdata) {
        return false;
    }

    public IBlockState getStateForPlacement(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        EnumFacing enumdirection1 = entityliving.getHorizontalFacing().rotateY();

        try {
            return super.getStateForPlacement(world, blockposition, enumdirection, f, f1, f2, i, entityliving).withProperty(BlockAnvil.FACING, enumdirection1).withProperty(BlockAnvil.DAMAGE, Integer.valueOf(i >> 2));
        } catch (IllegalArgumentException illegalargumentexception) {
            if (!world.isRemote) {
                BlockAnvil.LOGGER.warn(String.format("Invalid damage property for anvil at %s. Found %d, must be in [0, 1, 2]", new Object[] { blockposition, Integer.valueOf(i >> 2)}));
                if (entityliving instanceof EntityPlayer) {
                    entityliving.sendMessage(new TextComponentTranslation("Invalid damage property. Please pick in [0, 1, 2]", new Object[0]));
                }
            }

            return super.getStateForPlacement(world, blockposition, enumdirection, f, f1, f2, 0, entityliving).withProperty(BlockAnvil.FACING, enumdirection1).withProperty(BlockAnvil.DAMAGE, Integer.valueOf(0));
        }
    }

    public boolean onBlockActivated(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (!world.isRemote) {
            entityhuman.displayGui(new BlockAnvil.Anvil(world, blockposition));
        }

        return true;
    }

    public int damageDropped(IBlockState iblockdata) {
        return ((Integer) iblockdata.getValue(BlockAnvil.DAMAGE)).intValue();
    }

    public AxisAlignedBB getBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        EnumFacing enumdirection = (EnumFacing) iblockdata.getValue(BlockAnvil.FACING);

        return enumdirection.getAxis() == EnumFacing.Axis.X ? BlockAnvil.X_AXIS_AABB : BlockAnvil.Z_AXIS_AABB;
    }

    public void getSubBlocks(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        nonnulllist.add(new ItemStack(this));
        nonnulllist.add(new ItemStack(this, 1, 1));
        nonnulllist.add(new ItemStack(this, 1, 2));
    }

    protected void onStartFalling(EntityFallingBlock entityfallingblock) {
        entityfallingblock.setHurtEntities(true);
    }

    public void onEndFalling(World world, BlockPos blockposition, IBlockState iblockdata, IBlockState iblockdata1) {
        world.playEvent(1031, blockposition, 0);
    }

    public void onBroken(World world, BlockPos blockposition) {
        world.playEvent(1029, blockposition, 0);
    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockAnvil.FACING, EnumFacing.getHorizontal(i & 3)).withProperty(BlockAnvil.DAMAGE, Integer.valueOf((i & 15) >> 2));
    }

    public int getMetaFromState(IBlockState iblockdata) {
        byte b0 = 0;
        int i = b0 | ((EnumFacing) iblockdata.getValue(BlockAnvil.FACING)).getHorizontalIndex();

        i |= ((Integer) iblockdata.getValue(BlockAnvil.DAMAGE)).intValue() << 2;
        return i;
    }

    public IBlockState withRotation(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.getBlock() != this ? iblockdata : iblockdata.withProperty(BlockAnvil.FACING, enumblockrotation.rotate((EnumFacing) iblockdata.getValue(BlockAnvil.FACING)));
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockAnvil.FACING, BlockAnvil.DAMAGE});
    }

    public static class Anvil implements IInteractionObject {

        private final World world;
        private final BlockPos position;

        public Anvil(World world, BlockPos blockposition) {
            this.world = world;
            this.position = blockposition;
        }

        public String getName() {
            return "anvil";
        }

        public boolean hasCustomName() {
            return false;
        }

        public ITextComponent getDisplayName() {
            return new TextComponentTranslation(Blocks.ANVIL.getUnlocalizedName() + ".name", new Object[0]);
        }

        public Container createContainer(InventoryPlayer playerinventory, EntityPlayer entityhuman) {
            return new ContainerRepair(playerinventory, this.world, this.position, entityhuman);
        }

        public String getGuiID() {
            return "minecraft:anvil";
        }
    }
}
