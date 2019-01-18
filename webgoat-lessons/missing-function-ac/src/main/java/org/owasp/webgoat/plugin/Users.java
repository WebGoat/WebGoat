package org.owasp.webgoat.plugin;

import org.owasp.webgoat.assignments.Endpoint;
import org.owasp.webgoat.session.DatabaseUtilities;
import org.owasp.webgoat.session.UserSessionData;
import org.owasp.webgoat.session.WebSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.sql.*;
import java.util.HashMap;

public class Users extends Endpoint{

    @Autowired
    private WebSession webSession;

    @Autowired
    UserSessionData userSessionData;

    @RequestMapping(produces = {"application/json"}, method = RequestMethod.GET)
    @ResponseBody
    protected HashMap<Integer, HashMap> getUsers  (HttpServletRequest req) {

        try {
            Connection connection = DatabaseUtilities.getConnection(getWebSession());
            String query = "SELECT * FROM user_data";

            try {
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                ResultSet results = statement.executeQuery(query);
                HashMap<Integer,HashMap> allUsersMap = new HashMap();

                if ((results != null) && (results.first() == true)) {
                    ResultSetMetaData resultsMetaData = results.getMetaData();
                    StringBuffer output = new StringBuffer();

                    while (results.next()) {
                        int id = results.getInt(0);
                        HashMap<String,String> userMap = new HashMap<>();
                        userMap.put("first", results.getString(1));
                        userMap.put("last", results.getString(2));
                        userMap.put("cc", results.getString(3));
                        userMap.put("ccType", results.getString(4));
                        userMap.put("cookie", results.getString(5));
                        userMap.put("loginCount",Integer.toString(results.getInt(6)));
                        allUsersMap.put(id,userMap);
                    }
                    userSessionData.setValue("allUsers",allUsersMap);
                    return allUsersMap;

                }
            } catch (SQLException sqle) {
                sqle.printStackTrace();
                HashMap<String,String> errMap = new HashMap() {{
                    put("err",sqle.getErrorCode() + "::" + sqle.getMessage());
                }};

                return new HashMap<Integer,HashMap>() {{
                    put(0,errMap);
                }};
            } catch (Exception e) {
                e.printStackTrace();
                HashMap<String,String> errMap = new HashMap() {{
                    put("err",e.getMessage() + "::" + e.getCause());
                }};
                e.printStackTrace();
                return new HashMap<Integer,HashMap>() {{
                    put(0,errMap);
                }};


            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            HashMap<String,String> errMap = new HashMap() {{
                put("err",e.getMessage() + "::" + e.getCause());
            }};
            e.printStackTrace();
            return new HashMap<Integer,HashMap>() {{
                put(0,errMap);
            }};

        }
        return null;
    }

    protected  WebSession getWebSession() {
        return webSession;
    }

    @Override
    public String getPath() {
        return  "/access-control/list-users";
    }
}
