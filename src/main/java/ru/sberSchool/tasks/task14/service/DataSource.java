package ru.sberSchool.tasks.task14.service;

/**
 * Interface that defines the operations for a data source used for caching.
 * This interface is meant to be implemented by classes that handle storage and retrieval of cached data.
 *
 * @author Elland Ilia
 */
public interface DataSource {
    /**
     * Initializes the data source. This method is typically used to establish connections or perform setup tasks.
     * It should be called before using other methods like {@link #save(String, String)} and {@link #get(String)}.
     */
    void init();

    /**
     * Saves a key-value pair to the data source.
     * If the key already exists, it may overwrite the existing value, depending on the data source implementation.
     *
     * @param key The key to store the data under.
     * @param value The value to be stored.
     */
    void save(String key, String value);

    /**
     * Retrieves the value associated with the provided key from the data source.
     * If no value is found, this method should return {@code null}.
     *
     * @param key The key whose associated value is to be returned.
     * @return The value associated with the key, or {@code null} if no value exists for the key.
     */
    String get(String key);
}
