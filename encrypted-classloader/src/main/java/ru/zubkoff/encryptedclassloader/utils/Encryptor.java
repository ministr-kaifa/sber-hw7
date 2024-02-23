package ru.zubkoff.encryptedclassloader.utils;

public interface Encryptor {

  byte[] decrypt(String key, byte[] data);

  byte[] encrypt(String key, byte[] data);

}
