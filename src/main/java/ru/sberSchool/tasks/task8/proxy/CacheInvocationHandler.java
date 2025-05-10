package ru.sberSchool.tasks.task8.proxy;

import lombok.extern.slf4j.Slf4j;
import ru.sberSchool.tasks.Constants;
import ru.sberSchool.tasks.task8.annotations.Cache;
import ru.sberSchool.tasks.task8.exceptions.FolderCreationException;
import ru.sberSchool.tasks.task8.exceptions.NonSerializableDataException;
import ru.sberSchool.tasks.task8.exceptions.UnknownCacheTypeException;

import java.io.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * A dynamic invocation handler that enables caching for methods annotated with {@link Cache}.
 * <p>
 * This class handles caching logic, supporting both in-memory and file-based caching.
 * It generates cache keys, manages cache entries, and provides functionality to store
 * and retrieve data from the cache.
 * </p>
 *
 * @author Elland Ilia
 */
@Slf4j
public class CacheInvocationHandler implements InvocationHandler {

    private final Object target;
    private final String rootPath;
    private final Map<String, Object> cache;

    /**
     * Constructs a {@code CacheInvocationHandler} with the specified target object,
     * root path for file-based caching, and in-memory cache map.
     *
     * @param target    the target object to be proxied.
     * @param rootPath  the root directory for file-based caching.
     * @param cache     the in-memory cache map.
     */
    public CacheInvocationHandler(Object target, String rootPath, Map<String, Object> cache) {
        this.target = target;
        this.rootPath = rootPath;
        this.cache = cache;
    }

    /**
     * Intercepts method calls and applies caching logic if the method is annotated with {@link Cache}.
     *
     * @param proxy  the proxy instance.
     * @param method the method being invoked.
     * @param args   the arguments passed to the method.
     * @return the result of the method call, potentially cached.
     * @throws Throwable if any exception occurs during method execution or caching.
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.debug("invoke[0]: invoking method: {}", method.getName());
        Cache cacheAnnotation = method.getAnnotation(Cache.class);

        if (cacheAnnotation == null) {
            log.debug("invoke[1]: No caching annotation! Invoking original method.");
            return method.invoke(target, args);
        }

        if (method.getReturnType().equals(Void.TYPE)) {
            log.debug("invoke[2]: Method {} returns void, skipping caching.", method.getName());
            return method.invoke(target, args);
        }

        String cacheKey = generateCacheKey(method, args, cacheAnnotation);
        log.debug("invoke[3]: Cache key: {}", cacheKey);

        switch (cacheAnnotation.cacheType()) {
            case IN_MEMORY -> {
                return handleInMemoryCache(method, args, cacheKey, cacheAnnotation);
            }
            case FILE -> {
                return handleFileCache(method, args, cacheKey, cacheAnnotation);
            }
            default -> throw new UnknownCacheTypeException(String.format(Constants.UNKNOWN_CACHE_TYPE_MESSAGE, cacheAnnotation.cacheType()));
        }
    }

    /**
     * Handles in-memory caching for the specified method call.
     *
     * @param method          the method being invoked.
     * @param args            the arguments passed to the method.
     * @param cacheKey        the key to look up in the cache.
     * @param cacheAnnotation the cache annotation associated with the method.
     * @return the cached or freshly computed result of the method call.
     * @throws InvocationTargetException if the method invocation fails.
     * @throws IllegalAccessException    if the method cannot be accessed.
     */
    private Object handleInMemoryCache(Method method, Object[] args, String cacheKey, Cache cacheAnnotation) throws InvocationTargetException, IllegalAccessException {
        log.debug("handleInMemoryCache[0]: Handling in-memory cache with key: {}", cacheKey);
        if (cache.containsKey(cacheKey)) {
            log.debug("handleInMemoryCache[1]: Cache hit");
            return cache.get(cacheKey);
        }

        log.debug("handleInMemoryCache[2]: Cache miss, invoking original method");
        Object result = method.invoke(target, args);
        result = applyListLimit(result, cacheAnnotation.listLimit());
        cache.put(cacheKey, result);
        return result;
    }

