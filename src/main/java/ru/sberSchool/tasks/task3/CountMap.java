package ru.sberSchool.tasks.task3;

import java.util.Map;

/**
 * A generic interface for a container that counts the occurrences of elements.
 *
 * @param <T> the type of elements in this container
 * @author Elland Ilia
 * @version 1.0
 */
public interface CountMap <T> {

    /**
     * Adds an element to the container.
     *
     * @param o the element to add
     */
    void add(T o);

    /**
     * Returns the count of how many times the specified element has been added.
     *
     * @param o the element whose count is to be returned
     * @return the count of the specified element
     */
    int getCount(T o);

    /**
     * Removes the specified element from the container and returns its count
     * prior to removal.
     *
     * @param o the element to remove
     * @return the count of the element prior to removal
     */
    int remove(T o);

    /**
     * Returns the number of distinct elements in the container.
     *
     * @return the number of distinct elements
     */
    int size();

    /**
     * Adds all elements from the specified source CountMap to this container.
     * If an element exists in both containers, their counts are summed.
     *
     * @param source the source CountMap whose elements are to be added
     */
    void addAll(CountMap<T> source);

    /**
     * Returns a {@code Map} representation of the container. The keys are the elements
     * in the container, and the values are their respective counts.
     *
     * @return a {@code Map} representing the elements and their counts
     */
    Map<T, Integer> toMap();

    /**
     * Copies all elements and their counts from this container to the specified
     * destination {@code Map}.
     *
     * @param destination the destination {@code Map} where elements and counts will be stored
     */
    void toMap(Map<T, Integer> destination);
}
