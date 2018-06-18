package net.minecraft.block;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockStairs extends Block {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyEnum<BlockStairs.EnumHalf> HALF = PropertyEnum.create("half", BlockStairs.EnumHalf.class);
    public static final PropertyEnum<BlockStairs.EnumShape> SHAPE = PropertyEnum.create("shape", BlockStairs.EnumShape.class);
    protected static final AxisAlignedBB AABB_SLAB_TOP = new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_QTR_TOP_WEST = new AxisAlignedBB(0.0D, 0.5D, 0.0D, 0.5D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_QTR_TOP_EAST = new AxisAlignedBB(0.5D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_QTR_TOP_NORTH = new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 0.5D);
    protected static final AxisAlignedBB AABB_QTR_TOP_SOUTH = new AxisAlignedBB(0.0D, 0.5D, 0.5D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_OCT_TOP_NW = new AxisAlignedBB(0.0D, 0.5D, 0.0D, 0.5D, 1.0D, 0.5D);
    protected static final AxisAlignedBB AABB_OCT_TOP_NE = new AxisAlignedBB(0.5D, 0.5D, 0.0D, 1.0D, 1.0D, 0.5D);
    protected static final AxisAlignedBB AABB_OCT_TOP_SW = new AxisAlignedBB(0.0D, 0.5D, 0.5D, 0.5D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_OCT_TOP_SE = new AxisAlignedBB(0.5D, 0.5D, 0.5D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_SLAB_BOTTOM = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
    protected static final AxisAlignedBB AABB_QTR_BOT_WEST = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5D, 0.5D, 1.0D);
    protected static final AxisAlignedBB AABB_QTR_BOT_EAST = new AxisAlignedBB(0.5D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
    protected static final AxisAlignedBB AABB_QTR_BOT_NORTH = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 0.5D);
    protected static final AxisAlignedBB AABB_QTR_BOT_SOUTH = new AxisAlignedBB(0.0D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D);
    protected static final AxisAlignedBB AABB_OCT_BOT_NW = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5D, 0.5D, 0.5D);
    protected static final AxisAlignedBB AABB_OCT_BOT_NE = new AxisAlignedBB(0.5D, 0.0D, 0.0D, 1.0D, 0.5D, 0.5D);
    protected static final AxisAlignedBB AABB_OCT_BOT_SW = new AxisAlignedBB(0.0D, 0.0D, 0.5D, 0.5D, 0.5D, 1.0D);
    protected static final AxisAlignedBB AABB_OCT_BOT_SE = new AxisAlignedBB(0.5D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D);
    private final Block modelBlock;
    private final IBlockState modelState;

    protected BlockStairs(IBlockState iblockdata) {
        super(iblockdata.getBlock().blockMaterial);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockStairs.FACING, EnumFacing.NORTH).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM).withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.STRAIGHT));
        this.modelBlock = iblockdata.getBlock();
        this.modelState = iblockdata;
        this.setHardness(this.modelBlock.blockHardness);
        this.setResistance(this.modelBlock.blockResistance / 3.0F);
        this.setSoundType(this.modelBlock.blockSoundType);
        this.setLightOpacity(255);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    public void addCollisionBoxToList(IBlockState iblockdata, World world, BlockPos blockposition, AxisAlignedBB axisalignedbb, List<AxisAlignedBB> list, @Nullable Entity entity, boolean flag) {
        if (!flag) {
            iblockdata = this.getActualState(iblockdata, world, blockposition);
        }

        Iterator iterator = getCollisionBoxList(iblockdata).iterator();

        while (iterator.hasNext()) {
            AxisAlignedBB axisalignedbb1 = (AxisAlignedBB) iterator.next();

            addCollisionBoxToList(blockposition, axisalignedbb, list, axisalignedbb1);
        }

    }

    private static List<AxisAlignedBB> getCollisionBoxList(IBlockState iblockdata) {
        ArrayList arraylist = Lists.newArrayList();
        boolean flag = iblockdata.getValue(BlockStairs.HALF) == BlockStairs.EnumHalf.TOP;

        arraylist.add(flag ? BlockStairs.AABB_SLAB_TOP : BlockStairs.AABB_SLAB_BOTTOM);
        BlockStairs.EnumShape blockstairs_enumstairshape = (BlockStairs.EnumShape) iblockdata.getValue(BlockStairs.SHAPE);

        if (blockstairs_enumstairshape == BlockStairs.EnumShape.STRAIGHT || blockstairs_enumstairshape == BlockStairs.EnumShape.INNER_LEFT || blockstairs_enumstairshape == BlockStairs.EnumShape.INNER_RIGHT) {
            arraylist.add(getCollQuarterBlock(iblockdata));
        }

        if (blockstairs_enumstairshape != BlockStairs.EnumShape.STRAIGHT) {
            arraylist.add(getCollEighthBlock(iblockdata));
        }

        return arraylist;
    }

    private static AxisAlignedBB getCollQuarterBlock(IBlockState iblockdata) {
        boolean flag = iblockdata.getValue(BlockStairs.HALF) == BlockStairs.EnumHalf.TOP;

        switch ((EnumFacing) iblockdata.getValue(BlockStairs.FACING)) {
        case NORTH:
        default:
            return flag ? BlockStairs.AABB_QTR_BOT_NORTH : BlockStairs.AABB_QTR_TOP_NORTH;

        case SOUTH:
            return flag ? BlockStairs.AABB_QTR_BOT_SOUTH : BlockStairs.AABB_QTR_TOP_SOUTH;

        case WEST:
            return flag ? BlockStairs.AABB_QTR_BOT_WEST : BlockStairs.AABB_QTR_TOP_WEST;

        case EAST:
            return flag ? BlockStairs.AABB_QTR_BOT_EAST : BlockStairs.AABB_QTR_TOP_EAST;
        }
    }

    private static AxisAlignedBB getCollEighthBlock(IBlockState iblockdata) {
        EnumFacing enumdirection = (EnumFacing) iblockdata.getValue(BlockStairs.FACING);
        EnumFacing enumdirection1;

        switch ((BlockStairs.EnumShape) iblockdata.getValue(BlockStairs.SHAPE)) {
        case OUTER_LEFT:
        default:
            enumdirection1 = enumdirection;
            break;

        case OUTER_RIGHT:
            enumdirection1 = enumdirection.rotateY();
            break;

        case INNER_RIGHT:
            enumdirection1 = enumdirection.getOpposite();
            break;

        case INNER_LEFT:
            enumdirection1 = enumdirection.rotateYCCW();
        }

        boolean flag = iblockdata.getValue(BlockStairs.HALF) == BlockStairs.EnumHalf.TOP;

        switch (enumdirection1) {
        case NORTH:
        default:
            return flag ? BlockStairs.AABB_OCT_BOT_NW : BlockStairs.AABB_OCT_TOP_NW;

        case SOUTH:
            return flag ? BlockStairs.AABB_OCT_BOT_SE : BlockStairs.AABB_OCT_TOP_SE;

        case WEST:
            return flag ? BlockStairs.AABB_OCT_BOT_SW : BlockStairs.AABB_OCT_TOP_SW;

        case EAST:
            return flag ? BlockStairs.AABB_OCT_BOT_NE : BlockStairs.AABB_OCT_TOP_NE;
        }
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        iblockdata = this.getActualState(iblockdata, iblockaccess, blockposition);
        if (enumdirection.getAxis() == EnumFacing.Axis.Y) {
            return enumdirection == EnumFacing.UP == (iblockdata.getValue(BlockStairs.HALF) == BlockStairs.EnumHalf.TOP) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
        } else {
            BlockStairs.EnumShape blockstairs_enumstairshape = (BlockStairs.EnumShape) iblockdata.getValue(BlockStairs.SHAPE);

            if (blockstairs_enumstairshape != BlockStairs.EnumShape.OUTER_LEFT && blockstairs_enumstairshape != BlockStairs.EnumShape.OUTER_RIGHT) {
                EnumFacing enumdirection1 = (EnumFacing) iblockdata.getValue(BlockStairs.FACING);

                switch (blockstairs_enumstairshape) {
                case INNER_RIGHT:
                    return enumdirection1 != enumdirection && enumdirection1 != enumdirection.rotateYCCW() ? BlockFaceShape.UNDEFINED : BlockFaceShape.SOLID;

                case INNER_LEFT:
                    return enumdirection1 != enumdirection && enumdirection1 != enumdirection.rotateY() ? BlockFaceShape.UNDEFINED : BlockFaceShape.SOLID;

                case STRAIGHT:
                    return enumdirection1 == enumdirection ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;

                default:
                    return BlockFaceShape.UNDEFINED;
                }
            } else {
                return BlockFaceShape.UNDEFINED;
            }
        }
    }

    public boolean isOpaqueCube(IBlockState iblockdata) {
        return false;
    }

    public boolean isFullCube(IBlockState iblockdata) {
        return false;
    }

    public void onBlockClicked(World world, BlockPos blockposition, EntityPlayer entityhuman) {
        this.modelBlock.onBlockClicked(world, blockposition, entityhuman);
    }

    public void onBlockDestroyedByPlayer(World world, BlockPos blockposition, IBlockState iblockdata) {
        this.modelBlock.onBlockDestroyedByPlayer(world, blockposition, iblockdata);
    }

    public float getExplosionResistance(Entity entity) {
        return this.modelBlock.getExplosionResistance(entity);
    }

    public int tickRate(World world) {
        return this.modelBlock.tickRate(world);
    }

    public Vec3d modifyAcceleration(World world, BlockPos blockposition, Entity entity, Vec3d vec3d) {
        return this.modelBlock.modifyAcceleration(world, blockposition, entity, vec3d);
    }

    public boolean isCollidable() {
        return this.modelBlock.isCollidable();
    }

    public boolean canCollideCheck(IBlockState iblockdata, boolean flag) {
        return this.modelBlock.canCollideCheck(iblockdata, flag);
    }

    public boolean canPlaceBlockAt(World world, BlockPos blockposition) {
        return this.modelBlock.canPlaceBlockAt(world, blockposition);
    }

    public void onBlockAdded(World world, BlockPos blockposition, IBlockState iblockdata) {
        this.modelState.neighborChanged(world, blockposition, Blocks.AIR, blockposition);
        this.modelBlock.onBlockAdded(world, blockposition, this.modelState);
    }

    public void breakBlock(World world, BlockPos blockposition, IBlockState iblockdata) {
        this.modelBlock.breakBlock(world, blockposition, this.modelState);
    }

    public void onEntityWalk(World world, BlockPos blockposition, Entity entity) {
        this.modelBlock.onEntityWalk(world, blockposition, entity);
    }

    public void updateTick(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        this.modelBlock.updateTick(world, blockposition, iblockdata, random);
    }

    public boolean onBlockActivated(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        return this.modelBlock.onBlockActivated(world, blockposition, this.modelState, entityhuman, enumhand, EnumFacing.DOWN, 0.0F, 0.0F, 0.0F);
    }

    public void onBlockDestroyedByExplosion(World world, BlockPos blockposition, Explosion explosion) {
        this.modelBlock.onBlockDestroyedByExplosion(world, blockposition, explosion);
    }

    public boolean isTopSolid(IBlockState iblockdata) {
        return iblockdata.getValue(BlockStairs.HALF) == BlockStairs.EnumHalf.TOP;
    }

    public MapColor getMapColor(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return this.modelBlock.getMapColor(this.modelState, iblockaccess, blockposition);
    }

    public IBlockState getStateForPlacement(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        IBlockState iblockdata = super.getStateForPlacement(world, blockposition, enumdirection, f, f1, f2, i, entityliving);

        iblockdata = iblockdata.withProperty(BlockStairs.FACING, entityliving.getHorizontalFacing()).withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.STRAIGHT);
        return enumdirection != EnumFacing.DOWN && (enumdirection == EnumFacing.UP || (double) f1 <= 0.5D) ? iblockdata.withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM) : iblockdata.withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP);
    }

    @Nullable
    public RayTraceResult collisionRayTrace(IBlockState iblockdata, World world, BlockPos blockposition, Vec3d vec3d, Vec3d vec3d1) {
        ArrayList arraylist = Lists.newArrayList();
        Iterator iterator = getCollisionBoxList(this.getActualState(iblockdata, world, blockposition)).iterator();

        while (iterator.hasNext()) {
            AxisAlignedBB axisalignedbb = (AxisAlignedBB) iterator.next();

            arraylist.add(this.rayTrace(blockposition, vec3d, vec3d1, axisalignedbb));
        }

        RayTraceResult movingobjectposition = null;
        double d0 = 0.0D;
        Iterator iterator1 = arraylist.iterator();

        while (iterator1.hasNext()) {
            RayTraceResult movingobjectposition1 = (RayTraceResult) iterator1.next();

            if (movingobjectposition1 != null) {
                double d1 = movingobjectposition1.hitVec.squareDistanceTo(vec3d1);

                if (d1 > d0) {
                    movingobjectposition = movingobjectposition1;
                    d0 = d1;
                }
            }
        }

        return movingobjectposition;
    }

    public IBlockState getStateFromMeta(int i) {
        IBlockState iblockdata = this.getDefaultState().withProperty(BlockStairs.HALF, (i & 4) > 0 ? BlockStairs.EnumHalf.TOP : BlockStairs.EnumHalf.BOTTOM);

        iblockdata = iblockdata.withProperty(BlockStairs.FACING, EnumFacing.getFront(5 - (i & 3)));
        return iblockdata;
    }

    public int getMetaFromState(IBlockState iblockdata) {
        int i = 0;

        if (iblockdata.getValue(BlockStairs.HALF) == BlockStairs.EnumHalf.TOP) {
            i |= 4;
        }

        i |= 5 - ((EnumFacing) iblockdata.getValue(BlockStairs.FACING)).getIndex();
        return i;
    }

    public IBlockState getActualState(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return iblockdata.withProperty(BlockStairs.SHAPE, getStairsShape(iblockdata, iblockaccess, blockposition));
    }

    private static BlockStairs.EnumShape getStairsShape(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        EnumFacing enumdirection = (EnumFacing) iblockdata.getValue(BlockStairs.FACING);
        IBlockState iblockdata1 = iblockaccess.getBlockState(blockposition.offset(enumdirection));

        if (isBlockStairs(iblockdata1) && iblockdata.getValue(BlockStairs.HALF) == iblockdata1.getValue(BlockStairs.HALF)) {
            EnumFacing enumdirection1 = (EnumFacing) iblockdata1.getValue(BlockStairs.FACING);

            if (enumdirection1.getAxis() != ((EnumFacing) iblockdata.getValue(BlockStairs.FACING)).getAxis() && isDifferentStairs(iblockdata, iblockaccess, blockposition, enumdirection1.getOpposite())) {
                if (enumdirection1 == enumdirection.rotateYCCW()) {
                    return BlockStairs.EnumShape.OUTER_LEFT;
                }

                return BlockStairs.EnumShape.OUTER_RIGHT;
            }
        }

        IBlockState iblockdata2 = iblockaccess.getBlockState(blockposition.offset(enumdirection.getOpposite()));

        if (isBlockStairs(iblockdata2) && iblockdata.getValue(BlockStairs.HALF) == iblockdata2.getValue(BlockStairs.HALF)) {
            EnumFacing enumdirection2 = (EnumFacing) iblockdata2.getValue(BlockStairs.FACING);

            if (enumdirection2.getAxis() != ((EnumFacing) iblockdata.getValue(BlockStairs.FACING)).getAxis() && isDifferentStairs(iblockdata, iblockaccess, blockposition, enumdirection2)) {
                if (enumdirection2 == enumdirection.rotateYCCW()) {
                    return BlockStairs.EnumShape.INNER_LEFT;
                }

                return BlockStairs.EnumShape.INNER_RIGHT;
            }
        }

        return BlockStairs.EnumShape.STRAIGHT;
    }

    private static boolean isDifferentStairs(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        IBlockState iblockdata1 = iblockaccess.getBlockState(blockposition.offset(enumdirection));

        return !isBlockStairs(iblockdata1) || iblockdata1.getValue(BlockStairs.FACING) != iblockdata.getValue(BlockStairs.FACING) || iblockdata1.getValue(BlockStairs.HALF) != iblockdata.getValue(BlockStairs.HALF);
    }

    public static boolean isBlockStairs(IBlockState iblockdata) {
        return iblockdata.getBlock() instanceof BlockStairs;
    }

    public IBlockState withRotation(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.withProperty(BlockStairs.FACING, enumblockrotation.rotate((EnumFacing) iblockdata.getValue(BlockStairs.FACING)));
    }

    public IBlockState withMirror(IBlockState iblockdata, Mirror enumblockmirror) {
        EnumFacing enumdirection = (EnumFacing) iblockdata.getValue(BlockStairs.FACING);
        BlockStairs.EnumShape blockstairs_enumstairshape = (BlockStairs.EnumShape) iblockdata.getValue(BlockStairs.SHAPE);

        switch (enumblockmirror) {
        case LEFT_RIGHT:
            if (enumdirection.getAxis() == EnumFacing.Axis.Z) {
                switch (blockstairs_enumstairshape) {
                case OUTER_LEFT:
                    return iblockdata.withRotation(Rotation.CLOCKWISE_180).withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.OUTER_RIGHT);

                case OUTER_RIGHT:
                    return iblockdata.withRotation(Rotation.CLOCKWISE_180).withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.OUTER_LEFT);

                case INNER_RIGHT:
                    return iblockdata.withRotation(Rotation.CLOCKWISE_180).withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.INNER_LEFT);

                case INNER_LEFT:
                    return iblockdata.withRotation(Rotation.CLOCKWISE_180).withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.INNER_RIGHT);

                default:
                    return iblockdata.withRotation(Rotation.CLOCKWISE_180);
                }
            }
            break;

        case FRONT_BACK:
            if (enumdirection.getAxis() == EnumFacing.Axis.X) {
                switch (blockstairs_enumstairshape) {
                case OUTER_LEFT:
                    return iblockdata.withRotation(Rotation.CLOCKWISE_180).withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.OUTER_RIGHT);

                case OUTER_RIGHT:
                    return iblockdata.withRotation(Rotation.CLOCKWISE_180).withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.OUTER_LEFT);

                case INNER_RIGHT:
                    return iblockdata.withRotation(Rotation.CLOCKWISE_180).withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.INNER_RIGHT);

                case INNER_LEFT:
                    return iblockdata.withRotation(Rotation.CLOCKWISE_180).withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.INNER_LEFT);

                case STRAIGHT:
                    return iblockdata.withRotation(Rotation.CLOCKWISE_180);
                }
            }
        }

        return super.withMirror(iblockdata, enumblockmirror);
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockStairs.FACING, BlockStairs.HALF, BlockStairs.SHAPE});
    }

    public static enum EnumShape implements IStringSerializable {

        STRAIGHT("straight"), INNER_LEFT("inner_left"), INNER_RIGHT("inner_right"), OUTER_LEFT("outer_left"), OUTER_RIGHT("outer_right");

        private final String name;

        private EnumShape(String s) {
            this.name = s;
        }

        public String toString() {
            return this.name;
        }

        public String getName() {
            return this.name;
        }
    }

    public static enum EnumHalf implements IStringSerializable {

        TOP("top"), BOTTOM("bottom");

        private final String name;

        private EnumHalf(String s) {
            this.name = s;
        }

        public String toString() {
            return this.name;
        }

        public String getName() {
            return this.name;
        }
    }
}
