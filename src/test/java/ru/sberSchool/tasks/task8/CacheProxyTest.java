package ru.sberSchool.tasks.task8;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import ru.sberSchool.tasks.task8.exceptions.NonSerializableDataException;
import ru.sberSchool.tasks.task8.proxy.CacheProxy;
import ru.sberSchool.tasks.task8.service.Loader;
import ru.sberSchool.tasks.task8.service.NonSerializableService;
import ru.sberSchool.tasks.task8.service.Service;
import ru.sberSchool.tasks.task8.service.impl.LoaderImpl;
import ru.sberSchool.tasks.task8.service.impl.NonSerializableServiceImpl;
import ru.sberSchool.tasks.task8.service.impl.ServiceImpl;

import java.io.*;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class CacheProxyTest {
    private CacheProxy cacheProxy;

    @BeforeEach
    void setUp() {
        cacheProxy = new CacheProxy("./cache");
    }

    @Test
    void testInMemoryCacheWithIdentityBy() {
        log.debug("testInMemoryCacheWithIdentityBy[0]: Start test");

        Service service = cacheProxy.cache(new ServiceImpl());

        long startTime = System.nanoTime();
        double result1 = service.doHardWork("work1", 10);
        long firstCallTime = System.nanoTime() - startTime;
        log.debug("testInMemoryCacheWithIdentityBy[1]: Result 1 = {}, Time = {} ns", result1, firstCallTime);

        startTime = System.nanoTime();
        double result2 = service.doHardWork("work1", 5);
        long secondCallTime = System.nanoTime() - startTime;
        log.debug("testInMemoryCacheWithIdentityBy[2]: Result 2 = {}, Time = {} ns", result2, secondCallTime);

        assertEquals(result1, result2, "Results should match due to identityBy logic");
        assertTrue(secondCallTime < firstCallTime, "Second call should be faster due to caching");
    }

    @Test
    void testFileCacheWithIdentityBy() {
        log.debug("testFileCacheWithIdentityBy[0]: Start test");

        Service service = cacheProxy.cache(new ServiceImpl());

        long startTime = System.nanoTime();
        double result1 = service.doAnotherHardWork("work2", 15);
        long firstCallTime = System.nanoTime() - startTime;
        log.debug("testFileCacheWithIdentityBy[1]: Result 1 = {}, Time = {} ns", result1, firstCallTime);

        startTime = System.nanoTime();
        double result2 = service.doAnotherHardWork("work2", 15);
        long secondCallTime = System.nanoTime() - startTime;
        log.debug("testFileCacheWithIdentityBy[2]: Result 2 = {}, Time = {} ns", result2, secondCallTime);

        assertTrue(secondCallTime < firstCallTime, "Second call should be faster due to caching");

        File cacheFile = new File("./cache", "doAnotherHardWork_work2.cache");
        assertTrue(cacheFile.exists(), "Cache file should exist");
    }

    @Test
    void testIdentityByWithDifferentKeys() {
        log.debug("testIdentityByWithDifferentKeys[0]: Start test");

        Service service = cacheProxy.cache(new ServiceImpl());

        long startTime = System.nanoTime();
        double result1 = service.doHardWork("work3", 10);
        long firstCallTime = System.nanoTime() - startTime;
        log.debug("testIdentityByWithDifferentKeys[1]: Result 1 = {}, Time = {} ns", result1, firstCallTime);

        startTime = System.nanoTime();
        double result2 = service.doHardWork("work4", 10);
        long secondCallTime = System.nanoTime() - startTime;
        log.debug("testIdentityByWithDifferentKeys[2]: Result 2 = {}, Time = {} ns", result2, secondCallTime);

        assertNotEquals(result1, result2, "Results should differ for different keys");
    }

    @Test
    void testListLimitWithIdentityBy() {
        log.debug("testListLimitWithIdentityBy[0]: Start test");

        Loader loader = cacheProxy.cache(new LoaderImpl());

        long startTime = System.nanoTime();
        List<String> result1 = loader.loadData("dataset", 1000000);
        long firstCallTime = System.nanoTime() - startTime;
        log.debug("testListLimitWithIdentityBy[1]: Result size = {}, Time = {} ns", result1.size(), firstCallTime);

        assertTrue(result1.size() <= 100000, "List size should be limited");
    }

    @Test
    void testCacheFileCompression() throws IOException {
        log.debug("testCacheFileCompression[0]: Start test");

        Service service = cacheProxy.cache(new ServiceImpl());

        long startTime = System.nanoTime();
        service.processData("compressTest", 10);
        long firstCallTime = System.nanoTime() - startTime;
        log.debug("testCacheFileCompression[1]: First call, Time = {} ns", firstCallTime);

        long startTime2 = System.nanoTime();
        service.processData("compressTest", 10);
        long secondCallTime = System.nanoTime() - startTime2;
        log.debug("testCacheFileCompression[2]: Second call, Time = {} ns", secondCallTime);

        File cacheFile = new File("./cache", "processData_compressTest_10.cache.zip");
        assertTrue(cacheFile.exists(), "Compressed cache file should exist");

        assertTrue(cacheFile.length() > 0, "Cache file should not be empty");
    }

    @Test
    void testNonSerializableData() {
        log.debug("testNonSerializableData[0]: Start test");

        NonSerializableService service = cacheProxy.cache(new NonSerializableServiceImpl());

        Exception exception = assertThrows(NonSerializableDataException.class, () ->
                service.loadNonSerializable("test")
        );

        log.debug("testNonSerializableData[1]: Exception message = {}", exception.getMessage());

        assertTrue(exception.getMessage().contains("Data must be serializable"), "Exception should indicate serialization issue");
    }

    @Test
    void testConcurrentCacheAccessWithLocking() throws InterruptedException {
        log.debug("testConcurrentCacheAccessWithLocking[0]: Start test");

        CacheProxy cacheProxy = new CacheProxy("./cache");
        Service service = cacheProxy.cache(new ServiceImpl());

        // Количество потоков
        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // Счетчик для проверки inconsistencies (неконсистентных значений)
        AtomicInteger inconsistencies = new AtomicInteger();

        String task = "testTask";
        int value = 10;

        // Флаг для хранения первого результата
        final double[] firstResult = new double[1];

        // Задача, которую будут выполнять потоки
        Runnable taskRunner = () -> {
            try {
                // Выполняем работу и кэшируем результат
                double result = service.doHardWork(task, value);

                // Если это первый поток, сохраняем результат
                if (inconsistencies.get() == 0) {
                    firstResult[0] = result;
                }

                // Проверяем, что все потоки возвращают тот же результат
                if (firstResult[0] != result) {
                    inconsistencies.incrementAndGet();
                }
            } catch (Exception e) {
                log.error("testConcurrentCacheAccessWithLocking[1]: Exception occurred", e);
            } finally {
                latch.countDown();
            }
        };

        // Запускаем несколько потоков
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(taskRunner);
        }

        // Ожидаем завершения всех потоков
        latch.await();
        executorService.shutdown();

        log.debug("testConcurrentCacheAccessWithLocking[2]: Inconsistencies = {}", inconsistencies.get());

        // Проверяем, что все потоки завершили выполнение корректно (инконсистенций быть не должно)
        assertEquals(0, inconsistencies.get(), "There should be no inconsistencies, as the cache access is synchronized.");
    }

    @Test
    void testConcurrentCacheAccessWithoutLocking() throws InterruptedException {
        log.debug("testConcurrentCacheAccessWithoutLocking[0]: Start test");

        // Создаем кэш без блокировок для тестирования гонок
        Map<String, Object> unsafeCache = new HashMap<>();
        CacheProxy unsafeCacheProxy = new CacheProxy("./cache") {
            @Override
            protected Map<String, Object> createCache() {
                return unsafeCache;
            }
        };
        Service service = unsafeCacheProxy.cache(new ServiceImpl());

        // Количество потоков
        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // Счетчик для проверки inconsistencies (неконсистентных значений)
        AtomicInteger inconsistencies = new AtomicInteger();

        // Ключ для конкурентного доступа
        String task = "testTask";
        int value = 10;

        // Задача, которую будут выполнять потоки
        Runnable taskRunner = () -> {
            try {
                // Выполняем работу и кэшируем результат
                double result = service.doHardWork(task, value);

                // Сохраняем результат из первого потока, чтобы сравнить с результатами других потоков
                if (inconsistencies.get() == 0) {
                    inconsistencies.set((int) result); // Записываем первый результат
                }

                // Проверяем, что все потоки возвращают одинаковый результат
                if (inconsistencies.get() != result) {
                    inconsistencies.incrementAndGet();
                }
            } catch (Exception e) {
                log.error("testConcurrentCacheAccessWithoutLocking[1]: Exception occurred", e);
            } finally {
                latch.countDown();
            }
        };

        // Запускаем несколько потоков
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(taskRunner);
        }

        // Ожидаем завершения всех потоков
        latch.await();
        executorService.shutdown();

        log.debug("testConcurrentCacheAccessWithoutLocking[2]: Inconsistencies = {}", inconsistencies.get());

        assertTrue(inconsistencies.get() > 0, "There should be inconsistencies without proper locking");
    }



}
