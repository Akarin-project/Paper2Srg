package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;


import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.bukkit.event.block.BlockRedstoneEvent;

public abstract class BlockBasePressurePlate extends Block {

    protected static final AxisAlignedBB PRESSED_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.03125D, 0.9375D);
    protected static final AxisAlignedBB UNPRESSED_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.0625D, 0.9375D);
    protected static final AxisAlignedBB PRESSURE_AABB = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 0.25D, 0.875D);

    protected BlockBasePressurePlate(Material material) {
        this(material, material.getMaterialMapColor());
    }

    protected BlockBasePressurePlate(Material material, MapColor materialmapcolor) {
        super(material, materialmapcolor);
        this.setCreativeTab(CreativeTabs.REDSTONE);
        this.setTickRandomly(true);
    }

    public AxisAlignedBB getBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        boolean flag = this.getRedstoneStrength(iblockdata) > 0;

        return flag ? BlockBasePressurePlate.PRESSED_AABB : BlockBasePressurePlate.UNPRESSED_AABB;
    }

    public int tickRate(World world) {
        return 20;
    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockBasePressurePlate.NULL_AABB;
    }

    public boolean isOpaqueCube(IBlockState iblockdata) {
        return false;
    }

    public boolean isFullCube(IBlockState iblockdata) {
        return false;
    }

    public boolean isPassable(IBlockAccess iblockaccess, BlockPos blockposition) {
        return true;
    }

    public boolean canSpawnInBlock() {
        return true;
    }

    public boolean canPlaceBlockAt(World world, BlockPos blockposition) {
        return this.canBePlacedOn(world, blockposition.down());
    }

    public void neighborChanged(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        if (!this.canBePlacedOn(world, blockposition.down())) {
            this.dropBlockAsItem(world, blockposition, iblockdata, 0);
            world.setBlockToAir(blockposition);
        }

    }

    private boolean canBePlacedOn(World world, BlockPos blockposition) {
        return world.getBlockState(blockposition).isTopSolid() || world.getBlockState(blockposition).getBlock() instanceof BlockFence;
    }

    public void randomTick(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {}

    public void updateTick(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        if (!world.isRemote) {
            int i = this.getRedstoneStrength(iblockdata);

            if (i > 0) {
                this.updateState(world, blockposition, iblockdata, i);
            }

        }
    }

    public void onEntityCollidedWithBlock(World world, BlockPos blockposition, IBlockState iblockdata, Entity entity) {
        if (!world.isRemote) {
            int i = this.getRedstoneStrength(iblockdata);

            if (i == 0) {
                this.updateState(world, blockposition, iblockdata, i);
            }

        }
    }

    protected void updateState(World world, BlockPos blockposition, IBlockState iblockdata, int i) {
        int j = this.computeRedstoneStrength(world, blockposition);
        boolean flag = i > 0;
        boolean flag1 = j > 0;

        // CraftBukkit start - Interact Pressure Plate
        org.bukkit.World bworld = world.getWorld();
        org.bukkit.plugin.PluginManager manager = world.getServer().getPluginManager();

        if (flag != flag1) {
            BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(bworld.getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()), i, j);
            manager.callEvent(eventRedstone);

            flag1 = eventRedstone.getNewCurrent() > 0;
            j = eventRedstone.getNewCurrent();
        }
        // CraftBukkit end

        if (i != j) {
            iblockdata = this.setRedstoneStrength(iblockdata, j);
            world.setBlockState(blockposition, iblockdata, 2);
            this.updateNeighbors(world, blockposition);
            world.markBlockRangeForRenderUpdate(blockposition, blockposition);
        }

        if (!flag1 && flag) {
            this.playClickOffSound(world, blockposition);
        } else if (flag1 && !flag) {
            this.playClickOnSound(world, blockposition);
        }

        if (flag1) {
            world.scheduleUpdate(new BlockPos(blockposition), (Block) this, this.tickRate(world));
        }

    }

    protected abstract void playClickOnSound(World world, BlockPos blockposition);

    protected abstract void playClickOffSound(World world, BlockPos blockposition);

    public void breakBlock(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (this.getRedstoneStrength(iblockdata) > 0) {
            this.updateNeighbors(world, blockposition);
        }

        super.breakBlock(world, blockposition, iblockdata);
    }

    protected void updateNeighbors(World world, BlockPos blockposition) {
        world.notifyNeighborsOfStateChange(blockposition, this, false);
        world.notifyNeighborsOfStateChange(blockposition.down(), this, false);
    }

    public int getWeakPower(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        return this.getRedstoneStrength(iblockdata);
    }

    public int getStrongPower(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        return enumdirection == EnumFacing.UP ? this.getRedstoneStrength(iblockdata) : 0;
    }

    public boolean canProvidePower(IBlockState iblockdata) {
        return true;
    }

    public EnumPushReaction getMobilityFlag(IBlockState iblockdata) {
        return EnumPushReaction.DESTROY;
    }

    protected abstract int computeRedstoneStrength(World world, BlockPos blockposition);

    protected abstract int getRedstoneStrength(IBlockState iblockdata);

    protected abstract IBlockState setRedstoneStrength(IBlockState iblockdata, int i);

    public BlockFaceShape getBlockFaceShape(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }
}
