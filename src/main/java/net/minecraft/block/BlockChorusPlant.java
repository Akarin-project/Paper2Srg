package net.minecraft.block;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockChorusPlant extends Block {

    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");
    public static final PropertyBool UP = PropertyBool.create("up");
    public static final PropertyBool DOWN = PropertyBool.create("down");

    protected BlockChorusPlant() {
        super(Material.PLANTS, MapColor.PURPLE);
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockChorusPlant.NORTH, Boolean.valueOf(false)).withProperty(BlockChorusPlant.EAST, Boolean.valueOf(false)).withProperty(BlockChorusPlant.SOUTH, Boolean.valueOf(false)).withProperty(BlockChorusPlant.WEST, Boolean.valueOf(false)).withProperty(BlockChorusPlant.UP, Boolean.valueOf(false)).withProperty(BlockChorusPlant.DOWN, Boolean.valueOf(false)));
    }

    public IBlockState getActualState(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        Block block = iblockaccess.getBlockState(blockposition.down()).getBlock();
        Block block1 = iblockaccess.getBlockState(blockposition.up()).getBlock();
        Block block2 = iblockaccess.getBlockState(blockposition.north()).getBlock();
        Block block3 = iblockaccess.getBlockState(blockposition.east()).getBlock();
        Block block4 = iblockaccess.getBlockState(blockposition.south()).getBlock();
        Block block5 = iblockaccess.getBlockState(blockposition.west()).getBlock();

        return iblockdata.withProperty(BlockChorusPlant.DOWN, Boolean.valueOf(block == this || block == Blocks.CHORUS_FLOWER || block == Blocks.END_STONE)).withProperty(BlockChorusPlant.UP, Boolean.valueOf(block1 == this || block1 == Blocks.CHORUS_FLOWER)).withProperty(BlockChorusPlant.NORTH, Boolean.valueOf(block2 == this || block2 == Blocks.CHORUS_FLOWER)).withProperty(BlockChorusPlant.EAST, Boolean.valueOf(block3 == this || block3 == Blocks.CHORUS_FLOWER)).withProperty(BlockChorusPlant.SOUTH, Boolean.valueOf(block4 == this || block4 == Blocks.CHORUS_FLOWER)).withProperty(BlockChorusPlant.WEST, Boolean.valueOf(block5 == this || block5 == Blocks.CHORUS_FLOWER));
    }

    public AxisAlignedBB getBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        iblockdata = iblockdata.getActualState(iblockaccess, blockposition);
        float f = 0.1875F;
        float f1 = ((Boolean) iblockdata.getValue(BlockChorusPlant.WEST)).booleanValue() ? 0.0F : 0.1875F;
        float f2 = ((Boolean) iblockdata.getValue(BlockChorusPlant.DOWN)).booleanValue() ? 0.0F : 0.1875F;
        float f3 = ((Boolean) iblockdata.getValue(BlockChorusPlant.NORTH)).booleanValue() ? 0.0F : 0.1875F;
        float f4 = ((Boolean) iblockdata.getValue(BlockChorusPlant.EAST)).booleanValue() ? 1.0F : 0.8125F;
        float f5 = ((Boolean) iblockdata.getValue(BlockChorusPlant.UP)).booleanValue() ? 1.0F : 0.8125F;
        float f6 = ((Boolean) iblockdata.getValue(BlockChorusPlant.SOUTH)).booleanValue() ? 1.0F : 0.8125F;

        return new AxisAlignedBB((double) f1, (double) f2, (double) f3, (double) f4, (double) f5, (double) f6);
    }

    public void addCollisionBoxToList(IBlockState iblockdata, World world, BlockPos blockposition, AxisAlignedBB axisalignedbb, List<AxisAlignedBB> list, @Nullable Entity entity, boolean flag) {
        if (!flag) {
            iblockdata = iblockdata.getActualState(world, blockposition);
        }

        float f = 0.1875F;
        float f1 = 0.8125F;

        addCollisionBoxToList(blockposition, axisalignedbb, list, new AxisAlignedBB(0.1875D, 0.1875D, 0.1875D, 0.8125D, 0.8125D, 0.8125D));
        if (((Boolean) iblockdata.getValue(BlockChorusPlant.WEST)).booleanValue()) {
            addCollisionBoxToList(blockposition, axisalignedbb, list, new AxisAlignedBB(0.0D, 0.1875D, 0.1875D, 0.1875D, 0.8125D, 0.8125D));
        }

        if (((Boolean) iblockdata.getValue(BlockChorusPlant.EAST)).booleanValue()) {
            addCollisionBoxToList(blockposition, axisalignedbb, list, new AxisAlignedBB(0.8125D, 0.1875D, 0.1875D, 1.0D, 0.8125D, 0.8125D));
        }

        if (((Boolean) iblockdata.getValue(BlockChorusPlant.UP)).booleanValue()) {
            addCollisionBoxToList(blockposition, axisalignedbb, list, new AxisAlignedBB(0.1875D, 0.8125D, 0.1875D, 0.8125D, 1.0D, 0.8125D));
        }

        if (((Boolean) iblockdata.getValue(BlockChorusPlant.DOWN)).booleanValue()) {
            addCollisionBoxToList(blockposition, axisalignedbb, list, new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 0.8125D, 0.1875D, 0.8125D));
        }

        if (((Boolean) iblockdata.getValue(BlockChorusPlant.NORTH)).booleanValue()) {
            addCollisionBoxToList(blockposition, axisalignedbb, list, new AxisAlignedBB(0.1875D, 0.1875D, 0.0D, 0.8125D, 0.8125D, 0.1875D));
        }

        if (((Boolean) iblockdata.getValue(BlockChorusPlant.SOUTH)).booleanValue()) {
            addCollisionBoxToList(blockposition, axisalignedbb, list, new AxisAlignedBB(0.1875D, 0.1875D, 0.8125D, 0.8125D, 0.8125D, 1.0D));
        }

    }

    public int getMetaFromState(IBlockState iblockdata) {
        return 0;
    }

    public void updateTick(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        if (!this.canSurviveAt(world, blockposition)) {
            world.destroyBlock(blockposition, true);
        }

    }

    public Item getItemDropped(IBlockState iblockdata, Random random, int i) {
        return Items.CHORUS_FRUIT;
    }

    public int quantityDropped(Random random) {
        return random.nextInt(2);
    }

    public boolean isFullCube(IBlockState iblockdata) {
        return false;
    }

    public boolean isOpaqueCube(IBlockState iblockdata) {
        return false;
    }

    public boolean canPlaceBlockAt(World world, BlockPos blockposition) {
        return super.canPlaceBlockAt(world, blockposition) ? this.canSurviveAt(world, blockposition) : false;
    }

    public void neighborChanged(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        if (!this.canSurviveAt(world, blockposition)) {
            world.scheduleUpdate(blockposition, (Block) this, 1);
        }

    }

    public boolean canSurviveAt(World world, BlockPos blockposition) {
        boolean flag = world.isAirBlock(blockposition.up());
        boolean flag1 = world.isAirBlock(blockposition.down());
        Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

        Block block;

        do {
            BlockPos blockposition1;
            Block block1;

            do {
                if (!iterator.hasNext()) {
                    Block block2 = world.getBlockState(blockposition.down()).getBlock();

                    return block2 == this || block2 == Blocks.END_STONE;
                }

                EnumFacing enumdirection = (EnumFacing) iterator.next();

                blockposition1 = blockposition.offset(enumdirection);
                block1 = world.getBlockState(blockposition1).getBlock();
            } while (block1 != this);

            if (!flag && !flag1) {
                return false;
            }

            block = world.getBlockState(blockposition1.down()).getBlock();
        } while (block != this && block != Blocks.END_STONE);

        return true;
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockChorusPlant.NORTH, BlockChorusPlant.EAST, BlockChorusPlant.SOUTH, BlockChorusPlant.WEST, BlockChorusPlant.UP, BlockChorusPlant.DOWN});
    }

    public boolean isPassable(IBlockAccess iblockaccess, BlockPos blockposition) {
        return false;
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }
}
