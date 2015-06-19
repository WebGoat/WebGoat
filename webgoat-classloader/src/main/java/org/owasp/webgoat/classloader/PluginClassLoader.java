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
 */
public class PluginClassLoader extends WebappClassLoader {
    public PluginClassLoader() {
    }

    public PluginClassLoader(ClassLoader parent) {
        super(parent);
    }

    public void addURL(List<URL> urls) {
        for (URL url : urls) {
            super.addURL(url);
        }
    }
}
