package org.owasp.webgoat.lessons.cryptography;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import javax.xml.bind.DatatypeConverter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class CryptoUtilTest {

  @Test
  public void testSigningAssignment() {
    try {
      KeyPair keyPair = CryptoUtil.generateKeyPair();
      RSAPublicKey rsaPubKey = (RSAPublicKey) keyPair.getPublic();
      PrivateKey privateKey =
          CryptoUtil.getPrivateKeyFromPEM(CryptoUtil.getPrivateKeyInPEM(keyPair));
      String modulus = DatatypeConverter.printHexBinary(rsaPubKey.getModulus().toByteArray());
      String signature = CryptoUtil.signMessage(modulus, privateKey);
      log.debug("public exponent {}", rsaPubKey.getPublicExponent());
      assertThat(CryptoUtil.verifyAssignment(modulus, signature, keyPair.getPublic())).isTrue();
    } catch (Exception e) {
      fail("Signing failed");
    }
  }
}
