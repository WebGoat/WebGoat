package org.owasp.webgoat.plugins;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.owasp.webgoat.classloader.PluginClassLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

public class PluginsLoader implements Runnable {

    protected static final String WEBGOAT_PLUGIN_EXTENSION = "jar";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Path pluginSource;
    private Path pluginTarget;


    public PluginsLoader(Path pluginSource, Path pluginTarget) {
        Preconditions.checkNotNull(pluginSource, "plugin source cannot be null");
        Preconditions.checkNotNull(pluginTarget, "plugin target cannot be null");

        this.pluginSource = pluginSource;
        this.pluginTarget = pluginTarget;
    }

    public List<Plugin> loadPlugins(final boolean reload) {
        final PluginClassLoader cl = (PluginClassLoader)Thread.currentThread().getContextClassLoader();
        List<Plugin> plugins = Lists.newArrayList();

        try {
            PluginFileUtils.createDirsIfNotExists(pluginTarget);
            List<URL> jars = listJars();
            cl.addURL(jars);
            plugins = processPlugins(jars, reload);
        } catch (IOException | URISyntaxException e) {
            logger.error("Loading plugins failed", e);
        }
        return plugins;
    }

    private List<URL> listJars() throws IOException {
        final List<URL> jars = Lists.newArrayList();
        Files.walkFileTree(pluginSource, new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (PluginFileUtils.fileEndsWith(file, WEBGOAT_PLUGIN_EXTENSION)) {
                    jars.add(file.toUri().toURL());
                }
                return FileVisitResult.CONTINUE;
            }
        });
        return jars;
    }

    private List<Plugin> processPlugins(List<URL> jars, boolean reload) throws URISyntaxException, IOException {
        final List<Plugin> plugins = Lists.newArrayList();
        for (URL jar : jars) {

            PluginExtractor extractor = new PluginExtractor(Paths.get(jar.toURI()));
            extractor.extract(pluginTarget);

            Plugin plugin = new Plugin(pluginTarget, extractor.getClasses());
            if (plugin.getLesson().isPresent()) {
                PluginFileUtils.createDirsIfNotExists(pluginTarget);
                plugin.loadFiles(extractor.getFiles(), reload);
                plugin.rewritePaths(pluginTarget);
                plugins.add(plugin);
            }
        }
        return plugins;
    }

    @Override
    public void run() {
        loadPlugins(true);
    }
}
