
package org.owasp.webgoat.plugin;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;

import org.owasp.webgoat.endpoints.AssignmentEndpoint;
import org.owasp.webgoat.endpoints.AssignmentPath;
import org.owasp.webgoat.lessons.AttackResult;
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
@AssignmentPath("/CrossSiteScripting/attack5a")
public class CrossSiteScriptingLesson5a extends AssignmentEndpoint {

	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody AttackResult completed(@RequestParam Integer QTY1,
												@RequestParam Integer QTY2, @RequestParam Integer QTY3,
												@RequestParam Integer QTY4, @RequestParam String field1,
												@RequestParam Integer field2, HttpServletRequest request)
			throws IOException {
		System.out.println("foo");
		// Should add some QTY validation here.  Someone could have fun and enter a negative quantity and get merchanidise and a refund :)
		double totalSale = QTY1.intValue() * 69.99 + QTY2.intValue() * 27.99 + QTY3.intValue() * 1599.99 + QTY4.intValue() * 299.99;  
		
       	StringBuffer cart = new StringBuffer();
       	cart.append("Thank you for shopping at WebGoat. <br />You're support is appreciated<hr />");
       	cart.append("<p>We have chaged credit card:" + field1 + "<br />");
       	cart.append(   "                             ------------------- <br />");
       	cart.append(   "                               $" + totalSale);
        return trackProgress(AttackResult.failed(cart.toString()));
	}
}