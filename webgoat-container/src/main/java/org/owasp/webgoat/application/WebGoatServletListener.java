/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.owasp.webgoat.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

/**
 * Web application lifecycle listener.
 *
 * @author rlawson
 * @version $Id: $Id
 */
public class WebGoatServletListener implements ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(WebGoatServletListener.class);

    /** {@inheritDoc} */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        context.log("WebGoat is starting");
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
}
