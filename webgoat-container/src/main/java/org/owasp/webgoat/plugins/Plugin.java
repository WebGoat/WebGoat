package org.owasp.webgoat.plugins;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import lombok.Getter;
import org.owasp.webgoat.endpoints.AssignmentEndpoint;
import org.owasp.webgoat.endpoints.Endpoint;
import org.owasp.webgoat.lessons.AbstractLesson;
import org.owasp.webgoat.lessons.Assignment;
import org.owasp.webgoat.lessons.NewLesson;
import org.springframework.util.StringUtils;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.owasp.webgoat.plugins.PluginFileUtils.fileEndsWith;

/**
 * <p>Plugin class.</p>
 *
 * @author dm
 * @version $Id: $Id
 */
public class Plugin {

    @Getter
    private final String originationJar;
    private PluginClassLoader classLoader;
    private Class<NewLesson> newLesson;
    @Getter
    private List<Class<AssignmentEndpoint>> assignments = Lists.newArrayList();
    @Getter
    private List<Class<Endpoint>> endpoints = Lists.newArrayList();
    private List<File> pluginFiles = Lists.newArrayList();

    public Plugin(PluginClassLoader classLoader, String originatingJar) {
        this.classLoader = classLoader;
        this.originationJar = originatingJar;
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
                    this.assignments.add(clazz);
                } else
                    if (Endpoint.class.isAssignableFrom(clazz)) {
                        this.endpoints.add(clazz);
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
        if (fileEndsWith(file, ".css", ".jsp", ".js")) {
            pluginFiles.add(file.toFile());
        }
    }

    /**
     * Lesson is optional, it is also possible that the supplied jar contains only helper classes.
     *
     * @return a {@link com.google.common.base.Optional} object.
     */
    public Optional<AbstractLesson> getLesson() {
        try {
            if (newLesson != null) {
                AbstractLesson lesson = newLesson.newInstance();
                lesson.setAssignments(createAssignment(assignments));
                return Optional.of(lesson);
            }
        } catch (IllegalAccessException | InstantiationException e) {
            throw new PluginLoadingFailure("Unable to instantiate the lesson " + newLesson.getName(), e);
        }
        return Optional.absent();
    }


    private List<Assignment> createAssignment(List<Class<AssignmentEndpoint>> endpoints) {
        return endpoints.stream().map(e -> new Assignment(e.getSimpleName(), getPath(e))).collect(toList());
    }

    private String getPath(Class<AssignmentEndpoint> e) {
        return e.getAnnotationsByType(javax.ws.rs.Path.class)[0].value();
    }


}
