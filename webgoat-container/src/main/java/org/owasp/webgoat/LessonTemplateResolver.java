/**
 *************************************************************************************************
 *
 *
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 *
 * Copyright (c) 2002 - 20014 Bruce Mayhew
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
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software
 * projects.
 *
 * @author WebGoat
 * @since October 28, 2003
 * @version $Id: $Id
 */
package org.owasp.webgoat;

import com.google.common.collect.Sets;
import com.google.common.io.Files;
import org.thymeleaf.TemplateProcessingParameters;
import org.thymeleaf.resourceresolver.IResourceResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Dynamically resolve a lesson. In the html file this can be invoked as:
 *
 * <code>
 *    <div th:case="true" th:replace="lesson:__${lesson.class.simpleName}__"></div>
 * </code>
 *
 * Thymeleaf will invoke this resolver based on the prefix and this implementqtion will resolve the html in the plugins directory
 */
public class LessonTemplateResolver extends TemplateResolver {

    private final static String PREFIX = "lesson:";
    private final File pluginTargetDirectory;

    public LessonTemplateResolver(File pluginTargetDirectory) {
        this.pluginTargetDirectory = pluginTargetDirectory;
        setResourceResolver(new LessonResourceResolver());
        setResolvablePatterns(Sets.newHashSet(PREFIX + "*"));
    }

    @Override
    protected String computeResourceName(TemplateProcessingParameters params) {
        String templateName = params.getTemplateName();
        return templateName.substring(PREFIX.length());
    }

    private class LessonResourceResolver implements IResourceResolver {

        @Override
        public InputStream getResourceAsStream(TemplateProcessingParameters params, String resourceName) {
            File lesson = new File(pluginTargetDirectory, "/plugin/" + resourceName + "/html/" + resourceName + ".html");
            if (lesson != null) {
                try {
                    return new ByteArrayInputStream(Files.toByteArray(lesson));
                } catch (IOException e) {
                    //no html yet
                    return new ByteArrayInputStream(new byte[0]);
                }
            }
            return null;
        }

        @Override
        public String getName() {
            return "lessonResourceResolver";
        }
    }
}
