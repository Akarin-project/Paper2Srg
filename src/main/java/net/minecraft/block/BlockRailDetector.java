package net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;


import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartCommandBlock;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.bukkit.event.block.BlockRedstoneEvent;

public class BlockRailDetector extends BlockRailBase {

    public static final PropertyEnum<BlockRailBase.EnumRailDirection> SHAPE = PropertyEnum.create("shape", BlockRailBase.EnumRailDirection.class, new Predicate() {
        public boolean a(@Nullable BlockRailBase.EnumRailDirection blockminecarttrackabstract_enumtrackposition) {
            return blockminecarttrackabstract_enumtrackposition != BlockRailBase.EnumRailDirection.NORTH_EAST && blockminecarttrackabstract_enumtrackposition != BlockRailBase.EnumRailDirection.NORTH_WEST && blockminecarttrackabstract_enumtrackposition != BlockRailBase.EnumRailDirection.SOUTH_EAST && blockminecarttrackabstract_enumtrackposition != BlockRailBase.EnumRailDirection.SOUTH_WEST;
        }

        public boolean apply(@Nullable Object object) {
            return this.a((BlockRailBase.EnumRailDirection) object);
        }
    });
    public static final PropertyBool POWERED = PropertyBool.create("powered");

    public BlockRailDetector() {
        super(true);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockRailDetector.POWERED, Boolean.valueOf(false)).withProperty(BlockRailDetector.SHAPE, BlockRailBase.EnumRailDirection.NORTH_SOUTH));
        this.setTickRandomly(true);
    }

    public int tickRate(World world) {
        return 20;
    }

    public boolean canProvidePower(IBlockState iblockdata) {
        return true;
    }

    public void onEntityCollidedWithBlock(World world, BlockPos blockposition, IBlockState iblockdata, Entity entity) {
        if (!world.isRemote) {
            if (!((Boolean) iblockdata.getValue(BlockRailDetector.POWERED)).booleanValue()) {
                this.updatePoweredState(world, blockposition, iblockdata);
            }
        }
    }

    public void randomTick(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {}

    public void updateTick(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        if (!world.isRemote && ((Boolean) iblockdata.getValue(BlockRailDetector.POWERED)).booleanValue()) {
            this.updatePoweredState(world, blockposition, iblockdata);
        }
    }

    public int getWeakPower(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        return ((Boolean) iblockdata.getValue(BlockRailDetector.POWERED)).booleanValue() ? 15 : 0;
    }

    public int getStrongPower(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        return !((Boolean) iblockdata.getValue(BlockRailDetector.POWERED)).booleanValue() ? 0 : (enumdirection == EnumFacing.UP ? 15 : 0);
    }

    private void updatePoweredState(World world, BlockPos blockposition, IBlockState iblockdata) {
        boolean flag = ((Boolean) iblockdata.getValue(BlockRailDetector.POWERED)).booleanValue();
        boolean flag1 = false;
        List list = this.findMinecarts(world, blockposition, EntityMinecart.class, new Predicate[0]);

        if (!list.isEmpty()) {
            flag1 = true;
        }

        // CraftBukkit start
        if (flag != flag1) {
            org.bukkit.block.Block block = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());

            BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, flag ? 15 : 0, flag1 ? 15 : 0);
            world.getServer().getPluginManager().callEvent(eventRedstone);

            flag1 = eventRedstone.getNewCurrent() > 0;
        }
        // CraftBukkit end

        if (flag1 && !flag) {
            world.setBlockState(blockposition, iblockdata.withProperty(BlockRailDetector.POWERED, Boolean.valueOf(true)), 3);
            this.updateConnectedRails(world, blockposition, iblockdata, true);
            world.notifyNeighborsOfStateChange(blockposition, this, false);
            world.notifyNeighborsOfStateChange(blockposition.down(), this, false);
            world.markBlockRangeForRenderUpdate(blockposition, blockposition);
        }

        if (!flag1 && flag) {
            world.setBlockState(blockposition, iblockdata.withProperty(BlockRailDetector.POWERED, Boolean.valueOf(false)), 3);
            this.updateConnectedRails(world, blockposition, iblockdata, false);
            world.notifyNeighborsOfStateChange(blockposition, this, false);
            world.notifyNeighborsOfStateChange(blockposition.down(), this, false);
            world.markBlockRangeForRenderUpdate(blockposition, blockposition);
        }

        if (flag1) {
            world.scheduleUpdate(new BlockPos(blockposition), (Block) this, this.tickRate(world));
        }

        world.updateComparatorOutputLevel(blockposition, this);
    }

    protected void updateConnectedRails(World world, BlockPos blockposition, IBlockState iblockdata, boolean flag) {
        BlockRailBase.Rail blockminecarttrackabstract_minecarttracklogic = new BlockRailBase.Rail(world, blockposition, iblockdata);
        List list = blockminecarttrackabstract_minecarttracklogic.getConnectedRails();
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            BlockPos blockposition1 = (BlockPos) iterator.next();
            IBlockState iblockdata1 = world.getBlockState(blockposition1);

            if (iblockdata1 != null) {
                iblockdata1.neighborChanged(world, blockposition1, iblockdata1.getBlock(), blockposition);
            }
        }

    }

    public void onBlockAdded(World world, BlockPos blockposition, IBlockState iblockdata) {
        super.onBlockAdded(world, blockposition, iblockdata);
        this.updatePoweredState(world, blockposition, iblockdata);
    }

    public IProperty<BlockRailBase.EnumRailDirection> getShapeProperty() {
        return BlockRailDetector.SHAPE;
    }

    public boolean hasComparatorInputOverride(IBlockState iblockdata) {
        return true;
    }

    public int getComparatorInputOverride(IBlockState iblockdata, World world, BlockPos blockposition) {
        if (((Boolean) iblockdata.getValue(BlockRailDetector.POWERED)).booleanValue()) {
            List list = this.findMinecarts(world, blockposition, EntityMinecartCommandBlock.class, new Predicate[0]);

            if (!list.isEmpty()) {
                return ((EntityMinecartCommandBlock) list.get(0)).getCommandBlockLogic().getSuccessCount();
            }

            List list1 = this.findMinecarts(world, blockposition, EntityMinecart.class, new Predicate[] { EntitySelectors.HAS_INVENTORY});

            if (!list1.isEmpty()) {
                return Container.calcRedstoneFromInventory((IInventory) list1.get(0));
            }
        }

        return 0;
    }

    protected <T extends EntityMinecart> List<T> findMinecarts(World world, BlockPos blockposition, Class<T> oclass, Predicate<Entity>... apredicate) {
        AxisAlignedBB axisalignedbb = this.getDectectionBox(blockposition);

        return apredicate.length != 1 ? world.getEntitiesWithinAABB(oclass, axisalignedbb) : world.getEntitiesWithinAABB(oclass, axisalignedbb, apredicate[0]);
    }

    private AxisAlignedBB getDectectionBox(BlockPos blockposition) {
        float f = 0.2F;

        return new AxisAlignedBB((double) ((float) blockposition.getX() + 0.2F), (double) blockposition.getY(), (double) ((float) blockposition.getZ() + 0.2F), (double) ((float) (blockposition.getX() + 1) - 0.2F), (double) ((float) (blockposition.getY() + 1) - 0.2F), (double) ((float) (blockposition.getZ() + 1) - 0.2F));
    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockRailDetector.SHAPE, BlockRailBase.EnumRailDirection.byMetadata(i & 7)).withProperty(BlockRailDetector.POWERED, Boolean.valueOf((i & 8) > 0));
    }

    public int getMetaFromState(IBlockState iblockdata) {
        byte b0 = 0;
        int i = b0 | ((BlockRailBase.EnumRailDirection) iblockdata.getValue(BlockRailDetector.SHAPE)).getMetadata();

        if (((Boolean) iblockdata.getValue(BlockRailDetector.POWERED)).booleanValue()) {
            i |= 8;
        }

        return i;
    }

    public IBlockState withRotation(IBlockState iblockdata, Rotation enumblockrotation) {
        switch (enumblockrotation) {
        case CLOCKWISE_180:
            switch ((BlockRailBase.EnumRailDirection) iblockdata.getValue(BlockRailDetector.SHAPE)) {
            case ASCENDING_EAST:
                return iblockdata.withProperty(BlockRailDetector.SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_WEST);

            case ASCENDING_WEST:
                return iblockdata.withProperty(BlockRailDetector.SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_EAST);

            case ASCENDING_NORTH:
                return iblockdata.withProperty(BlockRailDetector.SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_SOUTH);

            case ASCENDING_SOUTH:
                return iblockdata.withProperty(BlockRailDetector.SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_NORTH);

            case SOUTH_EAST:
                return iblockdata.withProperty(BlockRailDetector.SHAPE, BlockRailBase.EnumRailDirection.NORTH_WEST);

            case SOUTH_WEST:
                return iblockdata.withProperty(BlockRailDetector.SHAPE, BlockRailBase.EnumRailDirection.NORTH_EAST);

            case NORTH_WEST:
                return iblockdata.withProperty(BlockRailDetector.SHAPE, BlockRailBase.EnumRailDirection.SOUTH_EAST);

            case NORTH_EAST:
                return iblockdata.withProperty(BlockRailDetector.SHAPE, BlockRailBase.EnumRailDirection.SOUTH_WEST);
            }

        case COUNTERCLOCKWISE_90:
            switch ((BlockRailBase.EnumRailDirection) iblockdata.getValue(BlockRailDetector.SHAPE)) {
            case ASCENDING_EAST:
                return iblockdata.withProperty(BlockRailDetector.SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_NORTH);

            case ASCENDING_WEST:
                return iblockdata.withProperty(BlockRailDetector.SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_SOUTH);

            case ASCENDING_NORTH:
                return iblockdata.withProperty(BlockRailDetector.SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_WEST);

            case ASCENDING_SOUTH:
                return iblockdata.withProperty(BlockRailDetector.SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_EAST);

            case SOUTH_EAST:
                return iblockdata.withProperty(BlockRailDetector.SHAPE, BlockRailBase.EnumRailDirection.NORTH_EAST);

            case SOUTH_WEST:
                return iblockdata.withProperty(BlockRailDetector.SHAPE, BlockRailBase.EnumRailDirection.SOUTH_EAST);

            case NORTH_WEST:
                return iblockdata.withProperty(BlockRailDetector.SHAPE, BlockRailBase.EnumRailDirection.SOUTH_WEST);

            case NORTH_EAST:
                return iblockdata.withProperty(BlockRailDetector.SHAPE, BlockRailBase.EnumRailDirection.NORTH_WEST);

            case NORTH_SOUTH:
                return iblockdata.withProperty(BlockRailDetector.SHAPE, BlockRailBase.EnumRailDirection.EAST_WEST);

            case EAST_WEST:
                return iblockdata.withProperty(BlockRailDetector.SHAPE, BlockRailBase.EnumRailDirection.NORTH_SOUTH);
            }

        case CLOCKWISE_90:
            switch ((BlockRailBase.EnumRailDirection) iblockdata.getValue(BlockRailDetector.SHAPE)) {
            case ASCENDING_EAST:
                return iblockdata.withProperty(BlockRailDetector.SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_SOUTH);

            case ASCENDING_WEST:
                return iblockdata.withProperty(BlockRailDetector.SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_NORTH);

            case ASCENDING_NORTH:
                return iblockdata.withProperty(BlockRailDetector.SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_EAST);

            case ASCENDING_SOUTH:
                return iblockdata.withProperty(BlockRailDetector.SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_WEST);

            case SOUTH_EAST:
                return iblockdata.withProperty(BlockRailDetector.SHAPE, BlockRailBase.EnumRailDirection.SOUTH_WEST);

            case SOUTH_WEST:
                return iblockdata.withProperty(BlockRailDetector.SHAPE, BlockRailBase.EnumRailDirection.NORTH_WEST);

            case NORTH_WEST:
                return iblockdata.withProperty(BlockRailDetector.SHAPE, BlockRailBase.EnumRailDirection.NORTH_EAST);

            case NORTH_EAST:
                return iblockdata.withProperty(BlockRailDetector.SHAPE, BlockRailBase.EnumRailDirection.SOUTH_EAST);

            case NORTH_SOUTH:
                return iblockdata.withProperty(BlockRailDetector.SHAPE, BlockRailBase.EnumRailDirection.EAST_WEST);

            case EAST_WEST:
                return iblockdata.withProperty(BlockRailDetector.SHAPE, BlockRailBase.EnumRailDirection.NORTH_SOUTH);
            }

        default:
            return iblockdata;
        }
    }

    public IBlockState withMirror(IBlockState iblockdata, Mirror enumblockmirror) {
        BlockRailBase.EnumRailDirection blockminecarttrackabstract_enumtrackposition = (BlockRailBase.EnumRailDirection) iblockdata.getValue(BlockRailDetector.SHAPE);

        switch (enumblockmirror) {
        case LEFT_RIGHT:
            switch (blockminecarttrackabstract_enumtrackposition) {
            case ASCENDING_NORTH:
                return iblockdata.withProperty(BlockRailDetector.SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_SOUTH);

            case ASCENDING_SOUTH:
                return iblockdata.withProperty(BlockRailDetector.SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_NORTH);

            case SOUTH_EAST:
                return iblockdata.withProperty(BlockRailDetector.SHAPE, BlockRailBase.EnumRailDirection.NORTH_EAST);

            case SOUTH_WEST:
                return iblockdata.withProperty(BlockRailDetector.SHAPE, BlockRailBase.EnumRailDirection.NORTH_WEST);

            case NORTH_WEST:
                return iblockdata.withProperty(BlockRailDetector.SHAPE, BlockRailBase.EnumRailDirection.SOUTH_WEST);

            case NORTH_EAST:
                return iblockdata.withProperty(BlockRailDetector.SHAPE, BlockRailBase.EnumRailDirection.SOUTH_EAST);

            default:
                return super.withMirror(iblockdata, enumblockmirror);
            }

        case FRONT_BACK:
            switch (blockminecarttrackabstract_enumtrackposition) {
            case ASCENDING_EAST:
                return iblockdata.withProperty(BlockRailDetector.SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_WEST);

            case ASCENDING_WEST:
                return iblockdata.withProperty(BlockRailDetector.SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_EAST);

            case ASCENDING_NORTH:
            case ASCENDING_SOUTH:
            default:
                break;

            case SOUTH_EAST:
                return iblockdata.withProperty(BlockRailDetector.SHAPE, BlockRailBase.EnumRailDirection.SOUTH_WEST);

            case SOUTH_WEST:
                return iblockdata.withProperty(BlockRailDetector.SHAPE, BlockRailBase.EnumRailDirection.SOUTH_EAST);

            case NORTH_WEST:
                return iblockdata.withProperty(BlockRailDetector.SHAPE, BlockRailBase.EnumRailDirection.NORTH_EAST);

            case NORTH_EAST:
                return iblockdata.withProperty(BlockRailDetector.SHAPE, BlockRailBase.EnumRailDirection.NORTH_WEST);
            }
        }

        return super.withMirror(iblockdata, enumblockmirror);
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockRailDetector.SHAPE, BlockRailDetector.POWERED});
    }
}
