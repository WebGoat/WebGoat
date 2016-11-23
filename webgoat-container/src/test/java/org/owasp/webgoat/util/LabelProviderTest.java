package org.owasp.webgoat.util;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.owasp.webgoat.i18n.LabelProvider;

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
    public void shouldFallBackToEnglishIfLanguageNotSupported() {
        LabelProvider labelProvider = new LabelProvider();
        assertThat(labelProvider.get(Locale.CHINESE, "LessonCompleted"), CoreMatchers.equalTo(
                "Congratulations. You have successfully completed this lesson."));
    }

    @Test
    public void shouldUseProvidedLanguageIfSupported() {
        LabelProvider labelProvider = new LabelProvider();
        assertThat(labelProvider.get(Locale.GERMAN, "RestartLesson"), CoreMatchers.equalTo(
                "Lektion neu beginnen"));
    }

}