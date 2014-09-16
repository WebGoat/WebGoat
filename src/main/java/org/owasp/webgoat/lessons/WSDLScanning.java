/*
 * Created on May 26, 2005 TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */

package org.owasp.webgoat.lessons;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.html.A;
import org.apache.ecs.html.BR;
import org.apache.ecs.html.IMG;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.Option;
import org.apache.ecs.html.P;
import org.apache.ecs.html.Select;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
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
public class WSDLScanning extends LessonAdapter
{

    static boolean completed = false;

    static boolean beenRestartedYet = false;

    public final static String firstName = "getFirstName";

    public final static String lastName = "getLastName";

    public final static String loginCount = "getLoginCount";

    public final static String ccNumber = "getCreditCard";

    final static IMG CREDITS_LOGO = new IMG("images/logos/parasoft.jpg").setAlt("Parasoft").setBorder(0).setHspace(0)
            .setVspace(0);

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
        WSDLScanning.webgoatContext = webgoatContext;
    }

    @Override
    public WebgoatContext getWebgoatContext()
    {
        return WSDLScanning.webgoatContext;
    }

    protected Category getDefaultCategory()
    {
        return Category.WEB_SERVICES;
    }

    protected List<String> getHints(WebSession s)
    {
        List<String> hints = new ArrayList<String>();
        hints.add("Try connecting to the WSDL with a browser or Web Service tool.");
        hints.add("Sometimes the WSDL will define methods that are not available through a web API. "
                + "Try to find operations that are in the WSDL, but not part of this API");
        hints.add("The URL for the web service is: http://localhost/webgoat/services/WSDLScanning <br>"
                + "The WSDL can usually be viewed by adding a ?WSDL on the end of the request.");
        hints.add("Look in the WSDL for the getCreditCard operation and insert the field in an intercepted request.");
        return hints;
    }

    private final static Integer DEFAULT_RANKING = new Integer(120);

    protected Integer getDefaultRanking()
    {
        return DEFAULT_RANKING;
    }

    public String getTitle()
    {
        return "WSDL Scanning";
    }

    public Object accessWGService(WebSession s, String serv, int port, String proc, String parameterName, Object parameterValue)
    {
        String targetNamespace = "WebGoat";
        try
        {
            QName serviceName = new QName(targetNamespace, serv);
            QName operationName = new QName(targetNamespace, proc);
            Service service = new Service();
            Call call = (Call) service.createCall();
            call.setOperationName(operationName);
            call.addParameter(parameterName, serviceName, ParameterMode.INOUT);
            call.setReturnType(XMLType.XSD_STRING);
            call.setUsername("guest");
            call.setPassword("guest");
            call.setTargetEndpointAddress("http://localhost:" + port + "/" + s.getRequest().getContextPath() + "/services/" + serv);
            Object result = call.invoke(new Object[] { parameterValue });
            return result;
        } catch (RemoteException e)
        {
            e.printStackTrace();
        } catch (ServiceException e)
        {
            e.printStackTrace();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    protected Element createContent(WebSession s)
    {
        ElementContainer ec = new ElementContainer();

        Table t1 = new Table().setCellSpacing(0).setCellPadding(2);

        if (s.isColor())
        {
            t1.setBorder(1);
        }
        TR tr = new TR();
        tr.addElement(new TD("Enter your account number: "));
        tr.addElement(new TD(new Input(Input.TEXT, "id", "101")));
        t1.addElement(tr);

        tr = new TR();
        tr.addElement(new TD("Select the fields to return: "));
        tr.addElement(new TD(new Select("field").setMultiple(true).addElement(
                                                                                new Option(firstName)
                                                                                        .addElement("First Name"))
                .addElement(new Option(lastName).addElement("Last Name"))
                .addElement(new Option(loginCount).addElement("Login Count"))));
        t1.addElement(tr);

        tr = new TR();
        Element b = ECSFactory.makeButton("Submit");
        tr.addElement(new TD(b).setAlign("CENTER").setColSpan(2));
        t1.addElement(tr);

        ec.addElement(t1);

        try
        {
            String[] fields = s.getParser().getParameterValues("field");
            int id = s.getParser().getIntParameter("id");

            Table t = new Table().setCellSpacing(0).setCellPadding(2).setBorder(1);

            if (s.isColor())
            {
                t.setBorder(1);
            }
            TR header = new TR();
            TR results = new TR();
            int port = s.getRequest().getServerPort();
            for (int i = 0; i < fields.length; i++)
            {
                header.addElement(new TD().addElement(fields[i]));
                results.addElement(new TD().addElement((String) accessWGService(s, "WSDLScanning", port, fields[i],
                                                                                "acct_num", new Integer(id))));
            }
            if (fields.length == 0)
            {
                s.setMessage("Please select a value to return.");
            }
            t.addElement(header);
            t.addElement(results);
            ec.addElement(new P().addElement(t));
        } catch (Exception e)
        {

        }
        try
        {
            A a = new A("services/WSDLScanning?WSDL", "WebGoat WSDL File");
            ec.addElement(new P()
                    .addElement("View the web services definition language (WSDL) to see the complete API:"));
            ec.addElement(new BR());
            ec.addElement(a);
            // getLessonTracker( s ).setCompleted( completed );

            if (completed && !getLessonTracker(s).getCompleted() && !beenRestartedYet)
            {
                makeSuccess(s);
                beenRestartedYet = true;
            }
            else if (completed && !getLessonTracker(s).getCompleted() && beenRestartedYet)
            {
                completed = false;
                beenRestartedYet = false;
            }

            // accessWGService("WSDLScanning", "getCreditCard", "acct_num", new Integer(101));
        } catch (Exception e)
        {
            s.setMessage("Error generating " + this.getClass().getName());
            e.printStackTrace();
        }
        return (ec);
    }

    public String getResults(int id, String field)
    {
        try
        {
            Connection connection = DatabaseUtilities.getConnection("guest", getWebgoatContext());
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM user_data WHERE userid = ?");
            ps.setInt(1, id);
            try
            {
                ResultSet results = ps.executeQuery();
                if ((results != null) && (results.next() == true)) { return results.getString(field); }
            } catch (SQLException sqle)
            {
            }
        } catch (Exception e)
        {
        }
        return null;
    }

    public String getCreditCard(int id)
    {
        String result = getResults(id, "cc_number");
        if (result != null)
        {
            completed = true;
            return result;
        }
        return null;
    }

    public String getFirstName(int id)
    {
        String result = getResults(id, "first_name");
        if (result != null) { return result; }
        return null;
    }

    public String getLastName(int id)
    {
        String result = getResults(id, "last_name");
        if (result != null) { return result; }
        return null;
    }

    public String getLoginCount(int id)
    {
        String result = getResults(id, "login_count");
        if (result != null) { return result; }
        return null;
    }

    public Element getCredits()
    {
        return super.getCustomCredits("By Alex Smolen", CREDITS_LOGO);
    }

}