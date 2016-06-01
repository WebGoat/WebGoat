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
import java.util.Locale;
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
import org.owasp.webgoat.lessons.model.RequestParameter;
import org.owasp.webgoat.util.BeanProvider;
import org.owasp.webgoat.util.LabelManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * *************************************************************************************************
 *
 *
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details, please see
 * http://www.owasp.org/
 *
 * Copyright (c) 2002 - 20014 Bruce Mayhew
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Free
 * Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * Getting Source ==============
 *
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software
 * projects.
 *
 * @author Jeff Williams <a href="http://www.aspectsecurity.com">Aspect Security</a>
 * @author Bruce Mayhew <a href="http://code.google.com/p/webgoat">WebGoat</a>
 * @since October 28, 2003
 * @version $Id: $Id
 */
public class WebSession {

    final Logger logger = LoggerFactory.getLogger(WebSession.class);

    /**
     * Description of the Field
     */
    public final static String ADMIN = "admin";

    /**
     * Tomcat role for a webgoat user
     */
    public final static String WEBGOAT_USER = "ROLE_WEBGOAT_USER";

    /**
     * Tomcat role for a webgoat admin
     */
    public final static String WEBGOAT_ADMIN = "ROLE_WEBGOAT_ADMIN";

    /**
     * Description of the Field
     */
    public final static String CHALLENGE = "Challenge";

    /**
     * Description of the Field
     */
    public final static String COLOR = "color";

    /** Constant <code>COURSE="course"</code> */
    public final static String COURSE = "course";

    /**
     * Error screen number
     */
    public final static int ERROR = 0;

    /** Constant <code>STAGE="stage"</code> */
    public static final String STAGE = "stage";

    /**
     * session id string
     */
    public final static String JSESSION_ID = "jsessionid";

    /**
     * Logout parameter name
     */
    public final static String LOGOUT = "Logout";

    /**
     * Restart parameter name
     */
    public final static String RESTART = "Restart";

    /**
     * menu parameter name
     */
    public final static String MENU = "menu";

    /**
     * Screen parameter name
     */
    public final static String SCREEN = "Screen";

    /**
     * Description of the Field
     */
    public final static String SESSION = "websession";

    /** Constant <code>SHOWSOURCE="ShowSource"</code> */
    public final static String SHOWSOURCE = "ShowSource";

    /** Constant <code>SHOWSOLUTION="ShowSolution"</code> */
    public final static String SHOWSOLUTION = "ShowSolution";

    /** Constant <code>SHOWHINTS="ShowHints"</code> */
    public final static String SHOWHINTS = "ShowHints";

    /** Constant <code>SHOW="show"</code> */
    public final static String SHOW = "show";

    /** Constant <code>SHOW_NEXTHINT="NextHint"</code> */
    public final static String SHOW_NEXTHINT = "NextHint";

    /** Constant <code>SHOW_PREVIOUSHINT="PreviousHint"</code> */
    public final static String SHOW_PREVIOUSHINT = "PreviousHint";

    /** Constant <code>SHOW_PARAMS="Params"</code> */
    public final static String SHOW_PARAMS = "Params";

    /** Constant <code>SHOW_COOKIES="Cookies"</code> */
    public final static String SHOW_COOKIES = "Cookies";

    /** Constant <code>SHOW_SOURCE="Source"</code> */
    public final static String SHOW_SOURCE = "Source";

    /** Constant <code>SHOW_SOLUTION="Solution"</code> */
    public final static String SHOW_SOLUTION = "Solution";

    /** Constant <code>DEBUG="debug"</code> */
    public final static String DEBUG = "debug";

    /** Constant <code>LANGUAGE="language"</code> */
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

    private int previousStage = -1;

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

    private List<Cookie> cookiesOnLastRequest;

    private List<RequestParameter> parmsOnLastRequest;

