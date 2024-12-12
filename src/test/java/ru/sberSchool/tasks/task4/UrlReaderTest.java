package ru.sberSchool.tasks.task4;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import ru.sberSchool.tasks.task4.UrlReader;
import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class UrlReaderTest {

    @Test
    public void testReadContent_Success() throws Exception {
        log.debug("testReadContent_Success[0]: start test");
        String url = "https://www.oracle.com/java/";
        log.debug("testReadContent_Success[1]: test URL: {}", url);

        int responseCode = UrlReader.readContent(url);

        assertEquals(200, responseCode);
    }

    @Test
    public void testReadContent_InvalidUrl() {
        log.debug("testReadContent_InvalidUrl[0]: start test");
        String url = "htp://invalid-url";
        log.debug("testReadContent_InvalidUrl[1]: invalid URL: {}", url);

        assertThrows(MalformedURLException.class, () -> {
            UrlReader.readContent(url);
        });
    }
}
