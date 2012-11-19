package org.owasp.webgoat.lessons.model;

/**
 * Model component for the Http Basics lesson.  Using a model
 * for that simple lesson is architectural overkill.  We do it anyway
 * for illustrative purposes - to demonstrate the pattern that we will 
 * use for more complex lessons.
 * 
 */
public class HttpBasicsModel {

	private String personName;

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}
}
