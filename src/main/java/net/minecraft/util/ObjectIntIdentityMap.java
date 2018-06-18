package net.minecraft.util;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

public class ObjectIntIdentityMap<T> implements IObjectIntIterable { // Paper - Fix decompile error

    private final IdentityHashMap<T, Integer> identityMap;
    private final List<T> objectList;

    public ObjectIntIdentityMap() {
        this(512);
    }

    public ObjectIntIdentityMap(int i) {
        this.objectList = Lists.newArrayListWithExpectedSize(i);
        this.identityMap = new IdentityHashMap(i);
    }

    public void put(T t0, int i) {
        this.identityMap.put(t0, Integer.valueOf(i));

        while (this.objectList.size() <= i) {
            this.objectList.add(null); // Paper - Fix decompile error
        }

        this.objectList.set(i, t0);
    }

    public int get(T t0) {
        Integer integer = (Integer) this.identityMap.get(t0);

        return integer == null ? -1 : integer.intValue();
    }

    @Nullable
    public final T getByValue(int i) {
        return i >= 0 && i < this.objectList.size() ? this.objectList.get(i) : null;
    }

    public Iterator<T> iterator() {
        return Iterators.filter(this.objectList.iterator(), Predicates.notNull());
    }

    public int size() { return this.size(); } // Paper - Anti-Xray - OBFHELPER
    public int size() {
        return this.identityMap.size();
    }
}
