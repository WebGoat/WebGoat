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

package org.owasp.webgoat.ssrf;

import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.net.httpserver.Authenticator.Success;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;


@RestController
@AssignmentHints({"ssrf.hint3"})
public class SSRFTask2 extends AssignmentEndpoint {

    @PostMapping("/SSRF/task2")
    @ResponseBody
    public AttackResult completed(@RequestParam String url) {
        return furBall(url);
    }

    protected AttackResult furBall(String url) {
        try {
            StringBuffer html = new StringBuffer();

            if (url.matches("http://ifconfig.pro")) {
                URL u = new URL(url);
                URLConnection urlConnection = u.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    html.append(inputLine);
                }
                in.close();

                return trackProgress(success()
                        .feedback("ssrf.success")
                        .output(html.toString())
                        .build());
            } else {
                html.append("<img class=\"image\" alt=\"image post\" src=\"images/cat.jpg\">");
                return trackProgress(failed()
                        .feedback("ssrf.failure")
                        .output(html.toString())
                        .build());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return trackProgress(failed()
                    .output(e.getMessage())
                    .build());
        }
    }
}
