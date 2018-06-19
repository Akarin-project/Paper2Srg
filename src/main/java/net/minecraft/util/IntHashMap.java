package net.minecraft.util;

import javax.annotation.Nullable;

public class IntHashMap<V> {

    private transient IntHashMap.Entry<V>[] field_76055_a = new IntHashMap.Entry[16];
    private transient int field_76053_b;
    private int field_76054_c = 12;
    private final float field_76051_d = 0.75F;

    public IntHashMap() {}

    private static int func_76044_g(int i) {
        i ^= i >>> 20 ^ i >>> 12;
        return i ^ i >>> 7 ^ i >>> 4;
    }

    private static int func_76043_a(int i, int j) {
        return i & j - 1;
    }

    @Nullable
    public V func_76041_a(int i) {
        int j = func_76044_g(i);

        for (IntHashMap.Entry inthashmap_inthashmapentry = this.field_76055_a[func_76043_a(j, this.field_76055_a.length)]; inthashmap_inthashmapentry != null; inthashmap_inthashmapentry = inthashmap_inthashmapentry.field_76034_c) {
            if (inthashmap_inthashmapentry.field_76035_a == i) {
                return inthashmap_inthashmapentry.field_76033_b;
            }
        }

        return null;
    }

    public boolean func_76037_b(int i) {
        return this.func_76045_c(i) != null;
    }

    @Nullable
    final IntHashMap.Entry<V> func_76045_c(int i) {
        int j = func_76044_g(i);

        for (IntHashMap.Entry inthashmap_inthashmapentry = this.field_76055_a[func_76043_a(j, this.field_76055_a.length)]; inthashmap_inthashmapentry != null; inthashmap_inthashmapentry = inthashmap_inthashmapentry.field_76034_c) {
            if (inthashmap_inthashmapentry.field_76035_a == i) {
                return inthashmap_inthashmapentry;
            }
        }

        return null;
    }

    public void func_76038_a(int i, V v0) {
        int j = func_76044_g(i);
        int k = func_76043_a(j, this.field_76055_a.length);

        for (IntHashMap.Entry inthashmap_inthashmapentry = this.field_76055_a[k]; inthashmap_inthashmapentry != null; inthashmap_inthashmapentry = inthashmap_inthashmapentry.field_76034_c) {
            if (inthashmap_inthashmapentry.field_76035_a == i) {
                inthashmap_inthashmapentry.field_76033_b = v0;
                return;
            }
        }

        this.func_76040_a(j, i, v0, k);
    }

    private void func_76047_h(int i) {
        IntHashMap.Entry[] ainthashmap_inthashmapentry = this.field_76055_a;
        int j = ainthashmap_inthashmapentry.length;

        if (j == 1073741824) {
            this.field_76054_c = Integer.MAX_VALUE;
        } else {
            IntHashMap.Entry[] ainthashmap_inthashmapentry1 = new IntHashMap.Entry[i];

            this.func_76048_a(ainthashmap_inthashmapentry1);
            this.field_76055_a = ainthashmap_inthashmapentry1;
            this.field_76054_c = (int) ((float) i * this.field_76051_d);
        }
    }

    private void func_76048_a(IntHashMap.Entry<V>[] ainthashmap_inthashmapentry) {
        IntHashMap.Entry[] ainthashmap_inthashmapentry1 = this.field_76055_a;
        int i = ainthashmap_inthashmapentry.length;

        for (int j = 0; j < ainthashmap_inthashmapentry1.length; ++j) {
            IntHashMap.Entry inthashmap_inthashmapentry = ainthashmap_inthashmapentry1[j];

            if (inthashmap_inthashmapentry != null) {
                ainthashmap_inthashmapentry1[j] = null;

                IntHashMap.Entry inthashmap_inthashmapentry1;

                do {
                    inthashmap_inthashmapentry1 = inthashmap_inthashmapentry.field_76034_c;
                    int k = func_76043_a(inthashmap_inthashmapentry.field_76032_d, i);

                    inthashmap_inthashmapentry.field_76034_c = ainthashmap_inthashmapentry[k];
                    ainthashmap_inthashmapentry[k] = inthashmap_inthashmapentry;
                    inthashmap_inthashmapentry = inthashmap_inthashmapentry1;
                } while (inthashmap_inthashmapentry1 != null);
            }
        }

    }

