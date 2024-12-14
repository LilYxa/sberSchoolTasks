package ru.sberSchool.tasks.task9.util;

import lombok.extern.slf4j.Slf4j;
import ru.sberSchool.tasks.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A utility class that provides a fluent API for working with collections using a stream-like approach.
 * <p>
 * This class encapsulates a collection and provides methods for filtering, transforming, and converting it
 * into other data structures such as lists or maps.
 * </p>
 *
 * @param <T> the type of elements in the collection
 *
 * @author Elland Ilia
 * @version 1.0
 */
@Slf4j
public class Streams<T> {

    private final List<T> collection;

    /**
     * Private constructor to initialize the Streams instance with a collection.
     *
     * @param collection the collection to be encapsulated
     */
    private Streams(List<T> collection) {
        this.collection = new ArrayList<>(collection);
    }

    /**
     * Creates a new {@code Streams} instance from the provided collection.
     *
     * @param list the input collection
     * @param <T> the type of elements in the collection
     * @return a {@code Streams} instance containing the elements of the input collection
     * @throws NullPointerException if the input collection is {@code null}
     */
    public static <T> Streams<T> of(List<? extends T> list) {
        log.debug("of[0]: Creating Streams from collection: {}", list);
        return new Streams<>(new ArrayList<>(list));
    }

    /**
     * Filters the elements of the collection using the given predicate.
     *
     * @param predicate a {@link Predicate} to filter the elements
     * @return a new {@code Streams} instance containing only the elements that match the predicate
     * @throws NullPointerException if the predicate is {@code null}
     */
    public Streams<T> filter(Predicate<? super T> predicate) {
        log.debug("filter[0]: Filter using predicate: {}", predicate);
        Objects.requireNonNull(predicate, Constants.PREDICATE_NON_NULL);
        List<T> filteredList = collection.stream()
                .filter(predicate)
                .toList();
        log.debug("filter[1]: Filtered collection: {}", collection);
        return new Streams<>(filteredList);
    }

    /**
     * Transforms the elements of the collection using the given mapping function.
     *
     * @param function a {@link Function} to transform the elements
     * @param <R> the type of the elements in the resulting collection
     * @return a new {@code Streams} instance containing the transformed elements
     * @throws NullPointerException if the function is {@code null}
     */
    public <R> Streams<R> transform(Function<? super T, ? extends R> function) {
        log.debug("transform[0]: Transform with function: {}", function);
        Objects.requireNonNull(function, Constants.TRANSFORM_FUNC_NON_NULL);
        List<R> transformed = collection.stream()
                .map(function)
                .collect(Collectors.toList());
        return new Streams<>(transformed);
    }

    /**
     * Converts the elements of the collection into a {@link Map} using the given key and value mapping functions.
     *
     * @param keyMapper a {@link Function} to extract keys from the elements
     * @param valueMapper a {@link Function} to extract values from the elements
     * @param <K> the type of keys in the resulting map
     * @param <V> the type of values in the resulting map
     * @return a {@code Map} containing the elements of the collection as key-value pairs
     * @throws NullPointerException if either {@code keyMapper} or {@code valueMapper} is {@code null}
     */
    public <K, V> Map<K, V> toMap(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper) {
        log.debug("toMap[0]: Converting to map");
        Objects.requireNonNull(keyMapper, Constants.KEY_MAPPER_NON_NULL);
        Objects.requireNonNull(valueMapper, Constants.VALUE_MAPPER_NON_NULL);
        Map<K, V> map = collection.stream()
                .collect(Collectors.toMap(keyMapper, valueMapper));
        log.debug("toMap[1]: Result map: {}", map);
        return map;
    }

    /**
     * Converts the elements of the collection into a new {@link List}.
     *
     * @return a {@code List} containing the elements of the collection
     */
    public List<T> toList() {
        return new ArrayList<>(collection);
    }
}
