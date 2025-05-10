package ru.sberSchool.tasks.task11;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import ru.sberSchool.tasks.Constants;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
class ScalableThreadPoolTest {

    private ScalableThreadPool threadPool;

    @BeforeEach
    void setUp() {
        log.debug("setUp[0]: Setting up ScalableThreadPool for testing.");
        threadPool = new ScalableThreadPool(2, 5);
    }

    @Test
    void testStart() {
        log.debug("testStart[0]: Starting ThreadPool.");
        threadPool.start();
        assertTrue(threadPool.getIsRunning().get(), "ThreadPool should be running after start.");
        assertEquals(2, threadPool.getThreads().size(), "ThreadPool should start with minThreads.");
        log.debug("testStart[1]: ThreadPool started successfully with {} threads.", threadPool.getThreads().size());
    }

    @Test
    void testExecuteTask() throws InterruptedException {
        log.debug("testExecuteTask[0]: Executing single task in ThreadPool.");
        threadPool.start();
        AtomicInteger counter = new AtomicInteger();

        Runnable task = () -> {
            log.debug("testExecuteTask[1]: Task executed.");
            counter.incrementAndGet();
        };

        threadPool.execute(task);
        Thread.sleep(500); // Подождем, чтобы поток успел обработать задачу.
        assertEquals(1, counter.get(), "Task should have been executed once.");
        log.debug("testExecuteTask[2]: Task executed successfully, counter = {}", counter.get());
    }

    @Test
    void testScaleUp() throws InterruptedException {
        log.debug("testScaleUp[0]: Testing thread pool scaling up.");
        threadPool.start();
        AtomicInteger counter = new AtomicInteger();
        AtomicInteger maxThreadCount = new AtomicInteger(); // Для отслеживания максимального числа потоков

        for (int i = 0; i < 20; i++) {
            threadPool.execute(() -> {
                log.debug("testScaleUp[1]: Task executed.");
                counter.incrementAndGet();
                try {
                    Thread.sleep(500); // Имитируем выполнение длительной задачи
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        // Запускаем мониторинг числа потоков
        Thread monitorThread = new Thread(() -> {
            while (counter.get() < 20) { // Пока все задачи не завершены
                int currentThreadCount = threadPool.getThreads().size();
                maxThreadCount.updateAndGet(x -> Math.max(x, currentThreadCount));
                try {
                    Thread.sleep(100); // Частота обновления
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        monitorThread.start();

        monitorThread.join();
        Thread.sleep(1000); // Небольшая задержка для завершения потоков

        log.debug("testScaleUp[2]: Maximum thread count observed: {}", maxThreadCount.get());
        assertTrue(maxThreadCount.get() > 2, "Thread pool did not scale up.");
        assertEquals(20, counter.get(), "Not all tasks were executed.");
        log.debug("testScaleUp[3]: All tasks executed and thread pool scaled up successfully.");
    }

    @Test
    void testScaleDown() throws InterruptedException {
        log.debug("testScaleDown[0]: Testing ThreadPool scaling down.");
        threadPool.start();
        for (int i = 0; i < 3; i++) {
            threadPool.execute(() -> log.debug("testScaleDown[1]: Task executed."));
        }

        Thread.sleep(1000); // Подождем выполнения всех задач.
        assertEquals(2, threadPool.getThreads().size(), "ThreadPool should have scaled down to minThreads.");
        log.debug("testScaleDown[2]: ThreadPool scaled down successfully to {} threads.", threadPool.getThreads().size());
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
    void testInvalidThreadLimits() {
        log.debug("testInvalidThreadLimits[0]: Attempting to create ThreadPool with invalid thread limits.");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new ScalableThreadPool(5, 2));
        assertEquals(Constants.INVALID_THREAD_LIMITS_MESSAGE, exception.getMessage(), "Expected INVALID_THREAD_LIMITS_MESSAGE exception.");
        log.debug("testInvalidThreadLimits[1]: Exception thrown successfully: {}", exception.getMessage());
    }
}
