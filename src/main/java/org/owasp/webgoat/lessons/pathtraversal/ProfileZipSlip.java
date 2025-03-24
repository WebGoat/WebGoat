/*
 * SPDX-FileCopyrightText: Copyright © 2021 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.pathtraversal;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.owasp.webgoat.container.CurrentUsername;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AssignmentHints({
  "path-traversal-zip-slip.hint1",
  "path-traversal-zip-slip.hint2",
  "path-traversal-zip-slip.hint3",
  "path-traversal-zip-slip.hint4"
})
@Slf4j
public class ProfileZipSlip extends ProfileUploadBase {

  private static final String PICTURE_EXTENSION = ".jpg";

  public ProfileZipSlip(@Value("${webgoat.server.directory}") String webGoatHomeDirectory) {
    super(webGoatHomeDirectory);
  }

  @PostMapping(
      value = "/PathTraversal/zip-slip",
      consumes = ALL_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @ResponseBody
  public AttackResult uploadFileHandler(
      @RequestParam("uploadedFileZipSlip") MultipartFile file, @CurrentUsername String username) {
    if (!file.getOriginalFilename().toLowerCase().endsWith(".zip")) {
      return failed(this).feedback("path-traversal-zip-slip.no-zip").build();
    } else {
      return processZipUpload(file, username);
    }
  }

  @SneakyThrows
  private AttackResult processZipUpload(MultipartFile file, String username) {
    var tmpZipDirectory = Files.createTempDirectory(username);
    cleanupAndCreateDirectoryForUser(username);
    var currentImage = getProfilePictureAsBase64(username);

    try {
      var uploadedZipFile = tmpZipDirectory.resolve(file.getOriginalFilename());
      FileCopyUtils.copy(file.getBytes(), uploadedZipFile.toFile());

      // Используем ZipInputStream для обработки архива
      try (InputStream fileInputStream = Files.newInputStream(uploadedZipFile);
           ZipInputStream zis = new ZipInputStream(fileInputStream)) {
        ZipEntry entry;
        while ((entry = zis.getNextEntry()) != null) {
          if (entry.isDirectory()) {
            continue; // Пропускаем директории
          }

          // Генерируем уникальное имя файла вместо использования имени из архива
          String safeFileName = UUID.randomUUID().toString() + PICTURE_EXTENSION;
          Path safeFilePath = tmpZipDirectory.resolve(safeFileName);

          // Проверяем, что путь находится внутри tmpZipDirectory
          if (!safeFilePath.normalize().startsWith(tmpZipDirectory.normalize())) {
            throw new IOException("Invalid file path detected");
          }

          // Копируем содержимое записи в безопасный файл
          Files.copy(zis, safeFilePath, StandardCopyOption.REPLACE_EXISTING);
          zis.closeEntry();
        }
      }

      return isSolved(currentImage, getProfilePictureAsBase64(username));
    } catch (IOException e) {
      return failed(this).output("Error processing ZIP: " + e.getMessage()).build();
    }
  }

  private AttackResult isSolved(byte[] currentImage, byte[] newImage) {
    if (Arrays.equals(currentImage, newImage)) {
      return failed(this).output("path-traversal-zip-slip.extracted").build();
    }
    return success(this).output("path-traversal-zip-slip.extracted").build();
  }

  @GetMapping("/PathTraversal/zip-slip/")
  @ResponseBody
  public ResponseEntity<?> getProfilePicture(@CurrentUsername String username) {
    return super.getProfilePicture(username);
  }

  @GetMapping("/PathTraversal/zip-slip/profile-image/{username}")
  @ResponseBody
  public ResponseEntity<?> getProfileImage(@PathVariable String username) {
    return ResponseEntity.notFound().build();
  }
}