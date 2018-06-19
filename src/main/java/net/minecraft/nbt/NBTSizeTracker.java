package net.minecraft.nbt;

public class NBTSizeTracker {

    public static final NBTSizeTracker field_152451_a = new NBTSizeTracker(0L) {
        public void func_152450_a(long i) {}
    };
    private final long field_152452_b;
    private long field_152453_c;

    public NBTSizeTracker(long i) {
        this.field_152452_b = i;
    }

    public void func_152450_a(long i) {
        this.field_152453_c += i / 8L;
        if (this.field_152453_c > this.field_152452_b) {
            throw new RuntimeException("Tried to read NBT tag that was too big; tried to allocate: " + this.field_152453_c + "bytes where max allowed: " + this.field_152452_b);
        }
    }
}
