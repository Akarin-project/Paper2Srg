package net.minecraft.util;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class LowerStringMap<V> implements Map<String, V> {

    private final Map<String, V> field_76117_a = Maps.newLinkedHashMap();

    public LowerStringMap() {}

    public int size() {
        return this.field_76117_a.size();
    }

    public boolean isEmpty() {
        return this.field_76117_a.isEmpty();
    }

    public boolean containsKey(Object object) {
        return this.field_76117_a.containsKey(object.toString().toLowerCase(Locale.ROOT));
    }

    public boolean containsValue(Object object) {
        return this.field_76117_a.containsKey(object);
    }

    public V get(Object object) {
        return this.field_76117_a.get(object.toString().toLowerCase(Locale.ROOT));
    }

    public V put(String s, V v0) {
        return this.field_76117_a.put(s.toLowerCase(Locale.ROOT), v0);
    }

    public V remove(Object object) {
        return this.field_76117_a.remove(object.toString().toLowerCase(Locale.ROOT));
    }

    public void putAll(Map<? extends String, ? extends V> map) {
        Iterator iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            this.put((String) entry.getKey(), entry.getValue());
        }

    }

    public void clear() {
        this.field_76117_a.clear();
    }

    public Set<String> keySet() {
        return this.field_76117_a.keySet();
    }

    public Collection<V> values() {
        return this.field_76117_a.values();
    }

    public Set<Entry<String, V>> entrySet() {
        return this.field_76117_a.entrySet();
    }

    public Object put(Object object, Object object1) {
        return this.put((String) object, object1);
    }
}
