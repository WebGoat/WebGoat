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

package org.owasp.webgoat.challenges.challenge5;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.challenges.Flag;
import org.owasp.webgoat.session.DatabaseUtilities;
import org.owasp.webgoat.session.WebSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.*;

import static org.owasp.webgoat.challenges.SolutionConstants.PASSWORD_TOM;

/**
 * @author nbaars
 * @since 4/8/17.
 */
@RestController
@Slf4j
public class Assignment5 extends AssignmentEndpoint {

    //Make it more random at runtime (good luck guessing)
    private static final String USERS_TABLE_NAME = "challenge_users_" + RandomStringUtils.randomAlphabetic(16);

    @Autowired
    private WebSession webSession;

    @PostMapping("/challenge/5")
    @ResponseBody
    public AttackResult login(@RequestParam String username_login, @RequestParam String password_login) throws Exception {
        Connection connection = DatabaseUtilities.getConnection(webSession);
        checkDatabase(connection);

        if (!StringUtils.hasText(username_login) || !StringUtils.hasText(password_login)) {
            return failed().feedback("required4").build();
        }
        if (!"Larry".equals(username_login)) {
            return failed().feedback("user.not.larry").feedbackArgs(username_login).build();
        }

        PreparedStatement statement = connection.prepareStatement("select password from " + USERS_TABLE_NAME + " where userid = '" + username_login + "' and password = '" + password_login + "'");
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            return success().feedback("challenge.solved").feedbackArgs(Flag.FLAGS.get(5)).build();
        } else {
            return failed().feedback("challenge.close").build();
        }
    }

    private void checkDatabase(Connection connection) throws SQLException {
        try {
            Statement statement = connection.createStatement();
            statement.execute("select 1 from " + USERS_TABLE_NAME);
        } catch (SQLException e) {
            createChallengeTable(connection);
        }
    }

    private void createChallengeTable(Connection connection) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            String dropTable = "DROP TABLE " + USERS_TABLE_NAME;
            statement.executeUpdate(dropTable);
        } catch (SQLException e) {
            log.info("Delete failed, this does not point to an error table might not have been present...");
        }
        log.debug("Challenge 5 - Creating tables for users {}", USERS_TABLE_NAME);
        try {
            String createTableStatement = "CREATE TABLE " + USERS_TABLE_NAME
                    + " (" + "userid varchar(250),"
                    + "email varchar(30),"
                    + "password varchar(30)"
                    + ")";
            statement.executeUpdate(createTableStatement);

            String insertData1 = "INSERT INTO " + USERS_TABLE_NAME + " VALUES ('larry', 'larry@webgoat.org', 'larryknows')";
            String insertData2 = "INSERT INTO " + USERS_TABLE_NAME + " VALUES ('tom', 'tom@webgoat.org', '" + PASSWORD_TOM + "')";
            String insertData3 = "INSERT INTO " + USERS_TABLE_NAME + " VALUES ('alice', 'alice@webgoat.org', 'rt*(KJ()LP())$#**')";
            String insertData4 = "INSERT INTO " + USERS_TABLE_NAME + " VALUES ('eve', 'eve@webgoat.org', '**********')";
            statement.executeUpdate(insertData1);
            statement.executeUpdate(insertData2);
            statement.executeUpdate(insertData3);
            statement.executeUpdate(insertData4);
        } catch (SQLException e) {
            log.error("Unable create table", e);
        }
    }

}

