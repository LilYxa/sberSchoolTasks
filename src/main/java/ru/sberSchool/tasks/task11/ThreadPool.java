package ru.sberSchool.tasks.task11;

/**
 * A simple interface representing a thread pool, which is a mechanism
 * for managing and executing tasks concurrently using a pool of worker threads.
 *
 * @author Elland Ilia
 */
public interface ThreadPool {

    /**
     * Starts the thread pool by initializing and running the worker threads.
     */
    void start();

    /**
     * Submits a task for execution by the thread pool.
     *
     * @param runnable the task to be executed, represented as a {@link Runnable}
     * @throws IllegalStateException if the thread pool is not started
     */
    void execute(Runnable runnable);
}
