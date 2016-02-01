
package org.owasp.webgoat.lessons.admin;

import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.BR;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TH;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.owasp.webgoat.lessons.AbstractLesson;
import org.owasp.webgoat.lessons.Category;
import org.owasp.webgoat.lessons.LessonAdapter;
import org.owasp.webgoat.session.WebSession;

import java.net.URL;

import static org.springframework.util.StringUtils.getFilename;
import static org.springframework.util.StringUtils.stripFilenameExtension;


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
public class MenuToLessonMapperScreen extends LessonAdapter
{

    /**
     * {@inheritDoc}
     *
     * Description of the Method
     */
    protected Element createContent(WebSession s)
    {
        ElementContainer ec = new ElementContainer();
        ec.addElement(new StringElement("This page describes an overview of all the lessons and maps the lesson to the WebGoat-Lessons project"));
        ec.addElement(new BR());
        ec.addElement(new BR());
        ec.addElement(makeMenuToLessonMapping(s));

        return ec;
    }

    /**
     * Gets the category attribute of the UserAdminScreen object
     *
     * @return The category value
     */
    protected Category getDefaultCategory()
    {
        return Category.ADMIN_FUNCTIONS;
    }

    private final static Integer DEFAULT_RANKING = new Integer(1000);

    /**
     * <p>getDefaultRanking.</p>
     *
     * @return a {@link java.lang.Integer} object.
     */
    protected Integer getDefaultRanking()
    {
        return DEFAULT_RANKING;
    }

    /**
     * Gets the role attribute of the UserAdminScreen object
     *
     * @return The role value
     */
    public String getRole()
    {
        return ADMIN_ROLE;
    }

    /**
     * Gets the title attribute of the UserAdminScreen object
     *
     * @return The title value
     */
    public String getTitle()
    {
        return ("Lesson information");
    }

    /**
     * Description of the Method
     *
     * @param s
     *            Description of the Parameter
     * @return Description of the Return Value
     */
    public Element makeMenuToLessonMapping(WebSession s)
    {
        ElementContainer ec = new ElementContainer();
        Table t = new Table().setCellSpacing(0).setCellPadding(2).setBorder(1);
        t.addElement(makeHeaderRow());

        for (AbstractLesson lesson : s.getCourse().getLessons(s, AbstractLesson.USER_ROLE)) {
            TR tr = new TR();
            tr.addElement(new TD().addElement(lesson.getName()));

            URL jarLocation = lesson.getClass().getProtectionDomain().getCodeSource().getLocation();
            String projectName = removeVersion(stripFilenameExtension(getFilename(jarLocation.getFile())));
            tr.addElement(new TD().addElement(projectName));

            tr.addElement(new TD().addElement(lesson.getClass().getName() + ".java"));
            t.addElement(tr);
        }
        ec.addElement(t);
        return (ec);
    }

    //Remove version number and last '-'
    private static String removeVersion(String s) {
        return s.replaceAll("[^a-z\\-]", "").replaceAll("-$", "");
    }

    /**
     * Description of the Method
     *
     * @return Description of the Return Value
     */
    private TR makeHeaderRow()
    {
        TR tr = new TR();

        tr.addElement(new TH("Lesson menu item"));
        tr.addElement(new TH("Lesson project"));
        tr.addElement(new TH("Lesson source class"));

        return tr;
    }
}
