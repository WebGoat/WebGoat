/**
 * ************************************************************************************************
 * <p>
 * <p>
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 * <p>
 * Copyright (c) 2002 - 2014 Bruce Mayhew
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
 *
 * @author WebGoat
 * @version $Id: $Id
 * @since October 28, 2003
 */

package org.owasp.webgoat.container;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ResourceLoader;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.templateresource.ITemplateResource;
import org.thymeleaf.templateresource.StringTemplateResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Dynamically resolve a lesson. In the html file this can be invoked as:
 *
 * <code>
 * <div th:case="true" th:replace="lesson:__${lesson.class.simpleName}__"></div>
 * </code>
 * <p>
 * Thymeleaf will invoke this resolver based on the prefix and this implementation will resolve the html in the plugins directory
 */
@Slf4j
public class LessonTemplateResolver extends FileTemplateResolver {

    private static final String PREFIX = "lesson:";
    private ResourceLoader resourceLoader;
    private Map<String, byte[]> resources = new HashMap<>();

    public LessonTemplateResolver(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        setResolvablePatterns(Set.of(PREFIX + "*"));
    }

    @Override
    protected ITemplateResource computeTemplateResource(IEngineConfiguration configuration, String ownerTemplate, String template, String resourceName, String characterEncoding, Map<String, Object> templateResolutionAttributes) {
        var templateName = resourceName.substring(PREFIX.length());
        byte[] resource = resources.get(templateName);
        if (resource == null) {
            try {
                resource = resourceLoader.getResource("classpath:/" + templateName).getInputStream().readAllBytes();
            } catch (IOException e) {
                log.error("Unable to find lesson HTML: {}", template);
            }
            resources.put(templateName, resource);
        }
        return new StringTemplateResource(new String(resource, StandardCharsets.UTF_8));
    }
}
