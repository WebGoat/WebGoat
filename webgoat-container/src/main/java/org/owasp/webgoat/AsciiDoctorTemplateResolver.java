
/**
 *************************************************************************************************
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
 * @since  December 12, 2015
 * @version $Id: $Id
 */
package org.owasp.webgoat;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.asciidoctor.Asciidoctor;
import org.thymeleaf.TemplateProcessingParameters;
import org.thymeleaf.resourceresolver.IResourceResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

import static org.asciidoctor.Asciidoctor.Factory.create;

/**
 * Thymeleaf resolver for AsciiDoc used in the lesson, can be used as follows inside a lesson file:
 *
 * <code>
 *   <div th:replace="doc:AccessControlMatrix_plan.adoc"></div>
 * </code>
 */
public class AsciiDoctorTemplateResolver extends TemplateResolver {

    private static final Asciidoctor asciidoctor = create();
    private static final String PREFIX = "doc:";
    private final File pluginTargetDirectory;

    public AsciiDoctorTemplateResolver(File pluginTargetDirectory) {
        this.pluginTargetDirectory = pluginTargetDirectory;
        setResourceResolver(new AdocResourceResolver());
        setResolvablePatterns(Sets.newHashSet(PREFIX + "*"));
    }

    @Override
    protected String computeResourceName(TemplateProcessingParameters params) {
        String templateName = params.getTemplateName();
        return templateName.substring(PREFIX.length());
    }

    private class AdocResourceResolver implements IResourceResolver {

        @Override
        public InputStream getResourceAsStream(TemplateProcessingParameters params, String resourceName) {
            try {
                Optional<Path> adocFile = find(pluginTargetDirectory.toPath(), resourceName);
                if (adocFile.isPresent()) {
                    try (FileReader reader = new FileReader(adocFile.get().toFile())) {
                        StringWriter writer = new StringWriter();
                        asciidoctor.convert(reader, writer, createAttributes());
                        return new ByteArrayInputStream(writer.getBuffer().toString().getBytes());
                    }
                }
                return new ByteArrayInputStream(new byte[0]);
            } catch (IOException e) {
                //no html yet
                return new ByteArrayInputStream(new byte[0]);
            }
        }

        private Map<String, Object> createAttributes() {
            Map<String, Object> attributes = Maps.newHashMap();
            attributes.put("source-highlighter", "coderay");

            Map<String, Object> options = Maps.newHashMap();
            options.put("attributes", attributes);

            return options;
        }

        private Optional<Path> find(Path path, String resourceName) throws IOException {
            return Files.walk(path)
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(resourceName)).findFirst();
        }

        @Override
        public String getName() {
            return "adocResourceResolver";
        }
    }
}
