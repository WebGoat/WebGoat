/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.webauthn;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;

/**
 * Parsing and verification building blocks for WebAuthn. The assignment endpoints compose these
 * steps themselves so the (missing) checks are visible in the endpoint code.
 */
public final class WebAuthnSupport {

  public static final long ES256 = -7;
  public static final long EDDSA = -8;
  public static final long RS256 = -257;

  public static final int FLAG_UP = 0x01;
  public static final int FLAG_UV = 0x04;
  public static final int FLAG_BE = 0x08;
  public static final int FLAG_BS = 0x10;
  public static final int FLAG_AT = 0x40;
  public static final int FLAG_ED = 0x80;

  private static final ObjectMapper MAPPER = new ObjectMapper();
  private static final SecureRandom RANDOM = new SecureRandom();
  private static final byte[] ED25519_X509_PREFIX =
      HexFormat.of().parseHex("302a300506032b6570032100");
  private static final String AAGUID_EXTENSION_OID = "1.3.6.1.4.1.45724.1.1.4";

  private WebAuthnSupport() {}

  /**
   * The relying party as seen by the browser. WebGoat can be reached on any host name, so the
   * expected origin is derived from the request. A production relying party must use a fixed,
   * configured RP ID and list of allowed origins instead.
   */
  public record RelyingParty(String id, String name, String origin) {

    public static RelyingParty from(HttpServletRequest request, String name) {
      String scheme = request.getScheme();
      String host = request.getServerName();
      int port = request.getServerPort();
      boolean defaultPort =
          ("http".equals(scheme) && port == 80) || ("https".equals(scheme) && port == 443);
      String origin = scheme + "://" + host + (defaultPort ? "" : ":" + port);
      return new RelyingParty(host, name, origin);
    }

    public byte[] idHash() {
      return sha256(id.getBytes(StandardCharsets.UTF_8));
    }
  }

  public record ClientData(String type, String challenge, String origin, byte[] raw) {

    public byte[] hash() {
      return sha256(raw);
    }
  }

  public record AuthenticatorData(
      byte[] raw,
      byte[] rpIdHash,
      int flags,
      long signCount,
      byte[] aaguid,
      byte[] credentialId,
      Map<Object, Object> credentialPublicKey) {

    public boolean userPresent() {
      return (flags & FLAG_UP) != 0;
    }

    public boolean userVerified() {
      return (flags & FLAG_UV) != 0;
    }

    public boolean backupEligible() {
      return (flags & FLAG_BE) != 0;
    }

    public boolean backedUp() {
      return (flags & FLAG_BS) != 0;
    }

    public boolean hasAttestedCredentialData() {
      return (flags & FLAG_AT) != 0;
    }

    public String aaguidAsUuid() {
      if (aaguid == null) {
        return null;
      }
      String hex = HexFormat.of().formatHex(aaguid);
      return "%s-%s-%s-%s-%s"
          .formatted(
              hex.substring(0, 8),
              hex.substring(8, 12),
              hex.substring(12, 16),
              hex.substring(16, 20),
              hex.substring(20));
    }

    public String flagsDescription() {
      return "UP=%s, UV=%s, BE=%s, BS=%s, AT=%s"
          .formatted(
              userPresent(),
              userVerified(),
              backupEligible(),
              backedUp(),
              hasAttestedCredentialData());
    }
  }

  public record AttestationObject(
      String fmt, Map<Object, Object> attStmt, AuthenticatorData authData) {}

  public static String randomChallenge() {
    byte[] challenge = new byte[32];
    RANDOM.nextBytes(challenge);
    return base64Url(challenge);
  }

  public static byte[] randomBytes(int length) {
    byte[] bytes = new byte[length];
    RANDOM.nextBytes(bytes);
    return bytes;
  }

  public static String base64Url(byte[] data) {
    return Base64.getUrlEncoder().withoutPadding().encodeToString(data);
  }

