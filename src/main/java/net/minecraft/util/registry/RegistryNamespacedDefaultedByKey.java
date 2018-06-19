package net.minecraft.util.registry;

import java.util.Random;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.commons.lang3.Validate;

public class RegistryNamespacedDefaultedByKey<K, V> extends RegistryNamespaced<K, V> {

    private final K field_148760_d;
    private V field_148761_e;

    public RegistryNamespacedDefaultedByKey(K k0) {
        this.field_148760_d = k0;
    }

    public void func_177775_a(int i, K k0, V v0) {
        if (this.field_148760_d.equals(k0)) {
            this.field_148761_e = v0;
        }

        super.func_177775_a(i, k0, v0);
    }

    public void func_177776_a() {
        Validate.notNull(this.field_148761_e, "Missing default of DefaultedMappedRegistry: " + this.field_148760_d, new Object[0]);
    }

    public int func_148757_b(V v0) {
        int i = super.func_148757_b(v0);

        return i == -1 ? super.func_148757_b(this.field_148761_e) : i;
    }

    @Nonnull
    public K func_177774_c(V v0) {
        Object object = super.func_177774_c(v0);

        return object == null ? this.field_148760_d : object;
    }

    @Nonnull
    public V func_82594_a(@Nullable K k0) {
        Object object = super.func_82594_a(k0);

        return object == null ? this.field_148761_e : object;
    }

    @Nonnull
    public V func_148754_a(int i) {
        Object object = super.func_148754_a(i);

        return object == null ? this.field_148761_e : object;
    }

    @Nonnull
    public V func_186801_a(Random random) {
        Object object = super.func_186801_a(random);

        return object == null ? this.field_148761_e : object;
    }
}
