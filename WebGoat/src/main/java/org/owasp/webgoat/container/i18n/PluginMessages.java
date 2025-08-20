/*
 * SPDX-FileCopyrightText: Copyright © 2017 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container.i18n;

import java.io.IOException;
import java.util.Properties;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * Message resource bundle for plugins.
 */
public class PluginMessages extends ReloadableResourceBundleMessageSource {
  private static final String PROPERTIES_SUFFIX = ".properties";

  private final Language language;
  private final ResourcePatternResolver resourcePatternResolver;

  public PluginMessages(
      Messages messages, Language language, ResourcePatternResolver resourcePatternResolver) {
    this.language = language;
    this.setParentMessageSource(messages);
    this.setBasename("WebGoatLabels");
    this.resourcePatternResolver = resourcePatternResolver;
  }

  @Override
  protected PropertiesHolder refreshProperties(String filename, PropertiesHolder propHolder) {
    Properties properties = new Properties();
    long lastModified = System.currentTimeMillis();

    try {
      var resources =
          resourcePatternResolver.getResources(
              "classpath:/lessons/**/i18n" + "/WebGoatLabels" + PROPERTIES_SUFFIX);
      for (var resource : resources) {
        String sourcePath = resource.getURI().toString().replace(PROPERTIES_SUFFIX, "");
        PropertiesHolder holder = super.refreshProperties(sourcePath, propHolder);
        properties.putAll(holder.getProperties());
      }
    } catch (IOException e) {
      logger.error("Unable to read plugin message", e);
    }

    return new PropertiesHolder(properties, lastModified);
  }

  public Properties getMessages() {
    return getMergedProperties(language.getLocale()).getProperties();
  }

  public String getMessage(String code, Object... args) {
    return getMessage(code, args, language.getLocale());
  }

  public String getMessage(String code, String defaultValue, Object... args) {
    return super.getMessage(code, args, defaultValue, language.getLocale());
  }
}
