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

    public static <T> Iterable<T[]> func_179318_a(Class<T> oclass, Iterable<? extends Iterable<? extends T>> iterable) {
        return new Cartesian.Product(oclass, (Iterable[]) func_179322_b(Iterable.class, iterable), null);
    }

    public static <T> Iterable<List<T>> func_179321_a(Iterable<? extends Iterable<? extends T>> iterable) {
        return func_179323_b(func_179318_a(Object.class, iterable));
    }

    private static <T> Iterable<List<T>> func_179323_b(Iterable<Object[]> iterable) {
        return Iterables.transform(iterable, new Cartesian.GetList(null));
    }

    private static <T> T[] func_179322_b(Class<? super T> oclass, Iterable<? extends T> iterable) {
        ArrayList arraylist = Lists.newArrayList();
        Iterator iterator = iterable.iterator();

        while (iterator.hasNext()) {
            Object object = iterator.next();

            arraylist.add(object);
        }

        return (Object[]) arraylist.toArray(func_179319_b(oclass, arraylist.size()));
    }

    private static <T> T[] func_179319_b(Class<? super T> oclass, int i) {
        return (Object[]) ((Object[]) Array.newInstance(oclass, i));
    }

    static class Product<T> implements Iterable<T[]> {

        private final Class<T> field_179429_a;
        private final Iterable<? extends T>[] field_179428_b;

        private Product(Class<T> oclass, Iterable<? extends T>[] aiterable) {
            this.field_179429_a = oclass;
            this.field_179428_b = aiterable;
        }

        public Iterator<T[]> iterator() {
            return (Iterator) (this.field_179428_b.length <= 0 ? Collections.singletonList((Object[]) Cartesian.func_179319_b(this.field_179429_a, 0)).iterator() : new Cartesian.Product.ProductIterator(this.field_179429_a, this.field_179428_b, null));
        }

        Product(Class oclass, Iterable[] aiterable, Object object) {
            this(oclass, aiterable);
        }

        static class ProductIterator<T> extends UnmodifiableIterator<T[]> {

            private int field_179426_a;
            private final Iterable<? extends T>[] field_179424_b;
            private final Iterator<? extends T>[] field_179425_c;
            private final T[] field_179423_d;

            private ProductIterator(Class<T> oclass, Iterable<? extends T>[] aiterable) {
                this.field_179426_a = -2;
                this.field_179424_b = aiterable;
                this.field_179425_c = (Iterator[]) Cartesian.func_179319_b(Iterator.class, this.field_179424_b.length);

                for (int i = 0; i < this.field_179424_b.length; ++i) {
                    this.field_179425_c[i] = aiterable[i].iterator();
                }

                this.field_179423_d = Cartesian.func_179319_b(oclass, this.field_179425_c.length);
            }

            private void func_179422_b() {
                this.field_179426_a = -1;
                Arrays.fill(this.field_179425_c, (Object) null);
                Arrays.fill(this.field_179423_d, (Object) null);
            }

            public boolean hasNext() {
                if (this.field_179426_a == -2) {
                    this.field_179426_a = 0;
                    Iterator[] aiterator = this.field_179425_c;
                    int i = aiterator.length;

                    for (int j = 0; j < i; ++j) {
                        Iterator iterator = aiterator[j];

                        if (!iterator.hasNext()) {
                            this.func_179422_b();
                            break;
                        }
                    }

                    return true;
                } else {
                    if (this.field_179426_a >= this.field_179425_c.length) {
                        for (this.field_179426_a = this.field_179425_c.length - 1; this.field_179426_a >= 0; --this.field_179426_a) {
                            Iterator iterator1 = this.field_179425_c[this.field_179426_a];

                            if (iterator1.hasNext()) {
                                break;
                            }

                            if (this.field_179426_a == 0) {
                                this.func_179422_b();
                                break;
                            }

                            iterator1 = this.field_179424_b[this.field_179426_a].iterator();
                            this.field_179425_c[this.field_179426_a] = iterator1;
                            if (!iterator1.hasNext()) {
                                this.func_179422_b();
                                break;
                            }
                        }
                    }

                    return this.field_179426_a >= 0;
                }
            }

            public T[] next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                } else {
                    while (this.field_179426_a < this.field_179425_c.length) {
                        this.field_179423_d[this.field_179426_a] = this.field_179425_c[this.field_179426_a].next();
                        ++this.field_179426_a;
                    }

                    return (Object[]) this.field_179423_d.clone();
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
