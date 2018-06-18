package net.minecraft.util.registry;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.Nullable;

import net.minecraft.util.IObjectIntIterable;
import net.minecraft.util.IntIdentityHashBiMap;

public class RegistryNamespaced<K, V> extends RegistrySimple<K, V> implements IObjectIntIterable<V> {

    protected final IntIdentityHashBiMap<V> underlyingIntegerMap = new IntIdentityHashBiMap(256);
    protected final Map<V, K> inverseObjectRegistry;

    public RegistryNamespaced() {
        this.inverseObjectRegistry = ((BiMap) this.registryObjects).inverse();
    }

    public void register(int i, K k0, V v0) {
        this.underlyingIntegerMap.put(v0, i);
        this.putObject(k0, v0);
    }

    protected Map<K, V> createUnderlyingMap() {
        return HashBiMap.create();
    }

    @Nullable
    public V getObject(@Nullable K k0) {
        return super.getObject(k0);
    }

    @Nullable
    public K getNameForObject(V v0) {
        return this.inverseObjectRegistry.get(v0);
    }

    public boolean containsKey(K k0) {
        return super.containsKey(k0);
    }

    public int getIDForObject(@Nullable V v0) {
        return this.underlyingIntegerMap.getId(v0);
    }

    @Nullable
    public V getObjectById(int i) {
        return this.underlyingIntegerMap.get(i);
    }

    public Iterator<V> iterator() {
        return this.underlyingIntegerMap.iterator();
    }
}
