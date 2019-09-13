package org.owasp.webgoat.plugin.advanced;

import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.session.DatabaseUtilities;
import org.owasp.webgoat.session.WebSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.*;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@AssignmentHints(value ={"SqlInjectionChallengeHint1", "SqlInjectionChallengeHint2", "SqlInjectionChallengeHint3", "SqlInjectionChallengeHint4"})
public class SqlInjectionChallengeLogin extends AssignmentEndpoint {

  @Autowired
  private WebSession webSession;

  @PostMapping("/SqlInjectionAdvanced/challenge_Login")
  @ResponseBody
  public AttackResult login(@RequestParam String username_login, @RequestParam String password_login) throws Exception {
    Connection connection = DatabaseUtilities.getConnection(webSession);
    SqlInjectionChallenge.checkDatabase(connection);

    PreparedStatement statement = connection.prepareStatement("select password from " + SqlInjectionChallenge.USERS_TABLE_NAME + " where userid = ? and password = ?");
    statement.setString(1, username_login);
    statement.setString(2, password_login);
    ResultSet resultSet = statement.executeQuery();

    if (resultSet.next()) {
        return ("tom".equals(username_login)) ? trackProgress(success().build())
        		: success().feedback("ResultsButNotTom").build();
    } else {
      return failed().feedback("NoResultsMatched").build();
    }
  }
}
