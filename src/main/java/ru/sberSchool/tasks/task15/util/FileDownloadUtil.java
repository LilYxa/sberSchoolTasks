package ru.sberSchool.tasks.task15.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility class for downloading files from a given URL to a specified output directory.
 * Provides optional speed-limiting functionality to control the download rate.
 *
 * @author Elland Ilia
 */
@Slf4j
@Component
public class FileDownloadUtil {

    /**
     * Downloads a file from the specified URL to the provided output directory.
     *
     * @param fileUrl    the URL of the file to be downloaded.
     * @param outputDir  the directory where the downloaded file will be saved.
     * @param speedLimit the maximum download speed in bytes per second.
     *
     */
    public void download(String fileUrl, String outputDir, long speedLimit) {
        try {
            log.debug("download[0]: Starting download for URL: {}", fileUrl);

            URL url = new URL(fileUrl);
            URLConnection connection = url.openConnection();

            String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
            Path outputPath = Paths.get(outputDir);

            // Создать директорию, если она не существует
            if (!Files.exists(outputPath)) {
                Files.createDirectories(outputPath);
                log.debug("download[2]: Created output directory: {}", outputPath);
            }

            File outputFile = new File(outputPath.toString(), fileName);

            try (InputStream inputStream = connection.getInputStream();
                 OutputStream outputStream = new FileOutputStream(outputFile)) {

                byte[] buffer = new byte[1024];
                int bytesRead;
                long startTime = System.nanoTime();

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);

                    long elapsedTime = (System.nanoTime() - startTime) / 1_000_000;
                    long expectedTime = (bytesRead * 1000) / speedLimit;

                    if (elapsedTime < expectedTime) {
                        Thread.sleep(expectedTime - elapsedTime);
                    }

                    startTime = System.nanoTime();
                }

                log.debug("download[1]: Successfully downloaded: {}", fileName);
            }
        } catch (Exception e) {
            log.error("download[2]: Error downloading file: {}", e.getMessage(), e);
        }
    }
}
