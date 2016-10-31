
package org.owasp.webgoat.session;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;


/**
 * ************************************************************************************************
 * <p>
 * <p>
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 * <p>
 * Copyright (c) 2002 - 20014 Bruce Mayhew
 * <p>
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 * <p>
 * Getting Source ==============
 * <p>
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software
 * projects.
 *
 * @author Bruce Mayhew <a href="http://code.google.com/p/webgoat">WebGoat</a>
 * @version $Id: $Id
 * @since October 29, 2003
 */
@Slf4j
public class LessonTracker {

    private boolean completed = false;

    private int maxHintLevel = 0;

    private int numVisits = 0;

    private boolean viewedCookies = false;

    private boolean viewedHtml = false;

    private boolean viewedLessonPlan = false;

    private boolean viewedParameters = false;

    private boolean viewedSource = false;

    private boolean viewedSolution = false;

    Properties lessonProperties = new Properties();

    private int totalNumberOfAssignments = 0;

    public void setTotalNumberOfAssignments(int totalNumberOfAssignments) {
        this.totalNumberOfAssignments = totalNumberOfAssignments;
    }

    /**
     * Gets the completed attribute of the LessonTracker object
     *
     * @return The completed value
     */
    public boolean getCompleted() {
        return completed;
    }

    /**
     * Gets the maxHintLevel attribute of the LessonTracker object
     *
     * @return The maxHintLevel value
     */
    public int getMaxHintLevel() {
        return maxHintLevel;
    }

    /**
     * Gets the numVisits attribute of the LessonTracker object
     *
     * @return The numVisits value
     */
    public int getNumVisits() {
        return numVisits;
    }

    /**
     * Gets the viewedCookies attribute of the LessonTracker object
     *
     * @return The viewedCookies value
     */
    public boolean getViewedCookies() {
        return viewedCookies;
    }

    /**
     * Gets the viewedHtml attribute of the LessonTracker object
     *
     * @return The viewedHtml value
     */
    public boolean getViewedHtml() {
        return viewedHtml;
    }

    /**
     * Gets the viewedLessonPlan attribute of the LessonTracker object
     *
     * @return The viewedLessonPlan value
     */
    public boolean getViewedLessonPlan() {
        return viewedLessonPlan;
    }

    /**
     * Gets the viewedParameters attribute of the LessonTracker object
     *
     * @return The viewedParameters value
     */
    public boolean getViewedParameters() {
        return viewedParameters;
    }

    /**
     * Gets the viewedSource attribute of the LessonTracker object
     *
     * @return The viewedSource value
     */
    public boolean getViewedSource() {
        return viewedSource;
    }

    /**
     * <p>Getter for the field <code>viewedSolution</code>.</p>
     *
     * @return a boolean.
     */
    public boolean getViewedSolution() {
        return viewedSource;
    }

    /**
     * Description of the Method
     */
    public void incrementNumVisits() {
        numVisits++;
    }


    /**
     * Sets the properties attribute of the LessonTracker object
     *
     * @param props  The new properties value
     * @param screen a {@link org.owasp.webgoat.session.Screen} object.
     */
    protected void setProperties(Properties props, Screen screen) {
        completed = Boolean.valueOf(props.getProperty(screen.getTitle() + ".completed")).booleanValue();
        maxHintLevel = Integer.parseInt(props.getProperty(screen.getTitle() + ".maxHintLevel", "0"));
        numVisits = Integer.parseInt(props.getProperty(screen.getTitle() + ".numVisits", "0"));
        viewedCookies = Boolean.valueOf(props.getProperty(screen.getTitle() + ".viewedCookies", "false")).booleanValue();
        viewedHtml = Boolean.valueOf(props.getProperty(screen.getTitle() + ".viewedHtml", "false")).booleanValue();
        viewedLessonPlan = Boolean.valueOf(props.getProperty(screen.getTitle() + ".viewedLessonPlan", "false")).booleanValue();
        viewedParameters = Boolean.valueOf(props.getProperty(screen.getTitle() + ".viewedParameters", "false")).booleanValue();
        viewedSource = Boolean.valueOf(props.getProperty(screen.getTitle() + ".viewedSource", "false")).booleanValue();
        totalNumberOfAssignments = Integer.parseInt(props.getProperty(screen.getTitle() + ".totalNumberOfAssignments", "0"));
    }

    /**
     * <p>getUserDir.</p>
     *
     * @param s a {@link org.owasp.webgoat.session.WebSession} object.
     * @return a {@link java.lang.String} object.
     */
    public static String getUserDir(WebSession s) {
        return "";
    }

    private static String getTrackerFile(WebSession s, String user, Screen screen) {
        return getUserDir(s) + user + "." + screen.getClass().getName() + ".props";
    }

