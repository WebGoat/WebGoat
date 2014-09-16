/*
 * Created on May 26, 2005 TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */

package org.owasp.webgoat.lessons;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.html.A;
import org.apache.ecs.html.BR;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.P;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.owasp.webgoat.session.DatabaseUtilities;
import org.owasp.webgoat.session.ECSFactory;
import org.owasp.webgoat.session.ParameterNotFoundException;
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
public class SoapRequest extends SequentialLessonAdapter
{

    public final static String firstName = "getFirstName";

    public final static String lastName = "getLastName";

    public final static String loginCount = "getLoginCount";

    public final static String ccNumber = "getCreditCard";

    // int instead of boolean to keep track of method invocation count
    static int accessFirstName;

    static int accessLastName;

    static int accessCreditCard;

    static int accessLoginCount;

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
        SoapRequest.webgoatContext = webgoatContext;
    }

    @Override
    public WebgoatContext getWebgoatContext()
    {
        return SoapRequest.webgoatContext;
    }

    protected Category getDefaultCategory()
    {
        return Category.WEB_SERVICES;
    }

    protected List<String> getHints(WebSession s)
    {
        List<String> hints = new ArrayList<String>();
        hints
                .add("Accessible operations are delimited by the &lt;operation&gt; tag contained within the &lt;portType&gt; section of the WSDL. <BR> Below is an example of a typical operation (getFirstName): <br><br>"
                        + "&lt;wsdl:portType name=\"SoapRequest\"&gt; <br>"
                        + "&lt;wsdl:<strong>operation name=\"getFirstName\"</strong>&gt;<br>"
                        + "&lt;wsdl:input message=\"impl:getFirstNameRequest\" name=\"getFirstNameRequest\" /&gt;<br>"
                        + "&lt;wsdl:output message=\"impl:getFirstNameResponse\" name=\"getFirstNameResponse\" /&gt;<br>"
                        + "&lt;wsdlsoap:operation soapAction=\"\" /&gt;"
                        + "&lt;/wsdl:portType&gt;<br><br>"
                        + "The methods invoked are defined by the input and output message attributes. "
                        + "Example: <strong>\"getFirstNameRequest\"</strong>");
        hints
                .add("There are several tags within a SOAP envelope. "
                        + "Each namespace is defined in the &lt;definitions&gt; section of the WSDL, and is declared using the (xmlns:namespace_name_here=\"namespace_reference_location_here\") format.<br><br>"
                        + "The following example defines a tag \"&lt;xsd:\", whose attribute structure will reference the namespace location assigned to it in the declaration:<br>"
                        + "<strong>xmlns:xsd=\"http://www.w3.org/2001/XMLSchema</strong>");
        hints
                .add("Determine what parameters and types are required by the message definition corresponding to the operation's request method. "
                        + "This example defines a parameter (id) of type (int) in the namespace (xsd) for the method (getFirstNameRequest):<br>"
                        + "&lt;wsdl:message name=\"getFirstNameRequest\"<br><br>"
                        + "&lt;wsdl:<strong>part name=\"id\" type=\"xsd:int\"</strong> /&gt;<br>"
                        + "&lt;/wsdl:message&gt;<br><br>"
                        + "Examples of other types:<br>"
                        + "{boolean, byte, base64Binary, double, float, int, long, short, unsignedInt, unsignedLong, unsignedShort, string}.<br>");
        String soapEnv = "A SOAP request uses the following HTTP header: <br><br> "
                + "SOAPAction: some action header, can be &quot;&quot; <br><br>"
                + "The SOAP message body has the following format:<br>"
                + "&lt;?xml version=\"1.0\" encoding=\"UTF-8\"?&gt; <br>"
                + "&lt;SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" <br>"
                + "                   xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" <br>"
                + "                   xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"&gt; <br>"
                + "&nbsp;&nbsp;&lt;SOAP-ENV:Body&gt; <br>"
                + "&nbsp;&nbsp;&nbsp;&nbsp;&lt;ns1:getFirstName SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:ns1=\"http://lessons\"&gt; <br>"
                + "&nbsp;&nbsp;&nbsp;&nbsp;&lt;id xsi:type=\"xsd:int\"&gt;101&lt;/id&gt; <br>"
                + "&nbsp;&nbsp;&nbsp;&nbsp;&lt;/ns1:getFirstName&gt; <br>" + "&nbsp;&nbsp;&lt;/SOAP-ENV:Body&gt; <br>"
                + "&lt;/SOAP-ENV:Envelope&gt; <br><br>"
                + "Intercept the HTTP request and try to create a SOAP request.";
        soapEnv.replaceAll("(?s) ", "&nbsp;");
        hints.add(soapEnv);

        return hints;
    }

    private final static Integer DEFAULT_RANKING = new Integer(100);

    protected Integer getDefaultRanking()
    {
        return DEFAULT_RANKING;
    }

    public String getTitle()
    {
        return "Create a SOAP Request";
    }

    protected Element makeOperationsLine(WebSession s)
    {
        ElementContainer ec = new ElementContainer();

        Table t1 = new Table().setCellSpacing(0).setCellPadding(2);

        if (s.isColor())
        {
            t1.setBorder(1);
        }

        TR tr = new TR();
        tr.addElement(new TD().addElement("How many operations are defined in the WSDL: "));
        tr.addElement(new TD(new Input(Input.TEXT, "count", "")));
        Element b = ECSFactory.makeButton("Submit");
        tr.addElement(new TD(b).setAlign("LEFT"));
        t1.addElement(tr);

        ec.addElement(t1);

        return ec;
    }

    protected Element makeTypeLine(WebSession s)
    {
        ElementContainer ec = new ElementContainer();

        Table t1 = new Table().setCellSpacing(0).setCellPadding(2);

        if (s.isColor())
        {
            t1.setBorder(1);
        }

        TR tr = new TR();
        tr.addElement(new TD()
                .addElement("Now, what is the type of the (id) parameter in the \"getFirstNameRequest\" method: "));
        tr.addElement(new TD(new Input(Input.TEXT, "type", "")));
        Element b = ECSFactory.makeButton("Submit");
        tr.addElement(new TD(b).setAlign("LEFT"));
        t1.addElement(tr);

        ec.addElement(t1);

        return ec;
    }

    protected Element createContent(WebSession s)
    {
        return super.createStagedContent(s);
    }

    protected Element doStage1(WebSession s) throws Exception
    {
        return viewWsdl(s);
    }

    protected Element doStage2(WebSession s) throws Exception
    {
        return determineType(s);
    }

    protected Element doStage3(WebSession s) throws Exception
    {
        return createSoapEnvelope(s);
    }

    protected Element viewWsdl(WebSession s)
    {
        ElementContainer ec = new ElementContainer();

        // DEVNOTE: Test for stage completion.
        try
        {
            int operationCount = 0;
            operationCount = s.getParser().getIntParameter("count");

            if (operationCount == 4)
            {
                getLessonTracker(s).setStage(2);
                s.setMessage("Stage 1 completed.");

                // Redirect user to Stage2 content.
                ec.addElement(doStage2(s));
            }
            else
            {
                s.setMessage("Sorry, that is an incorrect count. Try Again.");
            }
        } catch (NumberFormatException nfe)
        {
            // DEVNOTE: Eat the exception.
            // ec.addElement( new P().addElement( nfe.getMessage() ) );
            s.setMessage("Sorry, that answer is invalid. Try again.");
        } catch (ParameterNotFoundException pnfe)
        {
            // DEVNOTE: Eat the exception.
            // ec.addElement( new P().addElement( pnfe.getMessage() ) );
        } catch (Exception e)
        {
            s.setMessage("Error generating " + this.getClass().getName());
            e.printStackTrace();
        }

        // DEVNOTE: Conditionally display Stage1 content depending on whether stage is completed or
        // not
        if (getLessonTracker(s).getStage() == 1)
        // if ( null == (getLessonTracker(s).getLessonProperties().getProperty(WebSession.STAGE)) ||
        // (getLessonTracker(s).getLessonProperties().getProperty(WebSession.STAGE)).equals("1") )
        {
            ec.addElement(makeOperationsLine(s));

            A a = new A("services/SoapRequest?WSDL", "WebGoat WSDL File");
            ec.addElement(new P().addElement("View the following WSDL and count available operations:"));
            ec.addElement(new BR());
            ec.addElement(a);
        }

        // getLessonTracker( s ).setCompleted( SoapRequest.completed );

        return (ec);
    }

    protected Element determineType(WebSession s)
    {
        ElementContainer ec = new ElementContainer();

        // DEVNOTE: Test for stage completion.
        try
        {
            String paramType = "";
            paramType = s.getParser().getStringParameter("type");

            // if (paramType.equalsIgnoreCase("int"))
            if (paramType.equals("int"))
            {
                getLessonTracker(s).setStage(3);
                s.setMessage("Stage 2 completed. ");
                // s.setMessage(
                // "Now, you'll craft a SOAP envelope for invoking a web service directly.");

                // Redirect user to Stage2 content.
                ec.addElement(doStage3(s));
            }
            else
            {
                s.setMessage("Sorry, that is an incorrect type. Try Again.");
            }
        } catch (ParameterNotFoundException pnfe)
        {
            // DEVNOTE: Eat the exception.
            // ec.addElement( new P().addElement( pnfe.getMessage() ) );
        } catch (Exception e)
        {
            s.setMessage("Error generating " + this.getClass().getName());
            e.printStackTrace();
        }

        // DEVNOTE: Conditionally display Stage2 content depending on whether stage is completed or
        // not
        if (getLessonTracker(s).getStage() == 2)
        // if ( null == (getLessonTracker(s).getLessonProperties().getProperty(WebSession.STAGE)) ||
        // (getLessonTracker(s).getLessonProperties().getProperty(WebSession.STAGE)).equals("2") )
        {
            ec.addElement(makeTypeLine(s));

            A a = new A("services/SoapRequest?WSDL", "WebGoat WSDL File");
            ec.addElement(new P().addElement("View the following WSDL and count available operations:"));
            ec.addElement(new BR());
            ec.addElement(a);
        }

        // getLessonTracker( s ).setCompleted( SoapRequest.completed );

        return (ec);
    }

    protected Element createSoapEnvelope(WebSession s)
    {
        ElementContainer ec = new ElementContainer();

        // Determine how many methods have been accessed. User needs to check at least two methods
        // before completing the lesson.
        if ((accessFirstName + accessLastName + accessCreditCard + accessLoginCount) >= 2)
        {
            /** Reset function access counters **/
            accessFirstName = accessLastName = accessCreditCard = accessLoginCount = 0;
            // SoapRequest.completed = true;
            makeSuccess(s);
        }
        else
        {

            // display Stage2 content
            ec
            .addElement(new P()
                    .addElement("Intercept the request and invoke any method by sending a valid SOAP request for a valid account. <br/>"));
            ec
            .addElement(new P()
                    .addElement("You must access at least 2 of the methods to pass the lesson. <br/>"));
            Element b = ECSFactory.makeButton("Press to generate an HTTP request");
            ec.addElement(b);

            // conditionally display invoked methods
            if ((accessFirstName + accessLastName + accessCreditCard + accessLoginCount) > 0)
            {
                ec.addElement("<br><br>Methods Invoked:<br>");
                ec.addElement("<ul>");
                if (accessFirstName > 0)
                {
                    ec.addElement("<li>getFirstName</li>");
                }
                if (accessLastName > 0)
                {
                    ec.addElement("<li>getLastName</li>");
                }
                if (accessCreditCard > 0)
                {
                    ec.addElement("<li>getCreditCard</li>");
                }
                if (accessLoginCount > 0)
                {
                    ec.addElement("<li>getLoginCount</li>");
                }
                ec.addElement("</ul>");
            }

            A a = new A("services/SoapRequest?WSDL", "WebGoat WSDL File");
            ec.addElement(new BR());
            ec.addElement(a);
        }

        // getLessonTracker( s ).setCompleted( SoapRequest.completed );
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
        // SoapRequest.completed = true;

        if (result != null)
        {
            // DEVNOTE: Always set method access counter to (1) no matter how many times it is
            // accessed.
            // This is intended to be used to determine how many methods have been accessed, not how
            // often.
            accessCreditCard = 1;
            return result;
        }
        return null;
    }

    public String getFirstName(int id)
    {
        String result = getResults(id, "first_name");
        if (result != null)
        {
            // DEVNOTE: Always set method access counter to (1) no matter how many times it is
            // accessed.
            // This is intended to be used to determine how many methods have been accessed, not how
            // often.
            accessFirstName = 1;
            return result;
        }
        return null;
    }

    public String getLastName(int id)
    {
        String result = getResults(id, "last_name");
        if (result != null)
        {
            // DEVNOTE: Always set method access counter to (1) no matter how many times it is
            // accessed.
            // This is intended to be used to determine how many methods have been accessed, not how
            // often.
            accessLastName = 1;
            return result;
        }
        return null;
    }

    public String getLoginCount(int id)
    {
        String result = getResults(id, "login_count");
        if (result != null)
        {
            // DEVNOTE: Always set method access counter to (1) no matter how many times it is
            // accessed.
            // This is intended to be used to determine how many methods have been accessed, not how
            // often.
            accessLoginCount = 1;
            return result;
        }
        return null;
    }
}
