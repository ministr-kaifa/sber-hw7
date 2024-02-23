package ru.zubkoff.encryptedclassloader.utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class SimpleEncryptor implements Encryptor {

  public enum Algorithm {
    AES;
  }

  private final Algorithm algorithm;

  public SimpleEncryptor(Algorithm algorithm) {
    this.algorithm = algorithm;
  }

  private SecretKey stringToSecretKey(String keyString) {
    byte[] keyBytes = keyString.getBytes(StandardCharsets.UTF_8);
    return new SecretKeySpec(keyBytes, algorithm.toString());
  }

  @Override
  public byte[] encrypt(String key, byte[] data) {
    try {
      Cipher cipher = Cipher.getInstance(algorithm.toString());
      cipher.init(Cipher.ENCRYPT_MODE, stringToSecretKey(key));
      return cipher.doFinal(data);
    } catch (IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException
        | InvalidKeyException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public byte[] decrypt(String key, byte[] data) {
    try {
      Cipher cipher = Cipher.getInstance(algorithm.toString());
      cipher.init(Cipher.DECRYPT_MODE, stringToSecretKey(key));
      return cipher.doFinal(data);
    } catch (IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException
        | InvalidKeyException e) {
      throw new RuntimeException(e);
    }
  }
}
