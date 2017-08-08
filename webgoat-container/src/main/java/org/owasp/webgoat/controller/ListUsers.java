package org.owasp.webgoat.controller;

import com.sun.corba.se.spi.activation.EndPointInfo;
import org.owasp.webgoat.assignments.*;
import org.owasp.webgoat.session.UserSessionData;
import org.owasp.webgoat.users.UserService;
import org.owasp.webgoat.users.WebGoatUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jason on 1/5/17.
 */

@Controller
public class ListUsers {

    @Autowired
    private UserService userService;

    @RequestMapping(path = {"list_users", "/"}, method = {RequestMethod.GET,RequestMethod.POST})
    public ModelAndView listUsers(HttpServletRequest request) {

        ModelAndView model = new ModelAndView();
        model.setViewName("list_users");
        List<WebGoatUser> allUsers = userService.getAllUsers();
        model.addObject("numUsers",allUsers.size());
        model.addObject("allUsers",allUsers);

        return model;
    }

}
