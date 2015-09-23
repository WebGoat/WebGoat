package org.owasp.webgoat.plugins.classloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLClassLoader;

/**
 * Holds the classloaders for the plugins. For now all the plugins are loaded by the same
 * classloader. This class can be extended to contain a classloader per plugin.
 */
public class PluginClassLoaderRepository {

    private static final Logger logger = LoggerFactory.getLogger(PluginClassLoaderRepository.class);
    private URLClassLoader currentPluginLoader;

    /**
     * @return the plugin classloader
     */
    public URLClassLoader get() {
        return currentPluginLoader;
    }

    public void replaceClassLoader(URLClassLoader classLoader) {
        if (this.currentPluginLoader != null) {
            try {
                this.currentPluginLoader.close();
            } catch (IOException e) {
                logger.warn("Unable to close the current classloader", e);
            }
        }
        this.currentPluginLoader = classLoader;
    }
}
