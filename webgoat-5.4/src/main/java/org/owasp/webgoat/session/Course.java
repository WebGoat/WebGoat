
package org.owasp.webgoat.session;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.LinkedList;
import javax.servlet.ServletContext;
import org.owasp.webgoat.HammerHead;
import org.owasp.webgoat.lessons.AbstractLesson;
import org.owasp.webgoat.lessons.Category;



/***************************************************************************************************
 * 
 * 
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 * 
 * Copyright (c) 2002 - 2007 Bruce Mayhew
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
 * Source for this application is maintained at code.google.com, a repository for free software
 * projects.
 * 
 * For details, please see http://code.google.com/p/webgoat/
 * 
 * @author Bruce Mayhew <a href="http://code.google.com/p/webgoat">WebGoat</a>
 * @created October 28, 2003
 */
public class Course
{

	private List<AbstractLesson> lessons = new LinkedList<AbstractLesson>();

	private final static String PROPERTIES_FILENAME = HammerHead.propertiesPath;

	private WebgoatProperties properties = null;

	private List<String> files = new LinkedList<String>();

	private WebgoatContext webgoatContext;

	
	public Course()
	{
		try
		{
			properties = new WebgoatProperties(PROPERTIES_FILENAME);
		} catch (IOException e)
		{
			System.out.println("Error loading WebGoat properties");
			e.printStackTrace();
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
	private static String getFileName(String s)
	{
		String fileName = new File(s).getName();

		if (fileName.indexOf("/") != -1)
		{
			fileName = fileName.substring(fileName.lastIndexOf("/"), fileName.length());
		}

		if (fileName.indexOf(".") != -1)
		{
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
	private static String getSourceFile(String className)
	{
		StringBuffer sb = new StringBuffer();

		sb.append(className.replace(".", "/"));
		sb.append(".java");

		return sb.toString();
	}

	/**
	 * Takes a file name and builds the class file name
	 * 
	 * @param fileName
	 *            Description of the Parameter
	 * @param path
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	private static String getClassFile(String fileName, String path)
	{
		String ext = ".class";
		fileName = fileName.trim();

		/**
		 * We do not handle directories.
		 * We do not handle files with different extensions
		 */
		if (fileName.endsWith("/") || !fileName.endsWith(ext)) { return null; }

		// if the file is in /WEB-INF/classes strip the dir info off
		int index = fileName.indexOf("/WEB-INF/classes/");
		if (index != -1)
		{
			fileName = fileName.substring(index + "/WEB-INF/classes/".length(), fileName.length() - ext.length());
			fileName = fileName.replace('/', '.');
			fileName = fileName.replace('\\', '.');
		}
		else
		{
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
	 * Gets the firstLesson attribute of the Course object
	 * 
	 * @return The firstLesson value
	 */
	public AbstractLesson getFirstLesson()
	{
		List<String> roles = new ArrayList<String>();
		roles.add(AbstractLesson.USER_ROLE);
		// Category 0 is the admin function. We want the first real category
		// to be returned. This is noramally the General category and the Http Basics lesson
		return ((AbstractLesson) getLessons((Category) getCategories().get(0), roles).get(0));
	}

	/**
	 * Gets the lesson attribute of the Course object
	 * 
	 * @param lessonId
	 *            Description of the Parameter
	 * @param role
	 *            Description of the Parameter
	 * @return The lesson value
	 */
	public AbstractLesson getLesson(WebSession s, int lessonId, List<String> roles)
	{
		if (s.isHackedAdmin())
		{
			roles.add(AbstractLesson.HACKED_ADMIN_ROLE);
		}
		// System.out.println("getLesson() with roles: " + roles);
		Iterator<AbstractLesson> iter = lessons.iterator();

		while (iter.hasNext())
		{
			AbstractLesson lesson = iter.next();

			// System.out.println("getLesson() at role: " + lesson.getRole());
			if (lesson.getScreenId() == lessonId && roles.contains(lesson.getRole())) { return lesson; }
		}

		return null;
	}

	public AbstractLesson getLesson(WebSession s, int lessonId, String role)
	{
		List<String> roles = new Vector<String>();
		roles.add(role);
		return getLesson(s, lessonId, roles);
	}

	public List getLessons(WebSession s, String role)
	{
		List<String> roles = new Vector<String>();
		roles.add(role);
		return getLessons(s, roles);
	}

	/**
	 * Gets the lessons attribute of the Course object
	 * 
	 * @param role
	 *            Description of the Parameter
	 * @return The lessons value
	 */
	public List<AbstractLesson> getLessons(WebSession s, List<String> roles)
	{
		if (s.isHackedAdmin())
		{
			roles.add(AbstractLesson.HACKED_ADMIN_ROLE);
		}
		List<AbstractLesson> lessonList = new ArrayList<AbstractLesson>();
		Iterator categoryIter = getCategories().iterator();

		while (categoryIter.hasNext())
		{
			lessonList.addAll(getLessons(s, (Category) categoryIter.next(), roles));
		}
		return lessonList;
	}

	/**
	 * Gets the lessons attribute of the Course object
	 * 
	 * @param category
	 *            Description of the Parameter
	 * @param role
	 *            Description of the Parameter
	 * @return The lessons value
	 */
	private List<AbstractLesson> getLessons(Category category, List roles)
	{
		List<AbstractLesson> lessonList = new ArrayList<AbstractLesson>();

		Iterator iter = lessons.iterator();
		while (iter.hasNext())
		{
			AbstractLesson lesson = (AbstractLesson) iter.next();

			if (lesson.getCategory().equals(category) && roles.contains(lesson.getRole()))
			{
				lessonList.add(lesson);
			}
		}

		Collections.sort(lessonList);
		// System.out.println(java.util.Arrays.asList(lessonList));
		return lessonList;
	}

	public List getLessons(WebSession s, Category category, String role)
	{
		List<String> roles = new Vector<String>();
		roles.add(role);
		return getLessons(s, category, roles);
	}

	public List<AbstractLesson> getLessons(WebSession s, Category category, List<String> roles)
	{
		if (s.isHackedAdmin())
		{
			roles.add(AbstractLesson.HACKED_ADMIN_ROLE);
		}
		return getLessons(category, roles);
	}

	/**
	 * Load all of the filenames into a temporary cache
	 * 
	 * @param context
	 * @param path
	 */
	private void loadFiles(ServletContext context, String path)
	{
		Set resourcePaths = context.getResourcePaths(path);
		Iterator itr = resourcePaths.iterator();

		while (itr.hasNext())
		{
			String file = (String) itr.next();

			if (file.length() != 1 && file.endsWith("/"))
			{
				loadFiles(context, file);
			}
			else
			{
				files.add(file);
			}
		}
	}

	/**
	 * Instantiate all the lesson objects into a cache
	 * 
	 * @param path
	 */
	private void loadLessons(String path)
	{
		Iterator itr = files.iterator();

		while (itr.hasNext())
		{
			String file = (String) itr.next();
			String className = getClassFile(file, path);

			if (className != null && !className.endsWith("_i"))
			{
				try
				{
					Class c = Class.forName(className);
					Object o = c.newInstance();

					if (o instanceof AbstractLesson)
					{
						AbstractLesson lesson = (AbstractLesson) o;
						lesson.setWebgoatContext(webgoatContext);

						lesson.update(properties);

						if (lesson.getHidden() == false)
						{
							lessons.add(lesson);
						}
					}
				} catch (Exception e)
				{
					// System.out.println("Warning: " + e.getMessage());
				}
			}
		}
	}

	private String getLanguageFromFileName(String first, String absoluteFile){
		int p1 = absoluteFile.indexOf("/",absoluteFile.indexOf(first)+1);
		int p2 = absoluteFile.indexOf("/",p1+1);
		String langStr=absoluteFile.substring(p1+1,p2);
		
		
		return new String(langStr);
	}
	
	/**
	 * For each lesson, set the source file and lesson file
	 */
	private void loadResources()
	{
		Iterator lessonItr = lessons.iterator();

		while (lessonItr.hasNext())
		{
			AbstractLesson lesson = (AbstractLesson) lessonItr.next();
			String className = lesson.getClass().getName();
			String classFile = getSourceFile(className);

			Iterator fileItr = files.iterator();

			while (fileItr.hasNext())
			{
				String absoluteFile = (String) fileItr.next();
				String fileName = getFileName(absoluteFile);
				// System.out.println("Course: looking at file: " + absoluteFile);

				if (absoluteFile.endsWith(classFile))
				{
					// System.out.println("Set source file for " + classFile);
					lesson.setSourceFileName(absoluteFile);
				}

				if (absoluteFile.startsWith("/lesson_plans") && absoluteFile.endsWith(".html")
						&& className.endsWith(fileName))
				{
					// System.out.println("DEBUG: setting lesson plan file " + absoluteFile + " for
					// lesson " +
					// lesson.getClass().getName());
					// System.out.println("fileName: " + fileName + " == className: " + className );
					String language = getLanguageFromFileName("/lesson_plans",absoluteFile);
					lesson.setLessonPlanFileName(language, absoluteFile);
					this.webgoatContext.getWebgoatI18N().loadLanguage(language);
				}
				if (absoluteFile.startsWith("/lesson_solutions") && absoluteFile.endsWith(".html")
						&& className.endsWith(fileName))
				{
					// System.out.println("DEBUG: setting lesson solution file " + absoluteFile + "
					// for lesson " +
					// lesson.getClass().getName());
					// System.out.println("fileName: " + fileName + " == className: " + className );
					lesson.setLessonSolutionFileName(absoluteFile);
				}
			}
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @param path
	 *            Description of the Parameter
	 * @param context
	 *            Description of the Parameter
	 */
	public void loadCourses(WebgoatContext webgoatContext, ServletContext context, String path)
	{
		this.webgoatContext = webgoatContext;
		loadFiles(context, path);
		loadLessons(path);
		loadResources();
	}
}
