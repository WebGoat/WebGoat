
package org.owasp.webgoat.session;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.owasp.webgoat.lessons.AbstractLesson;
import org.owasp.webgoat.lessons.Category;
import org.owasp.webgoat.lessons.RandomLessonAdapter;
import org.owasp.webgoat.lessons.SequentialLessonAdapter;
import org.owasp.webgoat.util.WebGoatI18N;



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
 * @author Jeff Williams <a href="http://www.aspectsecurity.com">Aspect Security</a>
 * @author Bruce Mayhew <a href="http://code.google.com/p/webgoat">WebGoat</a>
 * 
 * @created October 28, 2003
 */
public class WebSession
{
	/**
	 * Description of the Field
	 */
	public final static String ADMIN = "admin";

	/**
	 * Tomcat role for a webgoat user
	 */
	public final static String WEBGOAT_USER = "webgoat_user";

	/**
	 * Tomcat role for a webgoat admin
	 */
	public final static String WEBGOAT_ADMIN = "webgoat_admin";

	/**
	 * Description of the Field
	 */
	public final static String CHALLENGE = "Challenge";

	/**
	 * Description of the Field
	 */
	public final static String COLOR = "color";

	/**
	 * Description of the Field
	 */
	public final static int ERROR = 0;

	public static final String STAGE = "stage";

	/**
	 * Description of the Field
	 */
	public final static String JSESSION_ID = "jsessionid";

	/**
	 * Description of the Field
	 */
	public final static String LOGOUT = "Logout";

	/**
	 * Description of the Field
	 */
	public final static String RESTART = "Restart";

	/**
	 * Description of the Field
	 */
	public final static String MENU = "menu";

	/**
	 * Description of the Field
	 */
	public final static String SCREEN = "Screen";

	/**
	 * Description of the Field
	 */
	public final static String SESSION = "Session";

	public final static String SHOWSOURCE = "ShowSource";

	public final static String SHOWSOLUTION = "ShowSolution";

	public final static String SHOWHINTS = "ShowHints";

	public final static String SHOW = "show";

	public final static String SHOW_NEXTHINT = "NextHint";

	public final static String SHOW_PREVIOUSHINT = "PreviousHint";

	public final static String SHOW_PARAMS = "Params";

	public final static String SHOW_COOKIES = "Cookies";

	public final static String SHOW_SOURCE = "Source";

	public final static String SHOW_SOLUTION = "Solution";

	public final static String DEBUG = "debug";

	public final static String LANGUAGE = "language";
	
	/**
	 * Description of the Field
	 */
	public final static int WELCOME = -1;

	private WebgoatContext webgoatContext;

	private ServletContext context = null;

	private Course course;

	private int currentScreen = WELCOME;

	private int previousScreen = ERROR;

	private int hintNum = -1;

	private boolean isAdmin = false;

	private boolean isHackedAdmin = false;

	private boolean isAuthenticated = false;

	private boolean isColor = false;

	private boolean isDebug = false;

	private boolean hasHackedHackableAdmin = false;

	private StringBuffer message = new StringBuffer("");

	private ParameterParser myParser;

	private HttpServletRequest request = null;

	private HttpServletResponse response = null;

	private String servletName;

	private HashMap<String, Object> session = new HashMap<String, Object>();

	private boolean showCookies = false;

	private boolean showParams = false;

	private boolean showRequest = false;

	private boolean showSource = false;

	private boolean showSolution = false;

	private boolean completedHackableAdmin = false;

	private int currentMenu;

	private String currentLanguage = null;
	
	
	
	/**
	 * Constructor for the WebSession object
	 * 
	 * @param servlet
	 *            Description of the Parameter
	 * @param context
	 *            Description of the Parameter
	 */
	public WebSession(WebgoatContext webgoatContext, ServletContext context)
	{
		this.webgoatContext = webgoatContext;
		// initialize from web.xml
		showParams = webgoatContext.isShowParams();
		showCookies = webgoatContext.isShowCookies();
		showSource = webgoatContext.isShowSource();
		showSolution = webgoatContext.isShowSolution();
		showRequest = webgoatContext.isShowRequest();
		currentLanguage = webgoatContext.getDefaultLanguage();
		this.context = context;
		
		course = new Course();
		course.loadCourses(webgoatContext, context, "/");
	}

