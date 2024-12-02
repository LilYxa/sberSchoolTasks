package ru.sberSchool.tasks.task5.service;

import lombok.extern.slf4j.Slf4j;
import ru.sberSchool.tasks.task5.annotations.Cache;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * The {@code CachingProxy} class provides a proxy implementation that intercepts method calls
 * on an interface and adds caching functionality for methods annotated with the {@link Cache} annotation.
 *
 * @see Cache
 * @see Proxy
 *
 * @author Elland Ilia
 * @version 1.0
 */
@Slf4j
public class CachingProxy {

    /**
     * Creates a proxy instance for the provided target object, adding caching functionality
     * for methods annotated with {@code @Cache}.
     *
     * @param interfaceType the interface type that the proxy should implement
     * @param target the target object to which the proxy will delegate method calls
     * @param <T> the type of the target object, which must implement the provided interface
     * @return a proxy instance that implements the provided interface and adds caching behavior
     */
    public static <T> T createProxy(Class<T> interfaceType, T target) {
        log.debug("createProxy[0]: creating proxy for target: {}", target);
        Map<String, Object> cache = new HashMap<>();

        Object proxyInstance = Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                new Class<?>[]{interfaceType},
                (proxy, method, args) -> {
                    // Проверяем, есть ли аннотация @Cache
                    if (method.isAnnotationPresent(Cache.class)) {
                        String key = method.getName() + "(" + (args != null ? args[0] : "") + ")";
                        if (!cache.containsKey(key)) {
                            Object result = method.invoke(target, args);
                            cache.put(key, result);
                            return result;
                        }
                        return cache.get(key);
                    } else {
                        // Если аннотации нет, просто вызываем метод
                        return method.invoke(target, args);
                    }
                }
        );

        // Используем cast() для проверки
        return interfaceType.cast(proxyInstance);
    }
}
