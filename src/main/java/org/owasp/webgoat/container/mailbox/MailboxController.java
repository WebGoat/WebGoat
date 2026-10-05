/*
 * SPDX-FileCopyrightText: Copyright © 2017 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container.mailbox;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Mailbox used throughout the lessons. It lives in WebGoat (not WebWolf) so the mail a lesson sends
 * is shown in the same look and feel as the rest of WebGoat, reachable through the mailbox button in
 * the top navigation bar.
 */
@RestController
@RequiredArgsConstructor
public class MailboxController {

  private final MailboxRepository mailboxRepository;

  @GetMapping("/mail")
  public ModelAndView mail(Authentication authentication, Model model) {
    String username = (null != authentication) ? authentication.getName() : "anonymous";
    ModelAndView modelAndView = new ModelAndView();
    List<Email> emails = mailboxRepository.findByRecipientOrderByTimeDesc(username);
    if (emails != null && !emails.isEmpty()) {
      modelAndView.addObject("total", emails.size());
      modelAndView.addObject("emails", emails);
      // Opening the mailbox marks everything as read, clearing the unread badge on the button.
      emails.stream().filter(email -> !email.isRead()).forEach(email -> email.setRead(true));
      mailboxRepository.saveAll(emails);
    }
    modelAndView.setViewName("mailbox");
    model.addAttribute("username", username);
    return modelAndView;
  }

  @GetMapping("/mail/count")
  @ResponseBody
  public Map<String, Integer> count(Authentication authentication) {
    String username = (null != authentication) ? authentication.getName() : "anonymous";
    return Map.of("count", mailboxRepository.countByRecipientAndReadFalse(username));
  }

  @PostMapping("/mail")
  @ResponseStatus(HttpStatus.CREATED)
  public void sendEmail(@RequestBody Email email) {
    // time is @JsonIgnore (server-controlled). Stamp the receipt time here: Spring Boot 4 / Jackson
    // 3 deserializes via the all-args constructor, which bypasses the field's default initializer.
    email.setTime(LocalDateTime.now());
    mailboxRepository.save(email);
  }

  @DeleteMapping("/mail")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void deleteAllMail() {
    mailboxRepository.deleteAll();
  }
}
