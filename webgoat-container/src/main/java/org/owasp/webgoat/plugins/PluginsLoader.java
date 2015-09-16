package org.owasp.webgoat.plugins;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.owasp.webgoat.classloader.PluginClassLoader;
import org.owasp.webgoat.util.LabelProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>PluginsLoader class.</p>
 *
 * @version $Id: $Id
 */
public class PluginsLoader implements Runnable {

    /** Constant <code>WEBGOAT_PLUGIN_EXTENSION="jar"</code> */
    protected static final String WEBGOAT_PLUGIN_EXTENSION = "jar";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Path pluginSource;
    private Path pluginTarget;


    /**
     * <p>Constructor for PluginsLoader.</p>
     *
     * @param pluginSource a {@link java.nio.file.Path} object.
     * @param pluginTarget a {@link java.nio.file.Path} object.
     */
    public PluginsLoader(Path pluginSource, Path pluginTarget) {
        Preconditions.checkNotNull(pluginSource, "plugin source cannot be null");
        Preconditions.checkNotNull(pluginTarget, "plugin target cannot be null");

        this.pluginSource = pluginSource;
        this.pluginTarget = pluginTarget;
    }

    /**
     * <p>loadPlugins.</p>
     *
     * @param reload a boolean.
     * @return a {@link java.util.List} object.
     */
    public List<Plugin> loadPlugins(final boolean reload) {
        final PluginClassLoader cl = (PluginClassLoader) Thread.currentThread().getContextClassLoader();
        List<Plugin> plugins = Lists.newArrayList();

        try {
            PluginFileUtils.createDirsIfNotExists(pluginTarget);
            List<URL> jars = listJars();
            cl.addURL(jars);
            plugins = processPlugins(jars, reload);
        } catch (Exception e) {
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

    private List<Plugin> processPlugins(List<URL> jars, boolean reload) throws Exception {
        final List<Plugin> plugins = Lists.newArrayList();
        final ExecutorService executorService = Executors.newFixedThreadPool(20);
        final CompletionService<PluginExtractor> completionService = new ExecutorCompletionService<>(executorService);
        final List<Callable<PluginExtractor>> callables = extractJars(jars);

        for (Callable<PluginExtractor> s : callables) {
            completionService.submit(s);
        }
        int n = callables.size();
        for (int i = 0; i < n; i++) {
            PluginExtractor extractor = completionService.take().get();
            Plugin plugin = new Plugin(pluginTarget, extractor.getClasses());
            if (plugin.getLesson().isPresent()) {
                PluginFileUtils.createDirsIfNotExists(pluginTarget);
                plugin.loadFiles(extractor.getFiles(), reload);
                plugin.loadProperties(extractor.getProperties());
                plugin.rewritePaths(pluginTarget);
                plugins.add(plugin);
            }
        }
        LabelProvider.refresh();
        return plugins;
    }

    private List<Callable<PluginExtractor>> extractJars(List<URL> jars) {
        List<Callable<PluginExtractor>> extractorCallables = Lists.newArrayList();
        for (final URL jar : jars) {
            extractorCallables.add(new Callable<PluginExtractor>() {

                @Override
                public PluginExtractor call() throws Exception {
                    PluginExtractor extractor = new PluginExtractor(Paths.get(jar.toURI()));
                    extractor.extract(pluginTarget);
                    return extractor;
                }
            });
        }
        return extractorCallables;
    }

    /** {@inheritDoc} */
    @Override
    public void run() {
        loadPlugins(true);
    }
}
