package ru.sberSchool.tasks.task8.annotations;

import ru.sberSchool.tasks.task8.enums.CacheType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for enabling caching on a method. This can be used to cache the results of a method call
 * either in memory or in a file system, with additional options for customization.
 *
 * <p>Using this annotation, you can specify the type of cache, whether to compress cached data, and
 * other configurations like limiting list size or specifying keys to identify cache entries.</p>
 *
 * <p>Supported caching mechanisms include:
 * <ul>
 *     <li>In-memory caching</li>
 *     <li>File-based caching</li>
 * </ul>
 * </p>
 *
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * @Cache(cacheType = CacheType.FILE, fileNamePrefix = "myCache", zip = true, identityBy = {String.class})
 * public List<String> fetchData(String parameter) {
 *     // Method implementation
 * }
 * }
 * </pre>
 *
 * @author Elland Ilia
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Cache {

    /**
     * Specifies the type of caching to use.
     * <p>Default: {@link CacheType#IN_MEMORY}</p>
     *
     * @return {@link CacheType} - the cache type.
     */
    CacheType cacheType() default CacheType.IN_MEMORY;

    /**
     * Specifies a prefix for the cache file name if file-based caching is used.
     *
     * @return the file name prefix.
     */
    String fileNamePrefix() default "";

    /**
     * Indicates whether cached data should be compressed.
     * <p>When set to {@code true}, data is compressed using GZIP.</p>
     * <p>Default: {@code false}</p>
     *
     * @return {@code true} if data should be compressed, {@code false} otherwise.
     */
    boolean zip() default false;

    /**
     * Specifies the fields or parameters used to uniquely identify a cache entry.
     * <p>Default: empty array, which means all parameters are used as identity keys.</p>
     *
     * @return an array of classes representing the identity fields.
     */
    Class<?>[] identityBy() default {};

    /**
     * Limits the maximum size of a cached list.
     * <p>Applicable when the cached result is a collection. If the result size exceeds this limit,
     * it will be truncated.</p>
     * <p>Default: {@link Integer#MAX_VALUE}</p>
     *
     * @return the maximum number of elements allowed in the cached list.
     */
    int listLimit() default Integer.MAX_VALUE;

}