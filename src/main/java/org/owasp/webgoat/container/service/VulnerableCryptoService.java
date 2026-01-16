/*
 * SPDX-FileCopyrightText: Copyright © 2024 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container.service;

import java.security.SecureRandom;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * VULNERABILITY #5: Weak Cryptography
 * This service demonstrates weak cryptography vulnerabilities including:
 * - Use of weak random number generators
 * - Weak encryption algorithms
 * - Predictable cryptographic operations
 */
@RestController
@Slf4j
public class VulnerableCryptoService {

  // VULNERABILITY: Using java.util.Random instead of SecureRandom
  private static final Random weakRandom = new Random();

  @GetMapping("/api/vulnerable/generate-token")
  public ResponseEntity<String> generateToken() {
    // VULNERABILITY: Using weak random number generator for security-sensitive operations
    long token = Math.abs(weakRandom.nextLong());
    return ResponseEntity.ok("Generated token: " + token);
  }

  @GetMapping("/api/vulnerable/generate-session-id")
  public ResponseEntity<String> generateSessionId() {
    // VULNERABILITY: Using predictable random number generation
    int sessionId = weakRandom.nextInt(1000000);
    return ResponseEntity.ok("Session ID: " + sessionId);
  }

  @PostMapping("/api/vulnerable/encrypt")
  public ResponseEntity<String> encryptData(@RequestBody String data) {
    try {
      // VULNERABILITY: Using weak encryption algorithm (DES is deprecated and insecure)
      KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
      keyGenerator.init(56); // DES uses 56-bit keys which are too weak
      SecretKey secretKey = keyGenerator.generateKey();
      
      Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
      cipher.init(Cipher.ENCRYPT_MODE, secretKey);
      
      byte[] encrypted = cipher.doFinal(data.getBytes());
      String encryptedBase64 = java.util.Base64.getEncoder().encodeToString(encrypted);
      
      return ResponseEntity.ok("Encrypted: " + encryptedBase64);
    } catch (Exception e) {
      log.error("Error encrypting data", e);
      return ResponseEntity.status(500).body("Error: " + e.getMessage());
    }
  }

  @PostMapping("/api/vulnerable/weak-hash")
  public ResponseEntity<String> weakHash(@RequestBody String data) {
    // VULNERABILITY: Using weak hashing algorithm (MD5 is cryptographically broken)
    try {
      java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
      byte[] hash = md.digest(data.getBytes());
      StringBuilder hexString = new StringBuilder();
      for (byte b : hash) {
        hexString.append(String.format("%02x", b));
      }
      return ResponseEntity.ok("MD5 Hash: " + hexString.toString());
    } catch (Exception e) {
      log.error("Error hashing data", e);
      return ResponseEntity.status(500).body("Error: " + e.getMessage());
    }
  }
}

