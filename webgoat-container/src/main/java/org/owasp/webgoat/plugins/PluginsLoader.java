package org.owasp.webgoat.plugins;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.owasp.webgoat.lessons.AbstractLesson;
import org.owasp.webgoat.lessons.NewLesson;
import org.owasp.webgoat.session.Course;

import java.util.List;

/**
 * ************************************************************************************************
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 * <p>
 * Copyright (c) 2002 - 20014 Bruce Mayhew
 * <p>
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 * <p>
 * Getting Source ==============
 * <p>
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software
 * projects.
 * <p>
 *
 * @author nbaars
 * @version $Id: $Id
 * @since November 25, 2016
 */
@AllArgsConstructor
@Slf4j
public class PluginsLoader {

    private final PluginsExtractor extractor;
    private final PluginEndpointPublisher pluginEndpointPublisher;

    /**
     * <p>createLessonsFromPlugins.</p>
     */
    public Course loadPlugins() {
        List<AbstractLesson> lessons = Lists.newArrayList();
        for (Plugin plugin : extractor.loadPlugins()) {
            try {
                NewLesson lesson = (NewLesson) plugin.getLesson().get();
                lessons.add(lesson);
                pluginEndpointPublisher.publish(plugin);
            } catch (Exception e) {
                log.error("Error in loadLessons: ", e);
            }
        }
        if (lessons.isEmpty()) {
            log.error("No lessons found if you downloaded an official release of WebGoat please take the time to");
            log.error("create a new issue at https://github.com/WebGoat/WebGoat/issues/new");
            log.error("For developers run 'mvn package' first from the root directory.");
            log.error("Stopping WebGoat...");
            System.exit(1); //we always run standalone
        }
        return new Course(lessons);
    }

}
