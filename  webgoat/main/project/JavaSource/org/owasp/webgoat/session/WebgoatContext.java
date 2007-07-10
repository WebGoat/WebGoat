package org.owasp.webgoat.session;

import java.sql.Connection;

import javax.servlet.http.HttpServlet;

public class WebgoatContext {

	public final static String DATABASE_CONNECTION_STRING = "DatabaseConnectionString";

	public final static String DATABASE_DRIVER = "DatabaseDriver";

	public final static String DATABASE_USER = "DatabaseUser";

	public final static String DATABASE_PASSWORD = "DatabasePassword";

	public final static String ENTERPRISE = "Enterprise";

	public final static String SHOWCOOKIES = "ShowCookies";

	public final static String SHOWPARAMS = "ShowParams";

	public final static String SHOWREQUEST = "ShowRequest";

	public final static String SHOWSOURCE = "ShowSource";
	
	public final static String SHOWHINTS = "ShowHints";

	public final static String DEFUSEOSCOMMANDS = "DefuseOSCommands";

	public final static String FEEDBACK_ADDRESS = "FeedbackAddress";

	public final static String DEBUG = "debug";

	private static boolean databaseBuilt = false;
	
	private String databaseConnectionString;

	private String realConnectionString = null;

	private String databaseDriver;

	private String databaseUser;

	private String databasePassword;

	private boolean showCookies = false;

	private boolean showParams = false;

	private boolean showRequest = false;

	private boolean showSource = false;

	private boolean defuseOSCommands = false;

	private boolean enterprise = false;

	private String feedbackAddress = "<A HREF=mailto:webgoat@g2-inc.com>webgoat@g2-inc.com</A>";

	private boolean isDebug = false;

	private String servletName;

	private HttpServlet servlet;

	public WebgoatContext(HttpServlet servlet) {
		this.servlet = servlet;
		databaseConnectionString = servlet
				.getInitParameter(DATABASE_CONNECTION_STRING);
		databaseDriver = servlet.getInitParameter(DATABASE_DRIVER);
		databaseUser = servlet.getInitParameter(DATABASE_USER);
		databasePassword = servlet.getInitParameter(DATABASE_PASSWORD);
		
		// initialize from web.xml
		showParams = "true".equals( servlet.getInitParameter( SHOWPARAMS ) );
		showCookies = "true".equals( servlet.getInitParameter( SHOWCOOKIES ) );
		showSource = "true".equals( servlet.getInitParameter( SHOWSOURCE ) );
		defuseOSCommands = "true".equals( servlet.getInitParameter( DEFUSEOSCOMMANDS ) );
		enterprise = "true".equals( servlet.getInitParameter( ENTERPRISE ) );
		feedbackAddress = servlet.getInitParameter( FEEDBACK_ADDRESS ) != null ? servlet
				.getInitParameter( FEEDBACK_ADDRESS ) : feedbackAddress;
		showRequest = "true".equals( servlet.getInitParameter( SHOWREQUEST ) );
		isDebug = "true".equals( servlet.getInitParameter( DEBUG ) );
		servletName = servlet.getServletName();
		
		// FIXME: need to solve concurrency problem here -- make tables for this user
		if ( !databaseBuilt ) {
			try {
				Connection conn = DatabaseUtilities.makeConnection(this);
				new CreateDB().makeDB(conn);
				conn.close();
				databaseBuilt = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * returns the connection string with the real path to the database
	 * directory inserted at the word PATH
	 * 
	 * @return The databaseConnectionString value
	 */
	public String getDatabaseConnectionString() {
		if (realConnectionString == null)
			try {
				String path = servlet.getServletContext().getRealPath(
						"/database").replace('\\', '/');
				System.out.println("PATH: " + path);
				realConnectionString = databaseConnectionString.replaceAll(
						"PATH", path);
				System.out.println("Database Connection String: "
						+ realConnectionString);
			} catch (Exception e) {
				System.out
						.println("Couldn't open database: check web.xml database parameters");
				e.printStackTrace();
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

	public boolean isDefuseOSCommands() {
		return defuseOSCommands;
	}

	public boolean isEnterprise() {
		return enterprise;
	}

	public String getFeedbackAddress() {
		return feedbackAddress;
	}

	public boolean isDebug() {
		return isDebug;
	}

	public String getServletName() {
		return servletName;
	}

	public boolean isShowCookies() {
		return showCookies;
	}

	public boolean isShowParams() {
		return showParams;
	}

	public boolean isShowRequest() {
		return showRequest;
	}

	public boolean isShowSource() {
		return showSource;
	}

}
