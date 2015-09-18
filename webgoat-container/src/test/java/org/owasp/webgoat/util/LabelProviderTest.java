package org.owasp.webgoat.util;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.Locale;

import static org.junit.Assert.assertThat;

public class LabelProviderTest  {

    @Test
    public void defaultLabelsShouldBePresent() {
        LabelProvider labelProvider = new LabelProvider();
        assertThat(labelProvider.get(Locale.ENGLISH, "LessonCompleted"), CoreMatchers.equalTo(
                "Congratulations. You have successfully completed this lesson."));
    }

    @Test
    public void loadingPluginLabels() throws IOException {
        LabelProvider labelProvider = new LabelProvider();
        labelProvider.updatePluginResources(new ClassPathResource("log4j.properties").getFile().toPath());
        assertThat(labelProvider.get(Locale.ENGLISH, "LessonCompleted"), CoreMatchers.equalTo(
                "Congratulations. You have successfully completed this lesson."));
        assertThat(labelProvider.get(Locale.ENGLISH, "log4j.appender.CONSOLE.Target"), CoreMatchers.equalTo(
                "System.out"));
    }


}