/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.commandinjection;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.owasp.webgoat.container.lessons.Initializable;
import org.owasp.webgoat.container.users.WebGoatUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CommandInjectionTask4Service implements Initializable {

  private static final List<String> DENY_LIST = List.of(";", "&&", "|");

  private final String webGoatHomeDirectory;
  private final Map<String, String> userApiKeys = new ConcurrentHashMap<>();
  private final Map<String, File> userDirectories = new ConcurrentHashMap<>();
  private final CommandInjectionCatService catService;
  private final CommandExecutionService commandExecutionService;

  public CommandInjectionTask4Service(
      @Value("${webgoat.user.directory}") String webGoatHomeDirectory,
      CommandInjectionCatService catService,
      CommandExecutionService commandExecutionService) {
    this.webGoatHomeDirectory = webGoatHomeDirectory;
    this.catService = catService;
    this.commandExecutionService = commandExecutionService;
  }

  public SearchResponse search(WebGoatUser user, String title) {
    ensureInitialized(user);
    SanitizedPayload payload = sanitizeTitle(title);
    String command = buildCommand(payload.sanitizedTitle());
    CommandExecutionService.CommandExecutionResult executionResult =
        commandExecutionService.execute(userDirectories.get(user.getUsername()), command);
    String console = buildConsole(command, executionResult, payload);
    List<CatView> cats =
        catService.extractMatches(executionResult.output()).stream()
            .map(cat -> new CatView(cat.name(), cat.description(), cat.dataUri()))
            .toList();
    return new SearchResponse(
        command,
        console,
        executionResult.output(),
        executionResult.timedOut(),
        executionResult.executionError(),
        cats);
  }

  public boolean validateApiKey(WebGoatUser user, String submittedKey) {
    ensureInitialized(user);
    String expectedKey = userApiKeys.get(user.getUsername());
    return expectedKey != null && expectedKey.equals(submittedKey.trim());
  }

  public void ensureInitialized(WebGoatUser user) {
    userApiKeys.computeIfAbsent(user.getUsername(), name -> createKeyForUser(user));
  }

  @Override
  public void initialize(WebGoatUser user) {
    userApiKeys.remove(user.getUsername());
    userDirectories.remove(user.getUsername());
    ensureInitialized(user);
  }

  private SanitizedPayload sanitizeTitle(String title) {
    if (title == null) {
      return new SanitizedPayload("", false);
    }
    String sanitized = title;
    boolean filtered = false;
    for (String token : DENY_LIST) {
      if (sanitized.contains(token)) {
        filtered = true;
        sanitized = sanitized.replace(token, " ");
      }
    }
    return new SanitizedPayload(sanitized.trim(), filtered);
  }

  private String buildConsole(
      String command,
      CommandExecutionService.CommandExecutionResult result,
      SanitizedPayload payload) {
    StringBuilder console = new StringBuilder();
    if (payload.filtered()) {
      console.append("[Filter] Removed characters: ;, &&, |\n");
    }
    console.append("Command: ").append(command).append("\n");
    console.append(result.output());
    if (result.timedOut()) {
      console.append("\n[Process terminated after timeout]\n");
    }
    if (result.executionError() != null) {
      console.append("\n[Execution error] ").append(result.executionError());
    }
    return console.toString();
  }

  private String createKeyForUser(WebGoatUser user) {
    String apiKey = "API_KEY=" + UUID.randomUUID();
    File userDir =
        catService.prepareGallery(webGoatHomeDirectory, "command-injection/task4", user.getUsername());
    File keyFile = new File(userDir, "api-key.txt");
    try {
      Files.writeString(keyFile.toPath(), apiKey, UTF_8);
      userDirectories.put(user.getUsername(), userDir);
    } catch (IOException e) {
      throw new IllegalStateException("Unable to create api-key file", e);
    }
    return apiKey;
  }

  private String buildCommand(String title) {
    return "grep " + (title == null ? "" : title.trim()) + " images/*.txt";
  }

  public record SearchResponse(
      String command,
      String console,
      String stdout,
      boolean timedOut,
      String executionError,
      List<CatView> cats) {}

  public record CatView(String name, String description, String dataUri) {}

  private record SanitizedPayload(String sanitizedTitle, boolean filtered) {}
}
