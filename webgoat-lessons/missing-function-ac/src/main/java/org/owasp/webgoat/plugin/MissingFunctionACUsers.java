package org.owasp.webgoat.plugin;

import org.owasp.webgoat.users.UserService;
import org.owasp.webgoat.users.WebGoatUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jason on 1/5/17.
 */

@Controller
public class MissingFunctionACUsers {


    // this will actually put controllers on the /WebGoat/* path ... the jsp for list_users restricts what can be seen, but the add_user is not controlled carefully
    @Autowired
    private UserService userService;

    @RequestMapping(path = {"users"}, method = RequestMethod.GET)
    public ModelAndView listUsers(HttpServletRequest request) {

        ModelAndView model = new ModelAndView();
        model.setViewName("list_users");
        List<WebGoatUser> allUsers = userService.getAllUsers();
        model.addObject("numUsers",allUsers.size());
        //add display user objects in place of direct users
        List<DisplayUser> displayUsers = new ArrayList<>();
        for (WebGoatUser user : allUsers) {
            displayUsers.add(new DisplayUser(user));
        }
        model.addObject("allUsers",displayUsers);

        return model;
    }

    @RequestMapping(path = {"users", "/"}, method = RequestMethod.GET,consumes = "application/json")
    @ResponseBody
    public List<DisplayUser> usersService(HttpServletRequest request) {

        List<WebGoatUser> allUsers = userService.getAllUsers();
        List<DisplayUser> displayUsers = new ArrayList<>();
        for (WebGoatUser user : allUsers) {
            displayUsers.add(new DisplayUser(user));
        }
        return displayUsers;
    }

    @RequestMapping(path = {"users","/"}, method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseBody
    //@PreAuthorize()
    public WebGoatUser addUser(@RequestBody WebGoatUser newUser) {
        try {
            userService.addUser(newUser.getUsername(),newUser.getPassword(),newUser.getRole());
            return userService.loadUserByUsername(newUser.getUsername());
        } catch (Exception ex) {
            System.out.println("Error creating new User" + ex.getMessage());
            ex.printStackTrace();
            //TODO: implement error handling ...
        } finally {
            // no streams or other resources opened ... nothing to do, right?
        }
        return null;
    }

    //@RequestMapping(path = {"user/{username}","/"}, method = RequestMethod.DELETE, consumes = "application/json", produces = "application/json")
    //TODO implement delete method with id param and authorization

}
