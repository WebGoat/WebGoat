/*
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details, please see http://www.owasp.org/
 *
 * Copyright (c) 2002 - 2019 Bruce Mayhew
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * Getting Source ==============
 *
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software projects.
 */

package org.owasp.webgoat.sql_injection.mitigation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.owasp.webgoat.LessonDataSource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @author nbaars
 * @since 6/13/17.
 */
@RestController
@RequestMapping("SqlInjectionMitigations/servers")
@Slf4j
public class Servers {

    private final LessonDataSource dataSource;

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

    public Servers(LessonDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Server> sort(@RequestParam String column) throws Exception {
        List<Server> servers = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("select id, hostname, ip, mac, status, description from servers  where status <> 'out of order' order by " + column)) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Server server = new Server(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6));
                servers.add(server);
            }
        }
        return servers;
    }

}
