/*
 * SPDX-FileCopyrightText: Copyright © 2016 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container.i18n;

import java.util.Locale;
import lombok.AllArgsConstructor;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;

/**
 * Wrapper around the LocaleResolver from Spring so we do not need to bother with passing the
 * HttpRequest object when asking for a Locale.
 */
@AllArgsConstructor
public class Language {

  private final LocaleResolver localeResolver;

  public Locale getLocale() {
    return localeResolver.resolveLocale(
        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest());
  }
}
