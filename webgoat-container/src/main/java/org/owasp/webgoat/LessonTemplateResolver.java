package org.owasp.webgoat;

import com.google.common.collect.Sets;
import com.google.common.io.Files;
import org.thymeleaf.TemplateProcessingParameters;
import org.thymeleaf.resourceresolver.IResourceResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 */
public class LessonTemplateResolver extends TemplateResolver {


    private final static String PREFIX = "lesson:";
    private final File pluginTargetDirectory;

    public LessonTemplateResolver(File pluginTargetDirectory) {
        this.pluginTargetDirectory = pluginTargetDirectory;
        setResourceResolver(new LessonResourceResolver());
        setResolvablePatterns(Sets.newHashSet(PREFIX + "*"));
    }

    @Override
    protected String computeResourceName(TemplateProcessingParameters params) {
        String templateName = params.getTemplateName();
        return templateName.substring(PREFIX.length());
    }

    private class LessonResourceResolver implements IResourceResolver {

        @Override
        public InputStream getResourceAsStream(TemplateProcessingParameters params, String resourceName) {
            File lesson = new File(pluginTargetDirectory, "/plugin/" + resourceName + "/html/" + resourceName + ".html");
            if (lesson != null) {
                try {
                    return new ByteArrayInputStream(Files.toByteArray(lesson));
                } catch (IOException e) {
                    //no html yet
                    return new ByteArrayInputStream(new byte[0]);
                }
            }
            return null;
        }

        @Override
        public String getName() {
            return "lessonResourceResolver";
        }
    }
}
