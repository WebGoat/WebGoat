/*
 * SPDX-FileCopyrightText: Copyright © 2020 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.pathtraversal;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.owasp.webgoat.container.CurrentUsername;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@AssignmentHints({
  "path-traversal-profile.hint1",
  "path-traversal-profile.hint2",
  "path-traversal-profile.hint3"
})
public class ProfileUpload {

  private final String webGoatHomeDirectory;
  private static final String PROFILE_PICTURE_DIR = "profiles";
  private static final String PICTURE_EXTENSION = ".jpg";

  public ProfileUpload(@Value("${webgoat.server.directory}") String webGoatHomeDirectory) {
    this.webGoatHomeDirectory = webGoatHomeDirectory;
    // Создаем директорию для профилей, если она не существует
    File profileDir = new File(webGoatHomeDirectory, PROFILE_PICTURE_DIR);
    if (!profileDir.exists()) {
      profileDir.mkdirs();
    }
  }

  @PostMapping(
      value = "/PathTraversal/profile-upload",
      consumes = ALL_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ResponseBody
  public AttackResult uploadFileHandler(
      @RequestParam("uploadedFile") MultipartFile file,
      @RequestParam(value = "fullName", required = false) String fullName,
      @CurrentUsername String username) {
    if (file.isEmpty()) {
      return AttackResult.failed("No file uploaded").build();
    }

    try {
      // Генерируем уникальный идентификатор для файла вместо использования username или fullName
      String uniqueFileName = UUID.randomUUID().toString() + PICTURE_EXTENSION;
      Path filePath = Paths.get(webGoatHomeDirectory, PROFILE_PICTURE_DIR, uniqueFileName);

      // Сохраняем файл в безопасной фиксированной директории
      Files.write(filePath, file.getBytes());

      // Связываем имя пользователя с уникальным именем файла (например, в памяти или БД)
      // Здесь это упрощено, в реальном приложении можно использовать Map или БД
      return AttackResult.success("File uploaded successfully")
          .feedbackArgs(uniqueFileName)
          .build();
    } catch (IOException e) {
      return AttackResult.failed("Failed to upload file: " + e.getMessage()).build();
    }
  }

  @GetMapping("/PathTraversal/profile-picture")
  @ResponseBody
  public ResponseEntity<?> getProfilePicture(@CurrentUsername String username) {
    try {
      // Предполагаем, что имя файла связано с пользователем (например, через БД или фиксированное имя)
      String uniqueFileName = getFileNameForUser(username); // Метод для получения имени файла
      Path filePath = Paths.get(webGoatHomeDirectory, PROFILE_PICTURE_DIR, uniqueFileName);

      // Проверяем, что путь находится внутри разрешенной директории
      Path baseDir = Paths.get(webGoatHomeDirectory, PROFILE_PICTURE_DIR).normalize();
      if (!filePath.normalize().startsWith(baseDir)) {
        return ResponseEntity.badRequest().body("Invalid file path");
      }

      if (Files.exists(filePath)) {
        byte[] fileContent = Files.readAllBytes(filePath);
        return ResponseEntity.ok()
            .contentType(org.springframework.http.MediaType.IMAGE_JPEG)
            .body(fileContent);
      } else {
        return ResponseEntity.notFound().build();
      }
    } catch (IOException e) {
      return ResponseEntity.status(500).body("Error retrieving file: " + e.getMessage());
    }
  }

  // Пример метода для получения имени файла для пользователя
  private String getFileNameForUser(String username) {
    // В реальном приложении это может быть запрос к БД или использование фиксированного имени
    // Здесь для простоты возвращаем фиксированное имя с UUID, созданное ранее
    return username.hashCode() + PICTURE_EXTENSION; // Упрощенный пример, лучше использовать БД
  }
}