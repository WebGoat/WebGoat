package org.owasp.webgoat.session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.catalina.Role;
import org.apache.catalina.User;
import org.apache.catalina.users.MemoryUserDatabase;
/**
 *  Copyright (c) 2002 Free Software Foundation developed under the custody of the Open Web
 *  Application Security Project (http://www.owasp.org) This software package org.owasp.webgoat.is published by OWASP
 *  under the GPL. You should read and accept the LICENSE before you use, modify and/or redistribute
 *  this software.
 *
 * @author     Jeff Williams <a href="http://www.aspectsecurity.com">Aspect Security</a>
 * @created    October 29, 2003
 */

public class UserTracker
{

	private static UserTracker instance;

	// FIXME: persist this somehow!

	private static HashMap storage = new HashMap();

	private static MemoryUserDatabase usersDB = new MemoryUserDatabase();


	/**
	 *  Constructor for the UserTracker object
	 */
	private UserTracker() { }



	/**
	 *  Gets the completed attribute of the UserTracker object
	 *
	 * @param  userName  Description of the Parameter
	 * @return           The completed value
	 */
	public int getCompleted( String userName )
	{

		HashMap usermap = getUserMap( userName );

		Iterator i = usermap.entrySet().iterator();

		int count = 0;

		while ( i.hasNext() )
		{

			Map.Entry entry = (Map.Entry) i.next();

			int value = ( (Integer) entry.getValue() ).intValue();

			if ( value > 5 )
			{
				count++;
			}

		}

		return count;
	}


	/**
	 *  Gets the users attribute of the UserTracker object
	 *
	 * @return    The users value
	 */
	public Collection getUsers()
	{
		return storage.keySet();
	}

	public Collection getAllUsers(String roleName)
	{
		synchronized ( usersDB ) {
			Collection allUsers = new ArrayList();
			try { 
				usersDB.open();
				Iterator users = usersDB.getUsers();
				while (users.hasNext()) 
				{
					User user = (User) users.next();
					Iterator roles = user.getRoles();
					while( roles.hasNext() )
					{	
						Role role = (Role)roles.next();
						if ( role.getRolename().trim().equals(roleName)) 
						{	 	
							allUsers.add( user.getUsername() );
						}
					}
				}
				usersDB.close();
			}
			catch ( Exception e )
			{}
			return allUsers;
		}
	}

	public void deleteUser( String user )
	{
		synchronized ( usersDB ) {
			try
			{
				usersDB.open();
				Iterator users = usersDB.getUsers();
				while (users.hasNext()) 
				{
					User tomcatUser = (User) users.next();
					if ( tomcatUser.getUsername().equals( user ) )
					{	 	
						usersDB.removeUser(tomcatUser);
						// FIXME: delete all the lesson tracking property files
						break;
					}
				}
				usersDB.close();
				
			}
			catch ( Exception e )
			{}
		}
	}

	/**
	 *  Gets the lessonTracker attribute of the UserTracker object
	 *
	 * @param  screen    Description of the Parameter
	 * @param  userName  Description of the Parameter
	 * @return           The lessonTracker value
	 */
	public LessonTracker getLessonTracker( WebSession s, Screen screen )
	{
		return getLessonTracker(s, s.getUserName(), screen );
	}

	public LessonTracker getLessonTracker( WebSession s, String user, Screen screen )
	{
		HashMap usermap = getUserMap( user );
		LessonTracker tracker = (LessonTracker) usermap.get( screen.getTitle() );
		if ( tracker == null )
		{
			// Creates a new lesson tracker, if one does not exist on disk.
			tracker = LessonTracker.load( s, user, screen );
			usermap.put( screen.getTitle(), tracker );
		}
		//System.out.println( "User: [" + userName + "] UserTracker:getLessonTracker() LTH " + tracker.hashCode() + " for " + screen );
		return tracker;
	}


	/**
	 *  Gets the status attribute of the UserTracker object
	 *
	 * @param  screen    Description of the Parameter
	 * @param  userName  Description of the Parameter
	 * @return           The status value
	 */
	public String getStatus( WebSession s, Screen screen )
	{
		return ( "User [" + s.getUserName() + "] has accessed " + screen + " UserTracker:getStatus()LTH = " + getLessonTracker( s, screen ).hashCode() );
	}



	/**
	 *  Gets the userMap attribute of the UserTracker object
	 *
	 * @param  userName  Description of the Parameter
	 * @return           The userMap value
	 */
	private HashMap getUserMap( String userName )
	{

		HashMap usermap = (HashMap) storage.get( userName );

		if ( usermap == null )
		{

			usermap = new HashMap();

			storage.put( userName, usermap );

		}

		return ( usermap );
	}



	/**
	 *  Description of the Method
	 *
	 * @return    Description of the Return Value
	 */
	public static synchronized UserTracker instance()
	{

		if ( instance == null )
		{

			instance = new UserTracker();

		}

		return instance;
	}



	/**
	 *  Description of the Method
	 *
	 * @param  screen  Description of the Parameter
	 * @param  s       Description of the Parameter
	 */
	public void update( WebSession s, Screen screen )
	{

		LessonTracker tracker = getLessonTracker( s, screen );

		//System.out.println( "User [" + s.getUserName() + "] TRACKER: updating " + screen + " LTH " + tracker.hashCode() );
		tracker.store( s, screen );

		HashMap usermap = getUserMap( s.getUserName() );
		usermap.put( screen.getTitle(), tracker );

	}

}

