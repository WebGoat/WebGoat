package org.owasp.webgoat.lessons;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.Center;
import org.apache.ecs.html.H3;
import org.apache.ecs.html.P;
import org.apache.ecs.html.PRE;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.owasp.webgoat.session.WebSession;
import org.owasp.webgoat.util.WebGoatI18N;

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
 * For details, please see http://webgoat.github.io
 *
 * @author Bruce Mayhew <a href="http://code.google.com/p/webgoat">WebGoat</a>
 * @created October 28, 2003
 */
public abstract class LessonAdapter extends AbstractLesson {

    /**
     * Description of the Method
     *
     * @param s Description of the Parameter
     * @return Description of the Return Value
     */
    protected Element createContent(WebSession s) {
        // Mark this lesson as completed.
        makeSuccess(s);

        ElementContainer ec = new ElementContainer();

        ec.addElement(new Center().addElement(new H3().addElement(new StringElement(
                "Detailed Lesson Creation Instructions."))));
        ec.addElement(new P());
        ec
                .addElement(new StringElement(
                                "Lesson are simple to create and very little coding is required. &nbsp;&nbsp;"
                                + "In fact, most lessons can be created by following the easy to use instructions in the "
                                + "<A HREF=http://www.owasp.org/index.php/WebGoat_User_and_Install_Guide_Table_of_Contents>WebGoat User Guide.</A>&nbsp;&nbsp;"
                                + "If you would prefer, send your lesson ideas to "
                                + getWebgoatContext().getFeedbackAddressHTML()));

        String fileName = s.getContext().getRealPath("WEB-INF/classes/New Lesson Instructions.txt");
        if (fileName != null) {
            try {
                PRE pre = new PRE();
                BufferedReader in = new BufferedReader(new FileReader(fileName));
                String line = null;
                while ((line = in.readLine()) != null) {
                    pre.addElement(line + "\n");
                }
                ec.addElement(pre);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return (ec);
    }

    /**
     * Gets the category attribute of the LessonAdapter object. The default
     * category is "General" Only override this method if you wish to create a
     * new category or if you wish this lesson to reside within a category other
     * the "General"
     *
     * @return The category value
     */
    protected Category getDefaultCategory() {
        return Category.GENERAL;
    }

    protected boolean getDefaultHidden() {
        return false;
    }

    private final static Integer DEFAULT_RANKING = new Integer(1000);

    protected Integer getDefaultRanking() {
        return DEFAULT_RANKING;
    }

    /**
     * Gets the hintCount attribute of the LessonAdapter object
     *
     * @return The hintCount value
     */
    public int getHintCount(WebSession s) {
        return getHints(s).size();
    }

    /**
     * Fill in a minor hint that will help people who basically get it, but are
     * stuck on somthing silly. Hints will be returned to the user in the order
     * they appear below. The user must click on the "next hint" button before
     * the hint will be displayed.
     *
     * @return The hint1 value
     */
    protected List<String> getHints(WebSession s) {
        List<String> hints = new ArrayList<String>();
        hints.add("There are no hints defined.");
        return hints;
    }

    /**
     * Gets the credits attribute of the AbstractLesson object
     *
     * @return The credits value
     */
    public Element getCredits() {
        return new StringElement();
    }

    /**
     * Gets the instructions attribute of the LessonAdapter object. Instructions
     * will rendered as html and will appear below the control area and above
     * the actual lesson area. Instructions should provide the user with the
     * general setup and goal of the lesson.
     *
     * @return The instructions value
     */
    public String getInstructions(WebSession s) {
        StringBuffer buff = new StringBuffer();
        String lang = s.getCurrrentLanguage();
        try {
            String fileName = s.getWebResource(getLessonPlanFileName(lang));
            if (fileName != null) {
                BufferedReader in = new BufferedReader(new FileReader(fileName));
                String line = null;
                boolean startAppending = false;
                while ((line = in.readLine()) != null) {
                    if (line.indexOf("<!-- Start Instructions -->") != -1) {
                        startAppending = true;
                        continue;
                    }
                    if (line.indexOf("<!-- Stop Instructions -->") != -1) {
                        startAppending = false;
                        continue;
                    }
                    if (startAppending) {
                        buff.append(line + "\n");
                    }
                }
            }
        } catch (Exception e) {
        }

        return buff.toString();

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

    public String getCurrentAction(WebSession s) {
        return s.getLessonSession(this).getCurrentLessonScreen();
    }

    public void setCurrentAction(WebSession s, String lessonScreen) {
        s.getLessonSession(this).setCurrentLessonScreen(lessonScreen);
    }

    public Object getSessionAttribute(WebSession s, String key) {
        return s.getRequest().getSession().getAttribute(key);
    }

    public void setSessionAttribute(WebSession s, String key, Object value) {
        s.getRequest().getSession().setAttribute(key, value);
    }

    /**
     * Description of the Method
     *
     * @param s Description of the Parameter
     * @return Description of the Return Value
     */
    protected Element makeSuccess(WebSession s) {
        getLessonTracker(s).setCompleted(true);

        s.setMessage(WebGoatI18N.get("LessonCompleted"));

        return (null);
    }

    /**
     * Gets the credits attribute of the AbstractLesson object
     *
     * @return The credits value
     */
    protected Element getCustomCredits(String text, Element e) {
        Table t = new Table().setCellSpacing(0).setCellPadding(0).setBorder(0).setWidth("90%").setAlign("RIGHT");
        TR tr = new TR();
        tr.addElement(new TD(text).setVAlign("MIDDLE").setAlign("RIGHT").setWidth("100%"));
        tr.addElement(new TD(e).setVAlign("MIDDLE").setAlign("RIGHT"));
        t.addElement(tr);
        return t;
    }

}