	public static synchronized Connection getConnection(WebSession s) throws SQLException
	{
		return DatabaseUtilities.getConnection(s);
	}

	public static void returnConnection(WebSession s)
	{
		DatabaseUtilities.returnConnection(s.getUserName());
	}

	/**
	 * Description of the Method
	 * 
	 * @param key
	 *            Description of the Parameter
	 * @param value
	 *            Description of the Parameter
	 */
	public void add(String key, Object value)
	{
		session.put(key, value);
	}

	/**
	 * Description of the Method
	 */
	public void clearMessage()
	{
		message.setLength(0);
	}

	/**
	 * Description of the Method
	 */
	public void eatCookies()
	{
		Cookie[] cookies = request.getCookies();

		for (int loop = 0; loop < cookies.length; loop++)
		{
			if (!cookies[loop].getName().startsWith("JS"))
			{// skip jsessionid cookie
				cookies[loop].setMaxAge(0);// mark for deletion by browser
				response.addCookie(cookies[loop]);
			}
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @param key
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	public Object get(String key)
	{
		return (session.get(key));
	}

	/**
	 * Gets the context attribute of the WebSession object
	 * 
	 * @return The context value
	 */
	public ServletContext getContext()
	{
		return context;
	}

		
	
	
	public List<String> getRoles()
	{
		List<String> roles = new ArrayList<String>();

		roles.add(AbstractLesson.USER_ROLE);
		if (isAdmin())
		{
			roles.add(AbstractLesson.ADMIN_ROLE);
		}

		return roles;
	}
	
	/**
	 * Sets the admin flag - this routine is ONLY here to allow someone a backdoor to setting the
	 * user up as an admin.
	 * 
	 * This is also used by the WebSession to set the admin, but the method should be private
	 * 
	 * @param state
	 */
	public void setAdmin(boolean state)

	{
		isAdmin = state;

	}

	public String getRole()
	{

		String role = "";
		if (isAdmin())
		{
			role = AbstractLesson.ADMIN_ROLE;
		}
		else if (isHackedAdmin())
		{
			role = AbstractLesson.HACKED_ADMIN_ROLE;
		}
		else if (isChallenge())
		{
			role = AbstractLesson.CHALLENGE_ROLE;
		}
		else
		{
			role = AbstractLesson.USER_ROLE;
		}

		return role;
	}

	/**
	 * Gets the course attribute of the WebSession object
	 * 
	 * @return The course value
	 */
	public Course getCourse()
	{
		return course;
	}

	public void setCourse(Course course)
	{
		this.course = course;
	}

	/**
	 * Gets the currentScreen attribute of the WebSession object
	 * 
	 * @return The currentScreen value
	 */
	public int getCurrentScreen()
	{
		return (currentScreen);
	}

	public void setCurrentScreen(int screen)
	{
		currentScreen = screen;
	}

	public String getRestartLink()
	{
		return getCurrentLesson().getLink() + "&" + RESTART + "=" + getCurrentScreen();
	}

	public String getCurrentLink()
	{
		String thisLink = "attack";
		Enumeration<String> e = request.getParameterNames();
		boolean isFirstParameter = true;
		while (e.hasMoreElements())
		{
			String name = e.nextElement();
			if (isFirstParameter)
			{
				isFirstParameter = false;
				thisLink += "?";
			}
			else
			{
				thisLink += "&";
			}
			thisLink = thisLink + name + "=" + request.getParameter(name);
		}

		return thisLink;
	}

	public AbstractLesson getCurrentLesson()
	{
		return getCourse().getLesson(this, getCurrentScreen(), getRoles());
	}

	public AbstractLesson getLesson(int id)
	{
		return getCourse().getLesson(this, id, getRoles());
	}

	public List<AbstractLesson> getLessons(Category category)
	{
		return getCourse().getLessons(this, category, getRoles());
	}

	/**
	 * Gets the hint1 attribute of the WebSession object
	 * 
	 * @return The hint1 value
	 */
	private int getHintNum()
	{
		return (hintNum);
	}

	public String getHint()
	{
		String hint = null;
		int hints = getCurrentLesson().getHintCount(this);
		if (getHintNum() > hints) hintNum = -1;
		if (getHintNum() >= 0)
		// FIXME
			hint = getCurrentLesson().getHint(this, getHintNum());

		return hint;
	}

	public List<Parameter> getParams()
	{
		Vector<Parameter> params = null;

		if (showParams() && getParser() != null)
		{
			params = new Vector<Parameter>();

			Enumeration<String> e = getParser().getParameterNames();

			while ((e != null) && e.hasMoreElements())
			{
				String name = (String) e.nextElement();
				String[] values = getParser().getParameterValues(name);

				for (int loop = 0; (values != null) && (loop < values.length); loop++)
				{
					params.add(new Parameter(name, values[loop]));
					// params.add( name + " -> " + values[loop] );
				}
			}

			Collections.sort(params);
		}

		return params;
	}

	public List<Cookie> getCookies()
	{
		List<Cookie> cookies = null;

		if (showCookies()) cookies = Arrays.asList(request.getCookies());

		/*
		 * List cookies = new Vector(); HttpServletRequest request = getRequest(); Cookie[] cookies
		 * = request.getCookies(); if ( cookies.length == 0 ) { list.addElement( new LI(
		 * "No Cookies" ) ); } for ( int i = 0; i < cookies.length; i++ ) { Cookie cookie =
		 * cookies[i]; cookies.add(cookie); //list.addElement( new LI( cookie.getName() + " -> " +
		 * cookie.getValue() ) ); }
		 */

		return cookies;
	}

	/**
	 * Gets the cookie attribute of the CookieScreen object
	 * 
	 * @param s
	 *            Description of the Parameter
	 * @return The cookie value
	 */
	public String getCookie(String cookieName)
	{
		Cookie[] cookies = getRequest().getCookies();

		for (int i = 0; i < cookies.length; i++)
		{
			if (cookies[i].getName().equalsIgnoreCase(cookieName)) { return (cookies[i].getValue()); }
		}

		return (null);
	}

	public String getSource()
	{
		return "Sorry.  No Java Source viewing available.";
		// return getCurrentLesson().getSource(this);
	}

	public String getSolution()
	{
		return "Sorry.  No solution is available.";
		// return getCurrentLesson().getSolution(this);
	}

	public String getInstructions()
	{
		return getCurrentLesson().getInstructions(this);
	}

	/**
	 * Gets the message attribute of the WebSession object
	 * 
	 * @return The message value
	 */
	public String getMessage()
	{
		return (message.toString());
	}

	/**
	 * Gets the parser attribute of the WebSession object
	 * 
	 * @return The parser value
	 */
	public ParameterParser getParser()
	{
		return (myParser);
	}

	/**
	 * Gets the previousScreen attribute of the WebSession object
	 * 
	 * @return The previousScreen value
	 */
	public int getPreviousScreen()
	{
		return (previousScreen);
	}

	/**
	 * Gets the request attribute of the WebSession object
	 * 
	 * @return The request value
	 */
	public HttpServletRequest getRequest()
	{
		return request;
	}

	public void setRequest(HttpServletRequest request)
	{
		this.request = request;
	}

	/**
	 * Gets the response attribute of the WebSession object
	 * 
	 * @return The response value
	 */
	public HttpServletResponse getResponse()
	{
		return response;
	}

	/**
	 * Gets the servletName attribute of the WebSession object
	 * 
	 * @return The servletName value
	 */
	public String getServletName()
	{
		return (servletName);
	}

	/**
	 * Gets the sourceFile attribute of the WebSession object
	 * 
	 * @param screen
	 *            Description of the Parameter
	 * @return The sourceFile value
	 */
	public String getWebResource(String fileName)
	{
		// Note: doesn't work for admin path! Maybe with a ../ attack
		return (context.getRealPath(fileName));
	}

	/**
	 * Gets the admin attribute of the WebSession object
	 * 
	 * @return The admin value
	 */
	public boolean isAdmin()
	{
		return (isAdmin);
	}

	/**
	 * Gets the hackedAdmin attribute of the WebSession object
	 * 
	 * @return The hackedAdmin value
	 */
	public boolean isHackedAdmin()
	{
		return (isHackedAdmin);
	}

	/**
	 * Has the user ever hacked the hackable admin
	 * 
	 * @return The hackedAdmin value
	 */
	public boolean completedHackableAdmin()
	{
		return (completedHackableAdmin);
	}

	/**
	 * Gets the authenticated attribute of the WebSession object
	 * 
	 * @return The authenticated value
	 */
	public boolean isAuthenticated()
	{
		return (isAuthenticated);
	}

	private Map<AbstractLesson, LessonSession> lessonSessions = new Hashtable<AbstractLesson, LessonSession>();

	public boolean isAuthenticatedInLesson(AbstractLesson lesson)
	{
		boolean authenticated = false;

		LessonSession lessonSession = getLessonSession(lesson);
		if (lessonSession != null)
		{
			authenticated = lessonSession.isAuthenticated();
		}
		// System.out.println("Authenticated for lesson " + lesson + "? " + authenticated);

		return authenticated;
	}

	public boolean isAuthorizedInLesson(int employeeId, String functionId)
	{
		return getCurrentLesson().isAuthorized(this, employeeId, functionId);
	}

	public boolean isAuthorizedInLesson(String role, String functionId)
	{
		return getCurrentLesson().isAuthorized(this, role, functionId);
	}

	public int getUserIdInLesson() throws ParameterNotFoundException
	{
		return getCurrentLesson().getUserId(this);
	}

	public String getUserNameInLesson() throws ParameterNotFoundException
	{
		return getCurrentLesson().getUserName(this);
	}

	public void openLessonSession(AbstractLesson lesson)
	{
		System.out.println("Opening new lesson session for lesson " + lesson);
		LessonSession lessonSession = new LessonSession();
		lessonSessions.put(lesson, lessonSession);
	}

	public void closeLessonSession(AbstractLesson lesson)
	{
		lessonSessions.remove(lesson);
	}

	public LessonSession getLessonSession(AbstractLesson lesson)
	{
		return lessonSessions.get(lesson);
	}

	/**
	 * Gets the challenge attribute of the WebSession object
	 * 
	 * @return The challenge value
	 */
	public boolean isChallenge()
	{
		if (getCurrentLesson() != null) { return (Category.CHALLENGE.equals(getCurrentLesson().getCategory())); }
		return false;
	}

	/**
	 * Gets the color attribute of the WebSession object
	 * 
	 * @return The color value
	 */
	public boolean isColor()
	{
		return (isColor);
	}

	/**
	 * Gets the screen attribute of the WebSession object
	 * 
	 * @param value
	 *            Description of the Parameter
	 * @return The screen value
	 */
	public boolean isScreen(int value)
	{
		return (getCurrentScreen() == value);
	}

	/**
	 * Gets the user attribute of the WebSession object
	 * 
	 * @return The user value
	 */
	public boolean isUser()
	{
		return (!isAdmin && !isChallenge());
	}

	/**
	 * Sets the message attribute of the WebSession object
	 * 
	 * @param text
	 *            The new message value
	 */
	public void setMessage(String text)
	{
		message.append("<BR>" + " * " + text);
	}

	public void setLineBreak(String text)
	{
		message.append("<BR><BR>" + text);
	}

	/**
	 * Description of the Method
	 * 
	 * @return Description of the Return Value
	 */
	public boolean showCookies()
	{
		return (showCookies);
	}

	/**
	 * Description of the Method
	 * 
	 * @return Description of the Return Value
	 */
	public boolean showParams()
	{
		return (showParams);
	}

	/**
	 * Description of the Method
	 * 
	 * @return Description of the Return Value
	 */
	public boolean showRequest()
	{
		return (showRequest);
	}

	/**
	 * Description of the Method
	 * 
	 * @return Description of the Return Value
	 */
	public boolean showSource()
	{
		return (showSource);
	}

	public boolean showSolution()
	{
		return (showSolution);
	}

	/**
	 * Gets the userName attribute of the WebSession object
	 * 
	 * @return The userName value
	 */
	public String getUserName()
	{
		HttpServletRequest request = getRequest();
		if (request == null) throw new RuntimeException("Could not find the ServletRequest in the web session");
		Principal principal = request.getUserPrincipal();
		if (principal == null) throw new RuntimeException("Could not find the Principal in the Servlet Request");
		return principal.getName();
	}

	/**
	 * Parse parameters from the given request, handle any servlet commands, and update this session
	 * based on the parameters.
	 * 
	 * @param request
	 *            Description of the Parameter
	 * @param response
	 *            Description of the Parameter
	 * @param name
	 *            Description of the Parameter
	 */
	public void update(HttpServletRequest request, HttpServletResponse response, String name) throws IOException
	{
		String content = null;

		clearMessage();
		this.request = request;
		this.response = response;
		this.servletName = name;

		if (myParser == null)
		{
			myParser = new ParameterParser(request);
		}
		else
		{
			myParser.update(request);
		}
		
		if(myParser.getRawParameter(LANGUAGE,null)!=null){
			this.currentLanguage=new String(myParser.getRawParameter(LANGUAGE,null));
			WebGoatI18N.setCurrentLanguage(this.currentLanguage);
		}
		

		// System.out.println("Current Screen 1: " + currentScreen );
		// System.out.println("Previous Screen 1: " + previousScreen );
		// FIXME: requires ?Logout=true
		// FIXME: doesn't work right -- no reauthentication
		if (myParser.getRawParameter(LOGOUT, null) != null)
		{
			System.out.println("Logout " + request.getUserPrincipal());
			eatCookies();
			request.getSession().invalidate();
			currentScreen = WELCOME;
			previousScreen = ERROR;
		}

		// There are several scenarios where we want the first lesson to be loaded
		// 1) Previous screen is Welcome - Start of the course
		// 2) After a logout and after the session has been reinitialized
		if ((this.getPreviousScreen() == WebSession.WELCOME) || (getRequest().getSession(false) != null &&
		// getRequest().getSession(false).isNew() &&
				this.getCurrentScreen() == WebSession.WELCOME && this.getPreviousScreen() == WebSession.ERROR))
		{
			currentScreen = course.getFirstLesson().getScreenId();
			hintNum = -1;
		}

		// System.out.println("Current Screen 2: " + currentScreen );
		// System.out.println("Previous Screen 2: " + previousScreen );
		// update the screen variables
		previousScreen = currentScreen;

		try
		{
			// If the request is new there should be no parameters.
			// This can occur from a session timeout or a the starting of a new course.
			if (!request.getSession().isNew())
			{
				currentScreen = myParser.getIntParameter(SCREEN, currentScreen);
			}
			else
			{
				if (!myParser.getRawParameter(SCREEN, "NULL").equals("NULL"))
				{
					this.setMessage("Session Timeout - Starting new Session.");
				}
			}
		} catch (Exception e)
		{
		}

		// clear variables when switching screens
		if (this.getCurrentScreen() != this.getPreviousScreen())
		{
			if (webgoatContext.isDebug())
			{
				setMessage("Changed to a new screen, clearing cookies and hints");
			}
			eatCookies();
			hintNum = -1;
		}
		else if (myParser.getRawParameter(STAGE, null) != null)
		{
			AbstractLesson al = getCurrentLesson();
			if (al instanceof SequentialLessonAdapter)
			{
				SequentialLessonAdapter sla = (SequentialLessonAdapter) al;
				int stage = myParser.getIntParameter(STAGE, sla.getStage(this));
				if (stage > 0 && stage <= sla.getStageCount()) sla.setStage(this, stage);
			}
			else if (al instanceof RandomLessonAdapter)
			{
				try
				{
					RandomLessonAdapter rla = (RandomLessonAdapter) al;
					int stage = myParser.getIntParameter(STAGE) - 1;
					String[] stages = rla.getStages();
					if (stages == null) stages = new String[0];
					if (stage >= 0 && stage < stages.length) rla.setStage(this, stages[stage]);
				} catch (ParameterNotFoundException pnfe)
				{
				}
			}
		}
		// else update global variables for the current screen
		else
		{
			// Handle "restart" commands
			int lessonId = myParser.getIntParameter(RESTART, -1);
			if (lessonId != -1)
			{
				restartLesson(lessonId);
			}
			// if ( myParser.getBooleanParameter( RESTART, false ) )
			// {
			// getCurrentLesson().getLessonTracker( this ).getLessonProperties().setProperty(
			// CHALLENGE_STAGE, "1" );
			// }

			// Handle "show" commands
			String showCommand = myParser.getStringParameter(SHOW, null);
			if (showCommand != null)
			{
				if (showCommand.equalsIgnoreCase(SHOW_PARAMS))
				{
					showParams = !showParams;
				}
				else if (showCommand.equalsIgnoreCase(SHOW_COOKIES))
				{
					showCookies = !showCookies;
				}
				else if (showCommand.equalsIgnoreCase(SHOW_SOURCE))
				{
					content = getSource();
					// showSource = true;
				}
				else if (showCommand.equalsIgnoreCase(SHOW_SOLUTION))
				{
					content = getSolution();
					// showSource = true;
				}
				else if (showCommand.equalsIgnoreCase(SHOW_NEXTHINT))
				{
					getNextHint();
				}
				else if (showCommand.equalsIgnoreCase(SHOW_PREVIOUSHINT))
				{
					getPreviousHint();
				}
			}

		}

		isAdmin = request.isUserInRole(WEBGOAT_ADMIN);
		isHackedAdmin = myParser.getBooleanParameter(ADMIN, isAdmin);
		if (isHackedAdmin)
		{
			System.out.println("Hacked admin");
			hasHackedHackableAdmin = true;
		}
		isColor = myParser.getBooleanParameter(COLOR, isColor);
		isDebug = myParser.getBooleanParameter(DEBUG, isDebug);

		// System.out.println( "showParams:" + showParams );
		// System.out.println( "showSource:" + showSource );
		// System.out.println( "showSolution:" + showSolution );
		// System.out.println( "showCookies:" + showCookies );
		// System.out.println( "showRequest:" + showRequest );

		if (content != null)
		{
			response.setContentType("text/html");
			PrintWriter out = new PrintWriter(response.getOutputStream());
			out.print(content);
			out.flush();
			out.close();
		}
	}

	private void restartLesson(int lessonId)
	{
		AbstractLesson al = getLesson(lessonId);
		System.out.println("Restarting lesson: " + al);
		al.getLessonTracker(this).setCompleted(false);
		if (al instanceof SequentialLessonAdapter)
		{
			SequentialLessonAdapter sla = (SequentialLessonAdapter) al;
			sla.getLessonTracker(this).setStage(1);
		}
		else if (al instanceof RandomLessonAdapter)
		{
			RandomLessonAdapter rla = (RandomLessonAdapter) al;
			rla.setStage(this, rla.getStages()[0]);
		}
	}

	/**
	 * @param string
	 */
	public void setHasHackableAdmin(String role)
	{
		hasHackedHackableAdmin = (AbstractLesson.HACKED_ADMIN_ROLE.equals(role) & hasHackedHackableAdmin);

		// if the user got the Admin=true parameter correct AND they accessed an admin screen
		if (hasHackedHackableAdmin)
		{
			completedHackableAdmin = true;
		}
	}

	/**
	 * @return Returns the isDebug.
	 */
	public boolean isDebug()
	{
		return isDebug;
	}

	/**
	 * @param header
	 *            - request header value to return
	 * @return
	 */
	public String getHeader(String header)
	{
		return getRequest().getHeader(header);
	}

	public String getNextHint()
	{
		String hint = null;

		// FIXME
		int maxHints = getCurrentLesson().getHintCount(this);
		if (hintNum < maxHints - 1)
		{
			hintNum++;

			// Hints are indexed from 0
			getCurrentLesson().getLessonTracker(this).setMaxHintLevel(getHintNum() + 1);

			hint = (String) getCurrentLesson().getHint(this, getHintNum());
		}

		return hint;
	}

	public String getPreviousHint()
	{
		String hint = null;

		if (hintNum > 0)
		{
			hintNum--;

			// Hints are indexed from 0
			getCurrentLesson().getLessonTracker(this).setMaxHintLevel(getHintNum() + 1);

			hint = (String) getCurrentLesson().getHint(this, getHintNum());
		}

		return hint;
	}

	public void setCurrentMenu(Integer ranking)
	{
		currentMenu = ranking.intValue();
	}

	public int getCurrentMenu()
	{
		return currentMenu;
	}

	public WebgoatContext getWebgoatContext()
	{
		return webgoatContext;
	}

	public String getCurrrentLanguage() {
		return currentLanguage;
	}

	
}
