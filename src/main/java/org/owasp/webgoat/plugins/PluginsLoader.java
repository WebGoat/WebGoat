package org.owasp.webgoat.plugins;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class PluginsLoader {

    private final Path path;

    public PluginsLoader(Path path) {
        this.path = path;
    }

    public List<Plugin> loadPlugins() {
        final List<Plugin> plugins = new ArrayList<Plugin>();
        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    plugins.add(new PluginExtractor(file).extract());
                    return FileVisitResult.CONTINUE;
                }

            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return plugins;
    }


}
