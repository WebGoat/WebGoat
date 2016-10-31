
package org.owasp.webgoat.session;

import org.owasp.webgoat.lessons.AbstractLesson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


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
@Component
public class UserTracker {

    private static Map<String, HashMap<String, LessonTracker>> storage = new HashMap<>();
    private final String webgoatHome;
    private final WebSession webSession;

    public UserTracker(@Value("${webgoat.user.directory}") final String webgoatHome, final WebSession webSession) {
        this.webgoatHome = webgoatHome;
        this.webSession = webSession;
    }

    /**
     * <p>getCurrentLessonTracker.</p>
     *
     * @return a {@link org.owasp.webgoat.session.LessonTracker} object.
     */
    public LessonTracker getCurrentLessonTracker() {
        String lessonTitle = webSession.getCurrentLesson().getTitle();
        String username = webSession.getUserName();
        HashMap<String, LessonTracker> usermap = getUserMap(username);
        LessonTracker tracker = usermap.get(lessonTitle);
        if (tracker == null) {
            // Creates a new lesson tracker, if one does not exist on disk.
            tracker = LessonTracker.load(webSession, username, webSession.getCurrentLesson());
            usermap.put(lessonTitle, tracker);
        }
        return tracker;
    }

    /**
     * Returns the lesson tracker for a specific lesson if available.
     *
     * @param lesson the lesson
     * @return the optional lesson tracker
     */
    public Optional<LessonTracker> getLessonTracker(AbstractLesson lesson) {
        String username = webSession.getUserName();
        return Optional.ofNullable(getUserMap(username).getOrDefault(lesson.getTitle(), null));
    }


    /**
     * Gets the userMap attribute of the UserTracker object
     *
     * @param userName Description of the Parameter
     * @return The userMap value
     */
    private HashMap<String, LessonTracker> getUserMap(String userName) {

        HashMap<String, LessonTracker> usermap = storage.get(userName);

        if (usermap == null) {

            usermap = new HashMap<>();

            storage.put(userName, usermap);

        }

        return (usermap);
    }

}
