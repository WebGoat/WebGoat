package org.owasp.webgoat.plugins;

import org.owasp.webgoat.lessons.AbstractLesson;

import java.nio.file.Path;

public class Plugin {

    private final Class<AbstractLesson> lesson;
    private final String lessonPlanHtml;
    private final String lessonSolutionHtml;

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
            return new Plugin(this.lesson, null, null);
        }
    }

    public Plugin(Class<AbstractLesson> lesson, String lessonPlanHtml, String lessonSolutionHtml) {
        this.lesson = lesson;
        this.lessonPlanHtml = lessonPlanHtml;
        this.lessonSolutionHtml = lessonSolutionHtml;
    }


    public String getLessonPlanHtml() {
        return lessonPlanHtml;
    }

    public Class<AbstractLesson> getLesson() {
        return lesson;
    }

    public String getLessonSolutionHtml() {
        return lessonSolutionHtml;
    }
}
