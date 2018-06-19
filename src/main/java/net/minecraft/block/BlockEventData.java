package net.minecraft.block;
import net.minecraft.util.math.BlockPos;


public class BlockEventData {

    private final BlockPos field_180329_a;
    private final Block field_151344_d;
    private final int field_151345_e;
    private final int field_151343_f;

    public BlockEventData(BlockPos blockposition, Block block, int i, int j) {
        this.field_180329_a = blockposition;
        this.field_151345_e = i;
        this.field_151343_f = j;
        this.field_151344_d = block;
    }

    public BlockPos func_180328_a() {
        return this.field_180329_a;
    }

    public int func_151339_d() {
        return this.field_151345_e;
    }

    public int func_151338_e() {
        return this.field_151343_f;
    }

    public Block func_151337_f() {
        return this.field_151344_d;
    }

    public boolean equals(Object object) {
        if (!(object instanceof BlockEventData)) {
            return false;
        } else {
            BlockEventData blockactiondata = (BlockEventData) object;

            return this.field_180329_a.equals(blockactiondata.field_180329_a) && this.field_151345_e == blockactiondata.field_151345_e && this.field_151343_f == blockactiondata.field_151343_f && this.field_151344_d == blockactiondata.field_151344_d;
        }
    }

    public String toString() {
        return "TE(" + this.field_180329_a + ")," + this.field_151345_e + "," + this.field_151343_f + "," + this.field_151344_d;
    }
}
