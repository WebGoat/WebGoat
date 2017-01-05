package org.owasp.webgoat.plugin;


import org.owasp.webgoat.endpoints.AssignmentEndpoint;
import org.owasp.webgoat.endpoints.Endpoint;
import org.owasp.webgoat.lessons.AttackResult;
import org.owasp.webgoat.session.UserSessionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jason on 1/5/17.
 */
@Path("IDOR/profile/alt-path")
public class IDORViewOwnProfileAltUrl extends AssignmentEndpoint{

    @Autowired
    UserSessionData userSessionData;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public AttackResult completed(@RequestParam String url, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String,Object> details = new HashMap<>();
        try {
            if (userSessionData.getValue("idor-authenticated-as").equals("tom")) {
                //going to use session auth to view this one
                String authUserId = userSessionData.getValue("idor-authenticated-user-id");
                //don't care about http://localhost:8080 ... just want WebGoat/
                String[] urlParts = url.split("/");
                System.out.println("************");
                System.out.println(urlParts[0]);
                System.out.println(urlParts[1]);
                System.out.println(urlParts[2]);
                System.out.println(urlParts[3]);
                System.out.println("************");
                if (urlParts[0].equals("WebGoat") && urlParts[1].equals("IDOR") && urlParts[2].equals("profile") && urlParts[3].equals(authUserId)) {
                    UserProfile userProfile = new UserProfile(authUserId);
                    details.put("userId", userProfile.getUserId());
                    details.put("name", userProfile.getName());
                    details.put("color", userProfile.getColor());
                    details.put("size", userProfile.getSize());
                    details.put("admin", userProfile.isAdmin());
                    return AttackResult.success("congratultions, you have used the alternate Url/route to view your own profile.",details.toString());
                }

            } else {
                return AttackResult.failed("please try again. The alternoute route is very similar to the previous way you viewed your profile. Only one difference really");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return AttackResult.failed("fall back");
    }

}
