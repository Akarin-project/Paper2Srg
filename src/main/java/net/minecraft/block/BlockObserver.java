package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockObserver extends BlockDirectional {

    public static final PropertyBool POWERED = PropertyBool.create("powered");

    public BlockObserver() {
        super(Material.ROCK);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockObserver.FACING, EnumFacing.SOUTH).withProperty(BlockObserver.POWERED, Boolean.valueOf(false)));
        this.setCreativeTab(CreativeTabs.REDSTONE);
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockObserver.FACING, BlockObserver.POWERED});
    }

    public IBlockState withRotation(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.withProperty(BlockObserver.FACING, enumblockrotation.rotate((EnumFacing) iblockdata.getValue(BlockObserver.FACING)));
    }

    public IBlockState withMirror(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata.withRotation(enumblockmirror.toRotation((EnumFacing) iblockdata.getValue(BlockObserver.FACING)));
    }

    public void updateTick(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        if (((Boolean) iblockdata.getValue(BlockObserver.POWERED)).booleanValue()) {
            world.setBlockState(blockposition, iblockdata.withProperty(BlockObserver.POWERED, Boolean.valueOf(false)), 2);
        } else {
            world.setBlockState(blockposition, iblockdata.withProperty(BlockObserver.POWERED, Boolean.valueOf(true)), 2);
            world.scheduleUpdate(blockposition, (Block) this, 2);
        }

        this.updateNeighborsInFront(world, blockposition, iblockdata);
    }

    public void neighborChanged(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {}

    public void observedNeighborChanged(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        if (!world.isRemote && blockposition.offset((EnumFacing) iblockdata.getValue(BlockObserver.FACING)).equals(blockposition1)) {
            this.startSignal(iblockdata, world, blockposition);
        }

    }

    private void startSignal(IBlockState iblockdata, World world, BlockPos blockposition) {
        if (!((Boolean) iblockdata.getValue(BlockObserver.POWERED)).booleanValue()) {
            if (!world.isUpdateScheduled(blockposition, (Block) this)) {
                world.scheduleUpdate(blockposition, (Block) this, 2);
            }

        }
    }

    protected void updateNeighborsInFront(World world, BlockPos blockposition, IBlockState iblockdata) {
        EnumFacing enumdirection = (EnumFacing) iblockdata.getValue(BlockObserver.FACING);
        BlockPos blockposition1 = blockposition.offset(enumdirection.getOpposite());

        world.neighborChanged(blockposition1, (Block) this, blockposition);
        world.notifyNeighborsOfStateExcept(blockposition1, (Block) this, enumdirection);
    }

    public boolean canProvidePower(IBlockState iblockdata) {
        return true;
    }

    public int getStrongPower(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        return iblockdata.getWeakPower(iblockaccess, blockposition, enumdirection);
    }

    public int getWeakPower(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        return ((Boolean) iblockdata.getValue(BlockObserver.POWERED)).booleanValue() && iblockdata.getValue(BlockObserver.FACING) == enumdirection ? 15 : 0;
    }

    public void onBlockAdded(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (!world.isRemote) {
            if (((Boolean) iblockdata.getValue(BlockObserver.POWERED)).booleanValue()) {
                this.updateTick(world, blockposition, iblockdata, world.rand);
            }

            this.startSignal(iblockdata, world, blockposition);
        }

    }

    public void breakBlock(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (((Boolean) iblockdata.getValue(BlockObserver.POWERED)).booleanValue() && world.isUpdateScheduled(blockposition, (Block) this)) {
            this.updateNeighborsInFront(world, blockposition, iblockdata.withProperty(BlockObserver.POWERED, Boolean.valueOf(false)));
        }

    }

    public IBlockState getStateForPlacement(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        return this.getDefaultState().withProperty(BlockObserver.FACING, EnumFacing.getDirectionFromEntityLiving(blockposition, entityliving).getOpposite());
    }

    public int getMetaFromState(IBlockState iblockdata) {
        byte b0 = 0;
        int i = b0 | ((EnumFacing) iblockdata.getValue(BlockObserver.FACING)).getIndex();

        if (((Boolean) iblockdata.getValue(BlockObserver.POWERED)).booleanValue()) {
            i |= 8;
        }

        return i;
    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockObserver.FACING, EnumFacing.getFront(i & 7));
    }
}
