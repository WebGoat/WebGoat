/*
 * SPDX-FileCopyrightText: Copyright © 2018 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container.mailbox;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.mockito.ArgumentMatchers.anyList;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.owasp.webgoat.container.plugins.LessonTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

class MailboxControllerTest extends LessonTest {

  @MockitoBean private MailboxRepository mailbox;

  // Spring Boot 4's auto-configured mapper is Jackson 3; this test drives the Jackson 2
  // ObjectMapper directly to build the request body, so instantiate it here. Register modules so
  // java.time types (Email#getTimestamp) serialize.
  private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

  @JsonIgnoreProperties("time")
  public static class EmailMixIn {}

  private Authentication user(String username) {
    return UsernamePasswordAuthenticationToken.authenticated(username, "password", List.of());
  }

  @BeforeEach
  public void setupMixIn() {
    objectMapper.addMixIn(Email.class, EmailMixIn.class);
  }

  @Test
  public void sendingMailShouldStoreIt() throws Exception {
    Email email =
        Email.builder()
            .contents("This is a test mail")
            .recipient("test1234@webgoat.org")
            .sender("hacker@webgoat.org")
            .title("Click this mail")
            .time(LocalDateTime.now())
            .build();
    this.mockMvc
        .perform(
            post("/mail")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(email)))
        .andExpect(status().isCreated());
  }

  @Test
  public void userShouldBeAbleToReadOwnEmail() throws Exception {
    Email email =
        Email.builder()
            .contents("This is a test mail")
            .recipient("test1234@webgoat.org")
            .sender("hacker@webgoat.org")
            .title("Click this mail")
            .time(LocalDateTime.now())
            .build();
    Mockito.when(mailbox.findByRecipientOrderByTimeDesc("test1234"))
        .thenReturn(Lists.newArrayList(email));

    this.mockMvc
        .perform(get("/mail").principal(user("test1234")))
        .andExpect(status().isOk())
        .andExpect(view().name("mailbox"))
        .andExpect(content().string(containsString("Click this mail")))
        .andExpect(
            content()
                .string(
                    containsString(
                        DateTimeFormatter.ofPattern("h:mm a").format(email.getTimestamp()))));
  }

  @Test
  public void countShouldReturnNumberOfUnreadEmailsForCurrentUser() throws Exception {
    Mockito.when(mailbox.countByRecipientAndReadFalse("test1234")).thenReturn(1);

    this.mockMvc
        .perform(get("/mail/count").principal(user("test1234")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.count", is(1)));
  }

  @Test
  public void openingMailboxMarksEmailsAsRead() throws Exception {
    Email email =
        Email.builder()
            .contents("This is a test mail")
            .recipient("test1234@webgoat.org")
            .sender("hacker@webgoat.org")
            .title("Click this mail")
            .time(LocalDateTime.now())
            .read(false)
            .build();
    Mockito.when(mailbox.findByRecipientOrderByTimeDesc("test1234"))
        .thenReturn(Lists.newArrayList(email));

    this.mockMvc.perform(get("/mail").principal(user("test1234"))).andExpect(status().isOk());

    // Opening the mailbox flips the unread mail to read and persists it.
    assertThat(email.isRead()).isTrue();
    Mockito.verify(mailbox).saveAll(anyList());
  }

  @Test
  public void differentUserShouldNotBeAbleToReadOwnEmail() throws Exception {
    Email email =
        Email.builder()
            .contents("This is a test mail")
            .recipient("test1234@webgoat.org")
            .sender("hacker@webgoat.org")
            .title("Click this mail")
            .time(LocalDateTime.now())
            .build();
    Mockito.when(mailbox.findByRecipientOrderByTimeDesc("test1234"))
        .thenReturn(Lists.newArrayList(email));

    this.mockMvc
        .perform(get("/mail").principal(user("test1233")))
        .andExpect(status().isOk())
        .andExpect(view().name("mailbox"))
        .andExpect(content().string(not(containsString("Click this mail"))));
  }
}
