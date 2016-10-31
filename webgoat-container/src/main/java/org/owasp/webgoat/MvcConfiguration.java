/**
 *************************************************************************************************
 *
 *
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 *
 * Copyright (c) 2002 - 20014 Bruce Mayhew
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * Getting Source ==============
 *
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software
 * projects.
 *
 * @author WebGoat
 * @since October 28, 2003
 * @version $Id: $Id
 */
package org.owasp.webgoat;

import com.google.common.collect.Sets;
import org.owasp.webgoat.session.Course;
import org.owasp.webgoat.session.LabelDebugger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

import java.io.File;

/**
 * Configuration for Spring MVC
 */
@Configuration
public class MvcConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    @Qualifier("pluginTargetDirectory")
    private File pluginTargetDirectory;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/lesson_content").setViewName("lesson_content");
        registry.addViewController("/start.mvc").setViewName("main_new");
    }

    @Bean
    public TemplateResolver springThymeleafTemplateResolver(ApplicationContext applicationContext) {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setPrefix("classpath:/templates/");
        resolver.setSuffix(".html");
        resolver.setOrder(1);
        resolver.setApplicationContext(applicationContext);
        return resolver;
    }

    @Bean
    public LessonTemplateResolver lessonTemplateResolver() {
        LessonTemplateResolver resolver = new LessonTemplateResolver(pluginTargetDirectory);
        resolver.setOrder(2);
        resolver.setCacheable(false);
        return resolver;
    }

    @Bean
    public AsciiDoctorTemplateResolver asciiDoctorTemplateResolver() {
        AsciiDoctorTemplateResolver resolver = new AsciiDoctorTemplateResolver(pluginTargetDirectory);
        resolver.setCacheable(true);
        resolver.setOrder(3);
        return resolver;
    }

    @Bean
    public SpringTemplateEngine thymeleafTemplateEngine(TemplateResolver springThymeleafTemplateResolver,
                                                        LessonTemplateResolver lessonTemplateResolver,
                                                        AsciiDoctorTemplateResolver asciiDoctorTemplateResolver) {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.addDialect(new SpringSecurityDialect());
        engine.setTemplateResolvers(
                Sets.newHashSet(springThymeleafTemplateResolver, lessonTemplateResolver, asciiDoctorTemplateResolver));
        return engine;
    }

    /**
     * This way we expose the plugins target directory as a resource within the web application.
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/plugin_lessons/**").addResourceLocations("file:///" + pluginTargetDirectory.toString() + "/");
    }

    @Bean
    public HammerHead hammerHead(Course course) {
        return new HammerHead(course);
    }

    @Bean
    public LabelDebugger labelDebugger() {
        return new LabelDebugger();
    }
}