package ru.sberSchool.tasks.task8.proxy;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A proxy class for enabling caching functionality on service methods.
 *
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * CacheProxy cacheProxy = new CacheProxy("cache/directory");
 * MyService serviceWithCache = cacheProxy.cache(new MyServiceImpl());
 * serviceWithCache.someMethod(); // Caching logic will be applied
 * }
 * </pre>
 *
 * @author Elland Ilia
 */
@Getter
@Setter
@Slf4j
public class CacheProxy {

    private final String rootPath;
    private final Map<String, Object> defaultCache = new ConcurrentHashMap<>();

    public CacheProxy(String rootPath) {
        this.rootPath = rootPath;
    }

    /**
     * Creates a proxy for the specified service object, applying caching logic.
     *
     * @param service the service object to be proxied.
     * @param <T>     the type of the service object.
     * @return a proxied instance of the service object with caching functionality.
     */
    public <T> T cache(T service) {
        log.debug("cache[0]: Creating proxy for {}", service.getClass().getName());
        return (T) Proxy.newProxyInstance(
                service.getClass().getClassLoader(),
                service.getClass().getInterfaces(),
                new CacheInvocationHandler(service, rootPath, defaultCache)
        );
    }
}
