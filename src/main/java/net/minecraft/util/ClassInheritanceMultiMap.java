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

    private static final Set<Class<?>> field_181158_a = Sets.newConcurrentHashSet(); // CraftBukkit
    private final Map<Class<?>, List<T>> field_180218_a = Maps.newHashMap();
    private final Set<Class<?>> field_180216_b = Sets.newIdentityHashSet();
    private final Class<T> field_180217_c;
    private final List<T> field_181745_e = Lists.newArrayList();

    public ClassInheritanceMultiMap(Class<T> oclass) {
        this.field_180217_c = oclass;
        this.field_180216_b.add(oclass);
        this.field_180218_a.put(oclass, this.field_181745_e);
        Iterator iterator = ClassInheritanceMultiMap.field_181158_a.iterator();

        while (iterator.hasNext()) {
            Class oclass1 = (Class) iterator.next();

            this.func_180213_a(oclass1);
        }

    }

    protected void func_180213_a(Class<?> oclass) {
        ClassInheritanceMultiMap.field_181158_a.add(oclass);
        Iterator iterator = this.field_181745_e.iterator();

        while (iterator.hasNext()) {
            Object object = iterator.next();

            if (oclass.isAssignableFrom(object.getClass())) {
                this.func_181743_a((T) object, oclass);
            }
        }

        this.field_180216_b.add(oclass);
    }

    protected Class<?> func_181157_b(Class<?> oclass) {
        if (this.field_180217_c.isAssignableFrom(oclass)) {
            if (!this.field_180216_b.contains(oclass)) {
                this.func_180213_a(oclass);
            }

            return oclass;
        } else {
            throw new IllegalArgumentException("Don\'t know how to search for " + oclass);
        }
    }

    public boolean add(T t0) {
        Iterator iterator = this.field_180216_b.iterator();

        while (iterator.hasNext()) {
            Class oclass = (Class) iterator.next();

            if (oclass.isAssignableFrom(t0.getClass())) {
                this.func_181743_a(t0, oclass);
            }
        }

        return true;
    }

    private void func_181743_a(T t0, Class<?> oclass) {
        List list = (List) this.field_180218_a.get(oclass);

        if (list == null) {
            this.field_180218_a.put(oclass, Lists.newArrayList(t0));
        } else {
            list.add(t0);
        }

    }

    public boolean remove(Object object) {
        Object object1 = object;
        boolean flag = false;
        Iterator iterator = this.field_180216_b.iterator();

        while (iterator.hasNext()) {
            Class oclass = (Class) iterator.next();

            if (oclass.isAssignableFrom(object1.getClass())) {
                List list = (List) this.field_180218_a.get(oclass);

                if (list != null && list.remove(object1)) {
                    flag = true;
                }
            }
        }

        return flag;
    }

    public boolean contains(Object object) {
        return Iterators.contains(this.func_180215_b(object.getClass()).iterator(), object);
    }

    public <S> Iterable<S> func_180215_b(final Class<S> oclass) {
        return new Iterable() {
            public Iterator<S> iterator() {
                List list = (List) ClassInheritanceMultiMap.this.field_180218_a.get(ClassInheritanceMultiMap.this.func_181157_b(oclass));

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
        return (Iterator) (this.field_181745_e.isEmpty() ? Collections.emptyIterator() : Iterators.unmodifiableIterator(this.field_181745_e.iterator()));
    }

    public int size() {
        return this.field_181745_e.size();
    }
}
