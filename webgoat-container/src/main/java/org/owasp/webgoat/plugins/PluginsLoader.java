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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.lessons.AbstractLesson;
import org.owasp.webgoat.lessons.Assignment;
import org.owasp.webgoat.lessons.NewLesson;
import org.owasp.webgoat.session.Course;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.RegexPatternTypeFilter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@AllArgsConstructor
@Slf4j
@Configuration
public class PluginsLoader {

    @Bean
    public Course loadPlugins() {
        List<AbstractLesson> lessons = Lists.newArrayList();
        for (PluginResource plugin : findPluginResources()) {
            try {
                plugin.getLessons().forEach(c -> {
                    NewLesson lesson = null;
                    try {
                        lesson = (NewLesson) c.getConstructor().newInstance();
                        log.trace("Lesson loaded: {}", lesson.getId());
                    } catch (Exception e) {
                        log.error("Error while loading:" + c, e);
                    }
                    List<Class<AssignmentEndpoint>> assignments = plugin.getAssignments(c);
                    lesson.setAssignments(createAssignment(assignments));
                    lessons.add(lesson);
                });
            } catch (Exception e) {
                log.error("Error in loadLessons: ", e);
            }
        }
        if (lessons.isEmpty()) {
            log.error("No lessons found if you downloaded an official release of WebGoat please take the time to");
            log.error("create a new issue at https://github.com/WebGoat/WebGoat/issues/new");
            log.error("For developers run 'mvn package' first from the root directory.");
        }
        return new Course(lessons);
    }

    private List<Assignment> createAssignment(List<Class<AssignmentEndpoint>> endpoints) {
        return endpoints.stream().map(e -> new Assignment(e.getSimpleName(), getPath(e), getHints(e))).collect(toList());
    }

    private String getPath(Class<AssignmentEndpoint> e) {
        for (Method m : e.getMethods()) {
            if (m.getReturnType() == AttackResult.class) {
                var mapping = m.getAnnotation(RequestMapping.class);
                if (mapping == null) {
                    log.error("AttackResult method found without mapping in: {}", e.getSimpleName());
                } else {
                    return getMapping(m);
                }
            }
        }
        return "";
    }

    private String getMapping(Method m) {
        String[] path = null;
        if (m.getAnnotation(RequestMapping.class) != null) {
            path = m.getAnnotation(RequestMapping.class).path();
        } else if (m.getAnnotation(PostMapping.class) != null) {
            path = m.getAnnotation(PostMapping.class).path();
        } else if (m.getAnnotation(GetMapping.class) != null) {
            path = m.getAnnotation(GetMapping.class).value();
        }
        return path != null && path.length > 0 ? path[0] : "";
    }

    private List<String> getHints(Class<AssignmentEndpoint> e) {
        if (e.isAnnotationPresent(AssignmentHints.class)) {
            return Lists.newArrayList(e.getAnnotationsByType(AssignmentHints.class)[0].value());
        }
        return Lists.newArrayList();
    }

    @SneakyThrows
    public List<PluginResource> findPluginResources() {
        final ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new RegexPatternTypeFilter(Pattern.compile(".*")));
        final Set<BeanDefinition> classes = provider.findCandidateComponents("org.owasp.webgoat.plugin");
        Map<URL, List<Class>> pluginClasses = Maps.newHashMap();
        for (BeanDefinition bean : classes) {
            Class<?> clazz = Class.forName(bean.getBeanClassName());
            URL location = clazz.getProtectionDomain().getCodeSource().getLocation();
            List<Class> classFiles = pluginClasses.get(location);
            if (classFiles == null) {
                classFiles = Lists.newArrayList(clazz);
            } else {
                classFiles.add(clazz);
            }
            pluginClasses.put(location, classFiles);
        }
        return pluginClasses.entrySet().parallelStream()
                .map(e -> new PluginResource(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

}
