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

package org.owasp.webgoat.lessons.chromedevtools;

import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Assignment where the user has to look through an HTTP Request using the Developer Tools and find
 * a specific number.
 *
 * @author TMelzer
 * @since 30.11.18
 */
@RestController
@AssignmentHints({"networkHint1", "networkHint2"})
public class NetworkLesson extends AssignmentEndpoint {

  @PostMapping(
      value = "/ChromeDevTools/network",
      params = {"network_num", "number"})
  @ResponseBody
  public AttackResult completed(@RequestParam String network_num, @RequestParam String number) {
    if (network_num.equals(number)) {
      return success(this).feedback("network.success").output("").build();
    } else {
      return failed(this).feedback("network.failed").build();
    }
  }

  @PostMapping(path = "/ChromeDevTools/network", params = "networkNum")
  @ResponseBody
  public ResponseEntity<?> ok(@RequestParam String networkNum) {
    return ResponseEntity.ok().build();
  }
}
