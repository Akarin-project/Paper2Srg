package net.minecraft.util;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.commons.lang3.Validate;

public class NonNullList<E> extends AbstractList<E> {

    private final List<E> delegate;
    private final E defaultElement;

    public static <E> NonNullList<E> create() {
        return new NonNullList();
    }

    public static <E> NonNullList<E> withSize(int i, E e0) {
        Validate.notNull(e0);
        Object[] aobject = new Object[i];

        Arrays.fill(aobject, e0);
        return new NonNullList(Arrays.asList(aobject), e0);
    }

    public static <E> NonNullList<E> from(E e0, E... ae) {
        return new NonNullList(Arrays.asList(ae), e0);
    }

    protected NonNullList() {
        this(new ArrayList(), (Object) null);
    }

    protected NonNullList(List<E> list, @Nullable E e0) {
        this.delegate = list;
        this.defaultElement = e0;
    }

    @Nonnull
    public E get(int i) {
        return this.delegate.get(i);
    }

    public E set(int i, E e0) {
        Validate.notNull(e0);
        return this.delegate.set(i, e0);
    }

    public void add(int i, E e0) {
        Validate.notNull(e0);
        this.delegate.add(i, e0);
    }

    public E remove(int i) {
        return this.delegate.remove(i);
    }

    public int size() {
        return this.delegate.size();
    }

    public void clear() {
        if (this.defaultElement == null) {
            super.clear();
        } else {
            for (int i = 0; i < this.size(); ++i) {
                this.set(i, this.defaultElement);
            }
        }

    }
}
