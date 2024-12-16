package ru.sberSchool.tasks.task3;

import java.util.HashMap;
import java.util.Map;

/**
 * An implementation of the {@link CountMap} interface using a {@code HashMap}
 * to store elements and their counts.
 *
 * @param <T> the type of elements in this container
 * @author Elland Ilia
 * @version 1.0
 */
public class CountMapImpl<T> implements CountMap <T>{

    private final Map<T, Integer> map = new HashMap<>();

    @Override
    public void add(T o) {
        map.put(o, map.getOrDefault(o, 0) + 1);
    }


    @Override
    public int getCount(T o) {
        return map.getOrDefault(o, 0);
    }

    @Override
    public int remove(T o) {
        return map.remove(o);
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public void addAll(CountMap<T> source) {
        Map<T, Integer> sourceMap = source.toMap();
        sourceMap.forEach((key, value) -> map.put(key, map.getOrDefault(key, 0) + value));
    }

    @Override
    public Map<T, Integer> toMap() {
        return new HashMap<>(map);
    }

    @Override
    public void toMap(Map<T, Integer> destination) {
        destination.putAll(map);
    }
}
