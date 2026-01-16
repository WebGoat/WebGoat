/*
 * SPDX-FileCopyrightText: Copyright © 2024 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * VULNERABILITY #2: Command Injection
 * This service demonstrates command injection vulnerability by executing system commands
 * with user-supplied input without proper sanitization.
 */
@RestController
@Slf4j
public class VulnerableCommandExecutionService {

  @PostMapping("/api/vulnerable/execute")
  public ResponseEntity<String> executeCommand(@RequestBody String command) {
    try {
      // VULNERABILITY: Executing system commands with user input without validation
      Process process = Runtime.getRuntime().exec("ping -c 3 " + command);
      
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
      StringBuilder output = new StringBuilder();
      String line;
      
      while ((line = reader.readLine()) != null) {
        output.append(line).append("\n");
      }
      
      process.waitFor();
      return ResponseEntity.ok("Command output:\n" + output.toString());
    } catch (Exception e) {
      log.error("Error executing command", e);
      return ResponseEntity.status(500).body("Error: " + e.getMessage());
    }
  }
}

