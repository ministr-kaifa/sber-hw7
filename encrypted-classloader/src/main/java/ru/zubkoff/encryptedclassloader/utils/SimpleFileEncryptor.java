package ru.zubkoff.encryptedclassloader.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class SimpleFileEncryptor {

  private final Encryptor encryptor;

  public SimpleFileEncryptor(Encryptor encryptor) {
    this.encryptor = encryptor;
  }

  public void encrypt(File file, String key) throws IOException {
    Files.write(file.toPath(), encryptor.encrypt(key, Files.readAllBytes(file.toPath())));
  }

  public void decrypt(File file, String key) throws IOException {
    Files.write(file.toPath(), encryptor.decrypt(key, Files.readAllBytes(file.toPath())));
  }
}
