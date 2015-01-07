package org.owasp.webgoat.plugins;

import org.owasp.webgoat.lessons.AbstractLesson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Plugin {

    private static final Logger logger = LoggerFactory.getLogger(Plugin.class);
    private final Class<AbstractLesson> lesson;
    private final Path pluginDirectory;
    private final Map<String, File> solutionLanguageFiles;
    private final Map<String, File> lessonPlansLanguageFiles;
    private final File lessonSourceFile;

    public static class PluginLoadingFailure extends RuntimeException {

        public PluginLoadingFailure(String message) {
            super(message);
        }
    }

    public static class Builder {

        private Path pluginDirectory;
        private Class lesson;
        private final List<String> loadedClasses = new ArrayList<String>();
        private final Map<String, File> solutionLanguageFiles = new HashMap<>();
        private final Map<String, File> lessonPlansLanguageFiles = new HashMap<>();
        private File javaSource;

        public Builder loadClasses(Map<String, byte[]> classes) {
            for (Map.Entry<String, byte[]> clazz : classes.entrySet() ) {
                loadClass(clazz.getKey(), clazz.getValue());
            }
            return this;
        }

        public Builder loadClass(String name, byte[] classFile) {
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            PluginClassLoader pluginClassLoader = new PluginClassLoader(contextClassLoader, classFile);
            try {
                String realClassName = name.replaceFirst("/", "").replaceAll("/", ".").replaceAll(".class", "");
                Class clazz = pluginClassLoader.loadClass(realClassName);
                if (AbstractLesson.class.isAssignableFrom(clazz)) {
                    this.lesson = clazz;
                }
                loadedClasses.add(clazz.getName());
            } catch (ClassNotFoundException e) {
                logger.error("Unable to load class {}", name);
            }
            return this;
        }

        public Builder setBaseDirectory(Path pluginDirectory) {
            this.pluginDirectory = pluginDirectory;
            return this;
        }

        public Plugin build() {
            if ( lesson == null ) {
                throw new PluginLoadingFailure(String.format("Lesson class not found, following classes were detected in the plugin: %s",
                    StringUtils.collectionToCommaDelimitedString(loadedClasses)));
            }
            return new Plugin(this.lesson, pluginDirectory, lessonPlansLanguageFiles, solutionLanguageFiles, javaSource);
        }

        public void loadFiles(List<Path> files) {
            for (Path file : files) {
                if (file.getFileName().toString().endsWith(".html") && file.getParent().getParent().getFileName().toString()
                    .endsWith("lessonSolutions")) {
                    solutionLanguageFiles.put(file.getParent().getFileName().toString(), file.toFile());
                }
                if (file.getFileName().toString().endsWith(".html") && file.getParent().getParent().getFileName().toString()
                    .endsWith("lessonPlans")) {
                    lessonPlansLanguageFiles.put(file.getParent().getFileName().toString(), file.toFile());
                }
                if ( file.getFileName().toString().endsWith(".java")) {
                    javaSource = file.toFile();
                }
            }
        }
    }

    public Plugin(Class<AbstractLesson> lesson, Path pluginDirectory, Map<String, File> lessonPlansLanguageFiles,
        Map<String, File> solutionLanguageFiles, File lessonSourceFile) {
        this.lesson = lesson;
        this.pluginDirectory = pluginDirectory;
        this.lessonPlansLanguageFiles = lessonPlansLanguageFiles;
        this.solutionLanguageFiles = solutionLanguageFiles;
        this.lessonSourceFile = lessonSourceFile;
    }

    public Class<AbstractLesson> getLesson() {
        return lesson;
    }

    public Map<String, File> getLessonSolutions() {
        return this.solutionLanguageFiles;
    }

    public File getLessonSource() { return lessonSourceFile; }

    public Map<String, File> getLessonPlans() {
        return this.lessonPlansLanguageFiles;
    }
}
