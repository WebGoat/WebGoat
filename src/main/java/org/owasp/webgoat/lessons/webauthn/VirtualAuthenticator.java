/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.webauthn;

import static org.owasp.webgoat.lessons.webauthn.WebAuthnSupport.base64Url;
import static org.owasp.webgoat.lessons.webauthn.WebAuthnSupport.concat;
import static org.owasp.webgoat.lessons.webauthn.WebAuthnSupport.sha256;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Signature;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A software "security key" plus the part of the browser that builds {@code clientDataJSON}. It
 * creates ES256 credentials with "none" attestation, just like the virtual authenticator in the
 * Chrome DevTools WebAuthn panel. The lesson uses it so the assignments work without hardware.
 */
public class VirtualAuthenticator {

  public static final UUID AAGUID = UUID.nameUUIDFromBytes("WebGoat virtual security key".getBytes(StandardCharsets.UTF_8));

  private static final ObjectMapper MAPPER = new ObjectMapper();

  private record Key(KeyPair keyPair, byte[] userHandle, AtomicLong counter) {}

  private final Map<String, Key> keys = new ConcurrentHashMap<>();

  /** Creates a new key pair and returns its base64url credential id. */
  public String createKey(byte[] userHandle, long initialCounter) {
    try {
      KeyPairGenerator generator = KeyPairGenerator.getInstance("EC");
      generator.initialize(new ECGenParameterSpec("secp256r1"));
      String credentialId = base64Url(WebAuthnSupport.randomBytes(32));
      keys.put(
          credentialId,
          new Key(generator.generateKeyPair(), userHandle, new AtomicLong(initialCounter)));
      return credentialId;
    } catch (GeneralSecurityException e) {
      throw new IllegalStateException(e);
    }
  }

  public boolean hasCredential(String credentialId) {
    return keys.containsKey(credentialId);
  }

  public Map<Object, Object> coseKey(String credentialId) {
    ECPublicKey publicKey = (ECPublicKey) keys.get(credentialId).keyPair().getPublic();
    Map<Object, Object> cose = new LinkedHashMap<>();
    cose.put(1L, 2L); // kty: EC2
    cose.put(3L, WebAuthnSupport.ES256); // alg
    cose.put(-1L, 1L); // crv: P-256
    cose.put(-2L, unsigned32(publicKey.getW().getAffineX()));
    cose.put(-3L, unsigned32(publicKey.getW().getAffineY()));
    return cose;
  }

  /** Equivalent of {@code navigator.credentials.create()}. */
  public Map<String, Object> create(
      String origin, String rpId, String challenge, byte[] userHandle) {
    String credentialId = createKey(userHandle, 0);
    byte[] clientDataJson = clientDataJson("webauthn.create", challenge, origin);
    byte[] credentialIdBytes = WebAuthnSupport.fromBase64Url(credentialId);
    byte[] attestedCredentialData =
        concat(
            uuidBytes(AAGUID),
            new byte[] {0, (byte) credentialIdBytes.length},
            credentialIdBytes,
            Cbor.encode(coseKey(credentialId)));
    byte[] authData =
        concat(
            authDataHeader(
                rpId, WebAuthnSupport.FLAG_UP | WebAuthnSupport.FLAG_AT, 0),
            attestedCredentialData);
    Map<String, Object> attestationObject = new LinkedHashMap<>();
    attestationObject.put("fmt", "none");
    attestationObject.put("attStmt", Map.of());
    attestationObject.put("authData", authData);

    Map<String, Object> response = new LinkedHashMap<>();
    response.put("clientDataJSON", base64Url(clientDataJson));
    response.put("attestationObject", base64Url(Cbor.encode(attestationObject)));
    response.put("transports", List.of("usb"));
    return credential(credentialId, response);
  }

  /** Equivalent of {@code navigator.credentials.get()}. */
  public Map<String, Object> get(
      String origin, String rpId, String challenge, Collection<String> allowCredentials) {
    String credentialId =
        allowCredentials.stream()
            .filter(keys::containsKey)
            .findFirst()
            .orElseThrow(
                () ->
                    new IllegalStateException(
                        "NotAllowedError: this security key holds none of the allowed"
                            + " credentials"));
    Key key = keys.get(credentialId);
    byte[] clientDataJson = clientDataJson("webauthn.get", challenge, origin);
    byte[] authData =
        authDataHeader(rpId, WebAuthnSupport.FLAG_UP, key.counter().incrementAndGet());
    byte[] signature = sign(key.keyPair(), concat(authData, sha256(clientDataJson)));

    Map<String, Object> response = new LinkedHashMap<>();
    response.put("clientDataJSON", base64Url(clientDataJson));
    response.put("authenticatorData", base64Url(authData));
    response.put("signature", base64Url(signature));
    response.put("userHandle", base64Url(key.userHandle()));
    return credential(credentialId, response);
  }

  private static Map<String, Object> credential(
      String credentialId, Map<String, Object> response) {
    Map<String, Object> credential = new LinkedHashMap<>();
    credential.put("id", credentialId);
    credential.put("rawId", credentialId);
    credential.put("type", "public-key");
    credential.put("authenticatorAttachment", "cross-platform");
    credential.put("response", response);
    credential.put("clientExtensionResults", Map.of());
    return credential;
  }

  private static byte[] clientDataJson(String type, String challenge, String origin) {
    Map<String, Object> clientData = new LinkedHashMap<>();
    clientData.put("type", type);
    clientData.put("challenge", challenge);
    clientData.put("origin", origin);
    clientData.put("crossOrigin", false);
    try {
      return MAPPER.writeValueAsBytes(clientData);
    } catch (JsonProcessingException e) {
      throw new IllegalStateException(e);
    }
  }

  private static byte[] authDataHeader(String rpId, int flags, long counter) {
    return ByteBuffer.allocate(37)
        .put(sha256(rpId.getBytes(StandardCharsets.UTF_8)))
        .put((byte) flags)
        .putInt((int) counter)
        .array();
  }

  private static byte[] sign(KeyPair keyPair, byte[] data) {
    try {
      Signature signature = Signature.getInstance("SHA256withECDSA");
      signature.initSign(keyPair.getPrivate());
      signature.update(data);
      return signature.sign();
    } catch (GeneralSecurityException e) {
      throw new IllegalStateException(e);
    }
  }

  private static byte[] unsigned32(BigInteger value) {
    byte[] bytes = value.toByteArray();
    byte[] result = new byte[32];
    int length = Math.min(bytes.length, 32);
    System.arraycopy(bytes, bytes.length - length, result, 32 - length, length);
    return result;
  }

  private static byte[] uuidBytes(UUID uuid) {
    return ByteBuffer.allocate(16)
        .putLong(uuid.getMostSignificantBits())
        .putLong(uuid.getLeastSignificantBits())
        .array();
  }
}
