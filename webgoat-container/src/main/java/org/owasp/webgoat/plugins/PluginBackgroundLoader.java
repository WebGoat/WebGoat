package org.owasp.webgoat.plugins;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.nio.file.Paths;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebListener
/**
 * <p>PluginBackgroundLoader class.</p>
 *
 * @version $Id: $Id
 */
public class PluginBackgroundLoader implements ServletContextListener {

    private ScheduledExecutorService scheduler;

    /** {@inheritDoc} */
    @Override
    public void contextInitialized(ServletContextEvent event) {
        String pluginPath = event.getServletContext().getRealPath("plugin_lessons");
        String targetPath = event.getServletContext().getRealPath("plugin_extracted");

        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new PluginsLoader(Paths.get(pluginPath), Paths.get(targetPath)), 0, 5, TimeUnit.MINUTES);
    }

    /** {@inheritDoc} */
    @Override
    public void contextDestroyed(ServletContextEvent event) {
        scheduler.shutdownNow();
    }
}
