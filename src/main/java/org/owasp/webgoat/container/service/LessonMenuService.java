/**
 * *************************************************************************************************
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
import org.owasp.webgoat.container.session.Course;
import org.owasp.webgoat.container.users.LessonProgress;
import org.owasp.webgoat.container.users.UserProgress;
import org.owasp.webgoat.container.users.UserProgressRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * LessonMenuService class.
 *
 * @author rlawson
 * @version $Id: $Id
 */
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
        if (excludeLessons.contains(lesson.getName())) {
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
