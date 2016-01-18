
package org.owasp.webgoat.lessons.admin;

import java.util.Iterator;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.HtmlColor;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.Center;
import org.apache.ecs.html.H2;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TH;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.owasp.webgoat.lessons.AbstractLesson;
import org.owasp.webgoat.lessons.Category;
import org.owasp.webgoat.lessons.LessonAdapter;
import org.owasp.webgoat.session.LessonTracker;
import org.owasp.webgoat.session.Screen;
import org.owasp.webgoat.session.UserTracker;
import org.owasp.webgoat.session.WebSession;


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
public class ReportCardScreen extends LessonAdapter
{

    /**
     * Description of the Field
     */
    protected final static String USERNAME = "Username";

    /**
     * {@inheritDoc}
     *
     * Description of the Method
     */
    protected Element createContent(WebSession s)
    {
        ElementContainer ec = new ElementContainer();

        String user = null;

        try
        {
            if (s.getRequest().isUserInRole(WebSession.WEBGOAT_ADMIN))
            {
                user = s.getParser().getRawParameter(USERNAME);
            }
            else
            {
                user = s.getUserName();
            }
        } catch (Exception e)
        {
        }

        if (user == null)
        {
            user = s.getUserName();
        }

        ec.addElement(makeFeedback(s));
        ec.addElement(makeReportCard(s, user));

        return ec;
    }

    private Element makeFeedback(WebSession s)
    {
        ElementContainer ec = new ElementContainer();
        ec.addElement(new StringElement("Comments and suggestions are welcome. "
                + getWebgoatContext().getFeedbackAddressHTML() + "<br><br>"));

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
        return USER_ROLE;
    }

    /**
     * Gets the title attribute of the UserAdminScreen object
     *
     * @return The title value
     */
    public String getTitle()
    {
        return ("Report Card");
    }

    /**
     * Description of the Method
     * 
     * @param screen
     *            Description of the Parameter
     * @param s
     *            Description of the Parameter
     * @param user
     *            Description of the Parameter
     * @return Description of the Return Value
     */
    private TR makeLessonRow(WebSession s, String user, Screen screen)
    {
        LessonTracker lessonTracker = UserTracker.instance().getLessonTracker(s, user, screen);
        TR tr = new TR();
        if (lessonTracker.getCompleted())
        {
            tr.setBgColor(HtmlColor.LIGHTGREEN);
        }
        else if (lessonTracker.getNumVisits() == 0)
        {
            tr.setBgColor(HtmlColor.LIGHTBLUE);
        }
        else if (!lessonTracker.getCompleted() && lessonTracker.getNumVisits() > 10)
        {
            tr.setBgColor(HtmlColor.RED);
        }
        else
        {
            tr.setBgColor(HtmlColor.YELLOW);
        }
        tr.addElement(new TD().addElement(screen.getTitle()));
        tr.addElement(new TD().setAlign("CENTER").addElement(lessonTracker.getCompleted() ? "Y" : "N"));
        tr.addElement(new TD().setAlign("CENTER").addElement(Integer.toString(lessonTracker.getNumVisits())));
        tr.addElement(new TD().setAlign("CENTER").addElement(Integer.toString(lessonTracker.getMaxHintLevel())));
        return tr;
    }

    /**
     * {@inheritDoc}
     *
     * Description of the Method
     */
    protected Element makeMessages(WebSession s)
    {
        ElementContainer ec = new ElementContainer();

        return (ec);
    }

    /**
     * Description of the Method
     *
     * @param s
     *            Description of the Parameter
     * @param user
     *            Description of the Parameter
     * @return Description of the Return Value
     */
    public Element makeReportCard(WebSession s, String user)
    {
        ElementContainer ec = new ElementContainer();

        ec.addElement(makeUser(s, user));
        Table t = new Table().setCellSpacing(0).setCellPadding(2).setBorder(1);

        if (s.isColor())
        {
            t.setBorder(1);
        }
        TR tr = new TR();
        t.addElement(makeUserHeaderRow());

        // These are all the user lesson
        tr = new TR();
        tr.addElement(new TD().setAlign("CENTER").setColSpan(9).addElement("Normal user lessons"));
        t.addElement(tr);
        for (Iterator lessonIter = s.getCourse().getLessons(s, AbstractLesson.USER_ROLE).iterator(); lessonIter
                .hasNext();)
        {
            Screen screen = (Screen) lessonIter.next();
            t.addElement(makeLessonRow(s, user, screen));
        }

        // The user figured out there was a hackable admin acocunt
        tr = new TR();
        tr.addElement(new TD().setAlign("CENTER").setColSpan(9).addElement("Hackable Admin Screens"));
        t.addElement(tr);
        for (Iterator lessonIter = s.getCourse().getLessons(s, AbstractLesson.HACKED_ADMIN_ROLE).iterator(); lessonIter
                .hasNext();)
        {
            Screen screen = (Screen) lessonIter.next();
            t.addElement(makeLessonRow(s, user, screen));
        }

        // The user figured out how to actually hack the admin acocunt
        tr = new TR();
        tr.addElement(new TD().setAlign("CENTER").setColSpan(9).addElement("Actual Admin Screens"));
        t.addElement(tr);
        for (Iterator lessonIter = s.getCourse().getLessons(s, AbstractLesson.ADMIN_ROLE).iterator(); lessonIter
                .hasNext();)
        {
            Screen screen = (Screen) lessonIter.next();
            t.addElement(makeLessonRow(s, user, screen));
        }

        ec.addElement(t);
        return (ec);
    }

    /**
     * Description of the Method
     *
     * @param s
     *            Description of the Parameter
     * @param user
     *            Description of the Parameter
     * @return Description of the Return Value
     */
    protected Element makeUser(WebSession s, String user)
    {
        H2 h2 = new H2();
        // FIXME: The session is the current session, not the session of the user we are reporting.
        // String type = s.isAdmin() ? " [Administrative User]" : s.isHackedAdmin() ?
        // " [Normal User - Hacked Admin Access]" : " [Normal User]";
        String type = "";
        h2.addElement(new StringElement("Results for: " + user + type));
        return h2;
    }

    /**
     * Description of the Method
     * 
     * @return Description of the Return Value
     */
    private TR makeUserHeaderRow()
    {
        TR tr = new TR();

        tr.addElement(new TH("Lesson"));
        tr.addElement(new TH("Complete"));
        tr.addElement(new TH("Visits"));
        tr.addElement(new TH("Hints"));

        return tr;
    }
}
