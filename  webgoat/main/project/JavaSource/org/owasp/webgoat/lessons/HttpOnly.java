package org.owasp.webgoat.lessons;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.security.MessageDigest;

import javax.servlet.http.HttpServletResponse;

import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.Form;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.owasp.webgoat.session.WebSession;

import sun.misc.BASE64Encoder;

public class HttpOnly extends LessonAdapter {
	
	private final static Integer DEFAULT_RANKING = new Integer(125);
	
	private final static String UNIQUE2U = "unique2u";
	
	private final static String HTTPONLY = "httponly";
	
	private final static String ACTION = "action";
	
	private final static String READ = "Read Cookie";
	
	private final static String WRITE = "Write Cookie";
	
	private final static String READ_RESULT = "read_result";
	
	private boolean httpOnly = false;
	
	private boolean readSuccess = false;
	
	private boolean writeSuccess = false;
	
	private String original = "undefined";
	
	/**
	 *  Gets the title attribute of the EmailScreen object
	 *
	 * @return    The title value
	 */
	public String getTitle()
	{
		return ( "HTTPOnly Test" );
	}

	protected Integer getDefaultRanking()
	{
		return DEFAULT_RANKING;
	}
	
	/**
	 *  Description of the Method
	 *
	 * @param  s  Description of the Parameter
	 * @return    Description of the Return Value
	 */

