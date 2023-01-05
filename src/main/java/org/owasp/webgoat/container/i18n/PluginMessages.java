/*
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 * <p>
 * Copyright (c) 2002 - 2017 Bruce Mayhew
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
 */

package org.owasp.webgoat.container.i18n;

import java.io.IOException;
import java.util.Properties;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * Message resource bundle for plugins.
 *
 * @author nbaars
 * @date 2/4/17
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
