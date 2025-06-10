/*
 * SPDX-FileCopyrightText: Copyright © 2016 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.owasp.webgoat.container.i18n.Language;
import org.owasp.webgoat.container.i18n.Messages;
import org.owasp.webgoat.container.i18n.PluginMessages;
import org.owasp.webgoat.container.session.LabelDebugger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresource.ITemplateResource;
import org.thymeleaf.templateresource.StringTemplateResource;

/** Configuration for Spring MVC */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class MvcConfiguration implements WebMvcConfigurer {

  private static final String UTF8 = "UTF-8";

  private final LessonResourceScanner lessonScanner;

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController("/login").setViewName("login");
    registry.addViewController("/lesson_content").setViewName("lesson_content");
    registry.addViewController("/start.mvc").setViewName("main_new");
  }

  @Bean
  public ViewResolver viewResolver(SpringTemplateEngine thymeleafTemplateEngine) {
    ThymeleafViewResolver resolver = new ThymeleafViewResolver();
    resolver.setTemplateEngine(thymeleafTemplateEngine);
    resolver.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
    return resolver;
  }

  /**
   * Responsible for loading lesson templates based on Thymeleaf, for example:
   *
   * <p><div th:include="/lessons/spoofcookie/templates/spoofcookieform.html" id="content"></div>
   */
  @Bean
  public ITemplateResolver lessonThymeleafTemplateResolver(ResourceLoader resourceLoader) {
    var resolver =
        new FileTemplateResolver() {
          @Override
          protected ITemplateResource computeTemplateResource(
              IEngineConfiguration configuration,
              String ownerTemplate,
              String template,
              String resourceName,
              String characterEncoding,
              Map<String, Object> templateResolutionAttributes) {
            try (var is =
                resourceLoader.getResource("classpath:" + resourceName).getInputStream()) {
              return new StringTemplateResource(
                  new String(is.readAllBytes(), StandardCharsets.UTF_8));
            } catch (IOException e) {
              return null;
            }
          }
        };
    resolver.setOrder(1);
    return resolver;
  }

  /** Loads all normal WebGoat specific Thymeleaf templates */
  @Bean
  public ITemplateResolver springThymeleafTemplateResolver(ApplicationContext applicationContext) {
    SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
    resolver.setPrefix("classpath:/webgoat/templates/");
    resolver.setSuffix(".html");
    resolver.setTemplateMode(TemplateMode.HTML);
    resolver.setOrder(2);
    resolver.setCharacterEncoding(UTF8);
    resolver.setApplicationContext(applicationContext);
    return resolver;
  }

  /** Loads the html for the complete lesson, see lesson_content.html */
  @Bean
  public LessonTemplateResolver lessonTemplateResolver(ResourceLoader resourceLoader) {
    LessonTemplateResolver resolver = new LessonTemplateResolver(resourceLoader);
    resolver.setOrder(0);
    resolver.setCacheable(false);
    resolver.setCharacterEncoding(UTF8);
    return resolver;
  }

  /** Loads the lesson asciidoc. */
  @Bean
  public AsciiDoctorTemplateResolver asciiDoctorTemplateResolver(
      Language language, ResourceLoader resourceLoader) {
    log.debug("template locale {}", language);
    AsciiDoctorTemplateResolver resolver =
        new AsciiDoctorTemplateResolver(language, resourceLoader);
    resolver.setCacheable(false);
    resolver.setOrder(1);
    resolver.setCharacterEncoding(UTF8);
    return resolver;
  }

  @Bean
  public SpringTemplateEngine thymeleafTemplateEngine(
      ITemplateResolver springThymeleafTemplateResolver,
      LessonTemplateResolver lessonTemplateResolver,
      AsciiDoctorTemplateResolver asciiDoctorTemplateResolver,
      ITemplateResolver lessonThymeleafTemplateResolver) {
    SpringTemplateEngine engine = new SpringTemplateEngine();
    engine.setEnableSpringELCompiler(true);
    engine.addDialect(new SpringSecurityDialect());
    engine.setTemplateResolvers(
        Set.of(
            lessonTemplateResolver,
            asciiDoctorTemplateResolver,
            lessonThymeleafTemplateResolver,
            springThymeleafTemplateResolver));
    return engine;
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    // WebGoat internal
    registry.addResourceHandler("/css/**").addResourceLocations("classpath:/webgoat/static/css/");
    registry.addResourceHandler("/js/**").addResourceLocations("classpath:/webgoat/static/js/");
    registry
        .addResourceHandler("/plugins/**")
        .addResourceLocations("classpath:/webgoat/static/plugins/");
    registry
        .addResourceHandler("/fonts/**")
        .addResourceLocations("classpath:/webgoat/static/fonts/");

    // WebGoat lessons
    registry
        .addResourceHandler("/images/**")
        .addResourceLocations(
            lessonScanner.applyPattern("classpath:/lessons/%s/images/").toArray(String[]::new));
    registry
        .addResourceHandler("/lesson_js/**")
        .addResourceLocations(
            lessonScanner.applyPattern("classpath:/lessons/%s/js/").toArray(String[]::new));
    registry
        .addResourceHandler("/lesson_css/**")
        .addResourceLocations(
            lessonScanner.applyPattern("classpath:/lessons/%s/css/").toArray(String[]::new));
    registry
        .addResourceHandler("/lesson_templates/**")
        .addResourceLocations(
            lessonScanner.applyPattern("classpath:/lessons/%s/templates/").toArray(String[]::new));
    registry
        .addResourceHandler("/video/**")
        .addResourceLocations(
            lessonScanner.applyPattern("classpath:/lessons/%s/video/").toArray(String[]::new));
  }

  @Bean
  public PluginMessages pluginMessages(
      Messages messages, Language language, ResourcePatternResolver resourcePatternResolver) {
    PluginMessages pluginMessages = new PluginMessages(messages, language, resourcePatternResolver);
    pluginMessages.setDefaultEncoding("UTF-8");
    pluginMessages.setBasenames("i18n/WebGoatLabels");
    pluginMessages.setFallbackToSystemLocale(false);
    return pluginMessages;
  }

  @Bean
  public Language language(LocaleResolver localeResolver) {
    return new Language(localeResolver);
  }

  @Bean
  public LocaleResolver localeResolver() {
    SessionLocaleResolver localeResolver = new SessionLocaleResolver();
    return localeResolver;
  }

  @Bean
  public LocaleChangeInterceptor localeChangeInterceptor() {
    LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
    lci.setParamName("lang");
    return lci;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(localeChangeInterceptor());
    registry.addInterceptor(new UserInterceptor());
  }

  @Bean
  public Messages messageSource(Language language) {
    Messages messages = new Messages(language);
    messages.setDefaultEncoding("UTF-8");
    messages.setBasename("classpath:i18n/messages");
    messages.setFallbackToSystemLocale(false);
    return messages;
  }

  @Bean
  public LabelDebugger labelDebugger() {
    return new LabelDebugger();
  }
}
