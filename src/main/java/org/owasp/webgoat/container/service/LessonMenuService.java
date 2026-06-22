/*
 * SPDX-FileCopyrightText: Copyright © 2014 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.AllArgsConstructor;
import org.owasp.webgoat.container.CurrentUsername;
import org.owasp.webgoat.container.lessons.Category;
import org.owasp.webgoat.container.lessons.Lesson;
import org.owasp.webgoat.container.lessons.LessonMenuItem;
import org.owasp.webgoat.container.lessons.LessonMenuItemType;
import org.owasp.webgoat.container.lessons.LessonName;
import org.owasp.webgoat.container.session.Course;
import org.owasp.webgoat.container.users.LessonProgress;
import org.owasp.webgoat.container.users.UserProgress;
import org.owasp.webgoat.container.users.UserProgressRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@AllArgsConstructor
public class LessonMenuService {

  public static final String URL_LESSONMENU_MVC = "/service/lessonmenu.mvc";
  private final Course course;
  private UserProgressRepository userTrackerRepository;

  @Value("#{'${exclude.categories}'.split(',')}")
  private List<String> excludeCategories;

  @Value("#{'${exclude.lessons}'.split(',')}")
  private List<String> excludeLessons;

  /**
   * Returns the lesson menu which is used to build the left nav
   *
   * @return a {@link java.util.List} object.
   */
  @RequestMapping(path = URL_LESSONMENU_MVC, produces = "application/json")
  public @ResponseBody List<LessonMenuItem> showLeftNav(@CurrentUsername String username) {
    // TODO: this looks way too complicated. Either we save it incorrectly or we miss something to
    // easily find out
    // if a lesson if solved or not.
    List<LessonMenuItem> menu = new ArrayList<>();
    List<Category> categories = course.getCategories();
    UserProgress userTracker = userTrackerRepository.findByUser(username);

    for (Category category : categories) {
      if (excludeCategories.contains(category.name())) {
        continue;
      }
      LessonMenuItem categoryItem = new LessonMenuItem();
      categoryItem.setName(category.getName());
      categoryItem.setType(LessonMenuItemType.CATEGORY);
      // check for any lessons for this category
      List<Lesson> lessons = course.getLessons(category);
      lessons = lessons.stream().sorted(Comparator.comparing(Lesson::getTitle)).toList();
      for (Lesson lesson : lessons) {
        LessonName lessonName = lesson.getName();
        if (lessonName != null && excludeLessons.contains(lessonName.toString())) {
          continue;
        }
        LessonMenuItem lessonItem = new LessonMenuItem();
        lessonItem.setName(lesson.getTitle());
        lessonItem.setLink(lesson.getLink());
        lessonItem.setType(LessonMenuItemType.LESSON);
        LessonProgress lessonTracker = userTracker.getLessonProgress(lesson);
        boolean lessonSolved = lessonTracker.isLessonSolved();
        lessonItem.setComplete(lessonSolved);
        categoryItem.addChild(lessonItem);
      }
      categoryItem.getChildren().sort(Comparator.comparingInt(LessonMenuItem::getRanking));
      menu.add(categoryItem);
    }
    return menu;
  }
}
