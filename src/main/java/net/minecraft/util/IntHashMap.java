package net.minecraft.util;

import javax.annotation.Nullable;

public class IntHashMap<V> {

    private transient IntHashMap.Entry<V>[] slots = new IntHashMap.Entry[16];
    private transient int count;
    private int threshold = 12;
    private final float growFactor = 0.75F;

    public IntHashMap() {}

    private static int computeHash(int i) {
        i ^= i >>> 20 ^ i >>> 12;
        return i ^ i >>> 7 ^ i >>> 4;
    }

    private static int getSlotIndex(int i, int j) {
        return i & j - 1;
    }

    @Nullable
    public V lookup(int i) {
        int j = computeHash(i);

        for (IntHashMap.Entry inthashmap_inthashmapentry = this.slots[getSlotIndex(j, this.slots.length)]; inthashmap_inthashmapentry != null; inthashmap_inthashmapentry = inthashmap_inthashmapentry.nextEntry) {
            if (inthashmap_inthashmapentry.hashEntry == i) {
                return inthashmap_inthashmapentry.valueEntry;
            }
        }

        return null;
    }

    public boolean containsItem(int i) {
        return this.lookupEntry(i) != null;
    }

    @Nullable
    final IntHashMap.Entry<V> lookupEntry(int i) {
        int j = computeHash(i);

        for (IntHashMap.Entry inthashmap_inthashmapentry = this.slots[getSlotIndex(j, this.slots.length)]; inthashmap_inthashmapentry != null; inthashmap_inthashmapentry = inthashmap_inthashmapentry.nextEntry) {
            if (inthashmap_inthashmapentry.hashEntry == i) {
                return inthashmap_inthashmapentry;
            }
        }

        return null;
    }

    public void addKey(int i, V v0) {
        int j = computeHash(i);
        int k = getSlotIndex(j, this.slots.length);

        for (IntHashMap.Entry inthashmap_inthashmapentry = this.slots[k]; inthashmap_inthashmapentry != null; inthashmap_inthashmapentry = inthashmap_inthashmapentry.nextEntry) {
            if (inthashmap_inthashmapentry.hashEntry == i) {
                inthashmap_inthashmapentry.valueEntry = v0;
                return;
            }
        }

        this.insert(j, i, v0, k);
    }

    private void grow(int i) {
        IntHashMap.Entry[] ainthashmap_inthashmapentry = this.slots;
        int j = ainthashmap_inthashmapentry.length;

        if (j == 1073741824) {
            this.threshold = Integer.MAX_VALUE;
        } else {
            IntHashMap.Entry[] ainthashmap_inthashmapentry1 = new IntHashMap.Entry[i];

            this.copyTo(ainthashmap_inthashmapentry1);
            this.slots = ainthashmap_inthashmapentry1;
            this.threshold = (int) ((float) i * this.growFactor);
        }
    }

    private void copyTo(IntHashMap.Entry<V>[] ainthashmap_inthashmapentry) {
        IntHashMap.Entry[] ainthashmap_inthashmapentry1 = this.slots;
        int i = ainthashmap_inthashmapentry.length;

        for (int j = 0; j < ainthashmap_inthashmapentry1.length; ++j) {
            IntHashMap.Entry inthashmap_inthashmapentry = ainthashmap_inthashmapentry1[j];

            if (inthashmap_inthashmapentry != null) {
                ainthashmap_inthashmapentry1[j] = null;

                IntHashMap.Entry inthashmap_inthashmapentry1;

                do {
                    inthashmap_inthashmapentry1 = inthashmap_inthashmapentry.nextEntry;
                    int k = getSlotIndex(inthashmap_inthashmapentry.slotHash, i);

                    inthashmap_inthashmapentry.nextEntry = ainthashmap_inthashmapentry[k];
                    ainthashmap_inthashmapentry[k] = inthashmap_inthashmapentry;
                    inthashmap_inthashmapentry = inthashmap_inthashmapentry1;
                } while (inthashmap_inthashmapentry1 != null);
            }
        }

    }

    @Nullable
    public V removeObject(int i) {
        IntHashMap.Entry inthashmap_inthashmapentry = this.removeEntry(i);

        return inthashmap_inthashmapentry == null ? null : inthashmap_inthashmapentry.valueEntry;
    }

    @Nullable
    final IntHashMap.Entry<V> removeEntry(int i) {
        int j = computeHash(i);
        int k = getSlotIndex(j, this.slots.length);
        IntHashMap.Entry inthashmap_inthashmapentry = this.slots[k];

        IntHashMap.Entry inthashmap_inthashmapentry1;
        IntHashMap.Entry inthashmap_inthashmapentry2;

        for (inthashmap_inthashmapentry1 = inthashmap_inthashmapentry; inthashmap_inthashmapentry1 != null; inthashmap_inthashmapentry1 = inthashmap_inthashmapentry2) {
            inthashmap_inthashmapentry2 = inthashmap_inthashmapentry1.nextEntry;
            if (inthashmap_inthashmapentry1.hashEntry == i) {
                --this.count;
                if (inthashmap_inthashmapentry == inthashmap_inthashmapentry1) {
                    this.slots[k] = inthashmap_inthashmapentry2;
                } else {
                    inthashmap_inthashmapentry.nextEntry = inthashmap_inthashmapentry2;
                }

                return inthashmap_inthashmapentry1;
            }

            inthashmap_inthashmapentry = inthashmap_inthashmapentry1;
        }

        return inthashmap_inthashmapentry1;
    }

    public void clearMap() {
        IntHashMap.Entry[] ainthashmap_inthashmapentry = this.slots;

        for (int i = 0; i < ainthashmap_inthashmapentry.length; ++i) {
            ainthashmap_inthashmapentry[i] = null;
        }

        this.count = 0;
    }

    private void insert(int i, int j, V v0, int k) {
        IntHashMap.Entry inthashmap_inthashmapentry = this.slots[k];

        this.slots[k] = new IntHashMap.Entry(i, j, v0, inthashmap_inthashmapentry);
        if (this.count++ >= this.threshold) {
            this.grow(2 * this.slots.length);
        }

    }

    static class Entry<V> {

        final int hashEntry;
        V valueEntry;
        IntHashMap.Entry<V> nextEntry;
        final int slotHash;

        Entry(int i, int j, V v0, IntHashMap.Entry<V> inthashmap_inthashmapentry) {
            this.valueEntry = v0;
            this.nextEntry = inthashmap_inthashmapentry;
            this.hashEntry = j;
            this.slotHash = i;
        }

        public final int getHash() {
            return this.hashEntry;
        }

        public final V getValue() {
            return this.valueEntry;
        }

        public final boolean equals(Object object) {
            if (!(object instanceof IntHashMap.Entry)) {
                return false;
            } else {
                IntHashMap.Entry inthashmap_inthashmapentry = (IntHashMap.Entry) object;

                if (this.hashEntry == inthashmap_inthashmapentry.hashEntry) {
                    Object object1 = this.getValue();
                    Object object2 = inthashmap_inthashmapentry.getValue();

                    if (object1 == object2 || object1 != null && object1.equals(object2)) {
                        return true;
                    }
                }

                return false;
            }
        }

        public final int hashCode() {
            return IntHashMap.computeHash(this.hashEntry);
        }

        public final String toString() {
            return this.getHash() + "=" + this.getValue();
        }
    }
}
