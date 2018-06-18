package net.minecraft.util.registry;

import java.util.Random;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.commons.lang3.Validate;

public class RegistryNamespacedDefaultedByKey<K, V> extends RegistryNamespaced<K, V> {

    private final K defaultValueKey;
    private V defaultValue;

    public RegistryNamespacedDefaultedByKey(K k0) {
        this.defaultValueKey = k0;
    }

    public void register(int i, K k0, V v0) {
        if (this.defaultValueKey.equals(k0)) {
            this.defaultValue = v0;
        }

        super.register(i, k0, v0);
    }

    public void validateKey() {
        Validate.notNull(this.defaultValue, "Missing default of DefaultedMappedRegistry: " + this.defaultValueKey, new Object[0]);
    }

    public int getIDForObject(V v0) {
        int i = super.getIDForObject(v0);

        return i == -1 ? super.getIDForObject(this.defaultValue) : i;
    }

    @Nonnull
    public K getNameForObject(V v0) {
        Object object = super.getNameForObject(v0);

        return object == null ? this.defaultValueKey : object;
    }

    @Nonnull
    public V getObject(@Nullable K k0) {
        Object object = super.getObject(k0);

        return object == null ? this.defaultValue : object;
    }

    @Nonnull
    public V getObjectById(int i) {
        Object object = super.getObjectById(i);

        return object == null ? this.defaultValue : object;
    }

    @Nonnull
    public V getRandomObject(Random random) {
        Object object = super.getRandomObject(random);

        return object == null ? this.defaultValue : object;
    }
}
