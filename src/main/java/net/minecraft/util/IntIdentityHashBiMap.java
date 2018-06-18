package net.minecraft.util;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterators;
import java.util.Iterator;
import javax.annotation.Nullable;

import net.minecraft.util.math.MathHelper;

public class IntIdentityHashBiMap<K> implements IObjectIntIterable<K> {

    private static final Object EMPTY = null;
    private K[] values;
    private int[] intKeys;
    private K[] byId;
    private int nextFreeIndex;
    private int mapSize;

    public IntIdentityHashBiMap(int i) {
        i = (int) ((float) i / 0.8F);
        this.values = (Object[]) (new Object[i]);
        this.intKeys = new int[i];
        this.byId = (Object[]) (new Object[i]);
    }

    public int getId(@Nullable K k0) {
        return this.getValue(this.getIndex(k0, this.hashObject(k0)));
    }

    @Nullable
    public K get(int i) {
        return i >= 0 && i < this.byId.length ? this.byId[i] : null;
    }

    private int getValue(int i) {
        return i == -1 ? -1 : this.intKeys[i];
    }

    public int add(K k0) {
        int i = this.nextId();

        this.put(k0, i);
        return i;
    }

    private int nextId() {
        while (this.nextFreeIndex < this.byId.length && this.byId[this.nextFreeIndex] != null) {
            ++this.nextFreeIndex;
        }

        return this.nextFreeIndex;
    }

    private void grow(int i) {
        Object[] aobject = this.values;
        int[] aint = this.intKeys;

        this.values = (Object[]) (new Object[i]);
        this.intKeys = new int[i];
        this.byId = (Object[]) (new Object[i]);
        this.nextFreeIndex = 0;
        this.mapSize = 0;

        for (int j = 0; j < aobject.length; ++j) {
            if (aobject[j] != null) {
                this.put(aobject[j], aint[j]);
            }
        }

    }

    public void put(K k0, int i) {
        int j = Math.max(i, this.mapSize + 1);
        int k;

        if ((float) j >= (float) this.values.length * 0.8F) {
            for (k = this.values.length << 1; k < i; k <<= 1) {
                ;
            }

            this.grow(k);
        }

        k = this.findEmpty(this.hashObject(k0));
        this.values[k] = k0;
        this.intKeys[k] = i;
        this.byId[i] = k0;
        ++this.mapSize;
        if (i == this.nextFreeIndex) {
            ++this.nextFreeIndex;
        }

    }

    private int hashObject(@Nullable K k0) {
        return (MathHelper.hash(System.identityHashCode(k0)) & Integer.MAX_VALUE) % this.values.length;
    }

    private int getIndex(@Nullable K k0, int i) {
        int j;

        for (j = i; j < this.values.length; ++j) {
            if (this.values[j] == k0) {
                return j;
            }

            if (this.values[j] == IntIdentityHashBiMap.EMPTY) {
                return -1;
            }
        }

        for (j = 0; j < i; ++j) {
            if (this.values[j] == k0) {
                return j;
            }

            if (this.values[j] == IntIdentityHashBiMap.EMPTY) {
                return -1;
            }
        }

        return -1;
    }

    private int findEmpty(int i) {
        int j;

        for (j = i; j < this.values.length; ++j) {
            if (this.values[j] == IntIdentityHashBiMap.EMPTY) {
                return j;
            }
        }

        for (j = 0; j < i; ++j) {
            if (this.values[j] == IntIdentityHashBiMap.EMPTY) {
                return j;
            }
        }

        throw new RuntimeException("Overflowed :(");
    }

    public Iterator<K> iterator() {
        return Iterators.filter(Iterators.forArray(this.byId), Predicates.notNull());
    }

    public int size() {
        return this.mapSize;
    }
}
