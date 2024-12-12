package ru.sberSchool.tasks.task7;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import ru.sberSchool.tasks.task7.classLoaders.EncryptedClassLoader;
import ru.sberSchool.tasks.task7.utils.EncryptUtils;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class EncryptedClassLoaderTest {
    private final String encryptionKey = "mySecretKey";

    @Test
    public void testLoadEncryptedClassSuccessfully() throws Exception {
        log.debug("testLoadEncryptedClassSuccessfully[0]: Start test");

        File originalClassFile = new File("src/main/resources/test_encrypted_classes/TestClass.class");
        log.debug("testLoadEncryptedClassSuccessfully[1]: Original class file path: {}", originalClassFile.getAbsolutePath());

        File encryptedDir = new File("test_encrypted_classes");
        log.debug("testLoadEncryptedClassSuccessfully[2]: Encrypted classes directory: {}", encryptedDir.getAbsolutePath());

        log.debug("testLoadEncryptedClassSuccessfully[3]: Encrypting original class...");
        EncryptUtils.encryptClassFile(originalClassFile, encryptedDir, encryptionKey);
        log.debug("testLoadEncryptedClassSuccessfully[4]: Class successfully encrypted");

        log.debug("testLoadEncryptedClassSuccessfully[5]: Creating EncryptedClassLoader");
        EncryptedClassLoader loader = new EncryptedClassLoader(encryptionKey, encryptedDir, ClassLoader.getSystemClassLoader());

        log.debug("testLoadEncryptedClassSuccessfully[6]: Loading encrypted class TestClass...");
        Class<?> loadedClass = loader.loadClass("TestClass");
        assertNotNull(loadedClass);
        log.debug("testLoadEncryptedClassSuccessfully[7]: Class TestClass loaded successfully");

        log.debug("testLoadEncryptedClassSuccessfully[8]: Invoking sayHello method...");
        Object instance = loadedClass.getDeclaredConstructor().newInstance();
        String result = (String) loadedClass.getMethod("sayHello").invoke(instance);
        assertEquals("Hello, world!", result, "Method sayHello should return expected string");
        log.debug("testLoadEncryptedClassSuccessfully[9]: Method sayHello executed successfully, result: {}", result);

        log.info("TestClass успешно загружен и выполнен.");
    }

    @Test
    public void testLoadNonExistentClass() {
        log.debug("testLoadNonExistentClass[0]: Start test");

        File encryptedDir = new File("test_encrypted_classes");
        log.debug("testLoadNonExistentClass[1]: Encrypted classes directory: {}", encryptedDir.getAbsolutePath());

        log.debug("testLoadNonExistentClass[2]: Creating EncryptedClassLoader");
        EncryptedClassLoader loader = new EncryptedClassLoader(encryptionKey, encryptedDir, ClassLoader.getSystemClassLoader());

        log.debug("testLoadNonExistentClass[3]: Trying to load nonexistent class...");
        assertThrows(ClassNotFoundException.class, () -> loader.loadClass("ru.sberSchool.tasks.task7.NonExistentClass"));
        log.debug("testLoadNonExistentClass[4]: Expected exception ClassNotFoundException thrown");
    }

    @Test
    public void testInvalidDecryptionKey() throws Exception {
        log.debug("testInvalidDecryptionKey[0]: Start test");

        File originalClassFile = new File("src/main/resources/test_encrypted_classes/TestClass.class");
        log.debug("testInvalidDecryptionKey[1]: Original class file path: {}", originalClassFile.getAbsolutePath());

        File encryptedDir = new File("test_encrypted_classes");
        log.debug("testInvalidDecryptionKey[2]: Encrypted classes directory: {}", encryptedDir.getAbsolutePath());

        log.debug("testInvalidDecryptionKey[3]: Encrypting original class...");
        EncryptUtils.encryptClassFile(originalClassFile, encryptedDir, encryptionKey);
        log.debug("testInvalidDecryptionKey[4]: Class successfully encrypted");

        log.debug("testInvalidDecryptionKey[5]: Creating EncryptedClassLoader with invalid key");
        EncryptedClassLoader loader = new EncryptedClassLoader("wrongKey", encryptedDir, ClassLoader.getSystemClassLoader());

        log.debug("testInvalidDecryptionKey[6]: Trying to load class with invalid decryption key...");
        assertThrows(ClassFormatError.class, () -> loader.loadClass("TestClass"));
        log.debug("testInvalidDecryptionKey[7]: Expected exception ClassFormatError thrown");
    }
}

