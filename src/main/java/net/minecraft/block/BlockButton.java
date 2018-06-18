package net.minecraft.block;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.EntityInteractEvent;

// CraftBukkit start
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.EntityInteractEvent;
// CraftBukkit end

public abstract class BlockButton extends BlockDirectional {

    public static final PropertyBool POWERED = PropertyBool.create("powered");
    protected static final AxisAlignedBB AABB_DOWN_OFF = new AxisAlignedBB(0.3125D, 0.875D, 0.375D, 0.6875D, 1.0D, 0.625D);
    protected static final AxisAlignedBB AABB_UP_OFF = new AxisAlignedBB(0.3125D, 0.0D, 0.375D, 0.6875D, 0.125D, 0.625D);
    protected static final AxisAlignedBB AABB_NORTH_OFF = new AxisAlignedBB(0.3125D, 0.375D, 0.875D, 0.6875D, 0.625D, 1.0D);
    protected static final AxisAlignedBB AABB_SOUTH_OFF = new AxisAlignedBB(0.3125D, 0.375D, 0.0D, 0.6875D, 0.625D, 0.125D);
    protected static final AxisAlignedBB AABB_WEST_OFF = new AxisAlignedBB(0.875D, 0.375D, 0.3125D, 1.0D, 0.625D, 0.6875D);
    protected static final AxisAlignedBB AABB_EAST_OFF = new AxisAlignedBB(0.0D, 0.375D, 0.3125D, 0.125D, 0.625D, 0.6875D);
    protected static final AxisAlignedBB AABB_DOWN_ON = new AxisAlignedBB(0.3125D, 0.9375D, 0.375D, 0.6875D, 1.0D, 0.625D);
    protected static final AxisAlignedBB AABB_UP_ON = new AxisAlignedBB(0.3125D, 0.0D, 0.375D, 0.6875D, 0.0625D, 0.625D);
    protected static final AxisAlignedBB AABB_NORTH_ON = new AxisAlignedBB(0.3125D, 0.375D, 0.9375D, 0.6875D, 0.625D, 1.0D);
    protected static final AxisAlignedBB AABB_SOUTH_ON = new AxisAlignedBB(0.3125D, 0.375D, 0.0D, 0.6875D, 0.625D, 0.0625D);
    protected static final AxisAlignedBB AABB_WEST_ON = new AxisAlignedBB(0.9375D, 0.375D, 0.3125D, 1.0D, 0.625D, 0.6875D);
    protected static final AxisAlignedBB AABB_EAST_ON = new AxisAlignedBB(0.0D, 0.375D, 0.3125D, 0.0625D, 0.625D, 0.6875D);
    private final boolean wooden;

