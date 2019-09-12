package org.owasp.webgoat.plugins;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.lessons.NewLesson;

import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class PluginResource {

    private final URL location;
    private final List<Class> classes;

    public List<Class> getLessons() {
        return classes.stream().filter(c -> c.getSuperclass() == NewLesson.class).collect(Collectors.toList());
    }

    public List<Class<AssignmentEndpoint>> getAssignments(Class lesson) {
        return classes.stream().
                filter(c -> c.getSuperclass() == AssignmentEndpoint.class).
                filter(c -> c.getPackage().equals(lesson.getPackage())).
                map(c -> (Class<AssignmentEndpoint>) c).
                collect(Collectors.toList());
    }


}
