/*
 * Created on Jun 1, 2005 TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */

package org.owasp.webgoat.lessons;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.html.A;
import org.apache.ecs.html.BR;
import org.apache.ecs.html.IMG;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.P;
import org.apache.ecs.html.PRE;
import org.owasp.webgoat.session.DatabaseUtilities;
import org.owasp.webgoat.session.ECSFactory;
import org.owasp.webgoat.session.WebSession;
import org.owasp.webgoat.session.WebgoatContext;


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
 * @author asmolen
 * 
 *         TODO To change the template for this generated type comment go to Window - Preferences -
 *         Java - Code Style - Code Templates
 */
public class WsSqlInjection extends LessonAdapter
{

    public final static String ccNumber = "cc_number";

    private final static String ACCT_NUM = "account_number";

    private String accountNumber;

    final static IMG CREDITS_LOGO = new IMG("images/logos/parasoft.jpg").setAlt("Parasoft").setBorder(0).setHspace(0)
            .setVspace(0);

    /*
     * (non-Javadoc)
     * @see lessons.AbstractLesson#getMenuItem()
     */
    static boolean completed;

    private static WebgoatContext webgoatContext;

    /**
     * We maintain a static reference to WebgoatContext, since this class is also automatically
     * instantiated by the Axis web services module, which does not call setWebgoatContext()
     * (non-Javadoc)
     * 
     * @see org.owasp.webgoat.lessons.AbstractLesson#setWebgoatContext(org.owasp.webgoat.session.WebgoatContext)
     */
    @Override
    public void setWebgoatContext(WebgoatContext webgoatContext)
    {
        WsSqlInjection.webgoatContext = webgoatContext;
    }

    @Override
    public WebgoatContext getWebgoatContext()
    {
        return WsSqlInjection.webgoatContext;
    }

    protected Category getDefaultCategory()
    {
        return Category.WEB_SERVICES;
    }

    protected List<String> getHints(WebSession s)
    {
        List<String> hints = new ArrayList<String>();
        hints.add("Try connecting to the WSDL with a browser or Web Service tool.");
        hints.add("Sometimes the server side code will perform input validation before issuing  "
                + "the request to the web service operation.  Try to bypass this check by "
                + "accessing the web service directly");
        hints.add("The URL for the web service is: http://localhost/WebGoat/services/WsSqlInjection?WSDL <br>"
                + "The WSDL can usually be viewed by adding a ?WSDL on the end of the request.");
        hints.add("Create a new soap request for the getCreditCard(String id) operation.");
        hints
                .add("A soap request uses the following HTTP header: <br> "
                        + "SOAPAction: some action header, can be &quot;&quot;<br><br>"
                        + "The soap message body has the following format:<br>"
                        + "&lt;?xml version='1.0' encoding='UTF-8'?&gt; <br>"
                        + "&nbsp;&nbsp;&lt;SOAP-ENV:Envelope xmlns:SOAP-ENV='http://schemas.xmlsoap.org/soap/envelope/' xmlns:xsd='http://www.w3.org/2001/XMLSchema' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'&gt; <br>"
                        + "&nbsp;&nbsp;&nbsp;&nbsp;&lt;SOAP-ENV:Body&gt; <br>"
                        + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;ns1:getCreditCard SOAP-ENV:encodingStyle='http://schemas.xmlsoap.org/soap/encoding/' xmlns:ns1='http://lessons'&gt; <br>"
                        + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;id xsi:type='xsd:string'&gt;101&lt;/id&gt; <br>"
                        + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;/ns1:getCreditCard&gt; <br>"
                        + "&nbsp;&nbsp;&nbsp;&nbsp;&lt;/SOAP-ENV:Body&gt; <br>"
                        + "&nbsp;&nbsp;&lt;/SOAP-ENV:Envelope&gt; <br>" + "");
        hints.add("Use the \"Webservices\" Functions in OWASP ZAP.");
        /*
         * "&lt;?xml version=\"1.0\" encoding=\"UTF-8\"?&gt; <br>" + " &lt;SOAP-ENV:Envelope
         * xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" <br>" + "
         * xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" <br>" + "
         * xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"&gt; <br>" + "
         * &lt;SOAP-ENV:Body&gt; <br>" + " &lt;ns1:getCreditCard
         * SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"
         * xmlns:ns1=\"http://lessons\"&gt; <br>" + " &lt;id
         * xsi:type=\"xsd:string\"&gt;101&lt;/id&gt; <br>"+ " &lt;/ns1:getCreditCard&gt; <br>" + "
         * &lt;/SOAP-ENV:Body&gt; <br>" + " &lt;/SOAP-ENV:Envelope&gt; <br><br>" + "Intercept the
         * HTTP request and try to create a soap request.");
         */
        return hints;
    }

