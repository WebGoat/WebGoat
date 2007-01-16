package org.owasp.webgoat.session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.servlet.ServletContext;

import org.owasp.webgoat.HammerHead;
import org.owasp.webgoat.lessons.AbstractLesson;
import org.owasp.webgoat.lessons.Category;

/*******************************************************************************
 * 
 * 
 * This file is part of WebGoat, an Open Web Application Security Project
 * utility. For details, please see http://www.owasp.org/
 * 
 * Copyright (c) 2002 - 2007 Bruce Mayhew
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
 * Source for this application is maintained at code.google.com, a repository
 * for free software projects.
 * 
 * For details, please see http://code.google.com/p/webgoat/
 *
 * @author     Bruce Mayhew <a href="http://code.google.com/p/webgoat">WebGoat</a>
 * @created    October 28, 2003
 */
public class Course
{

    private List lessons = new ArrayList();

    private final static String PROPERTIES_FILENAME = HammerHead.propertiesPath;

    private WebgoatProperties properties = null;


    public Course()
    {
	try
	{
	    properties = new WebgoatProperties(PROPERTIES_FILENAME);
	}
	catch (IOException e)
	{
	    System.out.println("Error loading WebGoat properties");
	    e.printStackTrace();
	}
    }


    /**
     *  Description of the Method
     *
     * @param  fileName  Description of the Parameter
     * @param  path      Description of the Parameter
     * @param  ext       Description of the Parameter
     * @return           Description of the Return Value
     */
    private String clean(String fileName, String path, String ext)
    {
	fileName = fileName.trim();

	// check if file is a directory
	if (fileName.endsWith("/"))
	{
	    return fileName;
	}

	// check if file is a class or java file
	if (!fileName.endsWith(ext))
	{
	    return null;
	}

	// if the file is in /WEB-INF/classes strip the dir info off
	int index = fileName.indexOf("/WEB-INF/classes/");
	if (index != -1)
	{
	    fileName = fileName.substring(index + "/WEB-INF/classes/".length(),
		    fileName.length() - ext.length());
	    fileName = fileName.replace('/', '.');
	    fileName = fileName.replace('\\', '.');
	}
	else
	{
	    // Strip off the leading path info
	    fileName = fileName.substring(path.length(), fileName.length()
		    - ext.length());
	}

	return fileName;
    }


    /**
     *  Description of the Method
     * @param  lesson      Description of the Parameter
     * @param  context     Description of the Parameter
     * @param  path        Description of the Parameter
     * @param  courseName  Description of the Parameter
     * @param extension TODO
     */
    private void findSourceResource(AbstractLesson lesson,
	    ServletContext context, String path, String className,
	    String extension)
    {
	//System.out.println("findSourceResource() looking for source files in: " + path);
	//System.out.println("findSourceResource() looking for source files for class: " + className);
	Set files = context.getResourcePaths(path);
	Iterator fileIter = files.iterator();
	String resource = null;

	while (fileIter.hasNext())
	{
	    resource = (String) fileIter.next();
	    //System.out.println("findSourceResource() inspecting resource: " + resource);
	    String lessonName = clean(resource, path, extension);
	    //System.out.println("findSourceResource() cleaned resource name: " + lessonName);
	    //if ( className != null )
	    //{
	    //	System.out.println("Resource to check: " + resource);
	    //	System.out.println("Lesson name: " + lessonName);
	    //}

	    // Not a match
	    if (lessonName == null)
	    {
		continue;
	    }
	    // A subdirectory
	    else if ((lessonName.length() != 1) && lessonName.endsWith("/"))
	    {
		findSourceResource(lesson, context, lessonName, className,
			extension);
	    }
	    // A source file
	    else
	    {
		// Course name will be the fully qualified name: 
		// like lesson.admin.lessonName
		if (className.endsWith(lessonName))
		{
		    int length = 0;
		    int index = className.indexOf("admin.");
		    if (index == -1)
		    {
			index = className.indexOf("lessons.");
			length = "lessons.".length();
		    }
		    else
		    {
			length = "admin.".length();
		    }
		    className = className.substring(index + length);
		    //System.out.println("Resource to check: " + resource);
		    //System.out.println("Lesson name: " + lessonName);

		    //store the web path of the source file in the lesson
		    lesson.setSourceFileName(resource);

		}
	    }
	}
    }


