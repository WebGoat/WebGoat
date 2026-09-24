/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.passwordstorage;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.HexFormat;
import java.util.List;
import java.util.Optional;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;
import org.springframework.stereotype.Service;

@Service
public class PasswordStorageService {

  static final String AUTHOR_PASSWORD = "swordfish";
  static final String PEPPER = "training-only-secret";
  static final List<String> WORDLIST =
      List.of("summer2024", "letmein", "monkey", AUTHOR_PASSWORD, "qwerty");

  static final int ARGON_VERSION = 19;
  static final int ARGON_MEMORY_KIB = 19_456;
  static final int ARGON_ITERATIONS = 2;
  static final int ARGON_PARALLELISM = 1;

  private static final byte[] ALICE_SALT =
      HexFormat.of().parseHex("00112233445566778899aabbccddeeff");
  private static final byte[] BOB_SALT =
      HexFormat.of().parseHex("102132435465768798a9bacbdcedfe0f");
  private static final byte[] AUTHOR_SALT =
      HexFormat.of().parseHex("fedcba98765432100123456789abcdef");

  private final ArgonRecord argonRecord;
  private final String pepperedVerifier;
  private volatile CrackResult cachedCrackResult;

  public PasswordStorageService() {
    byte[] argonOutput = argon2id(AUTHOR_PASSWORD, AUTHOR_SALT);
    String salt = base64(AUTHOR_SALT);
    String output = base64(argonOutput);
    this.argonRecord =
        new ArgonRecord(
            "maria",
            "privileged author",
            salt,
            "$argon2id$v="
                + ARGON_VERSION
                + "$m="
                + ARGON_MEMORY_KIB
                + ",t="
                + ARGON_ITERATIONS
                + ",p="
                + ARGON_PARALLELISM
                + "$"
                + salt
                + "$"
                + output);
    this.pepperedVerifier = hmac(argonOutput, PEPPER);
  }

  public List<CredentialRecord> stage1Records() {
    String sharedHash = sha256("violet");
    return List.of(
        new CredentialRecord("alice", "reader", null, sharedHash),
        new CredentialRecord("bob", "reader", null, sharedHash),
        new CredentialRecord("maria", "privileged author", null, sha256(AUTHOR_PASSWORD)));
  }

  public List<CredentialRecord> stage2Records() {
    return List.of(
        saltedRecord("alice", "reader", "violet", ALICE_SALT),
        saltedRecord("bob", "reader", "violet", BOB_SALT),
        saltedRecord("maria", "privileged author", AUTHOR_PASSWORD, AUTHOR_SALT));
  }

  public ArgonRecord argonRecord() {
    return argonRecord;
  }

  public PepperedRecord pepperedRecord() {
    return new PepperedRecord(
        argonRecord.username(),
        argonRecord.role(),
        "argon2id",
        ARGON_VERSION,
        ARGON_MEMORY_KIB,
        ARGON_ITERATIONS,
        ARGON_PARALLELISM,
        argonRecord.salt(),
        "hmac-sha256",
        pepperedVerifier);
  }

  public CrackResult crackWithPepper(String suppliedPepper) {
    if (!constantTimeEquals(PEPPER, suppliedPepper)) {
      return new CrackResult(Optional.empty(), 0);
    }

    CrackResult cached = cachedCrackResult;
    return cached == null ? runBoundedAttack() : cached;
  }

  private synchronized CrackResult runBoundedAttack() {
    if (cachedCrackResult != null) {
      return cachedCrackResult;
    }

    int tested = 0;
    for (String candidate : WORDLIST) {
      tested++;
      byte[] candidateOutput = argon2id(candidate, AUTHOR_SALT);
      String candidateVerifier = hmac(candidateOutput, PEPPER);
      if (constantTimeEquals(pepperedVerifier, candidateVerifier)) {
        cachedCrackResult = new CrackResult(Optional.of(candidate), tested);
        return cachedCrackResult;
      }
    }
    cachedCrackResult = new CrackResult(Optional.empty(), tested);
    return cachedCrackResult;
  }

  static String sha256(String value) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      return HexFormat.of().formatHex(digest.digest(value.getBytes(StandardCharsets.UTF_8)));
    } catch (GeneralSecurityException e) {
      throw new IllegalStateException("SHA-256 is unavailable", e);
    }
  }

  private CredentialRecord saltedRecord(
      String username, String role, String password, byte[] salt) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      digest.update(password.getBytes(StandardCharsets.UTF_8));
      digest.update(salt);
      return new CredentialRecord(username, role, base64(salt), HexFormat.of().formatHex(digest.digest()));
    } catch (GeneralSecurityException e) {
      throw new IllegalStateException("SHA-256 is unavailable", e);
    }
  }

  private byte[] argon2id(String password, byte[] salt) {
    Argon2Parameters parameters =
        new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
            .withVersion(Argon2Parameters.ARGON2_VERSION_13)
            .withSalt(salt)
            .withMemoryAsKB(ARGON_MEMORY_KIB)
            .withIterations(ARGON_ITERATIONS)
            .withParallelism(ARGON_PARALLELISM)
            .build();
    Argon2BytesGenerator generator = new Argon2BytesGenerator();
    generator.init(parameters);
    byte[] output = new byte[32];
    generator.generateBytes(password.getBytes(StandardCharsets.UTF_8), output);
    parameters.clear();
    return output;
  }

  private static String hmac(byte[] value, String pepper) {
    try {
      Mac mac = Mac.getInstance("HmacSHA256");
      mac.init(new SecretKeySpec(pepper.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
      return base64(mac.doFinal(value));
    } catch (GeneralSecurityException e) {
      throw new IllegalStateException("HMAC-SHA-256 is unavailable", e);
    }
  }

  private static boolean constantTimeEquals(String expected, String actual) {
    if (actual == null) {
      return false;
    }
    return MessageDigest.isEqual(
        expected.getBytes(StandardCharsets.UTF_8), actual.getBytes(StandardCharsets.UTF_8));
  }

  private static String base64(byte[] value) {
    return Base64.getEncoder().withoutPadding().encodeToString(value);
  }

  public record CredentialRecord(String username, String role, String salt, String hash) {}

  public record ArgonRecord(String username, String role, String salt, String encodedHash) {}

  public record PepperedRecord(
      String username,
      String role,
      String algorithm,
      int version,
      int memory,
      int iterations,
      int parallelism,
      String salt,
      String verifierAlgorithm,
      String verifier) {}

  public record CrackResult(Optional<String> password, int candidatesTested) {}
}
