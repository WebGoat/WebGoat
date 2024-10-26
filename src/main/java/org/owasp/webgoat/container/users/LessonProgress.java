package org.owasp.webgoat.container.users;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Version;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.owasp.webgoat.container.lessons.Assignment;
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
@Entity
@EqualsAndHashCode
public class LessonProgress {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Getter private String lessonName;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private final Set<Assignment> assignments = new HashSet<>();

  @Getter private int numberOfAttempts = 0;
  @Version private Integer version;

  protected LessonProgress() {
    // JPA
  }

  public LessonProgress(Lesson lesson) {
    lessonName = lesson.getId();
    assignments.addAll(lesson.getAssignments() == null ? List.of() : lesson.getAssignments());
  }

  public Optional<Assignment> getAssignment(String name) {
    return assignments.stream().filter(a -> a.getName().equals(name)).findFirst();
  }

  /**
   * Mark an assignment as solved
   *
   * @param solvedAssignment the assignment which the user solved
   */
  public void assignmentSolved(String solvedAssignment) {
    getAssignment(solvedAssignment).ifPresent(Assignment::solved);
  }

  /**
   * @return did they user solved all solvedAssignments for the lesson?
   */
  public boolean isLessonSolved() {
    return assignments.stream().allMatch(Assignment::isSolved);
  }

  /** Increase the number attempts to solve the lesson */
  public void incrementAttempts() {
    numberOfAttempts++;
  }

  /** Reset the tracker. We do not reset the number of attempts here! */
  void reset() {
    assignments.clear();
  }

  /**
   * @return list containing all the assignments solved or not
   */
  public Map<Assignment, Boolean> getLessonOverview() {
    return assignments.stream().collect(Collectors.toMap(a -> a, Assignment::isSolved));
  }

  long numberOfSolvedAssignments() {
    return assignments.size();
  }
}
