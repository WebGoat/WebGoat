package org.owasp.webgoat.plugin.mitigation;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.session.DatabaseUtilities;
import org.owasp.webgoat.session.WebSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.*;

/**
 * @author nbaars
 * @since 6/13/17.
 */
@AssignmentPath("SqlInjection/attack12a")
@AssignmentHints(value = {"SqlStringInjectionHint-mitigation-12a-1", "SqlStringInjectionHint-mitigation-12a-2", "SqlStringInjectionHint-mitigation-12a-3", "SqlStringInjectionHint-mitigation-12a-4"})
@Slf4j
public class SqlInjectionLesson12a extends AssignmentEndpoint {

    @Autowired
    private WebSession webSession;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @SneakyThrows
    public AttackResult completed(@RequestParam String ip) {
        Connection connection = DatabaseUtilities.getConnection(webSession);
        PreparedStatement preparedStatement = connection.prepareStatement("select ip from servers where ip = ? and hostname = ?");
        preparedStatement.setString(1, ip);
        preparedStatement.setString(2, "webgoat-prd");
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return trackProgress(success().build());
        }
        return trackProgress(failed().build());
    }
}


