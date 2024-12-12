package ru.sberSchool.tasks.task3;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import ru.sberSchool.tasks.task3.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class CollectionUtilsTest {

    @Test
    public void testAddAll() {
        log.debug("testAddAll[0]: Начало теста");

        List<Integer> source = Arrays.asList(1, 2, 3);
        List<Number> destination = new ArrayList<>(Arrays.asList(4, 5));

        CollectionUtils.addAll(source, destination);
        log.debug("testAddAll[1]: Результат addAll: {}", destination);

        List<Number> expected = Arrays.asList(4, 5, 1, 2, 3);
        assertEquals(expected, destination);

        log.debug("testAddAll[2]: Тест завершен");
    }

    @Test
    public void testNewArrayList() {
        log.debug("testNewArrayList[0]: Начало теста");

        List<Integer> list = CollectionUtils.newArrayList();
        list.add(1);
        list.add(2);
        list.add(3);
        log.debug("testNewArrayList[1]: Результат newArrayList: {}", list);

        List<Integer> expected = Arrays.asList(1, 2, 3);
        assertEquals(expected, list);

        log.debug("testNewArrayList[2]: Тест завершен");
    }

    @Test
    public void testIndexOf() {
        log.debug("testIndexOf[0]: Начало теста");

        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        int index = CollectionUtils.indexOf(list, 3);
        log.debug("testIndexOf[1]: Результат indexOf: {}", index);

        assertEquals(2, index);

        log.debug("testIndexOf[2]: Тест завершен");
    }

    @Test
    public void testLimit() {
        log.debug("testLimit[0]: Начало теста");
        List<Integer> source = Arrays.asList(1, 2, 3, 4, 5);

        // Тест 1: Ограничиваем до 3 элементов
        List<Integer> result = CollectionUtils.limit(source, 3);
        log.debug("testLimit[1]: Ограничиваем до 3 элементов. Результат: {}", result);
        assertEquals(3, result.size(), "Размер списка должен быть 3");
        assertEquals(Arrays.asList(1, 2, 3), result, "Список должен содержать первые три элемента");

        // Тест 2: Ограничиваем до 5 элементов (все элементы)
        result = CollectionUtils.limit(source, 5);
        log.debug("testLimit[2]: Ограничиваем до 5 элементов (все элементы). Результат: {}", result);
        assertEquals(5, result.size(), "Размер списка должен быть 5");
        assertEquals(Arrays.asList(1, 2, 3, 4, 5), result, "Список должен содержать все элементы");

        // Тест 3: Ограничиваем до 10 элементов (больше чем в исходном списке)
        result = CollectionUtils.limit(source, 10);
        log.debug("testLimit[3]: Ограничиваем до 10 элементов (больше чем в списке). Результат: {}", result);
        assertEquals(5, result.size(), "Размер списка должен быть равен количеству элементов в исходном списке");
        assertEquals(Arrays.asList(1, 2, 3, 4, 5), result, "Список должен содержать все элементы");

        // Тест 4: Ограничиваем до 0 элементов
        result = CollectionUtils.limit(source, 0);
        log.debug("testLimit[4]: Ограничиваем до 0 элементов. Результат: {}", result);
        assertEquals(0, result.size(), "Размер списка должен быть 0");
        assertTrue(result.isEmpty(), "Список должен быть пустым");

        log.debug("testLimit[5]: Тест завершен");
    }

    @Test
    public void testAdd() {
        log.debug("testAdd[0]: Начало теста");
        // Исходный список
        List<Number> source = new ArrayList<>();

        // Тест 1: Добавляем элемент типа Integer
        CollectionUtils.add(source, 1);
        log.debug("testAdd[1]: Добавляем элемент типа Integer. Результат: {}", source);
        assertEquals(1, source.size(), "Размер списка должен быть 1");
        assertEquals(Arrays.asList(1), source, "Список должен содержать элемент 1");

        // Тест 2: Добавляем элемент типа Double
        CollectionUtils.add(source, 2.5);
        log.debug("testAdd[2]: Добавляем элемент типа Double. Результат: {}", source);
        assertEquals(2, source.size(), "Размер списка должен быть 2");
        assertEquals(Arrays.asList(1, 2.5), source, "Список должен содержать элементы 1 и 2.5");

        // Тест 3: Добавляем элемент типа Float
        CollectionUtils.add(source, 3.14f);
        log.debug("testAdd[3]: Добавляем элемент типа Float. Результат: {}", source);
        assertEquals(3, source.size(), "Размер списка должен быть 3");
        assertEquals(Arrays.asList(1, 2.5, 3.14f), source, "Список должен содержать элементы 1, 2.5 и 3.14f");

        // Тест 4: Добавляем элемент типа Long
        CollectionUtils.add(source, 100L);
        log.debug("testAdd[4]: Добавляем элемент типа Long. Результат: {}", source);
        assertEquals(4, source.size(), "Размер списка должен быть 4");
        assertEquals(Arrays.asList(1, 2.5, 3.14f, 100L), source, "Список должен содержать элементы 1, 2.5, 3.14f и 100L");

        log.debug("testAdd[5]: Тест завершен");
    }

    @Test
    public void testRemoveAll() {
        log.debug("testRemoveAll[0]: Начало теста");

        List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> list2 = Arrays.asList(2, 4);
        CollectionUtils.removeAll(list1, list2);
        log.debug("testRemoveAll[1]: Результат removeAll: {}", list1);

        List<Integer> expected = Arrays.asList(1, 3, 5);
        assertEquals(expected, list1);

        log.debug("testRemoveAll[2]: Тест завершен");
    }

    @Test
    public void testContainsAll() {
        log.debug("testContainsAll[0]: Начало теста");

        List<Integer> list1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> list2 = Arrays.asList(2, 3, 4);
        boolean result = CollectionUtils.containsAll(list1, list2);
        log.debug("testContainsAll[1]: Результат containsAll: {}", result);

        assertTrue(result);

        log.debug("testContainsAll[2]: Тест завершен");
    }

    @Test
    public void testContainsAny() {
        log.debug("testContainsAny[0]: Начало теста");

        List<Integer> list1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> list2 = Arrays.asList(6, 7, 8);
        boolean result = CollectionUtils.containsAny(list1, list2);
        log.debug("testContainsAny[1]: Результат containsAny: {}", result);

        assertFalse(result);

        list2 = Arrays.asList(4, 7, 8);
        result = CollectionUtils.containsAny(list1, list2);
        log.debug("testContainsAny[2]: Результат containsAny с измененным list2: {}", result);

        assertTrue(result);

        log.debug("testContainsAny[3]: Тест завершен");
    }

    @Test
    public void testRangeComparable() {
        log.debug("testRangeComparable[0]: Начало теста");

        List<Integer> inputList = Arrays.asList(8, 1, 3, 5, 6, 4);
        List<Integer> result = CollectionUtils.range(inputList, 3, 6);
        log.debug("testRangeComparable[1]: Результат range с Comparable: {}", result);

        List<Integer> expected = Arrays.asList(3, 4, 5, 6);
        assertEquals(expected, result);

        log.debug("testRangeComparable[2]: Тест завершен");
    }

    @Test
    public void testRangeComparator() {
        log.debug("testRangeComparator[0]: Начало теста");

        List<Integer> inputList = Arrays.asList(8, 1, 3, 5, 6, 4);
        Comparator<Integer> comparator = Integer::compareTo;
        List<Integer> result = CollectionUtils.range(inputList, 3, 6, comparator);
        log.debug("testRangeComparator[1]: Результат range с Comparator: {}", result);

        List<Integer> expected = Arrays.asList(3, 4, 5, 6);
        assertEquals(expected, result);

        log.debug("testRangeComparator[2]: Тест завершен");
    }
}

