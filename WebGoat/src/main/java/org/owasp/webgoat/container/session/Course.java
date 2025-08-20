/*
 * SPDX-FileCopyrightText: Copyright © 2008 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container.session;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.owasp.webgoat.container.lessons.Category;
import org.owasp.webgoat.container.lessons.Lesson;
import org.owasp.webgoat.container.lessons.LessonName;

@Slf4j
public class Course {

  private List<Lesson> lessons;

  public Course(List<Lesson> lessons) {
    this.lessons = lessons;
  }

  /**
   * Gets the categories attribute of the Course object
   *
   * @return The categories value
   */
  public List<Category> getCategories() {
    return lessons.parallelStream().map(Lesson::getCategory).distinct().sorted().toList();
  }

  /**
   * Gets the firstLesson attribute of the Course object
   *
   * @return The firstLesson value
   */
  public Lesson getFirstLesson() {
    // Category 0 is the admin function. We want the first real category
    // to be returned. This is normally the General category and the Http Basics lesson
    return getLessons(getCategories().get(0)).get(0);
  }

  /**
   * Getter for the field <code>lessons</code>.
   *
   * @return a {@link java.util.List} object.
   */
  public List<Lesson> getLessons() {
    return this.lessons;
  }

  /**
   * Getter for the field <code>lessons</code>.
   *
   * @param category a {@link org.owasp.webgoat.container.lessons.Category} object.
   * @return a {@link java.util.List} object.
   */
  public List<Lesson> getLessons(Category category) {
    return this.lessons.stream().filter(l -> l.getCategory() == category).toList();
  }

  public void setLessons(List<Lesson> lessons) {
    this.lessons = lessons;
  }

  public int getTotalOfLessons() {
    return this.lessons.size();
  }

  public int getTotalOfAssignments() {
    return this.lessons.stream()
        .reduce(0, (total, lesson) -> lesson.getAssignments().size() + total, Integer::sum);
  }

  public Lesson getLessonByName(LessonName lessonName) {
    return lessons.stream()
        .filter(lesson -> lesson.getName().equals(lessonName))
        .findFirst()
        .orElse(null);
  }

  public Lesson getLessonByAssignment(String assignmentName) {
    return lessons.stream()
        .filter(
            lesson ->
                lesson.getAssignments().stream()
                    .anyMatch(assignment -> assignment.getName().equals(assignmentName)))
        .findFirst()
        .orElse(null);
  }
}
