package org.owasp.webgoat.plugins;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.nio.file.Paths;

/**
 * Created by nanne_000 on 9/29/2015.
 */
@WebListener
public class PluginContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
        String pluginPath = event.getServletContext().getRealPath("plugin_lessons");
        String targetPath = event.getServletContext().getRealPath("plugin_extracted");

        if (event.getServletContext().getInitParameter("plugins_loaded") == null) {
            new PluginsLoader(Paths.get(pluginPath), Paths.get(targetPath)).copyJars();
        }
        event.getServletContext().setInitParameter("plugins_loaded", "");
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
//        String targetPath = event.getServletContext().getRealPath("plugin_extracted");
//        WebappClassLoader cl = (WebappClassLoader)Thread.currentThread().getContextClassLoader();
//        cl.closeJARs(true);
//        Path webInfLib = Paths.get(targetPath).getParent().resolve(cl.getJarPath().replaceFirst("\\/", ""));
//        try {
//            FileUtils.cleanDirectory(webInfLib.toFile());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
