package net.minecraft.block;
import net.minecraft.util.math.BlockPos;


public class BlockEventData {

    private final BlockPos position;
    private final Block blockType;
    private final int eventID;
    private final int eventParameter;

    public BlockEventData(BlockPos blockposition, Block block, int i, int j) {
        this.position = blockposition;
        this.eventID = i;
        this.eventParameter = j;
        this.blockType = block;
    }

    public BlockPos getPosition() {
        return this.position;
    }

    public int getEventID() {
        return this.eventID;
    }

    public int getEventParameter() {
        return this.eventParameter;
    }

    public Block getBlock() {
        return this.blockType;
    }

    public boolean equals(Object object) {
        if (!(object instanceof BlockEventData)) {
            return false;
        } else {
            BlockEventData blockactiondata = (BlockEventData) object;

            return this.position.equals(blockactiondata.position) && this.eventID == blockactiondata.eventID && this.eventParameter == blockactiondata.eventParameter && this.blockType == blockactiondata.blockType;
        }
    }

    public String toString() {
        return "TE(" + this.position + ")," + this.eventID + "," + this.eventParameter + "," + this.blockType;
    }
}