	protected Element createContent( WebSession s )
	{
		ElementContainer ec = new ElementContainer();
		String action = null;
		String http = null;
		
		http = s.getRequest().getParameter(HTTPONLY);
		action = s.getRequest().getParameter(ACTION);
		
		if(http != null) {
			httpOnly = Boolean.parseBoolean(http);
		}
		
		if(httpOnly) {
//			System.out.println("HttpOnly: Setting HttpOnly for cookie");
			setHttpOnly(s);
		} else {
//			System.out.println("HttpOnly: Removing HttpOnly for cookie");
			removeHttpOnly(s);
		}
		
		if(action != null) {
			if(action.equals(READ)) {
				handleReadAction(s);
			} else if(action.equals(WRITE)) {
				handleWriteAction(s);
			} else {
				//s.setMessage("Invalid Request. Please try again.");
			}
		}
		
		try
		{
			ec.addElement(makeContent(s));
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
		return AbstractLesson.A4;
	}


	/**
	 *  Gets the hints attribute of the EmailScreen object
	 *
	 * @return    The hints value
	 */
	protected List getHints()
	{
		List<String> hints = new ArrayList<String>();
		hints.add( "Read the directions and try out the buttons." );
		return hints;
	}
	
	private String createCustomCookieValue() {
		String value = null;
		byte[] buffer = null;
		MessageDigest md = null;
		BASE64Encoder encoder = new BASE64Encoder();
		
		try {
			md = MessageDigest.getInstance("SHA");
			buffer = new Date().toString().getBytes();
			
			md.update(buffer);
			value = encoder.encode(md.digest());
			original = value;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return value;
	}
	
	private void setHttpOnly(WebSession s) {
		String value = createCustomCookieValue();
		HttpServletResponse response = s.getResponse();
		String cookie = s.getCookie(UNIQUE2U);
		
		if(cookie == null || cookie.equals("HACKED")) {
			response.setHeader("Set-Cookie", UNIQUE2U + "=" + value + "; HttpOnly");
			original = value;
		} else {
			response.setHeader("Set-Cookie", UNIQUE2U + "=" + cookie + "; HttpOnly");
			original = cookie;
		}
	}
	
	private void removeHttpOnly(WebSession s) {
		String value = createCustomCookieValue();
		HttpServletResponse response = s.getResponse();
		String cookie = s.getCookie(UNIQUE2U);
		
		if(cookie == null || cookie.equals("HACKED")) {
			response.setHeader("Set-Cookie", UNIQUE2U + "=" + value + ";");
			original = value;
		} else {
			response.setHeader("Set-Cookie", UNIQUE2U + "=" + cookie + ";");
			original = cookie;
		}
	}
	
	private ElementContainer makeContent(WebSession s) {
		ElementContainer ec = new ElementContainer();
		Element r = null;
		Table t = null;
		TR tr = null;
		Form f = null;
		
		ec.addElement(new StringElement(getJavaScript()));

		f = new Form();
		
		t = new Table();
		t.setWidth(500);
		
		tr = new TR();
		
		tr.addElement(new TD(new StringElement("Your browser appears to be: " + getBrowserType(s))));
		t.addElement(tr);
		
		tr = new TR();
		t.addElement(tr);
		
		tr = new TR();
		
		tr.addElement( new TD(new StringElement ("Do you wish to turn HTTPOnly on?")));
		
		tr.addElement( new TD(new StringElement ("Yes")));
		
		if(httpOnly == true) {
			r = new Input(Input.RADIO, HTTPONLY, "True" ).addAttribute("Checked", "true");
		} else {
			r = new Input(Input.RADIO, HTTPONLY, "True" ).addAttribute("onClick", "document.form.submit()");
		}
		
		tr.addElement(new TD(r));
		
		tr.addElement( new TD(new StringElement ("No")));
		
		if(httpOnly == false) {
			r = new Input(Input.RADIO, HTTPONLY, "False").addAttribute("Checked", "True");
		} else {
			r = new Input(Input.RADIO, HTTPONLY, "False").addAttribute("onClick", "document.form.submit()");
		}
		
		tr.addElement(new TD(r));
		
		r = new Input(Input.HIDDEN, READ_RESULT, "");
		tr.addElement(r);
		
		t.addElement(tr);
		
/*		tr.addElement(new TD(new StringElement("<strong>Status:</strong> " )));
		t.addElement(tr);
		
		if(httpOnly == true) {
			tr.addElement(new TD(new StringElement("<div id=\"status\">On</div>")));
		} else {
			tr.addElement(new TD(new StringElement ("<div id=\"status\">Off</div>")));
		}
		
		t.addElement(tr);
		t.addElement(new TR(new TD(new StringElement("<br/>"))));
*/		f.addElement(t);
		
		t = new Table();
		tr = new TR();
		
		r = new Input(Input.SUBMIT, ACTION, READ).addAttribute("onclick", "myAlert();");
		tr.addElement(new TD(r));
		
		r = new Input(Input.SUBMIT, ACTION, WRITE).addAttribute("onclick", "modifyAlert();");
		tr.addElement(new TD(r));
		t.addElement(tr);
		
		f.addElement(t);
		ec.addElement(f);
		
		return ec;
	}
	
	private void handleReadAction(WebSession s) {
		
		String displayed = s.getRequest().getParameter(READ_RESULT);
		
		if(httpOnly == true) {
			if(displayed.indexOf(UNIQUE2U) != -1) {
				s.setMessage("FAILURE: Your browser did not enforce the HTTPOnly flag properly for the '" + UNIQUE2U 
						+ "' cookie. It allowed direct client side read access to this cookie.");
			} else {
				s.setMessage("SUCCESS: Your browser enforced the HTTPOnly flag properly for the '" + UNIQUE2U 
						+ "' cookie by preventing direct client side read access to this cookie.");
				if (writeSuccess) { 
					if (!this.isCompleted(s)) {
						makeSuccess(s);
						readSuccess = false;
						writeSuccess = false;
					}
				} else {
					if (!this.isCompleted(s)) {
						s.setMessage("Now try to see if your browser protects write access to this cookie.");
						readSuccess = true;
					}
				}
			}
		} else if(displayed.indexOf(UNIQUE2U) != -1) {
			s.setMessage("Since HTTPOnly was not enabled, the '" + UNIQUE2U + "' cookie was displayed in the alert dialog.");
		} else {
			s.setMessage("Since HTTPOnly was not enabled, the '" + UNIQUE2U 
					+ "' cookie should have been displayed in the alert dialog, but was not for some reason. "
					+ "(This shouldn't happen)");
		}
	}
	
	private void handleWriteAction(WebSession s) {
		String hacked = s.getCookie(UNIQUE2U);
		
		if(httpOnly == true) {
			if(!original.equals(hacked)) {
				s.setMessage("FAILURE: Your browser did not enforce the write protection property of the HTTPOnly flag for the '" + UNIQUE2U + "' cookie.");
				s.setMessage("The " + UNIQUE2U + " cookie was successfully modified to " + hacked + " on the client side.");
			} else {
				s.setMessage("SUCCESS: Your browser enforced the write protection property of the HTTPOnly flag for the '" 
						+ UNIQUE2U + "' cookie by preventing client side modification.");
				if (readSuccess) { 
					if (!this.isCompleted(s)) {
						makeSuccess(s);
						readSuccess = false;
						writeSuccess = false;
					}
				} else {
					if (!this.isCompleted(s)) {
						s.setMessage("Now try to see if your browser protects read access to this cookie.");
						writeSuccess = true;
					}
				}
			}
		} else if(!original.equals(hacked)) {
			s.setMessage("Since HTTPOnly was not enabled, the browser allowed the '" + UNIQUE2U 
					+ "' cookie to be modified on the client side.");
		} else {
			s.setMessage("Since HTTPOnly was not enabled, the browser should have allowed the '" + UNIQUE2U 
					+ "' cookie to be modified on the client side, but it was not for some reason. "
					+ "(This shouldn't happen)");
		}
	}
	
	private String getJavaScript() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("<script language=\"javascript\">\n");
		buffer.append("function myAlert() {\n");
		buffer.append("alert(document.cookie);\n");
		buffer.append("document.form.read_result.value=document.cookie;\n");
		buffer.append("return true;\n");
		buffer.append("}\n");
		buffer.append("function modifyAlert() {\n");
		buffer.append("document.cookie='" + UNIQUE2U + "=HACKED;\';\n");
		buffer.append("alert(document.cookie);\n");
		buffer.append("return true;\n");
		buffer.append("}\n");
		buffer.append("</script>\n");
		
		return buffer.toString();
	}
	
	private String getBrowserType(WebSession s) {
		int offset = -1;
		String result = "unknown";
		String browser = s.getHeader("user-agent").toLowerCase();
		
		if(browser != null) {
			if(browser.indexOf("firefox") != -1) {
				browser = browser.substring(browser.indexOf("firefox"));
				
				offset = getOffset(browser);
				
				result = browser.substring(0, offset);
			} else if(browser.indexOf("msie 6") != -1) {
				result = "Internet Explorer 6";
			} else if(browser.indexOf("msie 7") != -1) {
				result = "Internet Explorer 7";
			} else if(browser.indexOf("msie") != -1) {
				result = "Internet Explorer";
			} else if(browser.indexOf("opera") != -1) {
				result = "Opera";
			} else if(browser.indexOf("safari") != -1) {
				result = "Safari";
			} else if(browser.indexOf("netscape") != -1) {
				browser = browser.substring(browser.indexOf("netscape"));
				
				offset = getOffset(browser);
				
				result = browser.substring(0, offset);
			} else if(browser.indexOf("konqueror") != -1) {
				result = "Konqueror";
			} else if(browser.indexOf("mozilla") != -1) {
				result = "Mozilla";
			}
		}
		
		return result;
	}
	
	private int getOffset(String s) {
		int result = s.length();
		
		for(int i=0; i<s.length(); i++) {
			if(s.charAt(i) < 33 || s.charAt(i) > 126) {
				result = i;
				break;
			}
		}
		
		return result;
	}
}
