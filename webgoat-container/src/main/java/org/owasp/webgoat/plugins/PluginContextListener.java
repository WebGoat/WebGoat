package org.owasp.webgoat.plugins;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.nio.file.Paths;

/**
 * Copy the plugins to the WEB-INF/lib directory to take advantage of the automatic reloading of an application
 * server.
 */
@WebListener
public class PluginContextListener implements ServletContextListener {

    private static boolean alreadyLoaded = false;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        String pluginPath = event.getServletContext().getRealPath("plugin_lessons");
        String targetPath = event.getServletContext().getRealPath("plugin_extracted");

        if (!alreadyLoaded) {
            new PluginsLoader(Paths.get(pluginPath), Paths.get(targetPath)).copyJars();
            alreadyLoaded = true;
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
    }
}
