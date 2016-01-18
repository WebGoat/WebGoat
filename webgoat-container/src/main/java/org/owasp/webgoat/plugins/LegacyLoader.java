package org.owasp.webgoat.plugins;

import org.owasp.webgoat.lessons.AbstractLesson;
import org.owasp.webgoat.session.WebgoatContext;
import org.owasp.webgoat.session.WebgoatProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
public class LegacyLoader {

    final Logger logger = LoggerFactory.getLogger(LegacyLoader.class);

    private final List<String> files = new LinkedList<String>();

    /**
     * <p>Constructor for LegacyLoader.</p>
     */
    public LegacyLoader() {
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

        // skip over plugins and/or extracted plugins
        if ( fileName.indexOf("lessons/plugin") >= 0  ||  fileName.indexOf("plugin_extracted") >= 0) {
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
     * Load all of the filenames into a temporary cache
     *
     * @param context a {@link javax.servlet.ServletContext} object.
     * @param path a {@link java.lang.String} object.
     */
    public void loadFiles(ServletContext context, String path) {
        logger.debug("Loading files into cache, path: " + path);
        Set resourcePaths = context.getResourcePaths(path);
        if (resourcePaths == null) {
            logger.error("Unable to load file cache for courses, this is probably a bug or configuration issue");
            return;
        }
        Iterator itr = resourcePaths.iterator();

        while (itr.hasNext()) {
            String file = (String) itr.next();

            if (file.length() != 1 && file.endsWith("/")) {
                loadFiles(context, file);
            } else {
                files.add(file);
           }
        }
    }

    /**
     * Instantiate all the lesson objects into a cache
     *
     * @param path a {@link java.lang.String} object.
     * @param context a {@link javax.servlet.ServletContext} object.
     * @param webgoatContext a {@link org.owasp.webgoat.session.WebgoatContext} object.
     * @param properties a {@link org.owasp.webgoat.session.WebgoatProperties} object.
     * @return a {@link java.util.List} object.
     */
    public List<AbstractLesson> loadLessons(WebgoatContext webgoatContext, ServletContext context, String path, WebgoatProperties properties ) {

    	loadFiles(context, path);

        List<AbstractLesson> lessons = new LinkedList<AbstractLesson>();

        for (String file : files) {
            String className = getClassFile(file, path);

            if (className != null && !className.endsWith("_i") && className.startsWith("org.owasp.webgoat.lessons.admin")) {
                try {
                	Class c = Class.forName(className);
                    Object o = c.newInstance();

                    if (o instanceof AbstractLesson) {
                        AbstractLesson lesson = (AbstractLesson) o;
                        lesson.setWebgoatContext(webgoatContext);

                        lesson.update(properties);

                        if (lesson.getHidden() == false) {
                            lessons.add(lesson);
                        }
                    }
                } catch (Exception e) {
                	// Bruce says:
                	// I don't think we want to log the exception here. We could
                	// be potentially showing a lot of exceptions that don't matter.
                	// We would only care if the lesson extended AbstractLesson and we 
                	// can't tell that because it threw the exception.  Catch 22
                   // logger.error("Error in loadLessons: ", e);
                }
            }
        }
        loadResources(lessons);
        return lessons;
    }

    private String getLanguageFromFileName(String first, String absoluteFile) {
        int p1 = absoluteFile.indexOf("/", absoluteFile.indexOf(first) + 1);
        int p2 = absoluteFile.indexOf("/", p1 + 1);
        String langStr = absoluteFile.substring(p1 + 1, p2);

        return langStr;
    }

    /**
     * For each lesson, set the source file and lesson file
     *
     * @param lessons a {@link java.util.List} object.
     */
    public void loadResources(List<AbstractLesson> lessons ) {
        for (AbstractLesson lesson : lessons) {
            logger.info("Loading resources for lesson -> " + lesson.getName());
            String className = lesson.getClass().getName();
            String classFile = getSourceFile(className);
            logger.info("Lesson classname: " + className);
            logger.info("Lesson java file: " + classFile);

            for (String absoluteFile : files) {
                String fileName = getFileName(absoluteFile);
                //logger.debug("Course: looking at file: " + absoluteFile);

                if (absoluteFile.endsWith(classFile)) {
                    logger.info("Set source file for " + classFile);
                    lesson.setSourceFileName(absoluteFile);
                }

                if (absoluteFile.startsWith("/lesson_plans") && absoluteFile.endsWith(".html")
                        && className.endsWith(fileName)) {
                    logger.info("setting lesson plan file " + absoluteFile + " for lesson "
                            + lesson.getClass().getName());
                    logger.info("fileName: " + fileName + " == className: " + className);
                    String language = getLanguageFromFileName("/lesson_plans", absoluteFile);
                    lesson.setLessonPlanFileName(language, absoluteFile);
                }
                if (absoluteFile.startsWith("/lesson_solutions") && absoluteFile.endsWith(".html")
                        && className.endsWith(fileName)) {
                    logger.info("setting lesson solution file " + absoluteFile + " for lesson "
                            + lesson.getClass().getName());
                    logger.info("fileName: " + fileName + " == className: " + className);
                    lesson.setLessonSolutionFileName(absoluteFile);
                }
            }
        }
    }


}
