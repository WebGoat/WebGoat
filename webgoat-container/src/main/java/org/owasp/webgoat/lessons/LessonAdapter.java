package org.owasp.webgoat.lessons;

import com.google.common.base.Joiner;
import org.apache.commons.io.IOUtils;
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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
public abstract class LessonAdapter extends AbstractLesson {

    /**
     * {@inheritDoc}
     *
     * Description of the Method
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

        try (InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("New Lesson Instructions.txt")) {
            if (is != null) {
                PRE pre = new PRE();
                pre.addElement(Joiner.on("\n").join(IOUtils.readLines(is)));
                ec.addElement(pre);
            }
        } catch (IOException e) {
            e.printStackTrace();
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
     * {@inheritDoc}
     *
     * Gets the hintCount attribute of the LessonAdapter object
     */
    public int getHintCount(WebSession s) {
        return getHints(s).size();
    }

    /**
     * {@inheritDoc}
     *
     * Fill in a minor hint that will help people who basically get it, but are
     * stuck on somthing silly. Hints will be returned to the user in the order
     * they appear below. The user must click on the "next hint" button before
     * the hint will be displayed.
     */
    protected List<String> getHints(WebSession s) {
        List<String> hints = new ArrayList<String>();
        hints.add("There are no hints defined.");
        return hints;
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
     * {@inheritDoc}
     *
     * Gets the instructions attribute of the LessonAdapter object. Instructions
     * will rendered as html and will appear below the control area and above
     * the actual lesson area. Instructions should provide the user with the
     * general setup and goal of the lesson.
     */
    public String getInstructions(WebSession s) {
        StringBuffer buff = new StringBuffer();
        String lang = s.getCurrrentLanguage();
        try {
            String fileName = getLessonPlanFileName(lang);
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

    /** {@inheritDoc} */
    public String getCurrentAction(WebSession s) {
        return s.getLessonSession(this).getCurrentLessonScreen();
    }

    /** {@inheritDoc} */
    public void setCurrentAction(WebSession s, String lessonScreen) {
        s.getLessonSession(this).setCurrentLessonScreen(lessonScreen);
    }

    /**
     * <p>getSessionAttribute.</p>
     *
     * @param s a {@link org.owasp.webgoat.session.WebSession} object.
     * @param key a {@link java.lang.String} object.
     * @return a {@link java.lang.Object} object.
     */
    public Object getSessionAttribute(WebSession s, String key) {
        return s.getRequest().getSession().getAttribute(key);
    }

    /**
     * <p>setSessionAttribute.</p>
     *
     * @param s a {@link org.owasp.webgoat.session.WebSession} object.
     * @param key a {@link java.lang.String} object.
     * @param value a {@link java.lang.Object} object.
     */
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

        s.setMessage(getLabelManager().get("LessonCompleted"));

        return (null);
    }

    /**
     * Gets the credits attribute of the AbstractLesson object
     *
     * @return The credits value
     * @param text a {@link java.lang.String} object.
     * @param e a {@link org.apache.ecs.Element} object.
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
