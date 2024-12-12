package ru.sberSchool.tasks.task5.utils;

import lombok.extern.slf4j.Slf4j;
import ru.sberSchool.tasks.Constants;
import ru.sberSchool.tasks.task5.exceptions.FieldAccessException;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 * The {@code ReflectionUtils} class provides utility methods for inspecting and interacting with
 * Java classes and their metadata using reflection.
 *
 * @author Elland Ilia
 * @version 1.0
 */
@Slf4j
public class ReflectionUtils {

    /**
     * Prints all methods of the specified class, including public methods inherited
     * from superclasses and declared methods within the class itself.
     *
     * @param clazz the class whose methods will be printed
     */
    public static void printAllMethods(Class<?> clazz) {
        log.info("printAllMethods[0]: print all methods for class: {}", clazz);
        // Включает методы родителя
        Method[] methods = clazz.getMethods();
        Method[] declaredMethods = clazz.getDeclaredMethods();

        log.info("printAllMethods[1]: public methods:");
        Arrays.stream(methods)
                        .forEach(System.out::println);

        log.info("printAllMethods[1]: declared methods (including private):");
        Arrays.stream(declaredMethods)
                .forEach(System.out::println);
    }

    /**
     * Prints all getter methods of the specified class.
     *
     * @param clazz the class whose getter methods will be printed
     */
    public static void printAllGetters(Class<?> clazz) {
        log.info("printAllGetters[0]: print getters for class: {}", clazz);
        Arrays.stream(clazz.getDeclaredMethods())
            // Оставляем только публичные методы
            .filter(method -> Modifier.isPublic(method.getModifiers()))
            // Проверяем, начинается ли имя метода с "get"
            .filter(method -> method.getName().startsWith(Constants.GET_PREFIX))
            // Проверяем, что у метода нет параметров
            .filter(method -> method.getParameterCount() == 0)
            // Проверяем, что метод возвращает не void
            .filter(method -> !method.getReturnType().equals(void.class))
            // Печатаем геттеры
            .forEach(method -> System.out.println("Getter: " + method));
    }

    /**
     * Checks all string constants in the specified class to ensure their names match their values.
     * <p>If a mismatch is found between the field name and its value, a warning is logged. Otherwise, a match message is logged.</p>
     *
     * @param clazz the class whose string constants will be checked
     * @throws ru.sberSchool.tasks.task5.exceptions.FieldAccessException if a field's value cannot be accessed
     */
    public static void checkStringConstants(Class<?> clazz) {
        log.info("checkStringConstants[0]: checking class: {}", clazz);
        Arrays.stream(clazz.getDeclaredFields())
            // Оставляем только статические поля
            .filter(field -> Modifier.isStatic(field.getModifiers()))
            // Оставляем только финальные поля
            .filter(field -> Modifier.isFinal(field.getModifiers()))
            // Оставляем только поля типа String
            .filter(field -> field.getType().equals(String.class))
            .forEach(field -> {
                try {
                    // Получаем значение поля
                    String value = (String) field.get(null);
                    if (!field.getName().equals(value)) {
                        log.info("checkStringConstants[1]: mismatch: {} != {}", field.getName(), value);
                    } else {
                        log.info("checkStringConstants[2]: match: {}", field.getName());
                    }
                } catch (IllegalAccessException e) {
                    throw new FieldAccessException(String.format(Constants.FAILED_TO_ACCESS_FIELD, field.getName()));
                }
            });
    }
}