  public static byte[] fromBase64Url(String data) {
    if (data == null) {
      throw new IllegalArgumentException("Missing base64url value");
    }
    // be lenient for values copied from tools that use standard base64 with padding
    String normalized = data.trim().replace('+', '-').replace('/', '_').replace("=", "");
    return Base64.getUrlDecoder().decode(normalized);
  }

  public static byte[] sha256(byte[] data) {
    try {
      return MessageDigest.getInstance("SHA-256").digest(data);
    } catch (GeneralSecurityException e) {
      throw new IllegalStateException(e);
    }
  }

  public static byte[] concat(byte[]... parts) {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    for (byte[] part : parts) {
      out.writeBytes(part);
    }
    return out.toByteArray();
  }

  /** Constant time comparison of two base64url encoded values. */
  public static boolean sameBase64Url(String expected, String actual) {
    if (expected == null || actual == null) {
      return false;
    }
    return MessageDigest.isEqual(fromBase64Url(expected), fromBase64Url(actual));
  }

  public static ClientData parseClientData(String clientDataJsonB64) {
    byte[] raw = fromBase64Url(clientDataJsonB64);
    try {
      JsonNode node = MAPPER.readTree(raw);
      return new ClientData(
          node.path("type").asText(null),
          node.path("challenge").asText(null),
          node.path("origin").asText(null),
          raw);
    } catch (IOException e) {
      throw new IllegalArgumentException("clientDataJSON is not valid JSON", e);
    }
  }

  @SuppressWarnings("unchecked")
  public static AuthenticatorData parseAuthenticatorData(byte[] raw) {
    if (raw.length < 37) {
      throw new IllegalArgumentException("authenticatorData is too short");
    }
    ByteBuffer buffer = ByteBuffer.wrap(raw);
    byte[] rpIdHash = new byte[32];
    buffer.get(rpIdHash);
    int flags = buffer.get() & 0xff;
    long signCount = buffer.getInt() & 0xffffffffL;
    byte[] aaguid = null;
    byte[] credentialId = null;
    Map<Object, Object> publicKey = null;
    if ((flags & FLAG_AT) != 0) {
      if (buffer.remaining() < 18) {
        throw new IllegalArgumentException("attested credential data is too short");
      }
      aaguid = new byte[16];
      buffer.get(aaguid);
      int idLength = buffer.getShort() & 0xffff;
      if (idLength > 1023 || buffer.remaining() < idLength) {
        throw new IllegalArgumentException("Invalid credential id length");
      }
      credentialId = new byte[idLength];
      buffer.get(credentialId);
      Cbor.Item key = Cbor.decodeFirst(raw, buffer.position());
      if (!(key.value() instanceof Map<?, ?>)) {
        throw new IllegalArgumentException("credentialPublicKey is not a COSE key");
      }
      publicKey = (Map<Object, Object>) key.value();
      buffer.position(buffer.position() + key.length());
    }
    if ((flags & FLAG_ED) != 0) {
      Cbor.Item extensions = Cbor.decodeFirst(raw, buffer.position());
      buffer.position(buffer.position() + extensions.length());
    }
    if (buffer.hasRemaining()) {
      throw new IllegalArgumentException("Unexpected trailing bytes in authenticatorData");
    }
    return new AuthenticatorData(
        raw, rpIdHash, flags, signCount, aaguid, credentialId, publicKey);
  }

  @SuppressWarnings("unchecked")
  public static AttestationObject parseAttestationObject(String attestationObjectB64) {
    Object decoded = Cbor.decode(fromBase64Url(attestationObjectB64));
    if (!(decoded instanceof Map<?, ?> map)
        || !(map.get("fmt") instanceof String fmt)
        || !(map.get("attStmt") instanceof Map<?, ?> attStmt)
        || !(map.get("authData") instanceof byte[] authData)) {
      throw new IllegalArgumentException("attestationObject must contain fmt, attStmt, authData");
    }
    return new AttestationObject(
        fmt, (Map<Object, Object>) attStmt, parseAuthenticatorData(authData));
  }

