
/**
 * ************************************************************************************************
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 * <p>
 * Copyright (c) 2002 - 20014 Bruce Mayhew
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

package org.owasp.webgoat;

import lombok.extern.slf4j.Slf4j;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.extension.JavaExtensionRegistry;
import org.owasp.webgoat.asciidoc.*;
import org.owasp.webgoat.i18n.Language;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.templateresource.ITemplateResource;
import org.thymeleaf.templateresource.StringTemplateResource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.HashMap;
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

    public AsciiDoctorTemplateResolver(Language language) {
        this.language = language;
        setResolvablePatterns(Set.of(PREFIX + "*"));
    }

    @Override
    protected ITemplateResource computeTemplateResource(IEngineConfiguration configuration, String ownerTemplate, String template, String resourceName, String characterEncoding, Map<String, Object> templateResolutionAttributes) {
        var templateName = resourceName.substring(PREFIX.length());
        try (InputStream is = readInputStreamOrFallbackToEnglish(templateName, language)) {
            if (is == null) {
                log.warn("Resource name: {} not found, did you add the adoc file?", templateName);
                return new StringTemplateResource("");
            } else {
                JavaExtensionRegistry extensionRegistry = asciidoctor.javaExtensionRegistry();
                extensionRegistry.inlineMacro("webWolfLink", WebWolfMacro.class);
                extensionRegistry.inlineMacro("webWolfRootLink", WebWolfRootMacro.class);
                extensionRegistry.inlineMacro("webGoatVersion", WebGoatVersionMacro.class);
                extensionRegistry.inlineMacro("webGoatTempDir", WebGoatTmpDirMacro.class);
                extensionRegistry.inlineMacro("operatingSystem", OperatingSystemMacro.class);

                StringWriter writer = new StringWriter();
                asciidoctor.convert(new InputStreamReader(is), writer, createAttributes());
                return new StringTemplateResource(writer.getBuffer().toString());
            }
        } catch (IOException e) {
            //no html yet
            return new StringTemplateResource("");
        }
    }

    /**
     * The resource name is for example HttpBasics_content1.adoc. This is always located in the following directory:
     * <code>plugin/HttpBasics/lessonPlans/en/HttpBasics_content1.adoc</code>
     */
    private String computeResourceName(String resourceName, String language) {
        return String.format("lessonPlans/%s/%s", language, resourceName);
    }

    private InputStream readInputStreamOrFallbackToEnglish(String resourceName, Language language) {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(computeResourceName(resourceName, language.getLocale().getLanguage()));
        if (is == null) {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(computeResourceName(resourceName, "en"));
        }
        return is;
    }

    private Map<String, Object> createAttributes() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("source-highlighter", "coderay");
        attributes.put("backend", "xhtml");

        Map<String, Object> options = new HashMap<>();
        options.put("attributes", attributes);

        return options;
    }
}
