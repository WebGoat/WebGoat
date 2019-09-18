//package org.owasp.webgoat.plugin;
//
//import com.google.common.collect.Lists;
//import org.owasp.webgoat.assignments.AssignmentEndpoint;
//import org.owasp.webgoat.assignments.AssignmentHints;
//import org.owasp.webgoat.assignments.AssignmentPath;
//import org.owasp.webgoat.assignments.AttackResult;
//import org.owasp.webgoat.session.UserSessionData;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by jason on 1/5/17.
// */
//
//@AssignmentPath("/IDOR/viewprofile/{id}")
//@AssignmentHints({"idor.hints.otherProfile1","idor.hints.otherProfile2","idor.hints.otherProfile3"})
//public class ViewOtherUserProfile extends AssignmentEndpoint {
//
//    private String color;
//    private String size;
//    private boolean isAdmin;
//
//    @Autowired
//    UserSessionData userSessionData;
//
//    @RequestMapping(produces = {"application/json"})
//    public @ResponseBody
//    AttackResult completed(@PathVariable String userId, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        List json = Lists.newArrayList();
//        // can be re-used
//        Map<String, Object> errorMap = new HashMap();
//        errorMap.put("error","not logged in, go back and log in first");
//
//        if (userSessionData.getValue("idor-authenticated-as") == null) {
//            json.add(errorMap);
//            return trackProgress(failed().feedback("idor.view.other.profile.failure1").build());
//        } else {
//            if (userSessionData.getValue("idor-authenticated-as").equals("bill") || userSessionData.getValue("idor-authenticated-as").equals("tom")) {
//                System.out.println("**** authenticated as " + userSessionData.getValue("idor-authenticated-as"));
//                //logged in
//                String authUserId = (String)userSessionData.getValue("idor-authenticated-user-id");
//                //secure code would check to make sure authUserId matches userId or some similar access control
//                // ... and in this endpoint, we won't bother with that
//                UserProfile userProfile = new UserProfile(userId);
//                return trackProgress(failed().feedback("idor.view.other.profile.failure2").build());
//            }
//        }
//        // else
//        return trackProgress(failed().build());
//    }
//
//
//
//
//}
