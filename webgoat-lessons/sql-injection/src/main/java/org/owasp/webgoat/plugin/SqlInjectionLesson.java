
package org.owasp.webgoat.plugin;

import org.owasp.webgoat.lessons.AssignmentEndpoint;
import org.owasp.webgoat.lessons.LessonEndpointMapping;
import org.owasp.webgoat.lessons.model.AttackResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;



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
@LessonEndpointMapping
public class SqlInjectionLesson extends AssignmentEndpoint {

	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody AttackResult completed(@RequestParam String person, HttpServletRequest request) throws IOException {
	    if (!person.toString().equals("")) {
	        return trackProgress(AttackResult.success("The server has reversed your name: " + new StringBuffer(person).reverse().toString()));
	    } else {
	        return trackProgress(AttackResult.failed("You are close, try again"));
	    }
	}

    @Override
    public String getPath() {
        return "/SqlInjection/attack1";
    }


//    private final static String ACCT_NAME = "account_name";
//
//    private static String STAGE = "stage";
//
//    private String accountName;
//
//    /**
//     * Description of the Method
//     * 
//     * @param s
//     *            Description of the Parameter
//     * @return Description of the Return Value
//     */
//    protected Element createContent(WebSession s)
//    {
//        return super.createStagedContent(s);
//    }
//
//    protected Element doStage1(WebSession s) throws Exception
//    {
//        return injectableQuery(s);
//    }
//
//    protected Element doStage2(WebSession s) throws Exception
//    {
//        return parameterizedQuery(s);
//    }
//
//    protected Element injectableQuery(WebSession s)
//    {
//        ElementContainer ec = new ElementContainer();
//
//        try
//        {
//            Connection connection = DatabaseUtilities.getConnection(s);
//
//            ec.addElement(makeAccountLine(s));
//
//            String query = "SELECT * FROM user_data WHERE last_name = '" + accountName + "'";
//            ec.addElement(new PRE(query));
//
//            try
//            {
//                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
//                                                                    ResultSet.CONCUR_READ_ONLY);
//                ResultSet results = statement.executeQuery(query);
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
//                        getLessonTracker(s).setStage(2);
//
//                        StringBuffer msg = new StringBuffer();
//
//                        msg.append(getLabelManager().get("StringSqlInjectionSecondStage"));
//
//                        s.setMessage(msg.toString());
//                    }
//                }
//                else
//                {
//                    ec.addElement(getLabelManager().get("NoResultsMatched"));
//                }
//            } catch (SQLException sqle)
//            {
//                ec.addElement(new P().addElement(sqle.getMessage()));
//                sqle.printStackTrace();
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
