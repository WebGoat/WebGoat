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
import static org.owasp.webgoat.plugins.PluginFileUtils.fileEndsWith;
import static org.owasp.webgoat.plugins.PluginFileUtils.hasParentDirectoryWithName;

/**
 * Extract the jar file and place them in the system temp directory in the folder webgoat and collect the files
 * and classes.
 *
 * @version $Id: $Id
 */
public class PluginExtractor {

    private static final String NAME_LESSON_I18N_DIRECTORY = "i18n";
    private final Path pluginArchive;
    private final List<String> classes = Lists.newArrayList();
    private final List<Path> files = new ArrayList<>();
    private final List<Path> properties = new ArrayList<>();

    /**
     * <p>Constructor for PluginExtractor.</p>
     *
     * @param pluginArchive a {@link java.nio.file.Path} object.
     */
    public PluginExtractor(Path pluginArchive) {
        this.pluginArchive = pluginArchive;
    }

    /**
     * <p>extract.</p>
     *
     * @param target a {@link java.nio.file.Path} object.
     */
    public void extract(final Path target) {
        try (FileSystem zip = createZipFileSystem()) {
            final Path root = zip.getPath("/");
            Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (file.toString().endsWith(".class")) {
                        classes.add(file.toString());
                    }
                    if (fileEndsWith(file, ".properties") && hasParentDirectoryWithName(file,
                            NAME_LESSON_I18N_DIRECTORY)) {
                        properties.add(Files
                                .copy(file, createDirsIfNotExists(Paths.get(target.toString(), file.toString())),
                                        REPLACE_EXISTING));
                    }
                    files.add(Files.copy(file, createDirsIfNotExists(Paths.get(target.toString(), file.toString())),
                            REPLACE_EXISTING));
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (Exception e) {
            new PluginLoadingFailure(format("Unable to extract: %s", pluginArchive.getFileName()), e);
        }
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

    /**
     * <p>Getter for the field <code>properties</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<Path> getProperties() {
        return this.properties;
    }

    private FileSystem createZipFileSystem() throws Exception {
        final URI uri = URI.create("jar:file:" + pluginArchive.toUri().getPath());
        return FileSystems.newFileSystem(uri, new HashMap<String, Object>());
    }
}
