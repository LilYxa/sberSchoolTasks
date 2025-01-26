package ru.sberSchool.tasks.task15;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ru.sberSchool.tasks.Constants;
import ru.sberSchool.tasks.task15.service.DownloadService;
import ru.sberSchool.tasks.utils.PropertiesConfigUtil;

/**
 * Entry point for the application that initializes the Spring ApplicationContext
 * and starts the file download process using the {@link DownloadService}.
 *
 */
@SpringBootApplication
public class MainApplication {

    /**
     * The main method that serves as the entry point for the application.
     *
     * @param args command-line arguments.
     */
    public static void main(String[] args) {
        // Initialize the Spring ApplicationContext
        ApplicationContext context = SpringApplication.run(MainApplication.class, args);

        // Retrieve the DownloadService bean
        DownloadService downloaderService = context.getBean(DownloadService.class);

        // Start the download process using configuration properties
        downloaderService.startDownload(
                PropertiesConfigUtil.getProperty(Constants.LINKS_FILE),
                PropertiesConfigUtil.getProperty(Constants.DOWNLOAD_DIR),
                Integer.parseInt(PropertiesConfigUtil.getProperty(Constants.COUNT_THREADS)),
                Long.parseLong(PropertiesConfigUtil.getProperty(Constants.SPEED_LIMIT))
        );
    }
}