    /**
     * Constructor for the WebSession object
     *
     * @param webgoatContext a {@link org.owasp.webgoat.session.WebgoatContext} object.
     * @param context Description of the Parameter
     */
    public WebSession(WebgoatContext webgoatContext, ServletContext context) {
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

    /**
     * <p> getConnection. </p>
     *
     * @param s a {@link org.owasp.webgoat.session.WebSession} object.
     * @return a {@link java.sql.Connection} object.
     * @throws java.sql.SQLException if any.
     */
    public static synchronized Connection getConnection(WebSession s) throws SQLException {
        return DatabaseUtilities.getConnection(s);
    }

    /**
     * <p> returnConnection. </p>
     *
     * @param s a {@link org.owasp.webgoat.session.WebSession} object.
     */
    public static void returnConnection(WebSession s) {
        DatabaseUtilities.returnConnection(s.getUserName());
    }

    /**
     * Description of the Method
     *
     * @param key Description of the Parameter
     * @param value Description of the Parameter
     */
    public void add(String key, Object value) {
        session.put(key, value);
    }

    /**
     * Description of the Method
     */
    public void clearMessage() {
        message.setLength(0);
    }

    /**
     * Marks all cookies but the JSESSIONID for deletion and adds them to the response.
     */
    public void eatCookies() {
        Cookie[] cookies = request.getCookies();

        for (int loop = 0; loop < cookies.length; loop++) {
            if (!cookies[loop].getName().startsWith("JS")) {// skip jsessionid cookie
                cookies[loop].setMaxAge(0);// mark for deletion by browser
                response.addCookie(cookies[loop]);
            }
        }
    }

    /**
     * Description of the Method
     *
     * @param key Description of the Parameter
     * @return Description of the Return Value
     */
    public Object get(String key) {
        return (session.get(key));
    }

    /**
     * Gets the context attribute of the WebSession object
     *
     * @return The context value
     */
    public ServletContext getContext() {
        return context;
    }

    /**
     * <p> getRoles. </p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<String> getRoles() {
        List<String> roles = new ArrayList<String>();

        roles.add(AbstractLesson.USER_ROLE);
        if (isAdmin()) {
            roles.add(AbstractLesson.ADMIN_ROLE);
        }

        return roles;
    }

    /**
     * Sets the admin flag - this routine is ONLY here to allow someone a backdoor to setting the user up as an admin.
     *
     * This is also used by the WebSession to set the admin, but the method should be private
     *
     * @param state a boolean.
     */
    public void setAdmin(boolean state) {
        isAdmin = state;

    }

    /**
     * <p> getRole. </p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getRole() {

        String role = "";
        if (isAdmin()) {
            role = AbstractLesson.ADMIN_ROLE;
        } else if (isHackedAdmin()) {
            role = AbstractLesson.HACKED_ADMIN_ROLE;
        } else if (isChallenge()) {
            role = AbstractLesson.CHALLENGE_ROLE;
        } else {
            role = AbstractLesson.USER_ROLE;
        }

        return role;
    }

    /**
     * Gets the course attribute of the WebSession object
     *
     * @return The course value
     */
    public Course getCourse() {
        return course;
    }

    /**
     * <p> Setter for the field <code>course</code>. </p>
     *
     * @param course a {@link org.owasp.webgoat.session.Course} object.
     */
    public void setCourse(Course course) {
        this.course = course;
    }

    /**
     * Gets the currentScreen attribute of the WebSession object
     *
     * @return The currentScreen value
     */
    public int getCurrentScreen() {
        return (currentScreen);
    }

    /**
     * <p> Setter for the field <code>currentScreen</code>. </p>
     *
     * @param screen a int.
     */
    public void setCurrentScreen(int screen) {
        currentScreen = screen;
    }

    /**
     * <p> getRestartLink. </p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getRestartLink() {
        return getCurrentLesson().getLink() + "&" + RESTART + "=" + getCurrentScreen();
    }

    /**
     * <p> getCurrentLink. </p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getCurrentLink() {
        String thisLink = "attack";
        Enumeration<String> e = request.getParameterNames();
        boolean isFirstParameter = true;
        while (e.hasMoreElements()) {
            String name = e.nextElement();
            if (isFirstParameter) {
                isFirstParameter = false;
                thisLink += "?";
            } else {
                thisLink += "&";
            }
            thisLink = thisLink + name + "=" + request.getParameter(name);
        }

        return thisLink;
    }

    /**
     * <p> getCurrentLesson. </p>
     *
     * @return a {@link org.owasp.webgoat.lessons.AbstractLesson} object.
     */
    public AbstractLesson getCurrentLesson() {
        return getCourse().getLesson(this, getCurrentScreen(), getRoles());
    }

    /**
     * <p> getLesson. </p>
     *
     * @param id a int.
     * @return a {@link org.owasp.webgoat.lessons.AbstractLesson} object.
     */
    public AbstractLesson getLesson(int id) {
        return getCourse().getLesson(this, id, getRoles());
    }

    /**
     * <p> getLessons. </p>
     *
     * @param category a {@link org.owasp.webgoat.lessons.Category} object.
     * @return a {@link java.util.List} object.
     */
    public List<AbstractLesson> getLessons(Category category) {
        return getCourse().getLessons(this, category, getRoles());
    }

    /**
     * Gets the hint1 attribute of the WebSession object
     *
     * @return The hint1 value
     */
    private int getHintNum() {
        return (hintNum);
    }

    /**
     * <p> getHint. </p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getHint() {
        String hint = null;
        int hints = getCurrentLesson().getHintCount(this);
        if (getHintNum() > hints) {
            hintNum = -1;
        }
        if (getHintNum() >= 0) // FIXME
        {
            hint = getCurrentLesson().getHint(this, getHintNum());
        }

        return hint;
    }

    /**
     * <p> getParams. </p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<Parameter> getParams() {
        Vector<Parameter> params = null;

        if (showParams() && getParser() != null) {
            params = new Vector<Parameter>();

            Enumeration<String> e = getParser().getParameterNames();

            while ((e != null) && e.hasMoreElements()) {
                String name = (String) e.nextElement();
                String[] values = getParser().getParameterValues(name);

                for (int loop = 0; (values != null) && (loop < values.length); loop++) {
                    params.add(new Parameter(name, values[loop]));
                    // params.add( name + " -> " + values[loop] );
                }
            }

            Collections.sort(params);
        }

        return params;
    }

    /**
     * <p> getCookies. </p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<Cookie> getCookies() {
        List<Cookie> cookies = null;

        if (showCookies()) {
            cookies = Arrays.asList(request.getCookies());
        }

        /*
         * List cookies = new Vector(); HttpServletRequest request = getRequest(); Cookie[] cookies =
         * request.getCookies(); if ( cookies.length == 0 ) { list.addElement( new LI( "No Cookies" ) ); } for ( int i =
         * 0; i < cookies.length; i++ ) { Cookie cookie = cookies[i]; cookies.add(cookie); //list.addElement( new LI(
         * cookie.getName() + " -> " + cookie.getValue() ) ); }
         */
        return cookies;
    }

