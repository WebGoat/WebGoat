package org.owasp.webgoat.plugins;

import com.google.common.collect.Lists;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.String.format;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.owasp.webgoat.plugins.PluginFileUtils.createDirsIfNotExists;

/**
 * Extract the jar file and place them in the system temp directory in the folder webgoat and collect the files
 * and classes.
 */
public class PluginExtractor {

    private final Path pluginArchive;
    private final List<String> classes = Lists.newArrayList();
    private final List<Path> files = new ArrayList<>();

    public PluginExtractor(Path pluginArchive) {
        this.pluginArchive = pluginArchive;
    }

    public void extract(final Path target) {
        try (FileSystem zip = createZipFileSystem()) {
            final Path root = zip.getPath("/");
            Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (file.toString().endsWith(".class")) {
                        classes.add(file.toString());
                    }
                    files.add(Files.copy(file, createDirsIfNotExists(Paths.get(target.toString(), file.toString())), REPLACE_EXISTING));
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (Exception e) {
            new PluginLoadingFailure(format("Unable to extract: %s", pluginArchive.getFileName()), e);
        }
    }

    public List<String> getClasses() {
        return this.classes;
    }

    public List<Path> getFiles() {
        return this.files;
    }

    private FileSystem createZipFileSystem() throws Exception {
        final URI uri = URI.create("jar:file:" + pluginArchive.toUri().getPath());
        return FileSystems.newFileSystem(uri, new HashMap<String, Object>());
    }


}
