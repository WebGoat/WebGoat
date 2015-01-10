package org.owasp.webgoat.plugins;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ResourceBundleClassLoader {

    private final static ResourceBundleClassLoader classLoader = new ResourceBundleClassLoader();
    private Path propertiesPath;

    private ResourceBundleClassLoader() {
    }

    public static void setPropertiesPath(Path path) {
        classLoader.propertiesPath = path;
    }

    public static ClassLoader createPropertyFilesClassLoader(ClassLoader parentClassLoader) {
        final List<URL> urls = new ArrayList<>();

        try {
            urls.add(classLoader.propertiesPath.toUri().toURL());
        } catch (IOException e) {
            throw new Plugin.PluginLoadingFailure("Unable to load the properties for the classloader", e);
        }
        return new URLClassLoader(urls.toArray(new URL[urls.size()]), Thread.currentThread().getContextClassLoader());
    }

}