package org.owasp.webgoat.plugins;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import org.owasp.webgoat.classloader.PluginClassLoader;
import org.owasp.webgoat.lessons.AbstractLesson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static org.owasp.webgoat.plugins.PluginFileUtils.fileEndsWith;
import static org.owasp.webgoat.plugins.PluginFileUtils.hasParentDirectoryWithName;
import static org.owasp.webgoat.plugins.PluginFileUtils.replaceInFiles;

public class Plugin {

    private static final String NAME_LESSON_SOLUTION_DIRECTORY = "lessonSolutions";
    private static final String NAME_LESSON_PLANS_DIRECTORY = "lessonPlans";
    private static final String NAME_LESSON_I18N_DIRECTORY = "i18n";
    private final Path pluginDirectory;

    private Class<AbstractLesson> lesson;
    private Map<String, File> solutionLanguageFiles = new HashMap<>();
    private Map<String, File> lessonPlansLanguageFiles = new HashMap<>();
    private List<File> cssFiles = Lists.newArrayList();
    private File lessonSourceFile;

    public Plugin(Path pluginDirectory) {
        this.pluginDirectory = pluginDirectory;
    }

    public Plugin(Path pluginDirectory, List<String> classes) {
        this.pluginDirectory = pluginDirectory;
        findLesson(classes);
    }

    private void findLesson(List<String> classes) {
        for (String clazzName : classes) {
            findLesson(clazzName);
        }
    }

    private void findLesson(String name) {
        String realClassName = name.replaceFirst("/", "").replaceAll("/", ".").replaceAll(".class", "");
        PluginClassLoader cl = (PluginClassLoader) Thread.currentThread().getContextClassLoader();

        try {
            Class clazz = cl.loadClass(realClassName, true);

            if (AbstractLesson.class.isAssignableFrom(clazz)) {
                this.lesson = clazz;
            }
        } catch (ClassNotFoundException ce) {
            throw new PluginLoadingFailure("Class " + realClassName + " listed in jar but unable to load the class.", ce);
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
            if (fileEndsWith(file, ".css")) {
                cssFiles.add(file.toFile());
            }
        }
    }

    private void copyProperties(boolean reload, Path file) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            Files.copy(file, bos);
            Path propertiesPath = createPropertiesDirectory();
            ResourceBundleClassLoader.setPropertiesPath(propertiesPath);
            PluginFileUtils.createDirsIfNotExists(file.getParent());
            if (reload) {
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
            replaceInFiles(this.lesson.getSimpleName() + "_files",
                    pluginTarget.getFileName().toString() + "/plugin/" + this.lesson
                            .getSimpleName() + "/lessonSolutions/en/" + this.lesson.getSimpleName() + "_files",
                    solutionLanguageFiles.values());
            replaceInFiles(this.lesson.getSimpleName() + "_files",
                    pluginTarget.getFileName().toString() + "/plugin/" + this.lesson
                            .getSimpleName() + "/lessonPlans/en/" + this.lesson.getSimpleName() + "_files",
                    lessonPlansLanguageFiles.values());
            replaceInFiles("setSrc\\(\"js\\/", "setSrc\\(\"" + pluginTarget.getFileName().toString() + "/plugin/" + this.lesson
                    .getSimpleName() + "/js/", Arrays.asList(lessonSourceFile));
            replaceInFiles("url\\(images", "url\\(" + pluginTarget.getFileName().toString() + "/plugin/" + this.lesson
                    .getSimpleName() + "/jsp/images", cssFiles);
        } catch (IOException e) {
            throw new PluginLoadingFailure("Unable to rewrite the paths in the solutions", e);
        }
    }

    /**
     * Lesson is optional, it is also possible that the supplied jar contains only helper classes.
     */
    public Optional<AbstractLesson> getLesson() {
        try {
            if (lesson != null) {
                return Optional.of(lesson.newInstance());
            }
        } catch (IllegalAccessException | InstantiationException e) {
            throw new PluginLoadingFailure("Unable to instantiate the lesson " + lesson.getName(), e);
        }
        return Optional.absent();
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
