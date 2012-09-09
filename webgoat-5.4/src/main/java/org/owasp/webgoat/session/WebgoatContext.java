
package org.owasp.webgoat.session;

import java.util.Iterator;
import javax.servlet.http.HttpServlet;

import org.owasp.webgoat.util.WebGoatI18N;


public class WebgoatContext
{

	public final static String DATABASE_CONNECTION_STRING = "DatabaseConnectionString";

	public final static String DATABASE_DRIVER = "DatabaseDriver";

	public final static String DATABASE_USER = "DatabaseUser";

	public final static String DATABASE_PASSWORD = "DatabasePassword";

	public final static String ENTERPRISE = "Enterprise";

	public final static String CODING_EXERCISES = "CodingExercises";

	public final static String SHOWCOOKIES = "ShowCookies";

	public final static String SHOWPARAMS = "ShowParams";

	public final static String SHOWREQUEST = "ShowRequest";

	public final static String SHOWSOURCE = "ShowSource";

	public final static String SHOWSOLUTION = "ShowSolution";

	public final static String SHOWHINTS = "ShowHints";

	public final static String DEFUSEOSCOMMANDS = "DefuseOSCommands";

	public final static String FEEDBACK_ADDRESS_HTML = "FeedbackAddressHTML";

	public final static String FEEDBACK_ADDRESS = "email";

	public final static String DEBUG = "debug";
	
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

	private boolean defuseOSCommands = false;

	private boolean enterprise = false;

	private boolean codingExercises = false;

	private String feedbackAddress = "webgoat@owasp.org";

	private String feedbackAddressHTML = "<A HREF=mailto:webgoat@owasp.org>webgoat@owasp.org</A>";

	private boolean isDebug = false;

	private String servletName;

	private HttpServlet servlet;
	
	private String defaultLanguage;
	
	private WebGoatI18N webgoati18n = null;

	public WebgoatContext(HttpServlet servlet)
	{
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
		defuseOSCommands = "true".equals(getParameter(servlet, DEFUSEOSCOMMANDS));
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
		defaultLanguage = getParameter(servlet,DEFAULTLANGUAGE)!=null ? new String(getParameter(servlet, DEFAULTLANGUAGE)): new String("English");
		
		webgoati18n = new WebGoatI18N(this);
		
	}

	private String getParameter(HttpServlet servlet, String key)
	{
		String value = System.getenv().get(key);
		if (value == null) value = servlet.getInitParameter(key);
		return value;
	}

	/**
	 * returns the connection string with the real path to the database directory inserted at the
	 * word PATH
	 * 
	 * @return The databaseConnectionString value
	 */
	public String getDatabaseConnectionString()
	{
		if (realConnectionString == null) try
		{
			String path = servlet.getServletContext().getRealPath("/database").replace('\\', '/');
			System.out.println("PATH: " + path);
			realConnectionString = databaseConnectionString.replaceAll("PATH", path);
			System.out.println("Database Connection String: " + realConnectionString);
		} catch (Exception e)
		{
			System.out.println("Couldn't open database: check web.xml database parameters");
			e.printStackTrace();
		}
		return realConnectionString;
	}

	/**
	 * Gets the databaseDriver attribute of the WebSession object
	 * 
	 * @return The databaseDriver value
	 */
	public String getDatabaseDriver()
	{
		return (databaseDriver);
	}

	/**
	 * Gets the databaseUser attribute of the WebSession object
	 * 
	 * @return The databaseUser value
	 */
	public String getDatabaseUser()
	{
		return (databaseUser);
	}

	/**
	 * Gets the databasePassword attribute of the WebSession object
	 * 
	 * @return The databasePassword value
	 */
	public String getDatabasePassword()
	{
		return (databasePassword);
	}

	public boolean isDefuseOSCommands()
	{
		return defuseOSCommands;
	}

	public boolean isEnterprise()
	{
		return enterprise;
	}

	public boolean isCodingExercises()
	{
		return codingExercises;
	}

	public String getFeedbackAddress()
	{
		return feedbackAddress;
	}

	public String getFeedbackAddressHTML()
	{
		return feedbackAddressHTML;
	}

	public boolean isDebug()
	{
		return isDebug;
	}

	public String getServletName()
	{
		return servletName;
	}

	public boolean isShowCookies()
	{
		return showCookies;
	}

	public boolean isShowParams()
	{
		return showParams;
	}

	public boolean isShowRequest()
	{
		return showRequest;
	}

	public boolean isShowSource()
	{
		return showSource;
	}

	public boolean isShowSolution()
	{
		return showSolution;
	}

	public String getDefaultLanguage() {
		return defaultLanguage;
	}

	public void setWebgoatiI18N(WebGoatI18N webgoati18n) {
		this.webgoati18n = webgoati18n;
	}

	public WebGoatI18N getWebgoatI18N() {
		return webgoati18n;
	}
	
}
