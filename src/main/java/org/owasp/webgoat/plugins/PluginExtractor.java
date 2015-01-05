package org.owasp.webgoat.plugins;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
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
import java.util.HashMap;
import java.util.Map;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * Extract the wpf file and collect the classes to load and remember the base directory in which we extracted
 * the files (lesson plans etc)
 */
public class PluginExtractor {

    private static final String DIRECTORY = "webgoat";
    private final Path pluginArchive;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Map<String, byte[]> classes = new HashMap<String, byte[]>();
    private Path baseDirectory;

    public PluginExtractor(Path pluginArchive) {
        this.pluginArchive = pluginArchive;
    }

    public void extract() {
        try (FileSystem zip = createZipFileSystem()) {
            final Path root = zip.getPath("/");
            baseDirectory = Paths.get(System.getProperty("java.io.tmpdir"), DIRECTORY);
            Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (file.toString().endsWith(".class")) {
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        Files.copy(file, bos);
                        classes.put(file.toString(), bos.toByteArray());
                    }
                    Files.copy(file, Paths.get(baseDirectory.toString(), file.toString()), REPLACE_EXISTING);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException io) {
            logger.error(String.format("Unable to extract: %s", pluginArchive.getFileName()), io);
        }
    }

    public Map<String, byte[]> getClasses() {
        return this.classes;
    }

    public Path getBaseDirectory() {
        return this.baseDirectory;
    }

    private FileSystem createZipFileSystem() throws IOException {
        final URI uri = URI.create("jar:file:" + pluginArchive.toUri().getPath());
        return FileSystems.newFileSystem(uri, new HashMap<String, Object>());
    }

    private void closeZipFileSystem(FileSystem zip) {
        if (zip != null) {
            try {
                zip.close();
            } catch (IOException e) {
                //ignore
            }
        }
    }


}
