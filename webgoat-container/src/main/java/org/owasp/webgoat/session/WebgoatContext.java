package org.owasp.webgoat.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;

/**
 * <p>WebgoatContext class.</p>
 *
 * @version $Id: $Id
 * @author dm
 */
public class WebgoatContext {

    final Logger logger = LoggerFactory.getLogger(WebgoatContext.class);

    /** Constant <code>DATABASE_CONNECTION_STRING="DatabaseConnectionString"</code> */
    public final static String DATABASE_CONNECTION_STRING = "DatabaseConnectionString";

    /** Constant <code>DATABASE_DRIVER="DatabaseDriver"</code> */
    public final static String DATABASE_DRIVER = "DatabaseDriver";

    /** Constant <code>DATABASE_USER="DatabaseUser"</code> */
    public final static String DATABASE_USER = "DatabaseUser";

    /** Constant <code>DATABASE_PASSWORD="DatabasePassword"</code> */
    public final static String DATABASE_PASSWORD = "DatabasePassword";

    /** Constant <code>ENTERPRISE="Enterprise"</code> */
    public final static String ENTERPRISE = "Enterprise";

    /** Constant <code>CODING_EXERCISES="CodingExercises"</code> */
    public final static String CODING_EXERCISES = "CodingExercises";

    /** Constant <code>SHOWCOOKIES="ShowCookies"</code> */
    public final static String SHOWCOOKIES = "ShowCookies";

    /** Constant <code>SHOWPARAMS="ShowParams"</code> */
    public final static String SHOWPARAMS = "ShowParams";

    /** Constant <code>SHOWREQUEST="ShowRequest"</code> */
    public final static String SHOWREQUEST = "ShowRequest";

    /** Constant <code>SHOWSOURCE="ShowSource"</code> */
    public final static String SHOWSOURCE = "ShowSource";

    /** Constant <code>SHOWSOLUTION="ShowSolution"</code> */
    public final static String SHOWSOLUTION = "ShowSolution";

    /** Constant <code>SHOWHINTS="ShowHints"</code> */
    public final static String SHOWHINTS = "ShowHints";

    /** Constant <code>FEEDBACK_ADDRESS_HTML="FeedbackAddressHTML"</code> */
    public final static String FEEDBACK_ADDRESS_HTML = "FeedbackAddressHTML";

    /** Constant <code>FEEDBACK_ADDRESS="email"</code> */
    public final static String FEEDBACK_ADDRESS = "email";

    /** Constant <code>DEBUG="debug"</code> */
    public final static String DEBUG = "debug";

    /** Constant <code>DEFAULTLANGUAGE="DefaultLanguage"</code> */
    public final static String DEFAULTLANGUAGE = "DefaultLanguage";

    private String databaseConnectionString;

    private String realConnectionString = null;

    private String databaseDriver;

    private String databaseUser;

    private String databasePassword;

    private boolean showCookies = false;

    private boolean showParams = false;

    private boolean showRequest = false;

    private boolean showSource = false;

    private boolean showSolution = false;

    private boolean enterprise = false;

    private boolean codingExercises = false;

    private String feedbackAddress = "owasp-webgoat@list.owasp.org";

    private String feedbackAddressHTML = "<A HREF=mailto:owasp-webgoat@list.owasp.org>owasp-webgoat@list.owasp.org</A>";

    private boolean isDebug = false;

    private String servletName;

    private HttpServlet servlet;

    private String defaultLanguage;

    private java.nio.file.Path pluginDirectory;

