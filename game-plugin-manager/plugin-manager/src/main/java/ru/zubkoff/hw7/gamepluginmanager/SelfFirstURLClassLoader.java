package ru.zubkoff.hw7.gamepluginmanager;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * URLClassLoader который не делегирует сразу загрузку класса, а сначала
 * пытается сам загрузить класс
 */
public class SelfFirstURLClassLoader extends URLClassLoader {

  public SelfFirstURLClassLoader(URL[] urls, ClassLoader parent) {
    super(urls, parent);
  }

  @Override
  public Class<?> loadClass(String name) throws ClassNotFoundException {    
    Class<?> loadedClass = super.findLoadedClass(name);
    if (loadedClass != null) {
      return loadedClass;
    }

    try {
      return super.findClass(name);
    } catch (ClassNotFoundException e) {
      return super.loadClass(name);
    }
  }

}