package org.owasp.webgoat.plugins;

import com.google.common.collect.Lists;
import org.apache.catalina.loader.WebappClassLoader;
import org.apache.commons.io.FileUtils;
import org.owasp.webgoat.util.LabelProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>PluginsLoader class.</p>
 *
 * @version $Id: $Id
 * @author dm
 */
public class PluginsLoader {

    private static final String WEBGOAT_PLUGIN_EXTENSION = "jar";
    private static boolean alreadyLoaded = false;
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
        this.pluginSource = Objects.requireNonNull(pluginSource, "plugin source cannot be null");
        this.pluginTarget = Objects.requireNonNull(pluginTarget, "plugin target cannot be null");
    }

    /**
     * Copy jars to the lib directory
     */
    public void copyJars() {
        try {
            if (!alreadyLoaded) {
                WebappClassLoader cl = (WebappClassLoader) Thread.currentThread().getContextClassLoader();
                List<URL> jars = listJars();
                for (URL jar : jars) {
                    cl.addRepository(jar.toString());
                }
                alreadyLoaded = true;
            }
        } catch (Exception e) {
            logger.error("Copying plugins failed", e);
        }
    }

    /**
     * <p>loadPlugins.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<Plugin> loadPlugins() {
        copyJars();
        List<Plugin> plugins = Lists.newArrayList();

        try {
            PluginFileUtils.createDirsIfNotExists(pluginTarget);
            cleanupExtractedPluginsDirectory();
            List<URL> jars = listJars();

            plugins = processPlugins(jars);
        } catch (Exception e) {
            logger.error("Loading plugins failed", e);
        }
        return plugins;
    }

    private void cleanupExtractedPluginsDirectory() {
        Path i18nDirectory = pluginTarget.resolve("plugin/i18n/");
        FileUtils.deleteQuietly(i18nDirectory.toFile());
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

    private List<Plugin> processPlugins(List<URL> jars) throws Exception {
        final ExecutorService executorService = Executors.newFixedThreadPool(10);
        try {
            final List<Plugin> plugins = Lists.newArrayList();
            final CompletionService<Plugin> completionService = new ExecutorCompletionService<>(executorService);
            final List<Callable<Plugin>> callables = extractJars(jars);

            for (Callable<Plugin> s : callables) {
                completionService.submit(s);
            }
            int n = callables.size();
            for (int i = 0; i < n; i++) {
                Plugin plugin = completionService.take().get();
                if (plugin.getLesson().isPresent()) {
                    plugins.add(plugin);
                }
            }
            LabelProvider.updatePluginResources(pluginTarget.resolve("plugin/i18n/WebGoatLabels.properties"));
            return plugins;
        } finally {
            executorService.shutdown();
        }
    }

    private List<Callable<Plugin>> extractJars(List<URL> jars) {
        List<Callable<Plugin>> extractorCallables = Lists.newArrayList();
        for (final URL jar : jars) {
            extractorCallables.add(new Callable<Plugin>() {

                @Override
                public Plugin call() throws Exception {
                    PluginExtractor extractor = new PluginExtractor();
                    return extractor.extractJarFile(ResourceUtils.getFile(jar), pluginTarget.toFile());
                }
            });
        }
        return extractorCallables;
    }
}
