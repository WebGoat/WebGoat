/**
 *************************************************************************************************
 *
 *
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 *
 * Copyright (c) 2002 - 20014 Bruce Mayhew
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * Getting Source ==============
 *
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software
 * projects.
 *
 * @author Bruce Mayhew <a href="http://code.google.com/p/webgoat">WebGoat</a>
 * @since October 28, 2003
 * @version $Id: $Id
 */
package org.owasp.webgoat.lessons;

//// TODO: 11/8/2016 remove
public abstract class LessonAdapter extends AbstractLesson {


    /**
     * <p>getDefaultHidden.</p>
     *
     * @return a boolean.
     */
    protected boolean getDefaultHidden() {
        return false;
    }

    /**
     * Initiates lesson restart functionality. Lessons should override this for
     * lesson specific actions
     */
    public void restartLesson() {
        // Do Nothing - called when restart lesson is pressed. Each lesson can do something
    }
        
    private final static Integer DEFAULT_RANKING = new Integer(1000);

    /**
     * <p>getDefaultRanking.</p>
     *
     * @return a {@link java.lang.Integer} object.
     */
    protected Integer getDefaultRanking() {
        return DEFAULT_RANKING;
    }

    /**
     * provide a default submitMethod of lesson does not implement
     *
     * @return a {@link java.lang.String} object.
     */
    public String getSubmitMethod() {
        return "GET";
    }

    /**
     * Fill in a descriptive title for this lesson. The title of the lesson.
     * This will appear above the control area at the top of the page. This
     * field will be rendered as html.
     *
     * @return The title value
     */
    public String getTitle() {
        return "Untitled Lesson " + getScreenId();
    }


}
