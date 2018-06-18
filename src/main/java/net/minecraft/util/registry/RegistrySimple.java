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

    private static final Logger LOGGER = LogManager.getLogger();
    protected final Map<K, V> registryObjects = this.createUnderlyingMap();
    private Object[] values;

    public RegistrySimple() {}

    protected Map<K, V> createUnderlyingMap() {
        return Maps.newHashMap();
    }

    @Nullable
    public V getObject(@Nullable K k0) {
        return this.registryObjects.get(k0);
    }

    public void putObject(K k0, V v0) {
        Validate.notNull(k0);
        Validate.notNull(v0);
        this.values = null;
        if (this.registryObjects.containsKey(k0)) {
            RegistrySimple.LOGGER.debug("Adding duplicate key \'{}\' to registry", k0);
        }

        this.registryObjects.put(k0, v0);
    }

    public Set<K> getKeys() {
        return Collections.unmodifiableSet(this.registryObjects.keySet());
    }

    @Nullable
    public V getRandomObject(Random random) {
        if (this.values == null) {
            Collection collection = this.registryObjects.values();

            if (collection.isEmpty()) {
                return null;
            }

            this.values = collection.toArray(new Object[collection.size()]);
        }

        return this.values[random.nextInt(this.values.length)];
    }

    public boolean containsKey(K k0) {
        return this.registryObjects.containsKey(k0);
    }

    public Iterator<V> iterator() {
        return this.registryObjects.values().iterator();
    }
}
