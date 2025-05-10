package ru.sberSchool.tasks.task9.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import ru.sberSchool.tasks.task9.model.Person;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class StreamsTest {

    @Test
    public void testStreamsFunctionality() {
        log.debug("testStreamsFunctionality[0]: Start test");
        List<Person> people = List.of(new Person("Ivan", 20), new Person("Petr", 25), new Person("Vasya", 22));
        Map<String, Person> resultMap = Streams.of(people)
            .filter(p -> p.getAge() > 20)
            .transform( p -> new Person(p.getName(), p.getAge() + 30))
            .toMap(Person::getName, p -> p);
        log.debug("testStreamsFunctionality[1]: Result map: {}", resultMap);
        assertNotNull(resultMap);
    }

    @Test
    public void testOfMethod() {
        log.debug("testOfMethod[0]: Start test");
        List<Person> input = List.of(new Person("Ivan", 30), new Person("Petr", 25));
        Streams<Person> streams = Streams.of(input);
        assertNotNull(streams);
        List<Person> result = streams.toList();
        log.debug("testOfMethod[1]: Result collection: {}", result);
        assertEquals(input, result);
    }

    @Test
    public void testFilterPositive() {
        log.debug("testFilterPositive[0]: Start test");
        List<Person> input = List.of(
                new Person("Ivan", 30),
                new Person("Petr", 25),
                new Person("Kolya", 20)
        );
        Predicate<Person> predicate = person -> person.getAge() > 25;
        Streams<Person> streams = Streams.of(input)
                .filter(predicate);
        List<Person> result = streams.toList();
        log.debug("testFilterPositive[1]: Filtered collection: {}", result);
        assertEquals(List.of(new Person("Ivan", 30)), result);
    }

    @Test
    public void testFilterWithEmptyResult() {
        log.debug("testFilterWithEmptyResult[0]: Start test");
        List<Person> input = List.of(new Person("Ivan", 30), new Person("Petr", 25));
        Predicate<Person> predicate = person -> person.getAge() > 50;
        Streams<Person> streams = Streams.of(input)
                .filter(predicate);
        List<Person> result = streams.toList();
        log.debug("testFilterWithEmptyResult[1]: Filtered collection: {}", result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testTransformPositive() {
        log.debug("testTransformPositive[0]: Start test");
        List<Person> input = List.of(
                new Person("Ivan", 30),
                new Person("Petr", 25)
        );
        Function<Person, String> transformFunction = Person::getName;
        Streams<String> streams = Streams.of(input)
                .transform(transformFunction);
        List<String> result = streams.toList();
        log.debug("testTransformPositive[1]: Transformed collection: {}", result);
        assertEquals(List.of("Ivan", "Petr"), result);
    }

    @Test
    public void testToMapPositive() {
        log.debug("testToMapPositive[0]: Start test");
        List<Person> input = List.of(
                new Person("Ivan", 30),
                new Person("Petr", 25)
        );
        Function<Person, String> keyMapper = Person::getName;
        Function<Person, Integer> valueMapper = Person::getAge;
        Map<String, Integer> result = Streams.of(input)
                .toMap(keyMapper, valueMapper);
        log.debug("testToMapPositive[1]: Result map: {}", result);
        assertEquals(Map.of("Ivan", 30, "Petr", 25), result);
    }

    @Test
    public void testFilterWithNullPredicate() {
        log.debug("testFilterWithNullPredicate[0]: Start test");
        List<Person> input = List.of(new Person("Ivan", 30), new Person("Petr", 25));
        Exception exception = assertThrows(NullPointerException.class, () ->
                Streams.of(input).filter(null)
        );
        log.debug("testFilterWithNullPredicate[1]: Exception message: {}", exception.getMessage());
    }

    @Test
    public void testTransformWithNullFunction() {
        log.debug("testTransformWithNullFunction[0]: Start test");
        List<Person> input = List.of(new Person("Ivan", 30), new Person("Petr", 25));
        Exception exception = assertThrows(NullPointerException.class, () ->
                Streams.of(input).transform(null)
        );
        log.debug("testTransformWithNullFunction[1]: Exception message: {}", exception.getMessage());
    }

    @Test
    public void testToMapWithNullKeyMapper() {
        log.debug("testToMapWithNullKeyMapper[0]: Start test");
        List<Person> input = List.of(new Person("Ivan", 30), new Person("Petr", 25));
        Function<Person, Integer> valueMapper = Person::getAge;
        Exception exception = assertThrows(NullPointerException.class, () ->
                Streams.of(input).toMap(null, valueMapper)
        );
        log.debug("testToMapWithNullKeyMapper[1]: Exception message: {}", exception.getMessage());
    }

    @Test
    public void testToMapWithNullValueMapper() {
        log.debug("testToMapWithNullValueMapper[0]: Start test");
        List<Person> input = List.of(new Person("Ivan", 30), new Person("Petr", 25));
        Function<Person, String> keyMapper = Person::getName;
        Exception exception = assertThrows(NullPointerException.class, () ->
                Streams.of(input).toMap(keyMapper, null)
        );
        log.debug("testToMapWithNullValueMapper[1]: Exception message: {}", exception.getMessage());
    }

    @Test
    public void testFilterPreservesOriginalCollection() {
        log.debug("testFilterPreservesOriginalCollection[0]: Start test");
        List<Person> input = List.of(
                new Person("Ivan", 30),
                new Person("Petr", 25)
        );
        Streams<Person> streams = Streams.of(input);
        streams.filter(person -> person.getAge() > 25);
        log.debug("testFilterPreservesOriginalCollection[1]: Original collection: {}", input);
        assertEquals(List.of(
                new Person("Ivan", 30),
                new Person("Petr", 25)
        ), input);
    }
}

