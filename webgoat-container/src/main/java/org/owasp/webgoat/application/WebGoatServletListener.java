/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.owasp.webgoat.application;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;

import org.owasp.webgoat.HammerHead;
import org.owasp.webgoat.lessons.LessonServletMapping;
import org.owasp.webgoat.plugins.PluginsLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

/**
 * Web application lifecycle listener.
 *
 * @author rlawson
 * @version $Id: $Id
 */
public class WebGoatServletListener implements ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(HammerHead.class);

    /** {@inheritDoc} */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        context.log("WebGoat is starting");
        setApplicationVariables(context);
        context.log("Adding extra mappings for lessions");

        loadPlugins(sce);
        loadServlets(sce);
    }

    private void loadServlets(ServletContextEvent sce) {
        final ServletContext servletContext = sce.getServletContext();
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(
                false);
        provider.addIncludeFilter(new AnnotationTypeFilter(LessonServletMapping.class));
        Set<BeanDefinition> candidateComponents = provider.findCandidateComponents("org.owasp.webgoat");
        try {
            for (BeanDefinition beanDefinition : candidateComponents) {
                Class controllerClass = Class.forName(beanDefinition.getBeanClassName());
                LessonServletMapping pathAnnotation = (LessonServletMapping) controllerClass.getAnnotation(LessonServletMapping.class);
                final ServletRegistration.Dynamic dynamic = servletContext.addServlet(controllerClass.getSimpleName(), controllerClass);
                dynamic.addMapping(pathAnnotation.path());
            }
        } catch (Exception e) {
            logger.error("Error", e);
        }
    }

    private void loadPlugins(ServletContextEvent sce) {
        String pluginPath = sce.getServletContext().getRealPath("plugin_lessons");
        String targetPath = sce.getServletContext().getRealPath("plugin_extracted");
        new PluginsLoader(Paths.get(pluginPath), Paths.get(targetPath)).loadPlugins();
    }

    /** {@inheritDoc} */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        context.log("WebGoat is stopping");

        // Unregister JDBC drivers in this context's ClassLoader:
        // Get the webapp's ClassLoader
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        // Loop through all drivers
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            java.sql.Driver driver = drivers.nextElement();
            if (driver.getClass().getClassLoader() == cl) {
                // This driver was registered by the webapp's ClassLoader, so deregister it:
                try {
                    context.log("Unregister JDBC driver {}");
                    DriverManager.deregisterDriver(driver);
                } catch (SQLException ex) {
                    context.log("Error unregistering JDBC driver {}");
                }
            } else {
                // driver was not registered by the webapp's ClassLoader and may be in use elsewhere
                context.log("Not unregistering JDBC driver {} as it does not belong to this webapp's ClassLoader");
            }
        }
    }

    private void setApplicationVariables(ServletContext context) {
        Application app = Application.getInstance();
        try {
            InputStream inputStream = context.getResourceAsStream("/META-INF/MANIFEST.MF");
            Manifest manifest = new Manifest(inputStream);
            Attributes attr = manifest.getMainAttributes();
            String name = attr.getValue("Specification-Title");
            String version = attr.getValue("Specification-Version");
            String build = attr.getValue("Implementation-Version");
            app.setName(name);
            app.setVersion(version);
            app.setBuild(build);
        } catch (IOException ioe) {
            context.log("Error setting application variables", ioe);
        }
    }
}
