/*
 * SPDX-FileCopyrightText: Copyright © 2016 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ResourceLoader;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.templateresource.ITemplateResource;
import org.thymeleaf.templateresource.StringTemplateResource;

/**
 * Dynamically resolve a lesson. In the html file this can be invoked as: <code>
 * <div th:case="true" th:replace="lesson:__${lesson.class.simpleName}__"></div>
 * </code>
 *
 * <p>Thymeleaf will invoke this resolver based on the prefix and this implementation will resolve
 * the html in the plugins directory
 */
@Slf4j
public class LessonTemplateResolver extends FileTemplateResolver {

  private static final String PREFIX = "lesson:";
  private final ResourceLoader resourceLoader;
  private final Map<String, byte[]> resources = new HashMap<>();

  public LessonTemplateResolver(ResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
    setResolvablePatterns(Set.of(PREFIX + "*"));
  }

  @Override
  protected ITemplateResource computeTemplateResource(
      IEngineConfiguration configuration,
      String ownerTemplate,
      String template,
      String resourceName,
      String characterEncoding,
      Map<String, Object> templateResolutionAttributes) {
    var templateName = resourceName.substring(PREFIX.length());
    byte[] resource = resources.get(templateName);
    if (resource == null) {
      resource = loadAndCache(templateName);
    }

    if (resource == null) {
      return new StringTemplateResource("Unable to find lesson HTML: %s".formatted(templateName));
    }
    return new StringTemplateResource(new String(resource, StandardCharsets.UTF_8));
  }

  private byte[] loadAndCache(String templateName) {
    try {
      var resource =
          resourceLoader.getResource("classpath:/" + templateName).getInputStream().readAllBytes();
      resources.put(templateName, resource);
      return resource;
    } catch (IOException e) {
      log.error(
          "Unable to find lesson HTML: '{}', does the name of HTML file name match the lesson class name?",
          templateName);
      return null;
    }
  }
}
