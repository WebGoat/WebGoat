package org.owasp.webgoat.session;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import org.apache.ecs.MultiPartElement;
import org.apache.ecs.html.B;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;


/**
 * Copyright (c) 2002 Free Software Foundation developed under the custody of
 * the Open Web Application Security Project (http://www.owasp.org) This
 * software package org.owasp.webgoat.is published by OWASP under the GPL. You should read and
 * accept the LICENSE before you use, modify and/or redistribute this
 * software.
 *
 * @author Jeff Williams <a href="http://www.aspectsecurity.com">Aspect Security</a>
 */
public class DatabaseUtilities
{
	public static String servletContextRealPath = null;
    /**
     * Description of the Method
     *
     * @param s Description of the Parameter
     *
     * @return Description of the Return Value
     *
     * @exception ClassNotFoundException Description of the Exception
     * @exception SQLException Description of the Exception
     */
	public static Connection makeConnection(WebSession s) throws ClassNotFoundException, SQLException
	{
		Class.forName(s.getDatabaseDriver());
	
		return (DriverManager.getConnection(s.getDatabaseConnectionString()));
	}
	
	public static Connection makeConnection(String driverName, String connectionString) 
			throws ClassNotFoundException, SQLException
	{
		Class.forName(driverName);
	
		return (DriverManager.getConnection(connectionString));
	}
	    
    public static Connection makeConnection() {
    	try 
		{
    		// FIXME: Work around for not having a session object with the web service lessons
    		//        This is the same "logic" in the web.xml file
    		//        Get the path to webgoat database
    		
    		String dbName = (servletContextRealPath + "database" + File.separator);
    		String os = System.getProperty("os.name","Windows");
    		if ( os.toLowerCase().indexOf("window") != -1  )
    		{
        		dbName = dbName.concat("webgoat.mdb");
        		System.out.println("DBName: " + dbName);    		
				Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");			
				return DriverManager.getConnection("jdbc:odbc:;DRIVER=Microsoft Access Driver (*.mdb);DBQ=" + dbName + ";PWD=webgoat");
    		}
    		else 
    		{
    			dbName = dbName.concat("database.prp");
        	    Class.forName("org.enhydra.instantdb.jdbc.idbDriver");			
                return DriverManager.getConnection("jdbc:idb:" + dbName);
    		}
     	}
    	catch (Exception e) 
		{
    		e.printStackTrace();
    		return null;
    	}
    }


    /**
     * Description of the Method
     *
     * @param results Description of the Parameter
     * @param resultsMetaData Description of the Parameter
     *
     * @return Description of the Return Value
     *
     * @exception IOException Description of the Exception
     * @exception SQLException Description of the Exception
     */
    public static MultiPartElement writeTable(ResultSet results, ResultSetMetaData resultsMetaData) throws IOException, SQLException
    {
        int numColumns = resultsMetaData.getColumnCount();
        results.beforeFirst();

        if (results.next())
        {
            Table t = new Table(1); // 1 = with border
            t.setCellPadding(1);

            TR tr = new TR();

            for (int i = 1; i < (numColumns + 1); i++)
            {
                tr.addElement(new TD(new B(resultsMetaData.getColumnName(i))));
            }

            t.addElement(tr);
            results.beforeFirst();

            while (results.next())
            {
                TR row = new TR();

                for (int i = 1; i < (numColumns + 1); i++)
                {
                    row.addElement(new TD(results.getString(i).replaceAll(" ", "&nbsp;")));
                }

                t.addElement(row);
            }

            return (t);
        }
        else
        {
            return (new B("Query Successful; however no data was returned from this query."));
        }
    }
}
