package net.minecraft.util;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

public class ObjectIntIdentityMap<T> implements IObjectIntIterable { // Paper - Fix decompile error

    private final IdentityHashMap<T, Integer> field_148749_a;
    private final List<T> field_148748_b;

    public ObjectIntIdentityMap() {
        this(512);
    }

    public ObjectIntIdentityMap(int i) {
        this.field_148748_b = Lists.newArrayListWithExpectedSize(i);
        this.field_148749_a = new IdentityHashMap(i);
    }

    public void func_148746_a(T t0, int i) {
        this.field_148749_a.put(t0, Integer.valueOf(i));

        while (this.field_148748_b.size() <= i) {
            this.field_148748_b.add(null); // Paper - Fix decompile error
        }

        this.field_148748_b.set(i, t0);
    }

    public int func_148747_b(T t0) {
        Integer integer = (Integer) this.field_148749_a.get(t0);

        return integer == null ? -1 : integer.intValue();
    }

    @Nullable
    public final T func_148745_a(int i) {
        return i >= 0 && i < this.field_148748_b.size() ? this.field_148748_b.get(i) : null;
    }

    public Iterator<T> iterator() {
        return Iterators.filter(this.field_148748_b.iterator(), Predicates.notNull());
    }

    public int size() { return this.func_186804_a(); } // Paper - Anti-Xray - OBFHELPER
    public int func_186804_a() {
        return this.field_148749_a.size();
    }
}
