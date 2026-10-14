/*
 * SPDX-FileCopyrightText: Copyright © 2016 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container.i18n;

import java.util.Properties;
import lombok.AllArgsConstructor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * ExposedReloadableResourceMessageBundleSource class. Extends the reloadable message source with a
 * way to get all messages
 */
@AllArgsConstructor
public class Messages extends ReloadableResourceBundleMessageSource {

  private final Language language;

  /**
   * Gets all messages for presented Locale.
   *
   * @return all messages
   */
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
