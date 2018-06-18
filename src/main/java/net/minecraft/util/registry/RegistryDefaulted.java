package net.minecraft.util.registry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RegistryDefaulted<K, V> extends RegistrySimple<K, V> {

    private final V defaultObject;

    public RegistryDefaulted(V v0) {
        this.defaultObject = v0;
    }

    @Nonnull
    public V getObject(@Nullable K k0) {
        Object object = super.getObject(k0);

        return object == null ? this.defaultObject : object;
    }
}
