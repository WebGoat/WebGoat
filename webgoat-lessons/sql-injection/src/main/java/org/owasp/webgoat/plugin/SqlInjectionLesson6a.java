
package org.owasp.webgoat.plugin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;

import org.owasp.webgoat.endpoints.AssignmentEndpoint;
import org.owasp.webgoat.lessons.AttackResult;
import org.owasp.webgoat.session.DatabaseUtilities;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;



/***************************************************************************************************
 * 
 * 
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 * 
 * Copyright (c) 2002 - 20014 Bruce Mayhew
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
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software
 * projects.
 * 
 * For details, please see http://webgoat.github.io
 * 
 * @author Bruce Mayhew <a href="http://code.google.com/p/webgoat">WebGoat</a>
 * @created October 28, 2003
 */
public class SqlInjectionLesson6a extends AssignmentEndpoint {

	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody AttackResult completed(@RequestParam String userid_6a, HttpServletRequest request) throws IOException {
		return injectableQuery(userid_6a);
		// The answer: Smith' union select userid,user_name, password,cookie,cookie, cookie,userid from user_system_data --

	}

    @Override
    public String getPath() {
        return "/SqlInjection/attack6a";
    }


    protected AttackResult injectableQuery(String accountName)
    {
        try
        {
            Connection connection = DatabaseUtilities.getConnection(getWebSession());
            String query = "SELECT * FROM user_data WHERE last_name = '" + accountName + "'";

            try
            {
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                    ResultSet.CONCUR_READ_ONLY);
                ResultSet results = statement.executeQuery(query);

                if ((results != null) && (results.first() == true))
                {
                    ResultSetMetaData resultsMetaData = results.getMetaData();
                	StringBuffer output = new StringBuffer();

                    output.append(writeTable(results, resultsMetaData));
                    results.last();

                    // If they get back more than one user they succeeded
                    if (results.getRow() >= 6)
                    {
                    	return trackProgress(AttackResult.success("You have succeed: " + output.toString()));
                   } else {
                	   return trackProgress(AttackResult.failed("You are close, try again. " + output.toString()));
                   }
                    
                }
                else
                {
                	return trackProgress(AttackResult.failed("No Results Matched. Try Again. "));

                }
            } catch (SQLException sqle)
            {
            	
            	return trackProgress(AttackResult.failed(sqle.getMessage()));
            }
        } catch (Exception e)
        {
        	e.printStackTrace();
        	return trackProgress(AttackResult.failed( "ErrorGenerating" + this.getClass().getName() + " : " + e.getMessage()));
        }
    }
    
    public String writeTable(ResultSet results, ResultSetMetaData resultsMetaData) throws IOException,
	SQLException 
    {
		int numColumns = resultsMetaData.getColumnCount();
		results.beforeFirst();
		StringBuffer t = new StringBuffer();
		t.append("<p>");
	
		if (results.next())
		{
			for (int i = 1; i < (numColumns + 1); i++)
			{
				t.append(resultsMetaData.getColumnName(i));
				t.append(", ");
			}
		
			t.append("<br />");
			results.beforeFirst();
		
			while (results.next())
			{
		
				for (int i = 1; i < (numColumns + 1); i++)
				{
					t.append(results.getString(i));
					t.append(", ");
				}
		
				t.append("<br />");
			}
		
		}
		else
		{
			t.append ("Query Successful; however no data was returned from this query.");
		}
		
		t.append("</p>");
		return (t.toString());
    }
//
//    protected Element parameterizedQuery(WebSession s)
//    {
//        ElementContainer ec = new ElementContainer();
//
//        ec.addElement(getLabelManager().get("StringSqlInjectionSecondStage"));
//        if (s.getParser().getRawParameter(ACCT_NAME, "YOUR_NAME").equals("restart"))
//        {
//            getLessonTracker(s).getLessonProperties().setProperty(STAGE, "1");
//            return (injectableQuery(s));
//        }
//
//        ec.addElement(new BR());
//
//        try
//        {
//            Connection connection = DatabaseUtilities.getConnection(s);
//
//            ec.addElement(makeAccountLine(s));
//
//            String query = "SELECT * FROM user_data WHERE last_name = ?";
//            ec.addElement(new PRE(query));
//
//            try
//            {
//                PreparedStatement statement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
//                                                                            ResultSet.CONCUR_READ_ONLY);
//                statement.setString(1, accountName);
//                ResultSet results = statement.executeQuery();
//
//                if ((results != null) && (results.first() == true))
//                {
//                    ResultSetMetaData resultsMetaData = results.getMetaData();
//                    ec.addElement(DatabaseUtilities.writeTable(results, resultsMetaData));
//                    results.last();
//
//                    // If they get back more than one user they succeeded
//                    if (results.getRow() >= 6)
//                    {
//                        makeSuccess(s);
//                    }
//                }
//                else
//                {
//                    ec.addElement(getLabelManager().get("NoResultsMatched"));
//                }
//            } catch (SQLException sqle)
//            {
//                ec.addElement(new P().addElement(sqle.getMessage()));
//            }
//        } catch (Exception e)
//        {
//            s.setMessage(getLabelManager().get("ErrorGenerating") + this.getClass().getName());
//            e.printStackTrace();
//        }
//
//        return (ec);
//    }
//
//    protected Element makeAccountLine(WebSession s)
//    {
//        ElementContainer ec = new ElementContainer();
//        ec.addElement(new P().addElement(getLabelManager().get("EnterLastName")));
//
//        accountName = s.getParser().getRawParameter(ACCT_NAME, "Your Name");
//        Input input = new Input(Input.TEXT, ACCT_NAME, accountName.toString());
//        ec.addElement(input);
//
//        Element b = ECSFactory.makeButton(getLabelManager().get("Go!"));
//        ec.addElement(b);
//
//        return ec;
//
//    }

 

}
