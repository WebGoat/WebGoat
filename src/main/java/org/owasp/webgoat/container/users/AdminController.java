/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container.users;

import java.security.SecureRandom;
import java.util.List;
import lombok.AllArgsConstructor;
import org.owasp.webgoat.container.i18n.PluginMessages;
import org.owasp.webgoat.container.session.Course;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller exposing administration endpoints. All endpoints require the {@code
 * WEBGOAT_ADMIN} authority and are therefore inaccessible to regular users.
 *
 * <p>Endpoints:
 *
 * <ul>
 *   <li>{@code GET /service/admin/users} – list every registered user with a progress summary
 *   <li>{@code GET /service/admin/users/{username}} – lesson-level progress for one user
 *   <li>{@code POST /service/admin/users/{username}/reset-password} – reset a user's password
 * </ul>
 */
@RestController
@RequestMapping("/service/admin")
@AllArgsConstructor
@EnableMethodSecurity
public class AdminController {

  private static final String CHARS =
      "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$";
  private static final int TEMP_PASSWORD_LENGTH = 12;
  private static final SecureRandom SECURE_RANDOM = new SecureRandom();

  private final UserService userService;
  private final UserProgressRepository userProgressRepository;
  private final Course course;
  private final PluginMessages pluginMessages;

  // ── User list ──────────────────────────────────────────────────────────────

  /**
   * Returns every registered user together with a progress summary (lessons solved, assignments
   * solved, total lessons, total assignments).
   */
  @GetMapping("/users")
  @PreAuthorize("hasAuthority('WEBGOAT_ADMIN')")
  public List<UserSummary> listUsers() {
    int totalLessons = course.getTotalOfLessons();
    int totalAssignments = course.getTotalOfAssignments();

    return userService.getAllUsers().stream()
        .map(
            user -> {
              UserProgress progress = userProgressRepository.findByUser(user.getUsername());
              long lessonsSolved = progress != null ? progress.numberOfLessonsSolved() : 0;
              long assignmentsSolved = progress != null ? progress.numberOfAssignmentsSolved() : 0;
              return new UserSummary(
                  user.getUsername(),
                  user.getRole(),
                  lessonsSolved,
                  assignmentsSolved,
                  totalLessons,
                  totalAssignments);
            })
        .toList();
  }

  // ── User detail ────────────────────────────────────────────────────────────

  /**
   * Returns lesson-level progress for a single user. Each entry in the returned list contains the
   * lesson name, whether it is solved, and the number of attempts.
   */
  @GetMapping("/users/{username}")
  @PreAuthorize("hasAuthority('WEBGOAT_ADMIN')")
  public ResponseEntity<UserDetail> userDetail(@PathVariable String username) {
    WebGoatUser user =
        userService.getAllUsers().stream()
            .filter(u -> u.getUsername().equals(username))
            .findFirst()
            .orElse(null);

    if (user == null) {
      return ResponseEntity.notFound().build();
    }

    UserProgress progress = userProgressRepository.findByUser(username);

    // Group lessons by Category and calculate per-category progress
    java.util.Map<String, List<org.owasp.webgoat.container.lessons.Lesson>> lessonsByCategory =
        course.getLessons().stream()
            .collect(
                java.util.stream.Collectors.groupingBy(
                    l -> l.getCategory() != null ? l.getCategory().name() : "UNKNOWN",
                    java.util.LinkedHashMap::new,
                    java.util.stream.Collectors.toList()));

    List<CategorySummary> categoryProgress =
        lessonsByCategory.entrySet().stream()
            .map(
                entry -> {
                  String catName = entry.getKey();
                  var catLessons = entry.getValue();
                  int catTotal = catLessons.size();
                  long catSolved =
                      catLessons.stream()
                          .filter(
                              l -> {
                                var lp = progress != null ? progress.getLessonProgress(l) : null;
                                return lp != null && lp.isLessonSolved();
                              })
                          .count();
                  int catPct =
                      catTotal > 0 ? (int) Math.round((double) catSolved / catTotal * 100) : 0;
                  return new CategorySummary(catName, (int) catSolved, catTotal, catPct);
                })
            .toList();

    List<LessonDetail> lessonDetails =
        course.getLessons().stream()
            .map(
                lesson -> {
                  var lessonProgress =
                      progress != null ? progress.getLessonProgress(lesson) : null;
                  boolean solved = lessonProgress != null && lessonProgress.isLessonSolved();
                  int attempts =
                      lessonProgress != null ? lessonProgress.getNumberOfAttempts() : 0;
                  String category =
                      lesson.getCategory() != null ? lesson.getCategory().name() : "UNKNOWN";
                  return new LessonDetail(
                      pluginMessages.getMessage(lesson.getTitle()), category, solved, attempts);
                })
            .toList();

    return ResponseEntity.ok(
        new UserDetail(user.getUsername(), user.getRole(), categoryProgress, lessonDetails));
  }

  // ── Password reset ─────────────────────────────────────────────────────────

  /**
   * Resets a user's password to a randomly generated temporary password and returns it in the
   * response body so the administrator can communicate it to the user.
   *
   * <p>Passwords are stored as plain-text because WebGoat uses {@link
   * org.springframework.security.crypto.password.NoOpPasswordEncoder}. This is intentional – the
   * application exists solely to demonstrate vulnerabilities in a controlled environment.
   */
  @PostMapping("/users/{username}/reset-password")
  @PreAuthorize("hasAuthority('WEBGOAT_ADMIN')")
  public ResponseEntity<PasswordResetResponse> resetPassword(@PathVariable String username) {
    String tempPassword = generateTemporaryPassword();
    try {
      userService.resetPassword(username, tempPassword);
    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(new PasswordResetResponse(username, tempPassword));
  }

  // ── Helpers ────────────────────────────────────────────────────────────────

  private static String generateTemporaryPassword() {
    StringBuilder sb = new StringBuilder(TEMP_PASSWORD_LENGTH);
    for (int i = 0; i < TEMP_PASSWORD_LENGTH; i++) {
      sb.append(CHARS.charAt(SECURE_RANDOM.nextInt(CHARS.length())));
    }
    return sb.toString();
  }

  // ── DTOs ───────────────────────────────────────────────────────────────────

  /** Summary row displayed in the user table. */
  public record UserSummary(
      String username,
      String role,
      long lessonsSolved,
      long assignmentsSolved,
      int totalLessons,
      int totalAssignments) {}

  /** Full lesson-level and per-category detail for a single user. */
  public record UserDetail(
      String username,
      String role,
      List<CategorySummary> categoryProgress,
      List<LessonDetail> lessonDetails) {}

  /** Per-category progress summary. */
  public record CategorySummary(
      String category, int solvedLessons, int totalLessons, int percentage) {}

  /** Per-lesson progress entry. */
  public record LessonDetail(String name, String category, boolean solved, int attempts) {}

  /** Response body returned after a successful password reset. */
  public record PasswordResetResponse(String username, String temporaryPassword) {}
}
