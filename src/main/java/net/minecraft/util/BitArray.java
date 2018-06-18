package net.minecraft.util;


import net.minecraft.util.math.MathHelper;

public class BitArray {

    private final long[] longArray;
    private final int bitsPerEntry;
    private final long maxEntryValue;
    private final int arraySize;

    public BitArray(int i, int j) {
        //Validate.inclusiveBetween(1L, 32L, (long) i); // Paper
        this.arraySize = j;
        this.bitsPerEntry = i;
        this.maxEntryValue = (1L << i) - 1L;
        this.longArray = new long[MathHelper.roundUp(j * i, 64) / 64];
    }

    public void setAt(int i, int j) {
        //Validate.inclusiveBetween(0L, (long) (this.d - 1), (long) i); // Paper
        //Validate.inclusiveBetween(0L, this.c, (long) j); // Paper
        int k = i * this.bitsPerEntry;
        int l = k / 64;
        int i1 = ((i + 1) * this.bitsPerEntry - 1) / 64;
        int j1 = k % 64;

        this.longArray[l] = this.longArray[l] & ~(this.maxEntryValue << j1) | ((long) j & this.maxEntryValue) << j1;
        if (l != i1) {
            int k1 = 64 - j1;
            int l1 = this.bitsPerEntry - k1;

            this.longArray[i1] = this.longArray[i1] >>> l1 << l1 | ((long) j & this.maxEntryValue) >> k1;
        }

    }

    public int getAt(int i) {
        //Validate.inclusiveBetween(0L, (long) (this.d - 1), (long) i); // Paper
        int j = i * this.bitsPerEntry;
        int k = j / 64;
        int l = ((i + 1) * this.bitsPerEntry - 1) / 64;
        int i1 = j % 64;

        if (k == l) {
            return (int) (this.longArray[k] >>> i1 & this.maxEntryValue);
        } else {
            int j1 = 64 - i1;

            return (int) ((this.longArray[k] >>> i1 | this.longArray[l] << j1) & this.maxEntryValue);
        }
    }

    public long[] getDataBits() { return this.getBackingLongArray(); } // Paper - Anti-Xray - OBFHELPER
    public long[] getBackingLongArray() {
        return this.longArray;
    }

    public int size() {
        return this.arraySize;
    }
}
