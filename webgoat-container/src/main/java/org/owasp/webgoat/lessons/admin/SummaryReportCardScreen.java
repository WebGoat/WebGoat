
package org.owasp.webgoat.lessons.admin;

import java.util.Enumeration;
import java.util.Iterator;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.HtmlColor;
import org.apache.ecs.html.Center;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.P;
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
 * @author Bruce mayhew <a href="http://code.google.com">WebGoat</a>
 * @since October 28, 2003
 * @version $Id: $Id
 */
public class SummaryReportCardScreen extends LessonAdapter
{

    private int totalUsersNormalComplete = 0;

    private int totalUsersAdminComplete = 0;

    /**
     * {@inheritDoc}
     *
     * Description of the Method
     */
    protected Element createContent(WebSession s)
    {
        ElementContainer ec = new ElementContainer();

        String selectedUser = null;

        try
        {
            if (s.getRequest().isUserInRole(WebSession.WEBGOAT_ADMIN))
            {
                Enumeration e = s.getParser().getParameterNames();

                while (e.hasMoreElements())
                {
                    String key = (String) e.nextElement();
                    if (key.startsWith("View_"))
                    {
                        selectedUser = key.substring("View_".length());
                        ReportCardScreen reportCard = new ReportCardScreen();
                        return reportCard.makeReportCard(s, selectedUser);
                    }
                    if (key.startsWith("Delete_"))
                    {
                        selectedUser = key.substring("Delete_".length());
                        deleteUser(selectedUser);
                    }
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        ec.addElement(new Center().addElement(makeSummary(s)));

        ec.addElement(new P());

        Table t = new Table().setCellSpacing(0).setCellPadding(4).setBorder(1).setWidth("100%");
        if (s.isColor())
        {
            t.setBorder(1);
        }
        t.addElement(makeUserSummaryHeader());

        for (Iterator<String> userIter = UserTracker.instance().getAllUsers(WebSession.WEBGOAT_USER).iterator(); userIter
                .hasNext();)
        {

            String user = userIter.next();
            t.addElement(makeUserSummaryRow(s, user));
        }

        ec.addElement(new Center().addElement(t));

        return ec;
    }

    /**
     * <p>makeSummary.</p>
     *
     * @param s a {@link org.owasp.webgoat.session.WebSession} object.
     * @return a {@link org.apache.ecs.Element} object.
     */
    protected Element makeSummary(WebSession s)
    {
        Table t = new Table().setCellSpacing(0).setCellPadding(2).setBorder(0).setWidth("100%");
        if (s.isColor())
        {
            t.setBorder(1);
        }
        TR tr = new TR();
        // tr.addElement( new TH().addElement( "Summary").setColSpan(1));
        // t.addElement( tr );

        tr = new TR();
        tr.addElement(new TD().setWidth("60%").addElement("Total number of users"));
        tr.addElement(new TD().setAlign("LEFT").addElement(
                                                            Integer.toString(UserTracker.instance()
                                                                    .getAllUsers(WebSession.WEBGOAT_USER).size())));
        t.addElement(tr);

        tr = new TR();
        tr.addElement(new TD().setWidth("60%").addElement("Total number of users that completed all normal lessons"));
        tr.addElement(new TD().setAlign("LEFT").addElement(Integer.toString(totalUsersNormalComplete)));
        t.addElement(tr);

        tr = new TR();
        tr.addElement(new TD().setWidth("60%").addElement("Total number of users that completed all admin lessons"));
        tr.addElement(new TD().setAlign("LEFT").addElement(Integer.toString(totalUsersAdminComplete)));
        t.addElement(tr);
        return t;
    }

    private void deleteUser(String user)
    {
        UserTracker.instance().deleteUser(user);
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
        return ("Summary Report Card");
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
     * @return Description of the Return Value
     */
    protected Element makeUserSummaryHeader()
    {
        TR tr = new TR();

        tr.addElement(new TH("User Name"));
        tr.addElement(new TH("Normal Complete"));
        tr.addElement(new TH("Admin Complete"));
        tr.addElement(new TH("View"));
        tr.addElement(new TH("Delete"));

        return tr;
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
    protected Element makeUserSummaryRow(WebSession s, String user)
    {
        TR tr = new TR();

        tr.addElement(new TD().setAlign("LEFT").addElement(user));
        int lessonCount = 0;
        int passedCount = 0;
        boolean normalComplete = false;
        boolean adminComplete = false;

        for (Iterator lessonIter = s.getCourse().getLessons(s, AbstractLesson.USER_ROLE).iterator(); lessonIter
                .hasNext();)
        {
            lessonCount++;
            Screen screen = (Screen) lessonIter.next();

            LessonTracker lessonTracker = UserTracker.instance().getLessonTracker(s, user, screen);
            if (lessonTracker.getCompleted())
            {
                passedCount++;
            }
        }
        if (lessonCount == passedCount)
        {
            normalComplete = true;
            totalUsersNormalComplete++;
        }
        String text = Integer.toString(passedCount) + " of " + Integer.toString(lessonCount);
        tr.addElement(new TD().setAlign("CENTER").addElement(text));

        lessonCount = 0;
        passedCount = 0;
        for (Iterator lessonIter = s.getCourse().getLessons(s, AbstractLesson.HACKED_ADMIN_ROLE).iterator(); lessonIter
                .hasNext();)
        {
            lessonCount++;
            Screen screen = (Screen) lessonIter.next();

            LessonTracker lessonTracker = UserTracker.instance().getLessonTracker(s, user, screen);
            if (lessonTracker.getCompleted())
            {
                passedCount++;
            }
        }
        if (lessonCount == passedCount)
        {
            adminComplete = true;
            totalUsersAdminComplete++;
        }
        text = Integer.toString(passedCount) + " of " + Integer.toString(lessonCount);
        tr.addElement(new TD().setAlign("CENTER").addElement(text));

        tr.addElement(new TD().setAlign("CENTER").addElement(new Input(Input.SUBMIT, "View_" + user, "View")));
        tr.addElement(new TD().setAlign("CENTER").addElement(new Input(Input.SUBMIT, "Delete_" + user, "Delete")));

        if (normalComplete && adminComplete)
        {
            tr.setBgColor(HtmlColor.GREEN);
        }
        else if (normalComplete)
        {
            tr.setBgColor(HtmlColor.LIGHTGREEN);
        }
        else
        {
            tr.setBgColor(HtmlColor.LIGHTBLUE);
        }

        return (tr);
    }

    /**
     * <p>isEnterprise.</p>
     *
     * @return a boolean.
     */
    public boolean isEnterprise()
    {
        return true;
    }
}