    /**
     * Gets the cookie attribute of the CookieScreen object
     *
     * @return The cookie value
     * @param cookieName a {@link java.lang.String} object.
     */
    public String getCookie(String cookieName) {
        Cookie[] cookies = getRequest().getCookies();

        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equalsIgnoreCase(cookieName)) {
                return (cookies[i].getValue());
            }
        }

        return (null);
    }

    /**
     * <p> getSource. </p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getSource() {
        return "Sorry.  No Java Source viewing available.";
        // return getCurrentLesson().getSource(this);
    }

    /**
     * <p> getSolution. </p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getSolution() {
        return "Sorry.  No solution is available.";
        // return getCurrentLesson().getSolution(this);
    }

    /**
     * <p> getInstructions. </p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getInstructions() {
        return getCurrentLesson().getInstructions(this);
    }

    /**
     * Gets the message attribute of the WebSession object
     *
     * @return The message value
     */
    public String getMessage() {
        return (message.toString());
    }

    /**
     * Gets the parser attribute of the WebSession object
     *
     * @return The parser value
     */
    public ParameterParser getParser() {
        return (myParser);
    }

    /**
     * Gets the previousScreen attribute of the WebSession object
     *
     * @return The previousScreen value
     */
    public int getPreviousScreen() {
        return (previousScreen);
    }

    /**
     * Gets the request attribute of the WebSession object
     *
     * @return The request value
     */
    public HttpServletRequest getRequest() {
        return request;
    }

    /**
     * <p> Setter for the field <code>request</code>. </p>
     *
     * @param request a {@link javax.servlet.http.HttpServletRequest} object.
     */
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * Gets the response attribute of the WebSession object
     *
     * @return The response value
     */
    public HttpServletResponse getResponse() {
        return response;
    }

    /**
     * Gets the servletName attribute of the WebSession object
     *
     * @return The servletName value
     */
    public String getServletName() {
        return (servletName);
    }

    /**
     * Gets the sourceFile attribute of the WebSession object
     *
     * @return The sourceFile value
     * @param fileName a {@link java.lang.String} object.
     */
    public String getWebResource(String fileName) {
        // Note: doesn't work for admin path! Maybe with a ../ attack
        return (context.getRealPath(fileName));
    }

    /**
     * Gets the admin attribute of the WebSession object
     *
     * @return The admin value
     */
    public boolean isAdmin() {
        return (isAdmin);
    }

    /**
     * Gets the hackedAdmin attribute of the WebSession object
     *
     * @return The hackedAdmin value
     */
    public boolean isHackedAdmin() {
        return (isHackedAdmin);
    }

    /**
     * Has the user ever hacked the hackable admin
     *
     * @return The hackedAdmin value
     */
    public boolean completedHackableAdmin() {
        return (completedHackableAdmin);
    }

    /**
     * Gets the authenticated attribute of the WebSession object
     *
     * @return The authenticated value
     */
    public boolean isAuthenticated() {
        return (isAuthenticated);
    }

    private Map<AbstractLesson, LessonSession> lessonSessions = new Hashtable<AbstractLesson, LessonSession>();

    /**
     * <p> isAuthenticatedInLesson. </p>
     *
     * @param lesson a {@link org.owasp.webgoat.lessons.AbstractLesson} object.
     * @return a boolean.
     */
    public boolean isAuthenticatedInLesson(AbstractLesson lesson) {
        boolean authenticated = false;

        LessonSession lessonSession = getLessonSession(lesson);
        if (lessonSession != null) {
            authenticated = lessonSession.isAuthenticated();
        }
        // System.out.println("Authenticated for lesson " + lesson + "? " + authenticated);

        return authenticated;
    }

    /**
     * <p> isAuthorizedInLesson. </p>
     *
     * @param employeeId a int.
     * @param functionId a {@link java.lang.String} object.
     * @return a boolean.
     */
    public boolean isAuthorizedInLesson(int employeeId, String functionId) {
        return getCurrentLesson().isAuthorized(this, employeeId, functionId);
    }

    /**
     * <p> isAuthorizedInLesson. </p>
     *
     * @param role a {@link java.lang.String} object.
     * @param functionId a {@link java.lang.String} object.
     * @return a boolean.
     */
    public boolean isAuthorizedInLesson(String role, String functionId) {
        return getCurrentLesson().isAuthorized(this, role, functionId);
    }

    /**
     * <p> getUserIdInLesson. </p>
     *
     * @return a int.
     * @throws org.owasp.webgoat.session.ParameterNotFoundException if any.
     */
    public int getUserIdInLesson() throws ParameterNotFoundException {
        return getCurrentLesson().getUserId(this);
    }

    /**
     * <p> getUserNameInLesson. </p>
     *
     * @return a {@link java.lang.String} object.
     * @throws org.owasp.webgoat.session.ParameterNotFoundException if any.
     */
    public String getUserNameInLesson() throws ParameterNotFoundException {
        return getCurrentLesson().getUserName(this);
    }

    /**
     * <p> openLessonSession. </p>
     *
     * @param lesson a {@link org.owasp.webgoat.lessons.AbstractLesson} object.
     */
    public void openLessonSession(AbstractLesson lesson) {
        System.out.println("Opening new lesson session for lesson " + lesson);
        LessonSession lessonSession = new LessonSession();
        lessonSessions.put(lesson, lessonSession);
    }

    /**
     * <p> closeLessonSession. </p>
     *
     * @param lesson a {@link org.owasp.webgoat.lessons.AbstractLesson} object.
     */
    public void closeLessonSession(AbstractLesson lesson) {
        lessonSessions.remove(lesson);
    }

    /**
     * <p> getLessonSession. </p>
     *
     * @param lesson a {@link org.owasp.webgoat.lessons.AbstractLesson} object.
     * @return a {@link org.owasp.webgoat.session.LessonSession} object.
     */
    public LessonSession getLessonSession(AbstractLesson lesson) {
        return lessonSessions.get(lesson);
    }

    /**
     * Gets the challenge attribute of the WebSession object
     *
     * @return The challenge value
     */
    public boolean isChallenge() {
        if (getCurrentLesson() != null) {
            return (Category.CHALLENGE.equals(getCurrentLesson().getCategory()));
        }
        return false;
    }

    /**
     * Gets the color attribute of the WebSession object
     *
     * @return The color value
     */
    public boolean isColor() {
        return (isColor);
    }

    /**
     * Gets the screen attribute of the WebSession object
     *
     * @param value Description of the Parameter
     * @return The screen value
     */
    public boolean isScreen(int value) {
        return (getCurrentScreen() == value);
    }

    /**
     * Gets the user attribute of the WebSession object
     *
     * @return The user value
     */
    public boolean isUser() {
        return (!isAdmin && !isChallenge());
    }

    /**
     * Sets the message attribute of the WebSession object
     *
     * @param text The new message value
     */
    public void setMessage(String text) {
        message.append("<BR>" + " * " + text);
    }

    /**
     * <p> setLineBreak. </p>
     *
     * @param text a {@link java.lang.String} object.
     */
    public void setLineBreak(String text) {
        message.append("<BR><BR>" + text);
    }

    /**
     * Description of the Method
     *
     * @return Description of the Return Value
     */
    public boolean showCookies() {
        return (showCookies);
    }

    /**
     * Description of the Method
     *
     * @return Description of the Return Value
     */
    public boolean showParams() {
        return (showParams);
    }

    /**
     * Description of the Method
     *
     * @return Description of the Return Value
     */
    public boolean showRequest() {
        return (showRequest);
    }

    /**
     * Description of the Method
     *
     * @return Description of the Return Value
     */
    public boolean showSource() {
        return (showSource);
    }

    /**
     * <p> showSolution. </p>
     *
     * @return a boolean.
     */
    public boolean showSolution() {
        return (showSolution);
    }

    /**
     * Gets the userName attribute of the WebSession object
     *
     * @return The userName value
     */
    public String getUserName() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            throw new RuntimeException("Could not find the ServletRequest in the web session");
        }
        Principal principal = request.getUserPrincipal();
        if (principal == null) {
            throw new RuntimeException("Could not find the Principal in the Servlet Request");
        }
        return principal.getName();
    }

    /**
     * Parse parameters from the given request, handle any servlet commands, and update this session based on the
     * parameters.
     *
     * @param request Description of the Parameter
     * @param response Description of the Parameter
     * @param name Description of the Parameter
     * @throws java.io.IOException if any.
     */
    public void update(HttpServletRequest request, HttpServletResponse response, String name) throws IOException {
        String content = null;

        this.request = request;
        this.response = response;
        this.servletName = name;

        clearMessage();
        updateParser(request);

        // System.out.println("Current Screen 1: " + currentScreen );
        // System.out.println("Previous Screen 1: " + previousScreen );
        // FIXME: requires ?Logout=true
        // FIXME: doesn't work right -- no reauthentication
        // REMOVED - we have explicit logout now via spriing security
        /*
         * if (myParser.getRawParameter(LOGOUT, null) != null) { System.out.println("Logout " +
         * request.getUserPrincipal()); eatCookies(); request.getSession().invalidate(); currentScreen = WELCOME;
         * previousScreen = ERROR; }
         */

        updateScreenProperties(request);

        if (this.getCurrentScreen() != this.getPreviousScreen()) {
            clearScreenProperties();
        } else if (myParser.getRawParameter(STAGE, null) != null) {
            updateCurrentScreenStage();
        } else {
            content = updateCurrentScreen(content);
        }

        updateParameters(request);
        updateContent(response, content);
    }

    /**
     * Updates parameters isAdmin, isHackedAdmin, hasHackedHackableAdmin, isColor and isDebug
     *
     * @param request
     */
    private void updateParameters(HttpServletRequest request) {
        isAdmin = request.isUserInRole(WEBGOAT_ADMIN);
        isHackedAdmin = myParser.getBooleanParameter(ADMIN, isAdmin);
        if (isHackedAdmin) {
            System.out.println("Hacked admin");
            hasHackedHackableAdmin = true;
        }
        isColor = myParser.getBooleanParameter(COLOR, isColor);
        isDebug = myParser.getBooleanParameter(DEBUG, isDebug);
    }

    /**
     * If the content is not already set we get the response and sends it on its way
     *
     * @param response
     * @param content to send
     * @throws IOException
     */
    private void updateContent(HttpServletResponse response, String content) throws IOException {
        // System.out.println( "showParams:" + showParams );
        // System.out.println( "showSource:" + showSource );
        // System.out.println( "showSolution:" + showSolution );
        // System.out.println( "showCookies:" + showCookies );
        // System.out.println( "showRequest:" + showRequest );
        if (content != null) {
            response.setContentType("text/html");
            PrintWriter out = new PrintWriter(response.getOutputStream());
            out.print(content);
            out.flush();
            out.close();
        }
    }

    /**
     * Checks to see if the lesson should be restarted. Also handles parsing of "show" commands for getting hints,
     * params, cookies, source and solution.
     *
     * @param content
     * @return the updated content
     */
    private String updateCurrentScreen(String content) {
        // else update global variables for the current screen
        // Handle "restart" commands
        int lessonId = myParser.getIntParameter(RESTART, -1);
        if (lessonId != -1) {
            restartLesson(lessonId);
        }
        // if ( myParser.getBooleanParameter( RESTART, false ) )
        // {
        // getCurrentLesson().getLessonTracker( this ).getLessonProperties().setProperty(
        // CHALLENGE_STAGE, "1" );
        // }

        // Handle "show" commands
        String showCommand = myParser.getStringParameter(SHOW, null);
        if (showCommand != null) {
            if (showCommand.equalsIgnoreCase(SHOW_PARAMS)) {
                showParams = !showParams;
            } else if (showCommand.equalsIgnoreCase(SHOW_COOKIES)) {
                showCookies = !showCookies;
            } else if (showCommand.equalsIgnoreCase(SHOW_SOURCE)) {
                content = getSource();
                // showSource = true;
            } else if (showCommand.equalsIgnoreCase(SHOW_SOLUTION)) {
                content = getSolution();
                // showSource = true;
            } else if (showCommand.equalsIgnoreCase(SHOW_NEXTHINT)) {
                getNextHint();
            } else if (showCommand.equalsIgnoreCase(SHOW_PREVIOUSHINT)) {
                getPreviousHint();
            }
        }
        return content;
    }

    /**
     * Checks to see what kind of lesson we are viewing and parses the "stage" parameter accordingly. Sets the stage for
     * the lesson using setStage on the lesson object.
     */
    private void updateCurrentScreenStage() {
        AbstractLesson al = getCurrentLesson();
        if (al instanceof SequentialLessonAdapter) {
            updateSlaStage((SequentialLessonAdapter) al);
        } else if (al instanceof RandomLessonAdapter) {
            updateRlaStage((RandomLessonAdapter) al);
        }
    }

    /**
     * Updates the stage for a RandomLessonAdapter
     *
     * @param al
     */
    private void updateRlaStage(RandomLessonAdapter rla) {
        try {
            if (!myParser.getRawParameter(STAGE).equals("null")) {
                int currentStage = myParser.getIntParameter(STAGE) - 1;
                if (previousStage != currentStage) {
                    previousStage = currentStage;
                    String[] stages = rla.getStages();
                    if (stages == null) {
                        stages = new String[0];
                    }
                    if (currentStage >= 0 && currentStage < stages.length) {
                        rla.setStage(this, stages[currentStage]);
                    }
                }
            } else {
                rla.setStage(this, null);
            }
        } catch (ParameterNotFoundException pnfe) {
            logger.warn("ParameterNotFoundException when updating stage for RandomLessonAdapter: " + pnfe.getMessage() + " " + pnfe.getCause());
        }
    }

    /**
     * Updates the stage for a SequentialLessonAdapter
     *
     * @param al
     */
    private void updateSlaStage(SequentialLessonAdapter sla) {
        int stage = myParser.getIntParameter(STAGE, sla.getStage(this));
        if (stage > 0 && stage <= sla.getStageCount()) {
            sla.setStage(this, stage);
        }
    }

    /**
     * Eats all the cookies and resets hintNum and previousStage
     */
    private void clearScreenProperties() {
        if (webgoatContext.isDebug()) {
            setMessage("Changed to a new screen, clearing cookies and hints");
        }
        eatCookies();
        hintNum = -1;
        previousStage = -1;
    }

    /**
     * Updates the properties currentScreen, previousScreen and hintNum depending on which scenario is being handled.
     *
     * @param request
     */
    private void updateScreenProperties(HttpServletRequest request) {
        // There are several scenarios where we want the first lesson to be loaded
        // 1) Previous screen is Welcome - Start of the course
        // 2) After a logout and after the session has been reinitialized
        if ((this.getPreviousScreen() == WebSession.WELCOME) || 
                (getRequest().getSession(false) != null &&
                // getRequest().getSession(false).isNew() &&
                this.getCurrentScreen() == WebSession.WELCOME && 
                this.getPreviousScreen() == WebSession.ERROR)) {
            currentScreen = course.getFirstLesson().getScreenId();
            hintNum = -1;
        }

        // System.out.println("Current Screen 2: " + currentScreen );
        // System.out.println("Previous Screen 2: " + previousScreen );
        // update the screen variables
        previousScreen = currentScreen;

        try {
            // If the request is new there should be no parameters.
            // This can occur from a session timeout or a the starting of a new course.
            if (!request.getSession().isNew()) {
                currentScreen = myParser.getIntParameter(SCREEN, currentScreen);
            } else {
                if (!myParser.getRawParameter(SCREEN, "NULL").equals("NULL")) {
                    this.setMessage("Session Timeout - Starting new Session.");
                }
            }
        } catch (Exception e) {
            logger.warn("Exception when updating properties in updateScreenProperties: " + e.getMessage() + " " + e.getCause());
        }
    }

    /**
     * Updates the labelmanager local based on the labelManager bean
     *
     * @param request
     */
    private void updateLocale(HttpServletRequest request) {
        Locale locale = request.getLocale();
        if (locale != null) {
            LabelManager labelManager = BeanProvider.getBean("labelManager", LabelManager.class);
            labelManager.setLocale(locale);
        }
    }

    /**
     * Creates a new parser if not created yet. Sets the request on the parser for later use.
     *
     * @param request
     */
    private void updateParser(HttpServletRequest request) {
        if (myParser == null) {
            myParser = new ParameterParser(request);
        } else {
            myParser.update(request);
        }
    }

    /**
     * <p> updateLastAttackRequestInfo. </p>
     *
     * @param request a {@link javax.servlet.http.HttpServletRequest} object.
     */
    public void updateLastAttackRequestInfo(HttpServletRequest request) {
        // store cookies
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            this.cookiesOnLastRequest = new ArrayList<Cookie>();
        } else {
            this.cookiesOnLastRequest = Arrays.asList(cookies);
        }
        // store parameters
        Map<String, String[]> parmMap = request.getParameterMap();
        logger.info("PARM MAP: " + parmMap);
        if (parmMap == null) {
            this.parmsOnLastRequest = new ArrayList<RequestParameter>();
        } else {
            this.parmsOnLastRequest = new ArrayList<RequestParameter>();
            for (String name : parmMap.keySet()) {
                String[] values = parmMap.get(name);
                String delim = "";
                StringBuffer sb = new StringBuffer();
                if (values != null && values.length > 0) {
                    for (String parm : values) {
                        sb.append(delim).append(parm);
                        delim = ",";
                    }
                }
                RequestParameter parm = new RequestParameter(name, sb.toString());
                this.parmsOnLastRequest.add(parm);
            }
        }
    }

    /**
     * <p> restartLesson. </p>
     *
     * @param lessonId a int.
     */
    public void restartLesson(int lessonId) {
        AbstractLesson al = getLesson(lessonId);
        System.out.println("Restarting lesson: " + al);
        al.restartLesson();
        al.getLessonTracker(this).setCompleted(false);
        if (al instanceof SequentialLessonAdapter) {
            SequentialLessonAdapter sla = (SequentialLessonAdapter) al;
            sla.getLessonTracker(this).setStage(1);
        } else if (al instanceof RandomLessonAdapter) {
            RandomLessonAdapter rla = (RandomLessonAdapter) al;
            rla.setStage(this, rla.getStages()[0]);
        }
    }

    /**
     * <p> setHasHackableAdmin. </p>
     *
     * @param role a {@link java.lang.String} object.
     */
    public void setHasHackableAdmin(String role) {
        hasHackedHackableAdmin = (AbstractLesson.HACKED_ADMIN_ROLE.equals(role) & hasHackedHackableAdmin);

        // if the user got the Admin=true parameter correct AND they accessed an admin screen
        if (hasHackedHackableAdmin) {
            completedHackableAdmin = true;
        }
    }

    /**
     * <p> isDebug. </p>
     *
     * @return Returns the isDebug.
     */
    public boolean isDebug() {
        return isDebug;
    }

    /**
     * <p> getHeader. </p>
     *
     * @param header - request header value to return
     * @return a {@link java.lang.String} object.
     */
    public String getHeader(String header) {
        return getRequest().getHeader(header);
    }

    /**
     * <p> getNextHint. </p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getNextHint() {
        String hint = null;

        // FIXME
        int maxHints = getCurrentLesson().getHintCount(this);
        if (hintNum < maxHints - 1) {
            hintNum++;

            // Hints are indexed from 0
            getCurrentLesson().getLessonTracker(this).setMaxHintLevel(getHintNum() + 1);

            hint = (String) getCurrentLesson().getHint(this, getHintNum());
        }

        return hint;
    }

    /**
     * <p> getPreviousHint. </p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getPreviousHint() {
        String hint = null;

        if (hintNum > 0) {
            hintNum--;

            // Hints are indexed from 0
            getCurrentLesson().getLessonTracker(this).setMaxHintLevel(getHintNum() + 1);

            hint = (String) getCurrentLesson().getHint(this, getHintNum());
        }

        return hint;
    }

    /**
     * <p> Setter for the field <code>currentMenu</code>. </p>
     *
     * @param ranking a {@link java.lang.Integer} object.
     */
    public void setCurrentMenu(Integer ranking) {
        currentMenu = ranking.intValue();
    }

    /**
     * <p> Getter for the field <code>currentMenu</code>. </p>
     *
     * @return a int.
     */
    public int getCurrentMenu() {
        return currentMenu;
    }

    /**
     * <p> Getter for the field <code>webgoatContext</code>. </p>
     *
     * @return a {@link org.owasp.webgoat.session.WebgoatContext} object.
     */
    public WebgoatContext getWebgoatContext() {
        return webgoatContext;
    }

    /**
     * <p> getCurrrentLanguage. </p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getCurrrentLanguage() {
        return currentLanguage;
    }

    /**
     * <p> Getter for the field <code>cookiesOnLastRequest</code>. </p>
     *
     * @return the cookiesOnLastRequest
     */
    public List<Cookie> getCookiesOnLastRequest() {
        return cookiesOnLastRequest;
    }

    /**
     * <p> Getter for the field <code>parmsOnLastRequest</code>. </p>
     *
     * @return the parmsOnLastRequest
     */
    public List<RequestParameter> getParmsOnLastRequest() {
        return parmsOnLastRequest;
    }

}
