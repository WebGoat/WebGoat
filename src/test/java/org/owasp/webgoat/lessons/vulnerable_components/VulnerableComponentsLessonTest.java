/*
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details, please see http://www.owasp.org/
 *
 * Copyright (c) 2002 - 2019 Bruce Mayhew
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
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software projects.
 */

package org.owasp.webgoat.lessons.vulnerable_components;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.StreamException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VulnerableComponentsLessonTest {

	String strangeContact = "<contact class='dynamic-proxy'>\n" + 
			"<interface>org.owasp.webgoat.vulnerable_components.Contact</interface>\n" + 
			"  <handler class='java.beans.EventHandler'>\n" + 
			"    <target class='java.lang.ProcessBuilder'>\n" + 
			"      <command>\n" + 
			"        <string>calc.exe</string>\n" + 
			"      </command>\n" + 
			"    </target>\n" + 
			"    <action>start</action>\n" + 
			"  </handler>\n" + 
			"</contact>";
	String contact = "<contact>\n"+
			"</contact>";
    
    @Test
    public void testTransformation() throws Exception {
    	XStream xstream = new XStream();
        xstream.setClassLoader(Contact.class.getClassLoader());
        xstream.alias("contact", ContactImpl.class);
        xstream.ignoreUnknownElements();
        assertNotNull(xstream.fromXML(contact));
    }
    
    @Test
    @Disabled
    public void testIllegalTransformation() throws Exception {
    	XStream xstream = new XStream();
        xstream.setClassLoader(Contact.class.getClassLoader());
        xstream.alias("contact", ContactImpl.class);
        xstream.ignoreUnknownElements();
        Exception e = assertThrows(RuntimeException.class, ()->((Contact)xstream.fromXML(strangeContact)).getFirstName());
        assertTrue(e.getCause().getMessage().contains("calc.exe"));
    }
    
    @Test
    public void testIllegalPayload() throws Exception {
    	XStream xstream = new XStream();
        xstream.setClassLoader(Contact.class.getClassLoader());
        xstream.alias("contact", ContactImpl.class);
        xstream.ignoreUnknownElements();
        Exception e = assertThrows(StreamException.class, ()->((Contact)xstream.fromXML("bullssjfs")).getFirstName());
        assertTrue(e.getCause().getMessage().contains("START_DOCUMENT"));
    }
}
