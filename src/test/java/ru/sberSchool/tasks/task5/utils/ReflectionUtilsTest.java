package ru.sberSchool.tasks.task5.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.sberSchool.tasks.task5.TestClass;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class ReflectionUtilsTest {

    @BeforeEach
    public void setUp() {
        TestClass testClass = new TestClass();
    }

    @Test
    void testPrintAllMethods() {
        log.debug("testPrintAllMethods[0]: start test");
        ReflectionUtils.printAllMethods(TestClass.class);

        Method[] methods = TestClass.class.getMethods();
        Method[] declaredMethods = TestClass.class.getDeclaredMethods();

        List<String> methodNames = new ArrayList<>();
        for (Method method : methods) {
            methodNames.add(method.getName());
        }

        assertTrue(methodNames.contains("getPublicField"));
        assertTrue(methodNames.contains("getAnotherField"));

        List<String> declaredMethodNames = new ArrayList<>();
        for (Method method : declaredMethods) {
            declaredMethodNames.add(method.getName());
        }

        assertTrue(declaredMethodNames.contains("getPrivateField"));
    }

    @Test
    void testPrintAllGetters() {
        log.debug("testPrintAllGetters[0]: start test");
        ReflectionUtils.printAllGetters(TestClass.class);

        Method[] declaredMethods = TestClass.class.getDeclaredMethods();
        long getterCount = Arrays.stream(declaredMethods)
                .filter(method -> method.getName().startsWith("get"))
                .filter(method -> method.getParameterCount() == 0)
                .filter(method -> !method.getReturnType().equals(void.class))
                .count();

        assertEquals(3, getterCount);
    }

    @Test
    void testCheckStringConstants() {
        log.debug("testCheckStringConstants[0]: start test");

        ReflectionUtils.checkStringConstants(TestClass.class);

        assertDoesNotThrow(() -> {
            Field constantMatch = TestClass.class.getField("CONSTANT_MATCH");
            assertEquals("CONSTANT_MATCH", constantMatch.get(null));
        });

        assertDoesNotThrow(() -> {
            Field constantMismatch = TestClass.class.getField("CONSTANT_MISMATCH");
            assertNotEquals("CONSTANT_MISMATCH", constantMismatch.get(null));
        });
    }
}
