package ru.sberSchool.tasks.task5.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.sberSchool.tasks.task5.exceptions.AssignException;
import ru.sberSchool.tasks.task5.model.Source;
import ru.sberSchool.tasks.task5.model.Target;
import ru.sberSchool.tasks.task5.model.IncompatibleTarget;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class BeanUtilsTest {

    private Source source;
    private Target target;

    @BeforeEach
    public void setUp() {
        source = new Source();
        target = new Target();

        source.setName("John Doe");
        source.setAge(30);
    }

    @Test
    public void testAssignProperties() {
        log.debug("testAssignProperties[0]: start test");

        log.debug("testAssignProperties[1]: source name: {}", source.getName());
        log.debug("testAssignProperties[2]: source age: {}", source.getAge());

        BeanUtils.assign(target, source);

        log.debug("testAssignProperties[3]: target name: {}", target.getName());
        log.debug("testAssignProperties[4]: target age: {}", target.getAge());

        assertEquals(source.getName(), target.getName());
        assertEquals(source.getAge(), target.getAge());
    }

    @Test
    public void testAssignWithNullValues() {
        log.debug("testAssignWithNullValues[0]: start test");

        source.setName(null);
        source.setAge(0);

        BeanUtils.assign(target, source);

        assertNull(target.getName());
        assertEquals(0, target.getAge());

        log.debug("testAssignWithNullValues[1]: target name: {}", target.getName());
        log.debug("testAssignWithNullValues[2]: target age: {}", target.getAge());
    }


    @Test
    public void testAssignWithError() {
        log.debug("testAssignWithError[0]: start test");
        IncompatibleTarget incompatibleTarget = new IncompatibleTarget();

        try {
            BeanUtils.assign(incompatibleTarget, source);
            assertNull(incompatibleTarget.getName());
        } catch (AssignException e) {
            log.debug("testAssignWithError[1]: error: {}", e.getMessage());
        }
    }
}
