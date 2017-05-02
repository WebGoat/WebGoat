package org.owasp.webgoat.session;

import lombok.extern.slf4j.Slf4j;
import org.owasp.webgoat.lessons.AbstractLesson;
import org.owasp.webgoat.users.WebGoatUser;
import org.springframework.security.core.context.SecurityContextHolder;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * *************************************************************************************************
 * <p>
 * <p>
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details, please see
 * http://www.owasp.org/
 * <p>
 * Copyright (c) 2002 - 20014 Bruce Mayhew
 * <p>
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Free
 * Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 * <p>
 * Getting Source ==============
 * <p>
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software
 * projects.
 *
 * @author Jeff Williams <a href="http://www.aspectsecurity.com">Aspect Security</a>
 * @author Bruce Mayhew <a href="http://code.google.com/p/webgoat">WebGoat</a>
 * @version $Id: $Id
 * @since October 28, 2003
 */
@Slf4j
public class WebSession {

    private final WebGoatUser currentUser;
    private final WebgoatContext webgoatContext;
    private AbstractLesson currentLesson;

    /**
     * Constructor for the WebSession object
     *
     * @param webgoatContext a {@link org.owasp.webgoat.session.WebgoatContext} object.
     */
    public WebSession(WebgoatContext webgoatContext) {
        this.webgoatContext = webgoatContext;
        this.currentUser = (WebGoatUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * <p> getConnection. </p>
     *
     * @param s a {@link org.owasp.webgoat.session.WebSession} object.
     * @return a {@link java.sql.Connection} object.
     * @throws java.sql.SQLException if any.
     */
    public static synchronized Connection getConnection(WebSession s) throws SQLException {
        return DatabaseUtilities.getConnection(s);
    }

    /**
     * <p> returnConnection. </p>
     *
     * @param s a {@link org.owasp.webgoat.session.WebSession} object.
     */
    public static void returnConnection(WebSession s) {
        DatabaseUtilities.returnConnection(s.getUserName());
    }

    /**
     * <p> Setter for the field <code>currentScreen</code>. </p>
     *
     * @param lesson current lesson
     */
    public void setCurrentLesson(AbstractLesson lesson) {
        this.currentLesson = lesson;
    }

    /**
     * <p> getCurrentLesson. </p>
     *
     * @return a {@link org.owasp.webgoat.lessons.AbstractLesson} object.
     */
    public AbstractLesson getCurrentLesson() {
        return this.currentLesson;
    }

    /**
     * Gets the userName attribute of the WebSession object
     *
     * @return The userName value
     */
    public String getUserName() {
        return currentUser.getUsername();
    }

    /**
     * <p> Getter for the field <code>webgoatContext</code>. </p>
     *
     * @return a {@link org.owasp.webgoat.session.WebgoatContext} object.
     */
    public WebgoatContext getWebgoatContext() {
        return webgoatContext;
    }
}
