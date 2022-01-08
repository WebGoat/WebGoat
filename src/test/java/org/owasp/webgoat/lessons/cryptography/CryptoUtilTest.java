package org.owasp.webgoat.lessons.cryptography;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.xml.bind.DatatypeConverter;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
public class CryptoUtilTest {

	@Test
	public void testSigningAssignment() {
		try {
			KeyPair keyPair = CryptoUtil.generateKeyPair();
			RSAPublicKey rsaPubKey = (RSAPublicKey) keyPair.getPublic();
			PrivateKey privateKey = CryptoUtil.getPrivateKeyFromPEM(CryptoUtil.getPrivateKeyInPEM(keyPair));
			String modulus = DatatypeConverter.printHexBinary(rsaPubKey.getModulus().toByteArray());
			String signature = CryptoUtil.signMessage(modulus, privateKey);
			log.debug("public exponent {}", rsaPubKey.getPublicExponent());
			assertTrue(CryptoUtil.verifyAssignment(modulus, signature, keyPair.getPublic()));
		} catch (Exception e) {
			log.error("signing failed", e);;
			fail();
		}
	}

}
