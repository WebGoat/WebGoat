/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.commandinjection;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import org.owasp.webgoat.container.lessons.Initializable;
import org.owasp.webgoat.container.users.WebGoatUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class CommandInjectionTask3Service implements Initializable {

  private static final Pattern GREP_RESULT_PATTERN =
      Pattern.compile("images/([a-z0-9_-]+)\\.txt:.*", Pattern.CASE_INSENSITIVE);

  private final String webGoatHomeDirectory;
  private final Map<String, String> userFlags = new ConcurrentHashMap<>();
  private final Map<String, File> userDirectories = new ConcurrentHashMap<>();

  private static final Map<String, String> DATA_URI_CACHE = new ConcurrentHashMap<>();

  private static final List<CatDefinition> CAT_LIBRARY =
      List.of(
          new CatDefinition(
              "luna",
              "Luna",
              "Always chasing moonbeams.",
              "lessons/pathtraversal/images/cats/1.jpg"),
          new CatDefinition(
              "milo", "Milo", "Chief snack inspector.", "lessons/pathtraversal/images/cats/2.jpg"),
          new CatDefinition(
              "pixel",
              "Pixel",
              "Sleeps on keyboards only.",
              "lessons/pathtraversal/images/cats/3.jpg"),
          new CatDefinition(
              "nala",
              "Nala",
              "Window watcher extraordinaire.",
              "lessons/pathtraversal/images/cats/4.jpg"),
          new CatDefinition(
              "tiger",
              "Tiger",
              "Tiny roar, big attitude.",
              "lessons/pathtraversal/images/cats/5.jpg"));

  private static final Map<String, CatDefinition> CATS_BY_SLUG =
      CAT_LIBRARY.stream()
          .collect(Collectors.toUnmodifiableMap(CatDefinition::slug, Function.identity()));

  public CommandInjectionTask3Service(
      @Value("${webgoat.user.directory}") String webGoatHomeDirectory) {
    this.webGoatHomeDirectory = webGoatHomeDirectory;
  }

  public SearchResponse search(WebGoatUser user, String title) {
    ensureInitialized(user);
    String command = buildCommand(title);
    CommandResult result = executeCommand(user, command);
    String console = buildConsole(result);
    List<CatView> cats =
        extractMatches(result.output()).stream().map(CatDefinition::toView).toList();
    return new SearchResponse(
        command, console, result.output(), result.timedOut(), result.executionError(), cats);
  }

  public boolean validateFlag(WebGoatUser user, String submittedFlag) {
    ensureInitialized(user);
    String expectedFlag = userFlags.get(user.getUsername());
    return expectedFlag != null && expectedFlag.equals(submittedFlag.trim());
  }

  public void ensureInitialized(WebGoatUser user) {
    userFlags.computeIfAbsent(user.getUsername(), name -> createFlagForUser(user));
  }

  @Override
  public void initialize(WebGoatUser user) {
    userFlags.remove(user.getUsername());
    userDirectories.remove(user.getUsername());
    ensureInitialized(user);
  }

  private String buildConsole(CommandResult result) {
    StringBuilder console = new StringBuilder();
    console.append("Command: ").append(result.command()).append("\n");
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
    String flagValue = UUID.randomUUID().toString();
    File userDir = new File(webGoatHomeDirectory, "command-injection/" + user.getUsername());
    userDir.mkdirs();
    prepareGallery(userDir.toPath());
    File flagFile = new File(userDir, "flag.txt");

    Files.writeString(flagFile.toPath(), flagValue, UTF_8);
    userDirectories.put(user.getUsername(), userDir);
    return flagValue;
  }

  private void prepareGallery(Path userDir) {
    Path imagesDir = userDir.resolve("images");
    if (!Files.exists(imagesDir)) {
      imagesDir.toFile().mkdirs();
    }

    for (CatDefinition cat : CAT_LIBRARY) {
      Path imageTarget = imagesDir.resolve(cat.fileName());
      if (!Files.exists(imageTarget)) {
        try (InputStream in = new ClassPathResource(cat.resourcePath()).getInputStream()) {
          Files.copy(in, imageTarget, REPLACE_EXISTING);
        } catch (IOException e) {
          throw new IllegalStateException("Unable to copy cat image", e);
        }
      }

      Path metaFile = imagesDir.resolve(cat.slug() + ".txt");
      if (!Files.exists(metaFile)) {
        try {
          Files.writeString(metaFile, cat.displayName(), UTF_8);
        } catch (IOException e) {
          throw new IllegalStateException("Unable to create cat metadata", e);
        }
      }
    }
  }

  private String buildCommand(String title) {
    String trimmedTitle = title == null ? "" : title.trim();
    return "grep " + trimmedTitle + " images/*";
  }

  private CommandResult executeCommand(WebGoatUser user, String command) {
    boolean isWindows = System.getProperty("os.name", "").toLowerCase(Locale.US).contains("win");
    String shell = isWindows ? "cmd.exe" : "/bin/sh";
    String switchArg = isWindows ? "/c" : "-c";
    String[] cmd = new String[] {shell, switchArg, command};
    ProcessBuilder builder = new ProcessBuilder(cmd);
    builder.directory(userDirectories.get(user.getUsername()));
    builder.redirectErrorStream(true);
    String processOutput = "";
    boolean timedOut = false;
    String executionError = null;
    try {
      Process process = builder.start();
      boolean finished = process.waitFor(5, TimeUnit.SECONDS);
      if (!finished) {
        timedOut = true;
        process.destroyForcibly();
        finished = process.waitFor(1, TimeUnit.SECONDS);
      }
      if (finished || !process.isAlive()) {
        try (InputStream in = process.getInputStream()) {
          processOutput = new String(in.readAllBytes(), UTF_8);
        }
      } else {
        processOutput = "";
        process.getInputStream().close();
      }
    } catch (IOException | InterruptedException e) {
      executionError = e.getMessage();
      if (e instanceof InterruptedException) {
        Thread.currentThread().interrupt();
      }
    }
    return new CommandResult(command, processOutput, timedOut, executionError);
  }

  private List<CatDefinition> extractMatches(String output) {
    return output
        .lines()
        .map(GREP_RESULT_PATTERN::matcher)
        .filter(Matcher::matches)
        .map(matcher -> matcher.group(1).toLowerCase(Locale.ROOT))
        .map(CATS_BY_SLUG::get)
        .filter(cat -> cat != null)
        .distinct()
        .collect(Collectors.toList());
  }

  private record CommandResult(
      String command, String output, boolean timedOut, String executionError) {}

  public record SearchResponse(
      String command,
      String console,
      String stdout,
      boolean timedOut,
      String executionError,
      List<CatView> cats) {}

  private record CatDefinition(String slug, String displayName, String description, String resourcePath) {

    String fileName() {
      return slug + ".jpg";
    }

    CatView toView() {
      return new CatView(displayName, description, dataUri());
    }

    String dataUri() {
      return DATA_URI_CACHE.computeIfAbsent(resourcePath, CommandInjectionTask3Service::loadDataUri);
    }
  }

  private static String loadDataUri(String resourcePath) {
    try (InputStream is = new ClassPathResource(resourcePath).getInputStream()) {
      byte[] bytes = is.readAllBytes();
      return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(bytes);
    } catch (IOException e) {
      return "";
    }
  }

  public record CatView(String name, String description, String dataUri) {}
}
