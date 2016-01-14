package org.owasp.webgoat.lessons;

import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.owasp.webgoat.session.WebSession;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class AbstractLessonTest  {

    private AbstractLesson lesson = new AbstractLesson() {
        protected Element createContent(WebSession s) {
            return new ElementContainer();
        }
        public Category getCategory() {
            return Category.XSS;
        }
        protected Integer getDefaultRanking() { 
            return new Integer(5);
        }
        protected Category getDefaultCategory() {
            return Category.INTRODUCTION;
        }
        protected boolean getDefaultHidden() {
            return false;
        }
        protected List<String> getHints(WebSession s) {
            return Arrays.<String>asList();
        }
        public String getInstructions(WebSession s) {
            return "Instructions";
        }
        public String getTitle() {
            return "title";
        }
        public String getCurrentAction(WebSession s) {
            return "an action";
        }
        public String getSubmitMethod() { return "GET";}
        public void restartLesson() {
        }
        public void setCurrentAction(WebSession s, String lessonScreen) {
        }
    };

    @Test
    public void testLinks() {
        String mvcLink = lesson.getLink();
        assertThat(mvcLink, CoreMatchers.startsWith("#attack/"));
        assertThat(mvcLink, CoreMatchers.endsWith("/900"));

        String srvLink = lesson.getServletLink();
        assertThat(srvLink, CoreMatchers.startsWith("attack?Screen="));
        assertThat(srvLink, CoreMatchers.endsWith("&menu=900"));
        assertEquals(lesson.getSubmitMethod(),"GET");
    }
}


