package net.minecraft.util.registry;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.Nullable;

import net.minecraft.util.IObjectIntIterable;
import net.minecraft.util.IntIdentityHashBiMap;

public class RegistryNamespaced<K, V> extends RegistrySimple<K, V> implements IObjectIntIterable<V> {

    protected final IntIdentityHashBiMap<V> field_148759_a = new IntIdentityHashBiMap(256);
    protected final Map<V, K> field_148758_b;

    public RegistryNamespaced() {
        this.field_148758_b = ((BiMap) this.field_82596_a).inverse();
    }

    public void func_177775_a(int i, K k0, V v0) {
        this.field_148759_a.func_186814_a(v0, i);
        this.func_82595_a(k0, v0);
    }

    protected Map<K, V> func_148740_a() {
        return HashBiMap.create();
    }

    @Nullable
    public V func_82594_a(@Nullable K k0) {
        return super.func_82594_a(k0);
    }

    @Nullable
    public K func_177774_c(V v0) {
        return this.field_148758_b.get(v0);
    }

    public boolean func_148741_d(K k0) {
        return super.func_148741_d(k0);
    }

    public int func_148757_b(@Nullable V v0) {
        return this.field_148759_a.func_186815_a(v0);
    }

    @Nullable
    public V func_148754_a(int i) {
        return this.field_148759_a.func_186813_a(i);
    }

    public Iterator<V> iterator() {
        return this.field_148759_a.iterator();
    }
}
