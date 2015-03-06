package org.owasp.webgoat.plugins;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PluginClassLoader extends ClassLoader {

    private final Logger logger = LoggerFactory.getLogger(Plugin.class);
    private final byte[] classFile;

    public PluginClassLoader(ClassLoader parent, String nameOfClass, byte[] classFile) {
        super(parent);
        logger.debug("Creating class loader for {}", nameOfClass);
        this.classFile = classFile;
    }

    public Class findClass(String name) {
        logger.debug("Finding class " + name);
        return defineClass(name, classFile, 0, classFile.length);
    }

}

