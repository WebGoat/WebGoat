package org.owasp.webgoat.container.lessons;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class LessonScanner {

    @Getter
    private List<String> lessons = new ArrayList<>();

    public LessonScanner(ResourcePatternResolver resourcePatternResolver) {
        try {
            var resources = resourcePatternResolver.getResources("classpath:/lessons/*");
            for (var resource : resources) {
                lessons.add(resource.getFilename());
            }
        } catch (IOException e) {
            log.warn("No lessons found...");
        }
    }

    public List<String> applyPattern(String pattern) {
        return lessons.stream().map(lesson -> String.format(pattern, lesson)).toList();
    }
}