    /**
     *  Description of the Method
     * @param  lesson      Description of the Parameter
     * @param  context     Description of the Parameter
     * @param  path        Description of the Parameter
     * @param  courseName  Description of the Parameter
     * @param extension TODO
     */
    private void findLessonPlanResource(AbstractLesson lesson,
	    ServletContext context, String path, String courseName,
	    String extension)
    {
	Set files = context.getResourcePaths(path);
	Iterator fileIter = files.iterator();
	String resource = null;

	while (fileIter.hasNext())
	{
	    resource = (String) fileIter.next();
	    String className = clean(resource, path, extension);
	    //if ( className != null )
	    //{
	    //    System.out.println("ClassName: " + className);
	    //    System.out.println("ResourceToCheck: " + resourceToCheck);
	    //}

	    if (className == null)
	    {
		continue;
	    }
	    else if ((className.length() != 1) && className.endsWith("/"))
	    {
		findLessonPlanResource(lesson, context, className, courseName,
			extension);
	    }
	    else
	    {
		// Course name will be the fully qualified name: 
		// like lesson.admin.lessonName
		if (courseName.endsWith(className))
		{
		    int length = 0;
		    int index = courseName.indexOf("admin.");
		    if (index == -1)
		    {
			index = courseName.indexOf("lessons.");
			length = "lessons.".length();
		    }
		    else
		    {
			length = "admin.".length();
		    }
		    courseName = courseName.substring(index + length);
		    //System.out.println("ClassName: " + className);
		    //System.out.println("ResourceToCheck: " + resource);

		    //store the web path of the source file in the lesson
		    lesson.setLessonPlanFileName(resource);

		}
	    }
	}
    }


    /**
     *  Gets the categories attribute of the Course object
     *
     * @return    The categories value
     */
    public List getCategories()
    {
	List<Category> categories = new ArrayList<Category>();
	Iterator iter = lessons.iterator();

	while (iter.hasNext())
	{
	    AbstractLesson lesson = (AbstractLesson) iter.next();

	    if (!categories.contains(lesson.getCategory()))
	    {
		categories.add(lesson.getCategory());
	    }
	}

	Collections.sort(categories);

	return categories;
    }


    /**
     *  Gets the firstLesson attribute of the Course object
     *
     * @return    The firstLesson value
     */
    public AbstractLesson getFirstLesson()
    {
	List roles = new ArrayList();
	roles.add(AbstractLesson.USER_ROLE);
	// Category 0 is the admin function.  We want the first real category 
	// to be returned. This is noramally the General category and the Http Basics lesson
	return ((AbstractLesson) getLessons((Category) getCategories().get(1),
		roles).get(0));
    }


    /**
     *  Gets the lesson attribute of the Course object
     *
     * @param  lessonId  Description of the Parameter
     * @param  role      Description of the Parameter
     * @return           The lesson value
     */
    public AbstractLesson getLesson(WebSession s, int lessonId, List roles)
    {
	if (s.isHackedAdmin())
	{
	    roles.add(AbstractLesson.HACKED_ADMIN_ROLE);
	}
	//System.out.println("getLesson() with roles: " + roles);
	Iterator iter = lessons.iterator();

	while (iter.hasNext())
	{
	    AbstractLesson lesson = (AbstractLesson) iter.next();

	    //System.out.println("getLesson() at role: " + lesson.getRole());
	    if (lesson.getScreenId() == lessonId
		    && roles.contains(lesson.getRole()))
	    {
		return lesson;
	    }
	}

	return null;
    }


    public AbstractLesson getLesson(WebSession s, int lessonId, String role)
    {
	List roles = new Vector();
	roles.add(role);
	return getLesson(s, lessonId, roles);
    }


