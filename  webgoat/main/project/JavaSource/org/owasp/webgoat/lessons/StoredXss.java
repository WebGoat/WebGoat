package org.owasp.webgoat.lessons;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.A;
import org.apache.ecs.html.B;
import org.apache.ecs.html.H1;
import org.apache.ecs.html.HR;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.P;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.apache.ecs.html.TextArea;
import org.owasp.webgoat.session.*;
import org.owasp.webgoat.util.HtmlEncoder;


/**
 *  Copyright (c) 2002 Free Software Foundation developed under the custody of the Open Web
 *  Application Security Project (http://www.owasp.org) This software package org.owasp.webgoat.is published by OWASP
 *  under the GPL. You should read and accept the LICENSE before you use, modify and/or redistribute
 *  this software.
 *
 * @author     Jeff Williams <a href="http://www.aspectsecurity.com">Aspect Security</a>
 * @created    October 28, 2003
 */
public class StoredXss extends LessonAdapter
{
	private final static String MESSAGE = "message";
	private final static int MESSAGE_COL = 3;
	private final static String NUMBER = "Num";
	private final static int NUM_COL = 1;
	private final static String STANDARD_QUERY = "SELECT * FROM messages";
	private final static String TITLE = "title";
	private final static int TITLE_COL = 2;
	private static Connection connection = null;
	private static int count = 1;
	private final static int USER_COL = 4;	// Added by Chuck Willis - used to show user who posted message


