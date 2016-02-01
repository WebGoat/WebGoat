
package org.owasp.webgoat.util;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


/**
 * *************************************************************************************************
 *
 *
 * This file is part of WebGoat, an Open Web Application Security Project
 * utility. For details, please see http://www.owasp.org/
 *
 * Copyright (c) 2002 - 20014 Bruce Mayhew
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * Getting Source ==============
 *
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository
 * for free software projects.
 *
 * @version $Id: $Id
 * @author dm
 */
@Component
@Singleton
public class LabelProvider {
    /** Constant <code>DEFAULT_LANGUAGE="Locale.ENGLISH.getLanguage()"</code> */
    public final static String DEFAULT_LANGUAGE = Locale.ENGLISH.getLanguage();

    private static final List<Locale> SUPPORTED = Arrays.asList(Locale.GERMAN, Locale.FRENCH, Locale.ENGLISH,
            Locale.forLanguageTag("ru"));
    private final ReloadableResourceBundleMessageSource labels = new ReloadableResourceBundleMessageSource();
    private static final ReloadableResourceBundleMessageSource pluginLabels = new ReloadableResourceBundleMessageSource();

    /**
     * <p>Constructor for LabelProvider.</p>
     */
    public LabelProvider() {
        labels.setBasename("classpath:/i18n/WebGoatLabels");
        labels.setFallbackToSystemLocale(false);
        labels.setUseCodeAsDefaultMessage(true);
        pluginLabels.setParentMessageSource(labels);
    }

    /**
     * <p>updatePluginResources.</p>
     *
     * @param propertyFile a {@link java.nio.file.Path} object.
     */
    public static void updatePluginResources(final Path propertyFile) {
        pluginLabels.setBasename("WebGoatLabels");
        pluginLabels.setFallbackToSystemLocale(false);
        pluginLabels.setUseCodeAsDefaultMessage(true);
        pluginLabels.setResourceLoader(new ResourceLoader() {
            @Override
            public Resource getResource(String location) {
                try {
                    return new UrlResource(propertyFile.toUri());
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public ClassLoader getClassLoader() {
                return Thread.currentThread().getContextClassLoader();
            }
        });
        pluginLabels.clearCache();
    }

    /**
     * <p>get.</p>
     *
     * @param locale a {@link java.util.Locale} object.
     * @param strName a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public String get(Locale locale, String strName) {
        return pluginLabels.getMessage(strName, null, useLocaleOrFallbackToEnglish(locale));
    }

    private Locale useLocaleOrFallbackToEnglish(Locale locale) {
        return SUPPORTED.contains(locale) ? Locale.ENGLISH : locale;
    }

}
