package org.owasp.webgoat.lessons.model;

import org.owasp.webgoat.lessons.AbstractLesson;
import org.owasp.webgoat.lessons.Category;
import org.owasp.webgoat.session.WebSession;

/**
 * <p>LessonInfoModel class.</p>
 *
 * @author dm
 * @version $Id: $Id
 */
public class LessonInfoModel {

    private String lessonTitle;
    private int numberHints;
    private boolean hasSource;
    private boolean hasSolution;
    private boolean hasPlan;
    private String submitMethod;

    /**
     * <p>Constructor for LessonInfoModel.</p>
     *
     * @param webSession a {@link org.owasp.webgoat.session.WebSession} object.
     */
    public LessonInfoModel(WebSession webSession) {
        AbstractLesson lesson = webSession.getCurrentLesson();
        //TODO make these first class citizens of the lesson itself; and stop passing the session all over ... and generally tighten the checks up
        this.hasSource = !lesson.getSource(webSession).contains("Could not find the source file or source file does not exist");
        this.hasPlan = !lesson.getSource(webSession).contains("Could not find lesson plan");
        this.hasSolution = !lesson.getSolution(webSession).contains("Could not find the solution file or solution file does not exist");
        this.lessonTitle = lesson.getTitle();
        this.numberHints = lesson.getHintCount(webSession);
        this.submitMethod = lesson.getSubmitMethod();

        if ( this.numberHints < 1 || lesson.getHint(webSession,0).equals("Hint: There are no hints defined.")) {
            this.numberHints = 0;
        }
        //special challenge case
        if (lesson.getCategory().equals(Category.CHALLENGE)) {
            this.numberHints = (lesson.isAuthorized(webSession, AbstractLesson.CHALLENGE_ROLE, WebSession.SHOWHINTS)) ? lesson.getHintCount(webSession) : 0;
            this.hasSource = (lesson.isAuthorized(webSession, AbstractLesson.CHALLENGE_ROLE, WebSession.SHOWHINTS));
            this.hasSolution = (lesson.isAuthorized(webSession, AbstractLesson.CHALLENGE_ROLE, WebSession.SHOWHINTS)); //assuming we want this to fall in line with source and solution
            this.hasPlan = (lesson.isAuthorized(webSession, AbstractLesson.CHALLENGE_ROLE, WebSession.SHOWHINTS)); //assuming we want this to fall in line with source and solutionn
        }
    }

    // GETTERS
    /**
     * <p>Getter for the field <code>lessonTitle</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getLessonTitle() {
        return lessonTitle;
    }

    /**
     * <p>Getter for the field <code>numberHints</code>.</p>
     *
     * @return a int.
     */
    public int getNumberHints() {
        return numberHints;
    }

    /**
     * <p>isHasSource.</p>
     *
     * @return a boolean.
     */
    public boolean isHasSource() {
        return hasSource;
    }

    /**
     * <p>isHasSolution.</p>
     *
     * @return a boolean.
     */
    public boolean isHasSolution() {
        return hasSolution;
    }

    /**
     * <p>isHasPlan.</p>
     *
     * @return a boolean.
     */
    public boolean isHasPlan() {
        return hasPlan;
    }

    /**
     * <p>Getter for the field <code>submitMethod</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getSubmitMethod() {
        return submitMethod;
    }

}
