package org.owasp.webgoat.session;

import java.sql.Connection;

import javax.servlet.http.HttpServlet;

import org.owasp.webgoat.lessons.admin.RefreshDBScreen;

public class WebgoatContext {

	public final static String DATABASE_CONNECTION_STRING = "DatabaseConnectionString";

	public final static String DATABASE_DRIVER = "DatabaseDriver";

	public final static String DATABASE_USER = "DatabaseUser";

	public final static String DATABASE_PASSWORD = "DatabasePassword";

	private static boolean databaseBuilt = false;
	
	private String databaseConnectionString;

	private String realConnectionString = null;

	private String databaseDriver;

	private String databaseUser;

	private String databasePassword;

	private HttpServlet servlet;

	public WebgoatContext(HttpServlet servlet) {
		this.servlet = servlet;
		databaseConnectionString = servlet
				.getInitParameter(DATABASE_CONNECTION_STRING);
		databaseDriver = servlet.getInitParameter(DATABASE_DRIVER);
		databaseUser = servlet.getInitParameter(DATABASE_USER);
		databasePassword = servlet.getInitParameter(DATABASE_PASSWORD);
		
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

}
