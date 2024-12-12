package ru.sberSchool.tasks.task7.classLoaders;

import lombok.extern.slf4j.Slf4j;
import ru.sberSchool.tasks.Constants;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * A custom {@link ClassLoader} implementation that decrypts encrypted class files and loads them.
 * This class loader uses a key to decrypt class files stored in a specified directory.
 *
 * @author Elland Ilia
 * @version 1.0
 */
@Slf4j
public class EncryptedClassLoader extends ClassLoader {
    private final String key;
    private final File dir;

    /**
     * Creates a new {@code EncryptedClassLoader}.
     *
     * @param key    the decryption key to use.
     * @param dir    the directory where the encrypted class files are stored.
     * @param parent the parent class loader for delegation.
     */
    public EncryptedClassLoader(String key, File dir, ClassLoader parent) {
        super(parent);
        this.key = key;
        this.dir = dir;
    }

    /**
     * Finds and decrypts the specified class file.
     *
     * @param name the fully qualified name of the class to find.
     * @return the {@link Class} object representing the loaded class.
     * @throws ClassNotFoundException if the class cannot be found or loaded.
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            String path = name.replace('.', File.separatorChar) + Constants.CLASS_EXTENSION;
            File classFile = new File(dir, path);

            name = dir + "." + name;

            if (!classFile.exists()) {
                throw new ClassNotFoundException(String.format(Constants.CLASS_NOT_FOUND_MSG, name));
            }

            byte[] encryptedBytes = Files.readAllBytes(classFile.toPath());
            byte[] decryptedBytes = new byte[encryptedBytes.length];

            // Магическое число не расшифровываем (первые 4 байта)
            System.arraycopy(encryptedBytes, 0, decryptedBytes, 0, 4);

            decryptedBytes = decrypt(encryptedBytes, key);

            return defineClass(name, decryptedBytes, 0, decryptedBytes.length);
        } catch (IOException e) {
            throw new ClassNotFoundException(String.format(Constants.ERROR_DURING_CLASS_LOADING, name), e);
        }
    }

    /**
     * Loads the specified class, first attempting to find it locally,
     * and then delegating to the parent class loader if not found.
     *
     * @param name the fully qualified name of the class to load.
     * @return the {@link Class} object representing the loaded class.
     * @throws ClassNotFoundException if the class cannot be found.
     */
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        // Проверяем, не загружен ли класс уже
        Class<?> loadedClass = findLoadedClass(name);
        if (loadedClass == null) {
            try {
                // Пытаемся загрузить с помощью своего метода
                loadedClass = findClass(name);
            } catch (ClassNotFoundException e) {
                // Если не нашли класс, пробуем использовать родительский загрузчик
                loadedClass = getParent().loadClass(name);
            }
        }
        return loadedClass;
    }

    /**
     * Decrypts the given data using the specified key.
     *
     * @param data the encrypted byte array.
     * @param key  the decryption key.
     * @return the decrypted byte array.
     */
    private byte[] decrypt(byte[] data, String key) {
        log.debug("Decrypting data with key: {}", key);
        byte[] keyBytes = key.getBytes();
        byte[] decrypted = new byte[data.length];

        // Магическое число не расшифровываем
        System.arraycopy(data, 0, decrypted, 0, 4);

        for (int i = 4; i < data.length; i++) {
            decrypted[i] = (byte) (data[i] - keyBytes[i % keyBytes.length]);
            if (i < 10) {
                log.debug("Decrypt byte {}: original={}, decrypted={}", i, data[i], decrypted[i]);
            }
        }

        return decrypted;
    }

}

