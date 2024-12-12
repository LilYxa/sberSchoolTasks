package ru.sberSchool.tasks.task7;

import lombok.extern.slf4j.Slf4j;
import ru.sberSchool.tasks.Constants;
import ru.sberSchool.tasks.task7.classLoaders.PluginClassLoader;

import java.io.File;
import java.net.URL;

/**
 * The {@code PluginManager} class is responsible for managing and loading plugins from a specified root directory.
 *
 * @author Elland Ilia
 * @version 1.0
 *
 * @see Plugin
 */
@Slf4j
public class PluginManager {
    private final String pluginRootDirectory;

    public PluginManager(String pluginRootDirectory) {
        this.pluginRootDirectory = pluginRootDirectory;
    }

    /**
     * Loads a plugin by its name and class name.
     *
     * @param pluginName      the name of the plugin to load
     * @param pluginClassName the fully qualified class name of the plugin's main class
     * @return an instance of the {@link Plugin} class
     * @throws IllegalArgumentException if the plugin directory does not exist or the class does not
     *                                  implement the {@link Plugin} interface
     * @throws RuntimeException         if any other error occurs while loading the plugin
     */
    public Plugin load(String pluginName, String pluginClassName) {
        try {
            log.debug("load[0]: Attempting to load plugin: {} with class name: {}", pluginName, pluginClassName);

            File pluginDirectory = new File(pluginRootDirectory, pluginName);
            if (!pluginDirectory.exists() || !pluginDirectory.isDirectory()) {
                log.error("load[0]: Plugin directory not found: {}", pluginDirectory.getAbsolutePath());
                throw new IllegalArgumentException(String.format(Constants.PLUGIN_DIRECTORY_NON_EXIST_MSG, pluginDirectory.getAbsolutePath()));
            }

            File pluginFile = new File(pluginDirectory, "plugin.jar");

            URL[] urls = { pluginFile.toURI().toURL() };
            PluginClassLoader pluginClassLoader = new PluginClassLoader(urls);

            log.debug("load[1]: Created custom class loader for plugin: {}", pluginName);

            Class<?> pluginClass = pluginClassLoader.loadClass(pluginClassName);
            pluginClassLoader.close();
            log.debug("load[2]: Successfully loaded class: {} for plugin: {}", pluginClassName, pluginName);

            if (!Plugin.class.isAssignableFrom(pluginClass)) {
                log.error("load[1]: Class {} does not implement Plugin interface", pluginClassName);
                throw new IllegalArgumentException(String.format(Constants.CLASS_NOT_PLUGIN_MSG, pluginClassName));
            }

            return (Plugin) pluginClass.getDeclaredConstructor().newInstance();
        } catch (IllegalArgumentException e) {
            log.error("load[3]: Invalid argument: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("load[2]: Failed to load plugin: {} with class name: {}", pluginName, pluginClassName, e);
            throw new RuntimeException(String.format(Constants.FAILED_LOAD_PLUGIN, pluginName), e);
        }
    }
}
