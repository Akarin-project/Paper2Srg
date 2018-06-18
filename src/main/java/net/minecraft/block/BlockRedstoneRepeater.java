package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRedstoneRepeater extends BlockRedstoneDiode {

    public static final PropertyBool LOCKED = PropertyBool.create("locked");
    public static final PropertyInteger DELAY = PropertyInteger.create("delay", 1, 4);

    protected BlockRedstoneRepeater(boolean flag) {
        super(flag);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockRedstoneRepeater.FACING, EnumFacing.NORTH).withProperty(BlockRedstoneRepeater.DELAY, Integer.valueOf(1)).withProperty(BlockRedstoneRepeater.LOCKED, Boolean.valueOf(false)));
    }

    public String getLocalizedName() {
        return I18n.translateToLocal("item.diode.name");
    }

    public IBlockState getActualState(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return iblockdata.withProperty(BlockRedstoneRepeater.LOCKED, Boolean.valueOf(this.isLocked(iblockaccess, blockposition, iblockdata)));
    }

    public IBlockState withRotation(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.withProperty(BlockRedstoneRepeater.FACING, enumblockrotation.rotate((EnumFacing) iblockdata.getValue(BlockRedstoneRepeater.FACING)));
    }

    public IBlockState withMirror(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata.withRotation(enumblockmirror.toRotation((EnumFacing) iblockdata.getValue(BlockRedstoneRepeater.FACING)));
    }

    public boolean onBlockActivated(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (!entityhuman.capabilities.allowEdit) {
            return false;
        } else {
            world.setBlockState(blockposition, iblockdata.cycleProperty((IProperty) BlockRedstoneRepeater.DELAY), 3);
            return true;
        }
    }

    protected int getDelay(IBlockState iblockdata) {
        return ((Integer) iblockdata.getValue(BlockRedstoneRepeater.DELAY)).intValue() * 2;
    }

    protected IBlockState getPoweredState(IBlockState iblockdata) {
        Integer integer = (Integer) iblockdata.getValue(BlockRedstoneRepeater.DELAY);
        Boolean obool = (Boolean) iblockdata.getValue(BlockRedstoneRepeater.LOCKED);
        EnumFacing enumdirection = (EnumFacing) iblockdata.getValue(BlockRedstoneRepeater.FACING);

        return Blocks.POWERED_REPEATER.getDefaultState().withProperty(BlockRedstoneRepeater.FACING, enumdirection).withProperty(BlockRedstoneRepeater.DELAY, integer).withProperty(BlockRedstoneRepeater.LOCKED, obool);
    }

    protected IBlockState getUnpoweredState(IBlockState iblockdata) {
        Integer integer = (Integer) iblockdata.getValue(BlockRedstoneRepeater.DELAY);
        Boolean obool = (Boolean) iblockdata.getValue(BlockRedstoneRepeater.LOCKED);
        EnumFacing enumdirection = (EnumFacing) iblockdata.getValue(BlockRedstoneRepeater.FACING);

        return Blocks.UNPOWERED_REPEATER.getDefaultState().withProperty(BlockRedstoneRepeater.FACING, enumdirection).withProperty(BlockRedstoneRepeater.DELAY, integer).withProperty(BlockRedstoneRepeater.LOCKED, obool);
    }

    public Item getItemDropped(IBlockState iblockdata, Random random, int i) {
        return Items.REPEATER;
    }

    public ItemStack getItem(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(Items.REPEATER);
    }

    public boolean isLocked(IBlockAccess iblockaccess, BlockPos blockposition, IBlockState iblockdata) {
        return this.getPowerOnSides(iblockaccess, blockposition, iblockdata) > 0;
    }

    protected boolean isAlternateInput(IBlockState iblockdata) {
        return isDiode(iblockdata);
    }

    public void breakBlock(World world, BlockPos blockposition, IBlockState iblockdata) {
        super.breakBlock(world, blockposition, iblockdata);
        this.notifyNeighbors(world, blockposition, iblockdata);
    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockRedstoneRepeater.FACING, EnumFacing.getHorizontal(i)).withProperty(BlockRedstoneRepeater.LOCKED, Boolean.valueOf(false)).withProperty(BlockRedstoneRepeater.DELAY, Integer.valueOf(1 + (i >> 2)));
    }

    public int getMetaFromState(IBlockState iblockdata) {
        byte b0 = 0;
        int i = b0 | ((EnumFacing) iblockdata.getValue(BlockRedstoneRepeater.FACING)).getHorizontalIndex();

        i |= ((Integer) iblockdata.getValue(BlockRedstoneRepeater.DELAY)).intValue() - 1 << 2;
        return i;
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockRedstoneRepeater.FACING, BlockRedstoneRepeater.DELAY, BlockRedstoneRepeater.LOCKED});
    }
}
