package org.owasp.webgoat.plugins;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class PluginsLoader implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Path path;

    public PluginsLoader(Path path) {
        this.path = path;
    }

    public List<Plugin> loadPlugins(final boolean reload) {
        final List<Plugin> plugins = new ArrayList<Plugin>();
        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    try {
                        PluginExtractor extractor = new PluginExtractor(file);
                        extractor.extract();
                        Plugin plugin = new Plugin(extractor.getBaseDirectory());
                        plugin.loadClasses(extractor.getClasses());
                        plugin.loadFiles(extractor.getFiles(), reload);
                        plugins.add(plugin);
                    } catch (Plugin.PluginLoadingFailure e) {
                       logger.error("Unable to load plugin, continue reading others...");
                    }
                    return FileVisitResult.CONTINUE;
                }

            });
        } catch (IOException e) {
            logger.error("Loading plugins failed", e);
        }
        return plugins;
    }


    @Override
    public void run() {
        loadPlugins(true);
    }
}
