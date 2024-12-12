package ru.sberSchool.tasks.task2;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class that provides methods for processing arrays of words.
 * It includes functionality to extract unique words and count the occurrences of each word.
 *
 * @author Elland Ilia
 * @version 1.0
 */
@Slf4j
public class FirstTask {

    /**
     * Returns a list of unique words from the given array of words.
     *
     * @param words an array of words to process; must not be {@code null}.
     * @return a {@code List<String>} containing unique words from the input array.
     */
    public static List<String> getUniqueWords(String[] words) {
        log.debug("getUniqueWords[0]: getting unique words from array: {}", (Object) words);
        return Arrays.stream(words)
                .distinct()
                .toList();
    }

    /**
     * Counts the occurrences of each word in the given array of words.
     *
     * @param words an array of words to process; must not be {@code null}.
     * @return a {@code Map<String, Long>} where the keys are words and the values are their counts in the input array.
     */
    public static Map<String, Long> countWord(String[] words) {
        log.debug("countWord[0]: count each word in array: {}", (Object) words);
        return Arrays.stream(words)
                .collect(Collectors.groupingBy(word -> word, Collectors.counting()));
    }
}
