package ru.sberSchool.tasks.task6;

import ru.sberSchool.tasks.Constants;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An iterator for iterating over an array of objects of type {@code T}.
 * This class implements the {@link Iterator} interface and provides methods
 * to traverse through the array elements.
 *
 * @param <T> the type of elements in the array
 *
 * @author Elland Ilia
 */
public class ObjectArrayIterator<T> implements Iterator<T> {
    private final T[] array;
    private int currentIndex = 0;

    /**
     * Constructs a new {@code ObjectArrayIterator} for the specified array.
     *
     * @param array the array to iterate over. It must not be {@code null}.
     * @throws IllegalArgumentException if the specified array is {@code null}.
     */
    public ObjectArrayIterator(T[] array) {
        if (array == null) {
            throw new IllegalArgumentException(Constants.NON_NULL_ARRAY_MESSAGE);
        }
        this.array = array;
    }

    /**
     * Returns {@code true} if there are more elements to iterate over in the array.
     *
     * @return {@code true} if the array contains more elements, {@code false} otherwise.
     */
    @Override
    public boolean hasNext() {
        return currentIndex < array.length;
    }

    /**
     * Returns the next element in the array and advances the iterator's position.
     *
     * @return the next element in the array.
     * @throws NoSuchElementException if there are no more elements to iterate over.
     */
    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException(Constants.NO_ELEMENTS_MESSAGE);
        }
        return array[currentIndex++];
    }
}
