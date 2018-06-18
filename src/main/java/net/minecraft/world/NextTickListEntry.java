package net.minecraft.world;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;


public class NextTickListEntry implements Comparable<NextTickListEntry> {

    private static long nextTickEntryID;
    private final Block block;
    public final BlockPos position;
    public long scheduledTime;
    public int priority;
    private final long tickEntryID;

    public NextTickListEntry(BlockPos blockposition, Block block) {
        this.tickEntryID = (long) (NextTickListEntry.nextTickEntryID++);
        this.position = blockposition.toImmutable();
        this.block = block;
    }

    public boolean equals(Object object) {
        if (!(object instanceof NextTickListEntry)) {
            return false;
        } else {
            NextTickListEntry nextticklistentry = (NextTickListEntry) object;

            return this.position.equals(nextticklistentry.position) && Block.isEqualTo(this.block, nextticklistentry.block);
        }
    }

    public int hashCode() {
        return this.position.hashCode();
    }

    public NextTickListEntry setScheduledTime(long i) {
        this.scheduledTime = i;
        return this;
    }

    public void setPriority(int i) {
        this.priority = i;
    }

    public int compareTo(NextTickListEntry nextticklistentry) {
        return this.scheduledTime < nextticklistentry.scheduledTime ? -1 : (this.scheduledTime > nextticklistentry.scheduledTime ? 1 : (this.priority != nextticklistentry.priority ? this.priority - nextticklistentry.priority : (this.tickEntryID < nextticklistentry.tickEntryID ? -1 : (this.tickEntryID > nextticklistentry.tickEntryID ? 1 : 0))));
    }

    public String toString() {
        return Block.getIdFromBlock(this.block) + ": " + this.position + ", " + this.scheduledTime + ", " + this.priority + ", " + this.tickEntryID;
    }

    public Block getBlock() {
        return this.block;
    }

    public int compareTo(Object object) {
        return this.compareTo((NextTickListEntry) object);
    }
}
