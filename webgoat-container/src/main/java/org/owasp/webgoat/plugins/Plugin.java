package org.owasp.webgoat.plugins;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import org.owasp.webgoat.lessons.AbstractLesson;
import org.owasp.webgoat.lessons.AssignmentEndpoint;
import org.owasp.webgoat.lessons.NewLesson;
import org.springframework.util.StringUtils;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.owasp.webgoat.plugins.PluginFileUtils.fileEndsWith;
import static org.owasp.webgoat.plugins.PluginFileUtils.hasParentDirectoryWithName;

/**
 * <p>Plugin class.</p>
 *
 * @author dm
 * @version $Id: $Id
 */
public class Plugin {

    private static final String NAME_LESSON_SOLUTION_DIRECTORY = "lessonSolutions";
    private static final String NAME_LESSON_PLANS_DIRECTORY = "lessonPlans";

    private PluginClassLoader classLoader;
    private Class<NewLesson> newLesson;
    private List<Class<AssignmentEndpoint>> lessonEndpoints = Lists.newArrayList();
    private Map<String, File> solutionLanguageFiles = new HashMap<>();
    private Map<String, File> lessonPlansLanguageFiles = new HashMap<>();
    private List<File> pluginFiles = Lists.newArrayList();
    private File lessonSourceFile;

    public Plugin(PluginClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public List<Class<AssignmentEndpoint>> getLessonEndpoints() {
        return this.lessonEndpoints;
    }

    /**
     * <p>findLesson.</p>
     *
     * @param classes a {@link java.util.List} object.
     */
    public void findLesson(List<String> classes) {
        for (String clazzName : classes) {
            findLesson(clazzName);
        }
    }

    private void findLesson(String name) {
        String realClassName = StringUtils.trimLeadingCharacter(name, '/').replaceAll("/", ".").replaceAll(".class", "");

        try {
            Class clazz = classLoader.loadClass(realClassName);
            if (NewLesson.class.isAssignableFrom(clazz)) {
                this.newLesson = clazz;
            }
        } catch (ClassNotFoundException ce) {
            throw new PluginLoadingFailure("Class " + realClassName + " listed in jar but unable to load the class.", ce);
        }
    }

    public void findEndpoints(List<String> classes) {
        for (String clazzName : classes) {
            String realClassName = StringUtils.trimLeadingCharacter(clazzName, '/').replaceAll("/", ".").replaceAll(".class", "");

            try {
                Class clazz = classLoader.loadClass(realClassName);

                if (AssignmentEndpoint.class.isAssignableFrom(clazz)) {
                    this.lessonEndpoints.add(clazz);
                }
            } catch (ClassNotFoundException ce) {
                throw new PluginLoadingFailure("Class " + realClassName + " listed in jar but unable to load the class.", ce);
            }
        }
    }

    /**
     * <p>loadFiles.</p>
     *
     * @param file a {@link java.nio.file.Path} object.
     */
    public void loadFiles(Path file) {
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

    /**
     * Lesson is optional, it is also possible that the supplied jar contains only helper classes.
     * Lesson could be a new lesson (adoc based) or still ECS based.
     *
     * @return a {@link com.google.common.base.Optional} object.
     */
    public Optional<AbstractLesson> getLesson() {
        try {
            if (newLesson != null) {
                return Optional.of(newLesson.newInstance());
            }
        } catch (IllegalAccessException | InstantiationException e) {
            throw new PluginLoadingFailure("Unable to instantiate the lesson " + newLesson.getName(), e);
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
