package org.owasp.webgoat.lessons;

import java.util.ArrayList;
import java.util.List;

import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.P;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.apache.ecs.html.TextArea;
import org.owasp.webgoat.session.ECSFactory;
import org.owasp.webgoat.session.WebSession;

public class CSRF extends LessonAdapter {

	private final static String MESSAGE = "message";
	private final static String TITLE = "title";
	
	@Override
	protected Element createContent(WebSession s) {
		ElementContainer ec = new ElementContainer();
		String emailBody = null;
		
		try{
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
			ec = new ElementContainer();
			ec.addElement( t );
			ec.addElement( new P().addElement( b ) );
			
			emailBody = new String( s.getParser().getRawParameter( MESSAGE, "" ) );
									
		}
		catch (Exception e)
		{
			s.setMessage( "Error generating " + this.getClass().getName() );
			e.printStackTrace();			
		}
		
		if (emailBody.length() != 0 && 
				emailBody.indexOf( "<img" ) >=0 &&
				emailBody.indexOf( "src=") > 0 &&
				emailBody.indexOf( "height=\"1\"" ) > 0 &&
				emailBody.indexOf( "width=\"1\"" ) > 0)
		{
			makeSuccess( s );
		}
		
		return ec;
	}

	@Override	
	protected Category getDefaultCategory() {
		return AbstractLesson.A4;
	}

	private final static Integer DEFAULT_RANKING = new Integer(140);
	
	@Override
	protected Integer getDefaultRanking() {
		
		return DEFAULT_RANKING;
	}

	@Override	
	protected List getHints() {
		List<String> hints = new ArrayList<String>();
		hints.add( "Enter some text and try to include an image in there." );
		hints.add( "In order to make the picture almost invisible try to add width=\"1\" and height=\"1\"." );
		hints.add( "The format of an image in html is <pre>&lt;img src=\"[URL]\" width=\"1\" height=\"1\" /&gt;</pre>");		

		return hints;
	}

	/**
	 *  Gets the title attribute of the MessageBoardScreen object
	 *
	 * @return    The title value
	 */
	public String getTitle()
	{
		return ( "How to Perform Cross Site Request Forgery (CSRF)" );
	}

}
