
package org.owasp.webgoat.session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


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
 * @since October 29, 2003
 * @version $Id: $Id
 */
public class UserTracker
{

	private static UserTracker instance;

	// FIXME: persist this somehow!

	private static HashMap<String, HashMap<String, LessonTracker>> storage = new HashMap<String, HashMap<String, LessonTracker>>();

	private static UserDatabase usersDB = new UserDatabase();

	/**
	 * Constructor for the UserTracker object
	 */
	private UserTracker()
	{
	}

	/**
	 * Gets the completed attribute of the UserTracker object
	 *
	 * @param userName
	 *            Description of the Parameter
	 * @return The completed value
	 */
	public int getCompleted(String userName)
	{

		HashMap usermap = getUserMap(userName);

		Iterator i = usermap.entrySet().iterator();

		int count = 0;

		while (i.hasNext())
		{

			Map.Entry entry = (Map.Entry) i.next();

			int value = ((Integer) entry.getValue()).intValue();

			if (value > 5)
			{
				count++;
			}

		}

		return count;
	}

	/**
	 * Gets the users attribute of the UserTracker object
	 *
	 * @return The users value
	 */
	public Collection getUsers()
	{
		return storage.keySet();
	}

	/**
	 * <p>getAllUsers.</p>
	 *
	 * @param roleName a {@link java.lang.String} object.
	 * @return a {@link java.util.Collection} object.
	 */
	public Collection<String> getAllUsers(String roleName)
	{
		synchronized (usersDB)
		{
			Collection<String> allUsers = new ArrayList<String>();
			try
			{
				usersDB.open();
				Iterator users = usersDB.getUsers();
				while (users.hasNext())
				{
					User user = (User) users.next();
					Iterator roles = user.getRoles();
					while (roles.hasNext())
					{
						Role role = (Role) roles.next();
						if (role.getRolename().trim().equals(roleName))
						{
							allUsers.add(user.getUsername());
						}
					}
				}
				usersDB.close();
			} catch (Exception e)
			{
			}
			return allUsers;
		}
	}

	/**
	 * <p>deleteUser.</p>
	 *
	 * @param user a {@link java.lang.String} object.
	 */
	public void deleteUser(String user)
	{
		synchronized (usersDB)
		{
			try
			{
				usersDB.open();
				Iterator users = usersDB.getUsers();
				while (users.hasNext())
				{
					User tomcatUser = (User) users.next();
					if (tomcatUser.getUsername().equals(user))
					{
						usersDB.removeUser(tomcatUser);
						// FIXME: delete all the lesson tracking property files
						break;
					}
				}
				usersDB.close();

			} catch (Exception e)
			{
			}
		}
	}

	/**
	 * Gets the lessonTracker attribute of the UserTracker object
	 *
	 * @param screen
	 *            Description of the Parameter
	 * @return The lessonTracker value
	 * @param screen
	 *            Description of the Parameter
	 * @param s a {@link org.owasp.webgoat.session.WebSession} object.
	 */
	public LessonTracker getLessonTracker(WebSession s, Screen screen)
	{
		return getLessonTracker(s, s.getUserName(), screen);
	}

	/**
	 * <p>getLessonTracker.</p>
	 *
	 * @param s a {@link org.owasp.webgoat.session.WebSession} object.
	 * @param screen a {@link org.owasp.webgoat.session.Screen} object.
	 * @param user a {@link java.lang.String} object.
	 * @param screen a {@link org.owasp.webgoat.session.Screen} object.
	 * @return a {@link org.owasp.webgoat.session.LessonTracker} object.
	 */
	public LessonTracker getLessonTracker(WebSession s, String user, Screen screen)
	{
		HashMap<String, LessonTracker> usermap = getUserMap(user);
		LessonTracker tracker = (LessonTracker) usermap.get(screen.getTitle());
		if (tracker == null)
		{
			// Creates a new lesson tracker, if one does not exist on disk.
			tracker = LessonTracker.load(s, user, screen);
			usermap.put(screen.getTitle(), tracker);
		}
		// System.out.println( "User: [" + userName + "] UserTracker:getLessonTracker() LTH " +
		// tracker.hashCode() + " for " + screen );
		return tracker;
	}

	/**
	 * Gets the status attribute of the UserTracker object
	 *
	 * @param screen
	 *            Description of the Parameter
	 * @return The status value
	 * @param screen
	 *            Description of the Parameter
	 * @param s a {@link org.owasp.webgoat.session.WebSession} object.
	 */
	public String getStatus(WebSession s, Screen screen)
	{
		return ("User [" + s.getUserName() + "] has accessed " + screen + " UserTracker:getStatus()LTH = " + getLessonTracker(
																																s,
																																screen)
				.hashCode());
	}

	/**
	 * Gets the userMap attribute of the UserTracker object
	 * 
	 * @param userName
	 *            Description of the Parameter
	 * @return The userMap value
	 */
	private HashMap<String, LessonTracker> getUserMap(String userName)
	{

		HashMap<String, LessonTracker> usermap = storage.get(userName);

		if (usermap == null)
		{

			usermap = new HashMap<String, LessonTracker>();

			storage.put(userName, usermap);

		}

		return (usermap);
	}

	/**
	 * Description of the Method
	 *
	 * @return Description of the Return Value
	 */
	public static synchronized UserTracker instance()
	{

		if (instance == null)
		{

			instance = new UserTracker();

		}

		return instance;
	}

	/**
	 * Description of the Method
	 *
	 * @param screen
	 *            Description of the Parameter
	 * @param screen
	 *            Description of the Parameter
	 * @param screen
	 *            Description of the Parameter
	 * @param screen
	 *            Description of the Parameter
	 * @param screen
	 *            Description of the Parameter
	 * @param screen
	 *            Description of the Parameter
	 * @param s
	 *            Description of the Parameter
	 */
	public void update(WebSession s, Screen screen)
	{

		LessonTracker tracker = getLessonTracker(s, screen);

		// System.out.println( "User [" + s.getUserName() + "] TRACKER: updating " + screen +
		// " LTH " + tracker.hashCode() );
		tracker.store(s, screen);

		HashMap<String, LessonTracker> usermap = getUserMap(s.getUserName());
		usermap.put(screen.getTitle(), tracker);

	}

}
