package net.biryeongtrain.text_emulator.utils.collections;

import com.google.common.collect.Lists;
import org.jetbrains.annotations.Nullable;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * this is a list that has a default element
 * you can use this like in inventory system
 * @param <E>
 */

public class DefaultedList<E> extends AbstractList<E> {
    private final List<E> delegate;
    @Nullable
    private final E defaultElement;

    public static <E> DefaultedList<E> of() {
        return new DefaultedList<>(Lists.newArrayList(), null);
    }

    public static <E> DefaultedList<E> ofSize(int size, E defaultElement) {
        Objects.requireNonNull(defaultElement);
        Object[] objects = new Object[size];
        Arrays.fill(objects, defaultElement);
        return new DefaultedList<>(Arrays.asList((E[]) objects), defaultElement);
    }

    @SafeVarargs
    public static <E> DefaultedList<E> copyOf(E defaultElement, E ... values) {
        return new DefaultedList<>(Arrays.asList(values), defaultElement);
    }


    public DefaultedList(List<E> delegate, @Nullable E defaultElement) {
        this.delegate = delegate;
        this.defaultElement = defaultElement;
    }

    @Override
    public E get(int index) {
        return this.delegate.get(index);
    }

    @Override
    public E set(int index, E element) {
        Objects.requireNonNull(element);
        return this.delegate.set(index, element);
    }

    @Override
    public void add(int index, E element) {
        Objects.requireNonNull(element);
        this.delegate.add(index, element);
    }

    @Override
    public int size() {
        return this.delegate.size();
    }

    @Override
    public void clear() {
        if (this.defaultElement == null) {
            super.clear();
        } else {
            this.replaceAll(ignored -> this.defaultElement);
        }
    }
}
