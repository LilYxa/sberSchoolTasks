package ru.sberSchool.tasks.task6;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class ObjectArrayIteratorTest {

    private static final Logger log = LoggerFactory.getLogger(ObjectArrayIteratorTest.class);

    @Test
    void testIterationOverArray() {
        log.debug("testIterationOverArray[0]: start test");
        String[] testArray = {"A", "B", "C"};
        ObjectArrayIterator<String> iterator = new ObjectArrayIterator<>(testArray);

        assertTrue(iterator.hasNext());
        assertEquals("A", iterator.next());
        log.debug("testIterationOverArray[1]: First element: {}", "A");

        assertTrue(iterator.hasNext());
        assertEquals("B", iterator.next());
        log.debug("testIterationOverArray[2]: Second element: {}", "B");

        assertTrue(iterator.hasNext());
        assertEquals("C", iterator.next());
        log.debug("testIterationOverArray[3]: Third element: {}", "C");

        assertFalse(iterator.hasNext());
        log.debug("testIterationOverArray[4]: No more elements expected");
    }

    @Test
    void testEmptyArray() {
        log.debug("testEmptyArray[0]: start test");
        String[] testArray = {};
        ObjectArrayIterator<String> iterator = new ObjectArrayIterator<>(testArray);

        assertFalse(iterator.hasNext());
        log.debug("testEmptyArray[1]: No elements in the array");
    }

    @Test
    void testNextThrowsExceptionWhenNoElements() {
        log.debug("testNextThrowsExceptionWhenNoElements[0]: start test");
        String[] testArray = {};
        ObjectArrayIterator<String> iterator = new ObjectArrayIterator<>(testArray);

        assertThrows(NoSuchElementException.class, iterator::next);
        log.debug("testNextThrowsExceptionWhenNoElements[1]: Exception was thrown as expected");
    }

    @Test
    void testNullArrayThrowsException() {
        log.debug("testNullArrayThrowsException[0]: start test");
        assertThrows(IllegalArgumentException.class, () -> new ObjectArrayIterator<>(null));
        log.debug("testNullArrayThrowsException[1]: Exception was thrown as expected for null array");
    }
}
