package org.owasp.webgoat.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * *************************************************************************************************
 *
 *
 * This file is part of WebGoat, an Open Web Application Security Project
 * utility. For details, please see http://www.owasp.org/
 *
 * Copyright (c) 2002 - 20014 Bruce Mayhew
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * Getting Source ==============
 *
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository
 * for free software projects.
 *
 * @version $Id: $Id
 * @author dm
 */
@Component
public class WebgoatProperties {

    @Autowired
    private Environment env;

    /**
     *
     */
    private static final long serialVersionUID = 4351681705558227918L;
    final Logger logger = LoggerFactory.getLogger(WebgoatProperties.class);


    /**
     * <p>getIntProperty.</p>
     *
     * @param key a {@link java.lang.String} object.
     * @param defaultValue a int.
     * @return a int.
     */
    public int getIntProperty(String key, int defaultValue) {
        int value = defaultValue;

        String s = env.getProperty(key);
        if (s != null) {
            value = Integer.parseInt(s);
        }

        return value;
    }

    /**
     * <p>getBooleanProperty.</p>
     *
     * @param key a {@link java.lang.String} object.
     * @param defaultValue a boolean.
     * @return a boolean.
     */
    public boolean getBooleanProperty(String key, boolean defaultValue) {
        boolean value = defaultValue;
        key = this.trimLesson(key);

        String s = env.getProperty(key);
        if (s != null) {
            if (s.equalsIgnoreCase("true")) {
                value = true;
            } else if (s.equalsIgnoreCase("yes")) {
                value = true;
            } else if (s.equalsIgnoreCase("on")) {
                value = true;
            } else if (s.equalsIgnoreCase("false")) {
                value = false;
            } else if (s.equalsIgnoreCase("no")) {
                value = false;
            } else if (s.equalsIgnoreCase("off")) {
                value = false;
            }
        }

        return value;
    }

    private String trimLesson(String lesson) {
        String result = "";

        if (lesson.startsWith("org.owasp.webgoat.lessons.")) {
            result = lesson.substring("org.owasp.webgoat.lessons.".length(), lesson.length());
        } else {
            result = lesson;
        }

        return result;
    }
}
