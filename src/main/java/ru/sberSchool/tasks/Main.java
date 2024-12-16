package ru.sberSchool.tasks;

import lombok.extern.slf4j.Slf4j;
import ru.sberSchool.tasks.task1.Task1;
import ru.sberSchool.tasks.task2.FirstTask;
import ru.sberSchool.tasks.task2.PhoneBook;

@Slf4j
public class Main {
    public static void main(String[] args) {
        log.info("main[0]: Задание 1:");
        Task1.run();

        log.info("main[1]: Задание 2.1:");
        String[] words = {"one", "two", "three", "one", "four", "five", "five", "six", "two", "two", "seven", "three", "four", "eight", "seven", "nine", "five", "ten", "ten", "three"};
        log.info("main[2]: unique words: {}", FirstTask.getUniqueWords(words));
        log.info("main[3]: each word count: {}", FirstTask.countWord(words));

        log.info("main[4]: Задание 2.2:");
        PhoneBook phoneBook = new PhoneBook();

        phoneBook.add("Ivanov", "89281234567");
        phoneBook.add("Petrov", "89281234098");
        phoneBook.add("Ivanov", "89281111567");
        phoneBook.add("Sidorov", "89281098765");

        log.info("main[5]: Ivanov phone numbers: {}", phoneBook.get("Ivanov"));
        log.info("main[6]: Petrov phone numbers: {}", phoneBook.get("Petrov"));
        log.info("main[7]: Sidorov phone numbers: {}", phoneBook.get("Sidorov"));
        log.info("main[8]: Volkov phone numbers: {}", phoneBook.get("Volkov"));
    }
}