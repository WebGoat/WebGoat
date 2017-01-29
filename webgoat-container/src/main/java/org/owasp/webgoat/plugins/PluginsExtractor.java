package org.owasp.webgoat.plugins;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * <p>PluginsLoader class.</p>
 *
 * @author dm
 * @version $Id: $Id
 */
@Slf4j
public class PluginsExtractor {

    private static final String WEBGOAT_PLUGIN_EXTENSION = "jar";
    private static final int BUFFER_SIZE = 32 * 1024;
    private final File pluginTargetDirectory;
    private final PluginClassLoader classLoader;

    public PluginsExtractor(File pluginTargetDirectory, PluginClassLoader pluginClassLoader) {
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
            log.trace("Determining whether we run as standalone jar or as directory...");
            if (ResourceUtils.isFileURL(location)) {
                log.trace("Running from directory, copying lessons from {}", location.toString());
                extractToTargetDirectoryFromExplodedDirectory(ResourceUtils.getFile(location));
            } else {
                log.trace("Running from standalone jar, extracting lessons from {}", location.toString());
                extractToTargetDirectoryFromJarFile(ResourceUtils.getFile(ResourceUtils.extractJarFileURL(location)));
            }
            List<URL> jars = listJars();
            plugins = processPlugins(jars);
        } catch (Exception e) {
            log.error("Loading plugins failed", e);
        }
        return plugins;
    }

    private void extractToTargetDirectoryFromJarFile(File jarFile) throws IOException {
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
        log.trace("Extracting {} to {}", jar.getName(), pluginTargetDirectory);
    }

    private void extractToTargetDirectoryFromExplodedDirectory(File directory) throws IOException {
        Files.walkFileTree(directory.toPath(), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                if (dir.endsWith("plugin_lessons")) {
                    log.trace("Copying {} to {}", dir.toString(), pluginTargetDirectory);
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
                    log.trace("Found jar file at location: {}", file.toString());
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
                    log.trace("Plugin jar '{}' contains a lesson, loading into WebGoat...", plugin.getOriginationJar());
                    plugins.add(plugin);
                } else {
                    log.trace("Plugin jar: '{}' does not contain a lesson not processing as a plugin (can be a utility jar)",
                            plugin.getOriginationJar());
                }
            }
            new MessagePropertiesMerger(pluginTargetDirectory).mergeAllLanguage();
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
