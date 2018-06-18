package net.minecraft.world.chunk;

public class NibbleArray {

    private final byte[] data;

    public NibbleArray() {
        this.data = new byte[2048];
    }

    public NibbleArray(byte[] abyte) {
        this.data = abyte;
        if (abyte.length != 2048) {
            throw new IllegalArgumentException("ChunkNibbleArrays should be 2048 bytes not: " + abyte.length);
        }
    }

    public int get(int i, int j, int k) {
        return this.getFromIndex(this.getCoordinateIndex(i, j, k));
    }

    public void set(int i, int j, int k, int l) {
        this.setIndex(this.getCoordinateIndex(i, j, k), l);
    }

    private int getCoordinateIndex(int i, int j, int k) {
        return j << 8 | k << 4 | i;
    }

    public int getFromIndex(int i) {
        int j = this.getNibbleIndex(i);

        return this.data[j] >> ((i & 1) << 2) & 15; // Spigot
    }

    public void setIndex(int i, int j) {
        int k = this.getNibbleIndex(i);

        // Spigot start
        int shift = (i & 1) << 2;
        this.data[k] = (byte) (this.data[k] & ~(15 << shift) | (j & 15) << shift);
        // Spigot end
    }

    private boolean isLowerNibble(int i) {
        return (i & 1) == 0;
    }

    private int getNibbleIndex(int i) {
        return i >> 1;
    }

    public byte[] getData() {
        return this.data;
    }
}
