package org.owasp.webgoat.plugins;

import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.owasp.webgoat.util.LabelProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * <p>PluginsLoader class.</p>
 *
 * @author dm
 * @version $Id: $Id
 */
public class PluginsLoader {

    private static final String WEBGOAT_PLUGIN_EXTENSION = "jar";
    private static final int BUFFER_SIZE = 32 * 1024;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final File pluginTargetDirectory;
    private final PluginClassLoader classLoader;

    @Autowired
    public PluginsLoader(File pluginTargetDirectory, PluginClassLoader pluginClassLoader) {
        this.classLoader = pluginClassLoader;
        this.pluginTargetDirectory = pluginTargetDirectory;
    }

    /**
     * <p>loadPlugins.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<Plugin> loadPlugins() {
        List<Plugin> plugins = Lists.newArrayList();
        try {
            URL location = this.getClass().getProtectionDomain().getCodeSource().getLocation();
            if (ResourceUtils.isFileURL(location)) {
                extractToTempDirectoryFromExplodedDirectory(ResourceUtils.getFile(location));
            } else {
                extractToTempDirectoryFromJarFile(ResourceUtils.getFile(ResourceUtils.extractJarFileURL(location)));
            }
            List<URL> jars = listJars();
            plugins = processPlugins(jars);
        } catch (Exception e) {
            logger.error("Loading plugins failed", e);
        }
        return plugins;
    }

    private void extractToTempDirectoryFromJarFile(File jarFile) throws IOException {
        ZipFile jar = new ZipFile(jarFile);
        Enumeration<? extends ZipEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            ZipEntry zipEntry = entries.nextElement();
            if (zipEntry.getName().contains("plugin_lessons") && zipEntry.getName().endsWith(".jar")) {
                unpack(jar, zipEntry);
            }
        }
    }

    private void unpack(ZipFile jar, ZipEntry zipEntry) throws IOException {
        try (InputStream inputStream = jar.getInputStream(zipEntry)) {
            String name = zipEntry.getName();
            if (name.lastIndexOf("/") != -1) {
                name = name.substring(name.lastIndexOf("/") + 1);
            }
            try (OutputStream outputStream = new FileOutputStream(new File(pluginTargetDirectory, name))) {
                byte[] buffer = new byte[BUFFER_SIZE];
                int bytesRead = -1;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.flush();
            }
        }
    }

    private void extractToTempDirectoryFromExplodedDirectory(File directory) throws IOException {
        Files.walkFileTree(directory.toPath(), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                if (dir.endsWith("plugin_lessons")) {
                    FileUtils.copyDirectory(dir.toFile(), pluginTargetDirectory);
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private List<URL> listJars() throws Exception {
        final List<URL> jars = Lists.newArrayList();
        Files.walkFileTree(Paths.get(pluginTargetDirectory.toURI()), new SimpleFileVisitor<Path>() {

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

            callables.forEach(s -> completionService.submit(s));
            int n = callables.size();

            for (int i = 0; i < n; i++) {
                Plugin plugin = completionService.take().get();
                if (plugin.getLesson().isPresent()) {
                    plugins.add(plugin);
                }
            }
            LabelProvider.updatePluginResources(
                    pluginTargetDirectory.toPath().resolve("plugin/i18n/WebGoatLabels.properties"));
            return plugins;
        } finally {
            executorService.shutdown();
        }
    }

    private List<Callable<Plugin>> extractJars(List<URL> jars) {
        List<Callable<Plugin>> extractorCallables = Lists.newArrayList();

        for (final URL jar : jars) {
            classLoader.addURL(jar);
            extractorCallables.add(() -> {
                PluginExtractor extractor = new PluginExtractor();
                return extractor.extractJarFile(ResourceUtils.getFile(jar), pluginTargetDirectory, classLoader);
            });
        }
        return extractorCallables;
    }
}
