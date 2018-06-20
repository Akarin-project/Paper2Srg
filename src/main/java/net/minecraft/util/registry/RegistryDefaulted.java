package net.minecraft.util.registry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RegistryDefaulted<K, V> extends RegistrySimple<K, V> {

    private final V field_82597_b;

    public RegistryDefaulted(V v0) {
        this.field_82597_b = v0;
    }

    @Override
    @Nonnull
    public V func_82594_a(@Nullable K k0) {
        Object object = super.func_82594_a(k0);

        return object == null ? this.field_82597_b : (V) object;
    }
}
