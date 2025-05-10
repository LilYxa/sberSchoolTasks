package ru.sberSchool.tasks.task14.util;

import lombok.extern.slf4j.Slf4j;
import ru.sberSchool.tasks.task14.annotations.Cachable;
import ru.sberSchool.tasks.task14.service.DataSource;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The {@code CalculatorProxy} class is a proxy implementation for the {@link Calculator} interface.
 * It intercepts method calls to the {@link Calculator} and handles caching based on the {@link Cachable} annotation.
 *
 * @author Elland Ilia
 */
@Slf4j
public class CalculatorProxy implements InvocationHandler {
    private final Calculator target;
    private final ConcurrentHashMap<Method, DataSource> sourceCache = new ConcurrentHashMap<>();

    public CalculatorProxy(Calculator target) {
        this.target = target;
    }

    /**
     * Creates a proxy instance for the specified {@code Calculator} using the {@code Proxy.newProxyInstance} method.
     * The proxy intercepts method calls and manages caching of results based on the {@link Cachable} annotation.
     *
     * @param target the target {@code Calculator} to proxy
     * @return a proxy instance of the {@code Calculator} interface
     */
    @SuppressWarnings("unchecked")
    public static Calculator createProxy(Calculator target) {
        return (Calculator) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                new Class<?>[]{Calculator.class},
                new CalculatorProxy(target)
        );
    }

    /**
     * Intercepts method invocations and manages caching of results based on the {@link Cachable} annotation.
     * If caching is enabled, the result is either fetched from the cache or calculated and then cached.
     *
     * @param proxy the proxy instance
     * @param method the method being invoked
     * @param args the arguments passed to the method
     * @return the result of the method invocation
     * @throws Throwable if an error occurs during method invocation or caching
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Cachable cachable = method.getAnnotation(Cachable.class);
        if (cachable != null) {
            DataSource source = sourceCache.computeIfAbsent(method, m -> {
                try {
                    DataSource src = cachable.value().getConstructor().newInstance();
                    src.init();
                    return src;
                } catch (Exception e) {
                    throw new RuntimeException("Unable to initialize source", e);
                }
            });

            String key = method.getName() + "(" + args[0] + ")";
            String cachedValue = source.get(key);
            if (cachedValue != null) {
                log.debug("invoke[0]: Returning cached result for key = {}", key);
                return deserialize(cachedValue);
            }

            Object result = method.invoke(target, args);
            source.save(key, serialize(result));
            log.debug("invoke[1]: Result saved to cache for key = {}", key);
            return result;
        }
        return method.invoke(target, args);
    }

    /**
     * Serializes the given object to a string representation.
     *
     * @param obj the object to serialize
     * @return the string representation of the object
     */
    private String serialize(Object obj) {
        return obj.toString();
    }

    /**
     * Deserializes the given string into a list of integers.
     *
     * @param input the string representation of a list of integers
     * @return a {@code List<Integer>} representing the deserialized data
     * @throws IllegalArgumentException if the input string is invalid or empty
     */
    private List<Integer> deserialize(String input) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("Input cannot be null or empty");
        }

        // Удаляем квадратные скобки
        input = input.trim().replaceAll("[\\[\\]]", "");

        // Разделяем по запятой и пробелу
        String[] parts = input.split(",\\s*");

        // Преобразуем строки в числа
        List<Integer> result = new ArrayList<>();
        try {
            for (String part : parts) {
                result.add(Integer.parseInt(part));
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number in input: " + input, e);
        }

        return result;
    }
}
