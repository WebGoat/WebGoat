/*
 * SPDX-FileCopyrightText: Copyright © 2016 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container.assignments;

import org.owasp.webgoat.container.lessons.Lesson;
import org.owasp.webgoat.container.session.Course;
import org.owasp.webgoat.container.users.UserProgress;
import org.owasp.webgoat.container.users.UserProgressRepository;
import org.owasp.webgoat.container.users.WebGoatUser;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class LessonTrackerInterceptor implements ResponseBodyAdvice<Object> {

  private final Course course;
  private final UserProgressRepository userProgressRepository;

  public LessonTrackerInterceptor(Course course, UserProgressRepository userProgressRepository) {
    this.course = course;
    this.userProgressRepository = userProgressRepository;
  }

  @Override
  public boolean supports(
      MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> clazz) {
    return true;
  }

  @Override
  public Object beforeBodyWrite(
      Object o,
      MethodParameter methodParameter,
      MediaType mediaType,
      Class<? extends HttpMessageConverter<?>> aClass,
      ServerHttpRequest serverHttpRequest,
      ServerHttpResponse serverHttpResponse) {
    if (o instanceof AttackResult attackResult) {
      trackProgress(attackResult);
    }
    return o;
  }

  private void trackProgress(AttackResult attackResult) {
    var user = (WebGoatUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Assert.notNull(user, "User not found in SecurityContext");
    var username = realUsername(user);

    var userProgress = userProgressRepository.findByUser(username);
    if (userProgress == null) {
      userProgress = new UserProgress(username);
    }
    Lesson lesson = course.getLessonByAssignment(attackResult.getAssignment());
    Assert.notNull(lesson, "Lesson not found for assignment " + attackResult.getAssignment());

    if (attackResult.assignmentSolved()) {
      userProgress.assignmentSolved(lesson, attackResult.getAssignment());
    } else {
      userProgress.assignmentFailed(lesson);
    }
    userProgressRepository.save(userProgress);
  }

  private String realUsername(WebGoatUser user) {
    // maybe we shouldn't hard code this with just csrf- prefix for now it works
    return user.getUsername().startsWith("csrf-")
        ? user.getUsername().substring("csrf-".length())
        : user.getUsername();
  }
}