    public List getLessons(WebSession s, String role)
    {
	List roles = new Vector();
	roles.add(role);
	return getLessons(s, roles);
    }


    /**
     *  Gets the lessons attribute of the Course object
     *
     * @param  role  Description of the Parameter
     * @return       The lessons value
     */
    public List getLessons(WebSession s, List roles)
    {
	if (s.isHackedAdmin())
	{
	    roles.add(AbstractLesson.HACKED_ADMIN_ROLE);
	}
	List lessonList = new ArrayList();
	Iterator categoryIter = getCategories().iterator();

	while (categoryIter.hasNext())
	{
	    lessonList.addAll(getLessons(s, (Category) categoryIter.next(),
		    roles));
	}
	return lessonList;
    }


    /**
     *  Gets the lessons attribute of the Course object
     *
     * @param  category  Description of the Parameter
     * @param  role      Description of the Parameter
     * @return           The lessons value
     */
    private List getLessons(Category category, List roles)
    {
	List<AbstractLesson> lessonList = new ArrayList<AbstractLesson>();

	Iterator iter = lessons.iterator();
	while (iter.hasNext())
	{
	    AbstractLesson lesson = (AbstractLesson) iter.next();

	    if (lesson.getCategory().equals(category)
		    && roles.contains(lesson.getRole()))
	    {
		lessonList.add(lesson);
	    }
	}

	Collections.sort(lessonList);
	//		System.out.println(java.util.Arrays.asList(lessonList));
	return lessonList;
    }


    public List getLessons(WebSession s, Category category, String role)
    {
	List roles = new Vector();
	roles.add(role);
	return getLessons(s, category, roles);
    }


    public List getLessons(WebSession s, Category category, List roles)
    {
	if (s.isHackedAdmin())
	{
	    roles.add(AbstractLesson.HACKED_ADMIN_ROLE);
	}
	return getLessons(category, roles);
    }


    /**
     *  Description of the Method
     *
     * @param  path     Description of the Parameter
     * @param  context  Description of the Parameter
     */
    public void loadCourses(boolean enterprise, ServletContext context,
	    String path)
    {
	Set files = context.getResourcePaths(path);
	Iterator fileIter = files.iterator();

	while (fileIter.hasNext())
	{
	    String file = (String) fileIter.next();
	    String className = clean(file, path, ".class");

	    //if ( className != null )
	    //{
	    //	System.out.println( "Checking file: " + file );
	    //	System.out.println( "        class: " + className );
	    //}
	    if (className == null)
	    {
		continue;
	    }
	    else if ((className.length() != 1) && className.endsWith("/"))
	    {
		loadCourses(enterprise, context, className);
	    }
	    else
	    {
		Class lessonClass = null;
		try
		{
		    lessonClass = Class.forName(className);
		    Object possibleLesson = lessonClass.newInstance();

		    if (possibleLesson instanceof AbstractLesson)
		    {
			AbstractLesson lesson = (AbstractLesson) possibleLesson;

			// Determine if the screen is to be loaded.  Look 
			// to see if the session parameter has been initialized.
			// Look to see if the screen is an enterprise edition screen.
			if (!enterprise)
			{
			    if (lesson.isEnterprise())
			    {
				continue;
			    }
			}

			// Do not load instructor screens.  Currently, they must be manually deployed.
			if (lesson.getClass().getName().indexOf("instructor") > -1)
			    continue;

			// There are two methods instead of one because the developer was not
			// smart enough to figure out the recursive return value
			findSourceResource(lesson, context, "/", className,
				".java");
			findLessonPlanResource(lesson, context, "/", className,
				".html");

			// Override lesson attributes based on properties.
			lesson.update(properties);

			if (lesson.getHidden() == false)
			    lessons.add(lesson);
			//System.out.println( "Found lesson: " + lesson );
		    }
		}
		catch (Exception e)
		{
		    //System.out.println("Could not load lesson: " + className);
		    //e.printStackTrace();
		}
	    }
	}
    }
}
