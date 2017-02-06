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
package org.owasp.webgoat.i18n;

import lombok.AllArgsConstructor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;
import java.util.Properties;

/**
 * <p>ExposedReloadableResourceMessageBundleSource class.</p>
 * Extends the reloadable message source with a way to get all messages
 *
 * @author zupzup
 */
@AllArgsConstructor
public class Messages extends ReloadableResourceBundleMessageSource {

    private final LocaleResolver localeResolver;

    /**
     * Gets all messages for presented Locale.
     *
     * @return all messages
     */
    public Properties getMessages() {
        return getMergedProperties(resolveLocale()).getProperties();
    }

    public String getMessage(String code, Object... args) {
        return getMessage(code, args, resolveLocale());
    }

    public String getMessage(String code, String defaultValue, Object... args) {
        return super.getMessage(code, args, defaultValue, resolveLocale());
    }

    protected Locale resolveLocale() {
        return localeResolver.resolveLocale(((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest());
    }



}
