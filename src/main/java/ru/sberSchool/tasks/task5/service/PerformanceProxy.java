package ru.sberSchool.tasks.task5.service;

import ru.sberSchool.tasks.task5.annotations.Metric;

import java.lang.reflect.Proxy;

/**
 * The {@code PerformanceProxy} class provides a proxy implementation that measures and logs
 * the execution time of methods annotated with the {@link Metric} annotation.
 *
 * @see Metric
 * @see Proxy
 *
 * @author Elland Ilia
 * @version 1.0
 */
public class PerformanceProxy {

    /**
     * Creates a proxy instance for the provided target object, measuring the execution time
     * of methods annotated with {@code @Metric}.
     *
     * @param interfaceType the interface type that the proxy should implement
     * @param target the target object to which the proxy will delegate method calls
     * @param <T> the type of the target object, which must implement the provided interface
     * @return a proxy instance that implements the provided interface and measures execution time for annotated methods
     */
    public static <T> T createProxy(Class<T> interfaceType, T target) {
        return interfaceType.cast(Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                new Class<?>[]{interfaceType},
                (proxy, method, args) -> {
                    if (method.isAnnotationPresent(Metric.class)) {
                        long startTime = System.nanoTime();
                        Object result = method.invoke(target, args);
                        long endTime = System.nanoTime();
                        System.out.println("Время работы метода: " + (endTime - startTime) + " нс");
                        return result;
                    } else {
                        return method.invoke(target, args);
                    }
                }
        ));
    }

}
