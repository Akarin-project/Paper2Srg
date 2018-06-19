package net.minecraft.util;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.commons.lang3.Validate;

public class NonNullList<E> extends AbstractList<E> {

    private final List<E> field_191198_a;
    private final E field_191199_b;

    public static <E> NonNullList<E> func_191196_a() {
        return new NonNullList();
    }

    public static <E> NonNullList<E> func_191197_a(int i, E e0) {
        Validate.notNull(e0);
        Object[] aobject = new Object[i];

        Arrays.fill(aobject, e0);
        return new NonNullList(Arrays.asList(aobject), e0);
    }

    public static <E> NonNullList<E> func_193580_a(E e0, E... ae) {
        return new NonNullList(Arrays.asList(ae), e0);
    }

    protected NonNullList() {
        this(new ArrayList(), (Object) null);
    }

    protected NonNullList(List<E> list, @Nullable E e0) {
        this.field_191198_a = list;
        this.field_191199_b = e0;
    }

    @Nonnull
    public E get(int i) {
        return this.field_191198_a.get(i);
    }

    public E set(int i, E e0) {
        Validate.notNull(e0);
        return this.field_191198_a.set(i, e0);
    }

    public void add(int i, E e0) {
        Validate.notNull(e0);
        this.field_191198_a.add(i, e0);
    }

    public E remove(int i) {
        return this.field_191198_a.remove(i);
    }

    public int size() {
        return this.field_191198_a.size();
    }

    public void clear() {
        if (this.field_191199_b == null) {
            super.clear();
        } else {
            for (int i = 0; i < this.size(); ++i) {
                this.set(i, this.field_191199_b);
            }
        }

    }
}
