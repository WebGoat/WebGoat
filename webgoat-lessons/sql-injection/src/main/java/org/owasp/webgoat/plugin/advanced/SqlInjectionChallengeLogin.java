package org.owasp.webgoat.plugin.advanced;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.session.DatabaseUtilities;
import org.owasp.webgoat.session.WebSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.*;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@AssignmentPath("SqlInjection/challenge_Login")
@Slf4j
@AssignmentHints(value ={"SqlInjectionChallengeHint1", "SqlInjectionChallengeHint2", "SqlInjectionChallengeHint3", "SqlInjectionChallengeHint4"})
public class SqlInjectionChallengeLogin extends AssignmentEndpoint {

  @Autowired
  private WebSession webSession;


  @RequestMapping(method = POST)
  @ResponseBody
  public AttackResult login(@RequestParam String username_login, @RequestParam String password_login) throws Exception {
    System.out.println("right Method");
    Connection connection = DatabaseUtilities.getConnection(webSession);
    SqlInjectionChallenge.checkDatabase(connection);

    PreparedStatement statement = connection.prepareStatement("select password from " + SqlInjectionChallenge.USERS_TABLE_NAME + " where userid = ? and password = ?");
    statement.setString(1, username_login);
    statement.setString(2, password_login);
    ResultSet resultSet = statement.executeQuery();

    if (resultSet.next()) {
        return ("tom".equals(username_login)) ? success().build()
                : success().feedback("ResultsButNotTom").build();
    } else {
      return failed().feedback("NoResultsMatched").build();
    }
  }
}
