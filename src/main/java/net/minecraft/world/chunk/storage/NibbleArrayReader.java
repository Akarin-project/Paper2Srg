package net.minecraft.world.chunk.storage;

public class NibbleArrayReader {

    public final byte[] field_76689_a;
    private final int field_76687_b;
    private final int field_76688_c;

    public NibbleArrayReader(byte[] abyte, int i) {
        this.field_76689_a = abyte;
        this.field_76687_b = i;
        this.field_76688_c = i + 4;
    }

    public int func_76686_a(int i, int j, int k) {
        int l = i << this.field_76688_c | k << this.field_76687_b | j;
        int i1 = l >> 1;
        int j1 = l & 1;

        return j1 == 0 ? this.field_76689_a[i1] & 15 : this.field_76689_a[i1] >> 4 & 15;
    }
}
