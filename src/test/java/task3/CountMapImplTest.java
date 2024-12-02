package task3;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import ru.sberSchool.tasks.task3.CountMap;
import ru.sberSchool.tasks.task3.CountMapImpl;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class CountMapImplTest {

    @Test
    public void testAdd() {
        log.debug("testAdd[0]: Начало теста");

        CountMap<String> countMap = new CountMapImpl<>();

        countMap.add("one");
        countMap.add("one");
        countMap.add("two");

        log.debug("testAdd[1]: Результат после добавления элементов: {}", countMap.toMap());

        assertEquals(2, countMap.getCount("one"), "Количество 'one' должно быть 2");
        assertEquals(1, countMap.getCount("two"), "Количество 'two' должно быть 1");

        log.debug("testAdd[2]: Тест завершен");
    }

    @Test
    public void testGetCount() {
        log.debug("testGetCount[0]: Начало теста");

        CountMap<String> countMap = new CountMapImpl<>();

        countMap.add("one");
        countMap.add("two");
        countMap.add("two");

        log.debug("testGetCount[1]: Результат getCount('one'): {}", countMap.getCount("one"));
        log.debug("testGetCount[2]: Результат getCount('two'): {}", countMap.getCount("two"));

        assertEquals(1, countMap.getCount("one"), "Количество 'one' должно быть 1");
        assertEquals(2, countMap.getCount("two"), "Количество 'two' должно быть 2");

        log.debug("testGetCount[3]: Тест завершен");
    }

    @Test
    public void testRemove() {
        log.debug("testRemove[0]: Начало теста");

        CountMap<String> countMap = new CountMapImpl<>();
        countMap.add("one");
        countMap.add("one");
        countMap.add("two");

        log.debug("testRemove[1]: Результат перед удалением элемента: {}", countMap.toMap());

        // Удаляем элемент
        int removedCount = countMap.remove("one");

        log.debug("testRemove[2]: Количество удалённых элементов: {}", removedCount);
        log.debug("testRemove[3]: Результат после удаления элемента: {}", countMap.toMap());

        assertEquals(2, removedCount, "Количество удалённых элементов должно быть 2");
        assertEquals(0, countMap.getCount("one"), "Количество 'one' должно быть 0 после удаления");
        assertEquals(1, countMap.getCount("two"), "Количество 'two' должно остаться 1");

        log.debug("testRemove[4]: Тест завершен");
    }

    @Test
    public void testSize() {
        log.debug("testSize[0]: Начало теста");

        CountMap<String> countMap = new CountMapImpl<>();

        countMap.add("one");
        countMap.add("two");
        countMap.add("two");

        log.debug("testSize[1]: Размер CountMap перед проверкой: {}", countMap.size());

        assertEquals(2, countMap.size(), "Размер CountMap должен быть 2");

        log.debug("testSize[2]: Тест завершен");
    }

    @Test
    public void testAddAll() {
        log.debug("testAddAll[0]: Начало теста");

        CountMap<String> countMap1 = new CountMapImpl<>();
        CountMap<String> countMap2 = new CountMapImpl<>();

        countMap1.add("one");
        countMap1.add("two");

        countMap2.add("one");
        countMap2.add("one");
        countMap2.add("cherry");

        log.debug("testAddAll[1]: Результат до объединения: {}", countMap1.toMap());
        log.debug("testAddAll[2]: Результат второго CountMap: {}", countMap2.toMap());

        // Объединяем countMap2 в countMap1
        countMap1.addAll(countMap2);

        log.debug("testAddAll[3]: Результат после объединения: {}", countMap1.toMap());

        assertEquals(3, countMap1.getCount("one"), "Количество 'one' должно быть 3");
        assertEquals(1, countMap1.getCount("two"), "Количество 'two' должно быть 1");
        assertEquals(1, countMap1.getCount("cherry"), "Количество 'cherry' должно быть 1");

        log.debug("testAddAll[4]: Тест завершен");
    }

    @Test
    public void testToMap() {
        log.debug("testToMap[0]: Начало теста");

        CountMap<String> countMap = new CountMapImpl<>();
        countMap.add("one");
        countMap.add("two");
        countMap.add("two");

        Map<String, Integer> map = countMap.toMap();

        log.debug("testToMap[1]: Результат toMap: {}", map);

        assertEquals(2, map.size(), "Размер карты должен быть 2");
        assertEquals(1, map.get("one"), "Количество 'one' должно быть 1");
        assertEquals(2, map.get("two"), "Количество 'two' должно быть 2");

        log.debug("testToMap[2]: Тест завершен");
    }

    @Test
    public void testToMapWithDestination() {
        log.debug("testToMapWithDestination[0]: Начало теста");

        CountMap<String> countMap = new CountMapImpl<>();
        countMap.add("one");
        countMap.add("two");
        countMap.add("two");

        Map<String, Integer> destinationMap = new HashMap<>();
        countMap.toMap(destinationMap);

        log.debug("testToMapWithDestination[1]: Результат после копирования в destinationMap: {}", destinationMap);

        assertEquals(2, destinationMap.size(), "Размер карты должен быть 2");
        assertEquals(1, destinationMap.get("one"), "Количество 'one' должно быть 1");
        assertEquals(2, destinationMap.get("two"), "Количество 'two' должно быть 2");

        log.debug("testToMapWithDestination[2]: Тест завершен");
    }
}
