package org.owasp.webgoat.plugins;

public class PluginClassLoader extends ClassLoader {

    private final byte[] classFile;

    public PluginClassLoader(ClassLoader parent, byte[] classFile) {
        super(parent);
        this.classFile = classFile;
    }

    public Class findClass(String name) {
        return defineClass(name, classFile, 0, classFile.length);
    }

    public static void main(String[] args) {

    }


}

