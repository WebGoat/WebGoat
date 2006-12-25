package org.owasp.webgoat.lessons;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.sql.Statement;

import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.H2;
import org.apache.ecs.html.PRE;
import org.apache.ecs.html.Span;
import org.apache.ecs.html.Div;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.BR;
import org.owasp.webgoat.session.DatabaseUtilities;
import org.owasp.webgoat.session.WebSession;

public class BackDoors extends LessonAdapter {

	private static Connection connection = null;
	private final static Integer DEFAULT_RANKING = new Integer(80);
	private final static String USERNAME = "username";
	
	protected Element createContent( WebSession s )
	{
		return super.createStagedContent(s);
	}
	
	protected Element doStage1( WebSession s ) throws Exception
	{
		return concept1( s );
	}
	
	protected Element doStage2( WebSession s ) throws Exception
	{
		return concept2( s);
	}


	protected Element concept1( WebSession s) throws Exception
	{
		ElementContainer ec = new ElementContainer();
		
		ec.addElement( makeUsername(s));
		
		try
		{
			String userInput = s.getParser().getRawParameter(USERNAME, "");
			if (!userInput.equals(""))
			{
				String[] arrSQL = userInput.split(";");
				if (arrSQL.length == 2)
				{
					Connection conn = getConnection(s);
					Statement statement = conn.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
					statement.executeUpdate( arrSQL[1] );
					
					makeSuccess(s);
					getLessonTracker(s).setStage(2);
					s.setMessage("You have succeeded in exploiting the vulnerable query and created another SQL statement. Now move to stage 2 to learn how to create a backdoor or a DB worm");
				}
			}
		}
		catch(Exception ex)
		{
			ec.addElement( new PRE(ex.getMessage()) );
		}
		return ec;
	}

	protected Element concept2( WebSession s) throws Exception
	{
		ElementContainer ec = new ElementContainer();
		ec.addElement(makeUsername(s));
		
		String userInput = s.getParser().getRawParameter(USERNAME, "");
		
		if (!userInput.equals(""))
		{
			String[] arrSQL = userInput.split(";");
			if (arrSQL.length == 2)
			{
				if ( userInput.toUpperCase().indexOf("CREATE TRIGGER") != 0)
				{
					makeSuccess(s);					
				}
			}
			
		}
		return ec;
	}

	public String getInstructions(WebSession s)
	{
		String instructions = "";
		
		if (!getLessonTracker(s).getCompleted())
		{
			switch (getStage(s))
			{
			case 1:
				instructions = "Stage " + getStage(s) + ": Use String SQL Injection to execute more than one SQL Statement. ";
				instructions = instructions + " The first stage of this lesson is to teach you how to use a vulnerable field to create two SQL ";
				instructions = instructions + " statements. The first is the system's while the second is totally yours.";
				instructions = instructions + " Try to enter something in the email field and it will get updated in the rectangle below,";
				instructions = instructions + " to see the actual SQL statement that will be executed. Try to execute an update statement";
				break;
			case 2:
				instructions = "Stage " + getStage(s) + ": Use String SQL Injection to inject a backdoor. " ;
				instructions = instructions + " The second stage of this lesson is to teach you how to use a vulneable fields to inject the DB work or the backdoor." ;
				instructions = instructions + " Now try to use the same technique to inject a trigger that would act as " ;
				instructions = instructions + " SQL backdoor, the syntax of a trigger is: <br>";
				instructions = instructions + " CREATE TRIGGER myBackDoor BEFORE INSERT ON employee FOR EACH ROW BEGIN UPDATE employee SET email='john@hackme.com'WHERE userid = NEW.userid<br>";
				instructions = instructions + " Note that nothing will actually be executed because the current underlying DB doesn't support triggers.";
				break;
			}
		}
		
		return instructions;
	}
	protected Element makeUsername(WebSession s)
	{
		ElementContainer ec = new ElementContainer();
		StringBuffer script = new StringBuffer();
		script.append( "<STYLE TYPE=\"text/css\"> " );
		script.append( ".blocklabel { margin-top: 8pt; }" );
		script.append( ".myClass 	{ color:red;" );
		script.append( " font-weight: bold;" );
		script.append( "padding-left: 1px;" );
		script.append( "padding-right: 1px;" );
		script.append( "background: #DDDDDD;" );
		script.append( "border: thin black solid; }" );
		script.append( "LI	{ margin-top: 10pt; }" );
		script.append( "</STYLE>" );		
		ec.addElement( new StringElement(script.toString()));
			
		ec.addElement( new StringElement( "Username: " ) ) ;
		Input username = new Input( Input.TEXT, "username", "" );
		ec.addElement( username  );
				
		String userInput = s.getParser().getRawParameter("username" , "");

		ec.addElement(new BR());
		ec.addElement(new BR());
		
		String formattedInput = "<span class='myClass'>" + userInput + "</span>";
		ec.addElement( new Div("select userid, ssn, salary from employee where login=" + formattedInput ));
		
		Input b = new Input();
		
		b.setName("Submit");
		b.setType(Input.SUBMIT);
		b.setValue("Submit");
		
		ec.addElement(new PRE( b ) ); 	
		
		return ec;
	}

	public static synchronized Connection getConnection(WebSession s) 
	throws SQLException, ClassNotFoundException
	{
		if ( connection == null )
		{
			connection = DatabaseUtilities.makeConnection( s );
		}
		
		return connection;
	}
	
	public Element getCredits() {
		return new StringElement("Created by Sherif Koussa");
	}

	protected List getHints() {
		return super.getHints();
	}

	protected Category getDefaultCategory()
	{
		return AbstractLesson.A6;
	}

	protected Integer getDefaultRanking()
	{
		return DEFAULT_RANKING;
	}

	public String getTitle()
	{
		return ( "How to Use Database Backdoors " );
	}
}
