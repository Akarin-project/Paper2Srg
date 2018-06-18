package net.minecraft.block;

import java.util.Iterator;
import javax.annotation.Nullable;


import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.bukkit.event.block.BlockRedstoneEvent;

public class BlockLever extends Block {

    public static final PropertyEnum<BlockLever.EnumOrientation> FACING = PropertyEnum.create("facing", BlockLever.EnumOrientation.class);
    public static final PropertyBool POWERED = PropertyBool.create("powered");
    protected static final AxisAlignedBB LEVER_NORTH_AABB = new AxisAlignedBB(0.3125D, 0.20000000298023224D, 0.625D, 0.6875D, 0.800000011920929D, 1.0D);
    protected static final AxisAlignedBB LEVER_SOUTH_AABB = new AxisAlignedBB(0.3125D, 0.20000000298023224D, 0.0D, 0.6875D, 0.800000011920929D, 0.375D);
    protected static final AxisAlignedBB LEVER_WEST_AABB = new AxisAlignedBB(0.625D, 0.20000000298023224D, 0.3125D, 1.0D, 0.800000011920929D, 0.6875D);
    protected static final AxisAlignedBB LEVER_EAST_AABB = new AxisAlignedBB(0.0D, 0.20000000298023224D, 0.3125D, 0.375D, 0.800000011920929D, 0.6875D);
    protected static final AxisAlignedBB LEVER_UP_AABB = new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 0.6000000238418579D, 0.75D);
    protected static final AxisAlignedBB LEVER_DOWN_AABB = new AxisAlignedBB(0.25D, 0.4000000059604645D, 0.25D, 0.75D, 1.0D, 0.75D);

    protected BlockLever() {
        super(Material.CIRCUITS);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockLever.FACING, BlockLever.EnumOrientation.NORTH).withProperty(BlockLever.POWERED, Boolean.valueOf(false)));
        this.setCreativeTab(CreativeTabs.REDSTONE);
    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockLever.NULL_AABB;
    }

    public boolean isOpaqueCube(IBlockState iblockdata) {
        return false;
    }

    public boolean isFullCube(IBlockState iblockdata) {
        return false;
    }

    public boolean canPlaceBlockOnSide(World world, BlockPos blockposition, EnumFacing enumdirection) {
        return canAttachTo(world, blockposition, enumdirection);
    }

    public boolean canPlaceBlockAt(World world, BlockPos blockposition) {
        EnumFacing[] aenumdirection = EnumFacing.values();
        int i = aenumdirection.length;

        for (int j = 0; j < i; ++j) {
            EnumFacing enumdirection = aenumdirection[j];

            if (canAttachTo(world, blockposition, enumdirection)) {
                return true;
            }
        }

        return false;
    }

    protected static boolean canAttachTo(World world, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockButton.canPlaceBlock(world, blockposition, enumdirection);
    }

    public IBlockState getStateForPlacement(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        IBlockState iblockdata = this.getDefaultState().withProperty(BlockLever.POWERED, Boolean.valueOf(false));

        if (canAttachTo(world, blockposition, enumdirection)) {
            return iblockdata.withProperty(BlockLever.FACING, BlockLever.EnumOrientation.forFacings(enumdirection, entityliving.getHorizontalFacing()));
        } else {
            Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

            EnumFacing enumdirection1;

            do {
                if (!iterator.hasNext()) {
                    if (world.getBlockState(blockposition.down()).isTopSolid()) {
                        return iblockdata.withProperty(BlockLever.FACING, BlockLever.EnumOrientation.forFacings(EnumFacing.UP, entityliving.getHorizontalFacing()));
                    }

                    return iblockdata;
                }

                enumdirection1 = (EnumFacing) iterator.next();
            } while (enumdirection1 == enumdirection || !canAttachTo(world, blockposition, enumdirection1));

            return iblockdata.withProperty(BlockLever.FACING, BlockLever.EnumOrientation.forFacings(enumdirection1, entityliving.getHorizontalFacing()));
        }
    }

    public void neighborChanged(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        if (this.checkCanSurvive(world, blockposition, iblockdata) && !canAttachTo(world, blockposition, ((BlockLever.EnumOrientation) iblockdata.getValue(BlockLever.FACING)).getFacing())) {
            this.dropBlockAsItem(world, blockposition, iblockdata, 0);
            world.setBlockToAir(blockposition);
        }

    }

    private boolean checkCanSurvive(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (this.canPlaceBlockAt(world, blockposition)) {
            return true;
        } else {
            this.dropBlockAsItem(world, blockposition, iblockdata, 0);
            world.setBlockToAir(blockposition);
            return false;
        }
    }

    public AxisAlignedBB getBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        switch ((BlockLever.EnumOrientation) iblockdata.getValue(BlockLever.FACING)) {
        case EAST:
        default:
            return BlockLever.LEVER_EAST_AABB;

        case WEST:
            return BlockLever.LEVER_WEST_AABB;

        case SOUTH:
            return BlockLever.LEVER_SOUTH_AABB;

        case NORTH:
            return BlockLever.LEVER_NORTH_AABB;

        case UP_Z:
        case UP_X:
            return BlockLever.LEVER_UP_AABB;

        case DOWN_X:
        case DOWN_Z:
            return BlockLever.LEVER_DOWN_AABB;
        }
    }

    public boolean onBlockActivated(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (world.isRemote) {
            return true;
        } else {
            // CraftBukkit start - Interact Lever
            boolean powered = iblockdata.getValue(POWERED);
            org.bukkit.block.Block block = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());
            int old = (powered) ? 15 : 0;
            int current = (!powered) ? 15 : 0;

            BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, old, current);
            world.getServer().getPluginManager().callEvent(eventRedstone);

            if ((eventRedstone.getNewCurrent() > 0) != (!powered)) {
                return true;
            }
            // CraftBukkit end

            iblockdata = iblockdata.cycleProperty((IProperty) BlockLever.POWERED);
            world.setBlockState(blockposition, iblockdata, 3);
            float f3 = ((Boolean) iblockdata.getValue(BlockLever.POWERED)).booleanValue() ? 0.6F : 0.5F;

            world.playSound((EntityPlayer) null, blockposition, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, f3);
            world.notifyNeighborsOfStateChange(blockposition, this, false);
            EnumFacing enumdirection1 = ((BlockLever.EnumOrientation) iblockdata.getValue(BlockLever.FACING)).getFacing();

            world.notifyNeighborsOfStateChange(blockposition.offset(enumdirection1.getOpposite()), this, false);
            return true;
        }
    }

    public void breakBlock(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (((Boolean) iblockdata.getValue(BlockLever.POWERED)).booleanValue()) {
            world.notifyNeighborsOfStateChange(blockposition, this, false);
            EnumFacing enumdirection = ((BlockLever.EnumOrientation) iblockdata.getValue(BlockLever.FACING)).getFacing();

            world.notifyNeighborsOfStateChange(blockposition.offset(enumdirection.getOpposite()), this, false);
        }

        super.breakBlock(world, blockposition, iblockdata);
    }

    public int getWeakPower(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        return ((Boolean) iblockdata.getValue(BlockLever.POWERED)).booleanValue() ? 15 : 0;
    }

    public int getStrongPower(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        return !((Boolean) iblockdata.getValue(BlockLever.POWERED)).booleanValue() ? 0 : (((BlockLever.EnumOrientation) iblockdata.getValue(BlockLever.FACING)).getFacing() == enumdirection ? 15 : 0);
    }

    public boolean canProvidePower(IBlockState iblockdata) {
        return true;
    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockLever.FACING, BlockLever.EnumOrientation.byMetadata(i & 7)).withProperty(BlockLever.POWERED, Boolean.valueOf((i & 8) > 0));
    }

    public int getMetaFromState(IBlockState iblockdata) {
        byte b0 = 0;
        int i = b0 | ((BlockLever.EnumOrientation) iblockdata.getValue(BlockLever.FACING)).getMetadata();

        if (((Boolean) iblockdata.getValue(BlockLever.POWERED)).booleanValue()) {
            i |= 8;
        }

        return i;
    }

    public IBlockState withRotation(IBlockState iblockdata, Rotation enumblockrotation) {
        switch (enumblockrotation) {
        case CLOCKWISE_180:
            switch ((BlockLever.EnumOrientation) iblockdata.getValue(BlockLever.FACING)) {
            case EAST:
                return iblockdata.withProperty(BlockLever.FACING, BlockLever.EnumOrientation.WEST);

            case WEST:
                return iblockdata.withProperty(BlockLever.FACING, BlockLever.EnumOrientation.EAST);

            case SOUTH:
                return iblockdata.withProperty(BlockLever.FACING, BlockLever.EnumOrientation.NORTH);

            case NORTH:
                return iblockdata.withProperty(BlockLever.FACING, BlockLever.EnumOrientation.SOUTH);

            default:
                return iblockdata;
            }

        case COUNTERCLOCKWISE_90:
            switch ((BlockLever.EnumOrientation) iblockdata.getValue(BlockLever.FACING)) {
            case EAST:
                return iblockdata.withProperty(BlockLever.FACING, BlockLever.EnumOrientation.NORTH);

            case WEST:
                return iblockdata.withProperty(BlockLever.FACING, BlockLever.EnumOrientation.SOUTH);

            case SOUTH:
                return iblockdata.withProperty(BlockLever.FACING, BlockLever.EnumOrientation.EAST);

            case NORTH:
                return iblockdata.withProperty(BlockLever.FACING, BlockLever.EnumOrientation.WEST);

            case UP_Z:
                return iblockdata.withProperty(BlockLever.FACING, BlockLever.EnumOrientation.UP_X);

            case UP_X:
                return iblockdata.withProperty(BlockLever.FACING, BlockLever.EnumOrientation.UP_Z);

            case DOWN_X:
                return iblockdata.withProperty(BlockLever.FACING, BlockLever.EnumOrientation.DOWN_Z);

            case DOWN_Z:
                return iblockdata.withProperty(BlockLever.FACING, BlockLever.EnumOrientation.DOWN_X);
            }

        case CLOCKWISE_90:
            switch ((BlockLever.EnumOrientation) iblockdata.getValue(BlockLever.FACING)) {
            case EAST:
                return iblockdata.withProperty(BlockLever.FACING, BlockLever.EnumOrientation.SOUTH);

            case WEST:
                return iblockdata.withProperty(BlockLever.FACING, BlockLever.EnumOrientation.NORTH);

            case SOUTH:
                return iblockdata.withProperty(BlockLever.FACING, BlockLever.EnumOrientation.WEST);

            case NORTH:
                return iblockdata.withProperty(BlockLever.FACING, BlockLever.EnumOrientation.EAST);

            case UP_Z:
                return iblockdata.withProperty(BlockLever.FACING, BlockLever.EnumOrientation.UP_X);

            case UP_X:
                return iblockdata.withProperty(BlockLever.FACING, BlockLever.EnumOrientation.UP_Z);

            case DOWN_X:
                return iblockdata.withProperty(BlockLever.FACING, BlockLever.EnumOrientation.DOWN_Z);

            case DOWN_Z:
                return iblockdata.withProperty(BlockLever.FACING, BlockLever.EnumOrientation.DOWN_X);
            }

        default:
            return iblockdata;
        }
    }

    public IBlockState withMirror(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata.withRotation(enumblockmirror.toRotation(((BlockLever.EnumOrientation) iblockdata.getValue(BlockLever.FACING)).getFacing()));
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockLever.FACING, BlockLever.POWERED});
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }

    public static enum EnumOrientation implements IStringSerializable {

        DOWN_X(0, "down_x", EnumFacing.DOWN), EAST(1, "east", EnumFacing.EAST), WEST(2, "west", EnumFacing.WEST), SOUTH(3, "south", EnumFacing.SOUTH), NORTH(4, "north", EnumFacing.NORTH), UP_Z(5, "up_z", EnumFacing.UP), UP_X(6, "up_x", EnumFacing.UP), DOWN_Z(7, "down_z", EnumFacing.DOWN);

        private static final BlockLever.EnumOrientation[] META_LOOKUP = new BlockLever.EnumOrientation[values().length];
        private final int meta;
        private final String name;
        private final EnumFacing facing;

        private EnumOrientation(int i, String s, EnumFacing enumdirection) {
            this.meta = i;
            this.name = s;
            this.facing = enumdirection;
        }

        public int getMetadata() {
            return this.meta;
        }

        public EnumFacing getFacing() {
            return this.facing;
        }

        public String toString() {
            return this.name;
        }

        public static BlockLever.EnumOrientation byMetadata(int i) {
            if (i < 0 || i >= BlockLever.EnumOrientation.META_LOOKUP.length) {
                i = 0;
            }

            return BlockLever.EnumOrientation.META_LOOKUP[i];
        }

        public static BlockLever.EnumOrientation forFacings(EnumFacing enumdirection, EnumFacing enumdirection1) {
            switch (enumdirection) {
            case DOWN:
                switch (enumdirection1.getAxis()) {
                case X:
                    return BlockLever.EnumOrientation.DOWN_X;

                case Z:
                    return BlockLever.EnumOrientation.DOWN_Z;

                default:
                    throw new IllegalArgumentException("Invalid entityFacing " + enumdirection1 + " for facing " + enumdirection);
                }

            case UP:
                switch (enumdirection1.getAxis()) {
                case X:
                    return BlockLever.EnumOrientation.UP_X;

                case Z:
                    return BlockLever.EnumOrientation.UP_Z;

                default:
                    throw new IllegalArgumentException("Invalid entityFacing " + enumdirection1 + " for facing " + enumdirection);
                }

            case NORTH:
                return BlockLever.EnumOrientation.NORTH;

            case SOUTH:
                return BlockLever.EnumOrientation.SOUTH;

            case WEST:
                return BlockLever.EnumOrientation.WEST;

            case EAST:
                return BlockLever.EnumOrientation.EAST;

            default:
                throw new IllegalArgumentException("Invalid facing: " + enumdirection);
            }
        }

        public String getName() {
            return this.name;
        }

        static {
            BlockLever.EnumOrientation[] ablocklever_enumleverposition = values();
            int i = ablocklever_enumleverposition.length;

            for (int j = 0; j < i; ++j) {
                BlockLever.EnumOrientation blocklever_enumleverposition = ablocklever_enumleverposition[j];

                BlockLever.EnumOrientation.META_LOOKUP[blocklever_enumleverposition.getMetadata()] = blocklever_enumleverposition;
            }

        }
    }
}
