package com.googlecode.climb.game.utils;

import java.util.ArrayList;
import java.util.Comparator;


/**
 * 
 */
public class SortedArrayList<E>
{
    private final Comparator<E> comparator;

    private final ArrayList<E> list;

    /**
     * @param comparator
     */
    public SortedArrayList(Comparator<E> comparator)
    {
        this.comparator = comparator;
        this.list = new ArrayList<E>();
    }

    /**
     * @param element
     */
    public void add(E newElement)
    {
        final int size = this.list.size();

        for (int i = 0; i < size; i++) {
            final E currentElement = this.list.get(i);
            if (this.comparator.compare(newElement, currentElement) <= 0) {
                this.list.add(i, newElement);
                return;
            }
        }

        this.list.add(newElement);
    }

    public int size()
    {
        return this.list.size();
    }

    public E get(int index)
    {
        return this.list.get(index);
    }
}
