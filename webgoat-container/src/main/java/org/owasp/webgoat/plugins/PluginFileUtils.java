package org.owasp.webgoat.plugins;


import com.google.common.base.Preconditions;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PluginFileUtils {

    public static boolean fileEndsWith(Path p, String s) {
        return p.getFileName().toString().endsWith(s);
    }

    public static boolean fileEndsWith(Path p, String... suffixes) {
        for (String suffix : suffixes) {
            if (fileEndsWith(p, suffix)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasParentDirectoryWithName(Path p, String s) {
        if (p == null || p.getParent() == null || p.getParent().equals(p.getRoot())) {
            return false;
        }
        if (p.getParent().getFileName().toString().equals(s)) {
            return true;
        }
        return hasParentDirectoryWithName(p.getParent(), s);
    }

    public static Path createDirsIfNotExists(Path p) throws IOException {
        if (Files.notExists(p)) {
            Files.createDirectories(p);
        }
        return p;
    }

    public static List<Path> getFilesInDirectory(Path directory) throws IOException {
        List<Path> files = new ArrayList<>();
        DirectoryStream<Path> dirStream;
        dirStream = Files.newDirectoryStream(directory);
        for (Path entry : dirStream) {
            files.add(entry);
        }
        dirStream.close();
        return files;
    }

    public static void replaceInFiles(String replace, String with, Collection<File> files) throws IOException {
        Preconditions.checkNotNull(replace);
        Preconditions.checkNotNull(with);
        Preconditions.checkNotNull(files);

        for (File file : files) {
            replaceInFile(replace, with, Paths.get(file.toURI()));
        }
    }

    public static void replaceInFile(String replace, String with, Path file) throws IOException {
        Preconditions.checkNotNull(replace);
        Preconditions.checkNotNull(with);
        Preconditions.checkNotNull(file);

        byte[] fileAsBytes = Files.readAllBytes(file);
        String fileAsString = new String(fileAsBytes);
        fileAsString = fileAsString.replaceAll(replace, with);
        Files.write(file, fileAsString.getBytes());
    }

    public static void writeFile(Path targetFile, byte[] bytes, OpenOption... options) throws IOException {
        createDirsIfNotExists(targetFile.getParent());
        if (!Files.exists(targetFile)) {
            Files.createFile(targetFile);
        }
        Files.write(targetFile, bytes, options);
    }

}
