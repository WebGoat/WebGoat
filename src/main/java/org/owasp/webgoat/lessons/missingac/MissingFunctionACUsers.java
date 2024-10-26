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

package org.owasp.webgoat.lessons.missingac;

import static org.owasp.webgoat.lessons.missingac.MissingFunctionAC.PASSWORD_SALT_ADMIN;
import static org.owasp.webgoat.lessons.missingac.MissingFunctionAC.PASSWORD_SALT_SIMPLE;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.owasp.webgoat.container.CurrentUsername;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/** Created by jason on 1/5/17. */
@Controller
@AllArgsConstructor
@Slf4j
public class MissingFunctionACUsers {

  private final MissingAccessControlUserRepository userRepository;

  @GetMapping(path = {"access-control/users"})
  public ModelAndView listUsers() {

    ModelAndView model = new ModelAndView();
    model.setViewName("list_users");
    List<User> allUsers = userRepository.findAllUsers();
    model.addObject("numUsers", allUsers.size());
    // add display user objects in place of direct users
    List<DisplayUser> displayUsers = new ArrayList<>();
    for (User user : allUsers) {
      displayUsers.add(new DisplayUser(user, PASSWORD_SALT_SIMPLE));
    }
    model.addObject("allUsers", displayUsers);

    return model;
  }

  @GetMapping(
      path = {"access-control/users"},
      consumes = "application/json")
  @ResponseBody
  public ResponseEntity<List<DisplayUser>> usersService() {
    return ResponseEntity.ok(
        userRepository.findAllUsers().stream()
            .map(user -> new DisplayUser(user, PASSWORD_SALT_SIMPLE))
            .collect(Collectors.toList()));
  }

  @GetMapping(
      path = {"access-control/users-admin-fix"},
      consumes = "application/json")
  @ResponseBody
  public ResponseEntity<List<DisplayUser>> usersFixed(@CurrentUsername String username) {
    var currentUser = userRepository.findByUsername(username);
    if (currentUser != null && currentUser.isAdmin()) {
      return ResponseEntity.ok(
          userRepository.findAllUsers().stream()
              .map(user -> new DisplayUser(user, PASSWORD_SALT_ADMIN))
              .collect(Collectors.toList()));
    }
    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
  }

  @PostMapping(
      path = {"access-control/users", "access-control/users-admin-fix"},
      consumes = "application/json",
      produces = "application/json")
  @ResponseBody
  public User addUser(@RequestBody User newUser) {
    try {
      userRepository.save(newUser);
      return newUser;
    } catch (Exception ex) {
      log.error("Error creating new User", ex);
      return null;
    }

    // @RequestMapping(path = {"user/{username}","/"}, method = RequestMethod.DELETE, consumes =
    // "application/json", produces = "application/json")
    // TODO implement delete method with id param and authorization

  }
}
