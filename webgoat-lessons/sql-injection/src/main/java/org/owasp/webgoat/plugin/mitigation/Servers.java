package org.owasp.webgoat.plugin.mitigation;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import org.owasp.webgoat.session.DatabaseUtilities;
import org.owasp.webgoat.session.WebSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

/**
 * @author nbaars
 * @since 6/13/17.
 */
@RestController
@RequestMapping("SqlInjection/servers")
public class Servers {

    @AllArgsConstructor
    @Getter
    private class Server {

        private String id;
        private String hostname;
        private String ip;
        private String mac;
        private String status;
        private String description;
    }

    @Autowired
    private WebSession webSession;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @SneakyThrows
    @ResponseBody
    public List<Server> sort(@RequestParam String column) {
        Connection connection = DatabaseUtilities.getConnection(webSession);
        PreparedStatement preparedStatement = connection.prepareStatement("select id, hostname, ip, mac, status, description from servers  where status <> 'out of order' order by " + column);
        ResultSet rs = preparedStatement.executeQuery();
        List<Server> servers = Lists.newArrayList();
        while (rs.next()) {
            Server server = new Server(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6));
            servers.add(server);
        }
        return servers;
    }

}