    @Nullable
    public V func_76049_d(int i) {
        IntHashMap.Entry inthashmap_inthashmapentry = this.func_76036_e(i);

        return inthashmap_inthashmapentry == null ? null : inthashmap_inthashmapentry.field_76033_b;
    }

    @Nullable
    final IntHashMap.Entry<V> func_76036_e(int i) {
        int j = func_76044_g(i);
        int k = func_76043_a(j, this.field_76055_a.length);
        IntHashMap.Entry inthashmap_inthashmapentry = this.field_76055_a[k];

        IntHashMap.Entry inthashmap_inthashmapentry1;
        IntHashMap.Entry inthashmap_inthashmapentry2;

        for (inthashmap_inthashmapentry1 = inthashmap_inthashmapentry; inthashmap_inthashmapentry1 != null; inthashmap_inthashmapentry1 = inthashmap_inthashmapentry2) {
            inthashmap_inthashmapentry2 = inthashmap_inthashmapentry1.field_76034_c;
            if (inthashmap_inthashmapentry1.field_76035_a == i) {
                --this.field_76053_b;
                if (inthashmap_inthashmapentry == inthashmap_inthashmapentry1) {
                    this.field_76055_a[k] = inthashmap_inthashmapentry2;
                } else {
                    inthashmap_inthashmapentry.field_76034_c = inthashmap_inthashmapentry2;
                }

                return inthashmap_inthashmapentry1;
            }

            inthashmap_inthashmapentry = inthashmap_inthashmapentry1;
        }

        return inthashmap_inthashmapentry1;
    }

    public void func_76046_c() {
        IntHashMap.Entry[] ainthashmap_inthashmapentry = this.field_76055_a;

        for (int i = 0; i < ainthashmap_inthashmapentry.length; ++i) {
            ainthashmap_inthashmapentry[i] = null;
        }

        this.field_76053_b = 0;
    }

    private void func_76040_a(int i, int j, V v0, int k) {
        IntHashMap.Entry inthashmap_inthashmapentry = this.field_76055_a[k];

        this.field_76055_a[k] = new IntHashMap.Entry(i, j, v0, inthashmap_inthashmapentry);
        if (this.field_76053_b++ >= this.field_76054_c) {
            this.func_76047_h(2 * this.field_76055_a.length);
        }

    }

    static class Entry<V> {

        final int field_76035_a;
        V field_76033_b;
        IntHashMap.Entry<V> field_76034_c;
        final int field_76032_d;

        Entry(int i, int j, V v0, IntHashMap.Entry<V> inthashmap_inthashmapentry) {
            this.field_76033_b = v0;
            this.field_76034_c = inthashmap_inthashmapentry;
            this.field_76035_a = j;
            this.field_76032_d = i;
        }

        public final int func_76031_a() {
            return this.field_76035_a;
        }

        public final V func_76030_b() {
            return this.field_76033_b;
        }

        public final boolean equals(Object object) {
            if (!(object instanceof IntHashMap.Entry)) {
                return false;
            } else {
                IntHashMap.Entry inthashmap_inthashmapentry = (IntHashMap.Entry) object;

                if (this.field_76035_a == inthashmap_inthashmapentry.field_76035_a) {
                    Object object1 = this.func_76030_b();
                    Object object2 = inthashmap_inthashmapentry.func_76030_b();

                    if (object1 == object2 || object1 != null && object1.equals(object2)) {
                        return true;
                    }
                }

                return false;
            }
        }

        public final int hashCode() {
            return IntHashMap.func_76044_g(this.field_76035_a);
        }

        public final String toString() {
            return this.func_76031_a() + "=" + this.func_76030_b();
        }
    }
}
