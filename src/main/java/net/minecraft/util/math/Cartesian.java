package net.minecraft.util.math;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import javax.annotation.Nullable;

public class Cartesian {

    public static <T> Iterable<T[]> cartesianProduct(Class<T> oclass, Iterable<? extends Iterable<? extends T>> iterable) {
        return new Cartesian.Product(oclass, (Iterable[]) toArray(Iterable.class, iterable), null);
    }

    public static <T> Iterable<List<T>> cartesianProduct(Iterable<? extends Iterable<? extends T>> iterable) {
        return arraysAsLists(cartesianProduct(Object.class, iterable));
    }

    private static <T> Iterable<List<T>> arraysAsLists(Iterable<Object[]> iterable) {
        return Iterables.transform(iterable, new Cartesian.GetList(null));
    }

    private static <T> T[] toArray(Class<? super T> oclass, Iterable<? extends T> iterable) {
        ArrayList arraylist = Lists.newArrayList();
        Iterator iterator = iterable.iterator();

        while (iterator.hasNext()) {
            Object object = iterator.next();

            arraylist.add(object);
        }

        return (Object[]) arraylist.toArray(createArray(oclass, arraylist.size()));
    }

    private static <T> T[] createArray(Class<? super T> oclass, int i) {
        return (Object[]) ((Object[]) Array.newInstance(oclass, i));
    }

    static class Product<T> implements Iterable<T[]> {

        private final Class<T> clazz;
        private final Iterable<? extends T>[] iterables;

        private Product(Class<T> oclass, Iterable<? extends T>[] aiterable) {
            this.clazz = oclass;
            this.iterables = aiterable;
        }

        public Iterator<T[]> iterator() {
            return (Iterator) (this.iterables.length <= 0 ? Collections.singletonList((Object[]) Cartesian.createArray(this.clazz, 0)).iterator() : new Cartesian.Product.ProductIterator(this.clazz, this.iterables, null));
        }

        Product(Class oclass, Iterable[] aiterable, Object object) {
            this(oclass, aiterable);
        }

        static class ProductIterator<T> extends UnmodifiableIterator<T[]> {

            private int index;
            private final Iterable<? extends T>[] iterables;
            private final Iterator<? extends T>[] iterators;
            private final T[] results;

            private ProductIterator(Class<T> oclass, Iterable<? extends T>[] aiterable) {
                this.index = -2;
                this.iterables = aiterable;
                this.iterators = (Iterator[]) Cartesian.createArray(Iterator.class, this.iterables.length);

                for (int i = 0; i < this.iterables.length; ++i) {
                    this.iterators[i] = aiterable[i].iterator();
                }

                this.results = Cartesian.createArray(oclass, this.iterators.length);
            }

            private void endOfData() {
                this.index = -1;
                Arrays.fill(this.iterators, (Object) null);
                Arrays.fill(this.results, (Object) null);
            }

            public boolean hasNext() {
                if (this.index == -2) {
                    this.index = 0;
                    Iterator[] aiterator = this.iterators;
                    int i = aiterator.length;

                    for (int j = 0; j < i; ++j) {
                        Iterator iterator = aiterator[j];

                        if (!iterator.hasNext()) {
                            this.endOfData();
                            break;
                        }
                    }

                    return true;
                } else {
                    if (this.index >= this.iterators.length) {
                        for (this.index = this.iterators.length - 1; this.index >= 0; --this.index) {
                            Iterator iterator1 = this.iterators[this.index];

                            if (iterator1.hasNext()) {
                                break;
                            }

                            if (this.index == 0) {
                                this.endOfData();
                                break;
                            }

                            iterator1 = this.iterables[this.index].iterator();
                            this.iterators[this.index] = iterator1;
                            if (!iterator1.hasNext()) {
                                this.endOfData();
                                break;
                            }
                        }
                    }

                    return this.index >= 0;
                }
            }

            public T[] next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                } else {
                    while (this.index < this.iterators.length) {
                        this.results[this.index] = this.iterators[this.index].next();
                        ++this.index;
                    }

                    return (Object[]) this.results.clone();
                }
            }

            public Object next() {
                return this.next();
            }

            ProductIterator(Class oclass, Iterable[] aiterable, Object object) {
                this(oclass, aiterable);
            }
        }
    }

    static class GetList<T> implements Function<Object[], List<T>> {

        private GetList() {}

        public List<T> apply(@Nullable Object[] aobject) {
            return Arrays.asList((Object[]) aobject);
        }

        public Object apply(@Nullable Object object) {
            return this.apply((Object[]) object);
        }

        GetList(Object object) {
            this();
        }
    }
}
