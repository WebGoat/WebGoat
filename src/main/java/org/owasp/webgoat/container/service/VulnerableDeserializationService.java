/*
 * SPDX-FileCopyrightText: Copyright © 2024 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container.service;

import com.thoughtworks.xstream.XStream;
import java.io.ByteArrayInputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * VULNERABILITY #4: Insecure Deserialization
 * This service demonstrates insecure deserialization by deserializing untrusted data
 * without proper validation or security controls.
 */
@RestController
@Slf4j
public class VulnerableDeserializationService {

  @PostMapping("/api/vulnerable/deserialize")
  public ResponseEntity<String> deserializeData(@RequestBody byte[] serializedData) {
    try {
      // VULNERABILITY: Deserializing untrusted data without validation
      XStream xstream = new XStream();
      
      // VULNERABILITY: No type restrictions or security controls
      xstream.allowTypesByWildcard(new String[]{"**"});
      
      ByteArrayInputStream inputStream = new ByteArrayInputStream(serializedData);
      Object deserialized = xstream.fromXML(inputStream);
      
      log.info("Deserialized object: {}", deserialized.getClass().getName());
      return ResponseEntity.ok("Successfully deserialized: " + deserialized.toString());
    } catch (Exception e) {
      log.error("Error deserializing data", e);
      return ResponseEntity.status(500).body("Error: " + e.getMessage());
    }
  }

  @PostMapping("/api/vulnerable/deserialize-base64")
  public ResponseEntity<String> deserializeBase64(@RequestBody String base64Data) {
    try {
      // VULNERABILITY: Deserializing base64-encoded untrusted data
      byte[] decoded = java.util.Base64.getDecoder().decode(base64Data);
      
      XStream xstream = new XStream();
      xstream.allowTypesByWildcard(new String[]{"**"});
      
      Object deserialized = xstream.fromXML(new ByteArrayInputStream(decoded));
      
      return ResponseEntity.ok("Deserialized: " + deserialized.toString());
    } catch (Exception e) {
      log.error("Error deserializing base64 data", e);
      return ResponseEntity.status(500).body("Error: " + e.getMessage());
    }
  }
}

