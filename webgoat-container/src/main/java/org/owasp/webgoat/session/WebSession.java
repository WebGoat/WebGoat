package org.owasp.webgoat.session;

import org.owasp.webgoat.lessons.Lesson;
import org.owasp.webgoat.users.WebGoatUser;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.Serializable;

/**
 * *************************************************************************************************
 * <p>
 * <p>
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details, please see
 * http://www.owasp.org/
 * <p>
 * Copyright (c) 2002 - 2014 Bruce Mayhew
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
public class WebSession implements Serializable {

    private static final long serialVersionUID = -4270066103101711560L;
    private final WebGoatUser currentUser;
    private transient Lesson currentLesson;
    private boolean securityEnabled;

    public WebSession() {
        this.currentUser = (WebGoatUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * <p> Setter for the field <code>currentScreen</code>. </p>
     *
     * @param lesson current lesson
     */
    public void setCurrentLesson(Lesson lesson) {
        this.currentLesson = lesson;
    }

    /**
     * <p> getCurrentLesson. </p>
     *
     * @return a {@link Lesson} object.
     */
    public Lesson getCurrentLesson() {
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

    public void toggleSecurity() {
        this.securityEnabled = !this.securityEnabled;
    }

    public boolean isSecurityEnabled() {
        return securityEnabled;
    }
}
