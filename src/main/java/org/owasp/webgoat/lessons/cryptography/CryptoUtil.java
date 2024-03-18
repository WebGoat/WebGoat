package org.owasp.webgoat.lessons.cryptography;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAKeyGenParameterSpec;
import java.util.Base64;
import javax.xml.bind.DatatypeConverter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CryptoUtil {

  private static final BigInteger[] FERMAT_PRIMES = {
    BigInteger.valueOf(3),
    BigInteger.valueOf(5),
    BigInteger.valueOf(17),
    BigInteger.valueOf(257),
    BigInteger.valueOf(65537)
  };

  public static KeyPair generateKeyPair()
      throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
    RSAKeyGenParameterSpec kpgSpec =
        new RSAKeyGenParameterSpec(
            2048, FERMAT_PRIMES[new SecureRandom().nextInt(FERMAT_PRIMES.length)]);
    keyPairGenerator.initialize(kpgSpec);
    // keyPairGenerator.initialize(2048);
    return keyPairGenerator.generateKeyPair();
  }

  public static String getPrivateKeyInPEM(KeyPair keyPair) {
    String encodedString = "-----BEGIN PRIVATE KEY-----\n";
    encodedString =
        encodedString
            + new String(
                Base64.getEncoder().encode(keyPair.getPrivate().getEncoded()),
                Charset.forName("UTF-8"))
            + "\n";
    encodedString = encodedString + "-----END PRIVATE KEY-----\n";
    return encodedString;
  }

  public static String signMessage(String message, PrivateKey privateKey) {

    log.debug("start signMessage");
    String signature = null;

    try {
      // Initiate signature verification
      Signature instance = Signature.getInstance("SHA256withRSA");
      instance.initSign(privateKey);
      instance.update(message.getBytes("UTF-8"));

      // actual verification against signature
      signature = new String(Base64.getEncoder().encode(instance.sign()), Charset.forName("UTF-8"));

      log.info("signe the signature with result: {}", signature);
    } catch (Exception e) {
      log.error("Signature signing failed", e);
    }

    log.debug("end signMessage");
    return signature;
  }

  public static boolean verifyMessage(
      String message, String base64EncSignature, PublicKey publicKey) {

    log.debug("start verifyMessage");
    boolean result = false;

    try {

      base64EncSignature = base64EncSignature.replace("\r", "").replace("\n", "").replace(" ", "");
      // get raw signature from base64 encrypted string in header
      byte[] decodedSignature = Base64.getDecoder().decode(base64EncSignature);

      // Initiate signature verification
      Signature instance = Signature.getInstance("SHA256withRSA");
      instance.initVerify(publicKey);
      instance.update(message.getBytes("UTF-8"));

      // actual verification against signature
      result = instance.verify(decodedSignature);

      log.info("Verified the signature with result: {}", result);
    } catch (Exception e) {
      log.error("Signature verification failed", e);
    }

    log.debug("end verifyMessage");
    return result;
  }

  public static boolean verifyAssignment(String modulus, String signature, PublicKey publicKey) {

    /* first check if the signature is correct, i.e. right private key and right hash */
    boolean result = false;

    if (modulus != null && signature != null) {
      result = verifyMessage(modulus, signature, publicKey);

      /*
       * next check if the submitted modulus is the correct modulus of the public key
       */
      RSAPublicKey rsaPubKey = (RSAPublicKey) publicKey;
      if (modulus.length() == 512) {
        modulus = "00".concat(modulus);
      }
      result =
          result
              && (DatatypeConverter.printHexBinary(rsaPubKey.getModulus().toByteArray())
                  .equals(modulus.toUpperCase()));
    }
    return result;
  }

  public static PrivateKey getPrivateKeyFromPEM(String privateKeyPem)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    privateKeyPem = privateKeyPem.replace("-----BEGIN PRIVATE KEY-----", "");
    privateKeyPem = privateKeyPem.replace("-----END PRIVATE KEY-----", "");
    privateKeyPem = privateKeyPem.replace("\n", "").replace("\r", "");

    byte[] decoded = Base64.getDecoder().decode(privateKeyPem);

    PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
    KeyFactory kf = KeyFactory.getInstance("RSA");
    return kf.generatePrivate(spec);
  }
}
