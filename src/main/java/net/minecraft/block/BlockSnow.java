package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSnow extends Block {

    public static final PropertyInteger LAYERS = PropertyInteger.create("layers", 1, 8);
    protected static final AxisAlignedBB[] SNOW_AABB = new AxisAlignedBB[] { new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.375D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.625D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)};

    protected BlockSnow() {
        super(Material.SNOW);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockSnow.LAYERS, Integer.valueOf(1)));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }

    public AxisAlignedBB getBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockSnow.SNOW_AABB[((Integer) iblockdata.getValue(BlockSnow.LAYERS)).intValue()];
    }

    public boolean isPassable(IBlockAccess iblockaccess, BlockPos blockposition) {
        return ((Integer) iblockaccess.getBlockState(blockposition).getValue(BlockSnow.LAYERS)).intValue() < 5;
    }

    public boolean isTopSolid(IBlockState iblockdata) {
        return ((Integer) iblockdata.getValue(BlockSnow.LAYERS)).intValue() == 8;
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return enumdirection == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        int i = ((Integer) iblockdata.getValue(BlockSnow.LAYERS)).intValue() - 1;
        float f = 0.125F;
        AxisAlignedBB axisalignedbb = iblockdata.getBoundingBox(iblockaccess, blockposition);

        return new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.maxX, (double) ((float) i * 0.125F), axisalignedbb.maxZ);
    }

    public boolean isOpaqueCube(IBlockState iblockdata) {
        return false;
    }

    public boolean isFullCube(IBlockState iblockdata) {
        return false;
    }

    public boolean canPlaceBlockAt(World world, BlockPos blockposition) {
        IBlockState iblockdata = world.getBlockState(blockposition.down());
        Block block = iblockdata.getBlock();

        if (block != Blocks.ICE && block != Blocks.PACKED_ICE && block != Blocks.BARRIER) {
            BlockFaceShape enumblockfaceshape = iblockdata.getBlockFaceShape(world, blockposition.down(), EnumFacing.UP);

            return enumblockfaceshape == BlockFaceShape.SOLID || iblockdata.getMaterial() == Material.LEAVES || block == this && ((Integer) iblockdata.getValue(BlockSnow.LAYERS)).intValue() == 8;
        } else {
            return false;
        }
    }

    public void neighborChanged(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        this.checkAndDropBlock(world, blockposition, iblockdata);
    }

    private boolean checkAndDropBlock(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (!this.canPlaceBlockAt(world, blockposition)) {
            this.dropBlockAsItem(world, blockposition, iblockdata, 0);
            world.setBlockToAir(blockposition);
            return false;
        } else {
            return true;
        }
    }

    public void harvestBlock(World world, EntityPlayer entityhuman, BlockPos blockposition, IBlockState iblockdata, @Nullable TileEntity tileentity, ItemStack itemstack) {
        spawnAsEntity(world, blockposition, new ItemStack(Items.SNOWBALL, ((Integer) iblockdata.getValue(BlockSnow.LAYERS)).intValue() + 1, 0));
        world.setBlockToAir(blockposition);
        entityhuman.addStat(StatList.getBlockStats((Block) this));
    }

    public Item getItemDropped(IBlockState iblockdata, Random random, int i) {
        return Items.SNOWBALL;
    }

    public int quantityDropped(Random random) {
        return 0;
    }

    public void updateTick(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        if (world.getLightFor(EnumSkyBlock.BLOCK, blockposition) > 11) {
            // CraftBukkit start
            if (org.bukkit.craftbukkit.event.CraftEventFactory.callBlockFadeEvent(world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()), Blocks.AIR).isCancelled()) {
                return;
            }
            // CraftBukkit end
            this.dropBlockAsItem(world, blockposition, world.getBlockState(blockposition), 0);
            world.setBlockToAir(blockposition);
        }

    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockSnow.LAYERS, Integer.valueOf((i & 7) + 1));
    }

    public boolean isReplaceable(IBlockAccess iblockaccess, BlockPos blockposition) {
        return ((Integer) iblockaccess.getBlockState(blockposition).getValue(BlockSnow.LAYERS)).intValue() == 1;
    }

    public int getMetaFromState(IBlockState iblockdata) {
        return ((Integer) iblockdata.getValue(BlockSnow.LAYERS)).intValue() - 1;
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockSnow.LAYERS});
    }
}
