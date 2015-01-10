package org.owasp.webgoat.plugins;


import java.nio.file.Path;

public class PluginFileUtils {

    public static boolean fileEndsWith(Path p, String s) {
        return p.getFileName().toString().endsWith(s);
    }

    public static boolean hasParentDirectoryWithName(Path p, String s) {
        if (p == null || p.getParent() == null || p.getRoot().equals(p.getParent())) {
            return false;
        }
        if (p.getParent().getFileName().toString().equals(s)) {
            return true;
        }
        return hasParentDirectoryWithName(p.getParent(), s);
    }

}
