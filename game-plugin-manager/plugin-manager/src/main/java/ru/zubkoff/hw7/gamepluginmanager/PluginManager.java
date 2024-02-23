package ru.zubkoff.hw7.gamepluginmanager;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;

public class PluginManager {
  private final String pluginRootDirectory;

  public PluginManager(String pluginRootDirectory) {
    this.pluginRootDirectory = pluginRootDirectory;
  }

  public String getPluginRootDirectory() {
    return pluginRootDirectory;
  }

  /**
   * Загружает плагин из
   * pluginRootDirectory/pluginName/pluginClassName.class
   *
   * @param pluginName      конечный подпакет класса плагина
   * @param pluginClassName полное имя класса плагина
   * @return экземпляр загруженного плагина
   * @throws ClassNotFoundException если класс не найден
   */
  public Plugin load(String pluginName, String pluginClassName) throws ClassNotFoundException {
    var targetClassFilePath = Paths.get(pluginRootDirectory, pluginName, pluginClassName + ".class");
    Class<?> targetClazz;
    try (URLClassLoader classLoader = new SelfFirstURLClassLoader(new URL[] { targetClassFilePath.toUri().toURL() },
        this.getClass().getClassLoader())) {
      targetClazz = classLoader.loadClass(pluginClassName);
    } catch (MalformedURLException e) {
      throw new RuntimeException("Invalid file path", e);
    } catch (IOException e) {
      throw new RuntimeException("Cant close classLoader", e);
    }

    if (!Plugin.class.isAssignableFrom(targetClazz)) {
      throw new NotAPluginException("No Plugin implementation class found in given file");
    }

    Plugin plugin;
    try {
      plugin = (Plugin) targetClazz.getConstructor().newInstance();
    } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
        | IllegalArgumentException | InvocationTargetException e) {
      throw new RuntimeException("Cant create a plugin instance", e);
    }

    return plugin;
  }

}
