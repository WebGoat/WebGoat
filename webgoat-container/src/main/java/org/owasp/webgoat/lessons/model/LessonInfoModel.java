package org.owasp.webgoat.lessons.model;

import org.owasp.webgoat.lessons.AbstractLesson;
import org.owasp.webgoat.lessons.Category;
import org.owasp.webgoat.session.WebSession;

/**
 * Created by jason on 9/18/15.
 */
public class LessonInfoModel {

    private String lessonTitle;
    private int numberHints;
    private boolean hasSource;
    private boolean hasSolution;
    private boolean hasPlan;
    private String source;
    private String solution;
    private String plan;

    public LessonInfoModel(WebSession webSession) {
        AbstractLesson lesson = webSession.getCurrentLesson();
        //TODO make these first class citizens of the lesson itself; and stop passing the session all over ... and generally tighten the checks up
        this.hasSource = !lesson.getSource(webSession).contains("Could not find the source file or source file does not exist");
        this.hasPlan = !lesson.getSource(webSession).contains("Could not find lesson plan");
        this.hasSolution = !lesson.getSolution(webSession).contains("Could not find the solution file or solution file does not exist");
        this.lessonTitle = lesson.getTitle();
        this.numberHints = lesson.getHintCount(webSession);

        if ( this.numberHints < 1 || lesson.getHint(webSession,0).equals("Hint: There are no hints defined.")) {
            this.numberHints = 0;
        }
        //special challenge case
        if (lesson.getCategory().equals(Category.CHALLENGE)) {
            this.numberHints = (lesson.isAuthorized(webSession, AbstractLesson.CHALLENGE_ROLE, WebSession.SHOWHINTS)) ? lesson.getHintCount(webSession) : 0;
            this.hasSource = (lesson.isAuthorized(webSession, AbstractLesson.CHALLENGE_ROLE, WebSession.SHOWHINTS));
            this.hasSolution = (lesson.isAuthorized(webSession, AbstractLesson.CHALLENGE_ROLE, WebSession.SHOWHINTS)); //assuming we want this as well
        }
    }

    // GETTERS
    public String getLessonTitle() {
        return lessonTitle;
    }

    public int getNumberHints() {
        return numberHints;
    }

    public boolean isHasSource() {
        return hasSource;
    }

    public boolean isHasSolution() {
        return hasSolution;
    }

    public boolean isHasPlan() {
        return hasPlan;
    }

    public String getSource() {
        return source;
    }

    public String getSolution() {
        return solution;
    }

    public String getPlan() {
        return plan;
    }

}
