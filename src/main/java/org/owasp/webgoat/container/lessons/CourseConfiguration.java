/*
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details, please see http://www.owasp.org/
 *
 * Copyright (c) 2002 - 2019 Bruce Mayhew
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * Getting Source ==============
 *
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software projects.
 */

package org.owasp.webgoat.container.lessons;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import org.apache.commons.lang3.ArrayUtils;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.container.session.Course;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Configuration
public class CourseConfiguration {
  private final List<Lesson> lessons;
  private final List<AssignmentEndpoint> assignments;
  private final String contextPath;

  public CourseConfiguration(
      List<Lesson> lessons,
      List<AssignmentEndpoint> assignments,
      @Value("${server.servlet.context-path}") String contextPath) {
    this.lessons = lessons;
    this.assignments = assignments;
    this.contextPath = contextPath.equals("/") ? "" : contextPath;
  }

  private void attachToLessonInParentPackage(
      AssignmentEndpoint assignmentEndpoint, String packageName) {
    if (packageName.equals("org.owasp.webgoat.lessons")) {
      throw new IllegalStateException(
          "No lesson found for assignment: '%s'"
              .formatted(assignmentEndpoint.getClass().getSimpleName()));
    }
    lessons.stream()
        .filter(l -> l.getClass().getPackageName().equals(packageName))
        .findFirst()
        .ifPresentOrElse(
            l -> l.addAssignment(toAssignment(assignmentEndpoint)),
            () ->
                attachToLessonInParentPackage(
                    assignmentEndpoint, packageName.substring(0, packageName.lastIndexOf("."))));
  }

  /**
   * For each assignment endpoint, find the lesson in the same package or if not found, find the
   * lesson in the parent package
   */
  private void attachToLesson(AssignmentEndpoint assignmentEndpoint) {
    lessons.stream()
        .filter(
            l ->
                l.getClass()
                    .getPackageName()
                    .equals(assignmentEndpoint.getClass().getPackageName()))
        .findFirst()
        .ifPresentOrElse(
            l -> l.addAssignment(toAssignment(assignmentEndpoint)),
            () -> {
              var assignmentPackageName = assignmentEndpoint.getClass().getPackageName();
              attachToLessonInParentPackage(
                  assignmentEndpoint,
                  assignmentPackageName.substring(0, assignmentPackageName.lastIndexOf(".")));
            });
  }

  private Assignment toAssignment(AssignmentEndpoint endpoint) {
    return new Assignment(
        endpoint.getClass().getSimpleName(),
        getPath(endpoint.getClass()),
        getHints(endpoint.getClass()));
  }

  @Bean
  public Course course() {
    assignments.stream().forEach(this::attachToLesson);

    // Check if all assignments are attached to a lesson
    var assignmentsAttachedToLessons =
        lessons.stream().mapToInt(l -> l.getAssignments().size()).sum();
    Assert.isTrue(
        assignmentsAttachedToLessons == assignments.size(),
        "Not all assignments are attached to a lesson, please check the configuration. The"
            + " following assignments are not attached to any lesson: "
            + findDiff());
    return new Course(lessons);
  }

  private List<String> findDiff() {
    var matchedToLessons =
        lessons.stream().flatMap(l -> l.getAssignments().stream()).map(a -> a.getName()).toList();
    var allAssignments = assignments.stream().map(a -> a.getClass().getSimpleName()).toList();

    var diff = new ArrayList<>(allAssignments);
    diff.removeAll(matchedToLessons);
    return diff;
  }

  private String getPath(Class<? extends AssignmentEndpoint> e) {
    for (Method m : e.getMethods()) {
      if (methodReturnTypeIsOfTypeAttackResult(m)) {
        var mapping = getMapping(m);
        if (mapping != null) {
          return contextPath + mapping;
        }
      }
    }
    throw new IllegalStateException(
        "Assignment endpoint: "
            + e
            + " has no mapping like @GetMapping/@PostMapping etc,with return type 'AttackResult' or"
            + " 'ResponseEntity<AttackResult>' please consider adding one");
  }

  private boolean methodReturnTypeIsOfTypeAttackResult(Method m) {
    if (m.getReturnType() == AttackResult.class) {
      return true;
    }
    var genericType = m.getGenericReturnType();
    if (genericType instanceof ParameterizedType) {
      return ((ParameterizedType) m.getGenericReturnType()).getActualTypeArguments()[0]
          == AttackResult.class;
    }
    return false;
  }

  private String getMapping(Method m) {
    String[] paths = null;
    // Find the path, either it is @GetMapping("/attack") of GetMapping(path = "/attack") both are
    // valid, we need to consider both
    if (m.getAnnotation(RequestMapping.class) != null) {
      paths =
          ArrayUtils.addAll(
              m.getAnnotation(RequestMapping.class).value(),
              m.getAnnotation(RequestMapping.class).path());
    } else if (m.getAnnotation(PostMapping.class) != null) {
      paths =
          ArrayUtils.addAll(
              m.getAnnotation(PostMapping.class).value(),
              m.getAnnotation(PostMapping.class).path());
    } else if (m.getAnnotation(GetMapping.class) != null) {
      paths =
          ArrayUtils.addAll(
              m.getAnnotation(GetMapping.class).value(), m.getAnnotation(GetMapping.class).path());
    } else if (m.getAnnotation(PutMapping.class) != null) {
      paths =
          ArrayUtils.addAll(
              m.getAnnotation(PutMapping.class).value(), m.getAnnotation(PutMapping.class).path());
    }
    if (paths == null) {
      return null;
    } else {
      return Arrays.stream(paths).filter(path -> !"".equals(path)).findFirst().orElse("");
    }
  }

  private List<String> getHints(Class<? extends AssignmentEndpoint> e) {
    if (e.isAnnotationPresent(AssignmentHints.class)) {
      return List.of(e.getAnnotationsByType(AssignmentHints.class)[0].value());
    }
    return Collections.emptyList();
  }
}
