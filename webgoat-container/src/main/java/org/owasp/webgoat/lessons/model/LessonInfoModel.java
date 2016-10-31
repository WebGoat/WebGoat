package org.owasp.webgoat.lessons.model;

import org.owasp.webgoat.lessons.AbstractLesson;
import org.owasp.webgoat.session.WebSession;

/**
 * <p>LessonInfoModel class.</p>
 *
 * @author dm
 * @version $Id: $Id
 */
//// TODO: 11/5/2016 this can be removed???
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
        this.hasSource = false;
        this.hasPlan = false;
        this.hasSolution = false;
        this.lessonTitle = lesson.getTitle();
        this.numberHints = lesson.getHintCount();
        this.submitMethod = lesson.getSubmitMethod();
    }
}
