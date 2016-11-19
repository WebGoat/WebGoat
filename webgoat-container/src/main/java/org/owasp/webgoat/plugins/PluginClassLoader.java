package org.owasp.webgoat.plugins;

import java.net.URL;
import java.net.URLClassLoader;

public class PluginClassLoader extends URLClassLoader {

    public PluginClassLoader(ClassLoader parent) {
        super(new URL[] {}, parent);
    }

    @Override
    public void addURL(URL url) {
        super.addURL(url);
    }
}
