package org.owasp.webgoat.container.users;

import java.util.List;
import org.assertj.core.api.Assertions;
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
class UserTrackerRepositoryTest {

  private class TestLesson extends Lesson {

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
      Assignment assignment = new Assignment("test", "test", Lists.newArrayList());
      return Lists.newArrayList(assignment);
    }
  }

  @Autowired private UserProgressRepository userTrackerRepository;

  @Test
  void saveUserTracker() {
    UserProgress userTracker = new UserProgress("test");

    userTrackerRepository.save(userTracker);

    userTracker = userTrackerRepository.findByUser("test");
    Assertions.assertThat(userTracker.getLessonProgress("test")).isNotNull();
  }

  @Test
  void solvedAssignmentsShouldBeSaved() {
    UserProgress userTracker = new UserProgress("test");
    TestLesson lesson = new TestLesson();
    userTracker.getLessonProgress(lesson);
    userTracker.assignmentFailed(lesson);
    userTracker.assignmentFailed(lesson);
    userTracker.assignmentSolved(lesson, "test");

    userTrackerRepository.saveAndFlush(userTracker);

    userTracker = userTrackerRepository.findByUser("test");
    Assertions.assertThat(userTracker.numberOfAssignmentsSolved()).isEqualTo(1);
  }

  @Test
  void saveAndLoadShouldHaveCorrectNumberOfAttempts() {
    UserProgress userTracker = new UserProgress("test");
    TestLesson lesson = new TestLesson();
    userTracker.getLessonProgress(lesson);
    userTracker.assignmentFailed(lesson);
    userTracker.assignmentFailed(lesson);
    userTrackerRepository.saveAndFlush(userTracker);

    userTracker = userTrackerRepository.findByUser("test");
    userTracker.assignmentFailed(lesson);
    userTracker.assignmentFailed(lesson);
    userTrackerRepository.saveAndFlush(userTracker);

    Assertions.assertThat(userTracker.getLessonProgress(lesson).getNumberOfAttempts()).isEqualTo(4);
  }
}
