package net.minecraft.util;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

public class MapPopulator {

    public static <K, V> Map<K, V> func_179400_b(Iterable<K> iterable, Iterable<V> iterable1) {
        return func_179399_a(iterable, iterable1, Maps.newLinkedHashMap());
    }

    public static <K, V> Map<K, V> func_179399_a(Iterable<K> iterable, Iterable<V> iterable1, Map<K, V> map) {
        Iterator iterator = iterable1.iterator();
        Iterator iterator1 = iterable.iterator();

        while (iterator1.hasNext()) {
            Object object = iterator1.next();

            map.put(object, iterator.next());
        }

        if (iterator.hasNext()) {
            throw new NoSuchElementException();
        } else {
            return map;
        }
    }
}
