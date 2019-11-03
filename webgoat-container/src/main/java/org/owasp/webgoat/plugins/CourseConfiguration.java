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

package org.owasp.webgoat.plugins;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.lessons.Lesson;
import org.owasp.webgoat.lessons.Assignment;
import org.owasp.webgoat.session.Course;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Slf4j
@Configuration
public class CourseConfiguration {

    private final List<Lesson> lessons;
    private final List<AssignmentEndpoint> assignments;
    private final Map<String, List<AssignmentEndpoint>> assignmentsByPackage;

    public CourseConfiguration(List<Lesson> lessons, List<AssignmentEndpoint> assignments) {
        this.lessons = lessons;
        this.assignments = assignments;
        assignmentsByPackage = this.assignments.stream().collect(groupingBy(a -> a.getClass().getPackageName()));
    }

    @Bean
    public Course course() {
        lessons.stream().forEach(l -> l.setAssignments(createAssignment(l)));
        return new Course(lessons);
    }

    private List<Assignment> createAssignment(Lesson lesson) {
        var endpoints = assignmentsByPackage.get(lesson.getClass().getPackageName());
        if (CollectionUtils.isEmpty(endpoints)) {
            log.warn("Lesson: {} has no endpoints, is this intentionally?", lesson.getTitle());
            return new ArrayList();
        }
        return endpoints.stream().map(e -> new Assignment(e.getClass().getSimpleName(), getPath(e.getClass()), getHints(e.getClass()))).collect(toList());
    }

    private String getPath(Class<? extends AssignmentEndpoint> e) {
        for (Method m : e.getMethods()) {
            if (m.getReturnType() == AttackResult.class) {
                var mapping = getMapping(m);
                if (mapping == null) {
                    log.error("AttackResult method found without mapping in: {}", e.getSimpleName());
                } else {
                    return mapping;
                }
            }
        }
        return "none";
    }

    private String getMapping(Method m) {
        String[] paths = null;
        //Find the path, either it is @GetMapping("/attack") of GetMapping(path = "/attack") both are valid, we need to consider both
        if (m.getAnnotation(RequestMapping.class) != null) {
            paths = ArrayUtils.addAll(m.getAnnotation(RequestMapping.class).value(), m.getAnnotation(RequestMapping.class).path());
        } else if (m.getAnnotation(PostMapping.class) != null) {
            paths = ArrayUtils.addAll(m.getAnnotation(PostMapping.class).value(), m.getAnnotation(PostMapping.class).path());
        } else if (m.getAnnotation(GetMapping.class) != null) {
            paths = ArrayUtils.addAll(m.getAnnotation(GetMapping.class).value(), m.getAnnotation(GetMapping.class).path());
        } else if (m.getAnnotation(PutMapping.class) != null) {
            paths = ArrayUtils.addAll(m.getAnnotation(PutMapping.class).value(), m.getAnnotation(PutMapping.class).path());
        }
        if (paths == null) {
            return "";
        } else {
            return Arrays.stream(paths).filter(path -> !"".equals(path)).findFirst().orElseGet(() -> "");
        }
    }

    private List<String> getHints(Class<? extends AssignmentEndpoint> e) {
        if (e.isAnnotationPresent(AssignmentHints.class)) {
            return List.of(e.getAnnotationsByType(AssignmentHints.class)[0].value());
        }
        return Collections.emptyList();
    }
}
