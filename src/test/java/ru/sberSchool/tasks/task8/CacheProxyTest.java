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
}
