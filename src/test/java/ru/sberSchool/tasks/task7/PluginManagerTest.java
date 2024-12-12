package ru.sberSchool.tasks.task7;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class PluginManagerTest {

    @TempDir
    Path tempDir;

    private PluginManager pluginManager;

    @BeforeEach
    public void setUp() {
        pluginManager = new PluginManager(tempDir.toString());
    }

    @Test
    public void testLoadValidPlugin() throws Exception {
        log.debug("testLoadValidPlugin[0]: start test");
        File pluginDir = new File(tempDir.toFile(), "testPlugin");
        assertTrue(pluginDir.mkdir());

        createPluginJar(pluginDir, "TestPlugin", "public class TestPlugin implements ru.sberSchool.tasks.task7.Plugin { public void doUsefull() { System.out.println(\"Hello from plugin!\"); }}");

        Plugin plugin = pluginManager.load("testPlugin", "TestPlugin");
        assertNotNull(plugin);
        assertDoesNotThrow(plugin::doUsefull);
        log.debug("testLoadValidPlugin[1]: Successfully loaded and executed plugin.");
    }

    @Test
    public void testLoadPluginClassNotFound() {
        log.debug("testLoadPluginClassNotFound[0]: start test");
        File pluginDir = new File(tempDir.toFile(), "missingPlugin");
        assertTrue(pluginDir.mkdir());

        Exception exception = assertThrows(RuntimeException.class, () -> pluginManager.load("missingPlugin", "NonExistentClass"));
        assertTrue(exception.getMessage().contains("Failed to load plugin"));
        log.debug("testLoadPluginClassNotFound[1]: Exception message: {}", exception.getMessage());
    }

    @Test
    public void testLoadPluginInvalidInterface() throws Exception {
        log.debug("testLoadPluginInvalidInterface[0]: start test");
        File pluginDir = new File(tempDir.toFile(), "invalidPlugin");
        assertTrue(pluginDir.mkdir());

        createPluginJar(pluginDir, "InvalidPlugin", "public class InvalidPlugin { public void doSomething() {} }");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> pluginManager.load("invalidPlugin", "InvalidPlugin"));
        assertTrue(exception.getMessage().contains("does not implement Plugin interface"));
        log.debug("testLoadPluginInvalidInterface[1]: Exception message: {}", exception.getMessage());
    }

    @Test
    public void testLoadPluginDirectoryNotFound() {
        log.debug("testLoadPluginDirectoryNotFound[0]: start test");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> pluginManager.load("nonExistentPlugin", "SomeClass"));
        assertTrue(exception.getMessage().contains("Plugin directory does not exist"));
        log.debug("testLoadPluginDirectoryNotFound[1]: Exception message: {}", exception.getMessage());
    }

    private void createPluginJar(File pluginDir, String className, String classContent) throws IOException {
        File jarFile = new File(pluginDir, "plugin.jar");
        try (JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(jarFile))) {
            String classFileName = className + ".class";
            jarOutputStream.putNextEntry(new JarEntry(classFileName));
            jarOutputStream.write(compileClass(className, classContent));
            jarOutputStream.closeEntry();
            log.debug("createPluginJar: Created plugin jar file at {}", jarFile.getAbsolutePath());
        }
    }

    private byte[] compileClass(String className, String classContent) throws IOException {
        File sourceFile = new File(tempDir.toFile(), className + ".java");
        Files.writeString(sourceFile.toPath(), classContent);

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null)) {
            Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(Collections.singletonList(sourceFile));
            JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null, null, null, compilationUnits);
            if (!task.call()) {
                throw new IOException("Compilation failed");
            }
        }

        File compiledClassFile = new File(tempDir.toFile(), className + ".class");
        return Files.readAllBytes(compiledClassFile.toPath());
    }
}
