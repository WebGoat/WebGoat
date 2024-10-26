/**
 * ************************************************************************************************
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 *
 * <p>Copyright (c) 2002 - 2014 Bruce Mayhew
 *
 * <p>This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * <p>This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * <p>You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * <p>Getting Source ==============
 *
 * <p>Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository
 * for free software projects.
 *
 * <p>
 *
 * @author WebGoat
 * @version $Id: $Id
 * @since December 12, 2015
 */
package org.owasp.webgoat.container;

import static org.asciidoctor.Asciidoctor.Factory.create;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Attributes;
import org.asciidoctor.Options;
import org.asciidoctor.extension.JavaExtensionRegistry;
import org.owasp.webgoat.container.asciidoc.*;
import org.owasp.webgoat.container.i18n.Language;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.templateresource.ITemplateResource;
import org.thymeleaf.templateresource.StringTemplateResource;

/**
 * Thymeleaf resolver for AsciiDoc used in the lesson, can be used as follows inside a lesson file:
 *
 * <p><code>
 * <div th:replace="~{doc:AccessControlMatrix_plan.adoc}"></div>
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
  protected ITemplateResource computeTemplateResource(
      IEngineConfiguration configuration,
      String ownerTemplate,
      String template,
      String resourceName,
      String characterEncoding,
      Map<String, Object> templateResolutionAttributes) {
    var templateName = resourceName.substring(PREFIX.length());
    log.debug("template used: {}", templateName);
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
      return new StringTemplateResource(
          "<div>Unable to find documentation for: " + templateName + " </div>");
    }
  }

  private InputStream getInputStream(String templateName) throws IOException {
    log.debug("locale: {}", language.getLocale().getLanguage());
    String computedResourceName =
        computeResourceName(templateName, language.getLocale().getLanguage());
    if (resourceLoader
        .getResource("classpath:/" + computedResourceName)
        .isReadable() /*isFile()*/) {
      log.debug("localized file exists");
      return resourceLoader.getResource("classpath:/" + computedResourceName).getInputStream();
    } else {
      log.debug("using english template");
      return resourceLoader.getResource("classpath:/" + templateName).getInputStream();
    }
  }

  private String computeResourceName(String resourceName, String language) {
    String computedResourceName;
    if (language.equals("en")) {
      computedResourceName = resourceName;
    } else {
      computedResourceName = resourceName.replace(".adoc", "_".concat(language).concat(".adoc"));
    }
    log.debug("computed local file name: {}", computedResourceName);
    log.debug(
        "file exists: {}",
        resourceLoader.getResource("classpath:/" + computedResourceName).isReadable());
    return computedResourceName;
  }

  private Options createAttributes() {

    return Options.builder()
        .attributes(
            Attributes.builder()
                .attribute("source-highlighter", "coderay")
                .attribute("backend", "xhtml")
                .attribute("lang", determineLanguage())
                .attribute("icons", org.asciidoctor.Attributes.FONT_ICONS)
                .build())
        .build();
  }

  private String determineLanguage() {
    HttpServletRequest request =
        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

    Locale browserLocale =
        (Locale)
            request.getSession().getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
    if (null != browserLocale) {
      log.debug("browser locale {}", browserLocale);
      return browserLocale.getLanguage();
    } else {
      String langHeader = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
      if (null != langHeader) {
        log.debug("browser locale {}", langHeader);
        return langHeader.substring(0, 2);
      } else {
        log.debug("browser default english");
        return "en";
      }
    }
  }
}
