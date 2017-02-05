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

import lombok.SneakyThrows;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;

import java.io.File;
import java.util.Properties;

/**
 * Message resource bundle for plugins. The files is created after startup during the init of the plugins so we
 * need to load this file through a ResourceLoader instead of location on the classpath.
 *
 * @author nbaars
 * @date 2/4/17
 */
public class PluginMessages extends ReloadableResourceBundleMessageSource {

    private Messages messages;

    public PluginMessages(Messages messages) {
        this.messages = messages;
        this.setParentMessageSource(messages);
    }

    public Properties getMessages() {
        return getMergedProperties(messages.resolveLocale()).getProperties();
    }

    public String getMessage(String code, Object... args) {
        return getMessage(code, args, messages.resolveLocale());
    }

    public String getMessage(String code, String defaultValue, Object... args) {
        return super.getMessage(code, args, defaultValue, messages.resolveLocale());
    }

    public void addPluginMessageBundles(final File i18nPluginDirectory) {
        this.setBasename("WebGoatLabels");
        this.setResourceLoader(new ResourceLoader() {
            @Override
            @SneakyThrows
            public Resource getResource(String location) {
                return new UrlResource(new File(i18nPluginDirectory, location).toURI());
            }

            @Override
            public ClassLoader getClassLoader() {
                return Thread.currentThread().getContextClassLoader();
            }
        });
    }
}