  public static long coseAlgorithm(Map<Object, Object> coseKey) {
    if (coseKey.get(3L) instanceof Long alg) {
      return alg;
    }
    throw new IllegalArgumentException("COSE key has no algorithm");
  }

  /** Converts a COSE_Key (RFC 9052/9053) into a Java public key. */
  public static PublicKey toPublicKey(Map<Object, Object> coseKey) {
    try {
      long kty = (Long) coseKey.get(1L);
      long alg = coseAlgorithm(coseKey);
      if (kty == 2 && alg == ES256) {
        if (!Long.valueOf(1).equals(coseKey.get(-1L))) {
          throw new IllegalArgumentException("Only the P-256 curve is supported for ES256");
        }
        BigInteger x = new BigInteger(1, (byte[]) coseKey.get(-2L));
        BigInteger y = new BigInteger(1, (byte[]) coseKey.get(-3L));
        AlgorithmParameters parameters = AlgorithmParameters.getInstance("EC");
        parameters.init(new ECGenParameterSpec("secp256r1"));
        ECParameterSpec spec = parameters.getParameterSpec(ECParameterSpec.class);
        // KeyFactory validates that the point is on the curve
        return KeyFactory.getInstance("EC")
            .generatePublic(new ECPublicKeySpec(new ECPoint(x, y), spec));
      }
      if (kty == 1 && alg == EDDSA) {
        if (!Long.valueOf(6).equals(coseKey.get(-1L))) {
          throw new IllegalArgumentException("Only Ed25519 is supported for EdDSA");
        }
        byte[] encoded = concat(ED25519_X509_PREFIX, (byte[]) coseKey.get(-2L));
        return KeyFactory.getInstance("Ed25519").generatePublic(new X509EncodedKeySpec(encoded));
      }
      if (kty == 3 && alg == RS256) {
        BigInteger modulus = new BigInteger(1, (byte[]) coseKey.get(-1L));
        BigInteger exponent = new BigInteger(1, (byte[]) coseKey.get(-2L));
        return KeyFactory.getInstance("RSA")
            .generatePublic(new RSAPublicKeySpec(modulus, exponent));
      }
      throw new IllegalArgumentException(
          "Unsupported COSE key (kty=%d, alg=%d)".formatted(kty, alg));
    } catch (GeneralSecurityException | ClassCastException | NullPointerException e) {
      throw new IllegalArgumentException("Invalid COSE public key", e);
    }
  }

  public static boolean verifySignature(
      long alg, PublicKey publicKey, byte[] signedData, byte[] signature) {
    try {
      Signature verifier = Signature.getInstance(javaAlgorithmName(alg));
      verifier.initVerify(publicKey);
      verifier.update(signedData);
      return verifier.verify(signature);
    } catch (GeneralSecurityException e) {
      return false;
    }
  }

  private static String javaAlgorithmName(long alg) {
    if (alg == ES256) {
      return "SHA256withECDSA";
    }
    if (alg == EDDSA) {
      return "Ed25519";
    }
    if (alg == RS256) {
      return "SHA256withRSA";
    }
    throw new IllegalArgumentException("Unsupported algorithm " + alg);
  }

  /** Result of checking an attestation statement. */
  public record AttestationResult(boolean valid, String type, String description) {}

  /**
   * Verifies the attestation statement for the formats security keys typically produce ("none",
   * "packed" and "fido-u2f"). The certificate chain is <b>not</b> validated against a trust anchor
   * such as the FIDO Metadata Service: this only proves the attestation is internally consistent.
   */
  public static AttestationResult verifyAttestation(
      AttestationObject attestation, byte[] clientDataHash) {
    Map<Object, Object> attStmt = attestation.attStmt();
    AuthenticatorData authData = attestation.authData();
    byte[] signedData = concat(authData.raw(), clientDataHash);
    try {
      return switch (attestation.fmt()) {
        case "none" ->
            attStmt.isEmpty()
                ? new AttestationResult(true, "None", "No attestation provided")
                : new AttestationResult(false, "None", "attStmt must be empty for 'none'");
        case "packed" -> verifyPacked(attStmt, authData, signedData);
        case "fido-u2f" -> verifyFidoU2f(attStmt, authData, clientDataHash);
        default ->
            new AttestationResult(
                true,
                "Unverified",
                "Attestation format '%s' is not verified by WebGoat".formatted(attestation.fmt()));
      };
    } catch (GeneralSecurityException | ClassCastException | NullPointerException e) {
      return new AttestationResult(false, attestation.fmt(), "Invalid attestation: " + e);
    }
  }

