package ru.sberSchool.tasks.task5.utils;

import lombok.extern.slf4j.Slf4j;
import ru.sberSchool.tasks.Constants;
import ru.sberSchool.tasks.task5.exceptions.AssignException;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

/**
 * The {@code BeanUtils} class provides utility methods for object property copying.
 *
 * @author Elland Ilia
 * @version 1.0
 */
@Slf4j
public class BeanUtils {

    /**
     * Copies properties from one object to another by matching getter methods in the source object
     * to setter methods in the target object.
     *
     * @param to the target object to which properties will be assigned
     * @param from the source object from which properties will be copied
     * @throws AssignException if an error occurs during property assignment
     */
    public static void assign(Object to, Object from) {
        log.debug("assign[0]: from {} to {}", from, to);
        Arrays.stream(from.getClass().getMethods())
            .filter(fromMethod -> fromMethod.getName().startsWith(Constants.GET_PREFIX) && fromMethod.getParameterCount() == 0)
            .forEach(fromMethod -> {
                String propertyName = fromMethod.getName().substring(3);
                Class<?> returnType = fromMethod.getReturnType();

                Optional<Method> matchingSetter = Arrays.stream(to.getClass().getMethods())
                        .filter(toMethod -> toMethod.getName().equals(Constants.SET_PREFIX + propertyName))
                        .filter(toMethod -> toMethod.getParameterCount() == 1)
                        .filter(toMethod -> toMethod.getParameterTypes()[0].isAssignableFrom(returnType))
                        .findFirst();

                matchingSetter.ifPresent(toMethod -> {
                    try {
                        Object value = fromMethod.invoke(from);
                        toMethod.invoke(to, value);
                    } catch (Exception e) {
                        log.error("assign[0]: error: {}", e.getMessage());
                        throw new AssignException(Constants.ASSIGN_EXCEPTION_MESSAGE);
                    }
                });
            });
    }
}
