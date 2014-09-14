/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.owasp.webgoat.application;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Web application lifecycle listener.
 *
 * @author rlawson
 */
public class WebGoatServletListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        context.log("WebGoat is starting");
        setApplicationVariables(context);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        context.log("WebGoat is stopping");
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
