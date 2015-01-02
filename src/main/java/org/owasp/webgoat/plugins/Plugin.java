package org.owasp.webgoat.plugins;

import org.owasp.webgoat.lessons.AbstractLesson;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;

public class Plugin {

    private final Class<AbstractLesson> lesson;
    private final Path pluginDirectory;

    public static class Builder {

        private Path pluginDirectory;
        private Class lesson;

        public Builder loadClass(String name, byte[] classFile) {
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            PluginClassLoader pluginClassLoader = new PluginClassLoader(contextClassLoader, classFile);
            try {
                String realClassName = name.replace("/lesson_plans/", "").replaceAll("/", ".").replaceAll(".class", "");
                Class clazz = pluginClassLoader.loadClass(realClassName);
                if (AbstractLesson.class.isAssignableFrom(clazz)) {
                    this.lesson = clazz;
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return this;
        }

        public Builder setBaseDirectory(Path pluginDirectory) {
            this.pluginDirectory = pluginDirectory;
            return this;
        }

        public Plugin build() {
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
            e.printStackTrace();
        }
        return lesson_plans.resolve(this.lesson.getSimpleName() + ".html").toFile().toString();
    }

    public Class<AbstractLesson> getLesson() {
        return lesson;
    }

    public String getLessonSolutionHtml() {
        return null;
        //return lessonSolutionHtml;
    }

    public static void main(String[] args) throws Exception {
        Path tempDir = Files.createTempDirectory("tempfiles");

        Path tempFile = Files.createTempFile(tempDir, "tempfiles", ".tmp");
        List<String> lines = Arrays.asList("Line1", "Line2");
        Files.write(tempFile, lines, Charset.defaultCharset(), StandardOpenOption.WRITE);

        System.out.printf("Wrote text to temporary file %s%n", tempFile.toString());
}
}
