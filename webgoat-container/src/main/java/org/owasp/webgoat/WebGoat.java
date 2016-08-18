/**
 * ************************************************************************************************
 * <p>
 * <p>
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
 *
 * @author WebGoat
 * @version $Id: $Id
 * @since October 28, 2003
 */
package org.owasp.webgoat;

import org.owasp.webgoat.plugins.PluginClassLoader;
import org.owasp.webgoat.plugins.PluginsLoader;
import org.owasp.webgoat.session.Course;
import org.owasp.webgoat.session.UserTracker;
import org.owasp.webgoat.session.WebSession;
import org.owasp.webgoat.session.WebgoatContext;
import org.owasp.webgoat.session.WebgoatProperties;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import javax.servlet.ServletContext;
import java.io.File;

@SpringBootApplication
@PropertySource("classpath:/webgoat.properties")
public class WebGoat extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(WebGoat.class);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(WebGoat.class, args);
    }

    @Bean(name = "pluginTargetDirectory")
    public File pluginTargetDirectory() {
        File tempDir = com.google.common.io.Files.createTempDir();
        tempDir.deleteOnExit();
        return tempDir;
    }

    @Bean
    public PluginClassLoader pluginClassLoader() {
        return new PluginClassLoader(PluginClassLoader.class.getClassLoader());
    }

    @Bean
    public PluginsLoader pluginsLoader(@Qualifier("pluginTargetDirectory") File pluginTargetDirectory, PluginClassLoader classLoader) {
        return new PluginsLoader(pluginTargetDirectory, classLoader);
    }

    @Bean
    @Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public WebSession webSession(Course course, WebgoatContext webgoatContext, ServletContext context) {
        return new WebSession(course, webgoatContext, context);
    }

    @Bean
    public LessonEndpointProvider lessonEndpointProvider(ApplicationContext applicationContext, BeanFactory factory, PluginClassLoader cl) {
        LessonEndpointProvider lessonEndpointProvider = new LessonEndpointProvider("org.owasp.webgoat", applicationContext, factory, cl);
        return lessonEndpointProvider;
    }

    @Bean
    public Course course(PluginsLoader pluginsLoader, WebgoatContext webgoatContext, ServletContext context, WebgoatProperties webgoatProperties,
                         LessonEndpointProvider endpointProvider) {
        Course course = new Course(webgoatProperties);
        course.loadCourses(webgoatContext, context, "/");
        course.loadLessonFromPlugin(pluginsLoader.loadPlugins());
        endpointProvider.registerEndpoints();
        return course;
    }

    @Bean
    public UserTracker userTracker() {
        UserTracker userTracker = UserTracker.instance();
        return userTracker;
    }
}
