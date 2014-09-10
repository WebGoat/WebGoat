package org.owasp.webgoat;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.owasp.webgoat.lessons.AbstractLesson;
import org.owasp.webgoat.lessons.WelcomeScreen;
import org.owasp.webgoat.lessons.admin.WelcomeAdminScreen;
import org.owasp.webgoat.session.Course;
import org.owasp.webgoat.session.ErrorScreen;
import org.owasp.webgoat.session.Screen;
import org.owasp.webgoat.session.UserTracker;
import org.owasp.webgoat.session.WebSession;
import org.owasp.webgoat.session.WebgoatContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * *************************************************************************************************
 *
 *
 * This file is part of WebGoat, an Open Web Application Security Project
 * utility. For details, please see http://www.owasp.org/
 *
 * Copyright (c) 2002 - 20014 Bruce Mayhew
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * Getting Source ==============
 *
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository
 * for free software projects.
 *
 * For details, please see http://webgoat.github.io
 *
 *
 * @author Jeff Williams <a href="http://www.aspectsecurity.com">Aspect
 * Security</a>
 * @author Bruce Mayhew <a href="http://code.google.com/p/webgoat">WebGoat</a>
 * @created October 28, 2003
 */
public class HammerHead extends HttpServlet {

    final Logger logger = LoggerFactory.getLogger(HammerHead.class);

    private static final String WELCOMED = "welcomed";

    /**
     *
     */
    private static final long serialVersionUID = 645640331343188020L;

    /**
     * Description of the Field
     */
    protected static SimpleDateFormat httpDateFormat;

    /**
     * Set the session timeout to be 2 days
     */
    private final static int sessionTimeoutSeconds = 60 * 60 * 24 * 2;

    // private final static int sessionTimeoutSeconds = 1;
    /**
     * Properties file path
     */
    public static String propertiesPath = null;

    /**
     * provides convenience methods for getting setup information from the
     * ServletContext
     */
    private WebgoatContext webgoatContext = null;

    /**
     * Description of the Method
     *
     * @param request Description of the Parameter
     * @param response Description of the Parameter
     * @exception IOException Description of the Exception
     * @exception ServletException Description of the Exception
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doPost(request, response);
    }

    /**
     * Description of the Method
     *
     * @param request Description of the Parameter
     * @param response Description of the Parameter
     * @exception IOException Description of the Exception
     * @exception ServletException Description of the Exception
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Screen screen = null;

        WebSession mySession = null;
        try {
            logger.debug("Entering doPost");
            logger.debug("request: " + request);
            logger.debug("principle: " + request.getUserPrincipal());
            // setCacheHeaders(response, 0);
            ServletContext context = getServletContext();

            // FIXME: If a response is written by updateSession(), do not
            // call makeScreen() and writeScreen()
            mySession = updateSession(request, response, context);
            
            if (response.isCommitted()) {
                logger.debug("Response already committed, exiting");
                return;
            }
            
            if ("true".equals(request.getParameter("start")) || request.getQueryString() == null) {
                logger.warn("Redirecting to start controller");
                response.sendRedirect("start.mvc");
                return;
            }

            // Note: For the lesson to track the status, we need to update
            // the lesson tracker object
            // from the screen.createContent() method. The create content is
            // the only point
            // where the lesson "knows" what has happened. To track it at a
            // latter point would
            // require the lesson to have memory.
            screen = makeScreen(mySession);
            // This calls the lesson's
            // handleRequest()
            if (response.isCommitted()) {
                return;
            }

            // perform lesson-specific tracking activities
            if (screen instanceof AbstractLesson) {
                AbstractLesson lesson = (AbstractLesson) screen;

                // we do not count the initial display of the lesson screen as a visit
                if ("GET".equals(request.getMethod())) {
                    String uri = request.getRequestURI() + "?" + request.getQueryString();
                    if (!uri.endsWith(lesson.getLink())) {
                        screen.getLessonTracker(mySession).incrementNumVisits();
                    }
                } else if ("POST".equals(request.getMethod())
                        && mySession.getPreviousScreen() == mySession.getCurrentScreen()) {
                    screen.getLessonTracker(mySession).incrementNumVisits();
                }
            }

            // log the access to this screen for this user
            UserTracker userTracker = UserTracker.instance();
            userTracker.update(mySession, screen);
            log(request, screen.getClass().getName() + " | " + mySession.getParser().toString());

            // Redirect the request to our View servlet
            String userAgent = request.getHeader("user-agent");
            String clientBrowser = "Not known!";
            if (userAgent != null) {
                clientBrowser = userAgent;
            }
            request.setAttribute("client.browser", clientBrowser);
            // removed - this is being done in updateSession call
            //request.getSession().setAttribute(WebSession.SESSION, mySession);
            // not sure why this is being set in the session?
            //request.getSession().setAttribute(WebSession.COURSE, mySession.getCourse());
            String viewPage = getViewPage(mySession);
            logger.debug("Forwarding to view: " + viewPage);
            logger.debug("Screen: " + screen);
            request.getRequestDispatcher(viewPage).forward(request, response);
        } catch (Throwable t) {
            logger.error("Error handling request", t);
            screen = new ErrorScreen(mySession, t);
        } finally {
            try {
                if (screen instanceof ErrorScreen) {
                    this.writeScreen(mySession, screen, response);
                }
            } catch (Throwable thr) {
                logger.error("Could not write error screen", thr);
            }
            WebSession.returnConnection(mySession);
            logger.debug("Leaving doPost: ");
        }
    }

    private String getViewPage(WebSession webSession) {
        // now always display the lesson content
        String page = "/lesson_content.jsp";
        //page = "/main.jsp";
        return page;
    }

    /**
     * Description of the Method
     *
     * @param date Description of the Parameter
     * @return RFC 1123 http date format
     */
    protected static String formatHttpDate(Date date) {
        synchronized (httpDateFormat) {
            return httpDateFormat.format(date);
        }
    }

