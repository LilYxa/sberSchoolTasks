package ru.sberSchool.tasks.task2;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * A simple phone book implementation that maps last names to lists of phone numbers.
 *
 * @author Elland Ilia
 * @version 1.0
 */
@Slf4j
public class PhoneBook {

    private Map<String, List<String>> phoneBook = new HashMap<>();

    /**
     * Adds a new phone number for a given last name.
     *
     * @param lastName the last name of the contact; must not be {@code null}.
     * @param number the phone number to associate with the last name; must not be {@code null}.
     * @throws NullPointerException if either {@code lastName} or {@code number} is {@code null}.
     */
    public void add(String lastName, String number) {
        log.debug("add[0]: adding new contact with last name: {} and phone: {}", lastName, number);
        phoneBook.computeIfAbsent(lastName, k -> new ArrayList<>()).add(number);
    }

    /**
     * Retrieves the list of phone numbers associated with a given last name.
     *
     * @param lastName the last name of the contact; must not be {@code null}.
     * @return a {@code List<String>} of phone numbers associated with the given last name,
     *         or an empty list if no entry exists for the last name.
     * @throws NullPointerException if {@code lastName} is {@code null}.
     */
    public List<String> get(String lastName) {
        log.debug("get[0]: getting phone number by last name: {}", lastName);
        return phoneBook.getOrDefault(lastName, Collections.emptyList());
    }
}
