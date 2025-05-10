package ru.sberSchool.tasks.task11;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.sberSchool.tasks.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A fixed-size thread pool implementation.
 * The {@code FixedThreadPool} class manages a fixed number of worker threads that
 * execute submitted tasks concurrently. Tasks are stored in a blocking queue and
 * processed by the threads.
 *
 * @author Elland Ilia
 * @since 1.0
 */
@Slf4j
@Data
public class FixedThreadPool implements ThreadPool {

    // Очередь для хранения задач, которые ожидают выполнения.
    private BlockingQueue<Runnable> taskQueue;
    // Количество потоков в пуле.
    private final int countOfThreads;
    // Список рабочих потоков.
    private List<Thread> threads;
    // Флаг для проверки состояния пула (запущен/остановлен).
    private final AtomicBoolean isRunning = new AtomicBoolean(false);

    /**
     * Creates a fixed thread pool with the specified number of threads.
     *
     * @param countOfThreads the number of threads in the pool
     * @throws IllegalArgumentException if {@code countOfThreads <= 0}
     */
    public FixedThreadPool(int countOfThreads) {
        if (countOfThreads <= 0) {
            throw new IllegalArgumentException(Constants.ILLEGAL_TASKS_COUNT_MESSAGE);
        }
        this.countOfThreads = countOfThreads;
        this.threads = new ArrayList<>();
        this.taskQueue = new LinkedBlockingDeque<>();
        log.debug("FixedThreadPool[0]: Created with thread count: {}", countOfThreads);
    }

    /**
     * Starts the thread pool and initializes the worker threads.
     * <p>
     * If the thread pool is already running, this method has no effect.
     * </p>
     */
    @Override
    public void start() {
        log.debug("start[0]: Starting threads");
        if (isRunning.get()) {
            log.debug("start[1]: ThreadPool already running");
            return;
        }
        isRunning.set(true);
        // Создаём и запускаем потоки.
        for (int i = 0; i < countOfThreads; i++) {
            Thread thread = new Thread(() -> {
                while (isRunning.get()) {
                    try {
                        // Извлекаем задачу из очереди и выполняем её.
                        Runnable task = taskQueue.take();
                        log.debug("start[2]: Executing task in thread {}", Thread.currentThread().getName());
                        task.run();
                    } catch (InterruptedException e) {
                        // Завершаем поток, если он был прерван.
                        Thread.currentThread().interrupt();
                        log.debug("start[3]: Thread interrupted: {}", e.getMessage());
                    }
                }
            });
            // Добавляем поток в список.
            threads.add(thread);
            // Запускаем поток.
            thread.start();
            log.debug("start[5]: Thread {} started.", thread.getName());
        }
    }

    /**
     * Submits a task for execution by the thread pool.
     *
     * @param runnable the task to be executed
     * @throws IllegalStateException if the thread pool is not started
     */
    @Override
    public void execute(Runnable runnable) {
        log.debug("execute[0]: Adding task to queue.");
        if (!isRunning.get()) {
            log.error("execute[0]: Thread pool not started yet!");
            throw new IllegalStateException(Constants.THREAD_POOL_NOT_STARTED_MESSAGE);
        }
        // Добавляем задачу в очередь.
        taskQueue.offer(runnable);
    }

    /**
     * Stops the thread pool and interrupts all worker threads.
     */
    public void stop() {
        log.debug("stop[0]: Stopping ThreadPool.");
        // Устанавливаем флаг завершения работы.
        isRunning.set(false);
        // Прерываем все потоки.
        threads.forEach(Thread::interrupt);
    }
}