	/**
	 *  Adds a feature to the Message attribute of the MessageBoardScreen object
	 *
	 * @param  s  The feature to be added to the Message attribute
	 */
	protected void addMessage( WebSession s )
	{
		try
		{
			String title = HtmlEncoder.encode( s.getParser().getRawParameter( TITLE, "" ) );
			String message = s.getParser().getRawParameter( MESSAGE, "" );

			if ( connection == null )
			{
				connection = DatabaseUtilities.makeConnection( s );
			}

			String query = "INSERT INTO messages VALUES (?, ?, ?, ? )";

			PreparedStatement statement = connection.prepareStatement( query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
			statement.setInt(1, count++);
			statement.setString(2, title);
			statement.setString(3, message);
			statement.setString(4, s.getUserName());
			statement.executeQuery();
		}
		catch ( Exception e )
		{
			// ignore the empty resultset on the insert.  There are a few more SQL Injection errors
			// that could be trapped here but we will let them try.  One error would be something
			// like "Characters found after end of SQL statement." 
			if ( e.getMessage().indexOf("No ResultSet was produced") == -1 )
			{	
				s.setMessage( "Could not add message to database" );
			}
		}
	}

	/**
	 *  Description of the Method
	 *
	 * @param  s  Description of the Parameter
	 * @return    Description of the Return Value
	 */
	protected Element createContent( WebSession s )
	{
		addMessage( s );

		ElementContainer ec = new ElementContainer();
		ec.addElement( makeInput( s ) );
		ec.addElement( new HR() );
		ec.addElement( makeCurrent( s ) );
		ec.addElement( new HR() );
		ec.addElement( makeList( s ) );

		return ( ec );
	}


	/**
	 *  Gets the category attribute of the StoredXss object
	 *
	 * @return    The category value
	 */
	protected Category getDefaultCategory()
	{
		return AbstractLesson.A4;
	}


	/**
	 *  Gets the hints attribute of the MessageBoardScreen object
	 *
	 * @return    The hints value
	 */
	protected List getHints()
	{
		List<String> hints = new ArrayList<String>();
		hints.add( "You can put HTML tags in your message." );
		hints.add( "Bury a SCRIPT tag in the message to attack anyone who reads it." );
		hints.add( "Enter this: &lt;script language=\"javascript\" type=\"text/javascript\"&gt;alert(\"Ha Ha Ha\");&lt;/script&gt; in the message field." );
		hints.add( "Enter this: &lt;script&gtalert(\"document.cookie\");&lt;/script&gt; in the message field." );

		return hints;
	}




	private final static Integer DEFAULT_RANKING = new Integer(100);

	protected Integer getDefaultRanking()
	{
		return DEFAULT_RANKING;
	}

	/**
	 *  Gets the title attribute of the MessageBoardScreen object
	 *
	 * @return    The title value
	 */
	public String getTitle()
	{
		return ( "How to Perform Stored Cross Site Scripting (XSS)" );
	}


	/**
	 *  Description of the Method
	 *
	 * @param  s  Description of the Parameter
	 * @return    Description of the Return Value
	 */
	protected Element makeCurrent( WebSession s )
	{
		ElementContainer ec = new ElementContainer();

		try
		{
			int messageNum = s.getParser().getIntParameter( NUMBER, 0 );

			if ( connection == null )
			{
				connection = DatabaseUtilities.makeConnection( s );
			}

			// edit by Chuck Willis - Added logic to associate similar usernames
			// The idea is that users chuck-1, chuck-2, etc will see each other's messages
			// but not anyone elses.  This allows users to try out XSS to grab another user's
			// cookies, but not get confused by other users scripts
			
			String query = "SELECT * FROM messages WHERE user_name LIKE ? and num = ?";
			PreparedStatement statement = connection.prepareStatement( query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
			statement.setString(1, getNameroot( s.getUserName() ) + "%");
			statement.setInt(2, messageNum);
			ResultSet results = statement.executeQuery();

			if ( ( results != null ) && results.first() )
			{
				ec.addElement( new H1( "Message Contents For: " + results.getString( TITLE_COL )) );
				Table t = new Table( 0 ).setCellSpacing( 0 ).setCellPadding( 0 ).setBorder( 0 );
				TR row1 = new TR( new TD( new B(new StringElement( "Title:" )) ) );
				row1.addElement( new TD( new StringElement( results.getString( TITLE_COL ) ) ) );
				t.addElement( row1 );

				String messageData = results.getString( MESSAGE_COL );
				TR row2 = new TR( new TD( new B(new StringElement( "Message:" )) ) );
				row2.addElement( new TD( new StringElement( messageData ) ) );
				t.addElement( row2 );
				
				// Edited by Chuck Willis - added display of the user who posted the message, so that
				// if users use a cross site request forgery or XSS to make another user post a message,
				// they can see that the message is attributed to that user
								
				TR row3 = new TR( new TD( new StringElement( "Posted By:" ) ) );
				row3.addElement( new TD( new StringElement( results.getString( USER_COL ) ) ) );
				t.addElement( row3 );
								
				ec.addElement( t );
				
				// Some sanity checks that the script may be correct
				if ( messageData.toLowerCase().indexOf( "<script>" ) != -1  && 
				        messageData.toLowerCase().indexOf( "</script>" ) != -1 &&
				        messageData.toLowerCase().indexOf( "alert" ) != -1 )
				{
					makeSuccess( s );
				}

			}
			else
			{
				if ( messageNum != 0 )
				{
					ec.addElement( new P().addElement( "Could not find message " + messageNum ) );
				}
			}
		}
		catch ( Exception e )
		{
			s.setMessage( "Error generating " + this.getClass().getName() );
			e.printStackTrace();
		}

		return ( ec );
	}


	/**
	 *  Description of the Method
	 *
	 * @param  s  Description of the Parameter
	 * @return    Description of the Return Value
	 */
	protected Element makeInput( WebSession s )
	{
		Table t = new Table( 0 ).setCellSpacing( 0 ).setCellPadding( 0 ).setBorder( 0 );
		TR row1 = new TR();
		TR row2 = new TR();
		row1.addElement( new TD( new StringElement( "Title: " ) ) );

		Input inputTitle = new Input( Input.TEXT, TITLE, "" );
		row1.addElement( new TD( inputTitle ) );

		TD item1 = new TD();
		item1.setVAlign( "TOP" );
		item1.addElement( new StringElement( "Message: " ) );
		row2.addElement( item1 );

		TD item2 = new TD();
		TextArea ta = new TextArea( MESSAGE, 5, 60 );
		item2.addElement( ta );
		row2.addElement( item2 );
		t.addElement( row1 );
		t.addElement( row2 );

		Element b = ECSFactory.makeButton( "Submit" );
		ElementContainer ec = new ElementContainer();
		ec.addElement( t );
		ec.addElement( new P().addElement( b ) );

		return ( ec );
	}


	/**
	 *  Description of the Method
	 *
	 * @param  s  Description of the Parameter
	 * @return    Description of the Return Value
	 */
	public static Element makeList( WebSession s )
	{
		Table t = new Table( 0 ).setCellSpacing( 0 ).setCellPadding( 0 ).setBorder( 0 );

		try
		{
			if ( connection == null )
			{
				connection = DatabaseUtilities.makeConnection( s );
			}

			Statement statement = connection.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
			// edit by Chuck Willis - Added logic to associate similar usernames
			// The idea is that users chuck-1, chuck-2, etc will see each other's messages
			// but not anyone elses.  This allows users to try out XSS to grab another user's
			// cookies, but not get confused by other users scripts
			
			ResultSet results = statement.executeQuery( STANDARD_QUERY + " WHERE user_name LIKE '" + getNameroot( s.getUserName() ) + "%'" );

			if ( ( results != null ) && ( results.first() == true ) )
			{
				results.beforeFirst();

				for ( int i = 0; results.next(); i++ )
				{
					A a = ECSFactory.makeLink( results.getString( TITLE_COL ), NUMBER, results.getInt( NUM_COL ) );
					TD td = new TD().addElement( a );
					TR tr = new TR().addElement( td );
					t.addElement( tr );
				}
			}
		}
		catch ( Exception e )
		{
			s.setMessage( "Error while getting message list." );
		}

		ElementContainer ec = new ElementContainer();
		ec.addElement( new H1( "Message List" ) );
		ec.addElement( t );

		return ( ec );
	}
	
	private static String getNameroot( String name )
	{
		String nameroot = name;
		if (nameroot.indexOf('-') != -1) 
		{
			nameroot = nameroot.substring(0, nameroot.indexOf('-')); 
		}
		return nameroot;
	}
}

