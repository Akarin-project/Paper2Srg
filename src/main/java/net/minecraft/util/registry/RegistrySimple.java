package net.minecraft.util.registry;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.annotation.Nullable;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RegistrySimple<K, V> implements IRegistry<K, V> {

    private static final Logger field_148743_a = LogManager.getLogger();
    protected final Map<K, V> field_82596_a = this.func_148740_a();
    private Object[] field_186802_b;

    public RegistrySimple() {}

    protected Map<K, V> func_148740_a() {
        return Maps.newHashMap();
    }

    @Nullable
    public V func_82594_a(@Nullable K k0) {
        return this.field_82596_a.get(k0);
    }

    @Override
    public void func_82595_a(K k0, V v0) {
        Validate.notNull(k0);
        Validate.notNull(v0);
        this.field_186802_b = null;
        if (this.field_82596_a.containsKey(k0)) {
            RegistrySimple.field_148743_a.debug("Adding duplicate key \'{}\' to registry", k0);
        }

        this.field_82596_a.put(k0, v0);
    }

    public Set<K> func_148742_b() {
        return Collections.unmodifiableSet(this.field_82596_a.keySet());
    }

    @Nullable
    public V func_186801_a(Random random) {
        if (this.field_186802_b == null) {
            Collection collection = this.field_82596_a.values();

            if (collection.isEmpty()) {
                return null;
            }

            this.field_186802_b = collection.toArray(new Object[collection.size()]);
        }

        return (V) this.field_186802_b[random.nextInt(this.field_186802_b.length)];
    }

    public boolean func_148741_d(K k0) {
        return this.field_82596_a.containsKey(k0);
    }

    @Override
    public Iterator<V> iterator() {
        return this.field_82596_a.values().iterator();
    }
}