    private final static Integer DEFAULT_RANKING = new Integer(150);

    protected Integer getDefaultRanking()
    {
        return DEFAULT_RANKING;
    }

    public String getTitle()
    {
        return "Web Service SQL Injection";
    }

    protected Element makeAccountLine(WebSession s)
    {
        ElementContainer ec = new ElementContainer();

        ec.addElement(new P().addElement("Enter your Account Number: "));

        accountNumber = s.getParser().getRawParameter(ACCT_NUM, "101");
        Input input = new Input(Input.TEXT, ACCT_NUM, accountNumber.toString());
        ec.addElement(input);

        Element b = ECSFactory.makeButton("Go!");
        ec.addElement(b);

        return ec;
    }

    protected Element createContent(WebSession s)
    {
        ElementContainer ec = new ElementContainer();
        try
        {
            ec.addElement(makeAccountLine(s));

            String query = "SELECT * FROM user_data WHERE userid = " + accountNumber;
            ec.addElement(new PRE(query));
            for (int i = 0; i < accountNumber.length(); i++)
            {
                char c = accountNumber.charAt(i);
                if (c < '0' || c > '9')
                {
                    ec.addElement("Invalid account number. ");
                    accountNumber = "0";
                }
            }
            try
            {
                ResultSet results = getResults(accountNumber);
                if ((results != null) && (results.first() == true))
                {
                    ResultSetMetaData resultsMetaData = results.getMetaData();
                    ec.addElement(DatabaseUtilities.writeTable(results, resultsMetaData));
                    results.last();
                    if (results.getRow() >= 6)
                    {
                        // this should never happen
                    }
                }
                else
                {
                    ec.addElement("No results matched.  Try Again.");
                }
            } catch (SQLException sqle)
            {
                ec.addElement(new P().addElement(sqle.getMessage()));
            }
            A a = new A("services/WsSqlInjection?WSDL", "WebGoat WSDL File");
            ec.addElement(new P().addElement("Exploit the following WSDL to access sensitive data:"));
            ec.addElement(new BR());
            ec.addElement(a);
            getLessonTracker(s).setCompleted(completed);
        } catch (Exception e)
        {
            s.setMessage("Error generating " + this.getClass().getName());
            e.printStackTrace();
        }
        return (ec);
    }

    public ResultSet getResults(String id)
    {
        try
        {
            Connection connection = DatabaseUtilities.getConnection("guest", getWebgoatContext());
            String query = "SELECT * FROM user_data WHERE userid = " + id;
            try
            {
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                    ResultSet.CONCUR_READ_ONLY);
                ResultSet results = statement.executeQuery(query);
                return results;
            } catch (SQLException sqle)
            {
            }
        } catch (Exception e)
        {
        }
        return null;
    }

    public String[] getCreditCard(String id)
    {
        ResultSet results = getResults(id);
        if ((results != null))
        {
            try
            {
                results.last();
                String[] users = new String[results.getRow()];
                if (users.length > 4)
                {
                    completed = true;
                }
                results.beforeFirst();
                while (results.next() == true)
                {
                    int i = results.getRow();
                    users[i - 1] = results.getString(ccNumber);
                }
                return users;
            } catch (SQLException sqle)
            {
            }
        }
        return null;
    }

    public Element getCredits()
    {
        return super.getCustomCredits("By Alex Smolen", CREDITS_LOGO);
    }
}
