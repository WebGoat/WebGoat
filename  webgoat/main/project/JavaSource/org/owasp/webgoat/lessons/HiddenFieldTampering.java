package org.owasp.webgoat.lessons;

import java.util.ArrayList;
import java.util.List;

import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.html.B;
import org.apache.ecs.html.BR;
import org.apache.ecs.html.Center;
import org.apache.ecs.html.H1;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.P;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TH;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.owasp.webgoat.session.ECSFactory;
import org.owasp.webgoat.session.WebSession;


/**
 *  Copyright (c) 2002 Free Software Foundation developed under the custody of the Open Web
 *  Application Security Project (http://www.owasp.org) This software package org.owasp.webgoat.is published by OWASP
 *  under the GPL. You should read and accept the LICENSE before you use, modify and/or redistribute
 *  this software.
 *
 * @author     Jeff Williams <a href="http://www.aspectsecurity.com">Aspect Security</a>
 * @created    October 28, 2003
 */
public class HiddenFieldTampering extends LessonAdapter
{
	private final static String PRICE = "Price";

	private final static String PRICE_TV = "2999.99";
	
	private final static String PRICE_TV_HACKED = "9.99";	

	/**
	 *  Constructor for the HiddenFieldScreen object
	 */
	public HiddenFieldTampering() { }

	/**
	 *  Description of the Method
	 *
	 * @param  s  Description of the Parameter
	 * @return    Description of the Return Value
	 */
	protected Element createContent( WebSession s )
	{
		ElementContainer ec = new ElementContainer();

		try
		{
			String price = s.getParser().getRawParameter( PRICE, PRICE_TV );
			float quantity = s.getParser().getFloatParameter("QTY", 1.0f);
			float total = quantity * Float.parseFloat(price);
			
			if ( price.equals(PRICE_TV)  )
			{
				ec.addElement( new Center().addElement( new H1().addElement( "Shopping Cart " )));
                 ec.addElement( new BR() );
				Table t = new Table().setCellSpacing( 0 ).setCellPadding( 2 ).setBorder( 1 ).setWidth("90%").setAlign("center");

				if ( s.isColor() )
				{
					t.setBorder( 1 );
				}
				
				TR tr = new TR();
				tr.addElement( new TH().addElement("Shopping Cart Items -- To Buy Now").setWidth("80%"));
				tr.addElement( new TH().addElement("Price:").setWidth("10%"));
				tr.addElement( new TH().addElement("Quantity:").setWidth("3%"));
				tr.addElement( new TH().addElement("Total").setWidth("7%"));
				t.addElement( tr );
				
				tr = new TR();
				tr.addElement( new TD().addElement("56 inch HDTV (model KTV-551)"));
				tr.addElement( new TD().addElement(PRICE_TV).setAlign("right"));
				tr.addElement( new TD().addElement(new Input( Input.TEXT, "QTY", 1 )).setAlign( "right" ));
				tr.addElement( new TD().addElement("$" +total));
				t.addElement( tr );
				
				ec.addElement(t);
				
				t = new Table().setCellSpacing( 0 ).setCellPadding( 2 ).setBorder( 0 ).setWidth("90%").setAlign("center");

				if ( s.isColor() )
				{
					t.setBorder( 1 );
				}
				
				ec.addElement( new BR() );
				tr = new TR();
				tr.addElement( new TD().addElement( "The total charged to your credit card:" ) );
				tr.addElement( new TD().addElement( "$" + total ));
				tr.addElement( new TD().addElement( ECSFactory.makeButton( "Update Cart" )));
				tr.addElement( new TD().addElement( ECSFactory.makeButton( "Purchase" )));
				t.addElement( tr );
				
				ec.addElement(t);

				Input input = new Input( Input.HIDDEN, PRICE, PRICE_TV );
				ec.addElement( input );
				ec.addElement( new BR() );

			}
			else
			{
				if ( !price.toString().equals( PRICE_TV ) )
				{
					makeSuccess( s );
				}

				ec.addElement( new P().addElement( "Your total price is:" ) );
				ec.addElement( new B( "$" + total ) );
				ec.addElement( new BR() );
				ec.addElement( new P().addElement( "This amount will be charged to your credit card immediately." ) );
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
	 *  DOCUMENT ME!
	 *
	 * @return    DOCUMENT ME!
	 */
	protected Category getDefaultCategory()
	{
		return AbstractLesson.A1;
	}


	/**
	 *  Gets the hints attribute of the HiddenFieldScreen object
	 *
	 * @return    The hints value
	 */
	protected List getHints()
	{
		List<String> hints = new ArrayList<String>();
		hints.add( "This application is using hidden fields to transmit price information to the server." );
		hints.add( "Use a program to intercept and change the value in the hidden field." );
		hints.add( "Use <A href=\"http://www.owasp.org/development/webscarab\">WebScarab</A> to change the price of the TV from " + 
				PRICE_TV + " to " + PRICE_TV_HACKED + "." );

		return hints;
	}


	/**
	 *  Gets the instructions attribute of the HiddenFieldTampering object
	 *
	 * @return    The instructions value
	 */
	public String getInstructions(WebSession s)
	{
		String instructions = "Try to purchase the HDTV for less than the purchase price, if you have not done so already.";

		return ( instructions );
	}




	private final static Integer DEFAULT_RANKING = new Integer(50);

	protected Integer getDefaultRanking()
	{
		return DEFAULT_RANKING;
	}

	/**
	 *  Gets the title attribute of the HiddenFieldScreen object
	 *
	 * @return    The title value
	 */
	public String getTitle()
	{
		return ( "How to Exploit Hidden Fields" );
	}
}

