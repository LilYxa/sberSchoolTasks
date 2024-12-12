package ru.sberSchool.tasks.task7.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Utility class for encrypting Java class files.
 *
 * @author Elland Ilia
 * @version 1.0
 */
@Slf4j
public class EncryptUtils {

    /**
     * Encrypts a Java class file and saves the encrypted file to a specified directory.
     *
     * @param originalClassFile the original class file to be encrypted.
     * @param encryptedDir      the directory where the encrypted file will be saved.
     * @param key               the encryption key to use.
     * @throws IOException if an I/O error occurs during file read or write operations.
     */
    public static void encryptClassFile(File originalClassFile, File encryptedDir, String key) throws IOException {
        File encryptedClassFile = new File(encryptedDir, "TestClass.class");
        encryptedClassFile.getParentFile().mkdirs();
        byte[] keyBytes = key.getBytes();

        byte[] originalBytes = Files.readAllBytes(originalClassFile.toPath());

        // Магическое число не шифруем (первые 4 байта)
        byte[] encryptedBytes = new byte[originalBytes.length];
        System.arraycopy(originalBytes, 0, encryptedBytes, 0, 4);

        // Шифруем остальные байты (начиная с 5-го)
        for (int i = 4; i < originalBytes.length; i++) {
            encryptedBytes[i] = (byte) (originalBytes[i] + keyBytes[i % keyBytes.length]);
        }

        Files.write(encryptedClassFile.toPath(), encryptedBytes);

        log.info("Файл зашифрован: {}", encryptedClassFile.getAbsolutePath());
    }

}
