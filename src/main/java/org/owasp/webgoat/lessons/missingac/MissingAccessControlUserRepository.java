/*
 * SPDX-FileCopyrightText: Copyright © 2021 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.missingac;

import java.util.List;
import org.owasp.webgoat.container.LessonDataSource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class MissingAccessControlUserRepository {

  private final NamedParameterJdbcTemplate jdbcTemplate;
  private final RowMapper<User> mapper =
      (rs, rowNum) ->
          new User(rs.getString("username"), rs.getString("password"), rs.getBoolean("admin"));

  public MissingAccessControlUserRepository(LessonDataSource lessonDataSource) {
    this.jdbcTemplate = new NamedParameterJdbcTemplate(lessonDataSource);
  }

  public List<User> findAllUsers() {
    return jdbcTemplate.query("select username, password, admin from access_control_users", mapper);
  }

  public User findByUsername(String username) {
    var users =
        jdbcTemplate.query(
            "select username, password, admin from access_control_users where username=:username",
            new MapSqlParameterSource().addValue("username", username),
            mapper);
    if (CollectionUtils.isEmpty(users)) {
      return null;
    }
    return users.get(0);
  }

  public User save(User user) {
    jdbcTemplate.update(
        "INSERT INTO access_control_users(username, password, admin)"
            + " VALUES(:username,:password,:admin)",
        new MapSqlParameterSource()
            .addValue("username", user.getUsername())
            .addValue("password", user.getPassword())
            .addValue("admin", user.isAdmin()));
    return user;
  }
}
