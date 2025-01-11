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

/**
 * ************************************************************************************************
 *
 * <p>
 *
 * <p>This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 *
 * <p>Copyright (c) 2002 - 2014 Bruce Mayhew
 *
 * <p>This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * <p>This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * <p>You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * <p>Getting Source ==============
 *
 * <p>Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository
 * for free software projects.
 *
 * @author Bruce Mayhew <a href="http://code.google.com/p/webgoat">WebGoat</a>
 * @version $Id: $Id
 * @since October 29, 2003
 */
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
   * Returns an existing lesson tracker or create a new one based on the lesson
   *
   * @param lesson the lesson
   * @return a lesson tracker created if not already present
   */
  public LessonProgress getLessonProgress(Lesson lesson) {
    Optional<LessonProgress> progress =
        lessonProgress.stream().filter(l -> l.getLessonName().equals(lesson.getId())).findFirst();
    if (!progress.isPresent()) {
      LessonProgress newLessonTracker = new LessonProgress(lesson);
      lessonProgress.add(newLessonTracker);
      return newLessonTracker;
    } else {
      return progress.get();
    }
  }

  /**
   * Query method for finding a specific lesson tracker based on id
   *
   * @param id the id of the lesson
   * @return optional due to the fact we can only create a lesson tracker based on a lesson
   */
  public Optional<LessonProgress> getLessonProgress(String id) {
    return lessonProgress.stream().filter(l -> l.getLessonName().equals(id)).findFirst();
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
