package ru.sberSchool.tasks.task7.classLoaders;

import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * A custom {@link URLClassLoader} implementation for loading classes and resources
 * from plugin-specific directories or JAR files.
 *
 * @author Elland Ilia
 * @version 1.0
 */
@Slf4j
public class PluginClassLoader extends URLClassLoader {

    public PluginClassLoader(URL[] urls) {
        super(urls, ClassLoader.getSystemClassLoader());
    }

    /**
     * Loads the class with the specified name.
     *
     * @param name    the name of the class to load
     * @param resolve if {@code true}, the class will be resolved after being loaded
     * @return the resulting {@link Class} object
     * @throws ClassNotFoundException if the class could not be found
     */
    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        synchronized (getClassLoadingLock(name)) {
            // Check if the class has already been loaded
            Class<?> c = findLoadedClass(name);
            if (c == null) {
                try {
                    // Try to load the class from the plugin directory
                    c = findClass(name);
                    log.debug("Class {} loaded by PluginClassLoader", name);
                } catch (ClassNotFoundException e) {
                    // If not found, delegate to parent
                    c = super.loadClass(name, resolve);
                    log.debug("Class {} loaded by parent class loader", name);
                }
            }

            if (resolve) {
                resolveClass(c);
            }
            return c;
        }
    }
}