    /**
     * Return information about this servlet
     *
     * @return The servletInfo value
     */
    @Override
    public String getServletInfo() {
        return "WebGoat is sponsored by Aspect Security.";
    }

    /**
     * Return properties path
     *
     * @throws javax.servlet.ServletException
     */
    @Override
    public void init() throws ServletException {
        logger.info("Initializing main webgoat servlet");
        httpDateFormat = new SimpleDateFormat("EEE, dd MMM yyyyy HH:mm:ss z", Locale.US);
        httpDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        propertiesPath = getServletContext().getRealPath("/WEB-INF/webgoat.properties");
        webgoatContext = new WebgoatContext(this);
    }

    /**
     * Description of the Method
     *
     * @param request Description of the Parameter
     * @param message Description of the Parameter
     */
    public void log(HttpServletRequest request, String message) {
        String output = new Date() + " | " + request.getRemoteHost() + ":" + request.getRemoteAddr() + " | " + message;
        log(output);
        logger.debug(output);
    }

    /*
     * public List getLessons(Category category, String role) { Course course =
     * mySession.getCourse(); // May need to clone the List before returning it. //return new
     * ArrayList(course.getLessons(category, role)); return course.getLessons(category, role); }
     */
    /**
     * Description of the Method
     *
     * @param s Description of the Parameter
     * @return Description of the Return Value
     */
    protected Screen makeScreen(WebSession s) {
        Screen screen = null;
        int scr = s.getCurrentScreen();
        Course course = s.getCourse();

        if (s.isUser() || s.isChallenge()) {
            if (scr == WebSession.WELCOME) {
                screen = new WelcomeScreen(s);
            } else {
                AbstractLesson lesson = course.getLesson(s, scr, AbstractLesson.USER_ROLE);
                if (lesson == null && s.isHackedAdmin()) {
                    // If admin was hacked, let the user see some of the
                    // admin screens
                    lesson = course.getLesson(s, scr, AbstractLesson.HACKED_ADMIN_ROLE);
                }

                if (lesson != null) {
                    screen = lesson;

                    // We need to do some bookkeeping for the hackable admin
                    // interface.
                    // This is the only place we can tell if the user
                    // successfully hacked the hackable
                    // admin and has actually accessed an admin screen. You
                    // need BOTH pieces of information
                    // in order to satisfy the remote admin lesson.
                    s.setHasHackableAdmin(screen.getRole());

                    lesson.handleRequest(s);
                    s.setCurrentMenu(lesson.getCategory().getRanking());
                } else {
                    screen = new ErrorScreen(s, "Invalid screen requested.  Try: http://localhost/WebGoat/attack");
                }
            }
        } else if (s.isAdmin()) {
            if (scr == WebSession.WELCOME) {
                screen = new WelcomeAdminScreen(s);
            } else {
                // Admin can see all roles.
                // FIXME: should be able to pass a list of roles.
                AbstractLesson lesson = course.getLesson(s, scr, AbstractLesson.ADMIN_ROLE);
                if (lesson == null) {
                    lesson = course.getLesson(s, scr, AbstractLesson.HACKED_ADMIN_ROLE);
                }
                if (lesson == null) {
                    lesson = course.getLesson(s, scr, AbstractLesson.USER_ROLE);
                }

                if (lesson != null) {
                    screen = lesson;

                    // We need to do some bookkeeping for the hackable admin
                    // interface.
                    // This is the only place we can tell if the user
                    // successfully hacked the hackable
                    // admin and has actually accessed an admin screen. You
                    // need BOTH pieces of information
                    // in order to satisfy the remote admin lesson.
                    s.setHasHackableAdmin(screen.getRole());

                    lesson.handleRequest(s);
                    s.setCurrentMenu(lesson.getCategory().getRanking());
                } else {
                    screen = new ErrorScreen(s,
                            "Invalid screen requested.  Try Setting Admin to false or Try: http://localhost/WebGoat/attack");
                }
            }
        }

        return (screen);
    }

