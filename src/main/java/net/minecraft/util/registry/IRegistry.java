package net.minecraft.util.registry;

public interface IRegistry<K, V> extends Iterable<V> {

    void putObject(K k0, V v0);
}
