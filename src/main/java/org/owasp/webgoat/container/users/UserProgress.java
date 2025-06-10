/*
 * SPDX-FileCopyrightText: Copyright © 2008 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container.users;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.owasp.webgoat.container.lessons.Lesson;

@Slf4j
@Entity
@EqualsAndHashCode
public class UserProgress {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "username")
  private String user;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private Set<LessonProgress> lessonProgress = new HashSet<>();

  protected UserProgress() {}

  public UserProgress(final String user) {
    this.user = user;
  }

  /**
   * Returns an existing lesson progress or create a new one based on the lesson
   *
   * @param lesson the lesson
   * @return a lesson tracker created if not already present
   */
  public LessonProgress getLessonProgress(Lesson lesson) {
    Optional<LessonProgress> progress =
        lessonProgress.stream().filter(l -> l.getLessonName().equals(lesson.getId())).findFirst();
    if (progress.isEmpty()) {
      LessonProgress newLessonTracker = new LessonProgress(lesson);
      lessonProgress.add(newLessonTracker);
      return newLessonTracker;
    } else {
      return progress.get();
    }
  }

  public void assignmentSolved(Lesson lesson, String assignmentName) {
    LessonProgress progress = getLessonProgress(lesson);
    progress.incrementAttempts();
    progress.assignmentSolved(assignmentName);
  }

  public void assignmentFailed(Lesson lesson) {
    LessonProgress progress = getLessonProgress(lesson);
    progress.incrementAttempts();
  }

  public void reset(Lesson al) {
    LessonProgress progress = getLessonProgress(al);
    progress.reset();
  }

  public long numberOfLessonsSolved() {
    return lessonProgress.stream().filter(LessonProgress::isLessonSolved).count();
  }

  public long numberOfAssignmentsSolved() {
    return lessonProgress.stream()
        .map(LessonProgress::numberOfSolvedAssignments)
        .mapToLong(Long::valueOf)
        .sum();
  }
}
