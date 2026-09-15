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
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

/**
 * Shared helper for the command-injection tasks that displays the cat gallery. Responsible for
 * provisioning per-user image folders and parsing grep output to resolve cat metadata.
 */
@Service
public class CommandInjectionCatService {

  private static final Pattern GREP_RESULT_PATTERN =
      Pattern.compile("images/([a-z0-9_-]+)\\.txt:.*", Pattern.CASE_INSENSITIVE);

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

  private static final Map<String, String> DATA_URI_CACHE = new ConcurrentHashMap<>();

  /**
   * Ensure the per-user gallery exists and return the directory.
   *
   * @param webGoatHomeDirectory root WebGoat directory
   * @param lessonPath sub-path for the lesson (e.g., {@code command-injection} or
   *     {@code command-injection/task4})
   * @param username current user's name
   * @return File pointing to the user's working directory
   */
  public File prepareGallery(String webGoatHomeDirectory, String lessonPath, String username) {
    File userDir = new File(webGoatHomeDirectory, String.format("%s/%s", lessonPath, username));
    userDir.mkdirs();

    Path imagesDir = userDir.toPath().resolve("images");
    try {
      Files.createDirectories(imagesDir);
    } catch (IOException e) {
      throw new IllegalStateException("Unable to create images directory", e);
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

    return userDir;
  }

  public List<CatView> extractMatches(String output) {
    return output
        .lines()
        .map(GREP_RESULT_PATTERN::matcher)
        .filter(Matcher::matches)
        .map(matcher -> matcher.group(1).toLowerCase(Locale.ROOT))
        .map(CATS_BY_SLUG::get)
        .filter(cat -> cat != null)
        .distinct()
        .map(CatDefinition::toView)
        .toList();
  }

  private record CatDefinition(String slug, String displayName, String description, String resourcePath) {

    String fileName() {
      return slug + ".jpg";
    }

    CatView toView() {
      return new CatView(displayName, description, dataUri());
    }

    String dataUri() {
      return DATA_URI_CACHE.computeIfAbsent(resourcePath, CommandInjectionCatService::loadDataUri);
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
