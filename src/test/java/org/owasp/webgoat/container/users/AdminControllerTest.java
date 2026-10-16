/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container.users;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasLength;
import static org.hamcrest.Matchers.matchesRegex;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.owasp.webgoat.container.i18n.PluginMessages;
import org.owasp.webgoat.container.lessons.Category;
import org.owasp.webgoat.container.lessons.Lesson;
import org.owasp.webgoat.container.users.LessonProgress;
import org.owasp.webgoat.container.session.Course;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * Unit tests for {@link AdminController}.
 *
 * <p>Uses standalone MockMvc (no Spring context) following the same pattern as {@link
 * org.owasp.webgoat.container.report.ReportCardControllerTest}.
 *
 * <p>Covers:
 *
 * <ul>
 *   <li>List users – with and without progress records
 *   <li>User detail – category progress calculation and lesson detail accuracy
 *   <li>Category percentage is correct when some lessons are solved
 *   <li>Password reset – returns non-null password of correct length/charset
 *   <li>Password reset – verifies userService.resetPassword is called
 *   <li>Password reset – returns 404 when the user does not exist
 *   <li>Progress detail reflects actual solved / attempts from the DB model
 * </ul>
 */
@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

  private MockMvc mockMvc;

  @Mock private UserService userService;
  @Mock private UserProgressRepository userProgressRepository;
  @Mock private Course course;
  @Mock private PluginMessages pluginMessages;
  @Mock private UserProgress userProgress;
  @Mock private LessonProgress lessonProgressSolved;
  @Mock private LessonProgress lessonProgressUnsolved;
  @Mock private Lesson lessonA1Solved;
  @Mock private Lesson lessonA1Unsolved;
  @Mock private Lesson lesson;

  private WebGoatUser regularUser;
  private WebGoatUser adminUser;

  @BeforeEach
  void setUp() {
    regularUser = new WebGoatUser("testuser", "password", WebGoatUser.ROLE_USER);
    adminUser = new WebGoatUser("adminuser", "adminpass", WebGoatUser.ROLE_ADMIN);

    this.mockMvc =
        standaloneSetup(
                new AdminController(userService, userProgressRepository, course, pluginMessages))
            .build();
  }

  // ── List users ─────────────────────────────────────────────────────────────

  @Test
  @WithMockUser(username = "adminuser", authorities = "WEBGOAT_ADMIN")
  void listUsers_returnsSummaryForEachUser() throws Exception {
    when(userService.getAllUsers()).thenReturn(List.of(regularUser, adminUser));
    when(userProgressRepository.findByUser(anyString())).thenReturn(userProgress);
    when(userProgress.numberOfLessonsSolved()).thenReturn(3L);
    when(userProgress.numberOfAssignmentsSolved()).thenReturn(5L);
    when(course.getTotalOfLessons()).thenReturn(10);
    when(course.getTotalOfAssignments()).thenReturn(20);

    mockMvc
        .perform(MockMvcRequestBuilders.get("/service/admin/users"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].username", is("testuser")))
        .andExpect(jsonPath("$[0].role", is(WebGoatUser.ROLE_USER)))
        .andExpect(jsonPath("$[0].lessonsSolved", is(3)))
        .andExpect(jsonPath("$[0].totalLessons", is(10)))
        .andExpect(jsonPath("$[1].username", is("adminuser")))
        .andExpect(jsonPath("$[1].role", is(WebGoatUser.ROLE_ADMIN)));
  }

  @Test
  @WithMockUser(username = "adminuser", authorities = "WEBGOAT_ADMIN")
  void listUsers_noProgressRecord_returnZeroCounts() throws Exception {
    when(userService.getAllUsers()).thenReturn(List.of(regularUser));
    when(userProgressRepository.findByUser(anyString())).thenReturn(null);
    when(course.getTotalOfLessons()).thenReturn(5);
    when(course.getTotalOfAssignments()).thenReturn(8);

    mockMvc
        .perform(MockMvcRequestBuilders.get("/service/admin/users"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].lessonsSolved", is(0)))
        .andExpect(jsonPath("$[0].assignmentsSolved", is(0)));
  }

  /** Overall progress counts match solved/total from DB model. */
  @Test
  @WithMockUser(username = "adminuser", authorities = "WEBGOAT_ADMIN")
  void listUsers_progressSummary_matchesSolvedCount() throws Exception {
    when(userService.getAllUsers()).thenReturn(List.of(regularUser));
    when(userProgressRepository.findByUser(anyString())).thenReturn(userProgress);
    when(userProgress.numberOfLessonsSolved()).thenReturn(4L);
    when(userProgress.numberOfAssignmentsSolved()).thenReturn(10L);
    when(course.getTotalOfLessons()).thenReturn(10);
    when(course.getTotalOfAssignments()).thenReturn(20);

    mockMvc
        .perform(MockMvcRequestBuilders.get("/service/admin/users"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].lessonsSolved", is(4)))
        .andExpect(jsonPath("$[0].totalLessons", is(10)))
        .andExpect(jsonPath("$[0].assignmentsSolved", is(10)))
        .andExpect(jsonPath("$[0].totalAssignments", is(20)))
        .andExpect(jsonPath("$[0].lessonsSolved", greaterThanOrEqualTo(0)));
  }

  // ── User detail – lesson data ───────────────────────────────────────────────

  @Test
  @WithMockUser(username = "adminuser", authorities = "WEBGOAT_ADMIN")
  void userDetail_existingUser_returnsLessonDetails() throws Exception {
    when(pluginMessages.getMessage(anyString())).thenReturn("Test Lesson");
    when(userService.getAllUsers()).thenReturn(List.of(regularUser));
    when(userProgressRepository.findByUser("testuser")).thenReturn(userProgress);
    when(course.getLessons()).thenReturn(List.of(lesson));
    when(lesson.getTitle()).thenReturn("testlesson");
    when(lesson.getCategory()).thenReturn(Category.A1);
    when(userProgress.getLessonProgress(any(Lesson.class))).thenReturn(null);

    mockMvc
        .perform(MockMvcRequestBuilders.get("/service/admin/users/testuser"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username", is("testuser")))
        .andExpect(jsonPath("$.categoryProgress[0].category", is("A1")))
        .andExpect(jsonPath("$.categoryProgress[0].solvedLessons", is(0)))
        .andExpect(jsonPath("$.categoryProgress[0].totalLessons", is(1)))
        .andExpect(jsonPath("$.categoryProgress[0].percentage", is(0)))
        .andExpect(jsonPath("$.lessonDetails[0].name", is("Test Lesson")))
        .andExpect(jsonPath("$.lessonDetails[0].category", is("A1")))
        .andExpect(jsonPath("$.lessonDetails[0].solved", is(false)));
  }

  /** Verifies that solved=true and attempts > 0 are reflected from the DB state. */
  @Test
  @WithMockUser(username = "adminuser", authorities = "WEBGOAT_ADMIN")
  void userDetail_solvedLesson_reflectsActualDbState() throws Exception {
    when(pluginMessages.getMessage(anyString())).thenReturn("Injection Lesson");
    when(userService.getAllUsers()).thenReturn(List.of(regularUser));
    when(userProgressRepository.findByUser("testuser")).thenReturn(userProgress);
    when(course.getLessons()).thenReturn(List.of(lesson));
    when(lesson.getTitle()).thenReturn("injection");
    when(lesson.getCategory()).thenReturn(Category.A3);
    when(userProgress.getLessonProgress(any(Lesson.class))).thenReturn(lessonProgressSolved);
    when(lessonProgressSolved.isLessonSolved()).thenReturn(true);
    when(lessonProgressSolved.getNumberOfAttempts()).thenReturn(3);

    mockMvc
        .perform(MockMvcRequestBuilders.get("/service/admin/users/testuser"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonDetails[0].solved", is(true)))
        .andExpect(jsonPath("$.lessonDetails[0].attempts", is(3)));
  }

  // ── Category percentage accuracy ────────────────────────────────────────────

  /**
   * Two A1 lessons, one solved → 50%.
   * Confirms the percentage calculation: round(solved/total * 100).
   */
  @Test
  @WithMockUser(username = "adminuser", authorities = "WEBGOAT_ADMIN")
  void userDetail_categoryPercentage_isCalculatedCorrectly() throws Exception {
    when(pluginMessages.getMessage(anyString())).thenReturn("Lesson");
    when(userService.getAllUsers()).thenReturn(List.of(regularUser));
    when(userProgressRepository.findByUser("testuser")).thenReturn(userProgress);
    when(course.getLessons()).thenReturn(List.of(lessonA1Solved, lessonA1Unsolved));

    when(lessonA1Solved.getTitle()).thenReturn("l1");
    when(lessonA1Solved.getCategory()).thenReturn(Category.A1);
    when(lessonA1Unsolved.getTitle()).thenReturn("l2");
    when(lessonA1Unsolved.getCategory()).thenReturn(Category.A1);

    when(userProgress.getLessonProgress(lessonA1Solved)).thenReturn(lessonProgressSolved);
    when(userProgress.getLessonProgress(lessonA1Unsolved)).thenReturn(lessonProgressUnsolved);
    when(lessonProgressSolved.isLessonSolved()).thenReturn(true);
    when(lessonProgressUnsolved.isLessonSolved()).thenReturn(false);

    mockMvc
        .perform(MockMvcRequestBuilders.get("/service/admin/users/testuser"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.categoryProgress[0].category", is("A1")))
        .andExpect(jsonPath("$.categoryProgress[0].solvedLessons", is(1)))
        .andExpect(jsonPath("$.categoryProgress[0].totalLessons", is(2)))
        .andExpect(jsonPath("$.categoryProgress[0].percentage", is(50)));
  }

  @Test
  @WithMockUser(username = "adminuser", authorities = "WEBGOAT_ADMIN")
  void userDetail_unknownUser_returns404() throws Exception {
    when(userService.getAllUsers()).thenReturn(List.of(regularUser));

    mockMvc
        .perform(MockMvcRequestBuilders.get("/service/admin/users/nobody"))
        .andExpect(status().isNotFound());
  }

  // ── Password reset ──────────────────────────────────────────────────────────

  @Test
  @WithMockUser(username = "adminuser", authorities = "WEBGOAT_ADMIN")
  void resetPassword_existingUser_returnsTemporaryPassword() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.post("/service/admin/users/testuser/reset-password"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username", is("testuser")))
        .andExpect(jsonPath("$.temporaryPassword", notNullValue()));
  }

  /**
   * Verifies the generated password is exactly 12 characters and composed only of the allowed
   * charset (A-Z, a-z, 0-9, !@#$).
   */
  @Test
  @WithMockUser(username = "adminuser", authorities = "WEBGOAT_ADMIN")
  void resetPassword_generatedPassword_hasCorrectFormatAndLength() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.post("/service/admin/users/testuser/reset-password"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.temporaryPassword", hasLength(12)))
        .andExpect(jsonPath("$.temporaryPassword", matchesRegex("^[A-Za-z0-9!@#$]{12}$")));
  }

  /**
   * Verifies that resetPassword() delegates to {@link UserService#resetPassword}, so the password
   * is persisted via the existing JPA/password-encoder pipeline.
   */
  @Test
  @WithMockUser(username = "adminuser", authorities = "WEBGOAT_ADMIN")
  void resetPassword_callsUserServiceResetPassword() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.post("/service/admin/users/testuser/reset-password"))
        .andExpect(status().isOk());

    verify(userService).resetPassword(eq("testuser"), anyString());
  }

  /**
   * Verifies that resetting a non-existent user's password returns 404.
   */
  @Test
  @WithMockUser(username = "adminuser", authorities = "WEBGOAT_ADMIN")
  void resetPassword_nonExistentUser_returns404() throws Exception {
    doThrow(new UsernameNotFoundException("not found"))
        .when(userService)
        .resetPassword(eq("ghost"), anyString());

    mockMvc
        .perform(MockMvcRequestBuilders.post("/service/admin/users/ghost/reset-password"))
        .andExpect(status().isNotFound());
  }
}