    /**
     * <p>Constructor for WebgoatContext.</p>
     *
     * @param servlet a {@link javax.servlet.http.HttpServlet} object.
     */
    public WebgoatContext(HttpServlet servlet) {
        this.servlet = servlet;
        databaseConnectionString = getParameter(servlet, DATABASE_CONNECTION_STRING);
        databaseDriver = getParameter(servlet, DATABASE_DRIVER);
        databaseUser = getParameter(servlet, DATABASE_USER);
        databasePassword = getParameter(servlet, DATABASE_PASSWORD);

        // initialize from web.xml
        showParams = "true".equals(getParameter(servlet, SHOWPARAMS));
        showCookies = "true".equals(getParameter(servlet, SHOWCOOKIES));
        showSource = "true".equals(getParameter(servlet, SHOWSOURCE));
        showSolution = "true".equals(getParameter(servlet, SHOWSOLUTION));
        enterprise = "true".equals(getParameter(servlet, ENTERPRISE));
        codingExercises = "true".equals(getParameter(servlet, CODING_EXERCISES));
        feedbackAddressHTML = getParameter(servlet, FEEDBACK_ADDRESS_HTML) != null ? getParameter(servlet,
                FEEDBACK_ADDRESS_HTML)
                : feedbackAddressHTML;
        feedbackAddress = getParameter(servlet, FEEDBACK_ADDRESS) != null ? getParameter(servlet, FEEDBACK_ADDRESS)
                : feedbackAddress;
        showRequest = "true".equals(getParameter(servlet, SHOWREQUEST));
        isDebug = "true".equals(getParameter(servlet, DEBUG));
        servletName = servlet.getServletName();
        defaultLanguage = getParameter(servlet, DEFAULTLANGUAGE) != null ? new String(getParameter(servlet, DEFAULTLANGUAGE)) : new String("en");
    }

    private String getParameter(HttpServlet servlet, String key) {
        String value = System.getenv().get(key);
        if (value == null) {
            value = servlet.getInitParameter(key);
        }
        return value;
    }

    /**
     * returns the connection string with the real path to the database
     * directory inserted at the word PATH
     *
     * @return The databaseConnectionString value
     */
    public String getDatabaseConnectionString() {
        if (realConnectionString == null) {
            try {
                String path = servlet.getServletContext().getRealPath("/database").replace('\\', '/');
                System.out.println("PATH: " + path);
                realConnectionString = databaseConnectionString.replaceAll("PATH", path);
                System.out.println("Database Connection String: " + realConnectionString);
            } catch (Exception e) {
                logger.error("Couldn't open database: check web.xml database parameters", e);
            }
        }
        return realConnectionString;
    }

    /**
     * Gets the databaseDriver attribute of the WebSession object
     *
     * @return The databaseDriver value
     */
    public String getDatabaseDriver() {
        return (databaseDriver);
    }

    /**
     * Gets the databaseUser attribute of the WebSession object
     *
     * @return The databaseUser value
     */
    public String getDatabaseUser() {
        return (databaseUser);
    }

    /**
     * Gets the databasePassword attribute of the WebSession object
     *
     * @return The databasePassword value
     */
    public String getDatabasePassword() {
        return (databasePassword);
    }
    
    /**
     * <p>isEnterprise.</p>
     *
     * @return a boolean.
     */
    public boolean isEnterprise() {
        return enterprise;
    }

    /**
     * <p>isCodingExercises.</p>
     *
     * @return a boolean.
     */
    public boolean isCodingExercises() {
        return codingExercises;
    }

    /**
     * <p>Getter for the field <code>feedbackAddress</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getFeedbackAddress() {
        return feedbackAddress;
    }

    /**
     * <p>Getter for the field <code>feedbackAddressHTML</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getFeedbackAddressHTML() {
        return feedbackAddressHTML;
    }

    /**
     * <p>isDebug.</p>
     *
     * @return a boolean.
     */
    public boolean isDebug() {
        return isDebug;
    }

    /**
     * <p>Getter for the field <code>servletName</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getServletName() {
        return servletName;
    }

    /**
     * <p>isShowCookies.</p>
     *
     * @return a boolean.
     */
    public boolean isShowCookies() {
        return showCookies;
    }

    /**
     * <p>isShowParams.</p>
     *
     * @return a boolean.
     */
    public boolean isShowParams() {
        return showParams;
    }

    /**
     * <p>isShowRequest.</p>
     *
     * @return a boolean.
     */
    public boolean isShowRequest() {
        return showRequest;
    }

    /**
     * <p>isShowSource.</p>
     *
     * @return a boolean.
     */
    public boolean isShowSource() {
        return showSource;
    }

    /**
     * <p>isShowSolution.</p>
     *
     * @return a boolean.
     */
    public boolean isShowSolution() {
        return showSolution;
    }

    /**
     * <p>Getter for the field <code>defaultLanguage</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getDefaultLanguage() {
        return defaultLanguage;
    }
}
