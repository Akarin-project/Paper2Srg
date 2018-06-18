package net.minecraft.block;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;


import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.bukkit.event.entity.EntityInteractEvent;

public class BlockTripWire extends Block {

    public static final PropertyBool POWERED = PropertyBool.create("powered");
    public static final PropertyBool ATTACHED = PropertyBool.create("attached");
    public static final PropertyBool DISARMED = PropertyBool.create("disarmed");
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");
    protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.0D, 0.0625D, 0.0D, 1.0D, 0.15625D, 1.0D);
    protected static final AxisAlignedBB TRIP_WRITE_ATTACHED_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);

    public BlockTripWire() {
        super(Material.CIRCUITS);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockTripWire.POWERED, Boolean.valueOf(false)).withProperty(BlockTripWire.ATTACHED, Boolean.valueOf(false)).withProperty(BlockTripWire.DISARMED, Boolean.valueOf(false)).withProperty(BlockTripWire.NORTH, Boolean.valueOf(false)).withProperty(BlockTripWire.EAST, Boolean.valueOf(false)).withProperty(BlockTripWire.SOUTH, Boolean.valueOf(false)).withProperty(BlockTripWire.WEST, Boolean.valueOf(false)));
        this.setTickRandomly(true);
    }

    public AxisAlignedBB getBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return !((Boolean) iblockdata.getValue(BlockTripWire.ATTACHED)).booleanValue() ? BlockTripWire.TRIP_WRITE_ATTACHED_AABB : BlockTripWire.AABB;
    }

    public IBlockState getActualState(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return iblockdata.withProperty(BlockTripWire.NORTH, Boolean.valueOf(isConnectedTo(iblockaccess, blockposition, iblockdata, EnumFacing.NORTH))).withProperty(BlockTripWire.EAST, Boolean.valueOf(isConnectedTo(iblockaccess, blockposition, iblockdata, EnumFacing.EAST))).withProperty(BlockTripWire.SOUTH, Boolean.valueOf(isConnectedTo(iblockaccess, blockposition, iblockdata, EnumFacing.SOUTH))).withProperty(BlockTripWire.WEST, Boolean.valueOf(isConnectedTo(iblockaccess, blockposition, iblockdata, EnumFacing.WEST)));
    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockTripWire.NULL_AABB;
    }

    public boolean isOpaqueCube(IBlockState iblockdata) {
        return false;
    }

    public boolean isFullCube(IBlockState iblockdata) {
        return false;
    }

    public Item getItemDropped(IBlockState iblockdata, Random random, int i) {
        return Items.STRING;
    }

    public ItemStack getItem(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(Items.STRING);
    }

    public void onBlockAdded(World world, BlockPos blockposition, IBlockState iblockdata) {
        world.setBlockState(blockposition, iblockdata, 3);
        this.notifyHook(world, blockposition, iblockdata);
    }

    public void breakBlock(World world, BlockPos blockposition, IBlockState iblockdata) {
        this.notifyHook(world, blockposition, iblockdata.withProperty(BlockTripWire.POWERED, Boolean.valueOf(true)));
    }

    public void onBlockHarvested(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman) {
        if (!world.isRemote) {
            if (!entityhuman.getHeldItemMainhand().isEmpty() && entityhuman.getHeldItemMainhand().getItem() == Items.SHEARS) {
                world.setBlockState(blockposition, iblockdata.withProperty(BlockTripWire.DISARMED, Boolean.valueOf(true)), 4);
            }

        }
    }

    private void notifyHook(World world, BlockPos blockposition, IBlockState iblockdata) {
        EnumFacing[] aenumdirection = new EnumFacing[] { EnumFacing.SOUTH, EnumFacing.WEST};
        int i = aenumdirection.length;
        int j = 0;

        while (j < i) {
            EnumFacing enumdirection = aenumdirection[j];
            int k = 1;

            while (true) {
                if (k < 42) {
                    BlockPos blockposition1 = blockposition.offset(enumdirection, k);
                    IBlockState iblockdata1 = world.getBlockState(blockposition1);

                    if (iblockdata1.getBlock() == Blocks.TRIPWIRE_HOOK) {
                        if (iblockdata1.getValue(BlockTripWireHook.FACING) == enumdirection.getOpposite()) {
                            Blocks.TRIPWIRE_HOOK.calculateState(world, blockposition1, iblockdata1, false, true, k, iblockdata);
                        }
                    } else if (iblockdata1.getBlock() == Blocks.TRIPWIRE) {
                        ++k;
                        continue;
                    }
                }

                ++j;
                break;
            }
        }

    }

    public void onEntityCollidedWithBlock(World world, BlockPos blockposition, IBlockState iblockdata, Entity entity) {
        if (!world.isRemote) {
            if (!((Boolean) iblockdata.getValue(BlockTripWire.POWERED)).booleanValue()) {
                this.updateState(world, blockposition);
            }
        }
    }

    public void randomTick(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {}

    public void updateTick(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        if (!world.isRemote) {
            if (((Boolean) world.getBlockState(blockposition).getValue(BlockTripWire.POWERED)).booleanValue()) {
                this.updateState(world, blockposition);
            }
        }
    }

    private void updateState(World world, BlockPos blockposition) {
        IBlockState iblockdata = world.getBlockState(blockposition);
        boolean flag = ((Boolean) iblockdata.getValue(BlockTripWire.POWERED)).booleanValue();
        boolean flag1 = false;
        List list = world.getEntitiesWithinAABBExcludingEntity((Entity) null, iblockdata.getBoundingBox(world, blockposition).offset(blockposition));

        if (!list.isEmpty()) {
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                Entity entity = (Entity) iterator.next();

                if (!entity.doesEntityNotTriggerPressurePlate()) {
                    flag1 = true;
                    break;
                }
            }
        }

        // CraftBukkit start - Call interact even when triggering connected tripwire
        if (flag != flag1 && flag1 && (Boolean)iblockdata.getValue(ATTACHED)) {
            org.bukkit.World bworld = world.getWorld();
            org.bukkit.plugin.PluginManager manager = world.getServer().getPluginManager();
            org.bukkit.block.Block block = bworld.getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());
            boolean allowed = false;

            // If all of the events are cancelled block the tripwire trigger, else allow
            for (Object object : list) {
                if (object != null) {
                    org.bukkit.event.Cancellable cancellable;

                    if (object instanceof EntityPlayer) {
                        cancellable = org.bukkit.craftbukkit.event.CraftEventFactory.callPlayerInteractEvent((EntityPlayer) object, org.bukkit.event.block.Action.PHYSICAL, blockposition, null, null, null);
                    } else if (object instanceof Entity) {
                        cancellable = new EntityInteractEvent(((Entity) object).getBukkitEntity(), block);
                        manager.callEvent((EntityInteractEvent) cancellable);
                    } else {
                        continue;
                    }

                    if (!cancellable.isCancelled()) {
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

        if (flag1 != flag) {
            iblockdata = iblockdata.withProperty(BlockTripWire.POWERED, Boolean.valueOf(flag1));
            world.setBlockState(blockposition, iblockdata, 3);
            this.notifyHook(world, blockposition, iblockdata);
        }

        if (flag1) {
            world.scheduleUpdate(new BlockPos(blockposition), (Block) this, this.tickRate(world));
        }

    }

    public static boolean isConnectedTo(IBlockAccess iblockaccess, BlockPos blockposition, IBlockState iblockdata, EnumFacing enumdirection) {
        BlockPos blockposition1 = blockposition.offset(enumdirection);
        IBlockState iblockdata1 = iblockaccess.getBlockState(blockposition1);
        Block block = iblockdata1.getBlock();

        if (block == Blocks.TRIPWIRE_HOOK) {
            EnumFacing enumdirection1 = enumdirection.getOpposite();

            return iblockdata1.getValue(BlockTripWireHook.FACING) == enumdirection1;
        } else {
            return block == Blocks.TRIPWIRE;
        }
    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockTripWire.POWERED, Boolean.valueOf((i & 1) > 0)).withProperty(BlockTripWire.ATTACHED, Boolean.valueOf((i & 4) > 0)).withProperty(BlockTripWire.DISARMED, Boolean.valueOf((i & 8) > 0));
    }

    public int getMetaFromState(IBlockState iblockdata) {
        int i = 0;

        if (((Boolean) iblockdata.getValue(BlockTripWire.POWERED)).booleanValue()) {
            i |= 1;
        }

        if (((Boolean) iblockdata.getValue(BlockTripWire.ATTACHED)).booleanValue()) {
            i |= 4;
        }

        if (((Boolean) iblockdata.getValue(BlockTripWire.DISARMED)).booleanValue()) {
            i |= 8;
        }

        return i;
    }

    public IBlockState withRotation(IBlockState iblockdata, Rotation enumblockrotation) {
        switch (enumblockrotation) {
        case CLOCKWISE_180:
            return iblockdata.withProperty(BlockTripWire.NORTH, iblockdata.getValue(BlockTripWire.SOUTH)).withProperty(BlockTripWire.EAST, iblockdata.getValue(BlockTripWire.WEST)).withProperty(BlockTripWire.SOUTH, iblockdata.getValue(BlockTripWire.NORTH)).withProperty(BlockTripWire.WEST, iblockdata.getValue(BlockTripWire.EAST));

        case COUNTERCLOCKWISE_90:
            return iblockdata.withProperty(BlockTripWire.NORTH, iblockdata.getValue(BlockTripWire.EAST)).withProperty(BlockTripWire.EAST, iblockdata.getValue(BlockTripWire.SOUTH)).withProperty(BlockTripWire.SOUTH, iblockdata.getValue(BlockTripWire.WEST)).withProperty(BlockTripWire.WEST, iblockdata.getValue(BlockTripWire.NORTH));

        case CLOCKWISE_90:
            return iblockdata.withProperty(BlockTripWire.NORTH, iblockdata.getValue(BlockTripWire.WEST)).withProperty(BlockTripWire.EAST, iblockdata.getValue(BlockTripWire.NORTH)).withProperty(BlockTripWire.SOUTH, iblockdata.getValue(BlockTripWire.EAST)).withProperty(BlockTripWire.WEST, iblockdata.getValue(BlockTripWire.SOUTH));

        default:
            return iblockdata;
        }
    }

    public IBlockState withMirror(IBlockState iblockdata, Mirror enumblockmirror) {
        switch (enumblockmirror) {
        case LEFT_RIGHT:
            return iblockdata.withProperty(BlockTripWire.NORTH, iblockdata.getValue(BlockTripWire.SOUTH)).withProperty(BlockTripWire.SOUTH, iblockdata.getValue(BlockTripWire.NORTH));

        case FRONT_BACK:
            return iblockdata.withProperty(BlockTripWire.EAST, iblockdata.getValue(BlockTripWire.WEST)).withProperty(BlockTripWire.WEST, iblockdata.getValue(BlockTripWire.EAST));

        default:
            return super.withMirror(iblockdata, enumblockmirror);
        }
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockTripWire.POWERED, BlockTripWire.ATTACHED, BlockTripWire.DISARMED, BlockTripWire.NORTH, BlockTripWire.EAST, BlockTripWire.WEST, BlockTripWire.SOUTH});
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }
}
