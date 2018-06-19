package net.minecraft.world.chunk;

public class NibbleArray {

    private final byte[] field_76585_a;

    public NibbleArray() {
        this.field_76585_a = new byte[2048];
    }

    public NibbleArray(byte[] abyte) {
        this.field_76585_a = abyte;
        if (abyte.length != 2048) {
            throw new IllegalArgumentException("ChunkNibbleArrays should be 2048 bytes not: " + abyte.length);
        }
    }

    public int func_76582_a(int i, int j, int k) {
        return this.func_177480_a(this.func_177483_b(i, j, k));
    }

    public void func_76581_a(int i, int j, int k, int l) {
        this.func_177482_a(this.func_177483_b(i, j, k), l);
    }

    private int func_177483_b(int i, int j, int k) {
        return j << 8 | k << 4 | i;
    }

    public int func_177480_a(int i) {
        int j = this.func_177478_c(i);

        return this.field_76585_a[j] >> ((i & 1) << 2) & 15; // Spigot
    }

    public void func_177482_a(int i, int j) {
        int k = this.func_177478_c(i);

        // Spigot start
        int shift = (i & 1) << 2;
        this.field_76585_a[k] = (byte) (this.field_76585_a[k] & ~(15 << shift) | (j & 15) << shift);
        // Spigot end
    }

    private boolean func_177479_b(int i) {
        return (i & 1) == 0;
    }

    private int func_177478_c(int i) {
        return i >> 1;
    }

    public byte[] func_177481_a() {
        return this.field_76585_a;
    }
}
