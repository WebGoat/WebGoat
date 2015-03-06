package org.owasp.webgoat.plugins;

import com.google.common.base.Optional;
import org.owasp.webgoat.lessons.AbstractLesson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static org.owasp.webgoat.plugins.PluginFileUtils.fileEndsWith;
import static org.owasp.webgoat.plugins.PluginFileUtils.hasParentDirectoryWithName;

public class Plugin {

    private static final String NAME_LESSON_SOLUTION_DIRECTORY = "lessonSolutions";
    private static final String NAME_LESSON_PLANS_DIRECTORY = "lessonPlans";
    private static final String NAME_LESSON_I18N_DIRECTORY = "i18n";
    private final Logger logger = LoggerFactory.getLogger(Plugin.class);
    private final Path pluginDirectory;

    private Class<AbstractLesson> lesson;
    private Map<String, File> solutionLanguageFiles = new HashMap<>();
    private Map<String, File> lessonPlansLanguageFiles = new HashMap<>();
    private File lessonSourceFile;

    public static class PluginLoadingFailure extends RuntimeException {

        public PluginLoadingFailure(String message) {
            super(message);
        }

        public PluginLoadingFailure(String message, Exception e) {
            super(message, e);
        }
    }

    public Plugin(Path pluginDirectory) {
        this.pluginDirectory = pluginDirectory;
    }

    public void loadClasses(Map<String, byte[]> classes) {
        for (Map.Entry<String, byte[]> clazz : classes.entrySet()) {
            loadClass(clazz.getKey(), clazz.getValue());
        }
        if (lesson == null) {
            throw new PluginLoadingFailure(String
                .format("Lesson class not found, following classes were detected in the plugin: %s",
                    StringUtils.collectionToCommaDelimitedString(classes.keySet())));
        }
    }

    private void loadClass(String name, byte[] classFile) {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        PluginClassLoader pluginClassLoader = new PluginClassLoader(contextClassLoader, name, classFile);
        try {
            String realClassName = name.replaceFirst("/", "").replaceAll("/", ".").replaceAll(".class", "");
            Class clazz = pluginClassLoader.loadClass(realClassName);
            if (AbstractLesson.class.isAssignableFrom(clazz)) {
                this.lesson = clazz;
            }
        } catch (ClassNotFoundException e) {
            logger.error("Unable to load class {}", name);
        }
    }

    public void loadFiles(List<Path> files, boolean reload) {
        for (Path file : files) {
            if (fileEndsWith(file, ".html") && hasParentDirectoryWithName(file, NAME_LESSON_SOLUTION_DIRECTORY)) {
                solutionLanguageFiles.put(file.getParent().getFileName().toString(), file.toFile());
            }
            if (fileEndsWith(file, ".html") && hasParentDirectoryWithName(file, NAME_LESSON_PLANS_DIRECTORY)) {
                lessonPlansLanguageFiles.put(file.getParent().getFileName().toString(), file.toFile());
            }
            if (fileEndsWith(file, ".java")) {
                lessonSourceFile = file.toFile();
            }
            if (fileEndsWith(file, ".properties") && hasParentDirectoryWithName(file, NAME_LESSON_I18N_DIRECTORY)) {
                copyProperties(reload, file);
            }
        }
    }

    private void copyProperties(boolean reload, Path file) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            Files.copy(file, bos);
            Path propertiesPath = createPropertiesDirectory();
            ResourceBundleClassLoader.setPropertiesPath(propertiesPath);
            if ( reload ) {
                Files.write(propertiesPath.resolve(file.getFileName()), bos.toByteArray(), CREATE, APPEND);
            } else {
                Files.write(propertiesPath.resolve(file.getFileName()), bos.toByteArray(), CREATE, TRUNCATE_EXISTING);
            }
        } catch (IOException io) {
            throw new PluginLoadingFailure("Property file detected, but unable to copy the properties", io);
        }
    }

    private Path createPropertiesDirectory() throws IOException {
        if (Files.exists(pluginDirectory.resolve(NAME_LESSON_I18N_DIRECTORY))) {
            return pluginDirectory.resolve(NAME_LESSON_I18N_DIRECTORY);
        } else {
            return Files.createDirectory(pluginDirectory.resolve(NAME_LESSON_I18N_DIRECTORY));
        }
    }

    public void rewritePaths(Path pluginTarget) {
        try {
            PluginFileUtils.replaceInFiles(this.lesson.getSimpleName() + "_files", pluginTarget.getFileName().toString() + "/plugin/" + this.lesson.getSimpleName() + "/lessonSolutions/en/" + this.lesson.getSimpleName() + "_files", solutionLanguageFiles.values());
            PluginFileUtils.replaceInFiles(this.lesson.getSimpleName() + "_files", pluginTarget.getFileName().toString() + "/plugin/" + this.lesson.getSimpleName() + "/lessonPlans/en/" + this.lesson.getSimpleName() + "_files", lessonPlansLanguageFiles.values());
        } catch (IOException e) {
            throw new PluginLoadingFailure("Unable to rewrite the paths in the solutions", e);
        }
    }

    public AbstractLesson getLesson() {
        try {
            return lesson.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new PluginLoadingFailure("Unable to instantiate the lesson " + lesson.getName(), e);
        }
    }

    public Optional<File> getLessonSolution(String language) {
        return Optional.fromNullable(this.solutionLanguageFiles.get(language));
    }

    public Map<String, File> getLessonSolutions() {
        return this.solutionLanguageFiles;
    }

    public Optional<File> getLessonSource() {
        return Optional.fromNullable(lessonSourceFile);
    }

    public Map<String, File> getLessonPlans() {
        return this.lessonPlansLanguageFiles;
    }

}
