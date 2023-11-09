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

package org.owasp.webgoat.webwolf.mailbox;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.owasp.webgoat.webwolf.WebSecurityConfig;
import org.owasp.webgoat.webwolf.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MailboxController.class)
@Import(WebSecurityConfig.class)
public class MailboxControllerTest {

  @Autowired private MockMvc mvc;
  @MockBean private MailboxRepository mailbox;

  @MockBean private ClientRegistrationRepository clientRegistrationRepository;
  @MockBean private UserService userService;
  @Autowired private ObjectMapper objectMapper;

  @JsonIgnoreProperties("time")
  public static class EmailMixIn {}

  @BeforeEach
  public void setup() {
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
    this.mvc
        .perform(
            post("/mail")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(email)))
        .andExpect(status().isCreated());
  }

  @Test
  @WithMockUser(username = "test1234")
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

    this.mvc
        .perform(get("/mail"))
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
  @WithMockUser(username = "test1233")
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

    this.mvc
        .perform(get("/mail"))
        .andExpect(status().isOk())
        .andExpect(view().name("mailbox"))
        .andExpect(content().string(not(containsString("Click this mail"))));
  }
}
