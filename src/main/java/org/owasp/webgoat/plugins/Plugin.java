package org.owasp.webgoat.plugins;

import org.owasp.webgoat.lessons.AbstractLesson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Plugin {

    private static final Logger logger = LoggerFactory.getLogger(Plugin.class);
    private final Class<AbstractLesson> lesson;
    private final Path pluginDirectory;

    public static class PluginLoadingFailure extends RuntimeException {

        public PluginLoadingFailure(String message) {
            super(message);
        }
    }

    public static class Builder {

        private Path pluginDirectory;
        private Class lesson;
        private final List<String> loadedClasses = new ArrayList<String>();

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
                String realClassName = name.replace("/lesson_plans/", "").replaceAll("/", ".").replaceAll(".class", "");
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
            //Find necessary files flag if something went wrong plugin should complain
            return this;
        }

        public Plugin build() {
            if ( lesson == null ) {
                throw new PluginLoadingFailure(String.format("Lesson class not found, following classes were detected in the plugin: %s",
                    StringUtils.collectionToCommaDelimitedString(loadedClasses)));
            }
            return new Plugin(this.lesson, pluginDirectory);
        }

    }

    public Plugin(Class<AbstractLesson> lesson, Path pluginDirectory) {
        this.lesson = lesson;
        this.pluginDirectory = pluginDirectory;
    }

    public String getLessonPlanHtml() {
        Path lesson_plans = this.pluginDirectory.resolve("lesson_plans");
        try {
            Files.readAllLines(lesson_plans.resolve(this.lesson.getSimpleName() + ".html"), Charset.defaultCharset());
        } catch (IOException e) {
            logger.error("No html found in directory {}", lesson_plans.toString());
        }
        return "";
    }

    public Class<AbstractLesson> getLesson() {
        return lesson;
    }

    public String getLessonSolutionHtml() {
        return null;
        //return lessonSolutionHtml;
    }
}
