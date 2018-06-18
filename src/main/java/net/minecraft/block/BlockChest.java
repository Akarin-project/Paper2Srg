package net.minecraft.block;

import java.util.Iterator;
import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;

public class BlockChest extends BlockContainer {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    protected static final AxisAlignedBB NORTH_CHEST_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0D, 0.9375D, 0.875D, 0.9375D);
    protected static final AxisAlignedBB SOUTH_CHEST_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D, 1.0D);
    protected static final AxisAlignedBB WEST_CHEST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D);
    protected static final AxisAlignedBB EAST_CHEST_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 1.0D, 0.875D, 0.9375D);
    protected static final AxisAlignedBB NOT_CONNECTED_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D);
    public final BlockChest.Type chestType;

    protected BlockChest(BlockChest.Type blockchest_type) {
        super(Material.WOOD);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockChest.FACING, EnumFacing.NORTH));
        this.chestType = blockchest_type;
        this.setCreativeTab(blockchest_type == BlockChest.Type.TRAP ? CreativeTabs.REDSTONE : CreativeTabs.DECORATIONS);
    }

    public boolean isOpaqueCube(IBlockState iblockdata) {
        return false;
    }

    public boolean isFullCube(IBlockState iblockdata) {
        return false;
    }

    public EnumBlockRenderType getRenderType(IBlockState iblockdata) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    public AxisAlignedBB getBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return iblockaccess.getBlockState(blockposition.north()).getBlock() == this ? BlockChest.NORTH_CHEST_AABB : (iblockaccess.getBlockState(blockposition.south()).getBlock() == this ? BlockChest.SOUTH_CHEST_AABB : (iblockaccess.getBlockState(blockposition.west()).getBlock() == this ? BlockChest.WEST_CHEST_AABB : (iblockaccess.getBlockState(blockposition.east()).getBlock() == this ? BlockChest.EAST_CHEST_AABB : BlockChest.NOT_CONNECTED_AABB)));
    }

    public void onBlockAdded(World world, BlockPos blockposition, IBlockState iblockdata) {
        this.checkForSurroundingChests(world, blockposition, iblockdata);
        Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

        while (iterator.hasNext()) {
            EnumFacing enumdirection = (EnumFacing) iterator.next();
            BlockPos blockposition1 = blockposition.offset(enumdirection);
            IBlockState iblockdata1 = world.getBlockState(blockposition1);

            if (iblockdata1.getBlock() == this) {
                this.checkForSurroundingChests(world, blockposition1, iblockdata1);
            }
        }

    }

    public IBlockState getStateForPlacement(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        return this.getDefaultState().withProperty(BlockChest.FACING, entityliving.getHorizontalFacing());
    }

    public void onBlockPlacedBy(World world, BlockPos blockposition, IBlockState iblockdata, EntityLivingBase entityliving, ItemStack itemstack) {
        EnumFacing enumdirection = EnumFacing.getHorizontal(MathHelper.floor((double) (entityliving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3).getOpposite();

        iblockdata = iblockdata.withProperty(BlockChest.FACING, enumdirection);
        BlockPos blockposition1 = blockposition.north();
        BlockPos blockposition2 = blockposition.south();
        BlockPos blockposition3 = blockposition.west();
        BlockPos blockposition4 = blockposition.east();
        boolean flag = this == world.getBlockState(blockposition1).getBlock();
        boolean flag1 = this == world.getBlockState(blockposition2).getBlock();
        boolean flag2 = this == world.getBlockState(blockposition3).getBlock();
        boolean flag3 = this == world.getBlockState(blockposition4).getBlock();

        if (!flag && !flag1 && !flag2 && !flag3) {
            world.setBlockState(blockposition, iblockdata, 3);
        } else if (enumdirection.getAxis() == EnumFacing.Axis.X && (flag || flag1)) {
            if (flag) {
                world.setBlockState(blockposition1, iblockdata, 3);
            } else {
                world.setBlockState(blockposition2, iblockdata, 3);
            }

            world.setBlockState(blockposition, iblockdata, 3);
        } else if (enumdirection.getAxis() == EnumFacing.Axis.Z && (flag2 || flag3)) {
            if (flag2) {
                world.setBlockState(blockposition3, iblockdata, 3);
            } else {
                world.setBlockState(blockposition4, iblockdata, 3);
            }

            world.setBlockState(blockposition, iblockdata, 3);
        }

        if (itemstack.hasDisplayName()) {
            TileEntity tileentity = world.getTileEntity(blockposition);

            if (tileentity instanceof TileEntityChest) {
                ((TileEntityChest) tileentity).setCustomName(itemstack.getDisplayName());
            }
        }

    }

    public IBlockState checkForSurroundingChests(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (world.isRemote) {
            return iblockdata;
        } else {
            IBlockState iblockdata1 = world.getBlockState(blockposition.north());
            IBlockState iblockdata2 = world.getBlockState(blockposition.south());
            IBlockState iblockdata3 = world.getBlockState(blockposition.west());
            IBlockState iblockdata4 = world.getBlockState(blockposition.east());
            EnumFacing enumdirection = (EnumFacing) iblockdata.getValue(BlockChest.FACING);

            if (iblockdata1.getBlock() != this && iblockdata2.getBlock() != this) {
                boolean flag = iblockdata1.isFullBlock();
                boolean flag1 = iblockdata2.isFullBlock();

                if (iblockdata3.getBlock() == this || iblockdata4.getBlock() == this) {
                    BlockPos blockposition1 = iblockdata3.getBlock() == this ? blockposition.west() : blockposition.east();
                    IBlockState iblockdata5 = world.getBlockState(blockposition1.north());
                    IBlockState iblockdata6 = world.getBlockState(blockposition1.south());

                    enumdirection = EnumFacing.SOUTH;
                    EnumFacing enumdirection1;

                    if (iblockdata3.getBlock() == this) {
                        enumdirection1 = (EnumFacing) iblockdata3.getValue(BlockChest.FACING);
                    } else {
                        enumdirection1 = (EnumFacing) iblockdata4.getValue(BlockChest.FACING);
                    }

                    if (enumdirection1 == EnumFacing.NORTH) {
                        enumdirection = EnumFacing.NORTH;
                    }

                    if ((flag || iblockdata5.isFullBlock()) && !flag1 && !iblockdata6.isFullBlock()) {
                        enumdirection = EnumFacing.SOUTH;
                    }

                    if ((flag1 || iblockdata6.isFullBlock()) && !flag && !iblockdata5.isFullBlock()) {
                        enumdirection = EnumFacing.NORTH;
                    }
                }
            } else {
                BlockPos blockposition2 = iblockdata1.getBlock() == this ? blockposition.north() : blockposition.south();
                IBlockState iblockdata7 = world.getBlockState(blockposition2.west());
                IBlockState iblockdata8 = world.getBlockState(blockposition2.east());

                enumdirection = EnumFacing.EAST;
                EnumFacing enumdirection2;

                if (iblockdata1.getBlock() == this) {
                    enumdirection2 = (EnumFacing) iblockdata1.getValue(BlockChest.FACING);
                } else {
                    enumdirection2 = (EnumFacing) iblockdata2.getValue(BlockChest.FACING);
                }

                if (enumdirection2 == EnumFacing.WEST) {
                    enumdirection = EnumFacing.WEST;
                }

                if ((iblockdata3.isFullBlock() || iblockdata7.isFullBlock()) && !iblockdata4.isFullBlock() && !iblockdata8.isFullBlock()) {
                    enumdirection = EnumFacing.EAST;
                }

                if ((iblockdata4.isFullBlock() || iblockdata8.isFullBlock()) && !iblockdata3.isFullBlock() && !iblockdata7.isFullBlock()) {
                    enumdirection = EnumFacing.WEST;
                }
            }

            iblockdata = iblockdata.withProperty(BlockChest.FACING, enumdirection);
            world.setBlockState(blockposition, iblockdata, 3);
            return iblockdata;
        }
    }

    public IBlockState correctFacing(World world, BlockPos blockposition, IBlockState iblockdata) {
        EnumFacing enumdirection = null;
        Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

        while (iterator.hasNext()) {
            EnumFacing enumdirection1 = (EnumFacing) iterator.next();
            IBlockState iblockdata1 = world.getBlockState(blockposition.offset(enumdirection1));

            if (iblockdata1.getBlock() == this) {
                return iblockdata;
            }

            if (iblockdata1.isFullBlock()) {
                if (enumdirection != null) {
                    enumdirection = null;
                    break;
                }

                enumdirection = enumdirection1;
            }
        }

        if (enumdirection != null) {
            return iblockdata.withProperty(BlockChest.FACING, enumdirection.getOpposite());
        } else {
            EnumFacing enumdirection2 = (EnumFacing) iblockdata.getValue(BlockChest.FACING);

            if (world.getBlockState(blockposition.offset(enumdirection2)).isFullBlock()) {
                enumdirection2 = enumdirection2.getOpposite();
            }

            if (world.getBlockState(blockposition.offset(enumdirection2)).isFullBlock()) {
                enumdirection2 = enumdirection2.rotateY();
            }

            if (world.getBlockState(blockposition.offset(enumdirection2)).isFullBlock()) {
                enumdirection2 = enumdirection2.getOpposite();
            }

            return iblockdata.withProperty(BlockChest.FACING, enumdirection2);
        }
    }

    public boolean canPlaceBlockAt(World world, BlockPos blockposition) {
        int i = 0;
        BlockPos blockposition1 = blockposition.west();
        BlockPos blockposition2 = blockposition.east();
        BlockPos blockposition3 = blockposition.north();
        BlockPos blockposition4 = blockposition.south();

        if (world.getBlockState(blockposition1).getBlock() == this) {
            if (this.isDoubleChest(world, blockposition1)) {
                return false;
            }

            ++i;
        }

        if (world.getBlockState(blockposition2).getBlock() == this) {
            if (this.isDoubleChest(world, blockposition2)) {
                return false;
            }

            ++i;
        }

        if (world.getBlockState(blockposition3).getBlock() == this) {
            if (this.isDoubleChest(world, blockposition3)) {
                return false;
            }

            ++i;
        }

        if (world.getBlockState(blockposition4).getBlock() == this) {
            if (this.isDoubleChest(world, blockposition4)) {
                return false;
            }

            ++i;
        }

        return i <= 1;
    }

    private boolean isDoubleChest(World world, BlockPos blockposition) {
        if (world.getBlockState(blockposition).getBlock() != this) {
            return false;
        } else {
            Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

            EnumFacing enumdirection;

            do {
                if (!iterator.hasNext()) {
                    return false;
                }

                enumdirection = (EnumFacing) iterator.next();
            } while (world.getBlockState(blockposition.offset(enumdirection)).getBlock() != this);

            return true;
        }
    }

    public void neighborChanged(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        super.neighborChanged(iblockdata, world, blockposition, block, blockposition1);
        TileEntity tileentity = world.getTileEntity(blockposition);

        if (tileentity instanceof TileEntityChest) {
            tileentity.updateContainingBlockInfo();
        }

    }

    public void breakBlock(World world, BlockPos blockposition, IBlockState iblockdata) {
        TileEntity tileentity = world.getTileEntity(blockposition);

        if (tileentity instanceof IInventory) {
            InventoryHelper.dropInventoryItems(world, blockposition, (IInventory) tileentity);
            world.updateComparatorOutputLevel(blockposition, this);
        }

        super.breakBlock(world, blockposition, iblockdata);
    }

    public boolean onBlockActivated(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (world.isRemote) {
            return true;
        } else {
            ILockableContainer itileinventory = this.getLockableContainer(world, blockposition);

            if (itileinventory != null) {
                entityhuman.displayGUIChest(itileinventory);
                if (this.chestType == BlockChest.Type.BASIC) {
                    entityhuman.addStat(StatList.CHEST_OPENED);
                } else if (this.chestType == BlockChest.Type.TRAP) {
                    entityhuman.addStat(StatList.TRAPPED_CHEST_TRIGGERED);
                }
            }

            return true;
        }
    }

    @Nullable
    public ILockableContainer getLockableContainer(World world, BlockPos blockposition) {
        return this.getContainer(world, blockposition, false);
    }

    @Nullable
    public ILockableContainer getContainer(World world, BlockPos blockposition, boolean flag) {
        TileEntity tileentity = world.getTileEntity(blockposition);

        if (!(tileentity instanceof TileEntityChest)) {
            return null;
        } else {
            Object object = (TileEntityChest) tileentity;

            if (!flag && this.isBlocked(world, blockposition)) {
                return null;
            } else {
                Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

                while (iterator.hasNext()) {
                    EnumFacing enumdirection = (EnumFacing) iterator.next();
                    BlockPos blockposition1 = blockposition.offset(enumdirection);
                    // Paper start - don't load chunks if the other side of the chest is in unloaded chunk
                    final IBlockState type = world.getTypeIfLoaded(blockposition1); // Paper
                    if (type ==  null) {
                        continue;
                    }
                    Block block = type.getBlock();
                    // Paper end

                    if (block == this) {
                        if (!flag && this.isBlocked(world, blockposition1)) { // Paper - check for allowBlocked flag - MC-99321
                            return null;
                        }

                        TileEntity tileentity1 = world.getTileEntity(blockposition1);

                        if (tileentity1 instanceof TileEntityChest) {
                            if (enumdirection != EnumFacing.WEST && enumdirection != EnumFacing.NORTH) {
                                object = new InventoryLargeChest("container.chestDouble", (ILockableContainer) object, (TileEntityChest) tileentity1);
                            } else {
                                object = new InventoryLargeChest("container.chestDouble", (TileEntityChest) tileentity1, (ILockableContainer) object);
                            }
                        }
                    }
                }

                return (ILockableContainer) object;
            }
        }
    }

    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityChest();
    }

    public boolean canProvidePower(IBlockState iblockdata) {
        return this.chestType == BlockChest.Type.TRAP;
    }

    public int getWeakPower(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        if (!iblockdata.canProvidePower()) {
            return 0;
        } else {
            int i = 0;
            TileEntity tileentity = iblockaccess.getTileEntity(blockposition);

            if (tileentity instanceof TileEntityChest) {
                i = ((TileEntityChest) tileentity).numPlayersUsing;
            }

            return MathHelper.clamp(i, 0, 15);
        }
    }

    public int getStrongPower(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        return enumdirection == EnumFacing.UP ? iblockdata.getWeakPower(iblockaccess, blockposition, enumdirection) : 0;
    }

    private boolean isBlocked(World world, BlockPos blockposition) {
        return this.isBelowSolidBlock(world, blockposition) || this.isOcelotSittingOnChest(world, blockposition);
    }

    private boolean isBelowSolidBlock(World world, BlockPos blockposition) {
        return world.getBlockState(blockposition.up()).isNormalCube();
    }

    private boolean isOcelotSittingOnChest(World world, BlockPos blockposition) {
        // Paper start - Option ti dsiable chest cat detection
        if (world.paperConfig.disableChestCatDetection) {
            return false;
        }
        // Paper end
        Iterator iterator = world.getEntitiesWithinAABB(EntityOcelot.class, new AxisAlignedBB((double) blockposition.getX(), (double) (blockposition.getY() + 1), (double) blockposition.getZ(), (double) (blockposition.getX() + 1), (double) (blockposition.getY() + 2), (double) (blockposition.getZ() + 1))).iterator();

        EntityOcelot entityocelot;

        do {
            if (!iterator.hasNext()) {
                return false;
            }

            Entity entity = (Entity) iterator.next();

            entityocelot = (EntityOcelot) entity;
        } while (!entityocelot.isSitting());

        return true;
    }

    public boolean hasComparatorInputOverride(IBlockState iblockdata) {
        return true;
    }

    public int getComparatorInputOverride(IBlockState iblockdata, World world, BlockPos blockposition) {
        return Container.calcRedstoneFromInventory((IInventory) this.getLockableContainer(world, blockposition));
    }

    public IBlockState getStateFromMeta(int i) {
        EnumFacing enumdirection = EnumFacing.getFront(i);

        if (enumdirection.getAxis() == EnumFacing.Axis.Y) {
            enumdirection = EnumFacing.NORTH;
        }

        return this.getDefaultState().withProperty(BlockChest.FACING, enumdirection);
    }

    public int getMetaFromState(IBlockState iblockdata) {
        return ((EnumFacing) iblockdata.getValue(BlockChest.FACING)).getIndex();
    }

    public IBlockState withRotation(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.withProperty(BlockChest.FACING, enumblockrotation.rotate((EnumFacing) iblockdata.getValue(BlockChest.FACING)));
    }

    public IBlockState withMirror(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata.withRotation(enumblockmirror.toRotation((EnumFacing) iblockdata.getValue(BlockChest.FACING)));
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockChest.FACING});
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }

    public static enum Type {

        BASIC, TRAP;

        private Type() {}
    }
}
