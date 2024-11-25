package ru.sberSchool.tasks.task3;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Task3 {

    public static void main(String[] args) {
        log.debug("main[0]: main method from Task3");
        CountMap<Integer> countMap = new CountMapImpl<>();

        log.debug("main[1]: Добавляем элементы 10, 10, 5, 6, 5, 10");
        countMap.add(10);
        countMap.add(10);
        countMap.add(5);
        countMap.add(6);
        countMap.add(5);
        countMap.add(10);

        log.debug("main[2]: Ожидаемое количество для 10: 3, Полученное: {}", countMap.getCount(10));
        log.debug("main[3]: Ожидаемое количество для 5: 2, Полученное: {}", countMap.getCount(5));
        log.debug("main[4]: Ожидаемое количество для 6: 1, Полученное: {}", countMap.getCount(6));

        log.debug("main[5]: Удаляем элемент 10");
        int removedCount = countMap.remove(10);
        log.debug("main[6]: Ожидаемое количество удалений для 10: 3, Полученное: {}", removedCount);
        log.debug("main[7]: Ожидаемое количество для 10 после удаления: 0, Полученное: {}", countMap.getCount(10));

        log.debug("main[8]: Ожидаемое количество уникальных элементов: 2, Полученное: {}", countMap.size());

        log.debug("main[9]: Создаем новую CountMap с элементами 5, 7, 7 и добавляем её в текущую CountMap");
        CountMap<Integer> anotherMap = new CountMapImpl<>();
        anotherMap.add(5);
        anotherMap.add(7);
        anotherMap.add(7);
        countMap.addAll(anotherMap);

        log.debug("main[10]: Ожидаемое количество для 5: 3, Полученное: {}", countMap.getCount(5));
        log.debug("main[11]: Ожидаемое количество для 7: 2, Полученное: {}", countMap.getCount(7));

        log.debug("main[12]: Преобразуем Map в CountMap");
        Map<Integer, Integer> mapRepresentation = countMap.toMap();
        log.debug("main[13]: Ожидаемая карта: {5=3, 6=1, 7=2}, Полученная: {}", mapRepresentation);

        log.debug("main[14]: Передаем данные в новую карту");
        Map<Integer, Integer> destination = new HashMap<>();
        countMap.toMap(destination);
        log.debug("main[15]: Ожидаемая карта: {5=3, 6=1, 7=2}, Полученная: {}", destination);

    }
}
