package ru.sberSchool.tasks.task2;

import lombok.extern.slf4j.Slf4j;
import ru.sberSchool.tasks.Constants;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * A class to compare the performance of {@link ArrayList} and {@link LinkedList}
 * for various operations, including insertion, deletion, and iteration.
 *
 * @author Elland Ilia
 * @version 1.0
 */
@Slf4j
public class ListPerformanceComparison {

    public static void main(String[] args) {
        compareLists(new ArrayList<>(), new LinkedList<>(), 1000000);
    }

    /**
     * Compares the performance of {@link ArrayList} and {@link LinkedList} for various operations.
     *
     * @param arrayList an instance of {@link ArrayList} to compare.
     * @param linkedList an instance of {@link LinkedList} to compare.
     * @param size the number of elements to fill in the lists initially.
     */
    private static void compareLists(List<Integer> arrayList, List<Integer> linkedList, int size) {
        log.info("compareLists[0]: Сравнение ArrayList и LinkedList");

        log.info("compareLists[1]: Заполнение ArrayList");
        long arrayListFillTime = measureTime(() -> fillList(arrayList, size));
        log.info("compareLists[2]: Время заполнения: {} {}", arrayListFillTime, Constants.MILLISECOND);

        log.info("compareLists[3]: Заполнение LinkedList");
        long linkedListFillTime = measureTime(() -> fillList(linkedList, size));
        log.info("compareLists[4]: Время заполнения: {} {}", linkedListFillTime, Constants.MILLISECOND);

        log.info("compareLists[5]: Добавление в начало ArrayList");
        long arrayListAddStartTime = measureTime(() -> addToStart(arrayList, 1000));
        log.info("compareLists[6]: Время добавления: {} {}", arrayListAddStartTime, Constants.MILLISECOND);

        log.info("compareLists[7]: Добавление в начало LinkedList");
        long linkedListAddStartTime = measureTime(() -> addToStart(linkedList, 1000));
        log.info("compareLists[8]: Время добавления: {} {}", linkedListAddStartTime, Constants.MILLISECOND);

        log.info("compareLists[9]: Добавление в середину ArrayList:");
        long arrayListAddMiddleTime = measureTime(() -> addToMiddle(arrayList, 1000));
        log.info("compareLists[10]: Время добавления: {} {}", arrayListAddMiddleTime, Constants.MILLISECOND);

        log.info("compareLists[11]: Добавление в середину LinkedList:");
        long linkedListAddMiddleTime = measureTime(() -> addToMiddle(linkedList, 1000));
        log.info("compareLists[12]: Время добавления: {} {}", linkedListAddMiddleTime, Constants.MILLISECOND);

        log.info("compareLists[13]: Удаление из начала ArrayList:");
        long arrayListRemoveStartTime = measureTime(() -> removeFromStart(arrayList, 1000));
        log.info("compareLists[14]: Время удаления: {} {}", arrayListRemoveStartTime, Constants.MILLISECOND);

        log.info("compareLists[15]: Удаление из начала LinkedList:");
        long linkedListRemoveStartTime = measureTime(() -> removeFromStart(linkedList, 1000));
        log.info("compareLists[16]: Время удаления: {} {}", linkedListRemoveStartTime, Constants.MILLISECOND);

        log.info("compareLists[17]: Удаление из конца ArrayList:");
        long arrayListRemoveEndTime = measureTime(() -> removeFromEnd(arrayList, 1000));
        log.info("compareLists[18]: Время удаления: {} {}", arrayListRemoveEndTime, Constants.MILLISECOND);

        log.info("compareLists[19]: Удаление из конца LinkedList:");
        long linkedListRemoveEndTime = measureTime(() -> removeFromEnd(linkedList, 1000));
        log.info("compareLists[20]: Время удаления: {} {}", linkedListRemoveEndTime, Constants.MILLISECOND);

        log.info("compareLists[21]: Итерация по ArrayList:");
        long arrayListIterationTime = measureTime(() -> iterateList(arrayList));
        log.info("compareLists[22]: Время итерации: {} {}", arrayListIterationTime, Constants.MILLISECOND);

        log.info("compareLists[23]: Итерация по LinkedList:");
        long linkedListIterationTime = measureTime(() -> iterateList(linkedList));
        log.info("compareLists[24]: Время итерации: {} {}", linkedListIterationTime, Constants.MILLISECOND);
    }

    /**
     * Fills a list with a specified number of elements.
     *
     * @param list the list to fill.
     * @param size the number of elements to add.
     */
    private static void fillList(List<Integer> list, int size) {
        for (int i = 0; i < size; i++) {
            list.add(i);
        }
    }

    /**
     * Adds a specified number of elements to the start of a list.
     *
     * @param list the list to modify.
     * @param count the number of elements to add.
     */
    private static void addToStart(List<Integer> list, int count) {
        for (int i = 0; i < count; i++) {
            list.add(0, -1);
        }
    }

    /**
     * Adds a specified number of elements to the middle of a list.
     *
     * @param list the list to modify.
     * @param count the number of elements to add.
     */
    private static void addToMiddle(List<Integer> list, int count) {
        int middle = list.size() / 2;
        for (int i = 0; i < count; i++) {
            list.add(middle, -1);
        }
    }

    /**
     * Removes a specified number of elements from the start of a list.
     *
     * @param list the list to modify.
     * @param count the number of elements to remove.
     */
    private static void removeFromStart(List<Integer> list, int count) {
        for (int i = 0; i < count; i++) {
            list.remove(0);
        }
    }

    /**
     * Removes a specified number of elements from the end of a list.
     *
     * @param list the list to modify.
     * @param count the number of elements to remove.
     */
    private static void removeFromEnd(List<Integer> list, int count) {
        for (int i = 0; i < count; i++) {
            list.remove(list.size() - 1);
        }
    }

    /**
     * Iterates through a list using a {@link ListIterator}.
     *
     * @param list the list to iterate through.
     */
    private static void iterateList(List<Integer> list) {
        ListIterator<Integer> iterator = list.listIterator();
        while (iterator.hasNext()) {
            iterator.next();
        }
    }

    /**
     * Measures the time taken to execute a task.
     *
     * @param task the {@link Runnable} task to execute.
     * @return the time taken in milliseconds.
     */
    private static long measureTime(Runnable task) {
        long start = System.currentTimeMillis();
        task.run();
        return System.currentTimeMillis() - start;
    }
}
