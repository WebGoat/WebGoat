package org.owasp.webgoat.lessons;

import org.owasp.webgoat.session.LessonTracker;
import org.owasp.webgoat.session.RandomLessonTracker;
import org.owasp.webgoat.session.WebSession;

public abstract class RandomLessonAdapter extends LessonAdapter {

	public abstract String[] getStages();
	
	public void setStage(WebSession s, String stage) {
		getLessonTracker(s).setStage(stage);
	}
	
	public String getStage(WebSession s) {
		return getLessonTracker(s).getStage();
	}
	
	public void setStageComplete(WebSession s, String stage) {
		RandomLessonTracker lt = getLessonTracker(s);
		lt.setStageComplete(stage, true);
		if (lt.getCompleted()) {
			s.setMessage("Congratulations, you have completed this lab");
		} else {
			String message = "You have completed " + stage + ".";
			if (! stage.equals(lt.getStage()))
				message = message + " Welcome to " + lt.getStage();
			s.setMessage(message);
		}
	}
	
	@Override
    public RandomLessonTracker getLessonTracker(WebSession s) {
    	return (RandomLessonTracker) super.getLessonTracker(s);
    }


	@Override
	public RandomLessonTracker getLessonTracker(WebSession s, AbstractLesson lesson) {
		return (RandomLessonTracker) super.getLessonTracker(s, lesson);
	}


	@Override
	public RandomLessonTracker getLessonTracker(WebSession s, String userNameOverride) {
		return (RandomLessonTracker) super.getLessonTracker(s, userNameOverride);
	}

	@Override
	public LessonTracker createLessonTracker() {
		return new RandomLessonTracker(getStages());
	}

}
