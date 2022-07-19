
/**
 * ************************************************************************************************
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
 * <p>
 *
 * @author WebGoat
 * @version $Id: $Id
 * @since December 12, 2015
 */

package org.owasp.webgoat.container;

import io.undertow.util.Headers;
import lombok.extern.slf4j.Slf4j;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.extension.JavaExtensionRegistry;
import org.owasp.webgoat.container.asciidoc.*;
import org.owasp.webgoat.container.i18n.Language;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.templateresource.ITemplateResource;
import org.thymeleaf.templateresource.StringTemplateResource;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static org.asciidoctor.Asciidoctor.Factory.create;

/**
 * Thymeleaf resolver for AsciiDoc used in the lesson, can be used as follows inside a lesson file:
 * <p>
 * <code>
 * <div th:replace="doc:AccessControlMatrix_plan.adoc"></div>
 * </code>
 */
@Slf4j
public class AsciiDoctorTemplateResolver extends FileTemplateResolver {

    private static final Asciidoctor asciidoctor = create();
    private static final String PREFIX = "doc:";

    private final Language language;
    private final ResourceLoader resourceLoader;

    public AsciiDoctorTemplateResolver(Language language, ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        this.language = language;
        setResolvablePatterns(Set.of(PREFIX + "*"));
    }

    @Override
    protected ITemplateResource computeTemplateResource(IEngineConfiguration configuration, String ownerTemplate, String template, String resourceName, String characterEncoding, Map<String, Object> templateResolutionAttributes) {
        var templateName = resourceName.substring(PREFIX.length());

        try (InputStream is = getInputStream(templateName)) {
            JavaExtensionRegistry extensionRegistry = asciidoctor.javaExtensionRegistry();
            extensionRegistry.inlineMacro("webWolfLink", WebWolfMacro.class);
            extensionRegistry.inlineMacro("webWolfRootLink", WebWolfRootMacro.class);
            extensionRegistry.inlineMacro("webGoatVersion", WebGoatVersionMacro.class);
            extensionRegistry.inlineMacro("webGoatTempDir", WebGoatTmpDirMacro.class);
            extensionRegistry.inlineMacro("operatingSystem", OperatingSystemMacro.class);
            extensionRegistry.inlineMacro("username", UsernameMacro.class);

            StringWriter writer = new StringWriter();
            asciidoctor.convert(new InputStreamReader(is), writer, createAttributes());
            return new StringTemplateResource(writer.getBuffer().toString());
        } catch (IOException e) {
            return new StringTemplateResource("<div>Unable to find documentation for: " + templateName + " </div>");
        }
    }

    private InputStream getInputStream(String templateName) throws IOException {
        if (resourceLoader.getResource("classpath:/" + computeResourceName(templateName, language.getLocale().getLanguage())).isFile()) {
            return resourceLoader.getResource("classpath:/" + computeResourceName(templateName, language.getLocale().getLanguage())).getInputStream();
        } else {
            return resourceLoader.getResource("classpath:/" + templateName).getInputStream();
        }
    }
    private String computeResourceName(String resourceName, String language) {
        if (language.equals("en")) {
            return resourceName;
        } else {
            return resourceName.replace(".adoc", "_".concat(language).concat(".adoc"));
        }
    }

    private Map<String, Object> createAttributes() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("source-highlighter", "coderay");
        attributes.put("backend", "xhtml");
        attributes.put("lang", determineLanguage());
        attributes.put("icons", org.asciidoctor.Attributes.FONT_ICONS);

        Map<String, Object> options = new HashMap<>();
        options.put("attributes", attributes);

        return options;
    }

    private String determineLanguage() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        Locale browserLocale = (Locale) request.getSession().getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
        if (null != browserLocale) {
            return browserLocale.getLanguage();
        } else {
            String langHeader = request.getHeader(Headers.ACCEPT_LANGUAGE_STRING);
            if (null != langHeader) {
                return langHeader.substring(0,2);
            } else {
                return "en";
            }
        }
    }
}
