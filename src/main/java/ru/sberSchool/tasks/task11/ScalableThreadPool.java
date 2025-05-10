package ru.sberSchool.tasks.task11;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.sberSchool.tasks.Constants;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A scalable thread pool implementation that adjusts the number of threads based on the workload.
 * <p>
 * The {@code ScalableThreadPool} class starts with a minimum number of threads and can scale up
 * to a maximum number of threads based on the number of tasks in the queue. If the workload decreases,
 * it scales down by removing idle threads, ensuring efficient resource utilization.
 * </p>
 *
 * @author Elland Ilia
 * @since 1.0
 */
@Slf4j
@Data
public class ScalableThreadPool implements ThreadPool {

    // Минимальное количество потоков в пуле.
    private final int minThreads;
    // Максимальное количество потоков в пуле.
    private final int maxThreads;
    // Очередь для хранения задач.
    private final BlockingQueue<Runnable> taskQueue;
    // Флаг, указывающий, работает ли пул.
    private final AtomicBoolean isRunning;
    // Коллекция для хранения потоков.
    private final ConcurrentLinkedQueue<Thread> threads;

    /**
     * Creates a scalable thread pool with specified minimum and maximum threads.
     *
     * @param minThreads the minimum number of threads
     * @param maxThreads the maximum number of threads
     * @throws IllegalArgumentException if {@code minThreads > maxThreads} or {@code minThreads <= 0}
     */
    public ScalableThreadPool(int minThreads, int maxThreads) {
        if (minThreads > maxThreads || minThreads <= 0) {
            throw new IllegalArgumentException(Constants.INVALID_THREAD_LIMITS_MESSAGE);
        }
        this.minThreads = minThreads;
        this.maxThreads = maxThreads;
        this.taskQueue = new LinkedBlockingDeque<>();
        this.threads = new ConcurrentLinkedQueue<>();
        this.isRunning = new AtomicBoolean(false);
        log.debug("ScalableThreadPool[0]: Created with minThreads = {}, maxThreads = {}", minThreads, maxThreads);
    }

    /**
     * Starts the thread pool and initializes the minimum number of threads.
     */
    @Override
    public void start() {
        log.debug("start[1]: Starting threads.");
        if (isRunning.get()) {
            log.debug("start[2]: ThreadPool already running.");
            return;
        }
        isRunning.set(true);
        // Создаём минимальное количество потоков.
        for (int i = 0; i < minThreads; i++) {
            addThread();
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
            throw new IllegalStateException(Constants.THREAD_POOL_NOT_STARTED_MESSAGE);
        }
        // Добавляем задачу в очередь.
        taskQueue.offer(runnable);
        synchronized (threads) {
            int currentThreadCount = threads.size();
            int taskCount = taskQueue.size();

            log.debug("execute[1]: Current queue size: {}, thread count: {}", taskCount, currentThreadCount);

            // Увеличиваем количество потоков, если задач больше, чем активных потоков.
            if (taskCount > currentThreadCount && currentThreadCount < maxThreads) {
                log.debug("execute[2]: Adding new thread to handle tasks.");
                addThread();
            }
        }
    }

    /**
     * Creates and starts a new worker thread.
     */
    private void addThread() {
        Thread thread = new Thread(() -> {
            while (isRunning.get() || !taskQueue.isEmpty()) {
                try {
                    // Получаем задачу из очереди с таймаутом.
                    Runnable task = taskQueue.poll(100, TimeUnit.MILLISECONDS);
                    if (task != null) {
                        log.debug("addThread[0]: Executing task in thread {}", Thread.currentThread().getName());
                        task.run();
                    } else {
                        synchronized (threads) {
                            // Удаляем лишний поток, если задач больше нет.
                            if (threads.size() > minThreads && taskQueue.isEmpty()) {
                                log.debug("addThread[1]: Scaling down, removing idle worker.");
//                                threads.remove(Thread.currentThread());
                                break;
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    // Обработка прерывания потока.
                    Thread.currentThread().interrupt();
                    log.debug("addThread[2]: Thread interrupted: {}", e.getMessage());
                    break;
                }
            }
            synchronized (threads) {
                threads.remove(Thread.currentThread());
            }
            log.debug("addThread[3]: Thread {} finished.", Thread.currentThread().getName());
        });
        // Добавляем поток в пул и запускаем его.
        threads.offer(thread);
        thread.start();
        log.debug("addThread[4]: Worker thread {} started.", thread.getName());
    }

    /**
     * Stops the thread pool and interrupts all worker threads.
     */
    public void stop() {
        log.debug("stop[0]: Stopping ThreadPool.");
        isRunning.set(false);
        // Прерываем все активные потоки.
        synchronized (threads) {
            threads.forEach(Thread::interrupt);
        }
    }
}
