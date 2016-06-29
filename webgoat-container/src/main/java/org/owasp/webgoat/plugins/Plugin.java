package org.owasp.webgoat.plugins;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.owasp.webgoat.lessons.AbstractLesson;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
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
    private Class<AbstractLesson> lesson;
    private YmlBasedLesson ymlBasedLesson;
    private Map<String, File> solutionLanguageFiles = new HashMap<>();
    private Map<String, File> lessonPlansLanguageFiles = new HashMap<>();
    private List<File> pluginFiles = Lists.newArrayList();
    private File lessonSourceFile;

    public Plugin(PluginClassLoader classLoader) {
        this.classLoader = classLoader;
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
        //Old code remove after we migrated the lessons
        String realClassName = StringUtils.trimLeadingCharacter(name, '/').replaceAll("/", ".").replaceAll(".class", "");

        try {
            Class clazz = classLoader.loadClass(realClassName);

            if (AbstractLesson.class.isAssignableFrom(clazz)) {
                this.lesson = clazz;
            }
        } catch (ClassNotFoundException ce) {
            throw new PluginLoadingFailure("Class " + realClassName + " listed in jar but unable to load the class.", ce);
        }

        //New code all lessons should work as below
        readYmlLessonConfiguration();
    }

    private void readYmlLessonConfiguration() {
        java.util.Optional<File> ymlFile = this.pluginFiles.stream().filter(f -> f.getName().endsWith(".yml")).findFirst();
        if (ymlFile.isPresent()) {
            try {
                String ymlStr = FileUtils.readFileToString(ymlFile.get());
                ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
                Map<String, Object> ymlAsMap = mapper.readValue(ymlStr, new TypeReference<Map<String, Object>>() {
                });
                Map<String, Object> lessonYml = (Map<String, Object>) ymlAsMap.get("lesson");
                final String category = (String) lessonYml.get("category");
                final List<String> hints = (List<String>) lessonYml.get("hints");
                final String title = (String) lessonYml.get("title");
                final String html = (String) lessonYml.get("html");
                this.ymlBasedLesson = new YmlBasedLesson(category, hints, title, html);
                this.lesson = null;
            } catch (IOException e) {
                throw new PluginLoadingFailure("Unable to read yml file", e);
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

        if (fileEndsWith(file, ".css", ".jsp", ".js", ".yml")) {
            pluginFiles.add(file.toFile());
        }
    }

    /**
     * <p>rewritePaths.</p>
     *
     * @param pluginTarget a {@link java.nio.file.Path} object.
     */
    public void rewritePaths(Path pluginTarget) {
//        try {
//            replaceInFiles(this.lesson.getSimpleName() + "_files",
//                    "plugin_lessons/plugin/" + this.lesson
//                            .getSimpleName() + "/lessonSolutions/en/" + this.lesson.getSimpleName() + "_files",
//                    solutionLanguageFiles.values());
//            replaceInFiles(this.lesson.getSimpleName() + "_files",
//                    "plugin_lessons/plugin/" + this.lesson
//                            .getSimpleName() + "/lessonPlans/en/" + this.lesson.getSimpleName() + "_files",
//                    lessonPlansLanguageFiles.values());
//
//            String[] replacements = {"jsp", "js"};
//            for (String replacement : replacements) {
//                String s = String.format("plugin/%s/%s/", this.lesson.getSimpleName(), replacement);
//                String r = String.format("plugin_lessons/plugin/s/%s/", this.lesson.getSimpleName(), replacement);
//                replaceInFiles(s, r, pluginFiles);
//                replaceInFiles(s, r, Arrays.asList(lessonSourceFile));
//            }
//
//            //CSS with url('/plugin/images') should not begin with / otherwise image cannot be found
//            String s = String.format("/plugin/%s/images/", this.lesson.getSimpleName());
//            String r = String
//                    .format("plugin_lessons/plugin/%s/images/", this.lesson.getSimpleName());
//            replaceInFiles(s, r, pluginFiles);
//            replaceInFiles(s, r, Arrays.asList(lessonSourceFile));
//        } catch (IOException e) {
//            throw new PluginLoadingFailure("Unable to rewrite the paths in the solutions", e);
//        }
    }

    /**
     * Lesson is optional, it is also possible that the supplied jar contains only helper classes.
     *
     * @return a {@link com.google.common.base.Optional} object.
     */
    public Optional<AbstractLesson> getLesson() {
        try {
            if (ymlBasedLesson != null) {
                return Optional.of(ymlBasedLesson);
            }
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