    /**
     * This method sets the required expiration headers in the response for a
     * given RunData object. This method attempts to set all relevant headers,
     * both for HTTP 1.0 and HTTP 1.1.
     *
     * @param response The new cacheHeaders value
     * @param expiry The new cacheHeaders value
     */
    protected static void setCacheHeaders(HttpServletResponse response, int expiry) {
        if (expiry == 0) {
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Expires", formatHttpDate(new Date()));
        } else {
            Date expiryDate = new Date(System.currentTimeMillis() + expiry);
            response.setHeader("Expires", formatHttpDate(expiryDate));
        }
    }

    /**
     * Description of the Method
     *
     * @param request Description of the Parameter
     * @param response Description of the Parameter
     * @param context Description of the Parameter
     * @return Description of the Return Value
     * @throws java.io.IOException
     */
    protected WebSession updateSession(HttpServletRequest request, HttpServletResponse response, ServletContext context)
            throws IOException {
        HttpSession hs;
        // session should already be created by spring security
        hs = request.getSession(false);

        logger.debug("HH Entering Session_id: " + hs.getId());
        // dumpSession( hs );
        // Get our session object out of the HTTP session
        WebSession session = null;
        Object o = hs.getAttribute(WebSession.SESSION);

        if ((o != null) && o instanceof WebSession) {
            session = (WebSession) o;
            hs.setAttribute(WebSession.COURSE, session.getCourse());
        } else {
            // Create new custom session and save it in the HTTP session
            logger.warn("HH Creating new WebSession");
            session = new WebSession(webgoatContext, context);
            // Ensure splash screen shows on any restart
            // rlawson - removed this since we show splash screen at login now
            //hs.removeAttribute(WELCOMED);
            hs.setAttribute(WebSession.SESSION, session);
            // reset timeout
            hs.setMaxInactiveInterval(sessionTimeoutSeconds);
        }

        session.update(request, response, this.getServletName());
        // update last attack request info (cookies, parms)
        // this is so the REST services can have access to them via the session 
        session.updateLastAttackRequestInfo(request);

        // to authenticate
        logger.debug("HH Leaving Session_id: " + hs.getId());
        //dumpSession( hs );
        return (session);
    }

    /**
     * Description of the Method
     *
     * @param s Description of the Parameter
     * @param screen
     * @param response Description of the Parameter
     * @exception IOException Description of the Exception
     */
    protected void writeScreen(WebSession s, Screen screen, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        if (s == null) {
            screen = new ErrorScreen(s, "Page to display was null");
        }

        // set the content-length of the response.
        // Trying to avoid chunked-encoding. (Aspect required)
        response.setContentLength(screen.getContentLength());
        response.setHeader("Content-Length", screen.getContentLength() + "");

        screen.output(out);
        out.flush();
        out.close();
    }
}
