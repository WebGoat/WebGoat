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
 * @author WebGoat
 * @version $Id: $Id
 * @since October 28, 2003
 */
package org.owasp.webgoat.container.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.owasp.webgoat.container.session.Course;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class StartLesson {

  private final Course course;

  public StartLesson(Course course) {
    this.course = course;
  }

  @GetMapping(
      value = {"*.lesson"},
      produces = "text/html")
  public ModelAndView lessonPage(HttpServletRequest request) {
    var model = new ModelAndView("lesson_content");
    var path = request.getRequestURL().toString(); // we now got /a/b/c/AccessControlMatrix.lesson
    var lessonName = path.substring(path.lastIndexOf('/') + 1, path.indexOf(".lesson"));

    course.getLessons().stream()
        .filter(l -> l.getId().equals(lessonName))
        .findFirst()
        .ifPresent(
            lesson -> {
              request.setAttribute("lesson", lesson);
            });

    return model;
  }
}
