package org.owasp.webgoat.plugins;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.owasp.webgoat.classloader.PluginClassLoader;
import org.owasp.webgoat.lessons.AbstractLesson;
import org.owasp.webgoat.util.LabelProvider;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.owasp.webgoat.plugins.PluginFileUtils.fileEndsWith;
import static org.owasp.webgoat.plugins.PluginFileUtils.hasParentDirectoryWithName;
import static org.owasp.webgoat.plugins.PluginFileUtils.replaceInFiles;

/**
 * <p>Plugin class.</p>
 *
 * @version $Id: $Id
 */
public class Plugin {

    private static final String NAME_LESSON_SOLUTION_DIRECTORY = "lessonSolutions";
    private static final String NAME_LESSON_PLANS_DIRECTORY = "lessonPlans";
    private final Path pluginDirectory;

    private Class<AbstractLesson> lesson;
    private Map<String, File> solutionLanguageFiles = new HashMap<>();
    private Map<String, File> lessonPlansLanguageFiles = new HashMap<>();
    private List<File> pluginFiles = Lists.newArrayList();
    private File lessonSourceFile;

    /**
     * <p>Constructor for Plugin.</p>
     *
     * @param pluginDirectory a {@link java.nio.file.Path} object.
     */
    public Plugin(Path pluginDirectory) {
        Preconditions.checkNotNull(pluginDirectory, "plugin directory cannot be null");
        Preconditions.checkArgument(Files.exists(pluginDirectory), "directory %s does not exists", pluginDirectory);
        this.pluginDirectory = pluginDirectory;
    }

    /**
     * <p>Constructor for Plugin.</p>
     *
     * @param pluginDirectory a {@link java.nio.file.Path} object.
     * @param classes a {@link java.util.List} object.
     */
    public Plugin(Path pluginDirectory, List<String> classes) {
        this(pluginDirectory);
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
            throw new PluginLoadingFailure("Class " + realClassName + " listed in jar but unable to load the class.",
                    ce);
        }
    }

    /**
     * <p>loadProperties.</p>
     *
     * @param properties a {@link java.util.List} object.
     */
    public void loadProperties(List<Path> properties) {
        for (Path propertyFile : properties) {
            LabelProvider.updatePluginResources(propertyFile);
            LabelProvider.refresh();
        }
    }

    /**
     * <p>loadFiles.</p>
     *
     * @param files a {@link java.util.List} object.
     * @param reload a boolean.
     */
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

            if (fileEndsWith(file, ".css", ".jsp", ".js")) {
                pluginFiles.add(file.toFile());
            }
        }
    }

    /**
     * <p>rewritePaths.</p>
     *
     * @param pluginTarget a {@link java.nio.file.Path} object.
     */
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

            String[] replacements = {"jsp", "js"};
            for (String replacement : replacements) {
                String s = String.format("plugin/%s/%s/", this.lesson.getSimpleName(), replacement);
                String r = String.format("%s/plugin/%s/%s/", pluginTarget.getFileName().toString(),
                        this.lesson.getSimpleName(), replacement);
                replaceInFiles(s, r, pluginFiles);
                replaceInFiles(s, r, Arrays.asList(lessonSourceFile));
            }

            //CSS with url('/plugin/images') should not begin with / otherwise image cannot be found
            String s = String.format("/plugin/%s/images/", this.lesson.getSimpleName());
            String r = String
                    .format("%s/plugin/%s/images/", pluginTarget.getFileName().toString(), this.lesson.getSimpleName());
            replaceInFiles(s, r, pluginFiles);
            replaceInFiles(s, r, Arrays.asList(lessonSourceFile));


        } catch (IOException e) {
            throw new PluginLoadingFailure("Unable to rewrite the paths in the solutions", e);
        }
    }

    /**
     * Lesson is optional, it is also possible that the supplied jar contains only helper classes.
     *
     * @return a {@link com.google.common.base.Optional} object.
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

    /**
     * <p>getLessonSolution.</p>
     *
     * @param language a {@link java.lang.String} object.
     * @return a {@link com.google.common.base.Optional} object.
     */
    public Optional<File> getLessonSolution(String language) {
        return Optional.fromNullable(this.solutionLanguageFiles.get(language));
    }

    /**
     * <p>getLessonSolutions.</p>
     *
     * @return a {@link java.util.Map} object.
     */
    public Map<String, File> getLessonSolutions() {
        return this.solutionLanguageFiles;
    }

    /**
     * <p>getLessonSource.</p>
     *
     * @return a {@link com.google.common.base.Optional} object.
     */
    public Optional<File> getLessonSource() {
        return Optional.fromNullable(lessonSourceFile);
    }

    /**
     * <p>getLessonPlans.</p>
     *
     * @return a {@link java.util.Map} object.
     */
    public Map<String, File> getLessonPlans() {
        return this.lessonPlansLanguageFiles;
    }

}