    protected BlockButton(boolean flag) {
        super(Material.CIRCUITS);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockButton.FACING, EnumFacing.NORTH).withProperty(BlockButton.POWERED, Boolean.valueOf(false)));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.REDSTONE);
        this.wooden = flag;
    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockButton.NULL_AABB;
    }

    public int tickRate(World world) {
        return this.wooden ? 30 : 20;
    }

    public boolean isOpaqueCube(IBlockState iblockdata) {
        return false;
    }

    public boolean isFullCube(IBlockState iblockdata) {
        return false;
    }

    public boolean canPlaceBlockOnSide(World world, BlockPos blockposition, EnumFacing enumdirection) {
        return canPlaceBlock(world, blockposition, enumdirection);
    }

    public boolean canPlaceBlockAt(World world, BlockPos blockposition) {
        EnumFacing[] aenumdirection = EnumFacing.values();
        int i = aenumdirection.length;

        for (int j = 0; j < i; ++j) {
            EnumFacing enumdirection = aenumdirection[j];

            if (canPlaceBlock(world, blockposition, enumdirection)) {
                return true;
            }
        }

        return false;
    }

    protected static boolean canPlaceBlock(World world, BlockPos blockposition, EnumFacing enumdirection) {
        BlockPos blockposition1 = blockposition.offset(enumdirection.getOpposite());
        IBlockState iblockdata = world.getBlockState(blockposition1);
        boolean flag = iblockdata.getBlockFaceShape(world, blockposition1, enumdirection) == BlockFaceShape.SOLID;
        Block block = iblockdata.getBlock();

        return enumdirection == EnumFacing.UP ? block == Blocks.HOPPER || !isExceptionBlockForAttaching(block) && flag : !isExceptBlockForAttachWithPiston(block) && flag;
    }

    public IBlockState getStateForPlacement(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        return canPlaceBlock(world, blockposition, enumdirection) ? this.getDefaultState().withProperty(BlockButton.FACING, enumdirection).withProperty(BlockButton.POWERED, Boolean.valueOf(false)) : this.getDefaultState().withProperty(BlockButton.FACING, EnumFacing.DOWN).withProperty(BlockButton.POWERED, Boolean.valueOf(false));
    }

    public void neighborChanged(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        if (this.checkForDrop(world, blockposition, iblockdata) && !canPlaceBlock(world, blockposition, (EnumFacing) iblockdata.getValue(BlockButton.FACING))) {
            this.dropBlockAsItem(world, blockposition, iblockdata, 0);
            world.setBlockToAir(blockposition);
        }

    }

    private boolean checkForDrop(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (this.canPlaceBlockAt(world, blockposition)) {
            return true;
        } else {
            this.dropBlockAsItem(world, blockposition, iblockdata, 0);
            world.setBlockToAir(blockposition);
            return false;
        }
    }

    public AxisAlignedBB getBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        EnumFacing enumdirection = (EnumFacing) iblockdata.getValue(BlockButton.FACING);
        boolean flag = ((Boolean) iblockdata.getValue(BlockButton.POWERED)).booleanValue();

        switch (enumdirection) {
        case EAST:
            return flag ? BlockButton.AABB_EAST_ON : BlockButton.AABB_EAST_OFF;

        case WEST:
            return flag ? BlockButton.AABB_WEST_ON : BlockButton.AABB_WEST_OFF;

        case SOUTH:
            return flag ? BlockButton.AABB_SOUTH_ON : BlockButton.AABB_SOUTH_OFF;

        case NORTH:
        default:
            return flag ? BlockButton.AABB_NORTH_ON : BlockButton.AABB_NORTH_OFF;

        case UP:
            return flag ? BlockButton.AABB_UP_ON : BlockButton.AABB_UP_OFF;

        case DOWN:
            return flag ? BlockButton.AABB_DOWN_ON : BlockButton.AABB_DOWN_OFF;
        }
    }

    public boolean onBlockActivated(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (((Boolean) iblockdata.getValue(BlockButton.POWERED)).booleanValue()) {
            return true;
        } else {
            // CraftBukkit start
            boolean powered = ((Boolean) iblockdata.getValue(POWERED));
            org.bukkit.block.Block block = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());
            int old = (powered) ? 15 : 0;
            int current = (!powered) ? 15 : 0;

            BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, old, current);
            world.getServer().getPluginManager().callEvent(eventRedstone);

            if ((eventRedstone.getNewCurrent() > 0) != (!powered)) {
                return true;
            }
            // CraftBukkit end
            world.setBlockState(blockposition, iblockdata.withProperty(BlockButton.POWERED, Boolean.valueOf(true)), 3);
            world.markBlockRangeForRenderUpdate(blockposition, blockposition);
            this.playClickSound(entityhuman, world, blockposition);
            this.notifyNeighbors(world, blockposition, (EnumFacing) iblockdata.getValue(BlockButton.FACING));
            world.scheduleUpdate(blockposition, (Block) this, this.tickRate(world));
            return true;
        }
    }

    protected abstract void playClickSound(@Nullable EntityPlayer entityhuman, World world, BlockPos blockposition);

    protected abstract void playReleaseSound(World world, BlockPos blockposition);

    public void breakBlock(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (((Boolean) iblockdata.getValue(BlockButton.POWERED)).booleanValue()) {
            this.notifyNeighbors(world, blockposition, (EnumFacing) iblockdata.getValue(BlockButton.FACING));
        }

        super.breakBlock(world, blockposition, iblockdata);
    }

    public int getWeakPower(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        return ((Boolean) iblockdata.getValue(BlockButton.POWERED)).booleanValue() ? 15 : 0;
    }

    public int getStrongPower(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        return !((Boolean) iblockdata.getValue(BlockButton.POWERED)).booleanValue() ? 0 : (iblockdata.getValue(BlockButton.FACING) == enumdirection ? 15 : 0);
    }

    public boolean canProvidePower(IBlockState iblockdata) {
        return true;
    }

    public void randomTick(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {}

    public void updateTick(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        if (!world.isRemote) {
            if (((Boolean) iblockdata.getValue(BlockButton.POWERED)).booleanValue()) {
                if (this.wooden) {
                    this.checkPressed(iblockdata, world, blockposition);
                } else {
                    // CraftBukkit start
                    org.bukkit.block.Block block = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());

                    BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, 15, 0);
                    world.getServer().getPluginManager().callEvent(eventRedstone);

                    if (eventRedstone.getNewCurrent() > 0) {
                        return;
                    }
                    // CraftBukkit end
                    world.setBlockState(blockposition, iblockdata.withProperty(BlockButton.POWERED, Boolean.valueOf(false)));
                    this.notifyNeighbors(world, blockposition, (EnumFacing) iblockdata.getValue(BlockButton.FACING));
                    this.playReleaseSound(world, blockposition);
                    world.markBlockRangeForRenderUpdate(blockposition, blockposition);
                }

            }
        }
    }

    public void onEntityCollidedWithBlock(World world, BlockPos blockposition, IBlockState iblockdata, Entity entity) {
        if (!world.isRemote) {
            if (this.wooden) {
                if (!((Boolean) iblockdata.getValue(BlockButton.POWERED)).booleanValue()) {
                    this.checkPressed(iblockdata, world, blockposition);
                }
            }
        }
    }

    private void checkPressed(IBlockState iblockdata, World world, BlockPos blockposition) {
        List list = world.getEntitiesWithinAABB(EntityArrow.class, iblockdata.getBoundingBox(world, blockposition).offset(blockposition));
        boolean flag = !list.isEmpty();
        boolean flag1 = ((Boolean) iblockdata.getValue(BlockButton.POWERED)).booleanValue();

        // CraftBukkit start - Call interact event when arrows turn on wooden buttons
        if (flag1 != flag && flag) {
            org.bukkit.block.Block block = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());
            boolean allowed = false;

            // If all of the events are cancelled block the button press, else allow
            for (Object object : list) {
                if (object != null) {
                    EntityInteractEvent event = new EntityInteractEvent(((Entity) object).getBukkitEntity(), block);
                    world.getServer().getPluginManager().callEvent(event);

                    if (!event.isCancelled()) {
                        allowed = true;
                        break;
                    }
                }
            }

            if (!allowed) {
                return;
            }
        }
        // CraftBukkit end

        if (flag && !flag1) {
            // CraftBukkit start
            org.bukkit.block.Block block = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());

            BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, 0, 15);
            world.getServer().getPluginManager().callEvent(eventRedstone);

            if (eventRedstone.getNewCurrent() <= 0) {
                return;
            }
            // CraftBukkit end
            world.setBlockState(blockposition, iblockdata.withProperty(BlockButton.POWERED, Boolean.valueOf(true)));
            this.notifyNeighbors(world, blockposition, (EnumFacing) iblockdata.getValue(BlockButton.FACING));
            world.markBlockRangeForRenderUpdate(blockposition, blockposition);
            this.playClickSound((EntityPlayer) null, world, blockposition);
        }

        if (!flag && flag1) {
            // CraftBukkit start
            org.bukkit.block.Block block = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());

            BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, 15, 0);
            world.getServer().getPluginManager().callEvent(eventRedstone);

            if (eventRedstone.getNewCurrent() > 0) {
                return;
            }
            // CraftBukkit end
            world.setBlockState(blockposition, iblockdata.withProperty(BlockButton.POWERED, Boolean.valueOf(false)));
            this.notifyNeighbors(world, blockposition, (EnumFacing) iblockdata.getValue(BlockButton.FACING));
            world.markBlockRangeForRenderUpdate(blockposition, blockposition);
            this.playReleaseSound(world, blockposition);
        }

        if (flag) {
            world.scheduleUpdate(new BlockPos(blockposition), (Block) this, this.tickRate(world));
        }

    }

    private void notifyNeighbors(World world, BlockPos blockposition, EnumFacing enumdirection) {
        world.notifyNeighborsOfStateChange(blockposition, this, false);
        world.notifyNeighborsOfStateChange(blockposition.offset(enumdirection.getOpposite()), this, false);
    }

    public IBlockState getStateFromMeta(int i) {
        EnumFacing enumdirection;

        switch (i & 7) {
        case 0:
            enumdirection = EnumFacing.DOWN;
            break;

        case 1:
            enumdirection = EnumFacing.EAST;
            break;

        case 2:
            enumdirection = EnumFacing.WEST;
            break;

        case 3:
            enumdirection = EnumFacing.SOUTH;
            break;

        case 4:
            enumdirection = EnumFacing.NORTH;
            break;

        case 5:
        default:
            enumdirection = EnumFacing.UP;
        }

        return this.getDefaultState().withProperty(BlockButton.FACING, enumdirection).withProperty(BlockButton.POWERED, Boolean.valueOf((i & 8) > 0));
    }

    public int getMetaFromState(IBlockState iblockdata) {
        int i;

        switch ((EnumFacing) iblockdata.getValue(BlockButton.FACING)) {
        case EAST:
            i = 1;
            break;

        case WEST:
            i = 2;
            break;

        case SOUTH:
            i = 3;
            break;

        case NORTH:
            i = 4;
            break;

        case UP:
        default:
            i = 5;
            break;

        case DOWN:
            i = 0;
        }

        if (((Boolean) iblockdata.getValue(BlockButton.POWERED)).booleanValue()) {
            i |= 8;
        }

        return i;
    }

    public IBlockState withRotation(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.withProperty(BlockButton.FACING, enumblockrotation.rotate((EnumFacing) iblockdata.getValue(BlockButton.FACING)));
    }

    public IBlockState withMirror(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata.withRotation(enumblockmirror.toRotation((EnumFacing) iblockdata.getValue(BlockButton.FACING)));
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockButton.FACING, BlockButton.POWERED});
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }
}
