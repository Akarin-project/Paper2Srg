package net.minecraft.util;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.AbstractSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClassInheritanceMultiMap<T> extends AbstractSet<T> {

    private static final Set<Class<?>> ALL_KNOWN = Sets.newConcurrentHashSet(); // CraftBukkit
    private final Map<Class<?>, List<T>> map = Maps.newHashMap();
    private final Set<Class<?>> knownKeys = Sets.newIdentityHashSet();
    private final Class<T> baseClass;
    private final List<T> values = Lists.newArrayList();

    public ClassInheritanceMultiMap(Class<T> oclass) {
        this.baseClass = oclass;
        this.knownKeys.add(oclass);
        this.map.put(oclass, this.values);
        Iterator iterator = ClassInheritanceMultiMap.ALL_KNOWN.iterator();

        while (iterator.hasNext()) {
            Class oclass1 = (Class) iterator.next();

            this.createLookup(oclass1);
        }

    }

    protected void createLookup(Class<?> oclass) {
        ClassInheritanceMultiMap.ALL_KNOWN.add(oclass);
        Iterator iterator = this.values.iterator();

        while (iterator.hasNext()) {
            Object object = iterator.next();

            if (oclass.isAssignableFrom(object.getClass())) {
                this.addForClass((T) object, oclass);
            }
        }

        this.knownKeys.add(oclass);
    }

    protected Class<?> initializeClassLookup(Class<?> oclass) {
        if (this.baseClass.isAssignableFrom(oclass)) {
            if (!this.knownKeys.contains(oclass)) {
                this.createLookup(oclass);
            }

            return oclass;
        } else {
            throw new IllegalArgumentException("Don\'t know how to search for " + oclass);
        }
    }

    public boolean add(T t0) {
        Iterator iterator = this.knownKeys.iterator();

        while (iterator.hasNext()) {
            Class oclass = (Class) iterator.next();

            if (oclass.isAssignableFrom(t0.getClass())) {
                this.addForClass(t0, oclass);
            }
        }

        return true;
    }

    private void addForClass(T t0, Class<?> oclass) {
        List list = (List) this.map.get(oclass);

        if (list == null) {
            this.map.put(oclass, Lists.newArrayList(t0));
        } else {
            list.add(t0);
        }

    }

    public boolean remove(Object object) {
        Object object1 = object;
        boolean flag = false;
        Iterator iterator = this.knownKeys.iterator();

        while (iterator.hasNext()) {
            Class oclass = (Class) iterator.next();

            if (oclass.isAssignableFrom(object1.getClass())) {
                List list = (List) this.map.get(oclass);

                if (list != null && list.remove(object1)) {
                    flag = true;
                }
            }
        }

        return flag;
    }

    public boolean contains(Object object) {
        return Iterators.contains(this.getByClass(object.getClass()).iterator(), object);
    }

    public <S> Iterable<S> getByClass(final Class<S> oclass) {
        return new Iterable() {
            public Iterator<S> iterator() {
                List list = (List) ClassInheritanceMultiMap.this.map.get(ClassInheritanceMultiMap.this.initializeClassLookup(oclass));

                if (list == null) {
                    return Collections.emptyIterator();
                } else {
                    Iterator iterator = list.iterator();

                    return Iterators.filter(iterator, oclass);
                }
            }
        };
    }

    public Iterator<T> iterator() {
        return (Iterator) (this.values.isEmpty() ? Collections.emptyIterator() : Iterators.unmodifiableIterator(this.values.iterator()));
    }

    public int size() {
        return this.values.size();
    }
}
