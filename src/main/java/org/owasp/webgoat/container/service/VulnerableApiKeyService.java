/*
 * SPDX-FileCopyrightText: Copyright © 2024 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * VULNERABILITY #3: Hardcoded Credentials
 * This service contains hardcoded API keys and passwords that should never be in source code.
 */
@RestController
@Slf4j
public class VulnerableApiKeyService {

  // VULNERABILITY: Hardcoded API keys and credentials in source code
  private static final String AWS_ACCESS_KEY = "AKIAIOSFODNN7EXAMPLE";
  private static final String AWS_SECRET_KEY = "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY";
  private static final String DATABASE_PASSWORD = "admin123";
  private static final String API_KEY = "sk_live_51Habc123xyz789secret";
  private static final String JWT_SECRET = "mySecretKey123";

  @GetMapping("/api/vulnerable/config")
  public ResponseEntity<String> getConfig() {
    // VULNERABILITY: Exposing hardcoded credentials through API endpoint
    String config = String.format(
        "AWS Access Key: %s\nAWS Secret Key: %s\nDatabase Password: %s\nAPI Key: %s\nJWT Secret: %s",
        AWS_ACCESS_KEY, AWS_SECRET_KEY, DATABASE_PASSWORD, API_KEY, JWT_SECRET);
    
    log.info("Configuration requested");
    return ResponseEntity.ok(config);
  }

  public String getApiKey() {
    return API_KEY;
  }

  public String getJwtSecret() {
    return JWT_SECRET;
  }
}

