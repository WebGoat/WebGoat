package org.owasp.webgoat.plugins.classloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

/**
 * Create a classloader for the plugins
 */
public class PluginClassLoaderFactory {

    private static final Logger logger = LoggerFactory.getLogger(PluginClassLoaderFactory.class);

    public static URLClassLoader createClassLoader(List<URL> urls) {
        return new URLClassLoader(urls.toArray(new URL[urls.size()]), determineParentClassLoader());
    }

    private static ClassLoader determineParentClassLoader() {
        ClassLoader parent = Thread.currentThread().getContextClassLoader();
        try {
            parent = Thread.currentThread().getContextClassLoader().getParent()
                    .loadClass("org.apache.jasper.runtime.JspContextWrapper").getClassLoader();
        } catch (ClassNotFoundException e) {
            logger.info("Tomcat JspContextWrapper not found, probably not running on Tomcat...");
        }
        return parent;
    }
}
