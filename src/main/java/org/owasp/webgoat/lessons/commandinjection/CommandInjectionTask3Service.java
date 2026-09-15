/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.commandinjection;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.SneakyThrows;
import org.owasp.webgoat.container.lessons.Initializable;
import org.owasp.webgoat.container.users.WebGoatUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CommandInjectionTask3Service implements Initializable {

  private final String webGoatHomeDirectory;
  private final Map<String, String> userFlags = new ConcurrentHashMap<>();
  private final Map<String, File> userDirectories = new ConcurrentHashMap<>();
  private final CommandInjectionCatService catService;
  private final CommandExecutionService commandExecutionService;

  public CommandInjectionTask3Service(
      @Value("${webgoat.user.directory}") String webGoatHomeDirectory,
      CommandInjectionCatService catService,
      CommandExecutionService commandExecutionService) {
    this.webGoatHomeDirectory = webGoatHomeDirectory;
    this.catService = catService;
    this.commandExecutionService = commandExecutionService;
  }

  public SearchResponse search(WebGoatUser user, String title) {
    ensureInitialized(user);
    String command = buildCommand(title);
    CommandExecutionService.CommandExecutionResult executionResult =
        commandExecutionService.execute(userDirectories.get(user.getUsername()), command);
    String console = buildConsole(command, executionResult);
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

  public boolean validateFlag(WebGoatUser user, String submittedFlag) {
    String expectedFlag = userFlags.get(user.getUsername());
    return expectedFlag != null && expectedFlag.contains(submittedFlag.trim());
  }

  private void ensureInitialized(WebGoatUser user) {
    userFlags.computeIfAbsent(user.getUsername(), name -> createFlagForUser(user));
  }

  @Override
  public void initialize(WebGoatUser user) {
    userFlags.remove(user.getUsername());
    userDirectories.remove(user.getUsername());
    ensureInitialized(user);
  }

  private String buildConsole(
      String command, CommandExecutionService.CommandExecutionResult result) {
    StringBuilder console = new StringBuilder();
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

  @SneakyThrows
  private String createFlagForUser(WebGoatUser user) {
    String flagValue = String.format(Locale.ROOT, "flag{%s}", UUID.randomUUID().toString());
    File userDir = catService.prepareGallery(webGoatHomeDirectory, "command-injection", user.getUsername());
    File flagFile = new File(userDir, "flag.txt");

    Files.writeString(flagFile.toPath(), flagValue, UTF_8);
    userDirectories.put(user.getUsername(), userDir);
    return flagValue;
  }

  private String buildCommand(String title) {
    String trimmedTitle = title == null ? "" : title.trim();
    return "grep " + trimmedTitle + " images/*.txt";
  }

  public record SearchResponse(
      String command,
      String console,
      String stdout,
      boolean timedOut,
      String executionError,
      List<CatView> cats) {}

  public record CatView(String name, String description, String dataUri) {}
}
