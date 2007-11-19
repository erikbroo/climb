package com.googlecode.climb.game.utils;

/**
 * An array list of fixed size. There is only one method
 * {@link #add(int) add(int element)} for adding new elements and one method
 * {@link get(int) get(int index)} for retrieving elements. The list stores the
 * n least recently added elements only (where n is the size of the list). The
 * most recently added element is stored at index 0 and the least recently added
 * element is stored at index {@code size - 1}.
 */
public class RingList<E>
{
    private final Object[] list;

    private int currentStartIndex;

    private final int size;

    public RingList(int size)
    {
        this.size = size;
        this.list = new Object[size];
        this.currentStartIndex = 0;
    }

    public void add(E element)
    {
        this.currentStartIndex -= 1;
        if (this.currentStartIndex == -1) {
            this.currentStartIndex = this.size - 1;
        }

        this.list[this.currentStartIndex] = element;
    }

    @SuppressWarnings( "unchecked" )
    public E get(int index)
    {
        if (index >= this.list.length) {
            throw new ArrayIndexOutOfBoundsException(index);
        }

        final int realIndex = (this.currentStartIndex + index)
                % this.list.length;

        return (E) this.list[realIndex];
    }

    public int getSize()
    {
        return this.size;
    }
}
