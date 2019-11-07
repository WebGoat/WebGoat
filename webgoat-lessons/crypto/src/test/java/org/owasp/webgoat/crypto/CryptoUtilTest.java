package org.owasp.webgoat.crypto;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;

import javax.xml.bind.DatatypeConverter;

import org.junit.jupiter.api.Test;

public class CryptoUtilTest {

	@Test
	public void testSigningAssignment() {
		try {
			KeyPair keyPair = CryptoUtil.generateKeyPair();
			RSAPublicKey rsaPubKey = (RSAPublicKey) keyPair.getPublic();
			PrivateKey privateKey = CryptoUtil.getPrivateKeyFromPEM(CryptoUtil.getPrivateKeyInPEM(keyPair));
			String modulus = DatatypeConverter.printHexBinary(rsaPubKey.getModulus().toByteArray());
			String signature = CryptoUtil.signMessage(modulus, privateKey);
			assertTrue(CryptoUtil.verifyAssignment(modulus, signature, keyPair.getPublic()));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

}