package org.owasp.webgoat.plugin;


import org.owasp.webgoat.endpoints.Endpoint;
import org.owasp.webgoat.session.UserSessionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.google.common.collect.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by jason on 1/5/17.
 */
public class IDORViewOwnProfile extends Endpoint{

    @Autowired
    UserSessionData userSessionData;

    @RequestMapping(produces = {"application/json"}, method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> invoke(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String,Object> details = new HashMap<>();
        try {
            if (userSessionData.getValue("idor-authenticated-as").equals("tom")) {
                //going to use session auth to view this one
                String authUserId = userSessionData.getValue("idor-authenticated-user-id");
                UserProfile userProfile = new UserProfile(authUserId);
                details.put("userId",userProfile.getUserId());
                details.put("name",userProfile.getName());
                details.put("color",userProfile.getColor());
                details.put("size",userProfile.getSize());
                details.put("admin",userProfile.isAdmin());

            } else {
                details.put("error","You do not have privileges to view the profile.");
                System.out.println("Not auth'd as tom");
            }
        }catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return details;
    }

    @Override
    public String getPath() {
        return "/IDOR/profile";
    }
}
