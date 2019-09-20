/*
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details, please see http://www.owasp.org/
 *
 * Copyright (c) 2002 - 2019 Bruce Mayhew
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
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software projects.
 */

package org.owasp.webgoat.lessons;

import lombok.Getter;
import lombok.Setter;
import lombok.Singular;

import java.util.List;

@Getter
@Setter
public abstract class Lesson {

    private static int count = 1;
    private Integer id = null;
    private List<Assignment> assignments;

    /**
     * Constructor for the Lesson object
     */
    public Lesson() {
        id = ++count;
    }


    /**
     * <p>getName.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getName() {
        String className = getClass().getName();
        return className.substring(className.lastIndexOf('.') + 1);
    }

    /**
     * Gets the category attribute of the Lesson object
     *
     * @return The category value
     */
    public Category getCategory() {
        return getDefaultCategory();
    }

    /**
     * <p>getDefaultCategory.</p>
     *
     * @return a {@link org.owasp.webgoat.lessons.Category} object.
     */
    protected abstract Category getDefaultCategory();

    /**
     * Gets the title attribute of the HelloScreen object
     *
     * @return The title value
     */
    public abstract String getTitle();

    /**
     * <p>Returns the default "path" portion of a lesson's URL.</p>
     * <p>
     * <p>
     * Legacy webgoat lesson links are of the form
     * "attack?Screen=Xmenu=Ystage=Z". This method returns the path portion of
     * the url, i.e., "attack" in the string above.
     * <p>
     * Newer, Spring-Controller-based classes will override this method to
     * return "*.do"-styled paths.
     *
     * @return a {@link java.lang.String} object.
     */
    protected String getPath() {
        return "#lesson/";
    }

    /**
     * Get the link that can be used to request this screen.
     * <p>
     * Rendering the link in the browser may result in Javascript sending
     * additional requests to perform necessary actions or to obtain data
     * relevant to the lesson or the element of the lesson selected by the
     * user.  Thanks to using the hash mark "#" and Javascript handling the
     * clicks, the user will experience less waiting as the pages do not have
     * to reload entirely.
     *
     * @return a {@link java.lang.String} object.
     */
    public String getLink() {
        return String.format("%s%s.lesson", getPath(), getId());
    }

    /**
     * Description of the Method
     *
     * @return Description of the Return Value
     */
    public String toString() {
        return getTitle();
    }

    public abstract String getId();
}