    /**
     * Description of the Method
     *
     * @param screen Description of the Parameter
     * @param screen Description of the Parameter
     * @param screen Description of the Parameter
     * @param screen Description of the Parameter
     * @param screen Description of the Parameter
     * @param screen Description of the Parameter
     * @param s      Description of the Parameter
     * @param user   a {@link java.lang.String} object.
     * @return Description of the Return Value
     */
    public static LessonTracker load(WebSession s, String user, Screen screen) {
        FileInputStream in = null;
        try {
            String fileName = getTrackerFile(s, user, screen);
            if (fileName != null) {
                Properties tempProps = new Properties();
                // System.out.println("Loading lesson state from: " + fileName);
                in = new FileInputStream(fileName);
                tempProps.load(in);
                // allow the screen to use any custom properties it may have set
                LessonTracker tempLessonTracker = new LessonTracker();
                tempLessonTracker.setProperties(tempProps, screen);
                return tempLessonTracker;
            }
        } catch (FileNotFoundException e) {
            // Normal if the lesson has not been accessed yet.
        } catch (Exception e) {
            System.out.println("Failed to load lesson state for " + screen);
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (Exception e) {
            }
        }

        return new LessonTracker();
    }

    /**
     * Sets the completed attribute of the LessonTracker object
     *
     * @param completed The new completed value
     */
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    /**
     * Sets the maxHintLevel attribute of the LessonTracker object
     *
     * @param maxHintLevel The new maxHintLevel value
     */
    public void setMaxHintLevel(int maxHintLevel) {
        this.maxHintLevel = Math.max(this.maxHintLevel, maxHintLevel);
    }

    /**
     * Allows the storing of properties for the logged in and a screen.
     *
     * @param s      Description of the Parameter
     * @param screen a {@link org.owasp.webgoat.session.Screen} object.
     * @param screen a {@link org.owasp.webgoat.session.Screen} object.
     */
    public void store(WebSession s, Screen screen) {
        store(s, screen, s.getUserName());
    }

    /**
     * Allows the storing of properties for a user and a screen.
     *
     * @param s      Description of the Parameter
     * @param screen a {@link org.owasp.webgoat.session.Screen} object.
     * @param screen a {@link org.owasp.webgoat.session.Screen} object.
     * @param user   a {@link java.lang.String} object.
     */
    public void store(WebSession s, Screen screen, String user) {

        String fileName = getTrackerFile(s, user, screen);
        // System.out.println( "Storing data to" + fileName );
        lessonProperties.setProperty(screen.getTitle() + ".completed", Boolean.toString(completed));
        lessonProperties.setProperty(screen.getTitle() + ".maxHintLevel", Integer.toString(maxHintLevel));
        lessonProperties.setProperty(screen.getTitle() + ".numVisits", Integer.toString(numVisits));
        lessonProperties.setProperty(screen.getTitle() + ".viewedCookies", Boolean.toString(viewedCookies));
        lessonProperties.setProperty(screen.getTitle() + ".viewedHtml", Boolean.toString(viewedHtml));
        lessonProperties.setProperty(screen.getTitle() + ".viewedLessonPlan", Boolean.toString(viewedLessonPlan));
        lessonProperties.setProperty(screen.getTitle() + ".viewedParameters", Boolean.toString(viewedParameters));
        lessonProperties.setProperty(screen.getTitle() + ".viewedSource", Boolean.toString(viewedSource));
        lessonProperties.setProperty(screen.getTitle() + ".totalNumberOfAssignments", Integer.toString(totalNumberOfAssignments));
        try (FileOutputStream out = new FileOutputStream(fileName)) {
            lessonProperties.store(out, s.getUserName());
        } catch (IOException e) {
            log.warn("Warning User data for {} will not persist", s.getUserName());
        }
    }

    /**
     * Description of the Method
     *
     * @return Description of the Return Value
     */
    public String toString() {
        StringBuffer buff = new StringBuffer();
        buff.append("LessonTracker:" + "\n");
        buff.append("    - completed:................. " + completed + "\n");
        buff.append("    - maxHintLevel:.............. " + maxHintLevel + "\n");
        buff.append("    - numVisits:................. " + numVisits + "\n");
        buff.append("    - viewedCookies:............. " + viewedCookies + "\n");
        buff.append("    - viewedHtml:................ " + viewedHtml + "\n");
        buff.append("    - viewedLessonPlan:.......... " + viewedLessonPlan + "\n");
        buff.append("    - viewedParameters:.......... " + viewedParameters + "\n");
        buff.append("    - viewedSource:.............. " + viewedSource + "\n" + "\n");
        buff.append("    - totalNumberOfAssignments:.. " + viewedSource + "\n" + "\n");
        return buff.toString();
    }

    /**
     * <p>Getter for the field <code>lessonProperties</code>.</p>
     *
     * @return Returns the lessonProperties.
     */
    public Properties getLessonProperties() {
        return lessonProperties;
    }

    /**
     * <p>Setter for the field <code>lessonProperties</code>.</p>
     *
     * @param lessonProperties The lessonProperties to set.
     */
    public void setLessonProperties(Properties lessonProperties) {
        this.lessonProperties = lessonProperties;
    }
}