  private static AttestationResult verifyPacked(
      Map<Object, Object> attStmt, AuthenticatorData authData, byte[] signedData)
      throws GeneralSecurityException {
    long alg = (Long) attStmt.get("alg");
    byte[] sig = (byte[]) attStmt.get("sig");
    if (attStmt.get("x5c") instanceof List<?> x5c && !x5c.isEmpty()) {
      X509Certificate certificate = certificate((byte[]) x5c.getFirst());
      if (certificate.getVersion() != 3 || certificate.getBasicConstraints() != -1) {
        return new AttestationResult(false, "Basic", "Attestation certificate must be a v3 leaf");
      }
      byte[] aaguidExtension = certificate.getExtensionValue(AAGUID_EXTENSION_OID);
      if (aaguidExtension != null
          && !Arrays.equals(
              Arrays.copyOfRange(aaguidExtension, aaguidExtension.length - 16, aaguidExtension.length),
              authData.aaguid())) {
        return new AttestationResult(false, "Basic", "AAGUID in certificate does not match");
      }
      boolean valid = verifySignature(alg, certificate.getPublicKey(), signedData, sig);
      return new AttestationResult(
          valid, "Basic", "Signed by " + certificate.getSubjectX500Principal().getName());
    }
    // self attestation: signed with the credential private key itself
    if (alg != coseAlgorithm(authData.credentialPublicKey())) {
      return new AttestationResult(false, "Self", "Algorithm does not match credential key");
    }
    boolean valid =
        verifySignature(alg, toPublicKey(authData.credentialPublicKey()), signedData, sig);
    return new AttestationResult(valid, "Self", "Self attestation with the credential key");
  }

  private static AttestationResult verifyFidoU2f(
      Map<Object, Object> attStmt, AuthenticatorData authData, byte[] clientDataHash)
      throws GeneralSecurityException {
    List<?> x5c = (List<?>) attStmt.get("x5c");
    if (x5c == null || x5c.size() != 1) {
      return new AttestationResult(false, "Basic", "fido-u2f requires exactly one certificate");
    }
    X509Certificate certificate = certificate((byte[]) x5c.getFirst());
    if (!(certificate.getPublicKey() instanceof ECPublicKey)) {
      return new AttestationResult(false, "Basic", "fido-u2f certificate must use an EC key");
    }
    Map<Object, Object> coseKey = authData.credentialPublicKey();
    byte[] u2fPublicKey =
        concat(new byte[] {0x04}, (byte[]) coseKey.get(-2L), (byte[]) coseKey.get(-3L));
    byte[] verificationData =
        concat(
            new byte[] {0x00},
            authData.rpIdHash(),
            clientDataHash,
            authData.credentialId(),
            u2fPublicKey);
    boolean valid =
        verifySignature(
            ES256, certificate.getPublicKey(), verificationData, (byte[]) attStmt.get("sig"));
    return new AttestationResult(
        valid, "Basic", "Signed by " + certificate.getSubjectX500Principal().getName());
  }

  private static X509Certificate certificate(byte[] der) throws GeneralSecurityException {
    return (X509Certificate)
        CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(der));
  }

  public static String toJson(Object value) {
    try {
      return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(value);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  /** Reads a nested string value such as {@code credential.response.clientDataJSON}. */
  public static String path(Map<String, Object> json, String... keys) {
    Object current = json;
    for (String key : keys) {
      if (!(current instanceof Map<?, ?> map)) {
        return null;
      }
      current = map.get(key);
    }
    return current instanceof String s ? s : null;
  }
}
