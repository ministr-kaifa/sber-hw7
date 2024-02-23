package ru.zubkoff.encryptedclassloader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import ru.zubkoff.encryptedclassloader.utils.Encryptor;

public class EncryptedClassLoader extends ClassLoader {
  private final String key;
  private final Path dir;
  private final Encryptor encryptor;

  public EncryptedClassLoader(String key, Path dir, ClassLoader parent, Encryptor encryptor) {
    super(parent);
    this.key = key;
    this.dir = dir;
    this.encryptor = encryptor;
  }

  @Override
  protected Class<?> findClass(String name) throws ClassNotFoundException {
    try {
      byte[] encryptedClassData = encryptor.decrypt(key, Files.readAllBytes(dir.resolve(name + ".class")));
      return defineClass(name, encryptedClassData, 0, encryptedClassData.length);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }


}
