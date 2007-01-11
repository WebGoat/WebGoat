package org.owasp.webgoat.lessons;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.BR;
import org.apache.ecs.html.Div;
import org.apache.ecs.html.Form;
import org.apache.ecs.html.H1;
import org.apache.ecs.html.H3;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.PRE;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.owasp.webgoat.session.WebSession;

public class SilentTransactions extends LessonAdapter {
	private final static Integer DEFAULT_RANKING = new Integer(40);
	private final static Double CURRENT_BALANCE = 11987.09;
	/**
	 *  Copyright (c) 2002 Free Software Foundation developed under the 
	 *  custody of the Open Web Application Security Project 
	 *  (http://www.owasp.org) This software package is published by OWASP
	 *  under the GPL. You should read and accept the LICENSE before you 
	 *  use, modify and/or redistribute this software.
	 *
	 * @author     sherif@macadamian.com
	 * @created    December 26, 2006
	 */

	public void handleRequest(WebSession s) {
		
		try 
		{
			if(s.getParser().getRawParameter("from", "").equals("ajax"))
			{
				if (s.getParser().getRawParameter( "confirm", "").equals("Confirm"))
				{
					String amount = s.getParser().getRawParameter( "amount", "");
					s.getResponse().setContentType("text/html");
					s.getResponse().setHeader("Cache-Control", "no-cache");
					PrintWriter out = new PrintWriter(s.getResponse().getOutputStream());
					StringBuffer result =  new StringBuffer();
					result.append("<br><br>* Congratulations. You have successfully completed this lesson.<br>");
					if (!amount.equals(""))
					{
						result.append("You have just silently authorized ");
						result.append(amount);
						result.append("$ without the user interaction.<br>");
					}
					result.append("Now you can send out a spam email containing this link and whoever clicks on it<br>");
					result.append(" and happens to be logged in the same time will loose their money !!");
					out.print(result.toString());	
					out.flush();
					out.close();
					getLessonTracker(s).setCompleted(true);
					return;
				}
				else if (s.getParser().getRawParameter( "confirm", "").equals("Transferring"))
				{
					s.getResponse().setContentType("text/html");
					s.getResponse().setHeader("Cache-Control", "no-cache");
					PrintWriter out = new PrintWriter(s.getResponse().getOutputStream());
					out.print("<br><br>The Transaction has Completed Successfully.");	
					out.flush();
					out.close();
					return;
				}
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		
		Form form = new Form( getFormAction(), Form.POST ).setName( "form" ).setEncType( "" );

		form.addElement( createContent( s ) );

        setContent(form);

	}
	
	/**
	 * Description of the Method
	 * 
	 * @param s Current WebSession
	 */
	
	protected Element createContent(WebSession s)
	{
		ElementContainer ec = new ElementContainer();
		String lineSep = System.getProperty("line.separator");
		String script  =  "<script>" + lineSep +
			"function processData(){" + lineSep +
			" var accountNo = document.getElementById('newAccount').value;" + lineSep +
			" var amount = document.getElementById('amount').value;" + lineSep +
			" if ( accountNo == ''){" + lineSep +
			" alert('Please enter a valid account number to transfer to.')" + lineSep +
			" return;" + lineSep +
			"}" + lineSep +
			" else if ( amount == ''){" + lineSep +
			" alert('Please enter a valid amount to transfer.')" + lineSep +
			" return;" + lineSep +
			"}" + lineSep +
			" var balanceValue = document.getElementById('balanceID').innerText;" + lineSep +
			" balanceValue = balanceValue.replace( new RegExp('$') , '');" + lineSep +
			" if ( parseFloat(amount) > parseFloat(balanceValue) ) {" + lineSep +
			" alert('You can not transfer more funds than what is available in your balance.')" + lineSep +
			" return;" + lineSep +
			"}" + lineSep +
			" document.getElementById('confirm').value  = 'Transferring'" + lineSep +
			"submitData(accountNo, amount);" + lineSep +
			" document.getElementById('confirm').value  = 'Confirm'" + lineSep +
			"balanceValue = parseFloat(balanceValue) - parseFloat(amount);" + lineSep +
			"balanceValue = balanceValue.toFixed(2);" + lineSep +
			"document.getElementById('balanceID').innerText = balanceValue + '$';" + lineSep +
			"}" + lineSep +
			"function submitData(accountNo, balance) {" + lineSep +
			"var url = '/WebGoat/attack?Screen=" + String.valueOf(getScreenId()) +
			"&menu=" + getDefaultCategory().getRanking().toString() +
			"&from=ajax&newAccount='+ accountNo+ '&amount=' + balance +'&confirm=' + document.getElementById('confirm').value; " + lineSep +
			"if (typeof XMLHttpRequest != 'undefined') {" + lineSep +
			"req = new XMLHttpRequest();" + lineSep +
			"} else if (window.ActiveXObject) {" + lineSep +
			"req = new ActiveXObject('Microsoft.XMLHTTP');" + lineSep +
			"   }" + lineSep +
			"   req.open('GET', url, true);" + lineSep +
			"   req.onreadystatechange = callback;" + lineSep +
			"   req.send(null);" + lineSep +
			"}" + lineSep +
			"function callback() {" + lineSep +
			"    if (req.readyState == 4) { " + lineSep +
			"        if (req.status == 200) { " + lineSep +
			"                   var result =  req.responseText ;" + lineSep +
			"			 var resultsDiv = document.getElementById('resultsDiv');" + lineSep +
			"				resultsDiv.innerHTML = '';" + lineSep +
			"				resultsDiv.innerHTML = result;" + lineSep +			
			"        }}}" + lineSep +
			"</script>" + lineSep;
		
		ec.addElement( new StringElement(script) );	
		ec.addElement( new H1("Welcome to WebGoat Banking System"));
		ec.addElement( new BR() );
		ec.addElement( new H3("Account Summary:"));
		
		Table t1 = new Table().setCellSpacing(0).setCellPadding(0).setBorder(1).setWidth("70%").setAlign("left");
		ec.addElement( new BR() );
		TR tr = new TR();			
		tr.addElement( new TD( new StringElement( "Account Balance:" ) ));
		tr.addElement( new TD( new StringElement( "<div id='balanceID'>" + CURRENT_BALANCE.toString() + "$</div>") ));
		t1.addElement( tr );
		
		tr = new TR();
		tr.addElement( new TD( new StringElement( "Transfer to Account:" )));
		Input newAccount = new Input();
		newAccount.setType( Input.TEXT );
		newAccount.setName( "newAccount" );
		newAccount.setValue( "" );
		tr.addElement( new TD( newAccount ));
		t1.addElement( tr );
		
		tr = new TR();
		tr.addElement( new TD( new StringElement( "Transfer Amount:" )));
		Input amount = new Input();
		amount.setType( Input.TEXT );
		amount.setName( "amount" );
		amount.setValue( 0 );
		tr.addElement( new TD( amount ));
		t1.addElement( tr );
		
		ec.addElement( t1 );
		ec.addElement( new BR() );
		ec.addElement( new BR() );
		
		ec.addElement( new PRE() );
		Input b = new Input();
		b.setType( Input.BUTTON );
		b.setName( "confirm" );
		b.setValue( "Confirm" );
		b.setOnClick( "processData();" );
		ec.addElement( b );
		
		ec.addElement( new BR());
		Div div = new Div();
		div.addAttribute("name", "resultsDiv");
		div.addAttribute("id", "resultsDiv");
		div.setStyle("font-weight: bold;color:red;");
		ec.addElement(div);
		
		return ec;
	}
				
	protected Category getDefaultCategory()
	{
		return AbstractLesson.AJAX_SECURITY;
	}

	protected List getHints()
	{
		List<String> hints = new ArrayList<String>();
		hints.add("Check the javascript in the HTML source.");
		hints.add("Check how the application calls a specific javascript function to execute the transaction.");
		hints.add("Check the javascript functions processData and submitData()");
		hints.add("Function submitData() is the one responsible for actually ececuting the transaction.");
		hints.add("Check if your browser supports running javascript from the address bar.");
		hints.add("Try to navigate to 'javascript:submitData(1234556,11000);'");
		return hints;
	
	}

	protected Integer getDefaultRanking()
	{
		return DEFAULT_RANKING;
	}

	/**
	 *  Gets the title attribute of the HelloScreen object
	 *
	 * @return    The title value
	 */
	public String getTitle()
	{
		return ( "Silent Transactions Attacks" );
	}

	public Element getCredits() {
		return new StringElement("Created by Sherif Koussa");
	}

}
