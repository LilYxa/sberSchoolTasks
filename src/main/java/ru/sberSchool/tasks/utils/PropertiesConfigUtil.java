package ru.sberSchool.tasks.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.sberSchool.tasks.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@NoArgsConstructor
@Slf4j
public class PropertiesConfigUtil {
    @Getter
    @Setter
    private static String configPath = "";
    private static final Properties config = new Properties();

//    public static void setConfigPath(String configPath) {
//        PropertiesConfigUtil.configPath = configPath;
//    }

    public static Properties getConfiguration() throws IOException {
        if (config.isEmpty()) {
            loadConfiguration();
        }
        return config;
    }

    private static void loadConfiguration() throws IOException {
        File file = new File(configPath.isEmpty() ? Constants.PROPERTIES_CONFIG_PATH : configPath);
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            config.load(fileInputStream);
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    public static String getProperty(String key) {
        log.debug("getProperty[1]: key: {}", key);
        try {
            return getConfiguration().getProperty(key);
        } catch (IOException e) {
            log.error("getProperty[2]: error: {}", e.getMessage());
        }
        return "";
    }
}