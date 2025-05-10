package ru.sberSchool.tasks.task11;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import ru.sberSchool.tasks.Constants;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
class FixedThreadPoolTest {

    private FixedThreadPool threadPool;

    @BeforeEach
    void setUp() {
        log.debug("setUp[0]: Setting up FixedThreadPool for testing.");
        threadPool = new FixedThreadPool(3);
    }

    @Test
    void testStart() {
        log.debug("testStart[0]: Starting ThreadPool.");
        threadPool.start();
        assertTrue(threadPool.getIsRunning().get(), "ThreadPool should be running after start.");
        log.debug("testStart[1]: ThreadPool started successfully.");
    }

    @Test
    void testExecuteTask() throws InterruptedException {
        log.debug("testExecuteTask[0]: Executing a task in ThreadPool.");
        threadPool.start();
        AtomicInteger counter = new AtomicInteger();

        Runnable task = () -> {
            log.debug("testExecuteTask[1]: Task executed.");
            counter.incrementAndGet();
        };

        threadPool.execute(task);
        Thread.sleep(1000); // Подождем, чтобы поток успел обработать задачу.
        assertEquals(1, counter.get(), "Task should have been executed once.");
        log.debug("testExecuteTask[2]: Task executed successfully, counter = {}", counter.get());
    }

    @Test
    void testMultipleTasks() throws InterruptedException {
        log.debug("testMultipleTasks[0]: Executing multiple tasks in ThreadPool.");
        threadPool.start();
        AtomicInteger counter = new AtomicInteger();

        for (int i = 0; i < 10; i++) {
            threadPool.execute(() -> {
                log.debug("testMultipleTasks[1]: Task executed.");
                counter.incrementAndGet();
            });
        }

        Thread.sleep(2000); // Подождем, чтобы потоки успели выполнить задачи.
        assertEquals(10, counter.get(), "All 10 tasks should have been executed.");
        log.debug("testMultipleTasks[2]: All tasks executed successfully, counter = {}", counter.get());
    }

    @Test
    void testStop() throws InterruptedException {
        log.debug("testStop[0]: Stopping ThreadPool.");
        threadPool.start();
        threadPool.stop();

        Thread.sleep(500); // Небольшая задержка, чтобы потоки успели остановиться.
        assertFalse(threadPool.getIsRunning().get(), "ThreadPool should not be running after stop.");
        log.debug("testStop[1]: ThreadPool stopped successfully.");
    }

    @Test
    void testExecuteWithoutStart() {
        log.debug("testExecuteWithoutStart[0]: Attempting to execute task without starting ThreadPool.");
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            threadPool.execute(() -> log.debug("testExecuteWithoutStart[1]: Task should not be executed."));
        });
        assertEquals(Constants.THREAD_POOL_NOT_STARTED_MESSAGE, exception.getMessage(), "Expected THREAD_POOL_NOT_STARTED_MESSAGE exception.");
        log.debug("testExecuteWithoutStart[2]: Exception thrown successfully: {}", exception.getMessage());
    }

    @Test
    void testInvalidThreadCount() {
        log.debug("testInvalidThreadCount[0]: Attempting to create ThreadPool with invalid thread count.");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new FixedThreadPool(0));
        assertEquals(Constants.ILLEGAL_TASKS_COUNT_MESSAGE, exception.getMessage(), "Expected ILLEGAL_TASKS_COUNT_MESSAGE exception.");
        log.debug("testInvalidThreadCount[1]: Exception thrown successfully: {}", exception.getMessage());
    }
}

