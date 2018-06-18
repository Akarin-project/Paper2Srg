package net.minecraft.village;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;


public class VillageDoorInfo {

    private final BlockPos doorBlockPos;
    private final BlockPos insideBlock;
    private final EnumFacing insideDirection;
    private int lastActivityTimestamp;
    private boolean isDetachedFromVillageFlag;
    private int doorOpeningRestrictionCounter;

    public VillageDoorInfo(BlockPos blockposition, int i, int j, int k) {
        this(blockposition, getFaceDirection(i, j), k);
    }

    private static EnumFacing getFaceDirection(int i, int j) {
        return i < 0 ? EnumFacing.WEST : (i > 0 ? EnumFacing.EAST : (j < 0 ? EnumFacing.NORTH : EnumFacing.SOUTH));
    }

    public VillageDoorInfo(BlockPos blockposition, EnumFacing enumdirection, int i) {
        this.doorBlockPos = blockposition;
        this.insideDirection = enumdirection;
        this.insideBlock = blockposition.offset(enumdirection, 2);
        this.lastActivityTimestamp = i;
    }

    public int getDistanceSquared(int i, int j, int k) {
        return (int) this.doorBlockPos.distanceSq((double) i, (double) j, (double) k);
    }

    public int getDistanceToDoorBlockSq(BlockPos blockposition) {
        return (int) blockposition.distanceSq(this.getDoorBlockPos());
    }

    public int getDistanceToInsideBlockSq(BlockPos blockposition) {
        return (int) this.insideBlock.distanceSq(blockposition);
    }

    public boolean isInsideSide(BlockPos blockposition) {
        int i = blockposition.getX() - this.doorBlockPos.getX();
        int j = blockposition.getZ() - this.doorBlockPos.getY();

        return i * this.insideDirection.getFrontOffsetX() + j * this.insideDirection.getFrontOffsetZ() >= 0;
    }

    public void resetDoorOpeningRestrictionCounter() {
        this.doorOpeningRestrictionCounter = 0;
    }

    public void incrementDoorOpeningRestrictionCounter() {
        ++this.doorOpeningRestrictionCounter;
    }

    public int getDoorOpeningRestrictionCounter() {
        return this.doorOpeningRestrictionCounter;
    }

    public BlockPos getDoorBlockPos() {
        return this.doorBlockPos;
    }

    public BlockPos getInsideBlockPos() {
        return this.insideBlock;
    }

    public int getInsideOffsetX() {
        return this.insideDirection.getFrontOffsetX() * 2;
    }

    public int getInsideOffsetZ() {
        return this.insideDirection.getFrontOffsetZ() * 2;
    }

    public int getLastActivityTimestamp() {
        return this.lastActivityTimestamp;
    }

    public void setLastActivityTimestamp(int i) {
        this.lastActivityTimestamp = i;
    }

    public boolean getIsDetachedFromVillageFlag() {
        return this.isDetachedFromVillageFlag;
    }

    public void setIsDetachedFromVillageFlag(boolean flag) {
        this.isDetachedFromVillageFlag = flag;
    }

    public EnumFacing getInsideDirection() {
        return this.insideDirection;
    }
}
