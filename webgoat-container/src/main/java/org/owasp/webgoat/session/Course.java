package org.owasp.webgoat.session;

import org.owasp.webgoat.HammerHead;
import org.owasp.webgoat.lessons.AbstractLesson;
import org.owasp.webgoat.lessons.Category;
import org.owasp.webgoat.plugins.Plugin;
import org.owasp.webgoat.plugins.PluginsLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;

import org.owasp.webgoat.HammerHead;
import org.owasp.webgoat.lessons.AbstractLesson;
import org.owasp.webgoat.lessons.Category;
import org.owasp.webgoat.plugins.LegacyLoader;
import org.owasp.webgoat.plugins.Plugin;
import org.owasp.webgoat.plugins.PluginsLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class Course {

    final Logger logger = LoggerFactory.getLogger(Course.class);

    private final List<AbstractLesson> lessons = new LinkedList<AbstractLesson>();

    private final static String PROPERTIES_FILENAME = HammerHead.propertiesPath;

    private WebgoatProperties properties = null;

    private WebgoatContext webgoatContext;

    /**
     * <p>Constructor for Course.</p>
     */
    public Course() {
        try {
            properties = new WebgoatProperties(PROPERTIES_FILENAME);
        } catch (IOException e) {
            logger.error("Error loading webgoat properties", e);
        }
    }

    /**
     * Take an absolute file and return the filename.
     *
     * Ex. /etc/password becomes password
     *
     * @param s
     * @return the file name
     */
    private static String getFileName(String s) {
        String fileName = new File(s).getName();

        if (fileName.contains("/")) {
            fileName = fileName.substring(fileName.lastIndexOf("/"), fileName.length());
        }

        if (fileName.contains(".")) {
            fileName = fileName.substring(0, fileName.indexOf("."));
        }

        return fileName;
    }

    /**
     * Take a class name and return the equivalent file name
     *
     * Ex. org.owasp.webgoat becomes org/owasp/webgoat.java
     *
     * @param className
     * @return
     */
    private static String getSourceFile(String className) {
        StringBuilder sb = new StringBuilder();

        sb.append(className.replace(".", "/"));
        sb.append(".java");

        return sb.toString();
    }

    /**
     * Takes a file name and builds the class file name
     *
     * @param fileName Description of the Parameter
     * @param path     Description of the Parameter
     * @return Description of the Return Value
     */
    private static String getClassFile(String fileName, String path) {
        String ext = ".class";
        fileName = fileName.trim();

        /**
         * We do not handle directories. We do not handle files with different
         * extensions
         */
        if (fileName.endsWith("/") || !fileName.endsWith(ext)) {
            return null;
        }

        // if the file is in /WEB-INF/classes strip the dir info off
        int index = fileName.indexOf("/WEB-INF/classes/");
        if (index != -1) {
            fileName = fileName.substring(index + "/WEB-INF/classes/".length(), fileName.length() - ext.length());
            fileName = fileName.replace('/', '.');
            fileName = fileName.replace('\\', '.');
        } else {
            // Strip off the leading path info
            fileName = fileName.substring(path.length(), fileName.length() - ext.length());
        }

        return fileName;
    }

    /**
     * Gets the categories attribute of the Course object
     *
     * @return The categories value
     */
    public List getCategories() {
        List<Category> categories = new ArrayList<Category>();
        for (AbstractLesson lesson : lessons) {
            if (!categories.contains(lesson.getCategory())) {
                categories.add(lesson.getCategory());
            }
        }

        Collections.sort(categories);

        return categories;
    }

    /**
     * Gets the firstLesson attribute of the Course object
     *
     * @return The firstLesson value
     */
    public AbstractLesson getFirstLesson() {
        List<String> roles = new ArrayList<String>();
        roles.add(AbstractLesson.USER_ROLE);
        // Category 0 is the admin function. We want the first real category
        // to be returned. This is normally the General category and the Http Basics lesson
        return ((AbstractLesson) getLessons((Category) getCategories().get(0), roles).get(0));
    }

    /**
     * Gets the lesson attribute of the Course object
     *
     * @param s a {@link org.owasp.webgoat.session.WebSession} object.
     * @param lessonId Description of the Parameter
     * @param roles a {@link java.util.List} object.
     * @return The lesson value
     */
    public AbstractLesson getLesson(WebSession s, int lessonId, List<String> roles) {
        if (s.isHackedAdmin()) {
            roles.add(AbstractLesson.HACKED_ADMIN_ROLE);
        }
        // System.out.println("getLesson() with roles: " + roles);
        Iterator<AbstractLesson> iter = lessons.iterator();

        while (iter.hasNext()) {
            AbstractLesson lesson = iter.next();

            if (lesson.getScreenId() == lessonId && roles.contains(lesson.getRole())) {
                return lesson;
            }
        }

        return null;
    }

    /**
     * <p>getLesson.</p>
     *
     * @param s a {@link org.owasp.webgoat.session.WebSession} object.
     * @param lessonId a int.
     * @param role a {@link java.lang.String} object.
     * @return a {@link org.owasp.webgoat.lessons.AbstractLesson} object.
     */
    public AbstractLesson getLesson(WebSession s, int lessonId, String role) {
        List<String> roles = new ArrayList<String>();
        roles.add(role);
        return getLesson(s, lessonId, roles);
    }

    /**
     * <p>Getter for the field <code>lessons</code>.</p>
     *
     * @param s a {@link org.owasp.webgoat.session.WebSession} object.
     * @param role a {@link java.lang.String} object.
     * @return a {@link java.util.List} object.
     */
    public List<AbstractLesson> getLessons(WebSession s, String role) {
        List<String> roles = new ArrayList<String>();
        roles.add(role);
        return getLessons(s, roles);
    }

    /**
     * Gets the lessons attribute of the Course object
     *
     * @param s a {@link org.owasp.webgoat.session.WebSession} object.
     * @param roles a {@link java.util.List} object.
     * @return The lessons value
     */
    public List<AbstractLesson> getLessons(WebSession s, List<String> roles) {
        if (s.isHackedAdmin()) {
            roles.add(AbstractLesson.HACKED_ADMIN_ROLE);
        }
        List<AbstractLesson> lessonList = new ArrayList<AbstractLesson>();
        Iterator categoryIter = getCategories().iterator();

        while (categoryIter.hasNext()) {
            lessonList.addAll(getLessons(s, (Category) categoryIter.next(), roles));
        }
        return lessonList;
    }

    /**
     * Gets the lessons attribute of the Course object
     *
     * @param category Description of the Parameter
     * @param role     Description of the Parameter
     * @return The lessons value
     */
    private List<AbstractLesson> getLessons(Category category, List roles) {
        List<AbstractLesson> lessonList = new ArrayList<AbstractLesson>();

        for (AbstractLesson lesson : lessons) {
            if (lesson.getCategory().equals(category) && roles.contains(lesson.getRole())) {
                lessonList.add(lesson);
            }
        }

        Collections.sort(lessonList);
        return lessonList;
    }

    /**
     * <p>Getter for the field <code>lessons</code>.</p>
     *
     * @param s a {@link org.owasp.webgoat.session.WebSession} object.
     * @param category a {@link org.owasp.webgoat.lessons.Category} object.
     * @param role a {@link java.lang.String} object.
     * @return a {@link java.util.List} object.
     */
    public List getLessons(WebSession s, Category category, String role) {
        List<String> roles = new ArrayList<String>();
        roles.add(role);
        return getLessons(s, category, roles);
    }

    /**
     * <p>Getter for the field <code>lessons</code>.</p>
     *
     * @param s a {@link org.owasp.webgoat.session.WebSession} object.
     * @param category a {@link org.owasp.webgoat.lessons.Category} object.
     * @param roles a {@link java.util.List} object.
     * @return a {@link java.util.List} object.
     */
    public List<AbstractLesson> getLessons(WebSession s, Category category, List<String> roles) {
        if (s.isHackedAdmin()) {
            roles.add(AbstractLesson.HACKED_ADMIN_ROLE);
        }
        return getLessons(category, roles);
    }

    /**
     * <p>getLesson.</p>
     *
     * @param lessonId a int.
     * @return a {@link org.owasp.webgoat.lessons.AbstractLesson} object.
     */
    public AbstractLesson getLesson(int lessonId) {
        for (AbstractLesson l : lessons) {
            if (l.getScreenId() == lessonId) {
                return l;
            }
        }
        return null;
    }

    /**
     * <p>loadLessonFromPlugin.</p>
     *
     * @param context a {@link javax.servlet.ServletContext} object.
     */
    public void loadLessonFromPlugin(ServletContext context) {
        logger.debug("Loading plugins into cache");
        String pluginPath = context.getRealPath("plugin_lessons");
        String targetPath = context.getRealPath("plugin_extracted");

        if (pluginPath == null) {
            logger.error("Plugins directory {} not found", pluginPath);
            return;
        }
        lessons.clear();
        List<Plugin> plugins = new PluginsLoader(Paths.get(pluginPath), Paths.get(targetPath)).loadPlugins();
        for (Plugin plugin : plugins) {
            try {
                AbstractLesson lesson = plugin.getLesson().get();
                lesson.setWebgoatContext(webgoatContext);
                lesson.update(properties);

                if (!lesson.getHidden()) {
                    lessons.add(lesson);
                }
                for(Map.Entry<String, File> lessonPlan : plugin.getLessonPlans().entrySet()) {
                    lesson.setLessonPlanFileName(lessonPlan.getKey(), lessonPlan.getValue().toString());
                }
                if (plugin.getLessonSolution("en").isPresent()) {
                    lesson.setLessonSolutionFileName(plugin.getLessonSolution("en").get().toString());
                }
                if (plugin.getLessonSource().isPresent()) {
                    lesson.setSourceFileName(plugin.getLessonSource().get().toString());
                }
            } catch (Exception e) {
                logger.error("Error in loadLessons: ", e);
            }
        }
    }

    /**
     * Description of the Method
     *
     * @param webgoatContext a {@link org.owasp.webgoat.session.WebgoatContext} object.
     * @param path           Description of the Parameter
     * @param context        Description of the Parameter
     */
    public void loadCourses(WebgoatContext webgoatContext, ServletContext context, String path) {
        logger.info("Loading courses: " + path);
        this.webgoatContext = webgoatContext;
        loadLessonFromPlugin(context);
        LegacyLoader loader = new LegacyLoader();
        lessons.addAll(loader.loadLessons(webgoatContext, context, path, properties));        
    }
}
