package org.owasp.webgoat.lessons;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.BR;
import org.apache.ecs.html.H1;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.owasp.webgoat.session.WebSession;

public class DOMInjection extends LessonAdapter {

	private final static Integer DEFAULT_RANKING = new Integer(10);
	private final static String KEY = "key";
	
	protected Element createContent(WebSession s) {
		
		String key = "K1JFWP8BSO8HI52LNPQS8F5L01N";
		ElementContainer ec = new ElementContainer();

		try
		{
			String userKey = s.getParser().getRawParameter(KEY, "");
			String fromAJAX = s.getParser().getRawParameter("from" , "");
			if (fromAJAX.equalsIgnoreCase("ajax") && userKey.length()!= 0 && userKey.equals(key))
			{
				s.getResponse().setContentType("text/html");
				s.getResponse().setHeader("Cache-Control", "no-cache");
				PrintWriter out = new PrintWriter(s.getResponse().getOutputStream());
				out.print("document.forms[0].SUBMIT.disabled = false;");	
				out.flush();
				out.close();
				return ec;
			}
			if (s.getRequest().getMethod().equalsIgnoreCase("POST"))
			{
				makeSuccess(s);
			}
		}
		catch(Exception e)
		{
			s.setMessage( "Error generating " + this.getClass().getName() );
			e.printStackTrace();				
		}
		
		String lineSep = System.getProperty("line.separator");
		String script  =  "<script>" + lineSep +
			"function validate() {" + lineSep +
			"var keyField = document.getElementById('key');" + lineSep +
			"var url = '/WebGoat/attack?Screen=" + String.valueOf(getScreenId()) +
			"&menu=" + getDefaultCategory().getRanking().toString() +
			"&from=ajax&key=' + encodeURIComponent(keyField.value);" + lineSep +	        
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
			"            var message = req.responseText;" + lineSep +
			"			 eval(message);" + lineSep +	
			"        }}}" + lineSep +
			"</script>" + lineSep;
		
		ec.addElement( new StringElement(script));
		ec.addElement( new BR().addElement (new H1().addElement( "Welcome to WebGoat Registration Page:")));
		ec.addElement( new BR().addElement ("Please enter the license key that was emailed to you to start using the application."));
		ec.addElement( new BR());
		ec.addElement( new BR());
		Table t1 = new Table().setCellSpacing(0).setCellPadding(0).setBorder(0).setWidth("70%").setAlign("center");
		
		TR tr = new TR();			
		tr.addElement( new TD( new StringElement( "License Key: " ) ));		
		
		Input input1 = new Input( Input.TEXT, KEY , "" );
		input1.addAttribute("onkeyup", "validate();");
		tr.addElement( new TD( input1 ) );
		t1.addElement( tr );

		tr = new TR();
		tr.addElement( new TD( "&nbsp;" ).setColSpan(2));
		
		t1.addElement( tr );
		
		tr = new TR();
		Input b = new Input();
		b.setType( Input.SUBMIT );
		b.setValue( "Activate!" );
		b.setName("SUBMIT");
		b.setDisabled(true);
		tr.addElement(new TD( "&nbsp;" ));
		tr.addElement( new TD( b ) );
		
		t1.addElement(tr);
		ec.addElement( t1 );		
		
		
		return ec ;
	}

	public Element getCredits() {
		return new StringElement("Created by Sherif Koussa");
	}

	protected Category getDefaultCategory() {
		
		return AJAX_SECURITY;
	}

	protected Integer getDefaultRanking() {
		
		return DEFAULT_RANKING;
	}

	protected List getHints() {
		
		List<String> hints = new ArrayList<String>();
		hints.add( "This page is using XMLHTTP to comunicate with the server." );
		hints.add( "Try to find a way to inject the DOM to enable the Activate button." );
		hints.add( "Intercept the reply and replace the body withx document.forms[0].SUBMIT.disabled = false;" );
		return hints;
	}

	public String getTitle() {
		return "DOM Injection";
	}

}
