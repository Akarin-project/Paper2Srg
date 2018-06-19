package net.minecraft.util;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterators;
import java.util.Iterator;
import javax.annotation.Nullable;

import net.minecraft.util.math.MathHelper;

public class IntIdentityHashBiMap<K> implements IObjectIntIterable<K> {

    private static final Object field_186817_a = null;
    private K[] field_186818_b;
    private int[] field_186819_c;
    private K[] field_186820_d;
    private int field_186821_e;
    private int field_186822_f;

    public IntIdentityHashBiMap(int i) {
        i = (int) ((float) i / 0.8F);
        this.field_186818_b = (Object[]) (new Object[i]);
        this.field_186819_c = new int[i];
        this.field_186820_d = (Object[]) (new Object[i]);
    }

    public int func_186815_a(@Nullable K k0) {
        return this.func_186805_c(this.func_186816_b(k0, this.func_186811_d(k0)));
    }

    @Nullable
    public K func_186813_a(int i) {
        return i >= 0 && i < this.field_186820_d.length ? this.field_186820_d[i] : null;
    }

    private int func_186805_c(int i) {
        return i == -1 ? -1 : this.field_186819_c[i];
    }

    public int func_186808_c(K k0) {
        int i = this.func_186809_c();

        this.func_186814_a(k0, i);
        return i;
    }

    private int func_186809_c() {
        while (this.field_186821_e < this.field_186820_d.length && this.field_186820_d[this.field_186821_e] != null) {
            ++this.field_186821_e;
        }

        return this.field_186821_e;
    }

    private void func_186807_d(int i) {
        Object[] aobject = this.field_186818_b;
        int[] aint = this.field_186819_c;

        this.field_186818_b = (Object[]) (new Object[i]);
        this.field_186819_c = new int[i];
        this.field_186820_d = (Object[]) (new Object[i]);
        this.field_186821_e = 0;
        this.field_186822_f = 0;

        for (int j = 0; j < aobject.length; ++j) {
            if (aobject[j] != null) {
                this.func_186814_a(aobject[j], aint[j]);
            }
        }

    }

    public void func_186814_a(K k0, int i) {
        int j = Math.max(i, this.field_186822_f + 1);
        int k;

        if ((float) j >= (float) this.field_186818_b.length * 0.8F) {
            for (k = this.field_186818_b.length << 1; k < i; k <<= 1) {
                ;
            }

            this.func_186807_d(k);
        }

        k = this.func_186806_e(this.func_186811_d(k0));
        this.field_186818_b[k] = k0;
        this.field_186819_c[k] = i;
        this.field_186820_d[i] = k0;
        ++this.field_186822_f;
        if (i == this.field_186821_e) {
            ++this.field_186821_e;
        }

    }

    private int func_186811_d(@Nullable K k0) {
        return (MathHelper.func_188208_f(System.identityHashCode(k0)) & Integer.MAX_VALUE) % this.field_186818_b.length;
    }

    private int func_186816_b(@Nullable K k0, int i) {
        int j;

        for (j = i; j < this.field_186818_b.length; ++j) {
            if (this.field_186818_b[j] == k0) {
                return j;
            }

            if (this.field_186818_b[j] == IntIdentityHashBiMap.field_186817_a) {
                return -1;
            }
        }

        for (j = 0; j < i; ++j) {
            if (this.field_186818_b[j] == k0) {
                return j;
            }

            if (this.field_186818_b[j] == IntIdentityHashBiMap.field_186817_a) {
                return -1;
            }
        }

        return -1;
    }

    private int func_186806_e(int i) {
        int j;

        for (j = i; j < this.field_186818_b.length; ++j) {
            if (this.field_186818_b[j] == IntIdentityHashBiMap.field_186817_a) {
                return j;
            }
        }

        for (j = 0; j < i; ++j) {
            if (this.field_186818_b[j] == IntIdentityHashBiMap.field_186817_a) {
                return j;
            }
        }

        throw new RuntimeException("Overflowed :(");
    }

    public Iterator<K> iterator() {
        return Iterators.filter(Iterators.forArray(this.field_186820_d), Predicates.notNull());
    }

    public int func_186810_b() {
        return this.field_186822_f;
    }
}
