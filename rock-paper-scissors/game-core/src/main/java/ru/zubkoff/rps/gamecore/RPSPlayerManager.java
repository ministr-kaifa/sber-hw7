package ru.zubkoff.rps.gamecore;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.jar.JarFile;

import ru.zubkoff.rps.api.RPSPlayer;

public class RPSPlayerManager {
  private final Path playerRootDirectory;

  public RPSPlayerManager(Path playerRootDirectory) {
    this.playerRootDirectory = playerRootDirectory;
  }

  public Path getPlayerRootDirectory() {
    return playerRootDirectory;
  }

  /**
   * Загружает класс игрока из
   * playerRootDirectory/jarName.jar/playerClassName.class
   *
   * @param jarName         имя .jar файла
   * @return экземпляр загруженного класса игрока
   * @throws ClassNotFoundException если класс не найден
   * @throws MalformedURLException
   * @throws IOException
   */
  public RPSPlayer loadPlayer(String jarName) {
    var pathTotargetClassJar = playerRootDirectory.resolve(jarName + ".jar");
    URLClassLoader classsLoader;
    JarFile targetClassJar;
    try {
      classsLoader = new URLClassLoader(new URL[] { pathTotargetClassJar.toUri().toURL() });
      targetClassJar = new JarFile(pathTotargetClassJar.toFile());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    var player = Collections.list(targetClassJar.entries()).stream()
      .filter(jarEntry -> !jarEntry.isDirectory())
      .filter(jarEntry -> jarEntry.getName().endsWith(".class"))
      .map(classFile -> classFile.getName().replace("/", "."))
      .map(classFileName -> classFileName.substring(0, classFileName.length() - ".class".length()))
      .map(className -> {
        try {
          return classsLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
          throw new RuntimeException(e);
        }
      })
      .filter(RPSPlayer.class::isAssignableFrom)
      .map(playerClazz -> {
        try {
          return playerClazz.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
            | InvocationTargetException
            | NoSuchMethodException | SecurityException e) {
          throw new RuntimeException(e);
        }
      })
      .map(RPSPlayer.class::cast)
      .findAny();

    try {
      targetClassJar.close();
      classsLoader.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return player.orElseThrow(() -> {throw new RuntimeException("No valid player class found");});
  }

  public List<String> getAllJarFileNames() {
    return Arrays.asList(playerRootDirectory.toFile().listFiles((dir, fileName) -> fileName.endsWith(".jar"))).stream()
      .map(File::getName)
      .map(fileName -> fileName.substring(0, fileName.length() - ".jar".length()))
      .toList();
  }

}
