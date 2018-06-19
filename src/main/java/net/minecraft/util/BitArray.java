package net.minecraft.util;


import net.minecraft.util.math.MathHelper;

public class BitArray {

    private final long[] field_188145_a;
    private final int field_188146_b;
    private final long field_188147_c;
    private final int field_188148_d;

    public BitArray(int i, int j) {
        //Validate.inclusiveBetween(1L, 32L, (long) i); // Paper
        this.field_188148_d = j;
        this.field_188146_b = i;
        this.field_188147_c = (1L << i) - 1L;
        this.field_188145_a = new long[MathHelper.func_154354_b(j * i, 64) / 64];
    }

    public void func_188141_a(int i, int j) {
        //Validate.inclusiveBetween(0L, (long) (this.d - 1), (long) i); // Paper
        //Validate.inclusiveBetween(0L, this.c, (long) j); // Paper
        int k = i * this.field_188146_b;
        int l = k / 64;
        int i1 = ((i + 1) * this.field_188146_b - 1) / 64;
        int j1 = k % 64;

        this.field_188145_a[l] = this.field_188145_a[l] & ~(this.field_188147_c << j1) | ((long) j & this.field_188147_c) << j1;
        if (l != i1) {
            int k1 = 64 - j1;
            int l1 = this.field_188146_b - k1;

            this.field_188145_a[i1] = this.field_188145_a[i1] >>> l1 << l1 | ((long) j & this.field_188147_c) >> k1;
        }

    }

    public int func_188142_a(int i) {
        //Validate.inclusiveBetween(0L, (long) (this.d - 1), (long) i); // Paper
        int j = i * this.field_188146_b;
        int k = j / 64;
        int l = ((i + 1) * this.field_188146_b - 1) / 64;
        int i1 = j % 64;

        if (k == l) {
            return (int) (this.field_188145_a[k] >>> i1 & this.field_188147_c);
        } else {
            int j1 = 64 - i1;

            return (int) ((this.field_188145_a[k] >>> i1 | this.field_188145_a[l] << j1) & this.field_188147_c);
        }
    }

    public long[] getDataBits() { return this.func_188143_a(); } // Paper - Anti-Xray - OBFHELPER
    public long[] func_188143_a() {
        return this.field_188145_a;
    }

    public int func_188144_b() {
        return this.field_188148_d;
    }
}
