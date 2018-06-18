package net.minecraft.block;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockRailBase extends Block {

    protected static final AxisAlignedBB FLAT_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);
    protected static final AxisAlignedBB ASCENDING_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
    protected final boolean isPowered;

    public static boolean isRailBlock(World world, BlockPos blockposition) {
        return isRailBlock(world.getBlockState(blockposition));
    }

    public static boolean isRailBlock(IBlockState iblockdata) {
        Block block = iblockdata.getBlock();

        return block == Blocks.RAIL || block == Blocks.GOLDEN_RAIL || block == Blocks.DETECTOR_RAIL || block == Blocks.ACTIVATOR_RAIL;
    }

    protected BlockRailBase(boolean flag) {
        super(Material.CIRCUITS);
        this.isPowered = flag;
        this.setCreativeTab(CreativeTabs.TRANSPORTATION);
    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockRailBase.NULL_AABB;
    }

    public boolean isOpaqueCube(IBlockState iblockdata) {
        return false;
    }

    public AxisAlignedBB getBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        BlockRailBase.EnumRailDirection blockminecarttrackabstract_enumtrackposition = iblockdata.getBlock() == this ? (BlockRailBase.EnumRailDirection) iblockdata.getValue(this.getShapeProperty()) : null;

        return blockminecarttrackabstract_enumtrackposition != null && blockminecarttrackabstract_enumtrackposition.isAscending() ? BlockRailBase.ASCENDING_AABB : BlockRailBase.FLAT_AABB;
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }

    public boolean isFullCube(IBlockState iblockdata) {
        return false;
    }

    public boolean canPlaceBlockAt(World world, BlockPos blockposition) {
        return world.getBlockState(blockposition.down()).isTopSolid();
    }

    public void onBlockAdded(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (!world.isRemote) {
            iblockdata = this.updateDir(world, blockposition, iblockdata, true);
            if (this.isPowered) {
                iblockdata.neighborChanged(world, blockposition, this, blockposition);
            }
        }

    }

    public void neighborChanged(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        if (!world.isRemote) {
            BlockRailBase.EnumRailDirection blockminecarttrackabstract_enumtrackposition = (BlockRailBase.EnumRailDirection) iblockdata.getValue(this.getShapeProperty());
            boolean flag = false;

            if (!world.getBlockState(blockposition.down()).isTopSolid()) {
                flag = true;
            }

            if (blockminecarttrackabstract_enumtrackposition == BlockRailBase.EnumRailDirection.ASCENDING_EAST && !world.getBlockState(blockposition.east()).isTopSolid()) {
                flag = true;
            } else if (blockminecarttrackabstract_enumtrackposition == BlockRailBase.EnumRailDirection.ASCENDING_WEST && !world.getBlockState(blockposition.west()).isTopSolid()) {
                flag = true;
            } else if (blockminecarttrackabstract_enumtrackposition == BlockRailBase.EnumRailDirection.ASCENDING_NORTH && !world.getBlockState(blockposition.north()).isTopSolid()) {
                flag = true;
            } else if (blockminecarttrackabstract_enumtrackposition == BlockRailBase.EnumRailDirection.ASCENDING_SOUTH && !world.getBlockState(blockposition.south()).isTopSolid()) {
                flag = true;
            }

            if (flag && !world.isAirBlock(blockposition)) {
                this.dropBlockAsItem(world, blockposition, iblockdata, 0);
                world.setBlockToAir(blockposition);
            } else {
                this.updateState(iblockdata, world, blockposition, block);
            }

        }
    }

    protected void updateState(IBlockState iblockdata, World world, BlockPos blockposition, Block block) {}

    protected IBlockState updateDir(World world, BlockPos blockposition, IBlockState iblockdata, boolean flag) {
        return world.isRemote ? iblockdata : (new BlockRailBase.Rail(world, blockposition, iblockdata)).place(world.isBlockPowered(blockposition), flag).getBlockState();
    }

    public EnumPushReaction getMobilityFlag(IBlockState iblockdata) {
        return EnumPushReaction.NORMAL;
    }

    public void breakBlock(World world, BlockPos blockposition, IBlockState iblockdata) {
        super.breakBlock(world, blockposition, iblockdata);
        if (((BlockRailBase.EnumRailDirection) iblockdata.getValue(this.getShapeProperty())).isAscending()) {
            world.notifyNeighborsOfStateChange(blockposition.up(), this, false);
        }

        if (this.isPowered) {
            world.notifyNeighborsOfStateChange(blockposition, this, false);
            world.notifyNeighborsOfStateChange(blockposition.down(), this, false);
        }

    }

    public abstract IProperty<BlockRailBase.EnumRailDirection> getShapeProperty();

    public static enum EnumRailDirection implements IStringSerializable {

        NORTH_SOUTH(0, "north_south"), EAST_WEST(1, "east_west"), ASCENDING_EAST(2, "ascending_east"), ASCENDING_WEST(3, "ascending_west"), ASCENDING_NORTH(4, "ascending_north"), ASCENDING_SOUTH(5, "ascending_south"), SOUTH_EAST(6, "south_east"), SOUTH_WEST(7, "south_west"), NORTH_WEST(8, "north_west"), NORTH_EAST(9, "north_east");

        private static final BlockRailBase.EnumRailDirection[] META_LOOKUP = new BlockRailBase.EnumRailDirection[values().length];
        private final int meta;
        private final String name;

        private EnumRailDirection(int i, String s) {
            this.meta = i;
            this.name = s;
        }

        public int getMetadata() {
            return this.meta;
        }

        public String toString() {
            return this.name;
        }

        public boolean isAscending() {
            return this == BlockRailBase.EnumRailDirection.ASCENDING_NORTH || this == BlockRailBase.EnumRailDirection.ASCENDING_EAST || this == BlockRailBase.EnumRailDirection.ASCENDING_SOUTH || this == BlockRailBase.EnumRailDirection.ASCENDING_WEST;
        }

        public static BlockRailBase.EnumRailDirection byMetadata(int i) {
            if (i < 0 || i >= BlockRailBase.EnumRailDirection.META_LOOKUP.length) {
                i = 0;
            }

            return BlockRailBase.EnumRailDirection.META_LOOKUP[i];
        }

        public String getName() {
            return this.name;
        }

        static {
            BlockRailBase.EnumRailDirection[] ablockminecarttrackabstract_enumtrackposition = values();
            int i = ablockminecarttrackabstract_enumtrackposition.length;

            for (int j = 0; j < i; ++j) {
                BlockRailBase.EnumRailDirection blockminecarttrackabstract_enumtrackposition = ablockminecarttrackabstract_enumtrackposition[j];

                BlockRailBase.EnumRailDirection.META_LOOKUP[blockminecarttrackabstract_enumtrackposition.getMetadata()] = blockminecarttrackabstract_enumtrackposition;
            }

        }
    }

    public class Rail {

        private final World world;
        private final BlockPos pos;
        private final BlockRailBase block;
        private IBlockState state;
        private final boolean isPowered;
        private final List<BlockPos> connectedRails = Lists.newArrayList();

        public Rail(World world, BlockPos blockposition, IBlockState iblockdata) {
            this.world = world;
            this.pos = blockposition;
            this.state = iblockdata;
            this.block = (BlockRailBase) iblockdata.getBlock();
            BlockRailBase.EnumRailDirection blockminecarttrackabstract_enumtrackposition = (BlockRailBase.EnumRailDirection) iblockdata.getValue(this.block.getShapeProperty());

            this.isPowered = this.block.isPowered;
            this.updateConnectedRails(blockminecarttrackabstract_enumtrackposition);
        }

        public List<BlockPos> getConnectedRails() {
            return this.connectedRails;
        }

        private void updateConnectedRails(BlockRailBase.EnumRailDirection blockminecarttrackabstract_enumtrackposition) {
            this.connectedRails.clear();
            switch (blockminecarttrackabstract_enumtrackposition) {
            case NORTH_SOUTH:
                this.connectedRails.add(this.pos.north());
                this.connectedRails.add(this.pos.south());
                break;

            case EAST_WEST:
                this.connectedRails.add(this.pos.west());
                this.connectedRails.add(this.pos.east());
                break;

            case ASCENDING_EAST:
                this.connectedRails.add(this.pos.west());
                this.connectedRails.add(this.pos.east().up());
                break;

            case ASCENDING_WEST:
                this.connectedRails.add(this.pos.west().up());
                this.connectedRails.add(this.pos.east());
                break;

            case ASCENDING_NORTH:
                this.connectedRails.add(this.pos.north().up());
                this.connectedRails.add(this.pos.south());
                break;

            case ASCENDING_SOUTH:
                this.connectedRails.add(this.pos.north());
                this.connectedRails.add(this.pos.south().up());
                break;

            case SOUTH_EAST:
                this.connectedRails.add(this.pos.east());
                this.connectedRails.add(this.pos.south());
                break;

            case SOUTH_WEST:
                this.connectedRails.add(this.pos.west());
                this.connectedRails.add(this.pos.south());
                break;

            case NORTH_WEST:
                this.connectedRails.add(this.pos.west());
                this.connectedRails.add(this.pos.north());
                break;

            case NORTH_EAST:
                this.connectedRails.add(this.pos.east());
                this.connectedRails.add(this.pos.north());
            }

        }

        private void removeSoftConnections() {
            for (int i = 0; i < this.connectedRails.size(); ++i) {
                BlockRailBase.Rail blockminecarttrackabstract_minecarttracklogic = this.findRailAt((BlockPos) this.connectedRails.get(i));

                if (blockminecarttrackabstract_minecarttracklogic != null && blockminecarttrackabstract_minecarttracklogic.isConnectedToRail(this)) {
                    this.connectedRails.set(i, blockminecarttrackabstract_minecarttracklogic.pos);
                } else {
                    this.connectedRails.remove(i--);
                }
            }

        }

        private boolean hasRailAt(BlockPos blockposition) {
            return BlockRailBase.isRailBlock(this.world, blockposition) || BlockRailBase.isRailBlock(this.world, blockposition.up()) || BlockRailBase.isRailBlock(this.world, blockposition.down());
        }

        @Nullable
        private BlockRailBase.Rail findRailAt(BlockPos blockposition) {
            IBlockState iblockdata = this.world.getBlockState(blockposition);

            if (BlockRailBase.isRailBlock(iblockdata)) {
                return BlockRailBase.this.new Rail(this.world, blockposition, iblockdata);
            } else {
                BlockPos blockposition1 = blockposition.up();

                iblockdata = this.world.getBlockState(blockposition1);
                if (BlockRailBase.isRailBlock(iblockdata)) {
                    return BlockRailBase.this.new Rail(this.world, blockposition1, iblockdata);
                } else {
                    blockposition1 = blockposition.down();
                    iblockdata = this.world.getBlockState(blockposition1);
                    return BlockRailBase.isRailBlock(iblockdata) ? BlockRailBase.this.new Rail(this.world, blockposition1, iblockdata) : null;
                }
            }
        }

        private boolean isConnectedToRail(BlockRailBase.Rail blockminecarttrackabstract_minecarttracklogic) {
            return this.isConnectedTo(blockminecarttrackabstract_minecarttracklogic.pos);
        }

        private boolean isConnectedTo(BlockPos blockposition) {
            for (int i = 0; i < this.connectedRails.size(); ++i) {
                BlockPos blockposition1 = (BlockPos) this.connectedRails.get(i);

                if (blockposition1.getX() == blockposition.getX() && blockposition1.getZ() == blockposition.getZ()) {
                    return true;
                }
            }

            return false;
        }

        protected int countAdjacentRails() {
            int i = 0;
            Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

            while (iterator.hasNext()) {
                EnumFacing enumdirection = (EnumFacing) iterator.next();

                if (this.hasRailAt(this.pos.offset(enumdirection))) {
                    ++i;
                }
            }

            return i;
        }

        private boolean canConnectTo(BlockRailBase.Rail blockminecarttrackabstract_minecarttracklogic) {
            return this.isConnectedToRail(blockminecarttrackabstract_minecarttracklogic) || this.connectedRails.size() != 2;
        }

        private void connectTo(BlockRailBase.Rail blockminecarttrackabstract_minecarttracklogic) {
            this.connectedRails.add(blockminecarttrackabstract_minecarttracklogic.pos);
            BlockPos blockposition = this.pos.north();
            BlockPos blockposition1 = this.pos.south();
            BlockPos blockposition2 = this.pos.west();
            BlockPos blockposition3 = this.pos.east();
            boolean flag = this.isConnectedTo(blockposition);
            boolean flag1 = this.isConnectedTo(blockposition1);
            boolean flag2 = this.isConnectedTo(blockposition2);
            boolean flag3 = this.isConnectedTo(blockposition3);
            BlockRailBase.EnumRailDirection blockminecarttrackabstract_enumtrackposition = null;

            if (flag || flag1) {
                blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
            }

            if (flag2 || flag3) {
                blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.EAST_WEST;
            }

            if (!this.isPowered) {
                if (flag1 && flag3 && !flag && !flag2) {
                    blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.SOUTH_EAST;
                }

                if (flag1 && flag2 && !flag && !flag3) {
                    blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.SOUTH_WEST;
                }

                if (flag && flag2 && !flag1 && !flag3) {
                    blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.NORTH_WEST;
                }

                if (flag && flag3 && !flag1 && !flag2) {
                    blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.NORTH_EAST;
                }
            }

            if (blockminecarttrackabstract_enumtrackposition == BlockRailBase.EnumRailDirection.NORTH_SOUTH) {
                if (BlockRailBase.isRailBlock(this.world, blockposition.up())) {
                    blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.ASCENDING_NORTH;
                }

                if (BlockRailBase.isRailBlock(this.world, blockposition1.up())) {
                    blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.ASCENDING_SOUTH;
                }
            }

            if (blockminecarttrackabstract_enumtrackposition == BlockRailBase.EnumRailDirection.EAST_WEST) {
                if (BlockRailBase.isRailBlock(this.world, blockposition3.up())) {
                    blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.ASCENDING_EAST;
                }

                if (BlockRailBase.isRailBlock(this.world, blockposition2.up())) {
                    blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.ASCENDING_WEST;
                }
            }

            if (blockminecarttrackabstract_enumtrackposition == null) {
                blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
            }

            this.state = this.state.withProperty(this.block.getShapeProperty(), blockminecarttrackabstract_enumtrackposition);
            this.world.setBlockState(this.pos, this.state, 3);
        }

        private boolean hasNeighborRail(BlockPos blockposition) {
            BlockRailBase.Rail blockminecarttrackabstract_minecarttracklogic = this.findRailAt(blockposition);

            if (blockminecarttrackabstract_minecarttracklogic == null) {
                return false;
            } else {
                blockminecarttrackabstract_minecarttracklogic.removeSoftConnections();
                return blockminecarttrackabstract_minecarttracklogic.canConnectTo(this);
            }
        }

        public BlockRailBase.Rail place(boolean flag, boolean flag1) {
            BlockPos blockposition = this.pos.north();
            BlockPos blockposition1 = this.pos.south();
            BlockPos blockposition2 = this.pos.west();
            BlockPos blockposition3 = this.pos.east();
            boolean flag2 = this.hasNeighborRail(blockposition);
            boolean flag3 = this.hasNeighborRail(blockposition1);
            boolean flag4 = this.hasNeighborRail(blockposition2);
            boolean flag5 = this.hasNeighborRail(blockposition3);
            BlockRailBase.EnumRailDirection blockminecarttrackabstract_enumtrackposition = null;

            if ((flag2 || flag3) && !flag4 && !flag5) {
                blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
            }

            if ((flag4 || flag5) && !flag2 && !flag3) {
                blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.EAST_WEST;
            }

            if (!this.isPowered) {
                if (flag3 && flag5 && !flag2 && !flag4) {
                    blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.SOUTH_EAST;
                }

                if (flag3 && flag4 && !flag2 && !flag5) {
                    blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.SOUTH_WEST;
                }

                if (flag2 && flag4 && !flag3 && !flag5) {
                    blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.NORTH_WEST;
                }

                if (flag2 && flag5 && !flag3 && !flag4) {
                    blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.NORTH_EAST;
                }
            }

            if (blockminecarttrackabstract_enumtrackposition == null) {
                if (flag2 || flag3) {
                    blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
                }

                if (flag4 || flag5) {
                    blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.EAST_WEST;
                }

                if (!this.isPowered) {
                    if (flag) {
                        if (flag3 && flag5) {
                            blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.SOUTH_EAST;
                        }

                        if (flag4 && flag3) {
                            blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.SOUTH_WEST;
                        }

                        if (flag5 && flag2) {
                            blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.NORTH_EAST;
                        }

                        if (flag2 && flag4) {
                            blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.NORTH_WEST;
                        }
                    } else {
                        if (flag2 && flag4) {
                            blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.NORTH_WEST;
                        }

                        if (flag5 && flag2) {
                            blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.NORTH_EAST;
                        }

                        if (flag4 && flag3) {
                            blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.SOUTH_WEST;
                        }

                        if (flag3 && flag5) {
                            blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.SOUTH_EAST;
                        }
                    }
                }
            }

            if (blockminecarttrackabstract_enumtrackposition == BlockRailBase.EnumRailDirection.NORTH_SOUTH) {
                if (BlockRailBase.isRailBlock(this.world, blockposition.up())) {
                    blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.ASCENDING_NORTH;
                }

                if (BlockRailBase.isRailBlock(this.world, blockposition1.up())) {
                    blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.ASCENDING_SOUTH;
                }
            }

            if (blockminecarttrackabstract_enumtrackposition == BlockRailBase.EnumRailDirection.EAST_WEST) {
                if (BlockRailBase.isRailBlock(this.world, blockposition3.up())) {
                    blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.ASCENDING_EAST;
                }

                if (BlockRailBase.isRailBlock(this.world, blockposition2.up())) {
                    blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.ASCENDING_WEST;
                }
            }

            if (blockminecarttrackabstract_enumtrackposition == null) {
                blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
            }

            this.updateConnectedRails(blockminecarttrackabstract_enumtrackposition);
            this.state = this.state.withProperty(this.block.getShapeProperty(), blockminecarttrackabstract_enumtrackposition);
            if (flag1 || this.world.getBlockState(this.pos) != this.state) {
                this.world.setBlockState(this.pos, this.state, 3);

                for (int i = 0; i < this.connectedRails.size(); ++i) {
                    BlockRailBase.Rail blockminecarttrackabstract_minecarttracklogic = this.findRailAt((BlockPos) this.connectedRails.get(i));

                    if (blockminecarttrackabstract_minecarttracklogic != null) {
                        blockminecarttrackabstract_minecarttracklogic.removeSoftConnections();
                        if (blockminecarttrackabstract_minecarttracklogic.canConnectTo(this)) {
                            blockminecarttrackabstract_minecarttracklogic.connectTo(this);
                        }
                    }
                }
            }

            return this;
        }

        public IBlockState getBlockState() {
            return this.state;
        }
    }
}
