/*
 * Created on Jun 1, 2005 TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */

package org.owasp.webgoat.lessons;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.B;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.P;
import org.apache.ecs.html.PRE;
import org.owasp.webgoat.session.ECSFactory;
import org.owasp.webgoat.session.WebSession;
import org.owasp.webgoat.util.HtmlEncoder;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;


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
 * @author rdawes
 * 
 *         TODO To change the template for this generated type comment go to Window - Preferences -
 *         Java - Code Style - Code Templates
 */
public class WsSAXInjection extends LessonAdapter
{

    private final static String PASSWORD = "password";

    private String password;

    private static String template1 = "<?xml version='1.0' encoding='UTF-8'?>\n" + "<wsns0:Envelope\n"
            + "  xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'\n"
            + "  xmlns:xsd='http://www.w3.org/2001/XMLSchema'\n"
            + "  xmlns:wsns0='http://schemas.xmlsoap.org/soap/envelope/'\n"
            + "  xmlns:wsns1='http://lessons.webgoat.owasp.org'>\n" + "  <wsns0:Body>\n"
            + "    <wsns1:changePassword>\n" + "      <id xsi:type='xsd:int'>101</id>\n"
            + "      <password xsi:type='xsd:string'>";

    private static String template2 = "</password>\n" + "    </wsns1:changePassword>\n" + "  </wsns0:Body>\n"
            + "</wsns0:Envelope>";

    static boolean completed;

    protected Category getDefaultCategory()
    {
        return Category.WEB_SERVICES;
    }

    protected List<String> getHints(WebSession s)
    {
        List<String> hints = new ArrayList<String>();

        hints.add("The backend parses the XML received using a SAX parser.");
        hints.add("SAX parsers often don't care if an element is repeated.");
        hints.add("If there are repeated elements, the last one is the one that is effective");
        hints.add("Try injecting matching 'close' tags, and creating your own XML elements");

        return hints;
    }

    private final static Integer DEFAULT_RANKING = new Integer(150);

    protected Integer getDefaultRanking()
    {
        return DEFAULT_RANKING;
    }

    public String getTitle()
    {
        return "Web Service SAX Injection";
    }

    protected Element makeInputLine(WebSession s)
    {
        ElementContainer ec = new ElementContainer();

        ec.addElement(new P().addElement("Please change your password: "));

        Input input = new Input(Input.TEXT, PASSWORD);
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
            ec.addElement(makeInputLine(s));

            password = s.getParser().getRawParameter(PASSWORD, null);

            PRE pre = new PRE();
            String xml = template1;
            xml = xml + (password == null ? "[password]" : password);
            xml = xml + template2;
            pre.addElement(HtmlEncoder.encode(xml));
            ec.addElement(pre);

            if (password != null)
            {
                ec.addElement(checkXML(s, xml));
            }
        } catch (Exception e)
        {
            s.setMessage("Error generating " + this.getClass().getName());
            e.printStackTrace();
        }
        return (ec);
    }

    private Element checkXML(WebSession s, String xml)
    {
        try
        {
            XMLReader reader = XMLReaderFactory.createXMLReader();
            PasswordChanger changer = new PasswordChanger();
            reader.setContentHandler(changer);
            reader.parse(new InputSource(new StringReader(xml)));
            if (!"101".equals(changer.getId()))
            {
                makeSuccess(s);
                return new B(HtmlEncoder.encode("You have changed the passsword for userid " + changer.getId()
                        + " to '" + changer.getPassword() + "'"));
            }
            else
            {
                return new StringElement("You changed the password for userid 101. Try again.");
            }
        } catch (SAXException saxe)
        {
            return new StringElement("The XML was not well formed: " + saxe.getLocalizedMessage());
        } catch (IOException ioe)
        {
            return new StringElement(ioe.getLocalizedMessage());
        }
    }

    private static class PasswordChanger extends DefaultHandler
    {

        private static String PASSWORD_TAG = "password";

        private static String ID_TAG = "id";

        private String id = null;

        private String password = null;

        private StringBuffer text = new StringBuffer();

        public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException
        {
            text.delete(0, text.length());
        }

        public void characters(char[] ch, int start, int length) throws SAXException
        {
            text.append(ch, start, length);
        }

        public void endElement(String uri, String localName, String qName) throws SAXException
        {
            if (localName.equals(ID_TAG)) id = text.toString();
            if (localName.equals(PASSWORD_TAG)) password = text.toString();
            text.delete(0, text.length());
        }

        public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException
        {
            text.append(ch, start, length);
        }

        public String getId()
        {
            return id;
        }

        public String getPassword()
        {
            return password;
        }

    }
}
