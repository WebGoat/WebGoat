package org.owasp.webgoat.plugins;

import com.google.common.collect.Lists;
import com.google.common.io.Files;
import org.apache.commons.fileupload.util.Streams;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Extract the jar file and place them in the system temp directory in the folder webgoat and collect the files
 * and classes.
 *
 * @author dm
 * @version $Id: $Id
 */
public class PluginExtractor {

    private final List<String> classes = Lists.newArrayList();
    private final List<Path> files = new ArrayList<>();

    /**
     * <p>extractJarFile.</p>
     *
     * @param archive a {@link java.io.File} object.
     * @param targetDirectory a {@link java.io.File} object.
     * @return a {@link org.owasp.webgoat.plugins.Plugin} object.
     * @throws java.io.IOException if any.
     */
    public Plugin extractJarFile(final File archive, final File targetDirectory) throws IOException {
        ZipFile zipFile = new ZipFile(archive);
        Plugin plugin = new Plugin();
        try {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                final ZipEntry zipEntry = entries.nextElement();
                if (shouldProcessFile(zipEntry)) {
                    boolean processed = processClassFile(zipEntry);

                    if (!processed) {
                        processed = processPropertyFile(zipFile, zipEntry, targetDirectory);
                    }
                    if (!processed) {
                        processFile(plugin, zipFile, zipEntry, targetDirectory);
                    }
                }
            }
        } finally {
            plugin.findLesson(this.classes);
            if (plugin.getLesson().isPresent()) {
                plugin.rewritePaths(targetDirectory.toPath());
            }
            zipFile.close();
        }
        return plugin;
    }

    private void processFile(Plugin plugin, ZipFile zipFile, ZipEntry zipEntry, File targetDirectory)
            throws IOException {
        final File targetFile = new File(targetDirectory, zipEntry.getName());
        copyFile(zipFile, zipEntry, targetFile, false);
        plugin.loadFiles(targetFile.toPath());
    }

    private boolean processPropertyFile(ZipFile zipFile, ZipEntry zipEntry, File targetDirectory)
            throws IOException {
        if (zipEntry.getName().endsWith(".properties")) {
            final File targetFile = new File(targetDirectory, zipEntry.getName());
            copyFile(zipFile, zipEntry, targetFile, true);
            return true;
        }
        return false;
    }

    private boolean processClassFile(ZipEntry zipEntry) {
        if (zipEntry.getName().endsWith(".class")) {
            classes.add(zipEntry.getName());
            return true;
        }
        return false;
    }

    private boolean shouldProcessFile(ZipEntry zipEntry) {
        return !zipEntry.isDirectory() && !zipEntry.getName().startsWith("META-INF");
    }

    private File copyFile(ZipFile zipFile, ZipEntry zipEntry, File targetFile, boolean append) throws IOException {
        Files.createParentDirs(targetFile);
        try (FileOutputStream fos = new FileOutputStream(targetFile, append)) {
            Streams.copy(zipFile.getInputStream(zipEntry), fos, true);
        }
        return targetFile;
    }

    /**
     * <p>Getter for the field <code>classes</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<String> getClasses() {
        return this.classes;
    }

    /**
     * <p>Getter for the field <code>files</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<Path> getFiles() {
        return this.files;
    }
}
