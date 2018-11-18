
package org.owasp.webgoat.plugin;

import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.session.UserSessionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;



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
@AssignmentHints(value = {"xss-reflected-5a-hint-1", "xss-reflected-5a-hint-2", "xss-reflected-5a-hint-3", "xss-reflected-5a-hint-4"})
public class CrossSiteScriptingLesson5a extends AssignmentEndpoint {

	@Autowired
	UserSessionData userSessionData;

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody AttackResult completed(@RequestParam Integer QTY1,
												@RequestParam Integer QTY2, @RequestParam Integer QTY3,
												@RequestParam Integer QTY4, @RequestParam String field1,
												@RequestParam String field2, HttpServletRequest request)
			throws IOException {

		if (field2.toLowerCase().matches("<script>.*(console\\.log\\(.*\\)|alert\\(.*\\))<\\/script>")) {
			return trackProgress(failed().feedback("xss-reflected-5a-failed-wrong-field").build());
		}

		double totalSale = QTY1.intValue() * 69.99 + QTY2.intValue() * 27.99 + QTY3.intValue() * 1599.99 + QTY4.intValue() * 299.99;

		userSessionData.setValue("xss-reflected1-complete",(Object)"false");
		StringBuffer cart = new StringBuffer();
		cart.append("Thank you for shopping at WebGoat. <br />You're support is appreciated<hr />");
		cart.append("<p>We have charged credit card:" + field1 + "<br />");
		cart.append(   "                             ------------------- <br />");
		cart.append(   "                               $" + totalSale);

		//init state
		if (userSessionData.getValue("xss-reflected1-complete") == null) {
			userSessionData.setValue("xss-reflected1-complete",(Object)"false");
		}

		if (field1.toLowerCase().matches("<script>.*(console\\.log\\(.*\\)|alert\\(.*\\))<\\/script>")) {
			//return trackProgress()
			userSessionData.setValue("xss-reflected-5a-complete","true");
			if(field1.toLowerCase().contains("console.log")) {
				return trackProgress(success().feedback("xss-reflected-5a-success-console").output(cart.toString()).build());
			} else {
				return trackProgress(success().feedback("xss-reflected-5a-success-alert").output(cart.toString()).build());
			}
		} else {
			userSessionData.setValue("xss-reflected1-complete","false");
			return trackProgress(success()
					.feedback("xss-reflected-5a-failure")
					.output(cart.toString())
					.build());
		}
	}
}