package org.owasp.webgoat.classloader;

import org.apache.catalina.loader.WebappClassLoader;

import java.net.URL;
import java.util.List;

/**
 * Classloader for Tomcat.
 *
 * We need to provide this classloader otherwise jsp files cannot be compiled. JspContextWrapper uses
 * Thread.currentThread().getContextClassLoader() but during initialisation it loads the classloader which means
 * this classloader will never pickup the plugin classes.
 *
 * With this loader we can add jars we load during the plugin loading and the jsp will pick it up because this is
 * the same classloader.
 *
 * @version $Id: $Id
 */
public class PluginClassLoader extends WebappClassLoader {
    /**
     * <p>Constructor for PluginClassLoader.</p>
     */
    public PluginClassLoader() {
    }

    /**
     * <p>Constructor for PluginClassLoader.</p>
     *
     * @param parent a {@link java.lang.ClassLoader} object.
     */
    public PluginClassLoader(ClassLoader parent) {
        super(parent);
    }

    /**
     * <p>addURL.</p>
     *
     * @param urls a {@link java.util.List} object.
     */
    public void addURL(List<URL> urls) {
        for (URL url : urls) {
            super.addURL(url);
        }
    }
}
