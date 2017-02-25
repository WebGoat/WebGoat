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

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.ByteStreams;
import lombok.SneakyThrows;
import org.springframework.core.io.ResourceLoader;
import org.thymeleaf.TemplateProcessingParameters;
import org.thymeleaf.resourceresolver.IResourceResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Map;

/**
 * Dynamically resolve a lesson. In the html file this can be invoked as:
 *
 * <code>
 *    <div th:case="true" th:replace="lesson:__${lesson.class.simpleName}__"></div>
 * </code>
 *
 * Thymeleaf will invoke this resolver based on the prefix and this implementation will resolve the html in the plugins directory
 */
public class LessonTemplateResolver extends TemplateResolver {

    private final static String PREFIX = "lesson:";
    private final File pluginTargetDirectory;
    private ResourceLoader resourceLoader;
    private Map<String, byte[]> resources = Maps.newHashMap();

    public LessonTemplateResolver(File pluginTargetDirectory, ResourceLoader resourceLoader) {
        this.pluginTargetDirectory = pluginTargetDirectory;
        this.resourceLoader = resourceLoader;
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
        @SneakyThrows
        public InputStream getResourceAsStream(TemplateProcessingParameters params, String resourceName) {
            byte[] resource = resources.get(resourceName);
            if (resource == null) {
                resource = ByteStreams.toByteArray(resourceLoader.getResource("classpath:/html/" + resourceName + ".html").getInputStream());
                resources.put(resourceName, resource);
            }
            return new ByteArrayInputStream(resource);
        }

        @Override
        public String getName() {
            return "lessonResourceResolver";
        }
    }
}
