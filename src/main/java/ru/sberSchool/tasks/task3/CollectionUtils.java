package ru.sberSchool.tasks.task3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for performing common operations on collections.
 *
 * @author Elland Ilia
 * @version 1.0
 */
public class CollectionUtils {

    /**
     * Adds all elements from the source list to the destination list.
     *
     * @param source      the source list providing elements
     * @param destination the destination list receiving elements
     * @param <T>         the type of elements in the lists
     */
    public static <T> void addAll(List<? extends T> source, List<? super T> destination) {
        destination.addAll(source);
    }

    /**
     * Creates a new, empty {@code ArrayList}.
     *
     * @param <T> the type of elements in the list
     * @return a new empty {@code ArrayList}
     */
    public static <T> List<T> newArrayList() {
        return new ArrayList<>();
    }

    /**
     * Returns the index of the first occurrence of the specified element in the list.
     *
     * @param source the list to search
     * @param o      the element to find
     * @param <T>    the type of elements in the list
     * @return the index of the element, or -1 if not found
     */
    public static <T> int indexOf(List<? extends T> source, T o) {
        return source.indexOf(o);
    }

    /**
     * Returns a list containing at most the specified number of elements from the source list.
     *
     * @param source the source list
     * @param size   the maximum number of elements to include
     * @param <T>    the type of elements in the list
     * @return a list containing the first {@code size} elements of the source list
     */
    public static <T> List<T> limit(List<? extends T> source, int size) {
        return new ArrayList<>(source.subList(0, Math.min(size, source.size())));
    }

    /**
     * Adds the specified element to the list.
     *
     * @param source the list to which the element will be added
     * @param o      the element to add
     * @param <T>    the type of elements in the list
     */
    public static <T> void add(List<? super T> source, T o) {
        source.add(o);
    }

    /**
     * Removes all elements in the second list from the first list.
     *
     * @param removeFrom the list from which elements are to be removed
     * @param c2         the list of elements to remove
     * @param <T>        the type of elements in the lists
     */
    public static <T> void removeAll(List<? super T> removeFrom, List<T> c2) {
        removeFrom.removeAll(c2);
    }

    /**
     * Checks if the first list contains all elements of the second list.
     *
     * @param c1 the list to check
     * @param c2 the list of elements to check for
     * @param <T> the type of elements in the lists
     * @return {@code true} if the first list contains all elements of the second list
     */
    public static <T> boolean containsAll(List<? extends T> c1, List<T> c2) {
        return c1.containsAll(c2);
    }

    /**
     * Checks if the first list contains at least one element of the second list.
     *
     * @param c1 the list to check
     * @param c2 the list of elements to check for
     * @param <T> the type of elements in the lists
     * @return {@code true} if the first list contains at least one element of the second list
     */
    public static <T> boolean containsAny(List<? extends T> c1, List<T> c2) {
        return c2.stream()
                .anyMatch(c1::contains);
    }

    /**
     * Returns a list containing elements from the source list that are within the specified range.
     * The range is defined by elements that implement {@link Comparable}.
     *
     * @param list the source list
     * @param min  the lower bound (inclusive)
     * @param max  the upper bound (inclusive)
     * @param <T>  the type of elements in the list
     * @return a list containing elements in the specified range
     */
    public static <T extends Comparable<? super T>> List<T> range(List<? extends T> list, T min, T max) {
        return list.stream()
                .filter(el -> el.compareTo(min) >= 0 && el.compareTo(max) <= 0)
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Returns a list containing elements from the source list that are within the specified range.
     * The range is defined by a custom {@link Comparator}.
     *
     * @param list       the source list
     * @param min        the lower bound (inclusive)
     * @param max        the upper bound (inclusive)
     * @param comparator the comparator defining the order
     * @param <T>        the type of elements in the list
     * @return a list containing elements in the specified range
     */
    public static <T> List<T> range(List<? extends T> list, T min, T max, Comparator<T> comparator) {
        return list.stream()
                .filter(el -> comparator.compare(el, min) >= 0 && comparator.compare(el, max) <= 0)
                .sorted()
                .collect(Collectors.toList());
    }
}

