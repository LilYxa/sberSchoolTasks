package ru.sberSchool.tasks.task15.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sberSchool.tasks.task15.service.DownloadService;
import ru.sberSchool.tasks.task15.util.FileDownloadUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Implementation of the {@link DownloadService} interface that handles downloading files
 * concurrently using a thread pool. This service reads download links from a file and processes
 * them with the specified constraints.
 *
 * @author Elland Ilia
 */
@Slf4j
@Service
public class DownloadServiceImpl implements DownloadService {

    private final FileDownloadUtil fileDownloadUtil;

    @Autowired
    public DownloadServiceImpl(FileDownloadUtil fileDownloader) {
        this.fileDownloadUtil = fileDownloader;
    }

    /**
     * Starts downloading files from the specified list of links.
     *
     * <p>The links are read from a file, and each link is processed concurrently
     * using a thread pool with the specified number of threads. The download speed
     * for each link can optionally be limited.
     *
     * @param linksFile  the path to the file containing the download links.
     * @param outputDir  the directory where the downloaded files will be saved.
     * @param threads    the number of threads to use for concurrent downloads.
     * @param speedLimit the maximum download speed in bytes per second.
     *
     * @throws IOException if there is an error reading the links file.
     */
    @Override
    public void startDownload(String linksFile, String outputDir, int threads, long speedLimit) {
        try {
            log.debug("startDownload[1]: Reading links from file: {}", linksFile);
            List<String> links = Files.readAllLines(Paths.get(linksFile));

            ExecutorService executor = Executors.newFixedThreadPool(threads);
            for (String link : links) {
                executor.submit(() -> fileDownloadUtil.download(link, outputDir, speedLimit));
            }
            executor.shutdown();
        } catch (IOException e) {
            log.error("startDownload[2]: Error reading links file: {}", e.getMessage(), e);
        }
    }
}