    /**
     * Handles file-based caching for the specified method call.
     *
     * @param method          the method being invoked.
     * @param args            the arguments passed to the method.
     * @param cacheKey        the key to look up in the file cache.
     * @param cacheAnnotation the cache annotation associated with the method.
     * @return the cached or freshly computed result of the method call.
     * @throws FolderCreationException  if the required directories cannot be created.
     * @throws IOException              if file operations fail.
     * @throws ClassNotFoundException   if deserialization fails.
     * @throws InvocationTargetException if the method invocation fails.
     * @throws IllegalAccessException    if the method cannot be accessed.
     * @throws NonSerializableDataException if the result is not serializable.
     */
    private Object handleFileCache(Method method, Object[] args, String cacheKey, Cache cacheAnnotation) throws FolderCreationException, IOException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, NonSerializableDataException {
        log.debug("handleFileCache[0]: Handling file cache with key: {}", cacheKey);
        String fileName = cacheKey + Constants.CACHE_EXTENSION + (cacheAnnotation.zip() ? Constants.ZIP_EXTENSION : "");
        File cacheFile = new File(rootPath, fileName);
        File parentDir = cacheFile.getParentFile();

        if (parentDir != null && !parentDir.exists()) {
            boolean dirsCreated = parentDir.mkdirs();
            if (dirsCreated) {
                log.debug("handleFileCache[1]: Folder was created.");
            } else {
                log.error("handleFileCache[0]: Error during folder creation");
                throw new FolderCreationException(Constants.FAILED_FOLDER_CREATION);
            }
        }

        if (cache.containsKey(cacheKey)) {
            log.debug("handleFileCache[2]: Cache hit");
            return readFromFile(cacheFile, cacheAnnotation.zip());
        }

        log.debug("handleFileCache[3]: Cache miss, invoking original method");
        Object result = method.invoke(target, args);
        result = applyListLimit(result, cacheAnnotation.listLimit());
        writeToFile(cacheFile, result, cacheAnnotation.zip());
        return result;
    }

    /**
     * Generates a unique cache key based on the method, arguments, and annotation settings.
     *
     * @param method          the method being invoked.
     * @param args            the arguments passed to the method.
     * @param cacheAnnotation the cache annotation associated with the method.
     * @return a unique cache key.
     */
    private String generateCacheKey(Method method, Object[] args, Cache cacheAnnotation) {
        log.debug("generateCacheKey[0]: Generating cache key for method: {}", method.getName());
        String prefix = cacheAnnotation.fileNamePrefix().isEmpty() ? method.getName() : cacheAnnotation.fileNamePrefix();

        Set<Class<?>> identityClasses = new HashSet<>(Arrays.asList(cacheAnnotation.identityBy()));
        return prefix + IntStream.range(0, args.length)
                .filter(i -> identityClasses.isEmpty() || identityClasses.contains(method.getParameterTypes()[i]))
                .mapToObj(i -> Constants.UNDERSCORE_SEPARATOR + args[i])
                .collect(Collectors.joining());
    }

    /**
     * Applies a limit to the size of a list if the result is a list and exceeds the specified limit.
     *
     * @param result the result of the method call.
     * @param limit  the maximum allowed size of the list.
     * @return the original result or a truncated list.
     */
    private Object applyListLimit(Object result, int limit) {
        log.debug("applyListLimit[0]: Applying list limit: {}", limit);
        if (result instanceof List<?> && ((List<?>) result).size() > limit) {
            log.debug("applyListLimit[1]: Limiting list size to {}", limit);
            return ((List<?>) result).subList(0, limit);
        }
        return result;
    }

    /**
     * Reads an object from a file, optionally decompressing it with GZIP.
     *
     * @param file the file to read from.
     * @param zip  whether the file is compressed with GZIP.
     * @return the deserialized object.
     * @throws IOException            if file operations fail.
     * @throws ClassNotFoundException if deserialization fails.
     */
    private Object readFromFile(File file, boolean zip) throws IOException, ClassNotFoundException {
        log.debug("readFromFile[0]: Reading from file: {}", file.getName());
        try (InputStream fis = new FileInputStream(file);
             InputStream is = zip ? new GZIPInputStream(fis) : fis;
             ObjectInputStream ois = new ObjectInputStream(is)) {
            return ois.readObject();
        }
    }

    /**
     * Writes an object to a file, optionally compressing it with GZIP.
     *
     * @param file the file to write to.
     * @param data the object to be written.
     * @param zip  whether to compress the data with GZIP.
     * @throws NonSerializableDataException if the object is not serializable.
     * @throws IOException                  if file operations fail.
     */
    private void writeToFile(File file, Object data, boolean zip) throws NonSerializableDataException, IOException {
        log.debug("writeToFile[0]: Writing data to file: {}", file.getName());
        if (!(data instanceof Serializable)) {
            throw new NonSerializableDataException(Constants.NON_SERIALIZABLE_DATA_MESSAGE);
        }

        try (OutputStream fos = new FileOutputStream(file);
             OutputStream os = zip ? new GZIPOutputStream(fos) : fos;
             ObjectOutputStream oos = new ObjectOutputStream(os)) {
            oos.writeObject(data);
        }
    }


}
