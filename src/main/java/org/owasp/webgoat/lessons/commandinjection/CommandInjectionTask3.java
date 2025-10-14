/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.commandinjection;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.informationMessage;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.Base64;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.lang3.RandomStringUtils;
import org.owasp.webgoat.container.CurrentUser;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.container.lessons.Initializable;
import org.owasp.webgoat.container.users.WebGoatUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

/** Task 3: escalate to reading secrets by issuing real commands. */
@RestController
@AssignmentHints({
  "commandinjection.task3.hint1",
  "commandinjection.task3.hint2",
  "commandinjection.task3.hint3",
  "commandinjection.task3.hint4"
})
public class CommandInjectionTask3 implements AssignmentEndpoint, Initializable {

  private final String webGoatHomeDirectory;
  private final Map<String, String> userFlags = new ConcurrentHashMap<>();
  private final Map<String, File> userDirectories = new ConcurrentHashMap<>();

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

  private static final Pattern GREP_RESULT_PATTERN =
      Pattern.compile("images/([a-z0-9_-]+)\\.txt:.*", Pattern.CASE_INSENSITIVE);

  public CommandInjectionTask3(@Value("${webgoat.user.directory}") String webGoatHomeDirectory) {
    this.webGoatHomeDirectory = webGoatHomeDirectory;
  }

  @PostMapping(
      value = "/CommandInjection/task3/search",
      consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public AttackResult search(@CurrentUser WebGoatUser user, @RequestParam("title") String title) {
    if (title == null || title.isBlank()) {
      return failed(this).feedback("commandinjection.task3.failure.payload").build();
    }

    String command = buildCommand(title);
    CommandResult result = executeCommand(user, command);
    String output = renderOutput(result);
    return informationMessage(this).output(output).build();
  }

  @PostMapping(
      value = "/CommandInjection/task3/flag",
      consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public AttackResult submitFlag(
      @CurrentUser WebGoatUser user, @RequestParam("flag") String submittedFlag) {
    if (submittedFlag == null || submittedFlag.isBlank()) {
      return failed(this).feedback("commandinjection.task3.failure.blank").build();
    }

    String expectedFlag = userFlags.get(user.getUsername());

    if (expectedFlag.equals(submittedFlag.trim())) {
      return success(this).feedback("commandinjection.task3.success").build();
    }

    return failed(this).feedback("commandinjection.task3.failure.invalid").build();
  }

  private String createFlagForUser(WebGoatUser user) {
    String flagValue = "#{%s}".formatted(UUID.randomUUID().toString());
    File userDir = new File(webGoatHomeDirectory, "command-injection/" + user.getUsername());
    userDir.mkdirs();
    prepareGallery(userDir.toPath());
    File flagFile = new File(userDir, "flag.txt");
    try {
      Files.writeString(flagFile.toPath(), flagValue, UTF_8);
      userDirectories.put(user.getUsername(), userDir);
    } catch (IOException e) {
      throw new IllegalStateException("Unable to create flag file", e);
    }
    return flagValue;
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

  @Override
  public void initialize(WebGoatUser user) {
    userFlags.remove(user.getUsername());
    userDirectories.remove(user.getUsername());
    createFlagForUser(user);
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

  private String renderOutput(CommandResult result) {
    StringBuilder rendered = new StringBuilder();
    StringBuilder rawOutput = new StringBuilder();
    rawOutput.append("Command: ").append(result.command()).append("\n");
    rawOutput.append(result.output());
    if (result.timedOut()) {
      rawOutput.append("\n[Process terminated after timeout]\n");
    }
    if (result.executionError() != null) {
      rawOutput.append("\n[Execution error] ").append(result.executionError());
    }

    rendered
        .append("<div class=\"command-output\"><pre>")
        .append(HtmlUtils.htmlEscape(rawOutput.toString()))
        .append("</pre></div>");

    List<CatDefinition> matches = extractMatches(result.output());
    if (!matches.isEmpty()) {
      rendered.append(renderGallery(matches));
    }

    return rendered.toString();
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

  private String renderGallery(List<CatDefinition> matches) {
    StringBuilder gallery = new StringBuilder();
    gallery.append("<div class=\"cat-gallery\">");
    gallery.append("<h4>Matching Cats</h4>");
    gallery.append("<div class=\"cat-grid\">");
    for (CatDefinition cat : matches) {
      gallery.append("<figure class=\"cat-card\">");
      gallery
          .append("<img src=\"")
          .append(cat.dataUri())
          .append("\" alt=\"")
          .append(HtmlUtils.htmlEscape(cat.displayName()))
          .append("\" />");
      gallery.append("<figcaption>");
      gallery
          .append("<strong>")
          .append(HtmlUtils.htmlEscape(cat.displayName()))
          .append("</strong>");
      if (!cat.description().isBlank()) {
        gallery.append("<br/>").append(HtmlUtils.htmlEscape(cat.description()));
      }
      gallery.append("</figcaption>");
      gallery.append("</figure>");
    }
    gallery.append("</div></div>");
    return gallery.toString();
  }

  private record CommandResult(
      String command, String output, boolean timedOut, String executionError) {}

  private static class CatDefinition {
    private final String slug;
    private final String displayName;
    private final String description;
    private final String resourcePath;
    private final String fileName;
    private volatile String dataUri;

    CatDefinition(String slug, String displayName, String description, String resourcePath) {
      this.slug = slug;
      this.displayName = displayName;
      this.description = description;
      this.resourcePath = resourcePath;
      this.fileName = slug + ".jpg";
    }

    String slug() {
      return slug;
    }

    String displayName() {
      return displayName;
    }

    String description() {
      return description;
    }

    String resourcePath() {
      return resourcePath;
    }

    String fileName() {
      return fileName;
    }

    String dataUri() {
      String current = dataUri;
      if (current != null) {
        return current;
      }
      synchronized (this) {
        if (dataUri == null) {
          try (InputStream is = new ClassPathResource(resourcePath).getInputStream()) {
            byte[] bytes = is.readAllBytes();
            dataUri = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(bytes);
          } catch (IOException e) {
            dataUri = "";
          }
        }
        return dataUri;
      }
    }
  }
}
