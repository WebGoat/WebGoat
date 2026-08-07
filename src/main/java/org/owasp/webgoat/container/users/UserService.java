/*
 * SPDX-FileCopyrightText: Copyright © 2017 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container.users;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import org.flywaydb.core.Flyway;
import org.owasp.webgoat.container.lessons.Initializable;
import org.owasp.webgoat.container.mailbox.Email;
import org.owasp.webgoat.container.mailbox.MailboxRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;
  private final UserProgressRepository userTrackerRepository;
  private final JdbcTemplate jdbcTemplate;
  private final Function<String, Flyway> flywayLessons;
  private final List<Initializable> lessonInitializables;
  private final MailboxRepository mailboxRepository;

  @Override
  public WebGoatUser loadUserByUsername(String username) throws UsernameNotFoundException {
    WebGoatUser webGoatUser = userRepository.findByUsername(username);
    if (webGoatUser == null) {
      throw new UsernameNotFoundException("User not found");
    } else {
      webGoatUser.createUser();
      // TODO maybe better to use an event to initialize lessons to keep dependencies low
      lessonInitializables.forEach(l -> l.initialize(webGoatUser));
    }
    return webGoatUser;
  }

  public void addUser(String username, String password) {
    // get user if there exists one by the name
    var userAlreadyExists = userRepository.existsByUsername(username);
    var webGoatUser = userRepository.save(new WebGoatUser(username, password));

    if (!userAlreadyExists) {
      userTrackerRepository.save(
          new UserProgress(username)); // if user previously existed it will not get another tracker
      createLessonsForUser(webGoatUser);
      sendWelcomeEmail(username);
    }
  }

  private void sendWelcomeEmail(String username) {
    Email welcome =
        Email.builder()
            .time(LocalDateTime.now())
            .recipient(username)
            .sender("webgoat@owasp.org")
            .title("Welcome to WebGoat")
            .contents(
                "Hi "
                    + username
                    + ",\n\n"
                    + "Welcome to WebGoat! This is your personal mailbox. Some lessons send you"
                    + " e-mail (for example password reset links), and you will find those messages"
                    + " right here.\n\n"
                    + "Happy hacking,\n"
                    + "The WebGoat team")
            .build();
    mailboxRepository.save(welcome);
  }

  private void createLessonsForUser(WebGoatUser webGoatUser) {
    jdbcTemplate.execute("CREATE SCHEMA \"" + webGoatUser.getUsername() + "\" authorization dba");
    flywayLessons.apply(webGoatUser.getUsername()).migrate();
  }

  public List<WebGoatUser> getAllUsers() {
    return userRepository.findAll();
  }
}
