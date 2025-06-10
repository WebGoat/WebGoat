/*
 * SPDX-FileCopyrightText: Copyright © 2017 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container.users;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.lessons.Assignment;
import org.owasp.webgoat.container.lessons.Category;
import org.owasp.webgoat.container.lessons.Lesson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("webgoat-test")
class UserProgressRepositoryTest {

  private static class TestLesson extends Lesson {

    @Override
    public Category getDefaultCategory() {
      return Category.CLIENT_SIDE;
    }

    @Override
    public String getTitle() {
      return "test";
    }

    @Override
    public List<Assignment> getAssignments() {
      var assignment1 = new Assignment("test1", "test1", Lists.newArrayList());
      var assignment2 = new Assignment("test2", "test2", Lists.newArrayList());

      return List.of(assignment1, assignment2);
    }
  }

  private static final String USER = "user";
  @Autowired private UserProgressRepository userProgressRepository;

  @Test
  void saveUserTracker() {
    var userProgress = new UserProgress(USER);
    userProgressRepository.save(userProgress);

    userProgress = userProgressRepository.findByUser(USER);

    assertThat(userProgress.getLessonProgress(new TestLesson())).isNotNull();
  }

  @Test
  void solvedAssignmentsShouldBeSaved() {
    var userProgress = new UserProgress(USER);
    var lesson = new TestLesson();
    userProgress.getLessonProgress(lesson);
    userProgress.assignmentFailed(lesson);
    userProgress.assignmentFailed(lesson);
    userProgress.assignmentSolved(lesson, "test1");
    userProgress.assignmentSolved(lesson, "test2");
    userProgressRepository.saveAndFlush(userProgress);

    userProgress = userProgressRepository.findByUser(USER);

    assertThat(userProgress.numberOfAssignmentsSolved()).isEqualTo(2);
  }

  @Test
  void saveAndLoadShouldHaveCorrectNumberOfAttempts() {
    UserProgress userProgress = new UserProgress(USER);
    TestLesson lesson = new TestLesson();
    userProgress.getLessonProgress(lesson);
    userProgress.assignmentFailed(lesson);
    userProgress.assignmentFailed(lesson);
    userProgressRepository.saveAndFlush(userProgress);

    userProgress = userProgressRepository.findByUser(USER);
    userProgress.assignmentFailed(lesson);
    userProgress.assignmentFailed(lesson);
    userProgressRepository.saveAndFlush(userProgress);

    assertThat(userProgress.getLessonProgress(lesson).getNumberOfAttempts()).isEqualTo(4);
  }
}
