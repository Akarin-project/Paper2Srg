package net.minecraft.block;

import java.util.Random;


import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.event.CraftEventFactory;

public class BlockCocoa extends BlockHorizontal implements IGrowable {

    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 2);
    protected static final AxisAlignedBB[] COCOA_EAST_AABB = new AxisAlignedBB[] { new AxisAlignedBB(0.6875D, 0.4375D, 0.375D, 0.9375D, 0.75D, 0.625D), new AxisAlignedBB(0.5625D, 0.3125D, 0.3125D, 0.9375D, 0.75D, 0.6875D), new AxisAlignedBB(0.4375D, 0.1875D, 0.25D, 0.9375D, 0.75D, 0.75D)};
    protected static final AxisAlignedBB[] COCOA_WEST_AABB = new AxisAlignedBB[] { new AxisAlignedBB(0.0625D, 0.4375D, 0.375D, 0.3125D, 0.75D, 0.625D), new AxisAlignedBB(0.0625D, 0.3125D, 0.3125D, 0.4375D, 0.75D, 0.6875D), new AxisAlignedBB(0.0625D, 0.1875D, 0.25D, 0.5625D, 0.75D, 0.75D)};
    protected static final AxisAlignedBB[] COCOA_NORTH_AABB = new AxisAlignedBB[] { new AxisAlignedBB(0.375D, 0.4375D, 0.0625D, 0.625D, 0.75D, 0.3125D), new AxisAlignedBB(0.3125D, 0.3125D, 0.0625D, 0.6875D, 0.75D, 0.4375D), new AxisAlignedBB(0.25D, 0.1875D, 0.0625D, 0.75D, 0.75D, 0.5625D)};
    protected static final AxisAlignedBB[] COCOA_SOUTH_AABB = new AxisAlignedBB[] { new AxisAlignedBB(0.375D, 0.4375D, 0.6875D, 0.625D, 0.75D, 0.9375D), new AxisAlignedBB(0.3125D, 0.3125D, 0.5625D, 0.6875D, 0.75D, 0.9375D), new AxisAlignedBB(0.25D, 0.1875D, 0.4375D, 0.75D, 0.75D, 0.9375D)};

    public BlockCocoa() {
        super(Material.PLANTS);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockCocoa.FACING, EnumFacing.NORTH).withProperty(BlockCocoa.AGE, Integer.valueOf(0)));
        this.setTickRandomly(true);
    }

    public void updateTick(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        if (!this.canBlockStay(world, blockposition, iblockdata)) {
            this.dropBlock(world, blockposition, iblockdata);
        } else if (world.rand.nextInt(Math.max(1, (int) (100.0F / world.spigotConfig.cocoaModifier) * 5)) == 0) { // Spigot
            int i = ((Integer) iblockdata.getValue(BlockCocoa.AGE)).intValue();

            if (i < 2) {
                // CraftBukkit start
                IBlockState data = iblockdata.withProperty(AGE, Integer.valueOf(i + 1));
                CraftEventFactory.handleBlockGrowEvent(world, blockposition.getX(), blockposition.getY(), blockposition.getZ(), this, getMetaFromState(data));
                // CraftBukkit end
            }
        }

    }

    public boolean canBlockStay(World world, BlockPos blockposition, IBlockState iblockdata) {
        blockposition = blockposition.offset((EnumFacing) iblockdata.getValue(BlockCocoa.FACING));
        IBlockState iblockdata1 = world.getBlockState(blockposition);

        return iblockdata1.getBlock() == Blocks.LOG && iblockdata1.getValue(BlockOldLog.VARIANT) == BlockPlanks.EnumType.JUNGLE;
    }

    public boolean isFullCube(IBlockState iblockdata) {
        return false;
    }

    public boolean isOpaqueCube(IBlockState iblockdata) {
        return false;
    }

    public AxisAlignedBB getBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        int i = ((Integer) iblockdata.getValue(BlockCocoa.AGE)).intValue();

        switch ((EnumFacing) iblockdata.getValue(BlockCocoa.FACING)) {
        case SOUTH:
            return BlockCocoa.COCOA_SOUTH_AABB[i];

        case NORTH:
        default:
            return BlockCocoa.COCOA_NORTH_AABB[i];

        case WEST:
            return BlockCocoa.COCOA_WEST_AABB[i];

        case EAST:
            return BlockCocoa.COCOA_EAST_AABB[i];
        }
    }

    public IBlockState withRotation(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.withProperty(BlockCocoa.FACING, enumblockrotation.rotate((EnumFacing) iblockdata.getValue(BlockCocoa.FACING)));
    }

    public IBlockState withMirror(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata.withRotation(enumblockmirror.toRotation((EnumFacing) iblockdata.getValue(BlockCocoa.FACING)));
    }

    public void onBlockPlacedBy(World world, BlockPos blockposition, IBlockState iblockdata, EntityLivingBase entityliving, ItemStack itemstack) {
        EnumFacing enumdirection = EnumFacing.fromAngle((double) entityliving.rotationYaw);

        world.setBlockState(blockposition, iblockdata.withProperty(BlockCocoa.FACING, enumdirection), 2);
    }

    public IBlockState getStateForPlacement(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        if (!enumdirection.getAxis().isHorizontal()) {
            enumdirection = EnumFacing.NORTH;
        }

        return this.getDefaultState().withProperty(BlockCocoa.FACING, enumdirection.getOpposite()).withProperty(BlockCocoa.AGE, Integer.valueOf(0));
    }

    public void neighborChanged(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        if (!this.canBlockStay(world, blockposition, iblockdata)) {
            this.dropBlock(world, blockposition, iblockdata);
        }

    }

    private void dropBlock(World world, BlockPos blockposition, IBlockState iblockdata) {
        world.setBlockState(blockposition, Blocks.AIR.getDefaultState(), 3);
        this.dropBlockAsItem(world, blockposition, iblockdata, 0);
    }

    public void dropBlockAsItemWithChance(World world, BlockPos blockposition, IBlockState iblockdata, float f, int i) {
        int j = ((Integer) iblockdata.getValue(BlockCocoa.AGE)).intValue();
        byte b0 = 1;

        if (j >= 2) {
            b0 = 3;
        }

        for (int k = 0; k < b0; ++k) {
            spawnAsEntity(world, blockposition, new ItemStack(Items.DYE, 1, EnumDyeColor.BROWN.getDyeDamage()));
        }

    }

    public ItemStack getItem(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(Items.DYE, 1, EnumDyeColor.BROWN.getDyeDamage());
    }

    public boolean canGrow(World world, BlockPos blockposition, IBlockState iblockdata, boolean flag) {
        return ((Integer) iblockdata.getValue(BlockCocoa.AGE)).intValue() < 2;
    }

    public boolean canUseBonemeal(World world, Random random, BlockPos blockposition, IBlockState iblockdata) {
        return true;
    }

    public void grow(World world, Random random, BlockPos blockposition, IBlockState iblockdata) {
        // CraftBukkit start
        IBlockState data = iblockdata.withProperty(AGE, Integer.valueOf(((Integer) iblockdata.getValue(AGE)).intValue() + 1));
        CraftEventFactory.handleBlockGrowEvent(world, blockposition.getX(), blockposition.getY(), blockposition.getZ(), this, getMetaFromState(data));
        // CraftBukkit end
    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockCocoa.FACING, EnumFacing.getHorizontal(i)).withProperty(BlockCocoa.AGE, Integer.valueOf((i & 15) >> 2));
    }

    public int getMetaFromState(IBlockState iblockdata) {
        byte b0 = 0;
        int i = b0 | ((EnumFacing) iblockdata.getValue(BlockCocoa.FACING)).getHorizontalIndex();

        i |= ((Integer) iblockdata.getValue(BlockCocoa.AGE)).intValue() << 2;
        return i;
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockCocoa.FACING, BlockCocoa.AGE});
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }
}
