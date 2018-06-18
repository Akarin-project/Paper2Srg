package net.minecraft.tileentity;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class TileEntityPiston extends TileEntity implements ITickable {

    private IBlockState pistonState;
    private EnumFacing pistonFacing;
    private boolean extending;
    private boolean shouldHeadBeRendered;
    private static final ThreadLocal<EnumFacing> MOVING_ENTITY = new ThreadLocal() {
        protected EnumFacing a() {
            return null;
        }

        protected Object initialValue() {
            return this.a();
        }
    };
    private float progress;
    private float lastProgress;

    public TileEntityPiston() {}

    public TileEntityPiston(IBlockState iblockdata, EnumFacing enumdirection, boolean flag, boolean flag1) {
        this.pistonState = iblockdata;
        this.pistonFacing = enumdirection;
        this.extending = flag;
        this.shouldHeadBeRendered = flag1;
    }

    public IBlockState getPistonState() {
        return this.pistonState;
    }

    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    public int getBlockMetadata() {
        return 0;
    }

    public boolean isExtending() {
        return this.extending;
    }

    public EnumFacing getFacing() {
        return this.pistonFacing;
    }

    public boolean shouldPistonHeadBeRendered() {
        return this.shouldHeadBeRendered;
    }

    private float getExtendedProgress(float f) {
        return this.extending ? f - 1.0F : 1.0F - f;
    }

    public AxisAlignedBB getAABB(IBlockAccess iblockaccess, BlockPos blockposition) {
        return this.getAABB(iblockaccess, blockposition, this.progress).union(this.getAABB(iblockaccess, blockposition, this.lastProgress));
    }

    public AxisAlignedBB getAABB(IBlockAccess iblockaccess, BlockPos blockposition, float f) {
        f = this.getExtendedProgress(f);
        IBlockState iblockdata = this.getCollisionRelatedBlockState();

        return iblockdata.getBoundingBox(iblockaccess, blockposition).offset((double) (f * (float) this.pistonFacing.getFrontOffsetX()), (double) (f * (float) this.pistonFacing.getFrontOffsetY()), (double) (f * (float) this.pistonFacing.getFrontOffsetZ()));
    }

    private IBlockState getCollisionRelatedBlockState() {
        return !this.isExtending() && this.shouldPistonHeadBeRendered() ? Blocks.PISTON_HEAD.getDefaultState().withProperty(BlockPistonExtension.TYPE, this.pistonState.getBlock() == Blocks.STICKY_PISTON ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT).withProperty(BlockPistonExtension.FACING, this.pistonState.getValue(BlockPistonBase.FACING)) : this.pistonState;
    }

    private void moveCollidedEntities(float f) {
        EnumFacing enumdirection = this.extending ? this.pistonFacing : this.pistonFacing.getOpposite();
        double d0 = (double) (f - this.progress);
        ArrayList arraylist = Lists.newArrayList();

        this.getCollisionRelatedBlockState().addCollisionBoxToList(this.world, BlockPos.ORIGIN, new AxisAlignedBB(BlockPos.ORIGIN), arraylist, (Entity) null, true);
        if (!arraylist.isEmpty()) {
            AxisAlignedBB axisalignedbb = this.moveByPositionAndProgress(this.getMinMaxPiecesAABB((List) arraylist));
            List list = this.world.getEntitiesWithinAABBExcludingEntity((Entity) null, this.getMovementArea(axisalignedbb, enumdirection, d0).union(axisalignedbb));

            if (!list.isEmpty()) {
                boolean flag = this.pistonState.getBlock() == Blocks.SLIME_BLOCK;

                for (int i = 0; i < list.size(); ++i) {
                    Entity entity = (Entity) list.get(i);

                    if (entity.getPushReaction() != EnumPushReaction.IGNORE) {
                        if (flag) {
                            switch (enumdirection.getAxis()) {
                            case X:
                                entity.motionX = (double) enumdirection.getFrontOffsetX();
                                break;

                            case Y:
                                entity.motionY = (double) enumdirection.getFrontOffsetY();
                                break;

                            case Z:
                                entity.motionZ = (double) enumdirection.getFrontOffsetZ();
                            }
                        }

                        double d1 = 0.0D;

                        for (int j = 0; j < arraylist.size(); ++j) {
                            AxisAlignedBB axisalignedbb1 = this.getMovementArea(this.moveByPositionAndProgress((AxisAlignedBB) arraylist.get(j)), enumdirection, d0);
                            AxisAlignedBB axisalignedbb2 = entity.getEntityBoundingBox();

                            if (axisalignedbb1.intersects(axisalignedbb2)) {
                                d1 = Math.max(d1, this.getMovement(axisalignedbb1, enumdirection, axisalignedbb2));
                                if (d1 >= d0) {
                                    break;
                                }
                            }
                        }

                        if (d1 > 0.0D) {
                            d1 = Math.min(d1, d0) + 0.01D;
                            TileEntityPiston.MOVING_ENTITY.set(enumdirection);
                            entity.move(MoverType.PISTON, d1 * (double) enumdirection.getFrontOffsetX(), d1 * (double) enumdirection.getFrontOffsetY(), d1 * (double) enumdirection.getFrontOffsetZ());
                            TileEntityPiston.MOVING_ENTITY.set((Object) null);
                            if (!this.extending && this.shouldHeadBeRendered) {
                                this.fixEntityWithinPistonBase(entity, enumdirection, d0);
                            }
                        }
                    }
                }

            }
        }
    }

    private AxisAlignedBB getMinMaxPiecesAABB(List<AxisAlignedBB> list) {
        double d0 = 0.0D;
        double d1 = 0.0D;
        double d2 = 0.0D;
        double d3 = 1.0D;
        double d4 = 1.0D;
        double d5 = 1.0D;

        AxisAlignedBB axisalignedbb;

        for (Iterator iterator = list.iterator(); iterator.hasNext(); d5 = Math.max(axisalignedbb.maxZ, d5)) {
            axisalignedbb = (AxisAlignedBB) iterator.next();
            d0 = Math.min(axisalignedbb.minX, d0);
            d1 = Math.min(axisalignedbb.minY, d1);
            d2 = Math.min(axisalignedbb.minZ, d2);
            d3 = Math.max(axisalignedbb.maxX, d3);
            d4 = Math.max(axisalignedbb.maxY, d4);
        }

        return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
    }

    private double getMovement(AxisAlignedBB axisalignedbb, EnumFacing enumdirection, AxisAlignedBB axisalignedbb1) {
        switch (enumdirection.getAxis()) {
        case X:
            return getDeltaX(axisalignedbb, enumdirection, axisalignedbb1);

        case Y:
        default:
            return getDeltaY(axisalignedbb, enumdirection, axisalignedbb1);

        case Z:
            return getDeltaZ(axisalignedbb, enumdirection, axisalignedbb1);
        }
    }

    private AxisAlignedBB moveByPositionAndProgress(AxisAlignedBB axisalignedbb) {
        double d0 = (double) this.getExtendedProgress(this.progress);

        return axisalignedbb.offset((double) this.pos.getX() + d0 * (double) this.pistonFacing.getFrontOffsetX(), (double) this.pos.getY() + d0 * (double) this.pistonFacing.getFrontOffsetY(), (double) this.pos.getZ() + d0 * (double) this.pistonFacing.getFrontOffsetZ());
    }

    private AxisAlignedBB getMovementArea(AxisAlignedBB axisalignedbb, EnumFacing enumdirection, double d0) {
        double d1 = d0 * (double) enumdirection.getAxisDirection().getOffset();
        double d2 = Math.min(d1, 0.0D);
        double d3 = Math.max(d1, 0.0D);

        switch (enumdirection) {
        case WEST:
            return new AxisAlignedBB(axisalignedbb.minX + d2, axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.minX + d3, axisalignedbb.maxY, axisalignedbb.maxZ);

        case EAST:
            return new AxisAlignedBB(axisalignedbb.maxX + d2, axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.maxX + d3, axisalignedbb.maxY, axisalignedbb.maxZ);

        case DOWN:
            return new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY + d2, axisalignedbb.minZ, axisalignedbb.maxX, axisalignedbb.minY + d3, axisalignedbb.maxZ);

        case UP:
        default:
            return new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.maxY + d2, axisalignedbb.minZ, axisalignedbb.maxX, axisalignedbb.maxY + d3, axisalignedbb.maxZ);

        case NORTH:
            return new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ + d2, axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ + d3);

        case SOUTH:
            return new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ + d2, axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ + d3);
        }
    }

    private void fixEntityWithinPistonBase(Entity entity, EnumFacing enumdirection, double d0) {
        AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox();
        AxisAlignedBB axisalignedbb1 = Block.FULL_BLOCK_AABB.offset(this.pos);

        if (axisalignedbb.intersects(axisalignedbb1)) {
            EnumFacing enumdirection1 = enumdirection.getOpposite();
            double d1 = this.getMovement(axisalignedbb1, enumdirection1, axisalignedbb) + 0.01D;
            double d2 = this.getMovement(axisalignedbb1, enumdirection1, axisalignedbb.intersect(axisalignedbb1)) + 0.01D;

            if (Math.abs(d1 - d2) < 0.01D) {
                d1 = Math.min(d1, d0) + 0.01D;
                TileEntityPiston.MOVING_ENTITY.set(enumdirection);
                entity.move(MoverType.PISTON, d1 * (double) enumdirection1.getFrontOffsetX(), d1 * (double) enumdirection1.getFrontOffsetY(), d1 * (double) enumdirection1.getFrontOffsetZ());
                TileEntityPiston.MOVING_ENTITY.set((Object) null);
            }
        }

    }

    private static double getDeltaX(AxisAlignedBB axisalignedbb, EnumFacing enumdirection, AxisAlignedBB axisalignedbb1) {
        return enumdirection.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE ? axisalignedbb.maxX - axisalignedbb1.minX : axisalignedbb1.maxX - axisalignedbb.minX;
    }

    private static double getDeltaY(AxisAlignedBB axisalignedbb, EnumFacing enumdirection, AxisAlignedBB axisalignedbb1) {
        return enumdirection.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE ? axisalignedbb.maxY - axisalignedbb1.minY : axisalignedbb1.maxY - axisalignedbb.minY;
    }

    private static double getDeltaZ(AxisAlignedBB axisalignedbb, EnumFacing enumdirection, AxisAlignedBB axisalignedbb1) {
        return enumdirection.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE ? axisalignedbb.maxZ - axisalignedbb1.minZ : axisalignedbb1.maxZ - axisalignedbb.minZ;
    }

    public void clearPistonTileEntity() {
        if (this.lastProgress < 1.0F && this.world != null) {
            this.progress = 1.0F;
            this.lastProgress = this.progress;
            this.world.removeTileEntity(this.pos);
            this.invalidate();
            if (this.world.getBlockState(this.pos).getBlock() == Blocks.PISTON_EXTENSION) {
                this.world.setBlockState(this.pos, this.pistonState, 3);
                this.world.neighborChanged(this.pos, this.pistonState.getBlock(), this.pos);
            }
        }

    }

    public void update() {
        this.lastProgress = this.progress;
        if (this.lastProgress >= 1.0F) {
            this.world.removeTileEntity(this.pos);
            this.invalidate();
            if (this.world.getBlockState(this.pos).getBlock() == Blocks.PISTON_EXTENSION) {
                this.world.setBlockState(this.pos, this.pistonState, 3);
                this.world.neighborChanged(this.pos, this.pistonState.getBlock(), this.pos);
            }

        } else {
            float f = this.progress + 0.5F;

            this.moveCollidedEntities(f);
            this.progress = f;
            if (this.progress >= 1.0F) {
                this.progress = 1.0F;
            }

        }
    }

    public static void registerFixesPiston(DataFixer dataconvertermanager) {}

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.pistonState = Block.getBlockById(nbttagcompound.getInteger("blockId")).getStateFromMeta(nbttagcompound.getInteger("blockData"));
        this.pistonFacing = EnumFacing.getFront(nbttagcompound.getInteger("facing"));
        this.progress = nbttagcompound.getFloat("progress");
        this.lastProgress = this.progress;
        this.extending = nbttagcompound.getBoolean("extending");
        this.shouldHeadBeRendered = nbttagcompound.getBoolean("source");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("blockId", Block.getIdFromBlock(this.pistonState.getBlock()));
        nbttagcompound.setInteger("blockData", this.pistonState.getBlock().getMetaFromState(this.pistonState));
        nbttagcompound.setInteger("facing", this.pistonFacing.getIndex());
        nbttagcompound.setFloat("progress", this.lastProgress);
        nbttagcompound.setBoolean("extending", this.extending);
        nbttagcompound.setBoolean("source", this.shouldHeadBeRendered);
        return nbttagcompound;
    }

    public void addCollissionAABBs(World world, BlockPos blockposition, AxisAlignedBB axisalignedbb, List<AxisAlignedBB> list, @Nullable Entity entity) {
        if (!this.extending && this.shouldHeadBeRendered) {
            this.pistonState.withProperty(BlockPistonBase.EXTENDED, Boolean.valueOf(true)).addCollisionBoxToList(world, blockposition, axisalignedbb, list, entity, false);
        }

        EnumFacing enumdirection = (EnumFacing) TileEntityPiston.MOVING_ENTITY.get();

        if ((double) this.progress >= 1.0D || enumdirection != (this.extending ? this.pistonFacing : this.pistonFacing.getOpposite())) {
            int i = list.size();
            IBlockState iblockdata;

            if (this.shouldPistonHeadBeRendered()) {
                iblockdata = Blocks.PISTON_HEAD.getDefaultState().withProperty(BlockPistonExtension.FACING, this.pistonFacing).withProperty(BlockPistonExtension.SHORT, Boolean.valueOf(this.extending != 1.0F - this.progress < 0.25F));
            } else {
                iblockdata = this.pistonState;
            }

            float f = this.getExtendedProgress(this.progress);
            double d0 = (double) ((float) this.pistonFacing.getFrontOffsetX() * f);
            double d1 = (double) ((float) this.pistonFacing.getFrontOffsetY() * f);
            double d2 = (double) ((float) this.pistonFacing.getFrontOffsetZ() * f);

            iblockdata.addCollisionBoxToList(world, blockposition, axisalignedbb.offset(-d0, -d1, -d2), list, entity, true);

            for (int j = i; j < list.size(); ++j) {
                list.set(j, ((AxisAlignedBB) list.get(j)).offset(d0, d1, d2));
            }

        }
    }
}
