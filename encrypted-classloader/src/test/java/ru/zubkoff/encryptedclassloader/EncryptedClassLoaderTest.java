package ru.zubkoff.encryptedclassloader;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import ru.zubkoff.encryptedclassloader.utils.Encryptor;
import ru.zubkoff.encryptedclassloader.utils.SimpleEncryptor;
import ru.zubkoff.encryptedclassloader.utils.SimpleFileEncryptor;
import ru.zubkoff.encryptedclassloader.utils.SimpleEncryptor.Algorithm;

@TestInstance(Lifecycle.PER_CLASS)
class EncryptedClassLoaderTest {

  String key = "passwordpassword";
  Encryptor encryptor = new SimpleEncryptor(Algorithm.AES);
  SimpleFileEncryptor fileEncryptor = new SimpleFileEncryptor(encryptor);
  File encryptedClassFile = new File(".\\src\\test\\resources\\CryptedClass.class");

  @BeforeAll
  void onStart() throws IOException {
    fileEncryptor.encrypt(encryptedClassFile, key);
  }

  @AfterAll
  void onEnd() throws IOException {
    fileEncryptor.decrypt(encryptedClassFile, key);
  }

  @Test
  void givenEncryptedClassFile_whenEncryptedLoaderFindClass_thenExpectedToStringValue()
      throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException,
      IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

    // given
    var loader = new EncryptedClassLoader(key, Path.of(".\\src\\test\\resources\\"), this.getClass().getClassLoader(),
      encryptor);

    // when
    var decryptedClassInstance = loader.findClass("CryptedClass").getConstructor().newInstance();

    // then
    assertEquals("Decrypted!", decryptedClassInstance.toString());
  }

}
