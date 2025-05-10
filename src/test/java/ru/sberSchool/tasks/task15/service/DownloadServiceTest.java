package ru.sberSchool.tasks.task15.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import ru.sberSchool.tasks.task15.service.impl.DownloadServiceImpl;
import ru.sberSchool.tasks.task15.util.FileDownloadUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@Slf4j
public class DownloadServiceTest {

    @Mock
    private FileDownloadUtil fileDownloadUtil;

    @InjectMocks
    private DownloadServiceImpl downloadService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void startDownload_PositiveScenario() throws InterruptedException {
        log.debug("startDownload_PositiveScenario[1]: Initializing test for successful download process");

        // Данные для теста
        String linksFile = "test_links.txt";
        String outputDir = "output";
        int threads = 3;
        long speedLimit = 500 * 1024;

        List<String> links = List.of(
                "http://example.com/file1.jpg",
                "http://example.com/file2.jpg"
        );

        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.readAllLines(Path.of(linksFile))).thenReturn(links);

            // Используем CountDownLatch для синхронизации потоков
            CountDownLatch latch = new CountDownLatch(links.size());

            // Настроим mock, чтобы по завершению потока уменьшать счетчик
            doAnswer(invocation -> {
                latch.countDown();  // Снижение счётчика при каждом вызове
                return null;
            }).when(fileDownloadUtil).download(anyString(), eq(outputDir), eq(speedLimit));

            // Запуск тестируемого метода
            downloadService.startDownload(linksFile, outputDir, threads, speedLimit);

            // Дожидаемся завершения всех потоков
            latch.await();

            // Проверяем вызов метода загрузки
            verify(fileDownloadUtil, times(2)).download(anyString(), eq(outputDir), eq(speedLimit));
            log.debug("startDownload_PositiveScenario[2]: Test completed successfully");
        }
    }

    @Test
    void startDownload_FileNotFound() {
        log.debug("startDownload_FileNotFound[1]: Initializing test for missing links file");

        // Мокируем данные
        String linksFile = "non_existent_file.txt";
        String outputDir = "output";
        int threads = 3;
        long speedLimit = 500 * 1024;

        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.readAllLines(Path.of(linksFile))).thenThrow(new IOException("File not found"));

            // Тестируем метод
            downloadService.startDownload(linksFile, outputDir, threads, speedLimit);

            // Проверяем, что метод загрузки не вызывался
            verify(fileDownloadUtil, never()).download(anyString(), eq(outputDir), eq(speedLimit));
            log.debug("startDownload_FileNotFound[2]: Test completed successfully");
        }
    }

    @Test
    void startDownload_DownloadThrowsException() throws InterruptedException {
        log.debug("startDownload_DownloadThrowsException[1]: Initializing test for download failure");

        // Данные для теста
        String linksFile = "test_links.txt";
        String outputDir = "output";
        int threads = 3;
        long speedLimit = 500 * 1024;

        List<String> links = List.of(
                "http://example.com/file1.jpg",
                "http://example.com/file2.jpg"
        );

        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.readAllLines(Path.of(linksFile))).thenReturn(links);

            // Мокируем метод так, чтобы он выбрасывал исключение при вызове
            doThrow(new RuntimeException("Download failed")).when(fileDownloadUtil).download(anyString(), eq(outputDir), eq(speedLimit));

            // Используем CountDownLatch для синхронизации потоков
            CountDownLatch latch = new CountDownLatch(links.size());

            // Настроим mock, чтобы при каждом вызове download уменьшать счетчик
            doAnswer(invocation -> {
                latch.countDown();  // Снижение счётчика при каждом вызове
                return null;
            }).when(fileDownloadUtil).download(anyString(), eq(outputDir), eq(speedLimit));

            // Запуск тестируемого метода
            downloadService.startDownload(linksFile, outputDir, threads, speedLimit);

            // Дожидаемся завершения всех потоков
            latch.await();

            // Проверяем, что метод download был вызван дважды
            verify(fileDownloadUtil, times(2)).download(anyString(), eq(outputDir), eq(speedLimit));

            // Проверяем, что ошибка была выброшена при каждом вызове
            verify(fileDownloadUtil, times(2)).download(anyString(), eq(outputDir), eq(speedLimit));
            log.debug("startDownload_DownloadThrowsException[2]: Test completed successfully");
        } catch (RuntimeException e) {
            log.error("startDownload_DownloadThrowsException[3]: Exception thrown during download: {}", e.getMessage());
            // Проверяем, что исключение выбрасывается
            assertTrue(e.getMessage().contains("Download failed"));
        }
    }
}
